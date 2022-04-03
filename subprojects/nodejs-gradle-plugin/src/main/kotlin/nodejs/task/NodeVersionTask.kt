package com.nisecoder.gradle.plugin.nodejs.task

import org.gradle.api.tasks.TaskAction

abstract class NodeVersionTask: NodeTask() {
    @TaskAction
    override fun exec() {
        execSpec.commandLine(node, "-v")

        super.exec()
    }
}
