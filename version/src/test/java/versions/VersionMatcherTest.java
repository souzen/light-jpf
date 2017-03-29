package versions;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static versions.AnyVersionMatcher.matchingAnyVersion;
import static versions.ExactVersionMatcher.matchingExactVersion;
import static versions.MaxVersionMatcher.matchingMaxVersion;
import static versions.MinVersionMatcher.matchingMinVersion;

/**
 * Created by sosnickl on 2016-03-18.
 */
public class VersionMatcherTest {

    @Test
    public void verifyExactVersionMatcher() {
        assertThat(matchingExactVersion("1.2.3-SNAPSHOT").matches(Version.parse("1.2.3-SNAPSHOT")), is(true));
        assertThat(matchingExactVersion("1.2.3").matches(new Version(1, 2, 3)), is(true));

        assertThat(matchingExactVersion("1.2.3").matches(new Version(1, 2, 4)), is(false));
        assertThat(matchingExactVersion("1.2.3").matches(new Version(1, 2, 2)), is(false));
        assertThat(matchingExactVersion("1.2.3").matches(new Version(1, 2)), is(false));
    }

    @Test
    public void verifyMaxVersionMatcher() {
        assertThat(matchingMaxVersion("1.2.3-SNAPSHOT").matches(Version.parse("1.2.3-SNAPSHOT")), is(true));
        assertThat(matchingMaxVersion("1.2.3").matches(new Version(1, 2, 3)), is(true));
        assertThat(matchingMaxVersion("1.2.3").matches(new Version(1, 2, 2)), is(true));
        assertThat(matchingMaxVersion("1.2.3").matches(new Version(1, 0)), is(true));
        assertThat(matchingMaxVersion("2").matches(new Version(1, 0)), is(true));
        assertThat(matchingMaxVersion("2").matches(new Version(2, 0)), is(true));
        assertThat(matchingMaxVersion("2.3").matches(new Version(2)), is(true));

        assertThat(matchingMaxVersion("2.3").matches(new Version(3)), is(false));
        assertThat(matchingMaxVersion("3").matches(new Version(3, 1)), is(false));
        assertThat(matchingMaxVersion("3.0.1").matches(new Version(3, 0, 2)), is(false));
    }

    @Test
    public void verifyAnyVersionMatcher() {
        assertThat(matchingAnyVersion("1.2.*").matches(Version.parse("1.2.3-SNAPSHOT")), is(true));
        assertThat(matchingAnyVersion("1.2.3-SNAPSHOT").matches(Version.parse("1.2.3-SNAPSHOT")), is(true));
        assertThat(matchingAnyVersion("1.2.*").matches(new Version(1, 2, 3)), is(true));
        assertThat(matchingAnyVersion("1.2.*").matches(new Version(1, 2, 2)), is(true));
        assertThat(matchingAnyVersion("1.2.*").matches(new Version(1, 2, 4)), is(true));
        assertThat(matchingAnyVersion("1.2.*").matches(new Version(1, 2)), is(true));


        assertThat(matchingAnyVersion("1.2.*").matches(new Version(1, 3, 3)), is(false));
        assertThat(matchingAnyVersion("1.2.*").matches(new Version(1, 3, 2)), is(false));
        assertThat(matchingAnyVersion("1.2.*").matches(new Version(1, 3, 4)), is(false));
        assertThat(matchingAnyVersion("1.2.*").matches(new Version(1, 3)), is(false));
    }

    @Test
    public void verifyMinVersionMatcher() {
        assertThat(matchingMinVersion("1.2.3-SNAPSHOT").matches(Version.parse("1.2.3-SNAPSHOT")), is(true));
        assertThat(matchingMinVersion("1.2.3+").matches(new Version(1, 2, 3)), is(true));
        assertThat(matchingMinVersion("1.2.3+").matches(new Version(1, 2, 4)), is(true));
        assertThat(matchingMinVersion("1.2.3+").matches(new Version(2, 0, 0)), is(true));

        assertThat(matchingMinVersion("1.2.3+").matches(new Version(1, 2, 2)), is(false));
        assertThat(matchingMinVersion("1.2.3+").matches(new Version(1, 2)), is(false));
        assertThat(matchingMinVersion("1.2.3+").matches(new Version(1, 2, 0)), is(false));
        assertThat(matchingMinVersion("1.2.3+").matches(new Version(1, 1, 0)), is(false));

        assertThat(matchingMinVersion("1.2.+").matches(new Version(1, 2, 3)), is(true));
        assertThat(matchingMinVersion("1.2.+").matches(new Version(1, 2, 2)), is(true));
        assertThat(matchingMinVersion("1.2.+").matches(new Version(1, 2, 4)), is(true));
        assertThat(matchingMinVersion("1.2.+").matches(new Version(1, 2)), is(true));

        assertThat(matchingMinVersion("1.4").matches(new Version(1, 4, 0)), is(true));
        assertThat(matchingMinVersion("1.4").matches(new Version(1, 4)), is(true));

        assertThat(matchingMinVersion("1.4.1+").matches(new Version(1, 3)), is(false));
        assertThat(matchingMinVersion("1.4").matches(new Version(1, 3)), is(false));
    }

}
