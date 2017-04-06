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

import org.apache.commons.lang3.StringUtils;

/**
 * Created by sosnickl on 2016-03-17.
 */
public class Version {

    private static final Pattern PATTERN = Pattern.compile("(\\.|\\-)");
    public static final int UNKNOWN_VALUE = -1;

    private final int major;
    private final int minor;
    private final int patch;
    private final String info;

    public Version(final Integer major, final Integer minor, final Integer patch, final String info) {

        if(major == null || major < 0) {
            throw new IllegalArgumentException("Major version component cannot be null nor negative");
        }

        if (minor != null && minor < 0) {
            throw new IllegalArgumentException("Minor version component cannot be negative");
        }

        if(patch != null && patch < 0) {
            throw new IllegalArgumentException("Patch version component cannot be negative");
        }

        this.major = major;
        this.minor = minor == null? UNKNOWN_VALUE : minor;
        this.patch = patch == null? UNKNOWN_VALUE : patch;
        this.info = info;
    }

    public Version(final Integer major, final Integer minor, final Integer patch) {
        this(major, minor, patch, null);
    }

    public Version(final Integer major, final Integer minor, final String info) {
        this(major, minor, null, info);
    }

    public Version(final Integer major, final Integer minor) {
        this(major, minor, null, null);
    }

    public Version(final Integer major) {
        this(major, null, null, null);
    }

    public Version(int major, String info) {
        this(major, null, null, info);
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }

    public String getInfo() {
        return info;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Version version = (Version) o;

        if (major != version.major) return false;
        if (minor != version.minor) return false;
        if (patch != version.patch) return false;
        return info != null ? info.equals(version.info) : version.info == null;

    }

    @Override
    public int hashCode() {
        int result = major;
        result = 31 * result + minor;
        result = 31 * result + patch;
        result = 31 * result + (info != null ? info.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(major);

        if(minor != UNKNOWN_VALUE) {
            sb.append('.').append(minor);
        }

        if (patch != UNKNOWN_VALUE){
            sb.append('.').append(patch);
        }

        if (StringUtils.isNotBlank(info)) {
            sb.append('-').append(info);
        }
        return sb.toString();
    }

    public static Version parse(final String version) {
        final String[] split = PATTERN.split(version);

        if (split.length < 1) {
            throw new IllegalArgumentException("Version must have at least 2 components");
        }

        Integer[] versionDigits = new Integer[3];
        String info = null;

        for (int i = 0; i < split.length; i++) {

            if (!StringUtils.isNumeric(split[i])) {
                info = extractInfo(split, i);
            } else {
                versionDigits[i] = Integer.valueOf(split[i]);
            }
        }

        return new Version(versionDigits[0], versionDigits[1], versionDigits[2], info);
    }

    private static String extractInfo(String[] components, int from) {
        StringBuilder builder = new StringBuilder();

        for (int i = from; i < components.length; i++) {
            builder.append(components[i]);
        }

        return builder.toString();
    }
}
