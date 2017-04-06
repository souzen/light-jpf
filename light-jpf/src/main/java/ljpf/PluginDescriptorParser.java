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

import java.util.Objects;
import java.util.Properties;

/**
 * Created by souzen on 25.03.2017.
 */
public class PluginDescriptorParser {

    public static boolean valid(final Properties properties) {
        return properties != null
                && !properties.isEmpty()
                && properties.containsKey("id")
                && properties.containsKey("pluginClass");
    }

    public static PluginDescriptor parse(final Properties properties) {
        String id = properties.getProperty("id");
        String versionProperty = properties.getProperty("version");
        String pluginClass = properties.getProperty("pluginClass");
        String description = properties.getProperty("description");

        if (Objects.isNull(id) || Objects.isNull(versionProperty) || Objects.isNull(pluginClass))
            throw new PluginException(id, "Invalid descriptor file");

        Version version = Version.parse(versionProperty);

        return new PluginDescriptor(id, version, pluginClass, description);
    }

}
