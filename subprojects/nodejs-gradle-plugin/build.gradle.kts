import org.jetbrains.gradle.ext.settings
import org.jetbrains.gradle.ext.packagePrefix

plugins {
    java
    `maven-publish`
    id("com.gradle.plugin-publish")

    id("org.jetbrains.gradle.plugin.idea-ext")

    `kotlin-dsl-base`
    `java-gradle-plugin`

    kotlin("jvm")
    kotlin("plugin.serialization")
}

group = "com.nisecoder.gradle"

// inject in GitHub Action Publish Workflow
val publishVersion: String? by project
version = if (publishVersion?.isNotEmpty() == true) {
    publishVersion!!.replaceFirst("refs/tags/v", "")
} else {
    "1.0-SNAPSHOT"
}

pluginBundle {
    website = "https://github.com/nise-nabe/nodejs-gradle-plugin"
    vcsUrl = "https://github.com/nise-nabe/nodejs-gradle-plugin"
}

gradlePlugin {
    plugins {
        register("nodejs-plugin") {
            id = "com.nisecoder.nodejs"
            implementationClass = "com.nisecoder.gradle.plugin.NodeJsPlugin"
            description = "node.js integration"
        }
    }
}

idea {
    module {
        settings {
            val packageName = "com.nisecoder.gradle.plugin"
            packagePrefix["src/main/kotlin"] = packageName
            packagePrefix["src/test/kotlin"] = packageName
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation(gradleTestKit())
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.assertj:assertj-core:3.22.0")
}

tasks.test {
    useJUnitPlatform()
    maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).takeIf { it > 0 } ?: 1
}

publishing {
    repositories {
        maven {
            name = "GitHub"
            url = uri("https://maven.pkg.github.com/nise-nabe/nodejs-gradle-plugin")
            credentials(PasswordCredentials::class)
        }
    }
}
