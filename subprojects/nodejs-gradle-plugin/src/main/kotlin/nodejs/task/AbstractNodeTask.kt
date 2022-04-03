package com.nisecoder.gradle.plugin.nodejs.task

import org.gradle.api.DefaultTask
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.newInstance
import org.gradle.process.ExecResult
import org.gradle.process.internal.DefaultExecSpec
import org.gradle.process.internal.ExecActionFactory
import javax.inject.Inject

abstract class AbstractNodeTask: DefaultTask() {
    @get:Internal
    abstract val execResult: Property<ExecResult>

    @get:Inject
    abstract val objects: ObjectFactory

    @get:Inject
    abstract val execActionFactory: ExecActionFactory

    @get:Internal
    protected val execSpec: DefaultExecSpec by lazy { objects.newInstance() }

    @TaskAction
    open fun exec() {
        val action = execActionFactory.newExecAction()
        execSpec.copyTo(action)
        execResult.set(action.execute())
    }
}
