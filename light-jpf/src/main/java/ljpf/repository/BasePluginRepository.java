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

package ljpf.repository;


import ljpf.PluginException;
import ljpf.PluginRepository;
import ljpf.PluginRepositoryEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by souzen on 25.03.2017.
 */
public class BasePluginRepository implements PluginRepository {

    private static final Logger LOG = LoggerFactory.getLogger(BasePluginRepository.class.getSimpleName());

    private final Map<String, PluginRepositoryEntry> repositoryEntryMap;

    public BasePluginRepository() {
        repositoryEntryMap = new ConcurrentHashMap<>();
    }

    @Override
    public boolean containsPlugin(String id) {
        return repositoryEntryMap.containsKey(id);
    }

    @Override
    public PluginRepositoryEntry getPlugin(String id) {
        return repositoryEntryMap.get(id);
    }

    @Override
    public Collection<PluginRepositoryEntry> getPlugins() {
        return Collections.unmodifiableCollection(repositoryEntryMap.values());
    }

    public void addEntry(final PluginRepositoryEntry entry) {
        if (containsPlugin(entry.getDescriptor().getId()))
            throw new PluginException(entry.getDescriptor().getId(), "Duplicate plugin with same id in repository");

        repositoryEntryMap.put(entry.getDescriptor().getId(), entry);
        LOG.debug("Plugin added: {} {}", entry.getDescriptor().getId(), entry.getClasspath().getPath());
    }
}
