package com.hatenablog.shoma2da.android.timer.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.hatenablog.shoma2da.android.timer.notification.package_replace.PackageReplacedNotificationService
import com.hatenablog.shoma2da.android.timer.NotificationLauncherService

/**
 * Created by shoma2da on 2014/10/23.
 */

class PackageReplacedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, i:Intent) {
        if (i.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)
               && ("package:" + context.getPackageName()).equals(i.getDataString())) {
            //更新通知の表示
            val intent = Intent(context, PackageReplacedNotificationService::class.java)
            context.startService(intent)

            //通知バーランチャーを再起動
            context.startService(Intent(context, NotificationLauncherService::class.java))
        }
    }

}
