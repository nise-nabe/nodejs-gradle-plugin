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
