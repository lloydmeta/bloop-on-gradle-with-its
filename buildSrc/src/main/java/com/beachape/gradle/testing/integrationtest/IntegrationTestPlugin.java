package com.beachape.gradle.testing.integrationtest;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.testing.Test;

import java.io.File;
import java.util.function.Predicate;

public class IntegrationTestPlugin implements Plugin<Project> {

    public static final String IT_SOURCE_SET_NAME = "it";
    public static final String IT_IMPLEMENTATION_CONFIGURATION_NAME = IT_SOURCE_SET_NAME + "Implementation";
    public static final String IT_RUNTIME_ONLY_CONFIGURATION_NAME = IT_SOURCE_SET_NAME + "RuntimeOnly";
    public static final String TASK_NAME = "integrationTest";

    @Override
    public void apply(Project project) {
        TaskProvider<Test> integrationTestTask = project.getTasks().register(TASK_NAME, Test.class, task -> {
            task.setDescription("Runs integration tests.");
            task.setGroup("verification");
            task.mustRunAfter("test");
        });

        project.getTasks().getByName("check").dependsOn(integrationTestTask);


        if (new File(project.getProjectDir().getPath() + "/src/it").exists()) {
            JavaPluginConvention javaConvention = project.getConvention().getPlugin(JavaPluginConvention.class);
            SourceSet itSourceSet = javaConvention.getSourceSets().create(IT_SOURCE_SET_NAME, sourceSet -> {
                sourceSet.setCompileClasspath(sourceSet.getCompileClasspath().plus(javaConvention.getSourceSets().getByName(SourceSet.MAIN_SOURCE_SET_NAME).getOutput()));
                sourceSet.setRuntimeClasspath(sourceSet.getRuntimeClasspath().plus(javaConvention.getSourceSets().getByName(SourceSet.MAIN_SOURCE_SET_NAME).getOutput()));
            });
            project.getConfigurations().getByName(IT_IMPLEMENTATION_CONFIGURATION_NAME).extendsFrom(project.getConfigurations().getByName(JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME));
            project.getConfigurations().getByName(IT_RUNTIME_ONLY_CONFIGURATION_NAME).extendsFrom(project.getConfigurations().getByName(JavaPlugin.RUNTIME_ONLY_CONFIGURATION_NAME));

            integrationTestTask.get().setTestClassesDirs(itSourceSet.getOutput().getClassesDirs());
            integrationTestTask.get().setClasspath(itSourceSet.getRuntimeClasspath());
        }
    }
}
