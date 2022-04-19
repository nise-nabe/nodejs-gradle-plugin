package com.nisecoder.gradle.plugin.nodejs.task.yarn

import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input

abstract class YarnScriptTask: YarnTask() {
    @get:Input
    abstract val script: Property<String>

    override fun prepareArgs() {
        args(script.get())
    }
}