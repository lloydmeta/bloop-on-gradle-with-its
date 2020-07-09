rootProject.name = "bloop-on-gradle-with-its"

buildscript {

    repositories {
        // Add here whatever repositories you're already using
        mavenCentral()
    }

    dependencies {
        classpath("ch.epfl.scala:gradle-bloop_2.12:1.4.3")
    }


}