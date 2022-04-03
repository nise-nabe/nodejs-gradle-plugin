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
import java.nio.file.Path
import javax.inject.Inject

abstract class AbstractExecTask: DefaultTask() {
    @get:Internal
    abstract val execResult: Property<ExecResult>

    @get:Inject
    abstract val objects: ObjectFactory

    @get:Inject
    abstract val execActionFactory: ExecActionFactory

    @get:Internal
    protected abstract val executable: Path

    private val args: MutableList<String> = mutableListOf()

    protected fun args(vararg arg: String) {
        this.args.addAll(arg)
    }

    protected open fun prepareArgs() {
    }

    @TaskAction
    open fun exec() {
        prepareArgs()

        val execSpec: DefaultExecSpec = objects.newInstance()
        execSpec.executable = executable.toString()
        execSpec.args = args

        val action = execActionFactory.newExecAction()
        execSpec.copyTo(action)
        execResult.set(action.execute())
    }
}
