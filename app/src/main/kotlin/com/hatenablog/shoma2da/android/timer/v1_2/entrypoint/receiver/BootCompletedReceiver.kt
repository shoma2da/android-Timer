package com.hatenablog.shoma2da.android.timer.v1_2.entrypoint.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.hatenablog.shoma2da.android.timer.v1_2.domain.notificationlauncher.NotificationLauncherService

class BootCompletedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        context.startService(Intent(context, NotificationLauncherService::class.java))
    }

}
