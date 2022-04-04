package com.nisecoder.gradle.plugin.nodejs.task

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

internal class ConfiguredVersionTaskTest {
    companion object {
        const val NODE_VERSION = "v17.8.0"
        const val NPM_VERSION = "8.5.5"
        const val COREPACK_VERSION = "0.10.0"
        const val YARN_VERSION = "1.22.15"
        const val PNPM_VERSION = "6.11.0"
    }

    @TempDir
    private lateinit var testProjectDir: File

    private lateinit var settingsFile: File
    private lateinit var buildFile: File

    private lateinit var packageJson: File

    @BeforeEach
    fun setup() {
        settingsFile = testProjectDir.resolve("settings.gradle.kts")
        buildFile = testProjectDir.resolve("build.gradle.kts").apply {
            writeKotlin("""
              plugins {
                  id("com.nisecoder.nodejs")
              }

              nodejs {
                nodeVersion("v17.8.0")
             }
            """.trimIndent())
        }
        packageJson = testProjectDir.resolve("package.json")
    }

    @Test
    fun nodeVersion() {
        val buildResult = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withPluginClasspath()
            .withArguments("nodeVersion")
            .build()

        val taskResult = buildResult.task(":nodeVersion")
        assertNotNull(taskResult)
        assertThat(taskResult.outcome).isIn(TaskOutcome.SUCCESS)
        assertThat(buildResult.output.splitLine()).contains(NODE_VERSION)
    }


    @Test
    fun npmVersion() {
        val buildResult = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withPluginClasspath()
            .withArguments(":npmVersion")
            .build()

        val taskResult = buildResult.task(":npmVersion")
        assertNotNull(taskResult)
        assertThat(taskResult.outcome).isIn(TaskOutcome.SUCCESS)
        assertThat(buildResult.output.splitLine()).contains(NPM_VERSION)
    }

    @Test
    fun corepackVersion() {
        val buildResult = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withPluginClasspath()
            .withArguments(":corepackVersion")
            .build()

        val taskResult = buildResult.task(":corepackVersion")
        assertNotNull(taskResult)
        assertThat(taskResult.outcome).isIn(TaskOutcome.SUCCESS)
        assertThat(buildResult.output.splitLine()).contains(COREPACK_VERSION)
    }

    @Test
    fun yarnVersion() {
        val buildResult = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withPluginClasspath()
            .withArguments(":yarnVersion")
            .build()

        val taskResult = buildResult.task(":yarnVersion")
        assertNotNull(taskResult)
        assertThat(taskResult.outcome).isIn(TaskOutcome.SUCCESS)
        assertThat(buildResult.output.splitLine()).contains(YARN_VERSION)
    }

    @Test
    fun pnpmVersion() {
        val buildResult = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withPluginClasspath()
            .withArguments(":pnpmVersion")
            .build()

        val taskResult = buildResult.task(":pnpmVersion")
        assertNotNull(taskResult)
        assertThat(taskResult.outcome).isIn(TaskOutcome.SUCCESS)
        assertThat(buildResult.output.splitLine()).contains(PNPM_VERSION)
    }


    @Test
    fun invalidVersion() {
        buildFile.writeKotlin("""
              plugins {
                  id("com.nisecoder.nodejs")
              }
 
              nodejs{
                // version should start `v`
                nodeVersion("16.14.2")
              }
            """.trimIndent())

        val buildResult = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withPluginClasspath()
            .withArguments("nodeVersion")
            .buildAndFail()

        val taskResult = buildResult.task(":nodeVersion")
        assertNotNull(taskResult)
        assertThat(taskResult.outcome).isIn(TaskOutcome.FAILED)
        val errorMessage = "> node 16.14.2 is not found. Please check https://nodejs.org/en/download/releases/"
        assertThat(buildResult.output.splitLine()).contains(errorMessage)
    }
}
