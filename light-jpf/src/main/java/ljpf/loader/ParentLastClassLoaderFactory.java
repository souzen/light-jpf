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

package ljpf.loader;

import ljpf.PluginClassLoaderFactory;
import ljpf.PluginClasspath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by souzen on 25.03.2017.
 */
public class ParentLastClassLoaderFactory implements PluginClassLoaderFactory {

    private static final Logger LOG = LoggerFactory.getLogger(ParentLastClassLoaderFactory.class.getSimpleName());

    @Override
    public ClassLoader createClassLoader(final PluginClasspath pluginClasspath) {
        final ParentLastClassLoader loader = new ParentLastClassLoader(new URL[0], this.getClass().getClassLoader());

        pluginClasspath.getDirs().stream().forEach(dir -> addUrl(loader, dir));
        pluginClasspath.getJars().stream().forEach(jar -> addUrl(loader, jar));

        return loader;
    }

    private void addUrl(ParentLastClassLoader loader, String path) {
        try {
            final URL url = new File(path).toURI().toURL();
            loader.addURL(url);
        } catch (MalformedURLException e) {
            // NOOP
        }
    }

    private static class ParentLastClassLoader extends URLClassLoader {

        public ParentLastClassLoader(URL[] urls, ClassLoader parent) {
            super(urls, parent);
        }

        @Override
        protected void addURL(URL url) {
            super.addURL(url);
        }

        /**
         * This implementation of loadClass uses a child first delegation model rather than the standard parent first.
         * If the requested class cannot be found in this class loader, the parent class loader will be consulted
         * via the standard ClassLoader.loadClass(String) mechanism.
         */
        @Override
        public Class<?> loadClass(String className) throws ClassNotFoundException {
            synchronized (getClassLoadingLock(className)) {

                // check whether it's already been loaded
                Class<?> clazz = findLoadedClass(className);
                if (clazz != null) {
                    LOG.trace("Found loaded class '{}'", className);
                    return clazz;
                }

                // try to load clas locally
                try {
                    clazz = findClass(className);
                    LOG.trace("Found class '{}' in plugin classpath", className);
                    return clazz;
                } catch (ClassNotFoundException e) {
                    // try next step
                }

                LOG.trace("Couldn't find class '{}' in plugin classpath. Delegating to parent", className);

                // use the parent URLClassLoader (which follows normal parent delegation)
                return super.loadClass(className);
            }
        }

        /**
         * Load the named resource from this plugin. This implementation checks the plugin's classpath first
         * then delegates to the parent.
         *
         * @param name the name of the resource.
         * @return the URL to the resource, <code>null</code> if the resource was not found.
         */
        @Override
        public URL getResource(String name) {
            URL url = findResource(name);
            if (url != null) {
                LOG.trace("Found resource '{}' in plugin classpath", name);
                return url;
            }

            LOG.trace("Couldn't find resource '{}' in plugin classpath. Delegating to parent");

            return super.getResource(name);
        }

    }
}
