package com.nisecoder.gradle.plugin.nodejs.task.yarn

import com.nisecoder.gradle.plugin.nodejs.task.base.ProvisionedNodeTask
import java.nio.file.Path

abstract class YarnTask: ProvisionedNodeTask() {
    init {
        group = "yarn"
    }
    override val executable: Path by lazy { nodePath.resolve("yarn") }
}
