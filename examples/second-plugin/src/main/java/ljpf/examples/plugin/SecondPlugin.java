package ljpf.examples.plugin;

import ljpf.Plugin;
import ljpf.PluginConfig;
import ljpf.examples.common.ExampleConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.SpringVersion;

/**
 * Created by souzen on 29.03.2017.
 */
public class SecondPlugin implements Plugin {

    private static final Logger LOG = LoggerFactory.getLogger(SecondPlugin.class.getSimpleName());

    @Override
    public void init(PluginConfig config) {
        if(config instanceof ExampleConfig) {
            ExampleConfig exampleConfig = (ExampleConfig) config;
            LOG.info("Example config: {} world!", exampleConfig.getValue());
        }
    }

    @Override
    public void load() {
        LOG.info("Load [classloader {}]", this.getClass().getClassLoader());
        LOG.info("Spring version {}", SpringVersion.getVersion());
    }

    @Override
    public void unload() {
        LOG.info("Unload");
    }
}
