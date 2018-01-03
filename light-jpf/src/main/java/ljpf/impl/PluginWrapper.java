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

package ljpf.impl;

import ljpf.*;

import java.lang.reflect.Constructor;

/**
 * Created by souzen on 25.03.2017.
 */
public class PluginWrapper {

    private enum State {
        CREATED,
        INITIALIZED,
        LOADED,
        UNLOADED;
    }

    private Plugin plugin;
    private State state;
    private final PluginDescriptor descriptor;
    private final PluginClasspath classpath;
    private final ClassLoader classLoader;

    public PluginWrapper(PluginDescriptor descriptor, PluginClasspath classpath, ClassLoader classLoader) {
        this.state = State.CREATED;
        this.descriptor = descriptor;
        this.classpath = classpath;
        this.classLoader = classLoader;
    }

    public PluginDescriptor getDescriptor() {
        return descriptor;
    }

    public void init(final PluginConfig config) {
        if (state != State.CREATED)
            throw new PluginException(descriptor.getId(), String.format("Invalid Plugin State - %s", state));

        try {
            final Class<?> pluginClass = classLoader.loadClass(descriptor.getPluginClass());

            final Constructor<?> constructor = pluginClass.getConstructor();
            constructor.setAccessible(true);

            plugin = (Plugin) constructor.newInstance();

            config.setPluginWorkDir(classpath.getPath());
            plugin.init(config);

            state = State.INITIALIZED;

        } catch (Throwable t) {
            throw new PluginException(descriptor.getId(), "Plugin Init Failed", t);
        }
    }

    public void load() {
        if (state != State.INITIALIZED)
            throw new PluginException(descriptor.getId(), String.format("Invalid Plugin State - %s", state));

        try {
            plugin.load();

            state = State.LOADED;

        } catch (Throwable t) {
            throw new PluginException(descriptor.getId(), "Plugin Load Failed", t);
        }
    }

    public void unload() {
        if (state != State.LOADED)
            throw new PluginException(descriptor.getId(), String.format("Invalid Plugin State - %s", state));

        try {
            plugin.unload();

            state = State.UNLOADED;

        } catch (Throwable t) {
            throw new PluginException(descriptor.getId(), "Plugin Unload Failed", t);
        }
    }
}
