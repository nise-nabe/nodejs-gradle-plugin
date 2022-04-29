package com.nisecoder.gradle.plugin

import com.nisecoder.gradle.plugin.nodejs.NodeExtension
import com.nisecoder.gradle.plugin.nodejs.NodeProvisioningService
import com.nisecoder.gradle.plugin.nodejs.lib.PackageJson
import com.nisecoder.gradle.plugin.nodejs.task.corepack.CorepackEnableTask
import com.nisecoder.gradle.plugin.nodejs.task.corepack.CorepackVersionTask
import com.nisecoder.gradle.plugin.nodejs.task.NodeVersionTask
import com.nisecoder.gradle.plugin.nodejs.task.base.ProvisionedNodeTask
import com.nisecoder.gradle.plugin.nodejs.task.corepack.CorepackDisableTask
import com.nisecoder.gradle.plugin.nodejs.task.npm.NpmInstallTask
import com.nisecoder.gradle.plugin.nodejs.task.npm.NpmVersionTask
import com.nisecoder.gradle.plugin.nodejs.task.pnpm.PnpmTask
import com.nisecoder.gradle.plugin.nodejs.task.pnpm.PnpmVersionTask
import com.nisecoder.gradle.plugin.nodejs.task.yarn.YarnInstallTask
import com.nisecoder.gradle.plugin.nodejs.task.yarn.YarnScriptTask
import com.nisecoder.gradle.plugin.nodejs.task.yarn.YarnTask
import com.nisecoder.gradle.plugin.nodejs.task.yarn.YarnVersionTask
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.registerIfAbsent
import org.gradle.kotlin.dsl.withType

@Suppress("unused")
class NodeJsPlugin: Plugin<Project> {
    /**
     * Apply this plugin to the given target object.
     *
     * @param target The target object
     */
    override fun apply(target: Project): Unit = target.run {
        val nodeExtension = extensions.create<NodeExtension>("nodejs").also {
            it.nodeVersion.fixed.convention("v16.14.2")
            it.installationDir.set(gradle.gradleUserHomeDir.resolve("nodejs"))
            it.scriptsPrefix.convention("")
        }

        val nodeProvisioningServiceProvider = gradle.sharedServices.registerIfAbsent("nodeProvisioning", NodeProvisioningService::class) {
            parameters {
                nodeInstallationPath.set(nodeExtension.installationDir)
                verifyChecksum.set(true)
                // not to set node version to use different version for each projects
            }
        }

        tasks.withType<ProvisionedNodeTask>().configureEach {
            nodeProvisioningService.set(nodeProvisioningServiceProvider)
            nodeVersion.set(nodeExtension.nodeVersion)
        }

        // node tasks
        tasks {
            register<NodeVersionTask>("nodeVersion")
        }

        // npm tasks
        tasks {
            register<NpmVersionTask>("npmVersion")
            register<NpmInstallTask>("npmInstall") {
                packageLockFile.set(layout.projectDirectory.file("package-lock.json"))
            }
        }

        // corepack tasks
        tasks {
            register<CorepackVersionTask>("corepackVersion")
            register<CorepackDisableTask>("corepackDisable")
        }

        val corepackEnableTask = tasks.register<CorepackEnableTask>("corepackEnable")

        // yarn tasks
        tasks {
            // yarn need to enable corepack
            withType<YarnTask>().configureEach {
                dependsOn(corepackEnableTask)
            }

            register<YarnVersionTask>("yarnVersion")

            register<YarnInstallTask>("yarnInstall") {
                yarnLockFile.set(layout.projectDirectory.file("yarn.lock"))
            }

            // workaround for configured task prefix
            afterEvaluate {
                file("package.json").takeIf { it.isFile }?.inputStream()?.use {
                    val parser = Json { ignoreUnknownKeys = true }
                    parser.decodeFromStream<PackageJson>(it)
                }?.let {
                    it.scripts.forEach { (t, _) ->
                        val scriptPrefix = nodeExtension.scriptsPrefix.get().capitalize()
                        val scriptTaskName = t.capitalize()
                        val taskName = "yarn$scriptPrefix$scriptTaskName"
                        register<YarnScriptTask>(taskName) {
                            script.set(t)
                        }
                    }
                }
            }
        }

        // pnpm tasks
        tasks {
            // pnpm need to enable corepack
            withType<PnpmTask>().configureEach {
                dependsOn(corepackEnableTask)
            }

            register<PnpmVersionTask>("pnpmVersion") {
                dependsOn(corepackEnableTask)
            }
        }
    }
}
