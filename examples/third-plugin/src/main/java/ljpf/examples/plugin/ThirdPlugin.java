/*
 *    Copyright 2017 Luke Sosnicki
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ljpf.examples.plugin;

import ljpf.Plugin;
import ljpf.PluginConfig;
import ljpf.examples.common.ExampleConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Created by souzen on 29.03.2017.
 */
public class ThirdPlugin implements Plugin {

    private static final Logger LOG = LoggerFactory.getLogger(ThirdPlugin.class.getSimpleName());

    private ExampleConfig config;

    @Override
    public void init(PluginConfig config) {
        if (config instanceof ExampleConfig) {
            this.config = (ExampleConfig) config;
        }
    }

    @Override
    public void load() {
        LOG.debug("Load [classloader {}]", this.getClass().getClassLoader());

        // Greetings from application
        LOG.info("Hello {}!", config.getValue());

        // Greetings from plugin local resource
        Properties properties = new Properties();
        try {
            Path path = Paths.get(config.getPluginWorkDir(), "additional.properties");
            properties.load(new FileInputStream(path.toFile()));

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
