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

package ljpf.examples.app;

import ljpf.PluginClassLoaderFactory;
import ljpf.PluginManager;
import ljpf.examples.common.ExampleConfig;
import ljpf.DefaultPluginManager;
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
        LOG.info("Initializing...");

        MultiPluginRepository pluginRepository = new MultiPluginRepository(
                new ClasspathPluginRepository(),
                new DirPluginRepository("plugins"),
                new DirPluginRepository("examples/app/target/plugins")
        );

        PluginClassLoaderFactory classLoaderFactory = new ParentLastClassLoaderFactory();

        pluginManager = new DefaultPluginManager(pluginRepository, classLoaderFactory);

        ExampleConfig config = new ExampleConfig();
        config.setValue("World");

        pluginManager.load("FirstPlugin", config);
        pluginManager.load("SecondPlugin", config);
        pluginManager.load("ThirdPlugin", config);
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
