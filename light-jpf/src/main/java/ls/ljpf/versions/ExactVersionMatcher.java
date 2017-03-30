package ls.ljpf.versions;

/**
 * Created by sosnickl on 2016-03-18.
 */
public class ExactVersionMatcher implements VersionMatcher {

    private final Version exactVersion;
    private final String version;

    public ExactVersionMatcher(final String version) {
        this.version = version;
        this.exactVersion = Version.parse(version);
    }

    @Override
    public boolean matches(final Version version) {
        return exactVersion.equals(version);
    }

    public static VersionMatcher matchingExactVersion(final String exactVersion) {
        return new ExactVersionMatcher(exactVersion.trim());
    }

    @Override
    public String description() {
        return String.format("Exact version: %s", toString());
    }

    @Override
    public String toString() {
        return exactVersion.toString();
    }

    @Override
    public String toVersionString() {
        return version;
    }
}
