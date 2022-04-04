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

internal class InvalidVersionTaskTest {
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
 
              nodejs{
                // version should start `v`
                nodeVersion("16.14.2")
              }
            """.trimIndent())
        }
        packageJson = testProjectDir.resolve("package.json")
    }

    @Test
    fun errorResult() {
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
