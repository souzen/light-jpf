package ljpf.maven;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.dependency.fromConfiguration.ArtifactItem;
import org.apache.maven.plugins.dependency.fromConfiguration.UnpackMojo;
import org.apache.maven.plugins.dependency.utils.markers.MarkerHandler;
import org.apache.maven.plugins.dependency.utils.markers.UnpackFileMarkerHandler;

import java.io.File;
import java.util.List;

/**
 * Created by souzen on 03.04.2017.
 */
@Mojo(name = "make-plugin-repository", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class MakePluginRepositoryMojo extends UnpackMojo {

    @Parameter(property = "outputDirectory", defaultValue = "${project.build.directory}/plugins")
    private File pluginsDir;

    @Parameter(defaultValue = "plugin")
    private String defaultClassifier;

    protected void doExecute() throws MojoExecutionException, MojoFailureException {
        if (isSkip()) {
            return;
        }

        verifyRequirements();

        for (ArtifactItem artifactItem : getArtifactItems()) {
            if (artifactItem.getClassifier() == null) {
                artifactItem.setClassifier(defaultClassifier);
            }
        }

        List<ArtifactItem> processedItems = getProcessedArtifactItems(false);
        for (ArtifactItem artifactItem : processedItems) {
            if (artifactItem.isNeedsProcessing()) {
                unpackArtifact(artifactItem);
            } else {
                this.getLog().info(artifactItem.getArtifact().getFile().getName() + " already unpacked.");
            }
        }
    }

    private void unpackArtifact(ArtifactItem artifactItem)
            throws MojoExecutionException {
        MarkerHandler handler = new UnpackFileMarkerHandler(artifactItem, getMarkersDirectory());

        unpack(artifactItem.getArtifact(), artifactItem.getType(), artifactItem.getOutputDirectory(),
                artifactItem.getIncludes(), artifactItem.getExcludes(), artifactItem.getEncoding());
        handler.setMarker();
    }
}
