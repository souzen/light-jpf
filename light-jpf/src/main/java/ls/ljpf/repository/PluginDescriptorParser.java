package ls.ljpf.repository;

import ls.ljpf.PluginDescriptor;
import ls.ljpf.PluginException;

import java.util.Properties;

/**
 * Created by souzen on 25.03.2017.
 */
public class PluginDescriptorParser {

    public static final String FILE_EXT = ".*plugin";

    public static boolean valid(final Properties properties) {
        return properties != null
                && !properties.isEmpty()
                && properties.containsKey("id")
                && properties.containsKey("pluginClass");
    }

    public static PluginDescriptor parse(final Properties properties) {
        String id = properties.getProperty("id");
        String pluginClass = properties.getProperty("pluginClass");

        if (null == id || null == pluginClass)
            throw new PluginException(id, "Failed to parse plugin");

        return new PluginDescriptor(
                id,
                pluginClass);
    }

}
