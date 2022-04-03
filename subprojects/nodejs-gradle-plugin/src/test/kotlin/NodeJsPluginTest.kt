package com.nisecoder.gradle.plugin

import com.nisecoder.gradle.plugin.utils.splitLine
import com.nisecoder.gradle.plugin.utils.writeKotlin
import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.assertNotNull

internal class NodeJsPluginTest {
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
    }

    @Test
    fun applyTest() {
        buildFile.writeKotlin("""
          plugins {
            id("com.nisecoder.nodejs")
          }
        """.trimIndent())

        val buildResult = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withPluginClasspath()
            .withArguments(":help")
            .build()

        val taskResult = buildResult.task(":help")
        assertNotNull(taskResult)
        assertThat(taskResult.outcome).isIn(TaskOutcome.SUCCESS)
    }

    @Test
    fun configurationCache() {
        buildFile.writeKotlin("""
          plugins {
            id("com.nisecoder.nodejs")
          }
        """.trimIndent())

        GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withPluginClasspath()
            .withArguments("--configuration-cache", ":help")
            .build()

        val buildResult = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withPluginClasspath()
            .withArguments("--configuration-cache", ":help")
            .build()

        val taskResult = buildResult.task(":help")
        assertNotNull(taskResult)
        assertThat(taskResult.outcome).isIn(TaskOutcome.SUCCESS)
        assertThat(buildResult.output.splitLine()).contains("Reusing configuration cache.")
    }
}
