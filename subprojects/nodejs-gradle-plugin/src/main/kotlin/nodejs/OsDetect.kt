@file:Suppress("unused")
package com.nisecoder.gradle.plugin.nodejs

object OsDetect {
    @Suppress("MemberVisibilityCanBePrivate")
    val os: String
        get() = System.getProperty("os.name").toLowerCase()

    fun isWindows(): Boolean {
        return os.indexOf("win") >= 0
    }

    fun isMac(): Boolean {
        return os.indexOf("mac") >= 0
    }

    fun isUnix(): Boolean {
        return os.let { it.indexOf("nix") >= 0 || it.indexOf("nux") >= 0 }
    }

    fun isAix(): Boolean {
        return os.indexOf("aix") > 0
    }
}
