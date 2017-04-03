package ljpf.examples.app;

import ljpf.PluginClassLoaderFactory;
import ljpf.PluginManager;
import ljpf.examples.common.ExampleConfig;
import ljpf.impl.DefaultPluginManager;
import ljpf.loader.ParentLastClassLoaderFactory;
import ljpf.repository.ClasspathPluginRepository;
import ljpf.repository.DirPluginRepository;
import ljpf.repository.MultiPluginRepository;
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
        appConfig.setValue("World");

        pluginManager.load("FirstPlugin", appConfig);
        pluginManager.load("SecondPlugin", appConfig);
        pluginManager.load("ThirdPlugin", appConfig);

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
