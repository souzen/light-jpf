package ljpf.examples.plugin;

import ljpf.Plugin;
import ljpf.PluginConfig;
import ljpf.examples.common.ExampleConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.SpringVersion;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Created by souzen on 29.03.2017.
 */
public class SecondPlugin implements Plugin {

    private static final Logger LOG = LoggerFactory.getLogger(SecondPlugin.class.getSimpleName());

    private ExampleConfig config;

    @Override
    public void init(PluginConfig config) {
        if (config instanceof ExampleConfig) {
            this.config = (ExampleConfig) config;
        }
    }

    @Override
    public void load() {
        LOG.info("Load [classloader {}]", this.getClass().getClassLoader());
        LOG.info("Spring version {}", SpringVersion.getVersion());
        LOG.info("Hello {}!", config.getValue());

        Properties properties = new Properties();
        try {
            Path path = Paths.get(config.getPluginWorkDir(), "additional.properties");
            FileInputStream fileInputStream = new FileInputStream(path.toFile());
            properties.load(fileInputStream);

            LOG.info("Hello {}!", properties.getProperty("hello.property"));
        } catch (IOException e) {
            LOG.error("Failed to load properties", e);
        }
    }

    @Override
    public void unload() {
        LOG.info("Unload");
    }
}
