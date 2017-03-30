package versions;

import ls.ljpf.versions.Version;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import static ls.ljpf.versions.AnyVersionMatcher.matchingAnyVersion;
import static ls.ljpf.versions.ExactVersionMatcher.matchingExactVersion;
import static ls.ljpf.versions.MaxVersionMatcher.matchingMaxVersion;
import static ls.ljpf.versions.MinVersionMatcher.matchingMinVersion;

/**
 * Created by sosnickl on 2016-03-18.
 */
public class VersionMatcherTest {

    @Test
    public void verifyExactVersionMatcher() {
        MatcherAssert.assertThat(matchingExactVersion("1.2.3-SNAPSHOT").matches(Version.parse("1.2.3-SNAPSHOT")), Matchers.is(true));
        MatcherAssert.assertThat(matchingExactVersion("1.2.3").matches(new Version(1, 2, 3)), Matchers.is(true));

        MatcherAssert.assertThat(matchingExactVersion("1.2.3").matches(new Version(1, 2, 4)), Matchers.is(false));
        MatcherAssert.assertThat(matchingExactVersion("1.2.3").matches(new Version(1, 2, 2)), Matchers.is(false));
        MatcherAssert.assertThat(matchingExactVersion("1.2.3").matches(new Version(1, 2)), Matchers.is(false));
    }

    @Test
    public void verifyMaxVersionMatcher() {
        MatcherAssert.assertThat(matchingMaxVersion("1.2.3-SNAPSHOT").matches(Version.parse("1.2.3-SNAPSHOT")), Matchers.is(true));
        MatcherAssert.assertThat(matchingMaxVersion("1.2.3").matches(new Version(1, 2, 3)), Matchers.is(true));
        MatcherAssert.assertThat(matchingMaxVersion("1.2.3").matches(new Version(1, 2, 2)), Matchers.is(true));
        MatcherAssert.assertThat(matchingMaxVersion("1.2.3").matches(new Version(1, 0)), Matchers.is(true));
        MatcherAssert.assertThat(matchingMaxVersion("2").matches(new Version(1, 0)), Matchers.is(true));
        MatcherAssert.assertThat(matchingMaxVersion("2").matches(new Version(2, 0)), Matchers.is(true));
        MatcherAssert.assertThat(matchingMaxVersion("2.3").matches(new Version(2)), Matchers.is(true));

        MatcherAssert.assertThat(matchingMaxVersion("2.3").matches(new Version(3)), Matchers.is(false));
        MatcherAssert.assertThat(matchingMaxVersion("3").matches(new Version(3, 1)), Matchers.is(false));
        MatcherAssert.assertThat(matchingMaxVersion("3.0.1").matches(new Version(3, 0, 2)), Matchers.is(false));
    }

    @Test
    public void verifyAnyVersionMatcher() {
        MatcherAssert.assertThat(matchingAnyVersion("1.2.*").matches(Version.parse("1.2.3-SNAPSHOT")), Matchers.is(true));
        MatcherAssert.assertThat(matchingAnyVersion("1.2.3-SNAPSHOT").matches(Version.parse("1.2.3-SNAPSHOT")), Matchers.is(true));
        MatcherAssert.assertThat(matchingAnyVersion("1.2.*").matches(new Version(1, 2, 3)), Matchers.is(true));
        MatcherAssert.assertThat(matchingAnyVersion("1.2.*").matches(new Version(1, 2, 2)), Matchers.is(true));
        MatcherAssert.assertThat(matchingAnyVersion("1.2.*").matches(new Version(1, 2, 4)), Matchers.is(true));
        MatcherAssert.assertThat(matchingAnyVersion("1.2.*").matches(new Version(1, 2)), Matchers.is(true));


        MatcherAssert.assertThat(matchingAnyVersion("1.2.*").matches(new Version(1, 3, 3)), Matchers.is(false));
        MatcherAssert.assertThat(matchingAnyVersion("1.2.*").matches(new Version(1, 3, 2)), Matchers.is(false));
        MatcherAssert.assertThat(matchingAnyVersion("1.2.*").matches(new Version(1, 3, 4)), Matchers.is(false));
        MatcherAssert.assertThat(matchingAnyVersion("1.2.*").matches(new Version(1, 3)), Matchers.is(false));
    }

    @Test
    public void verifyMinVersionMatcher() {
        MatcherAssert.assertThat(matchingMinVersion("1.2.3-SNAPSHOT").matches(Version.parse("1.2.3-SNAPSHOT")), Matchers.is(true));
        MatcherAssert.assertThat(matchingMinVersion("1.2.3+").matches(new Version(1, 2, 3)), Matchers.is(true));
        MatcherAssert.assertThat(matchingMinVersion("1.2.3+").matches(new Version(1, 2, 4)), Matchers.is(true));
        MatcherAssert.assertThat(matchingMinVersion("1.2.3+").matches(new Version(2, 0, 0)), Matchers.is(true));

        MatcherAssert.assertThat(matchingMinVersion("1.2.3+").matches(new Version(1, 2, 2)), Matchers.is(false));
        MatcherAssert.assertThat(matchingMinVersion("1.2.3+").matches(new Version(1, 2)), Matchers.is(false));
        MatcherAssert.assertThat(matchingMinVersion("1.2.3+").matches(new Version(1, 2, 0)), Matchers.is(false));
        MatcherAssert.assertThat(matchingMinVersion("1.2.3+").matches(new Version(1, 1, 0)), Matchers.is(false));

        MatcherAssert.assertThat(matchingMinVersion("1.2.+").matches(new Version(1, 2, 3)), Matchers.is(true));
        MatcherAssert.assertThat(matchingMinVersion("1.2.+").matches(new Version(1, 2, 2)), Matchers.is(true));
        MatcherAssert.assertThat(matchingMinVersion("1.2.+").matches(new Version(1, 2, 4)), Matchers.is(true));
        MatcherAssert.assertThat(matchingMinVersion("1.2.+").matches(new Version(1, 2)), Matchers.is(true));

        MatcherAssert.assertThat(matchingMinVersion("1.4").matches(new Version(1, 4, 0)), Matchers.is(true));
        MatcherAssert.assertThat(matchingMinVersion("1.4").matches(new Version(1, 4)), Matchers.is(true));

        MatcherAssert.assertThat(matchingMinVersion("1.4.1+").matches(new Version(1, 3)), Matchers.is(false));
        MatcherAssert.assertThat(matchingMinVersion("1.4").matches(new Version(1, 3)), Matchers.is(false));
    }

}
