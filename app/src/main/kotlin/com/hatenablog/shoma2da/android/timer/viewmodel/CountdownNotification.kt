package com.hatenablog.shoma2da.android.timer.viewmodel

import android.app.Notification
import android.graphics.BitmapFactory
import com.hatenablog.shoma2da.android.timer.R
import com.hatenablog.shoma2da.android.timer.model.RemainTime
import com.hatenablog.shoma2da.android.timer.CountdownActivity
import android.content.Intent
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.app.NotificationManager

/**
 * Created by shoma2da on 2014/07/19.
 */

class CountdownNotification(val service:Service) {

    class object {
        private val NOTIFICATION_ID = 1
    }

    private var mNotificationBuilder:Notification.Builder? = null

    fun notify(time: RemainTime) {
        val intent = Intent(service, javaClass<CountdownActivity>())
        intent.putExtra(CountdownActivity.START_COUNTDOWN_PARAM_NAME, false)
        intent.putExtra(CountdownActivity.TIME_PARAM_NAME, time)
        val pendingIntent = PendingIntent.getActivity(service, 0, intent, 0)
        mNotificationBuilder = Notification.Builder(service).
                setLargeIcon(BitmapFactory.decodeResource(service.getResources(), R.drawable.ic_launcher)).
                setSmallIcon(R.drawable.ic_launcher).
                setTicker(service.getString(R.string.notification_set_timer)).
                setContentTitle(service.getResources()?.getString(R.string.app_name)).
                setContentText(time.toString()).
                setWhen(System.currentTimeMillis()).
                setContentIntent(pendingIntent)
        service.startForeground(NOTIFICATION_ID, mNotificationBuilder?.build())
    }

    fun update(time: RemainTime) {
        mNotificationBuilder?.setContentText(time.toString())
        val manager = service.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, mNotificationBuilder!!.build())
    }

    fun cancel() {
        service.stopForeground(true)
    }

}
