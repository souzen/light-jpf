package ls.ljpf.examples.app;

import ls.ljpf.PluginClassLoaderFactory;
import ls.ljpf.PluginManager;
import ls.ljpf.PluginRepository;
import ls.ljpf.examples.common.ExampleConfig;
import ls.ljpf.impl.DefaultPluginManager;
import ls.ljpf.loader.ParentLastClassLoaderFactory;
import ls.ljpf.repository.ClasspathPluginRepository;

/**
 * Created by souzen on 29.03.2017.
 */
public class App {

    private PluginManager pluginManager;

    public void init() {
        PluginRepository pluginRepository = new ClasspathPluginRepository(".*ls.ljpf.examples.*jar");
        PluginClassLoaderFactory classLoaderFactory = new ParentLastClassLoaderFactory();

        pluginManager = new DefaultPluginManager(pluginRepository, classLoaderFactory);

        ExampleConfig appConfig = new ExampleConfig();
        appConfig.setValue("Hello");

        pluginManager.load("FirstPlugin", appConfig);
        pluginManager.load("SecondPlugin", appConfig);

    }

    public void shutdown() {
        pluginManager.unloadAll();
    }

    public static void main(String[] args) {
        App app = new App();
        app.init();
        app.shutdown();
    }

}
