package com.nisecoder.gradle.plugin.nodejs.task.npm

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.OutputFile

abstract class NpmInstallTask: NpmTask() {
    @get:OutputFile
    abstract val packageLockFile: RegularFileProperty

    override fun prepareArgs() {
        args("install")
    }
}
