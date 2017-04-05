package ljpf.repository;

import com.google.common.collect.Lists;
import ljpf.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

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
                .map(this::parseDescriptorFile)
                .filter(PluginDescriptorParser::valid)
                .map(PluginDescriptorParser::parse)
                .map(descriptor -> new PluginRepositoryEntry(descriptor, getPluginClasspath(file)));
    }

    private PluginClasspath getPluginClasspath(File file) {
        List<String> dirs = Lists.newArrayList();
        List<String> jars = Lists.newArrayList();
        List<String> resources = Lists.newArrayList();

        final File dependencies = Paths.get(file.getPath(), PluginDescriptor.PLUGIN_LIB_DIR).toFile();

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
