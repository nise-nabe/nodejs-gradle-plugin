package com.nisecoder.gradle.plugin.nodejs.binary

enum class NodeBinaryArchiveType(val value: String) {
    Zip("zip"),
    TarGz("tar.gz"),
    @Suppress("unused")
    @Deprecated("gradle archive operations is not support xz")
    TarXz("tar.xz"),
}
