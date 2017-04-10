# Lightweight Java Plugin Framework

[![Maven Central](https://img.shields.io/maven-central/v/com.github.souzen/light-jpf.svg)](http://search.maven.org/#search|ga|1|light-jpf)
[![Travis](https://img.shields.io/travis/rust-lang/rust.svg)](https://travis-ci.org/souzen/light-jpf)



## 1. Features
- Simple api
- Sandboxing with custom java classloader
- Build plugins with maven



## 2. Usage

#### 2.1 Create plugin

Create Plugin class that implements ljpf.Plugin interface.

```java
public class CustomPlugin implements Plugin {

    @Override
    public void load() {
        ...
    }

    @Override
    public void unload() {
        ...
    }
}
```

Create descriptor file for corresponding plugin and place it in project resources.
Descriptor file must have .plugin extension.

src/main/resources/custom.plugin

```properties
id=CustomPluginId
version=0.0.1
pluginClass=ljpf.examples.plugin.CustomPlugin
description=My Custom plugin
```

#### 2.2 Create app

Load plugins in your main application using PluginManager interface. Use plugin id from descriptor file to load extensions.
Plugin repository determines way of loading plugins. Base case is to load jars from classpath plugins/ directory.


```java
public class App {

    public static void main(String[] args) {

        PluginRepository pluginRepository = new DirPluginRepository("plugins");
        PluginManager pluginManager = new DefaultPluginManager(pluginRepository);

        pluginManager.load("CustomPluginId");
    }

}
```

#### 2.3 Build with Maven

#### Build plugin with Maven

Use light-jpf-maven-plugin to create maven artifact (*-plugin.jar).
Maven will create fat jar with plugin code and all its dependencies. 

```xml
    <build>
        <plugins>
            <plugin>
                <groupId>com.github.souzen</groupId>
                <artifactId>light-jpf-maven-plugin</artifactId>
                <version>0.0.2</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>make-plugin</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
        ....    
    
    </build>
```

#### Add plugins to app

light-jpf-maven-plugin can also prepare plugins/ directory in your application.

```xml
    <build>
        <plugins>
            <plugin>
                <groupId>com.github.souzen</groupId>
                <artifactId>light-jpf-maven-plugin</artifactId>
                <version>0.0.2</version>
                <executions>
                    <execution>
                        <phase>process-sources</phase>
                        <goals>
                            <goal>make-plugin-repository</goal>
                        </goals>
                        <configuration>
                            <outputDirectory>${project.build.directory}/plugins</outputDirectory>
                            <artifactItems>
                                <artifactItem>
                                    <groupId>ljpf.examples.plugin</groupId>
                                    <artifactId>custom-plugin</artifactId>
                                    <version>0.0.1</version>
                                </artifactItem>
                            </artifactItems>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
            ... 
               
        </plugins>
        ....    
    
    </build>
```



## 3. Plugin Repositories

#### DirPluginRepository
TODO Loads plugins from given directory

#### ClasspathPluginRepository
TODO Loads plugins from java classpath

#### MultiPluginRepository
TODO Enables mixing multiple plugin reposiotories



## 4. Debugging
In app create plugins dir and run or place plugins as dependencies

```xml
    <dependencies>
        <dependency>
            <groupId>ljpf.examples.plugin</groupId>
            <artifactId>custom-plugin</artifactId>
            <version>0.0.1</version>
        </dependency>
        ...
        
    </dependencies>
```



## 5. Examples
See example project [here](https://github.com/souzen/light-jpf/tree/master/examples)

Build and run example

```
mvn clean package
unzip examples/app/target/app-0.0.3-SNAPSHOT-bin.zip
./app-0.0.3-SNAPSHOT/start.bat
```


## 6. Licence
Copyright 2017 Luke Sosnicki

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
