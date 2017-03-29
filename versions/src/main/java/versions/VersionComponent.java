package versions;

/**
 * Created by sosnickl on 2016-03-18.
 */
class VersionComponent {

    String value;

    public VersionComponent(final String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VersionComponent that = (VersionComponent) o;

        return value != null ? value.equals(that.value) : that.value == null;
    }
}
