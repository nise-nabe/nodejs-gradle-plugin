package com.nisecoder.gradle.plugin

import com.nisecoder.gradle.plugin.nodejs.NodeExtension
import com.nisecoder.gradle.plugin.nodejs.NodeProvisioningService
import com.nisecoder.gradle.plugin.nodejs.task.corepack.CorepackEnableTask
import com.nisecoder.gradle.plugin.nodejs.task.corepack.CorepackVersionTask
import com.nisecoder.gradle.plugin.nodejs.task.NodeVersionTask
import com.nisecoder.gradle.plugin.nodejs.task.base.ProvisionedNodeTask
import com.nisecoder.gradle.plugin.nodejs.task.corepack.CorepackDisableTask
import com.nisecoder.gradle.plugin.nodejs.task.npm.NpmInstallTask
import com.nisecoder.gradle.plugin.nodejs.task.npm.NpmVersionTask
import com.nisecoder.gradle.plugin.nodejs.task.pnpm.PnpmVersionTask
import com.nisecoder.gradle.plugin.nodejs.task.yarn.YarnInstallTask
import com.nisecoder.gradle.plugin.nodejs.task.yarn.YarnVersionTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.registerIfAbsent

@Suppress("unused")
class NodeJsPlugin: Plugin<Project> {
    /**
     * Apply this plugin to the given target object.
     *
     * @param target The target object
     */
    override fun apply(target: Project): Unit = target.run {
        val nodeExtension = extensions.create<NodeExtension>("nodejs").also {
            it.nodeVersion.convention("v16.14.2")
            it.installationDir.set(gradle.gradleUserHomeDir.resolve("nodejs"))
        }

        val nodeProvisioningServiceProvider = gradle.sharedServices.registerIfAbsent("nodeProvisioning", NodeProvisioningService::class) {
            parameters {
                nodeInstallationPath.set(nodeExtension.installationDir)
                // not to set node version to use different version for each projects
            }
        }

        fun ProvisionedNodeTask.prepare() {
            nodeProvisioningService.set(nodeProvisioningServiceProvider)
            nodeVersion.set(nodeExtension.nodeVersion)
        }

        // node tasks
        tasks {
            register<NodeVersionTask>("nodeVersion") {
                prepare()
            }
        }

        // npm tasks
        tasks {
            register<NpmVersionTask>("npmVersion") {
                prepare()
            }
            register<NpmInstallTask>("npmInstall") {
                prepare()

                packageLockFile.set(layout.projectDirectory.file("package-lock.json"))
            }
        }

        // corepack tasks
        tasks {
            register<CorepackVersionTask>("corepackVersion") {
                prepare()
            }

            register<CorepackDisableTask>("corepackDisable") {
                prepare()
            }
        }

        val corepackEnableTask = tasks.register<CorepackEnableTask>("corepackEnable") {
            prepare()
        }

        // yarn tasks
        tasks {
            register<YarnVersionTask>("yarnVersion") {
                prepare()

                dependsOn(corepackEnableTask)
            }

            register<YarnInstallTask>("yarnInstall") {
                prepare()

                dependsOn(corepackEnableTask)

                yarnLockFile.set(layout.projectDirectory.file("yarn.lock"))
            }
        }

        // pnpm tasks
        tasks {
            register<PnpmVersionTask>("pnpmVersion") {
                prepare()

                dependsOn(corepackEnableTask)
            }
        }
    }
}
