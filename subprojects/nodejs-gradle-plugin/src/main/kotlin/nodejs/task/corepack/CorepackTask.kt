package com.nisecoder.gradle.plugin.nodejs.task.corepack

import com.nisecoder.gradle.plugin.nodejs.task.base.ProvisionedNodeTask

abstract class CorepackTask: ProvisionedNodeTask() {
    override val executable by lazy { nodePath.corepack }
}
