plugins {
    java
    `maven-publish`
    `java-gradle-plugin`
    id("com.gradle.plugin-publish")
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
