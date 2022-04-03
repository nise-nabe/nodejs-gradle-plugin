package com.nisecoder.gradle.plugin.nodejs.task

import com.nisecoder.gradle.plugin.nodejs.task.base.ProvisionedNodeTask

abstract class NodeTask: ProvisionedNodeTask() {
    init {
        group = "nodejs"
    }

    override val executable by lazy { nodePath.node }
}
