package com.hatenablog.shoma2da.android.timer

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.graphics.BitmapFactory
import android.app.Notification
import android.app.PendingIntent

/**
 * Created by shoma2da on 2014/07/24.
 */

class NotificationLauncherService : Service() {
    override fun onBind(p0: Intent): IBinder? = null

    override fun onStartCommand(intent:Intent , flags:Int , startId:Int):Int {
        val intent = Intent(this, javaClass<MainActivity>())
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val notificationBuilder = Notification.Builder(this).
                setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher)).
                setSmallIcon(R.drawable.ic_launcher).
                setTicker("ランチャー").
                setContentTitle(getResources()?.getString(R.string.app_name)).
                setContentText("らんちゃー").
                setWhen(System.currentTimeMillis()).
                setContentIntent(pendingIntent)
        startForeground(10, notificationBuilder?.build())

        return Service.START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
    }

}
