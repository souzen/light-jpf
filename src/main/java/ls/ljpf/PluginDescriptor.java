package ls.ljpf;

import java.util.Properties;

/**
 * Created by souzen on 25.03.2017.
 */
public class PluginDescriptor {

    public static final String FILE_EXT = ".*plugin";

    private final String id;
    private final String pluginClass;

    public PluginDescriptor(String id, String pluginClass) {
        this.id = id;
        this.pluginClass = pluginClass;
    }

    public String getId() {
        return id;
    }

    public String getPluginClass() {
        return pluginClass;
    }

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
