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
import ljpf.PluginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by souzen on 25.03.2017.
 */
public class ParentFirstClassLoaderFactory implements PluginClassLoaderFactory {

    private static final Logger LOG = LoggerFactory.getLogger(ParentFirstClassLoaderFactory.class.getSimpleName());

    @Override
    public ClassLoader createClassLoader(final PluginClasspath pluginClasspath) {
        final ParentFirstClassLoader loader = new ParentFirstClassLoader(new URL[0], this.getClass().getClassLoader());

        pluginClasspath.getDirs().forEach(dir -> addUrl(loader, dir));
        pluginClasspath.getJars().forEach(jar -> addUrl(loader, jar));
        pluginClasspath.getResources().forEach(dir -> addUrlToParent(dir));

        return loader;
    }

    private void addUrl(ParentFirstClassLoader loader, String path) {
        try {
            final URL url = new File(path).toURI().toURL();
            loader.addURL(url);
        } catch (MalformedURLException e) {
            // NOOP
        }
    }

    private void addUrlToParent(String path) {
        try {
            final URL url = new File(path).toURI().toURL();

            URLClassLoader cl = (URLClassLoader) ClassLoader.getSystemClassLoader();
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});

            method.setAccessible(true);
            method.invoke(cl, new Object[]{url});

        } catch (MalformedURLException e) {
            // NOOP
        } catch (Throwable t) {
            throw new PluginException("Plugin Error when adding url to system ClassLoader", t.getMessage(), t);
        }
    }

    private static class ParentFirstClassLoader extends URLClassLoader {

        public ParentFirstClassLoader(URL[] urls, ClassLoader parent) {
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
                Class<?> clazz = null;

                try {
                    clazz = getParent().loadClass(className);
                } catch (ClassNotFoundException e) {
                    // try next step
                }

                if (clazz == null) {
                    LOG.trace("Couldn't find class '{}' in parent. Delegating to child", className);

                    clazz = findLoadedClass(className);

                    if (clazz != null) {
                        LOG.trace("Found loaded class '{}'", className);
                        return clazz;
                    }

                    // try to load clas locally
                    clazz = findClass(className);
                    LOG.trace("Found class '{}' in plugin classpath", className);
                }

                LOG.trace("Found class in parent '{}'", className);

                return clazz;
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
