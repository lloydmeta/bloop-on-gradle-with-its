package com.beachape.gradle.testing.integrationtest;

import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.UnknownDomainObjectException;
import org.gradle.api.artifacts.UnknownConfigurationException;
import org.gradle.api.internal.plugins.PluginApplicationException;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class IntegrationTestPluginTest {

    public File createTree(File baseFolder, Path path) throws IOException {
        File result = baseFolder.toPath().resolve(path).toFile();
        File toEnsureExists;
        if (result.isFile()) {
            toEnsureExists = result.getParentFile();
        } else {
            toEnsureExists = result;
        }
        if (!toEnsureExists.exists()) {
            toEnsureExists.mkdirs();
        }
        return result;
    }

    @Test
    public void testApplyPlugin() {
        Project project = ProjectBuilder.builder().build();
        project.getPluginManager().apply("java");
        project.getPluginManager().apply("integration-test");
        assertNotNull(project.getPlugins().getPlugin("integration-test"));
        assertNotNull(project.getTasks().getByName(IntegrationTestPlugin.TASK_NAME));
    }

    @Test()
    public void testApplyPluginWithoutJavaPluginShouldFail() {
        assertThrows(PluginApplicationException.class, () -> {
            Project project = ProjectBuilder.builder().build();
            project.getPluginManager().apply("integration-test");
        });
    }

    @Test
    public void testApplyPluginShouldCreateItSourceSet() throws IOException {
        Project project = ProjectBuilder.builder().build();
        createTree(project.getProjectDir(), Paths.get("src" ,"it"));
        project.getPluginManager().apply("java");
        project.getPluginManager().apply("integration-test");

        assertNotNull(project.getConvention().getPlugin(JavaPluginConvention.class).getSourceSets().getByName(IntegrationTestPlugin.IT_SOURCE_SET_NAME));
    }

    @Test
    public void testApplyPluginShouldNotCreateItSourceSetIfSourceDirDoesNotExist() {
        Project project = ProjectBuilder.builder().build();
        project.getPluginManager().apply("java");
        project.getPluginManager().apply("integration-test");

        assertThrows(UnknownDomainObjectException.class, () -> project.getConvention().getPlugin(JavaPluginConvention.class).getSourceSets().getByName(IntegrationTestPlugin.IT_SOURCE_SET_NAME));
    }

    @Test
    public void testApplyPluginShouldCreateItConfigurations() throws IOException {
        Project project = ProjectBuilder.builder().build();
        createTree(project.getProjectDir(), Paths.get("src" ,"it"));
        project.getPluginManager().apply("java");
        project.getPluginManager().apply("integration-test");

        assertNotNull(project.getConfigurations().getByName(IntegrationTestPlugin.IT_IMPLEMENTATION_CONFIGURATION_NAME));
        assertNotNull(project.getConfigurations().getByName(IntegrationTestPlugin.IT_RUNTIME_ONLY_CONFIGURATION_NAME));
    }

    @Test
    public void testApplyPluginShouldNotCreateItConfigurationsIfSourceDirDoesNotExist() {
        Project project = ProjectBuilder.builder().build();
        project.getPluginManager().apply("java");
        project.getPluginManager().apply("integration-test");

        assertThrows(UnknownConfigurationException.class, () -> project.getConfigurations().getByName(IntegrationTestPlugin.IT_IMPLEMENTATION_CONFIGURATION_NAME));
        assertThrows(UnknownConfigurationException.class, () -> project.getConfigurations().getByName(IntegrationTestPlugin.IT_RUNTIME_ONLY_CONFIGURATION_NAME));
    }

    @Test
    public void testApplyPluginShouldCreateIntegrationTestTask() {
        Project project = ProjectBuilder.builder().build();
        project.getPluginManager().apply("java");
        project.getPluginManager().apply("integration-test");

        assertNotNull(project.getTasks().getByName(IntegrationTestPlugin.TASK_NAME));
    }

    @Test
    public void testApplyPluginCheckTaskDependOnIntegrationTest() {
        Project project = ProjectBuilder.builder().build();
        project.getPluginManager().apply("java");
        project.getPluginManager().apply("integration-test");

        Task checkTask = project.getTasks().getByName("check");
        Set<? extends Task> checkDependencies = checkTask.getTaskDependencies().getDependencies(checkTask);
        Optional<? extends Task> actual = checkDependencies.stream().filter(task -> task.getName().equals("integrationTest")).findFirst();
        assertTrue(actual.isPresent());
    }
}