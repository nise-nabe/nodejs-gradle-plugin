package com.nisecoder.gradle.plugin.nodejs.task.npm

abstract class NpmVersionTask: NpmTask() {
    override fun prepareArgs() {
        args("-v")
    }
}
