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

import ljpf.versions.Version;

/**
 * Created by souzen on 25.03.2017.
 */
public class PluginDescriptor {

    private final String id;
    private final Version version;
    private final String pluginClass;
    private final String description;

    public PluginDescriptor(String id, Version version, String pluginClass, String description) {
        this.id = id;
        this.version = version;
        this.pluginClass = pluginClass;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public Version getVersion() {
        return version;
    }

    public String getPluginClass() {
        return pluginClass;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Plugin{" +
                "id='" + id + '\'' +
                ", version=" + version +
                '}';
    }
}
