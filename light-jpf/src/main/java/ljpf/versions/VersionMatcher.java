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

package ljpf.versions;


import java.util.function.Predicate;

/**
 * Created by sosnickl on 2016-03-18.
 */
public interface VersionMatcher {

    boolean matches(Version version);

    default Predicate<Version> predicate() {
        return version -> matches(version);
    }

    String description();

    String toVersionString();

}
