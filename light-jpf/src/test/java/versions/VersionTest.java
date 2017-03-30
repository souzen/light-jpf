package versions;

import ls.ljpf.versions.Version;
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
