package com.nisecoder.gradle.plugin.nodejs.task.corepack

abstract class CorepackEnableTask: CorepackTask() {
    override fun prepareArgs() {
        args("enable")

        // workaround for windows: corepack will discover `corepack.exe`
        args("--install-directory", nodePath.binDir.toString())
    }
}
