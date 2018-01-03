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

import ljpf.PluginDescriptor;
import ljpf.PluginException;
import ljpf.versions.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

/**
 * Created by souzen on 25.03.2017.
 */
public class PluginDescriptorParser {

    private static final Logger LOG = LoggerFactory.getLogger(PluginDescriptorParser.class.getSimpleName());

    public static final String FILE_EXTENSION = "plugin";
    public static final String PLUGIN_LIB_DIR = "lib";

    public static boolean valid(final Properties properties) {
        return properties != null
                && !properties.isEmpty()
                && properties.containsKey("id")
                && properties.containsKey("pluginClass");
    }

    public static PluginDescriptor parse(final Properties properties) {
        String id = properties.getProperty("id");
        String versionProperty = properties.getProperty("version");
        String pluginClass = properties.getProperty("pluginClass");
        String description = properties.getProperty("description");

        if (Objects.isNull(id) || Objects.isNull(versionProperty) || Objects.isNull(pluginClass))
            throw new PluginException(id, "Invalid descriptor file");

        Version version = Version.parse(versionProperty);

        return new PluginDescriptor(id, version, pluginClass, description);
    }

    public static Properties parseDescriptorFile(final File file) {
        Properties properties = new Properties();

        try {
            final InputStream inputStream = new FileInputStream(file);
            properties.load(inputStream);

        } catch (IOException e) {
            LOG.warn("Could not parse plugin descriptor {}", file.getAbsolutePath());
        }

        return properties;
    }

    public static Properties parseDescriptorFile(final JarFile jar, final JarEntry jarEntry) {
        Properties properties = new Properties();

        try {
            final InputStream inputStream = jar.getInputStream(jarEntry);
            properties.load(inputStream);

        } catch (IOException e) {
            LOG.warn("Could not parse plugin descriptor {}", jar.getName());
        }

        return properties;
    }

    public static boolean matchesDescriptorExtension(String filename) {
        return Pattern.matches(".*" + FILE_EXTENSION, filename);
    }

}
