package ljpf.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import ljpf.*;
import ljpf.loader.ParentLastClassLoaderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * Created by souzen on 25.03.2017.
 */
public class DefaultPluginManager implements PluginManager {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultPluginManager.class.getSimpleName());

    private final PluginRepository repository;
    private final PluginClassLoaderFactory pluginClassLoaderFactory;
    private final Map<String, PluginWrapper> pluginWrappers;
    private final Collection<PluginWrapper> loadedPlugins;

    public DefaultPluginManager(PluginRepository repository) {
        this(repository, new ParentLastClassLoaderFactory());
    }

    public DefaultPluginManager(PluginRepository repository, PluginClassLoaderFactory pluginClassLoaderFactory) {
        this.repository = repository;
        this.pluginClassLoaderFactory = pluginClassLoaderFactory;
        this.pluginWrappers = Collections.synchronizedMap(Maps.newLinkedHashMap());
        this.loadedPlugins = Lists.newCopyOnWriteArrayList();
    }

    @Override
    public Collection<PluginDescriptor> getAvailablePlugins() {
        return repository.getPlugins().stream().map(PluginRepositoryEntry::getDescriptor).collect(toList());
    }

    @Override
    public Collection<PluginDescriptor> getLoadedPlugins() {
        return loadedPlugins.stream().map(PluginWrapper::getDescriptor).collect(toList());
    }

    @Override
    public Optional<PluginDescriptor> getAvailablePlugin(String id) {
        return getAvailablePlugins().stream().filter(pluginDescriptor -> pluginDescriptor.getId().equals(id)).findAny();
    }

    @Override
    public Optional<PluginDescriptor> getLoadedPlugin(String id) {
        return getLoadedPlugins().stream().filter(pluginDescriptor -> pluginDescriptor.getId().equals(id)).findAny();
    }

    @Override
    public void load(String id) {
        load(id, new PluginConfig());
    }

    @Override
    public void load(String id, PluginConfig config) {
        final PluginWrapper pluginWrapper = getPluginWrapper(id);
        pluginWrapper.init(config);
        pluginWrapper.load();

        loadedPlugins.add(pluginWrapper);

        LOG.debug("Plugin Loaded: {}", pluginWrapper.getDescriptor());
    }

    @Override
    public void unload(String id) {
        final PluginWrapper pluginWrapper = getPluginWrapper(id);
        pluginWrapper.unload();

        loadedPlugins.remove(pluginWrapper);

        LOG.debug("Plugin Unloaded: {}", pluginWrapper.getDescriptor());
    }

    @Override
    public void unloadAll() {
        loadedPlugins.stream()
                .map(PluginWrapper::getDescriptor)
                .map(PluginDescriptor::getId)
                .forEach(this::unload);
    }

    PluginWrapper getPluginWrapper(final String id) {
        if (!repository.containsPlugin(id))
            throw new PluginException(id, "Plugin with given id is not in plugin repository");

        final PluginWrapper pluginWrapper;

        if (pluginWrappers.containsKey(id)) {
            pluginWrapper = pluginWrappers.get(id);
        } else {
            final PluginRepositoryEntry entry = repository.getPlugin(id);
            final ClassLoader pluginClassLoader = pluginClassLoaderFactory.createClassLoader(entry.getClasspath());

            pluginWrapper = new PluginWrapper(entry.getDescriptor(), entry.getClasspath(), pluginClassLoader);
            pluginWrappers.put(id, pluginWrapper);
        }

        return pluginWrapper;
    }
}
