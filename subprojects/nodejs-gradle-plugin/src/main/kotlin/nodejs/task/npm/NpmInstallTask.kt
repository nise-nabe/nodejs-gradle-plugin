package com.nisecoder.gradle.plugin.nodejs.task.npm

abstract class NpmInstallTask: NpmTask() {
    override fun prepareArgs() {
        args("install")
    }
}
