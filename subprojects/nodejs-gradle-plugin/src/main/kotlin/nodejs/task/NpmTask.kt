package com.nisecoder.gradle.plugin.nodejs.task

abstract class NpmTask: ProvisionedNodeTask() {
    init {
        group = "npm"
    }

    override val executable by lazy { nodePath.npm }
}
