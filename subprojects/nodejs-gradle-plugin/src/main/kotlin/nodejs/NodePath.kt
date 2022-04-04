package com.nisecoder.gradle.plugin.nodejs

import com.nisecoder.gradle.plugin.nodejs.binary.NodeBinaryType
import java.nio.file.Path

data class NodePath(
    val nodeBinaryType: NodeBinaryType,
    val installDir: Path,
    val binDir: Path,
    val node: Path,
    val npm: Path,
    val npx: Path,
    val corepack: Path,
) {
    fun resolve(name: String): Path {
        return if (nodeBinaryType.osName == "win") {
            binDir.resolve("$name.cmd")
        } else {
            binDir.resolve(name)
        }
    }
}
