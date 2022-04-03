package com.nisecoder.gradle.plugin.nodejs

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property


interface NodeExtension {
    val version: Property<String>
    val installationDir: DirectoryProperty
}
