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

import java.util.regex.Pattern;

/**
 * Created by sosnickl on 2016-03-18.
 */
public class AnyVersionMatcher implements VersionMatcher {

    private static final Pattern PATTERN = Pattern.compile("(\\.)");

    private static final VersionComponent ANY = new VersionComponent("*");

    private VersionComponent[] matcherComponents;
    private String wildcardVersion;

    public AnyVersionMatcher(final String wildcardVersion) {
        this.wildcardVersion = wildcardVersion;
        final String[] split = PATTERN.split(wildcardVersion.trim());

        matcherComponents = new VersionComponent[split.length];

        for (int i = 0, splitLength = split.length; i < splitLength; i++) {
            matcherComponents[i] = new VersionComponent(split[i]);
        }
    }

    @Override
    public boolean matches(final Version version) {

        if(version.toString().equals(wildcardVersion.trim())) {
            return true;
        }

        VersionComponent[] components = new VersionComponent[3];
        components[0] = new VersionComponent(String.valueOf(version.getMajor()));
        components[1] = new VersionComponent(String.valueOf(version.getMinor()));
        components[2] = new VersionComponent(String.valueOf(version.getPatch()));

        for (int i = 0; i < components.length; i++) {
            VersionComponent c = components[i];

            final VersionComponent matcher = matcherComponents[i];

            if (matcher.equals(ANY))
                return true;

            if (!c.equals(matcher))
                return false;
        }

        return true;
    }

    @Override
    public String description() {
        return String.format("Any version: %s", toString());
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        for (VersionComponent c : matcherComponents) {
            sb.append(c.value);
            sb.append('.');
        }

        return sb.substring(0, sb.length() - 1);
    }

    @Override
    public String toVersionString() {
        return wildcardVersion;
    }

    public static VersionMatcher matchingAnyVersion(final String wildcardVersion) {
        return new AnyVersionMatcher(wildcardVersion);
    }
}
