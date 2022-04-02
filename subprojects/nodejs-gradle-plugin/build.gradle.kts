import org.jetbrains.gradle.ext.settings
import org.jetbrains.gradle.ext.packagePrefix

plugins {
    java
    `maven-publish`
    `java-gradle-plugin`
    id("com.gradle.plugin-publish")

    id("org.jetbrains.gradle.plugin.idea-ext")
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
            packagePrefix["src/main/kotlin"] = "com.nisecoder.gradle.plugin"
            packagePrefix["src/test/kotlin"] = "com.nisecoder.gradle.plugin"
        }
    }
}
