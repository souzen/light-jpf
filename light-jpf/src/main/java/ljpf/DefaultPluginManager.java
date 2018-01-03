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

package ljpf;

import ljpf.impl.PluginWrapper;
import ljpf.loader.ParentLastClassLoaderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

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
        this.pluginWrappers = new ConcurrentHashMap<>();
        this.loadedPlugins = new CopyOnWriteArrayList<>();
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
