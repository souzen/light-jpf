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

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

/**
 * Created by sosnickl on 2016-03-18.
 */
public class VersionTest {

    @Test
    public void verifyVersionCreation() {
        Version version;

        version = new Version(1);
        MatcherAssert.assertThat(version.toString(), CoreMatchers.equalTo("1"));
        MatcherAssert.assertThat(version.getMajor(), CoreMatchers.equalTo(1));
        MatcherAssert.assertThat(version.getMinor(), CoreMatchers.equalTo(Version.UNKNOWN_VALUE));
        MatcherAssert.assertThat(version.getPatch(), CoreMatchers.equalTo(Version.UNKNOWN_VALUE));
        MatcherAssert.assertThat(version.getInfo(), CoreMatchers.equalTo(null));

        version = new Version(1, "SNAPSHOT");
        MatcherAssert.assertThat(version.toString(), CoreMatchers.equalTo("1-SNAPSHOT"));
        MatcherAssert.assertThat(version.getMajor(), CoreMatchers.equalTo(1));
        MatcherAssert.assertThat(version.getMinor(), CoreMatchers.equalTo(Version.UNKNOWN_VALUE));
        MatcherAssert.assertThat(version.getPatch(), CoreMatchers.equalTo(Version.UNKNOWN_VALUE));
        MatcherAssert.assertThat(version.getInfo(), CoreMatchers.equalTo("SNAPSHOT"));

        version = new Version(1, 2, 3, "SNAPSHOT");
        MatcherAssert.assertThat(version.toString(), CoreMatchers.equalTo("1.2.3-SNAPSHOT"));
        MatcherAssert.assertThat(version.getMajor(), CoreMatchers.equalTo(1));
        MatcherAssert.assertThat(version.getMinor(), CoreMatchers.equalTo(2));
        MatcherAssert.assertThat(version.getPatch(), CoreMatchers.equalTo(3));
        MatcherAssert.assertThat(version.getInfo(), CoreMatchers.equalTo("SNAPSHOT"));

        version = new Version(1, 2, 3);
        MatcherAssert.assertThat(version.toString(), CoreMatchers.equalTo("1.2.3"));
        MatcherAssert.assertThat(version.getMajor(), CoreMatchers.equalTo(1));
        MatcherAssert.assertThat(version.getMinor(), CoreMatchers.equalTo(2));
        MatcherAssert.assertThat(version.getPatch(), CoreMatchers.equalTo(3));
        MatcherAssert.assertThat(version.getInfo(), CoreMatchers.equalTo(null));

        version = new Version(1, 2);
        MatcherAssert.assertThat(version.toString(), CoreMatchers.equalTo("1.2"));
        MatcherAssert.assertThat(version.getMajor(), CoreMatchers.equalTo(1));
        MatcherAssert.assertThat(version.getMinor(), CoreMatchers.equalTo(2));
        MatcherAssert.assertThat(version.getPatch(), CoreMatchers.equalTo(Version.UNKNOWN_VALUE));
        MatcherAssert.assertThat(version.getInfo(), CoreMatchers.equalTo(null));

        version = new Version(1, 2);
        MatcherAssert.assertThat(version.toString(), CoreMatchers.equalTo("1.2"));
        MatcherAssert.assertThat(version.getMajor(), CoreMatchers.equalTo(1));
        MatcherAssert.assertThat(version.getMinor(), CoreMatchers.equalTo(2));
        MatcherAssert.assertThat(version.getPatch(), CoreMatchers.equalTo(Version.UNKNOWN_VALUE));
        MatcherAssert.assertThat(version.getInfo(), CoreMatchers.equalTo(null));

        version = new Version(1, 2, "SNAPSHOT");
        MatcherAssert.assertThat(version.toString(), CoreMatchers.equalTo("1.2-SNAPSHOT"));
        MatcherAssert.assertThat(version.getMajor(), CoreMatchers.equalTo(1));
        MatcherAssert.assertThat(version.getMinor(), CoreMatchers.equalTo(2));
        MatcherAssert.assertThat(version.getPatch(), CoreMatchers.equalTo(Version.UNKNOWN_VALUE));
        MatcherAssert.assertThat(version.getInfo(), CoreMatchers.equalTo("SNAPSHOT"));
    }

    @Test
    public void verifyVersionParsing() {
        Version version;

        version = Version.parse("1.2.3-SNAPSHOT");
        MatcherAssert.assertThat(version.toString(), CoreMatchers.equalTo("1.2.3-SNAPSHOT"));
        MatcherAssert.assertThat(version.getMajor(), CoreMatchers.equalTo(1));
        MatcherAssert.assertThat(version.getMinor(), CoreMatchers.equalTo(2));
        MatcherAssert.assertThat(version.getPatch(), CoreMatchers.equalTo(3));
        MatcherAssert.assertThat(version.getInfo(), CoreMatchers.equalTo("SNAPSHOT"));

        version = Version.parse("1");
        MatcherAssert.assertThat(version.toString(), CoreMatchers.equalTo("1"));
        MatcherAssert.assertThat(version.getMajor(), CoreMatchers.equalTo(1));
        MatcherAssert.assertThat(version.getMinor(), CoreMatchers.equalTo(Version.UNKNOWN_VALUE));
        MatcherAssert.assertThat(version.getPatch(), CoreMatchers.equalTo(Version.UNKNOWN_VALUE));
        MatcherAssert.assertThat(version.getInfo(), CoreMatchers.equalTo(null));

        version = Version.parse("1-SNAPSHOT");
        MatcherAssert.assertThat(version.toString(), CoreMatchers.equalTo("1-SNAPSHOT"));
        MatcherAssert.assertThat(version.getMajor(), CoreMatchers.equalTo(1));
        MatcherAssert.assertThat(version.getMinor(), CoreMatchers.equalTo(Version.UNKNOWN_VALUE));
        MatcherAssert.assertThat(version.getPatch(), CoreMatchers.equalTo(Version.UNKNOWN_VALUE));
        MatcherAssert.assertThat(version.getInfo(), CoreMatchers.equalTo("SNAPSHOT"));

        version = Version.parse("1.2.3");
        MatcherAssert.assertThat(version.toString(), CoreMatchers.equalTo("1.2.3"));
        MatcherAssert.assertThat(version.getMajor(), CoreMatchers.equalTo(1));
        MatcherAssert.assertThat(version.getMinor(), CoreMatchers.equalTo(2));
        MatcherAssert.assertThat(version.getPatch(), CoreMatchers.equalTo(3));
        MatcherAssert.assertThat(version.getInfo(), CoreMatchers.equalTo(null));

        version = Version.parse("1.2");
        MatcherAssert.assertThat(version.toString(), CoreMatchers.equalTo("1.2"));
        MatcherAssert.assertThat(version.getMajor(), CoreMatchers.equalTo(1));
        MatcherAssert.assertThat(version.getMinor(), CoreMatchers.equalTo(2));
        MatcherAssert.assertThat(version.getPatch(), CoreMatchers.equalTo(Version.UNKNOWN_VALUE));
        MatcherAssert.assertThat(version.getInfo(), CoreMatchers.equalTo(null));

        version = Version.parse("1.2-SNAPSHOT");
        MatcherAssert.assertThat(version.toString(), CoreMatchers.equalTo("1.2-SNAPSHOT"));
        MatcherAssert.assertThat(version.getMajor(), CoreMatchers.equalTo(1));
        MatcherAssert.assertThat(version.getMinor(), CoreMatchers.equalTo(2));
        MatcherAssert.assertThat(version.getPatch(), CoreMatchers.equalTo(Version.UNKNOWN_VALUE));
        MatcherAssert.assertThat(version.getInfo(), CoreMatchers.equalTo("SNAPSHOT"));
    }

}
