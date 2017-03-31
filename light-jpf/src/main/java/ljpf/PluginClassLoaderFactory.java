package ljpf;

/**
 * Created by souzen on 25.03.2017.
 */
public interface PluginClassLoaderFactory {

    ClassLoader createClassLoader(final PluginClasspath pluginClasspath);

}
