package com.nisecoder.gradle.plugin.nodejs.task

abstract class NpmVersionTask: NpmTask() {
    override fun prepareArgs() {
        args("-v")
    }
}
