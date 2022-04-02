package com.nisecoder.gradle.plugin

import com.nisecoder.gradle.plugin.nodejs.NodeExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

@Suppress("unused")
class NodeJsPlugin: Plugin<Project> {
    /**
     * Apply this plugin to the given target object.
     *
     * @param target The target object
     */
    override fun apply(target: Project): Unit = target.run {
        extensions.create<NodeExtension>("nodejs").also {
            it.version.convention("v16.14.2")
        }
    }
}
