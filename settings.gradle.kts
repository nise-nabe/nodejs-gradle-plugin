rootProject.name = "nodejs-gradle-plugin"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
    plugins {
        id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.7"
        id("com.gradle.plugin-publish") version "1.2.1"
        kotlin("jvm") version embeddedKotlinVersion
        kotlin("plugin.serialization") version embeddedKotlinVersion
    }
}

dependencyResolutionManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

includeBuild("./build-logic")

include("nodejs-gradle-plugin")

for (project in rootProject.children) {
    val projectPath = file("subprojects/${project.name}")

    project.projectDir = projectPath
}
