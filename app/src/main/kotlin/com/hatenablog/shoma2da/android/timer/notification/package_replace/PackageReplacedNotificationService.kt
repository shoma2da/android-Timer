package com.hatenablog.shoma2da.android.timer.notification.package_replace

import android.app.IntentService
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.hatenablog.shoma2da.android.timer.MainActivity
import com.hatenablog.shoma2da.android.timer.R
import com.hatenablog.shoma2da.android.timer.notification.NotificationIds
import com.hatenablog.shoma2da.android.timer.util.VersionUpDetector

class PackageReplacedNotificationService: IntentService(PackageReplacedNotificationService::class.java.simpleName) {

    override fun onHandleIntent(intent: Intent?) {
        val detector = VersionUpDetector(this)
        detector.detect(VersionUpDetector.VERSION,
            onInitial = {
                detector.noteUpdate()
                notifyUpdateNotification()
            },
            onDetect = {
                notifyUpdateNotification()
            }
        )
    }

    fun notifyUpdateNotification() {
        //通知をくみたて
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val notification = Notification.Builder(this).
                setAutoCancel(true).
                setSmallIcon(R.drawable.ic_launcher).
                setContentIntent(pendingIntent).
                setContentTitle(getString(R.string.versionup_notification_title)).
                setContentText(getString(R.string.versionup_notification_message)).
                setTicker(getString(R.string.versionup_notification_ticker)).
                setWhen(System.currentTimeMillis()).
                build()

        //通知を表示
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NotificationIds.VERSIONUP_NOTIFICATION_ID, notification)
    }

}
