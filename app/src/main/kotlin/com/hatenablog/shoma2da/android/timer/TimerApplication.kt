package com.hatenablog.shoma2da.android.timer

import android.app.Application
import com.parse.Parse
import com.google.android.gms.analytics.Tracker
import com.google.android.gms.analytics.GoogleAnalytics

class TimerApplication : Application() {

    var mTracker:Tracker? = null

    override fun onCreate() {
        super.onCreate()

        Parse.initialize(this, "JD6OCOsnYojTL0yQLqoYYBINmf7s9ugK6uHZgfBa", "GZvBpMmBaMG7ejDatcTUcBCsq9kxDOir4LYZAIIX")
    }

    public fun getTracker() : Tracker? {
        if (mTracker == null) {
            mTracker= GoogleAnalytics.getInstance(this)?.newTracker("UA-32548600-14")
        }
        return mTracker
    }

}