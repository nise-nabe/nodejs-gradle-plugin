package com.nisecoder.gradle.plugin.nodejs

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property


interface NodeExtension {
    val nodeVersion: Property<String>
    val installationDir: DirectoryProperty
}
