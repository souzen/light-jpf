package versions;


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
