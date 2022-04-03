package com.nisecoder.gradle.plugin.nodejs.task

abstract class CorepackEnableTask: CorepackTask() {
    override fun setupArgs() {
        execSpec.args("enable")

        // workaround for windows: corepack will discover `corepack.exe`
        execSpec.args("--install-directory", nodePath.installDir)
    }
}
