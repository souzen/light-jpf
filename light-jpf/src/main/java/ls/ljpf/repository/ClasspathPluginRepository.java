package ls.ljpf.repository;

import com.google.common.collect.Lists;
import ls.ljpf.PluginClasspath;
import ls.ljpf.PluginDescriptor;
import ls.ljpf.PluginRepository;
import ls.ljpf.PluginRepositoryEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

/**
 * Created by souzen on 25.03.2017.
 */
public class ClasspathPluginRepository extends BasePluginRepository implements PluginRepository {

    private static final Logger LOG = LoggerFactory.getLogger(ClasspathPluginRepository.class.getSimpleName());

    private static final String CLASSPATH_ATTR_NAME = "Class-Path";

    private String pluginJarExt;

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

            // extract classpath dirs if it was packed to jar file
            classpathUris.addAll(loadPackedClasspathDirs(classpathUris).collect(toList()));

            // Load other project dependencies from classes dir
            classpathUris.stream()
                    .filter(f -> f.isDirectory())
                    .flatMap(this::loadDirEntries)
                    .forEach(this::addEntry);

            // Load jar file dependencies from classpath
            classpathUris.stream()
                    .filter(f -> f.isFile() && Pattern.matches(this.pluginJarExt, f.getPath()))
                    .flatMap(this::loadJarEntries)
                    .forEach(this::addEntry);
        }
    }

    private Stream<File> loadPackedClasspathDirs(List<File> dirs) {
        try {
            // This is performed in order to load compressed intellij classpath

            for (int i = 0; i < dirs.size(); i++) {
                File file = dirs.get(i);
                final Path path = file.toPath();

                if (!Files.isReadable(path))
                    continue;

                JarInputStream jarStream = new JarInputStream(Files.newInputStream(path));
                Manifest mf = jarStream.getManifest();

                if (mf == null || mf.getMainAttributes().getValue(CLASSPATH_ATTR_NAME) == null) {
                    continue;
                }

                String classpath = mf.getMainAttributes().getValue(CLASSPATH_ATTR_NAME);

                return Stream.of(classpath.split("file:")).map(dir -> new File(dir.trim()));
            }

        } catch (IOException e) {
            // NO-OP
            LOG.debug("Couldn't load packed classpath dirs {}", e.getMessage());
        }

        return Stream.empty();
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
