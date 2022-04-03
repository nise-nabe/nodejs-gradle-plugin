package com.nisecoder.gradle.plugin.nodejs.task.pnpm

import com.nisecoder.gradle.plugin.nodejs.task.base.ProvisionedNodeTask

abstract class PnpmTask: ProvisionedNodeTask() {
    override val executable by lazy { nodePath.resolve("pnpm") }
}
