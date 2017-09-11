package com.hatenablog.shoma2da.android.timer.v2

import android.app.Application
import com.hatenablog.shoma2da.android.timer.v2.log.Logger

class TimerApplication : Application() {

    var mLogger:Logger? = null

    override fun onCreate() {
        super.onCreate()

        AppInitializer.initialize(this);
    }

    fun getLogger():Logger {
        if (mLogger == null) {
            mLogger = Injection.logger(this)
        }
        val logger = mLogger
        if (logger != null) {
            return logger
        }
        throw RuntimeException("logger is null")
    }

}