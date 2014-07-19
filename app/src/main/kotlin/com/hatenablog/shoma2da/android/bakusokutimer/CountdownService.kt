package com.hatenablog.shoma2da.android.bakusokutimer

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.hatenablog.shoma2da.android.bakusokutimer.model.RemainTime
import com.hatenablog.shoma2da.android.bakusokutimer.viewmodel.CountdownNotification

/**
 * Created by shoma2da on 2014/07/15.
 */
class CountdownService : Service() {

    class object {
        val ACTION_PARAM_NAME = "action_param"
        val TIME_PARAM_NAME = "time_param"

        val ACTION_UPDATE_REMAINTIME = "update_remaintime"
        val ACTION_FINISH_COUNTDOWN = "finish_countdown"
    }

    private var mNotification = CountdownNotification(this)

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
        mNotification.notify(time)

        //カウントダウン
        fun countdownToZero(time:RemainTime) {
            when (time.isEmpty()) {
                true -> {
                    //Activityを起動する
                    val intent = Intent(this, javaClass<CountdownActivity>())
                    intent.setAction(ACTION_FINISH_COUNTDOWN);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)

                    mNotification.cancel()
                    stopSelf()
                }
                false -> {
                    //情報をアプリ内全体に送信する
                    val intent = Intent(ACTION_UPDATE_REMAINTIME)
                    intent.putExtra(TIME_PARAM_NAME, time)
                    sendBroadcast(intent)

                    //通知を書き換える
                    mNotification.update(time)

                    time.countdown { countdownToZero(it) }
                }
            }
        }
        time.countdown{ countdownToZero(it) }
    }

}
