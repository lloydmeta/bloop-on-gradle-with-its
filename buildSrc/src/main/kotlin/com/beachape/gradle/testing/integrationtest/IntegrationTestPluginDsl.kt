package com.beachape.gradle.testing.integrationtest

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.`itImplementation`(dependencyNotation: Any): Dependency? =
        add(IntegrationTestPlugin.IT_IMPLEMENTATION_CONFIGURATION_NAME, dependencyNotation)

fun DependencyHandler.`itRuntimeOnly`(dependencyNotation: Any): Dependency? =
        add(IntegrationTestPlugin.IT_RUNTIME_ONLY_CONFIGURATION_NAME, dependencyNotation)