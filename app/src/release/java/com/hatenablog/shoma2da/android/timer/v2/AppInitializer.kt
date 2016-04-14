package com.hatenablog.shoma2da.android.timer.v2

import com.crashlytics.android.Crashlytics
import com.hatenablog.shoma2da.android.timer.v2.TimerApplication
import io.fabric.sdk.android.Fabric

class AppInitializer {
    companion object {
        fun initialize(app: TimerApplication) {
            Fabric.with(app, Crashlytics());
//            ParseAnalytics.trackAppOpenedInBackground(getIntent());
        }
    }
}