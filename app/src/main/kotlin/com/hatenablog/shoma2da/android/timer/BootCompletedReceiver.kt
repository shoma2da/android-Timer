package com.hatenablog.shoma2da.android.timer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * Created by shoma2da on 2014/07/24.
 */

class BootCompletedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent:Intent) {
        Log.d("shomatsu", "onReceive")
        context.startService(Intent(context, javaClass<NotificationLauncherService>()))
    }

}
