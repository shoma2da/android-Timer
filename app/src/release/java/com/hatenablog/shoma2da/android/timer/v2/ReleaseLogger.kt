package com.hatenablog.shoma2da.android.timer.v2

import com.google.android.gms.analytics.HitBuilders
import com.google.android.gms.analytics.Tracker
import com.hatenablog.shoma2da.android.timer.v2.log.Logger

class ReleaseLogger(val tracker: Tracker) : Logger(tracker) {

    override fun sendScreenLog(screenName:String) {
        tracker.setScreenName(screenName)
        tracker.send(HitBuilders.ScreenViewBuilder().build());
    }

    override fun sendEvent(category:String,
                           action:String,
                           label:String?,
                           value:Long?) {
        val builder = HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
        label?.let { builder.setLabel(it) }
        value?.let { builder.setValue(it) }

        tracker.send(builder.build())
    }

}
