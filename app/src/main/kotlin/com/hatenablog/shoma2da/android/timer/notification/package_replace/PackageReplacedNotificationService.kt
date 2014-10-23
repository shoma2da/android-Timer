package com.hatenablog.shoma2da.android.timer.notification.package_replace

import android.app.IntentService
import android.content.Intent
import android.util.Log

/**
 * Created by shoma2da on 2014/10/23.
 */
class PackageReplacedNotificationService: IntentService("PackageReplacedNotificationService") {

    override fun onHandleIntent(intent: Intent?) {
        Log.d("train", "on handle intent")
    }

}
