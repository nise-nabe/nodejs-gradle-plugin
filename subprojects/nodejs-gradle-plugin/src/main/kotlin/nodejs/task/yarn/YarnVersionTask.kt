package com.nisecoder.gradle.plugin.nodejs.task.yarn

abstract class YarnVersionTask: YarnTask() {
    override fun prepareArgs() {
        args("-v")
    }
}
