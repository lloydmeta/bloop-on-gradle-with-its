plugins {
    id("java")
    kotlin("jvm") version "1.3.61"
    id("java-gradle-plugin")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.61")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.2")
    testImplementation("org.mockito:mockito-core:2.23.4")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.5.2")
}

repositories {
    mavenCentral()
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}
tasks.test {
    useJUnitPlatform()
}

gradlePlugin {
    plugins {
        create("integration-test") {
            id = "integration-test"
            implementationClass = "com.beachape.gradle.testing.integrationtest.IntegrationTestPlugin"
        }
    }
}
