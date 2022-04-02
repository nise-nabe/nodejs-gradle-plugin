package com.nisecoder.gradle.plugin

import com.nisecoder.gradle.plugin.nodejs.NodeBinaryTypeSelector
import com.nisecoder.gradle.plugin.nodejs.NodeExtension
import com.nisecoder.gradle.plugin.nodejs.NodeProvisioningService
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.registerIfAbsent

@Suppress("unused")
class NodeJsPlugin: Plugin<Project> {
    /**
     * Apply this plugin to the given target object.
     *
     * @param target The target object
     */
    override fun apply(target: Project): Unit = target.run {
        extensions.create<NodeExtension>("nodejs").also {
            it.version.convention("v16.14.2")
        }

        val binaryType = NodeBinaryTypeSelector.select()

        val nodeCacheDir = gradle.gradleUserHomeDir.resolve("node")
        gradle.sharedServices.registerIfAbsent("nodeProvisioning", NodeProvisioningService::class) {
            parameters {
                nodeBinaryType.set(binaryType)
                nodeCachePath.set(nodeCacheDir)
            }
        }
    }
}
