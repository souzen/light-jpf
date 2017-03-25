package ls.ljpf;

/**
 * Created by souzen on 25.03.2017.
 */
public class PluginRepositoryEntry {

    private final PluginDescriptor descriptor;
    private final PluginClasspath classpath;

    public PluginRepositoryEntry(PluginDescriptor descriptor, PluginClasspath classpath) {
        this.descriptor = descriptor;
        this.classpath = classpath;
    }

    public PluginDescriptor getDescriptor() {
        return descriptor;
    }

    public PluginClasspath getClasspath() {
        return classpath;
    }

}