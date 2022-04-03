package com.nisecoder.gradle.plugin.nodejs.task

abstract class NodeVersionTask: NodeTask() {
    override fun prepareArgs() {
        args("-v")
    }
}
