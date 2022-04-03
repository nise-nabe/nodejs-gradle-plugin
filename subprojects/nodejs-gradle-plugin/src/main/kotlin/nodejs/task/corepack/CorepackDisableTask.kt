package com.nisecoder.gradle.plugin.nodejs.task.corepack

abstract class CorepackDisableTask: CorepackTask() {
    override fun prepareArgs() {
        args("disable")

        // workaround for windows: corepack will discover `corepack.exe`
        args("--install-directory", nodePath.binDir.toString())
    }
}
