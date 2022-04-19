package com.nisecoder.gradle.plugin.nodejs.lib

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class PackageJsonTest {
    @Test
    fun deserializeTest() {
        // language=package.json
        val packageJsonString = """
            {
                "scripts": {
                    "build": "webpack",
                    "test": "jest"
                }
            }
        """.trimIndent()

        val packageJson = Json {
            ignoreUnknownKeys = true
        }.decodeFromString<PackageJson>(packageJsonString)

        assertNotNull(packageJson.scripts["build"]) {
            assertEquals("webpack", it)
        }
        assertNotNull(packageJson.scripts["test"]) {
            assertEquals("jest", it)
        }
    }

}