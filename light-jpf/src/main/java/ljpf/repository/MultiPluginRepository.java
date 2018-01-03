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

import ljpf.PluginRepository;
import ljpf.PluginRepositoryEntry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by souzen on 30.03.2017.
 */
public class MultiPluginRepository implements PluginRepository {

    private final List<PluginRepository> pluginRepositories;

    public MultiPluginRepository(PluginRepository... pluginRepositories) {
        this.pluginRepositories = List.of(pluginRepositories);
    }

    @Override
    public boolean containsPlugin(final String id) {
        return pluginRepositories.stream().anyMatch(r -> r.containsPlugin(id));
    }

    @Override
    public PluginRepositoryEntry getPlugin(final String id) {
        return pluginRepositories.stream().filter(r -> r.containsPlugin(id)).findFirst().map(r -> r.getPlugin(id)).orElse(null);
    }

    @Override
    public Collection<PluginRepositoryEntry> getPlugins() {
        return pluginRepositories.stream().flatMap(r -> r.getPlugins().stream()).distinct().collect(toList());
    }
}
