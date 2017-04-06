/*
 *    Copyright 2017 Luke Sosnicki
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ljpf.maven;

import com.google.inject.internal.util.Lists;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.assembly.InvalidAssemblerConfigurationException;
import org.apache.maven.plugins.assembly.archive.ArchiveCreationException;
import org.apache.maven.plugins.assembly.archive.AssemblyArchiver;
import org.apache.maven.plugins.assembly.format.AssemblyFormattingException;
import org.apache.maven.plugins.assembly.model.Assembly;
import org.apache.maven.plugins.assembly.model.DependencySet;
import org.apache.maven.plugins.assembly.model.FileSet;
import org.apache.maven.plugins.assembly.mojos.AbstractAssemblyMojo;
import org.apache.maven.plugins.assembly.utils.AssemblyFormatUtils;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;

import java.io.File;
import java.util.List;

/**
 * Created by sosnickl on 2017-04-03.
 */
@Mojo(name = "make-plugin", defaultPhase = LifecyclePhase.PREPARE_PACKAGE)
public class MakePluginMojo extends AbstractAssemblyMojo {

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Component
    private MavenProjectHelper projectHelper;

    @Component
    private AssemblyArchiver assemblyArchiver;

    @Parameter(defaultValue = "${project.build.directory}/plugin")
    private File pluginIncludesDir;

    @Parameter(defaultValue = "plugin")
    private String classifier;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Assembly assembly = prepareAssembly();

        if (pluginIncludesDir.exists()) {
            getLog().info("Adding resources from: " + pluginIncludesDir.getAbsolutePath());

            FileSet pluginIncludesFileSet = new FileSet();
            pluginIncludesFileSet.setOutputDirectory("");
            pluginIncludesFileSet.setDirectory(pluginIncludesDir.getPath());
            pluginIncludesFileSet.setFiltered(false);
            assembly.getFileSets().add(pluginIncludesFileSet);
        } else {
            getLog().info("Plugin includes dir not present: " + pluginIncludesDir.getAbsolutePath());
        }

        createAssembly(assembly, true);
    }

    protected Assembly prepareAssembly() {
        Assembly assembly = new Assembly();
        assembly.setId(classifier);
        assembly.setFormats(Lists.newArrayList("jar"));
        assembly.setBaseDirectory("${project.name}");

        FileSet descriptorFileSet = new FileSet();
        descriptorFileSet.setOutputDirectory("");
        descriptorFileSet.setDirectory("src/main/resources");
        descriptorFileSet.setIncludes(Lists.newArrayList(String.format("*.%s", "plugin")));
        descriptorFileSet.setFiltered(true);

        DependencySet dependencySet = new DependencySet();
        dependencySet.setOutputDirectory("lib");
        dependencySet.setUseProjectArtifact(true);
        dependencySet.setScope("runtime");

        assembly.setFileSets(Lists.newArrayList(descriptorFileSet));
        assembly.setDependencySets(Lists.newArrayList(dependencySet));

        return assembly;
    }

    protected void createAssembly(Assembly assembly, boolean attach) throws MojoFailureException, MojoExecutionException {
        try {
            final String fullName = AssemblyFormatUtils.getDistributionName(assembly, this);

            List<String> effectiveFormats = assembly.getFormats();

            if (effectiveFormats == null || effectiveFormats.size() == 0) {
                throw new MojoFailureException("No formats specified in the execution parameters or the assembly descriptor.");
            }

            for (final String format : effectiveFormats) {
                final File destFile = assemblyArchiver.createArchive(assembly, fullName, format, this, true, getMergeManifestMode());

                final MavenProject project = getProject();
                final String type = project.getArtifact().getType();

                if (attach && destFile.isFile()) {
                    if (isAssemblyIdAppended()) {
                        projectHelper.attachArtifact(project, format, assembly.getId(), destFile);
                    } else if (!"pom".equals(type) && format.equals(type)) {
                        final File existingFile = project.getArtifact().getFile();
                        if ((existingFile != null) && existingFile.exists()) {
                            getLog().warn("Replacing pre-existing project main-artifact file: " + existingFile
                                    + "\nwith assembly file: " + destFile);
                        }

                        project.getArtifact().setFile(destFile);
                    } else {
                        projectHelper.attachArtifact(project, format, null, destFile);
                    }
                } else if (attach) {
                    getLog().warn("Assembly file: " + destFile + " is not a regular file (it may be a directory). "
                            + "It cannot be attached to the project build for installation or "
                            + "deployment.");
                }
            }
        } catch (final ArchiveCreationException e) {
            throw new MojoExecutionException("Failed to create assembly: " + e.getMessage(), e);
        } catch (final AssemblyFormattingException e) {
            throw new MojoExecutionException("Failed to create assembly: " + e.getMessage(), e);
        } catch (final InvalidAssemblerConfigurationException e) {
            throw new MojoFailureException(assembly, "Assembly is incorrectly configured: " + assembly.getId(),
                    "Assembly: " + assembly.getId() + " is not configured correctly: "
                            + e.getMessage());
        }
    }

    @Override
    public MavenProject getProject() {
        return project;
    }
}