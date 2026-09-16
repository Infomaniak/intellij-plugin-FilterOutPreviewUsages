import org.jetbrains.intellij.platform.gradle.extensions.intellijPlatform

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        id("org.jetbrains.kotlin.jvm") version "2.4.20"
        id("org.jetbrains.changelog") version "2.5.0"
    }
}

plugins {
    id("org.jetbrains.intellij.platform.settings") version "2.19.0"
}

rootProject.name = "MoreUsageFilters"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        intellijPlatform {
            defaultRepositories()
        }
    }
}
