package com.hatenablog.shoma2da.android.timer

import android.app.Application
import com.parse.Parse

class TimerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Parse.initialize(this, "JD6OCOsnYojTL0yQLqoYYBINmf7s9ugK6uHZgfBa", "GZvBpMmBaMG7ejDatcTUcBCsq9kxDOir4LYZAIIX")
    }

}