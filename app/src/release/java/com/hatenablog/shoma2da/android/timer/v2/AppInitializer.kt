package com.hatenablog.shoma2da.android.timer.v1_2

import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric

class AppInitializer {
    companion object {
        fun initialize(app:TimerApplication) {
            Fabric.with(app, Crashlytics());
//            ParseAnalytics.trackAppOpenedInBackground(getIntent());
        }
    }
}