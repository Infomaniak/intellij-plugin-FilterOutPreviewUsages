import org.jetbrains.intellij.platform.gradle.extensions.intellijPlatform

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        id("org.jetbrains.kotlin.jvm") version "2.3.20"
        id("org.jetbrains.changelog") version "2.5.0"
    }
}

plugins {
    id("org.jetbrains.intellij.platform.settings") version "2.18.1"
}

rootProject.name = "FilterOutPreviewUsages"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        intellijPlatform {
            defaultRepositories()
        }
    }
}
