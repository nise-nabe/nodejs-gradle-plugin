package com.nisecoder.gradle.plugin.nodejs.binary

import com.nisecoder.gradle.plugin.nodejs.binary.NodeBinaryArchiveType.TarGz
import com.nisecoder.gradle.plugin.nodejs.binary.NodeBinaryArchiveType.Zip

/**
 * see [nodejs releases](https://nodejs.org/dist/latest/)
 */
@Suppress("unused")
enum class NodeBinaryType(val osName: String, val arch: String, val ext: NodeBinaryArchiveType) {
    Win32("win", "x86", Zip),
    Win64("win", "x64", Zip),
    Mac64("darwin", "x64", TarGz),
    MacArm64("darwin", "arm64", TarGz),
    Linux64("linux", "x64", TarGz),
    LinuxArmv7("linux", "armv7l", TarGz),
    LinuxArmv8("linux", "armv64", TarGz),
}
