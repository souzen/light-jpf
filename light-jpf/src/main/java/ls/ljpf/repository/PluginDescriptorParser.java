package ls.ljpf.repository;

import ls.ljpf.PluginDescriptor;
import ls.ljpf.PluginException;
import versions.Version;

import java.util.Objects;
import java.util.Properties;

/**
 * Created by souzen on 25.03.2017.
 */
public class PluginDescriptorParser {

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

}
