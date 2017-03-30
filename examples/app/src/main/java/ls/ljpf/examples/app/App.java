package ls.ljpf.examples.app;

import ls.ljpf.PluginClassLoaderFactory;
import ls.ljpf.PluginManager;
import ls.ljpf.examples.common.ExampleConfig;
import ls.ljpf.impl.DefaultPluginManager;
import ls.ljpf.loader.ParentLastClassLoaderFactory;
import ls.ljpf.repository.ClasspathPluginRepository;
import ls.ljpf.repository.DirPluginRepository;
import ls.ljpf.repository.MultiPluginRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by souzen on 29.03.2017.
 */
public class App {

    private static final Logger LOG = LoggerFactory.getLogger(App.class.getSimpleName());

    private PluginManager pluginManager;

    public void init() {
        LOG.info("Initializing... {}");

        MultiPluginRepository pluginRepository = new MultiPluginRepository();
        pluginRepository.addRepository(new ClasspathPluginRepository(".*ls.ljpf.examples.*jar"));
        pluginRepository.addRepository(new DirPluginRepository("plugins"));

        PluginClassLoaderFactory classLoaderFactory = new ParentLastClassLoaderFactory();

        pluginManager = new DefaultPluginManager(pluginRepository, classLoaderFactory);

        ExampleConfig appConfig = new ExampleConfig();
        appConfig.setValue("Hello");

        pluginManager.load("FirstPlugin", appConfig);
        pluginManager.load("SecondPlugin", appConfig);

    }

    public void shutdown() {
        LOG.info("Shutting down...");
        pluginManager.unloadAll();
    }

    public static void main(String[] args) {
        App app = new App();
        app.init();
        app.shutdown();
    }

}
