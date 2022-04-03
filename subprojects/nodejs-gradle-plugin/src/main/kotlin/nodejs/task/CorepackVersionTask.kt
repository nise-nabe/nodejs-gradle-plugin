package com.nisecoder.gradle.plugin.nodejs.task

abstract class CorepackVersionTask: CorepackTask() {
    override fun prepareArgs() {
        args("-v")
    }
}
