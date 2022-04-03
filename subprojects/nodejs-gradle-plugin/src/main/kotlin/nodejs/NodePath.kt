package com.nisecoder.gradle.plugin.nodejs

import java.nio.file.Path

data class NodePath(
    val installDir: Path,
    val binDir: Path,
    val node: Path,
    val npm: Path,
    val npx: Path,
    val corepack: Path,
)
