package ljpf.examples.plugin;

import ljpf.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.SpringVersion;

/**
 * Created by souzen on 29.03.2017.
 */
public class FirstPlugin implements Plugin {

    private static final Logger LOG = LoggerFactory.getLogger(FirstPlugin.class.getSimpleName());

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
