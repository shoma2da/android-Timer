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
        val PARAM_NAME_ACTION = "action_param"
        val PARAM_NAME_TIME = "time_param"

        val ACTION_UPDATE_REMAINTIME = "update_remaintime"
        val ACTION_FINISH_COUNTDOWN = "finish_countdown"
    }

    private var mNotification = CountdownNotification(this)
    private var mIsStopped = false

    enum class Action {
        START; STOP;
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent:Intent, flags:Int, startId:Int):Int {
        val action = intent.getSerializableExtra(PARAM_NAME_ACTION) as Action?
        if (action == null || action is Action == false) {
            throw RuntimeException("CowntdownServiceには{ACTION_PARAM_NAME:Cowntdown.Action}を渡す必要があります")
        }

        when (action) {
            Action.START -> {
                val time = intent.getSerializableExtra(PARAM_NAME_TIME) as RemainTime
                startCowntdown(time)
            }
            Action.STOP -> {
                mIsStopped = true
                mNotification.cancel()
                stopSelf()
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun startCowntdown(time:RemainTime) {
        //通知表示（foreground）
        mNotification.notify(time)

        //カウントダウン
        fun countdownToZero(time:RemainTime) {
            if (mIsStopped) { return @countdownToZero }

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
                    intent.putExtra(PARAM_NAME_TIME, time)
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
