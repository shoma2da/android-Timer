package com.hatenablog.shoma2da.android.timer.v1_2

import android.app.Application
import com.hatenablog.shoma2da.android.timer.v1_2.log.Logger

class TimerApplication : Application() {

    var mLogger:Logger? = null

    override fun onCreate() {
        super.onCreate()

//        Parse.initialize(this, "JD6OCOsnYojTL0yQLqoYYBINmf7s9ugK6uHZgfBa", "GZvBpMmBaMG7ejDatcTUcBCsq9kxDOir4LYZAIIX")
//        ParseInstallation.getCurrentInstallation().saveInBackground();

        AppInitializer.initialize();
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