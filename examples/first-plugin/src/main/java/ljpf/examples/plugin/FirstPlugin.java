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

package ljpf.examples.plugin;

import ljpf.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.SpringVersion;

/**
 * Created by souzen on 29.03.2017.
 */
public class FirstPlugin implements Plugin {

    private static final Logger LOG = LoggerFactory.getLogger(FirstPlugin.class.getSimpleName());

    @Override
    public void load() {
        LOG.debug("Load [classloader {}]", getClass().getClassLoader());
        LOG.info("Spring version {}", SpringVersion.getVersion());
    }

    @Override
    public void unload() {
        LOG.info("Unload");
    }

}
