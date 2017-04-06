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

import java.util.Collection;

/**
 * Created by souzen on 25.03.2017.
 */
public class PluginClasspath {

    private final String path;
    private final Collection<String> dirs;
    private final Collection<String> jars;
    private final Collection<String> resources;

    public PluginClasspath(String path, Collection<String> dirs, Collection<String> jars, Collection<String> resources) {
        this.path = path;
        this.dirs = dirs;
        this.jars = jars;
        this.resources = resources;
    }

    public String getPath() {
        return path;
    }

    public Collection<String> getDirs() {
        return dirs;
    }

    public Collection<String> getJars() {
        return jars;
    }

    public Collection<String> getResources() {
        return resources;
    }

}
