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

import com.google.common.collect.Lists;
import ljpf.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarFile;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

/**
 * Created by souzen on 25.03.2017.
 */
public class ClasspathPluginRepository extends BasePluginRepository implements PluginRepository {

    private static final Logger LOG = LoggerFactory.getLogger(ClasspathPluginRepository.class.getSimpleName());

    private String pluginJarExt;

    public ClasspathPluginRepository() {
        this(null);
    }

    public ClasspathPluginRepository(String pluginJarExt) {
        this.pluginJarExt = pluginJarExt;
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();

        if ((systemClassLoader instanceof URLClassLoader)) {

            URLClassLoader cl = (URLClassLoader) systemClassLoader;

            List<File> classpathUris = Lists.newArrayList();
            for (URL url : cl.getURLs()) {
                try {
                    final URI uri = url.toURI();
                    classpathUris.add(new File(uri));
                } catch (URISyntaxException e) {
                    LOG.warn("Could not parse classpath element", e);
                }
            }

            // Load other project dependencies from classes dir
            classpathUris.stream()
                    .filter(f -> f.isDirectory())
                    .flatMap(this::loadDirEntries)
                    .forEach(this::addEntry);

            // Load jar file dependencies from classpath
            classpathUris.stream()
                    .filter(f -> f.isFile() && this.pluginJarExt != null && Pattern.matches(this.pluginJarExt, f.getPath()))
                    .flatMap(this::loadJarEntries)
                    .forEach(this::addEntry);
        }
    }

    private Stream<PluginRepositoryEntry> loadDirEntries(File file) {
        return getDirDescriptors(file).map(descriptor -> new PluginRepositoryEntry(descriptor, new PluginClasspath(
                file.getPath(),
                Lists.newArrayList(file.getPath()),
                Collections.EMPTY_LIST,
                Collections.EMPTY_LIST)));
    }

    private Stream<PluginRepositoryEntry> loadJarEntries(File file) {
        return getJarDescriptors(file).map(descriptor -> new PluginRepositoryEntry(descriptor, new PluginClasspath(
                file.getPath(),
                Collections.EMPTY_LIST,
                Lists.newArrayList(file.getPath()),
                Collections.EMPTY_LIST)));
    }

    private Stream<PluginDescriptor> getDirDescriptors(File file) {
        return stream(file.listFiles())
                .filter(f -> matchesDescriptorExtension(f.getName()))
                .map(this::parseDescriptorFile)
                .filter(PluginDescriptorParser::valid)
                .map(PluginDescriptorParser::parse);
    }

    private Stream<PluginDescriptor> getJarDescriptors(File file) {
        Stream<PluginDescriptor> result = Stream.empty();

        try {
            final JarFile jar = new JarFile(file);

            result = jar.stream()
                    .filter(jarEntry -> matchesDescriptorExtension(jarEntry.getName()))
                    .map(jarEntry -> parseDescriptorFile(jar, jarEntry))
                    .filter(PluginDescriptorParser::valid)
                    .map(PluginDescriptorParser::parse);

        } catch (IOException e) {
            LOG.warn("Could not parse plugin descriptor {}", file.getAbsolutePath());
        }

        return result;
    }

}
