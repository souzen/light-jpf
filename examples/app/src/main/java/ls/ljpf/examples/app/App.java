package ls.ljpf.examples.app;

import ls.ljpf.PluginManager;
import ls.ljpf.PluginRepository;
import ls.ljpf.examples.common.ExampleConfig;
import ls.ljpf.impl.DefaultPluginManager;
import ls.ljpf.repository.ClasspathPluginRepository;

/**
 * Created by souzen on 29.03.2017.
 */
public class App {

    private PluginManager pluginManager;

    public void init() {
        PluginRepository pluginRepository = new ClasspathPluginRepository(".*ls.ljpf.examples.*jar");

        ExampleConfig appConfig = new ExampleConfig();
        appConfig.setValue("Hello");

        pluginManager = new DefaultPluginManager(pluginRepository);
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
