package com.nisecoder.gradle.plugin.nodejs

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.newInstance


abstract class NodeExtension(objects: ObjectFactory) {
    val nodeVersion: NodeVersion = objects.newInstance()
    abstract val installationDir: DirectoryProperty
    abstract val scriptsPrefix: Property<String>

    fun nodeVersion(nodeVersion: String) {
        this.nodeVersion.fixed.set(nodeVersion)
    }
}

interface NodeVersion {
    /** should start `v` */
    val fixed: Property<String>
}
