package com.nisecoder.gradle.plugin.nodejs.task.yarn

abstract class YarnInstallTask: YarnTask() {
    override fun prepareArgs() {
        args("install")
    }
}
