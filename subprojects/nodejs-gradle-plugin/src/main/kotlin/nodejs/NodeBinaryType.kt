package com.nisecoder.gradle.plugin.nodejs

enum class NodeBinaryType(val osName: String, val arch: String, val ext: String) {
    Win32("win", "x86", "zip"),
    Win64("win", "x64", "zip"),
    Mac64("darwin", "x64", "tar.gz"),
    MacArm64("darwin", "arm64", "tar.gz"),
    Linux64("linux", "x64", "tar.gz"),
    LinuxArmv7("linux", "armv7l", "tar.gz"),
    LinuxArmv8("linux", "armv64", "tar.gz"),
}
