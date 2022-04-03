package com.nisecoder.gradle.plugin.nodejs.task

abstract class CorepackEnableTask: CorepackTask() {
    override fun setupArgs() {
        args("enable")

        // workaround for windows: corepack will discover `corepack.exe`
        args("--install-directory", nodePath.installDir)
    }
}
