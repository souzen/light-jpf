# Lightweight Java Plugin Framework

[![Maven Central](https://img.shields.io/maven-central/v/com.github.souzen/light-jpf.svg)](http://search.maven.org/#search|ga|1|light-jpf)
[![Travis](https://img.shields.io/travis/rust-lang/rust.svg)](https://travis-ci.org/souzen/light-jpf)

TODO Description

## 1. Features
- simple api
- custom classloader for sandboxing
- maven plugin

## 2. Usage

#### 2.1 Create plugin

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

```properties
id=CustomPlugin
version=0.0.1
pluginClass=ljpf.examples.plugin.CustomPlugin
description=My Custom plugin
```

#### 2.2 Create app
```java
public class App {

    public static void main(String[] args) {

        PluginRepository pluginRepository = new DirPluginRepository("plugin");
        PluginManager pluginManager = new DefaultPluginManager(pluginRepository);

        pluginManager.load("CustomPlugin");
    }

}
```

#### 2.3 Build plugin with Maven

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

#### 2.4 Add plugins to app

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
                            <outputDirectory>${plugins.dir}</outputDirectory>
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
TODO Loads plugins from directory

#### ClasspathPluginRepository
TODO Loads plugins from java classpath

#### MultiPluginRepository
TODO Enables mixing multiple plugin reposiotories

## 4. Examples
TODO

## 5. TODO
- dependency resolver
- Tests

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