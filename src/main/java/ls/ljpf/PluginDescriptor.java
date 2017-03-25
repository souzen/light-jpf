package ls.ljpf;

/**
 * Created by souzen on 25.03.2017.
 */
public class PluginDescriptor {

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
}
