package ljpf;

/**
 * Created by souzen on 25.03.2017.
 */
public class PluginException extends RuntimeException {

    private final String pluginId;

    public PluginException(final String pluginId, final String message) {
        super(message);
        this.pluginId = pluginId;
    }

    public PluginException(final String pluginId, final String message, final Throwable cause) {
        super(message, cause);
        this.pluginId = pluginId;
    }

    public String getPluginId() {
        return pluginId;
    }

    @Override
    public String getMessage() {
        return String.format("Plugin '%s': %s", pluginId, super.getMessage());
    }

}