package com.bsnl.faster

abstract class MainTask : Task() {
    override fun runOnMainThread(): Boolean {
        return true
    }
}