package com.nisecoder.gradle.plugin.nodejs.task

import org.gradle.api.tasks.TaskAction

abstract class NpmVersionTask: NodeTask() {
    @TaskAction
    override fun exec() {
        commandLine(nodePath.npm, "-v")

        super.exec()
    }
}
