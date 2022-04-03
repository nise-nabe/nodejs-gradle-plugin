package com.nisecoder.gradle.plugin.nodejs.task

import org.gradle.api.tasks.TaskAction

abstract class CorepackTask: NodeTask() {
    @TaskAction
    override fun exec() {
        execSpec.commandLine(nodePath.corepack.toString())
        setupArgs()
        super.exec()
    }

    /**
     * use args() to execute corepack
     */
    protected open fun setupArgs() {

    }
}
