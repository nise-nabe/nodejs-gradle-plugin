package com.nisecoder.gradle.plugin.nodejs.task.pnpm

abstract class PnpmVersionTask: PnpmTask() {
    override fun prepareArgs() {
        args("-v")
    }
}
