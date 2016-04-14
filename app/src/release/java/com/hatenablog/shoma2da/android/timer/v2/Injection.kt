package com.hatenablog.shoma2da.android.timer.v2

import android.content.Context
import com.google.android.gms.analytics.GoogleAnalytics
import com.google.android.gms.analytics.Tracker
import com.hatenablog.shoma2da.android.timer.v2.ReleaseLogger
import com.hatenablog.shoma2da.android.timer.v2.log.Logger

class Injection {

    companion object {

        fun logger(context: Context): Logger {
            val tracker: Tracker? = GoogleAnalytics.getInstance(context)?.newTracker("UA-32548600-14")
            if (tracker != null) {
                return ReleaseLogger(tracker)
            }
            throw RuntimeException()
        }

    }
}