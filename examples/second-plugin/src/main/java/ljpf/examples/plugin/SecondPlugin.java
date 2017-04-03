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

    @Override
    public void load() {
        LOG.debug("Load [classloader {}]", getClass().getClassLoader());
        LOG.info("Spring version {}", SpringVersion.getVersion());
    }

    @Override
    public void unload() {
        LOG.info("Unload");
    }
}
