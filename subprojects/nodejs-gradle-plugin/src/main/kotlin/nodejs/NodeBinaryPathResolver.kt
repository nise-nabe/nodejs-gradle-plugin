package com.nisecoder.gradle.plugin.nodejs

import java.nio.file.Path

class NodeBinaryPathResolver(
    val installPath: Path,
    private val nodeBinaryType: NodeBinaryType,
) {
    fun resolveNode(): Path {
        return if (nodeBinaryType.osName == "win") {
            installPath.resolve("node.exe")
        } else {
            installPath.resolve("bin").resolve("node")
        }
    }

    fun resolveNpm(): Path {
        return if (nodeBinaryType.osName == "win") {
            installPath.resolve("npm.cmd")
        } else {
            installPath.resolve("bin").resolve("npm")
        }
    }

    fun resolveNpx(): Path {
        return if (nodeBinaryType.osName == "win") {
            installPath.resolve("npx.cmd")
        } else {
            installPath.resolve("bin").resolve("npx")
        }
    }
}
