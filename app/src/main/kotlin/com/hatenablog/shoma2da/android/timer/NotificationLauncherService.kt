package com.hatenablog.shoma2da.android.timer

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.graphics.BitmapFactory
import android.app.Notification
import android.app.PendingIntent
import android.preference.PreferenceManager

/**
 * Created by shoma2da on 2014/07/24.
 */

class NotificationLauncherService : Service() {
    override fun onBind(p0: Intent): IBinder? = null

    override fun onStartCommand(intent:Intent , flags:Int , startId:Int):Int {
        //現在の設定を読み取って、通知バーを使わない設定だったらすぐに終わる
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val notificationLaunerEnabled = preferences?.getBoolean(getString(R.string.notification_launcher_id), false) as Boolean
        if (preferences == null || notificationLaunerEnabled == false) {
            stopSelf(startId)
            return@onStartCommand super.onStartCommand(intent, flags, startId)
        }

        //通知バータップ時のIntentを準備
        val packageManager = getPackageManager()
        val launchIntent = packageManager?.getLaunchIntentForPackage(getPackageName())
        launchIntent?.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
        if (launchIntent == null) {
            return@onStartCommand super.onStartCommand(intent, flags, startId)
        }

        //通知バーを設定
        val pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0)
        val notificationBuilder = Notification.Builder(this).
                setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher)).
                setSmallIcon(R.drawable.ic_launcher).
                setTicker(null).
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
