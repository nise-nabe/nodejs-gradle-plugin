package com.nisecoder.gradle.plugin.nodejs.task.corepack

abstract class CorepackVersionTask: CorepackTask() {
    override fun prepareArgs() {
        args("-v")
    }
}
