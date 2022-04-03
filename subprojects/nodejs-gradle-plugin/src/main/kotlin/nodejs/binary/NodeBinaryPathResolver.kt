package com.nisecoder.gradle.plugin.nodejs.binary

import java.nio.file.Path

/**
 * NOTE: gradle tarTree is not support symlink, so tools will use js file directly
 */
class NodeBinaryPathResolver(
    val installPath: Path,
    private val nodeBinaryType: NodeBinaryType,
) {
    fun resolveBinPath(): Path {
        return if (nodeBinaryType.osName == "win") {
            installPath
        } else {
            installPath.resolve("bin")
        }
    }

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
            installPath.resolve("lib/node_modules/npm/bin/npm-cli.js")
        }
    }

    fun resolveNpx(): Path {
        return if (nodeBinaryType.osName == "win") {
            installPath.resolve("npx.cmd")
        } else {
            installPath.resolve("lib/node_modules/npm/bin/npx-cli.js")
        }
    }

    fun resolveCorepack(): Path {
        return if (nodeBinaryType.osName == "win") {
            installPath.resolve("corepack.cmd")
        } else {
            installPath.resolve("lib/node_modules/corepack/dist/corepack.js")
        }
    }
}
