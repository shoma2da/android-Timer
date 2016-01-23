package com.hatenablog.shoma2da.android.timer.v1_2.domain.library.remaintime

import android.graphics.BitmapFactory
import com.hatenablog.shoma2da.android.timer.R
import com.hatenablog.shoma2da.android.timer.v1_2.domain.library.remaintime.RemainTime
import com.hatenablog.shoma2da.android.timer.v1_2.entrypoint.presentation.activity.CountdownActivity
import android.content.Intent
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.support.v4.app.NotificationCompat
import android.app.NotificationManager

class CountdownNotification(val service: Service) {

    companion object {
        private val NOTIFICATION_ID = 1
    }

    private var mNotificationBuilder: NotificationCompat.Builder? = null

    fun notify(time: RemainTime) {
        val intent = Intent(service, CountdownActivity::class.java)
        intent.putExtra(CountdownActivity.START_COUNTDOWN_PARAM_NAME, false)
        intent.putExtra(CountdownActivity.TIME_PARAM_NAME, time)
        val pendingIntent = PendingIntent.getActivity(service, 0, intent, 0)
        mNotificationBuilder = NotificationCompat.Builder(service).
                setLargeIcon(BitmapFactory.decodeResource(service.resources, R.drawable.ic_launcher))?.
                setSmallIcon(R.drawable.ic_launcher_notification)?.
                setTicker(service.getString(R.string.notification_set_timer))?.
                setContentTitle(service.resources?.getString(R.string.app_name))?.
                setContentText(time.toString())?.
                setWhen(System.currentTimeMillis())?.
                setContentIntent(pendingIntent)
        service.startForeground(NOTIFICATION_ID, mNotificationBuilder?.build())
    }

    fun update(time: RemainTime) {
        mNotificationBuilder?.setContentText(time.toString())
        val manager = service.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = mNotificationBuilder?.build()
        if (notification != null) {
            manager.notify(NOTIFICATION_ID, notification)
        }
    }

    fun cancel() {
        service.stopForeground(true)
    }

}
