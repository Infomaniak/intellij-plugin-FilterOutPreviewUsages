import org.jetbrains.changelog.Changelog
import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.intellij.platform")
    id("org.jetbrains.changelog")
}

group = providers.gradleProperty("pluginGroup").get()
version = providers.gradleProperty("pluginVersion").get()

kotlin {
    jvmToolchain(21)
}

// Read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html
dependencies {
    intellijPlatform {
        intellijIdea(providers.gradleProperty("platformVersion").get())

        // Kotlin PSI is needed to inspect annotations of the declarations surrounding a usage.
        bundledPlugin("org.jetbrains.kotlin")

        testFramework(TestFrameworkType.Platform)
    }

    testImplementation("junit:junit:4.13.2")
    testImplementation(kotlin("test-junit"))
}

// Read more: https://github.com/JetBrains/gradle-changelog-plugin
changelog {
    // The section of CHANGELOG.md to render is the one matching the current pluginVersion.
    version = providers.gradleProperty("pluginVersion")
    // Do not pre-create empty "Added", "Fixed", ... sections when running patchChangelog.
    groups.empty()
}

// Reading the file through the provider API registers it as a configuration cache input, so editing the changelog
// correctly invalidates the cache. The rendering itself has to be eager: referencing the `changelog` extension from
// inside a lazy provider would make the configuration cache try, and fail, to serialize the Gradle Project.
val changeNotesHtml = providers.fileContents(layout.projectDirectory.file("CHANGELOG.md")).asText.get().let {
    with(changelog) {
        renderItem(
            (getOrNull(version.get()) ?: getUnreleased()).withHeader(false).withEmptySections(false),
            Changelog.OutputType.HTML,
        )
    }
}

intellijPlatform {
    pluginConfiguration {
        version = providers.gradleProperty("pluginVersion")

        // The <change-notes> shown by the Marketplace are generated from CHANGELOG.md, the single source of truth.
        changeNotes = provider { changeNotesHtml }

        ideaVersion {
            sinceBuild = providers.gradleProperty("pluginSinceBuild")
            untilBuild = provider { null }
        }
    }

    pluginVerification {
        ides {
            recommended()
        }
    }
}


