package com.nisecoder.gradle.plugin.nodejs.task.yarn

import com.nisecoder.gradle.plugin.utils.splitLine
import com.nisecoder.gradle.plugin.utils.writeJson
import com.nisecoder.gradle.plugin.utils.writeKotlin
import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.assertNotNull

internal class YarnScriptTaskTest {
    @TempDir
    private lateinit var testProjectDir: File

    private lateinit var settingsFile: File
    private lateinit var buildFile: File

    private lateinit var packageJson: File

    @BeforeEach
    fun setup() {
        settingsFile = testProjectDir.resolve("settings.gradle.kts")
        buildFile = testProjectDir.resolve("build.gradle.kts")
        packageJson = testProjectDir.resolve("package.json")

        buildFile.writeKotlin("""
          plugins {
            id("com.nisecoder.nodejs")
          }
        """.trimIndent())
    }

    @Test
    fun yarnBuildUsingScripts() {
        packageJson.writeJson("""
            { 
                "scripts": {
                    "build": "build"
                }
            }
        """.trimIndent())

        val buildResult = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withPluginClasspath()
            .withArguments(":tasks")
            .build()

        val taskResult = buildResult.task(":tasks")
        assertNotNull(taskResult)
        assertThat(taskResult.outcome).isIn(TaskOutcome.SUCCESS)
        assertThat(buildResult.output.splitLine()).contains("yarnBuild")
    }
    @Test
    fun yarnBuildUsingScriptsWithPrefix() {
        buildFile.writeKotlin("""
          plugins {
            id("com.nisecoder.nodejs")
          }
          nodejs {
            scriptsPrefix.set("script")
          }         

        """.trimIndent())

        packageJson.writeJson("""
            { 
                "scripts": {
                    "build": "build"
                }
            }
        """.trimIndent())

        val buildResult = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withPluginClasspath()
            .withArguments(":tasks")
            .build()

        val taskResult = buildResult.task(":tasks")
        assertNotNull(taskResult)
        assertThat(taskResult.outcome).isIn(TaskOutcome.SUCCESS)
        assertThat(buildResult.output.splitLine()).contains("yarnScriptBuild")
    }
}
