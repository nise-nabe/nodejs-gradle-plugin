package com.nisecoder.gradle.plugin.nodejs.task.npm

import com.nisecoder.gradle.plugin.nodejs.task.base.ProvisionedNodeTask

abstract class NpmTask: ProvisionedNodeTask() {
    init {
        group = "npm"
    }

    override val executable by lazy { nodePath.npm }
}
