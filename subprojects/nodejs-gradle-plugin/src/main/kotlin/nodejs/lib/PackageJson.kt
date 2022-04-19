package com.nisecoder.gradle.plugin.nodejs.lib

import kotlinx.serialization.Serializable

@Serializable
data class PackageJson(
    val scripts: Map<String, String> = mapOf()
)