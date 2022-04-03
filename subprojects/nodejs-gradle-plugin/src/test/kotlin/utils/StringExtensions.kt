package com.nisecoder.gradle.plugin.utils

fun String.splitLine() = split(System.lineSeparator()).map { it.trim() }
