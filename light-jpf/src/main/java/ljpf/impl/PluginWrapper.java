package ljpf.impl;

import ljpf.*;

import java.lang.reflect.Constructor;

/**
 * Created by souzen on 25.03.2017.
 */
class PluginWrapper {

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

    PluginWrapper(PluginDescriptor descriptor, PluginClasspath classpath, ClassLoader classLoader) {
        this.state = State.CREATED;
        this.descriptor = descriptor;
        this.classpath = classpath;
        this.classLoader = classLoader;
    }

    public PluginDescriptor getDescriptor() {
        return descriptor;
    }

    void init(final PluginConfig config) {
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

    void load() {
        if (state != State.INITIALIZED)
            throw new PluginException(descriptor.getId(), String.format("Invalid Plugin State - %s", state));

        try {
            plugin.load();

            state = State.LOADED;

        } catch (Throwable t) {
            throw new PluginException(descriptor.getId(), "Plugin Load Failed", t);
        }
    }

    void unload() {
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
