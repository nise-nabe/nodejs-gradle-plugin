package com.nisecoder.gradle.plugin.nodejs.task

abstract class CorepackTask: ProvisionedNodeTask() {
    override val executable by lazy { nodePath.corepack }
}
