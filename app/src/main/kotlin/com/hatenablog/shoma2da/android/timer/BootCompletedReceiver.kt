package com.hatenablog.shoma2da.android.timer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootCompletedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent:Intent) {
        context.startService(Intent(context, NotificationLauncherService::class.java))
    }

}
