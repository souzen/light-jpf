package ls.ljpf;

import java.util.Collection;

/**
 * Created by souzen on 25.03.2017.
 */
public class PluginClasspath {

    private final Collection<String> dirs;
    private final Collection<String> jars;
    private final Collection<String> resources;

    public PluginClasspath(Collection<String> dirs, Collection<String> jars, Collection<String> resources) {
        this.dirs = dirs;
        this.jars = jars;
        this.resources = resources;
    }

    public Collection<String> getDirs() {
        return dirs;
    }

    public Collection<String> getJars() {
        return jars;
    }

    public Collection<String> getResources() {
        return resources;
    }

}
