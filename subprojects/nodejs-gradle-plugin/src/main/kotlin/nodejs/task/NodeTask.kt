package com.nisecoder.gradle.plugin.nodejs.task

import com.nisecoder.gradle.plugin.nodejs.NodePath
import com.nisecoder.gradle.plugin.nodejs.NodeProvisioningService
import org.gradle.api.file.ArchiveOperations
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Internal
import java.nio.file.Path
import javax.inject.Inject

abstract class NodeTask: AbstractNodeTask() {
    init {
        group = "nodejs"
    }

    @get:Internal
    abstract val nodeProvisioningService: Property<NodeProvisioningService>

    @get:Inject
    abstract val archiveOperations: ArchiveOperations

    @get:Inject
    abstract val fileSystemOperations: FileSystemOperations

    @get:Internal
    abstract val nodeVersion: Property<String>

    @get:Internal
    val nodePath: NodePath by lazy {
        nodeProvisioningService.get().provision(archiveOperations, fileSystemOperations, nodeVersion.get())
    }

    @get:Internal
    val node: Path by lazy { nodePath.node }
}
