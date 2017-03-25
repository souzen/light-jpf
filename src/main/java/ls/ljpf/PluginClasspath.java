package ls.ljpf;

import java.util.Collection;

/**
 * Created by souzen on 25.03.2017.
 */
public class PluginClasspath {

    private final String path;
    private final Collection<String> dirs;
    private final Collection<String> jars;
    private final Collection<String> resources;

    public PluginClasspath(String path, Collection<String> dirs, Collection<String> jars, Collection<String> resources) {
        this.path = path;
        this.dirs = dirs;
        this.jars = jars;
        this.resources = resources;
    }

    public String getPath() {
        return path;
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
