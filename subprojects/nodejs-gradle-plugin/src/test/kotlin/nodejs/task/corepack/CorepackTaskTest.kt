package com.nisecoder.gradle.plugin.nodejs.task.corepack

import com.nisecoder.gradle.plugin.utils.writeKotlin
import org.assertj.core.api.Assertions
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.assertNotNull

internal class CorepackTaskTest {
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
    fun corepackEnable() {
        buildFile.writeKotlin("""
          plugins {
            id("com.nisecoder.nodejs")
          }
        """.trimIndent())

        val buildResult = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withPluginClasspath()
            .withArguments(":corepackEnable")
            .build()

        val taskResult = buildResult.task(":corepackEnable")
        assertNotNull(taskResult)
        Assertions.assertThat(taskResult.outcome).isIn(TaskOutcome.SUCCESS)
    }

    @Test
    fun corepackDisable() {
        buildFile.writeKotlin("""
          plugins {
            id("com.nisecoder.nodejs")
          }
        """.trimIndent())

        val buildResult = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withPluginClasspath()
            .withArguments(":corepackDisable")
            .build()

        val taskResult = buildResult.task(":corepackDisable")
        assertNotNull(taskResult)
        Assertions.assertThat(taskResult.outcome).isIn(TaskOutcome.SUCCESS)
    }
}
