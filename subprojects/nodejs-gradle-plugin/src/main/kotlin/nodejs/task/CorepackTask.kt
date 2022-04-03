package com.nisecoder.gradle.plugin.nodejs.task

import org.gradle.api.tasks.TaskAction

abstract class CorepackTask: NodeTask() {
    @TaskAction
    override fun exec() {
        commandLine(nodePath.corepack.toString())
        setupArgs()
        super.exec()
    }

    /**
     * use args() to execute corepack
     */
    open fun setupArgs() {

    }
}
