package com.nisecoder.gradle.plugin.nodejs.task

abstract class NodeTask: ProvisionedNodeTask() {
    init {
        group = "nodejs"
    }

    override val executable by lazy { nodePath.node }
}
