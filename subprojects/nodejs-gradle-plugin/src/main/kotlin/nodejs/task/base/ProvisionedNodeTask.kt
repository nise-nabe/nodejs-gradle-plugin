package com.nisecoder.gradle.plugin.nodejs.task.base

import com.nisecoder.gradle.plugin.nodejs.NodePath
import com.nisecoder.gradle.plugin.nodejs.NodeProvisioningService
import com.nisecoder.gradle.plugin.nodejs.NodeVersion
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Internal

abstract class ProvisionedNodeTask: AbstractExecTask() {
    @get:Internal
    abstract val nodeProvisioningService: Property<NodeProvisioningService>

    @get:Internal
    abstract val nodeVersion: Property<NodeVersion>

    @get:Internal
    val nodePath: NodePath by lazy {
        nodeProvisioningService.get().provision(nodeVersion.get())
    }
}
