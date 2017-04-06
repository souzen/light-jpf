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
 * Created by kubis on 5/19/2016.
 */
public class MaxVersionMatcher implements VersionMatcher {

    private static final Pattern PATTERN = Pattern.compile("(\\.)");
    private VersionComponent[] matcherComponents;
    private String maxVersion;

    public MaxVersionMatcher(String maxVersion) {
        this.maxVersion = maxVersion;
        final String[] split = PATTERN.split(maxVersion.trim());

        matcherComponents = new VersionComponent[split.length];

        for (int i = 0, splitLength = split.length; i < splitLength; i++) {
            matcherComponents[i] = new VersionComponent(split[i]);
        }
    }

    @Override
    public boolean matches(Version version) {
        if (version.toString().equals(maxVersion.trim())) {
            return true;
        }

        int[] components = new int[3];
        components[0] = version.getMajor();
        components[1] = version.getMinor();
        components[2] = version.getPatch();

        for (int i = 0; i < components.length; i++) {
            int v = components[i];
            int matcher = i < matcherComponents.length ? Integer.valueOf(matcherComponents[i].value) : 0;

            if (v > matcher) {
                return false;
            } else if (v < matcher){
                return true;
            }
        }

        return true;
    }

    @Override
    public String description() {
        return "Max version: " + toString();
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
        return maxVersion;
    }

    public static VersionMatcher matchingMaxVersion(final String version) {
        return new MaxVersionMatcher(version);
    }
}
