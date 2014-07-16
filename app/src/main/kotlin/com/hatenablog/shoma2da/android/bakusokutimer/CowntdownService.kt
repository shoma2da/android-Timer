package com.hatenablog.shoma2da.android.bakusokutimer

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.app.Notification
import android.app.PendingIntent
import android.graphics.BitmapFactory

/**
 * Created by shoma2da on 2014/07/15.
 */

class CowntdownService : Service() {

    class object {
        val TIME_PARAM_NAME = "time_param"
        val STOP_PARAM_NAME = "stop_param"
    }

    private var count = 0

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent:Intent, flags:Int, startId:Int):Int {
        //val stopFlag = intent.getBooleanExtra(STOP_PARAM_NAME, false)
        Log.d("shomatsu", "start service" + count++)

        val pendingIntent = PendingIntent.getActivity(this, 0, Intent(this, javaClass<CountdownActivity>()), 0)
        val notification = Notification.Builder(this).
                setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher)).
                setSmallIcon(R.drawable.ic_launcher).
                setTicker("タイマーをセットしました").
                setContentTitle("タイマーが終了するまで").
                setContentText("あと○○分です").
                setWhen(System.currentTimeMillis()).
                setContentIntent(pendingIntent).build()
        startForeground(1, notification)

        return super.onStartCommand(intent, flags, startId)
    }

}
