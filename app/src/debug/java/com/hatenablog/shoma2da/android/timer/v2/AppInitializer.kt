package com.hatenablog.shoma2da.android.timer.v2

import timber.log.Timber

class AppInitializer {
    companion object {
        fun initialize(app:TimerApplication) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
