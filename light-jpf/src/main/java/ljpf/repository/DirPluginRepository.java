/*
 *    Copyright 2017 Luke Sosnicki
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ljpf.repository;

import ljpf.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static ljpf.repository.PluginDescriptorParser.matchesDescriptorExtension;

/**
 * Created by souzen on 25.03.2017.
 */
public class DirPluginRepository extends BasePluginRepository implements PluginRepository {

    private static final Logger LOG = LoggerFactory.getLogger(DirPluginRepository.class.getSimpleName());

    private final String path;

    public DirPluginRepository(String path) {
        this(new File(path));
    }

    public DirPluginRepository(final File file) {
        try {
            this.path = file.getCanonicalPath();

        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to load dir plugin repository", e);
        }

        File dir = Paths.get(path).toFile();

        if (!dir.exists() || !dir.isDirectory()) {
            LOG.warn("Dir Plugin Repository not found {}", dir.getAbsolutePath());
            return;
        }

        LOG.debug("Loading plugins from directory: " + file.getAbsolutePath());

        final File[] files = dir.listFiles();

        // Load descriptor directly from directory
        stream(files)
                .filter(f -> f.isDirectory())
                .flatMap(this::loadExternalDirEntries)
                .forEach(this::addEntry);
    }

    private Stream<PluginRepositoryEntry> loadExternalDirEntries(File file) {
        return stream(file.listFiles())
                .filter(f -> matchesDescriptorExtension(f.getName()))
                .map(PluginDescriptorParser::parseDescriptorFile)
                .filter(PluginDescriptorParser::valid)
                .map(PluginDescriptorParser::parse)
                .map(descriptor -> new PluginRepositoryEntry(descriptor, getPluginClasspath(file)));
    }

    private PluginClasspath getPluginClasspath(File file) {
        List<String> dirs = new ArrayList<>();
        List<String> jars = new ArrayList<>();
        List<String> resources = new ArrayList<>();

        final File dependencies = Paths.get(file.getPath(), PluginDescriptorParser.PLUGIN_LIB_DIR).toFile();

        final File[] dependencyFiles = dependencies.listFiles();

        if (dependencyFiles != null) {
            for (File libFile : dependencyFiles) {
                if (libFile.isDirectory()) {
                    dirs.add(libFile.getPath());
                } else {
                    jars.add(libFile.getPath());
                }
            }
        }

        resources.add(file.getPath());

        return new PluginClasspath(file.getPath(), dirs, jars, resources);
    }

}
