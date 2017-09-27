package com.hatenablog.shoma2da.android.timer.v2.log

import timber.log.Timber

open class Logger {

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