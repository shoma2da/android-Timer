package com.hatenablog.shoma2da.android.timer.v2

import android.app.Application
import co.meyasuba.android.sdk.Meyasubaco
import com.hatenablog.shoma2da.android.timer.v2.log.Logger
import com.parse.Parse
import com.parse.ParseInstallation

class TimerApplication : Application() {

    var mLogger:Logger? = null

    override fun onCreate() {
        super.onCreate()

        Parse.initialize(this, "JD6OCOsnYojTL0yQLqoYYBINmf7s9ugK6uHZgfBa", "GZvBpMmBaMG7ejDatcTUcBCsq9kxDOir4LYZAIIX")
        ParseInstallation.getCurrentInstallation().saveInBackground();

        Meyasubaco.setApiKey(this, "0da2add8c039ab3002194040efd225ecf281e4a2436313cbe5a4a14ca65b394a");

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