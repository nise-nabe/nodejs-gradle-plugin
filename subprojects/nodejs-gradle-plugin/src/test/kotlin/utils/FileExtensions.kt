package com.nisecoder.gradle.plugin.utils

import org.intellij.lang.annotations.Language
import java.io.File

fun File.writeKotlin(@Language("gradle.kts") src: String) {
    writeText(src)
}

fun File.writeJson(@Language("json") src: String) {
    writeText(src)
}
