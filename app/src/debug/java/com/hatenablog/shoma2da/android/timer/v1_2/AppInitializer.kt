package com.hatenablog.shoma2da.android.timer.v1_2

import timber.log.Timber

class AppInitializer {
    companion object {
        fun initialize() {
            Timber.plant(Timber.DebugTree())
        }
    }
}
