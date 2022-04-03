package com.nisecoder.gradle.plugin

import org.intellij.lang.annotations.Language
import java.io.File

fun File.writeKotlin(@Language("gradle.kts") src: String) {
    writeText(src)
}
