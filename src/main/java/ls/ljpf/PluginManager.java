package ls.ljpf;

import java.util.Collection;

/**
 * Created by souzen on 25.03.2017.
 */
public interface PluginManager {

    Collection<PluginDescriptor> getAvailablePlugins();

    Collection<PluginDescriptor> getLoadedPlugins();

    PluginDescriptor getAvailablePlugin(final String id);

    PluginDescriptor getLoadedPlugin(final String id);

    void load(final String id);

    void load(final String id, final PluginConfig config);

    void unload(final String id);

    void unloadAll();

}
