package com.hatenablog.shoma2da.android.timer.v1_2.log

import com.google.android.gms.analytics.Tracker
import timber.log.Timber

open class Logger(tracker:Tracker? = null) {

    open fun sendScreenLog(screenName:String) {
        Timber.i("ScreenLog : %s", screenName)
    }

    open fun sendEvent(category:String,
                       action:String,
                       label:String? = null,
                       value:Long? = null) {
        Timber.i("Event : %s,%s, %s, %d", category, action, label, value)
    }

}