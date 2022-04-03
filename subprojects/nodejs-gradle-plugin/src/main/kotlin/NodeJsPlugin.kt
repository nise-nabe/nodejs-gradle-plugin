package com.nisecoder.gradle.plugin

import com.nisecoder.gradle.plugin.nodejs.NodeBinaryTypeSelector
import com.nisecoder.gradle.plugin.nodejs.NodeExtension
import com.nisecoder.gradle.plugin.nodejs.NodeProvisioningService
import com.nisecoder.gradle.plugin.nodejs.task.CorepackVersionTask
import com.nisecoder.gradle.plugin.nodejs.task.NodeVersionTask
import com.nisecoder.gradle.plugin.nodejs.task.NpmVersionTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.invoke
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
            it.version.convention("v16.14.2")
        }

        val binaryType = NodeBinaryTypeSelector.select()

        val nodeCacheDir = gradle.gradleUserHomeDir.resolve("node")
        val nodeProvisioningServiceProvider = gradle.sharedServices.registerIfAbsent("nodeProvisioning", NodeProvisioningService::class) {
            parameters {
                nodeBinaryType.set(binaryType)
                nodeCachePath.set(nodeCacheDir)
            }
        }

        tasks {
            register<NodeVersionTask>("nodeVersion") {
                nodeProvisioningService.set(nodeProvisioningServiceProvider)
                nodeVersion.set(nodeExtension.version)
            }
            register<CorepackVersionTask>("corepackVersion") {
                nodeProvisioningService.set(nodeProvisioningServiceProvider)
                nodeVersion.set(nodeExtension.version)
            }
            register<NpmVersionTask>("npmVersion") {
                nodeProvisioningService.set(nodeProvisioningServiceProvider)
                nodeVersion.set(nodeExtension.version)
            }
        }
    }
}