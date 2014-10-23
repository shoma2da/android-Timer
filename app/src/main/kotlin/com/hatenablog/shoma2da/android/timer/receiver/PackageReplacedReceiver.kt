package com.hatenablog.shoma2da.android.timer.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.hatenablog.shoma2da.android.timer.notification.package_replace.PackageReplacedNotificationService

/**
 * Created by shoma2da on 2014/10/23.
 */

class PackageReplacedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent:Intent) {
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)
               && ("package:" + context.getPackageName()).equals(intent.getDataString())) {
            val intent = Intent(context, javaClass<PackageReplacedNotificationService>())
            context.startService(intent)
        }
    }

}
