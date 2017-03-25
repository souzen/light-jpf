package ls.ljpf;

/**
 * Created by souzen on 25.03.2017.
 */
public class PluginRepositoryEntry {

    private final String path;
    private final PluginDescriptor pluginDescriptor;
    private final PluginClasspath pluginClasspath;

    public PluginRepositoryEntry(String path, PluginDescriptor pluginDescriptor, PluginClasspath pluginClasspath) {
        this.path = path;
        this.pluginDescriptor = pluginDescriptor;
        this.pluginClasspath = pluginClasspath;
    }

    public String getPath() {
        return path;
    }

    public PluginDescriptor getPluginDescriptor() {
        return pluginDescriptor;
    }

    public PluginClasspath getPluginClasspath() {
        return pluginClasspath;
    }

}