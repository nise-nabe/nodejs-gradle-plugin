package com.nisecoder.gradle.plugin.nodejs.task

import com.nisecoder.gradle.plugin.nodejs.NodeBinary
import com.nisecoder.gradle.plugin.nodejs.NodeProvisioningService
import org.gradle.api.internal.file.FileOperations
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.Internal
import java.nio.file.Path
import javax.inject.Inject

abstract class NodeTask: Exec() {
    init {
        group = "nodejs"
    }

    @get:Internal
    abstract val nodeProvisioningService: Property<NodeProvisioningService>

    @get:Inject
    abstract val fileOperations: FileOperations

    @get:Internal
    abstract val nodeVersion: Property<String>

    private val nodeBinary: NodeBinary by lazy {
        nodeProvisioningService.get().provision(fileOperations, nodeVersion.get())
    }

    @get:Internal
    val node: Path by lazy { nodeBinary.node }
    @get:Internal
    val npm: Path by lazy { nodeBinary.npm }
    @get:Internal
    val npx: Path by lazy { nodeBinary.npx }
}
