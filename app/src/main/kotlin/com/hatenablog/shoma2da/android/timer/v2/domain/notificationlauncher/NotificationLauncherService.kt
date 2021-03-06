package com.hatenablog.shoma2da.android.timer.v2.domain.notificationlauncher

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.graphics.BitmapFactory
import android.app.PendingIntent
import android.preference.PreferenceManager
import android.support.v4.app.NotificationCompat
import com.hatenablog.shoma2da.android.timer.R

class NotificationLauncherService : Service() {
    override fun onBind(p0: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags:Int , startId:Int):Int {
        if (intent == null) {
            return START_STICKY
        }

        //現在の設定を読み取って、通知バーを使わない設定だったらすぐに終わる
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val notificationLaunerEnabled = preferences?.getBoolean(getString(R.string.notification_launcher_id), true) as Boolean
        if (preferences == null || notificationLaunerEnabled == false) {
            stopSelf(startId)
            return@onStartCommand super.onStartCommand(intent, flags, startId)
        }

        //通知バータップ時のIntentを準備
        val packageManager = packageManager
        val launchIntent = packageManager?.getLaunchIntentForPackage(packageName)
        launchIntent?.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
        if (launchIntent == null) {
            return@onStartCommand super.onStartCommand(intent, flags, startId)
        }

        //通知バーを設定
        val pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0)
        val notificationBuilder = NotificationCompat.Builder(this).
                setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_launcher))?.
                setSmallIcon(R.drawable.ic_launcher_notification)?.
                setTicker(null)?.
                setContentTitle(resources?.getString(R.string.notification_open_app))?.
                setContentText(resources?.getString(R.string.notification_open_app_setting))?.
                setWhen(System.currentTimeMillis())?.
                setContentIntent(pendingIntent)
        startForeground(10, notificationBuilder?.build())

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
    }

}
