package ls.ljpf;

import java.util.Collection;

/**
 * Created by souzen on 25.03.2017.
 */
public interface PluginRepository {

    boolean containsPlugin(final String id);

    PluginRepositoryEntry getPlugin(final String id);

    Collection<PluginRepositoryEntry> getPlugins();

}
