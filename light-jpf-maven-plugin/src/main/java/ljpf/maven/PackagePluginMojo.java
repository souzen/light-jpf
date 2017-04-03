package ljpf.maven;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.assembly.mojos.AbstractAssemblyMojo;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;

/**
 * Created by sosnickl on 2017-04-03.
 */
@Mojo(name = "package-plugin")
public class PackagePluginMojo extends AbstractAssemblyMojo {

    @Parameter(defaultValue = "${plugin}", readonly = true)
    private PluginDescriptor plugin;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        setDescriptors(new String[]{"src/main/resources/plugin-assembly.xml"});

        verifyRemovedParameter("classifier");
        verifyRemovedParameter("descriptor");
        verifyRemovedParameter("descriptorId");
        verifyRemovedParameter("includeSite");

        super.execute();
    }

    private void verifyRemovedParameter(String paramName) {
        Object pluginConfiguration = plugin.getPlugin().getConfiguration();
        if (pluginConfiguration instanceof Xpp3Dom) {
            Xpp3Dom configDom = (Xpp3Dom) pluginConfiguration;

            if (configDom.getChild(paramName) != null) {
                throw new IllegalArgumentException("parameter '" + paramName + "' has been removed from the plugin, please verify documentation.");
            }
        }
    }

    /**
     */
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Override
    public MavenProject getProject() {
        return project;
    }
}