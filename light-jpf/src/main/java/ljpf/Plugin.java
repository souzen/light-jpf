package ljpf;

/**
 * Created by souzen on 25.03.2017.
 */
public interface Plugin {

    default void init(final PluginConfig config) {}

    void load();

    void unload();

}
