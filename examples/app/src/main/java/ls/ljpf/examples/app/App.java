package ls.ljpf.examples.app;

import ls.ljpf.PluginManager;
import ls.ljpf.PluginRepository;
import ls.ljpf.impl.DefaultPluginManager;
import ls.ljpf.repository.ClasspathPluginRepository;

/**
 * Created by souzen on 29.03.2017.
 */
public class App {

    public void init() {
        PluginRepository pluginRepository = new ClasspathPluginRepository(".*ls.ljpf.examples.*jar");

        PluginManager pluginManager = new DefaultPluginManager(pluginRepository);
        pluginManager.load("FirstPlugin");
        pluginManager.load("SecondPlugin");
        pluginManager.unloadAll();
    }

    public static void main(String[] args) {
        App app = new App();
        app.init();
    }

}
