package com.hatenablog.shoma2da.android.timer.v2

import android.app.Application
import com.hatenablog.shoma2da.android.timer.v2.log.Logger

class TimerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        AppInitializer.initialize(this);
    }

}