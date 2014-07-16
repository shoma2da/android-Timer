package com.hatenablog.shoma2da.android.bakusokutimer

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.app.Notification
import android.app.PendingIntent
import android.graphics.BitmapFactory
import com.hatenablog.shoma2da.android.bakusokutimer.model.RemainTime

/**
 * Created by shoma2da on 2014/07/15.
 */

class CowntdownService : Service() {

    class object {
        val ACTION_PARAM_NAME = "action_param"
        val TIME_PARAM_NAME = "time_param"
    }

    enum class Action {
        START; STOP; PAUSE;
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent:Intent, flags:Int, startId:Int):Int {
        val action = intent.getSerializableExtra(ACTION_PARAM_NAME) as Action?
        if (action == null || action is Action == false) {
            throw RuntimeException("CowntdownServiceには{ACTION_PARAM_NAME:Cowntdown.Action}を渡す必要があります")
        }

        when (action) {
            Action.START -> {
                val time = intent.getSerializableExtra(TIME_PARAM_NAME) as RemainTime
                startCowntdown(time)
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun startCowntdown(time:RemainTime) {
        //通知表示（foreground）
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

        //カウントダウン
        fun countdownToZero(time:RemainTime) {
            when (time.isEmpty()) {
                true -> {
                    stopForeground(true)
                    stopSelf()
                }
                false -> {
                    time.countdown { countdownToZero(it) }
                }
            }
        }
        time.countdown{ countdownToZero(it) }
    }

}
