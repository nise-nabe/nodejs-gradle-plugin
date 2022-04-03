package com.nisecoder.gradle.plugin

import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.intellij.lang.annotations.Language
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
    fun nodeVersion() {
        buildFile.writeKotlin("""
          plugins {
            id("com.nisecoder.nodejs")
          }
        """.trimIndent())

        val buildResult = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withPluginClasspath()
            .withArguments("nodeVersion")
            .build()

        val taskResult = buildResult.task(":nodeVersion")
        assertNotNull(taskResult)
        assertThat(taskResult.outcome).isIn(TaskOutcome.SUCCESS)
        assertThat(buildResult.output.split(System.lineSeparator()).map { it.trim() }).contains("v16.14.2")
    }

    @Test
    fun corepackVersion() {
        buildFile.writeKotlin("""
          plugins {
            id("com.nisecoder.nodejs")
          }
        """.trimIndent())

        val buildResult = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withPluginClasspath()
            .withArguments(":corepackVersion")
            .build()

        val taskResult = buildResult.task(":corepackVersion")
        assertNotNull(taskResult)
        assertThat(taskResult.outcome).isIn(TaskOutcome.SUCCESS)
        assertThat(buildResult.output.split(System.lineSeparator()).map { it.trim() }).contains("0.10.0")
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
        assertThat(taskResult.outcome).isIn(TaskOutcome.SUCCESS)
    }

    @Test
    fun npmVersion() {
        buildFile.writeKotlin("""
          plugins {
            id("com.nisecoder.nodejs")
          }
        """.trimIndent())

        val buildResult = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withPluginClasspath()
            .withArguments(":npmVersion")
            .build()

        val taskResult = buildResult.task(":npmVersion")
        assertNotNull(taskResult)
        assertThat(taskResult.outcome).isIn(TaskOutcome.SUCCESS)
        assertThat(buildResult.output.split(System.lineSeparator()).map { it.trim() }).contains("8.5.0")
    }

    private fun File.writeKotlin(@Language("gradle.kts") src: String) {
        writeText(src)
    }
}
