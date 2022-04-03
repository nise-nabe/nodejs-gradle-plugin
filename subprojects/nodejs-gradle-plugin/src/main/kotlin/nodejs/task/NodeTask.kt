package com.nisecoder.gradle.plugin.nodejs.task

import com.nisecoder.gradle.plugin.nodejs.NodePath
import com.nisecoder.gradle.plugin.nodejs.NodeProvisioningService
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Internal
import java.nio.file.Path

abstract class NodeTask: AbstractNodeTask() {
    init {
        group = "nodejs"
    }

    @get:Internal
    abstract val nodeProvisioningService: Property<NodeProvisioningService>

    @get:Internal
    abstract val nodeVersion: Property<String>

    @get:Internal
    val nodePath: NodePath by lazy {
        nodeProvisioningService.get().provision(nodeVersion.get())
    }

    @get:Internal
    val node: Path by lazy { nodePath.node }
}
