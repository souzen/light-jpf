package versions;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by sosnickl on 2016-03-18.
 */
public class VersionTest {

    @Test
    public void verifyVersionCreation() {
        Version version;

        version = new Version(1);
        assertThat(version.toString(), equalTo("1"));
        assertThat(version.getMajor(), equalTo(1));
        assertThat(version.getMinor(), equalTo(Version.UNKNOWN_VALUE));
        assertThat(version.getPatch(), equalTo(Version.UNKNOWN_VALUE));
        assertThat(version.getInfo(), equalTo(null));

        version = new Version(1, "SNAPSHOT");
        assertThat(version.toString(), equalTo("1-SNAPSHOT"));
        assertThat(version.getMajor(), equalTo(1));
        assertThat(version.getMinor(), equalTo(Version.UNKNOWN_VALUE));
        assertThat(version.getPatch(), equalTo(Version.UNKNOWN_VALUE));
        assertThat(version.getInfo(), equalTo("SNAPSHOT"));

        version = new Version(1, 2, 3, "SNAPSHOT");
        assertThat(version.toString(), equalTo("1.2.3-SNAPSHOT"));
        assertThat(version.getMajor(), equalTo(1));
        assertThat(version.getMinor(), equalTo(2));
        assertThat(version.getPatch(), equalTo(3));
        assertThat(version.getInfo(), equalTo("SNAPSHOT"));

        version = new Version(1, 2, 3);
        assertThat(version.toString(), equalTo("1.2.3"));
        assertThat(version.getMajor(), equalTo(1));
        assertThat(version.getMinor(), equalTo(2));
        assertThat(version.getPatch(), equalTo(3));
        assertThat(version.getInfo(), equalTo(null));

        version = new Version(1, 2);
        assertThat(version.toString(), equalTo("1.2"));
        assertThat(version.getMajor(), equalTo(1));
        assertThat(version.getMinor(), equalTo(2));
        assertThat(version.getPatch(), equalTo(Version.UNKNOWN_VALUE));
        assertThat(version.getInfo(), equalTo(null));

        version = new Version(1, 2);
        assertThat(version.toString(), equalTo("1.2"));
        assertThat(version.getMajor(), equalTo(1));
        assertThat(version.getMinor(), equalTo(2));
        assertThat(version.getPatch(), equalTo(Version.UNKNOWN_VALUE));
        assertThat(version.getInfo(), equalTo(null));

        version = new Version(1, 2, "SNAPSHOT");
        assertThat(version.toString(), equalTo("1.2-SNAPSHOT"));
        assertThat(version.getMajor(), equalTo(1));
        assertThat(version.getMinor(), equalTo(2));
        assertThat(version.getPatch(), equalTo(Version.UNKNOWN_VALUE));
        assertThat(version.getInfo(), equalTo("SNAPSHOT"));
    }

    @Test
    public void verifyVersionParsing() {
        Version version;

        version = Version.parse("1.2.3-SNAPSHOT");
        assertThat(version.toString(), equalTo("1.2.3-SNAPSHOT"));
        assertThat(version.getMajor(), equalTo(1));
        assertThat(version.getMinor(), equalTo(2));
        assertThat(version.getPatch(), equalTo(3));
        assertThat(version.getInfo(), equalTo("SNAPSHOT"));

        version = Version.parse("1");
        assertThat(version.toString(), equalTo("1"));
        assertThat(version.getMajor(), equalTo(1));
        assertThat(version.getMinor(), equalTo(Version.UNKNOWN_VALUE));
        assertThat(version.getPatch(), equalTo(Version.UNKNOWN_VALUE));
        assertThat(version.getInfo(), equalTo(null));

        version = Version.parse("1-SNAPSHOT");
        assertThat(version.toString(), equalTo("1-SNAPSHOT"));
        assertThat(version.getMajor(), equalTo(1));
        assertThat(version.getMinor(), equalTo(Version.UNKNOWN_VALUE));
        assertThat(version.getPatch(), equalTo(Version.UNKNOWN_VALUE));
        assertThat(version.getInfo(), equalTo("SNAPSHOT"));

        version = Version.parse("1.2.3");
        assertThat(version.toString(), equalTo("1.2.3"));
        assertThat(version.getMajor(), equalTo(1));
        assertThat(version.getMinor(), equalTo(2));
        assertThat(version.getPatch(), equalTo(3));
        assertThat(version.getInfo(), equalTo(null));

        version = Version.parse("1.2");
        assertThat(version.toString(), equalTo("1.2"));
        assertThat(version.getMajor(), equalTo(1));
        assertThat(version.getMinor(), equalTo(2));
        assertThat(version.getPatch(), equalTo(Version.UNKNOWN_VALUE));
        assertThat(version.getInfo(), equalTo(null));

        version = Version.parse("1.2-SNAPSHOT");
        assertThat(version.toString(), equalTo("1.2-SNAPSHOT"));
        assertThat(version.getMajor(), equalTo(1));
        assertThat(version.getMinor(), equalTo(2));
        assertThat(version.getPatch(), equalTo(Version.UNKNOWN_VALUE));
        assertThat(version.getInfo(), equalTo("SNAPSHOT"));
    }

}
