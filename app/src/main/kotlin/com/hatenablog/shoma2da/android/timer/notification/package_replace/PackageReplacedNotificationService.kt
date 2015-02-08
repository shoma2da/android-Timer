package com.hatenablog.shoma2da.android.timer.notification.package_replace

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.hatenablog.shoma2da.android.timer.util.VersionUpDetector
import android.content.Context
import android.app.NotificationManager
import android.app.Notification
import com.hatenablog.shoma2da.android.timer.MainActivity
import android.app.PendingIntent
import android.graphics.BitmapFactory
import com.hatenablog.shoma2da.android.timer.R
import com.hatenablog.shoma2da.android.timer.notification.NotificationIds
import com.hatenablog.shoma2da.android.timer.viewmodel.please_review.PleaseReviewCondition
import android.preference.PreferenceManager

/**
 * Created by shoma2da on 2014/10/23.
 */
class PackageReplacedNotificationService: IntentService("PackageReplacedNotificationService") {

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
        val intent = Intent(this, javaClass<MainActivity>())
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
