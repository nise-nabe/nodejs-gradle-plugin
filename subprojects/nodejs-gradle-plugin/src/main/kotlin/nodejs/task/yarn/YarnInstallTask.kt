package com.nisecoder.gradle.plugin.nodejs.task.yarn

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.OutputFile

abstract class YarnInstallTask: YarnTask() {
    @get:OutputFile
    abstract val yarnLockFile: RegularFileProperty

    override fun prepareArgs() {
        args("install")
    }
}
