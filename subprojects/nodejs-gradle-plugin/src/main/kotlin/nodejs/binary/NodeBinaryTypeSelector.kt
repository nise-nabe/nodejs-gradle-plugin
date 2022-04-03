package com.nisecoder.gradle.plugin.nodejs.binary

@Suppress("unused", "MemberVisibilityCanBePrivate")
object NodeBinaryTypeSelector {
    fun select(): NodeBinaryType {
        return when {
            OsDetect.isWindows() -> {
                if (getOsArch().contains("64")) {
                    NodeBinaryType.Win64
                } else {
                    NodeBinaryType.Win32
                }
            }
            OsDetect.isMac() -> {
                if (getOsArch().contains("64")) {
                    NodeBinaryType.Mac64
                } else {
                    NodeBinaryType.Mac64
                }
            }
            OsDetect.isUnix() -> {
                // TODO support armv7, armv8
                return NodeBinaryType.Linux64
            }
            else -> throw IllegalStateException("Unsupported OS")
        }
    }

    fun getOsName(): String = System.getProperty("os.name")
    fun getOsArch(): String = System.getProperty("os.arch")
}
