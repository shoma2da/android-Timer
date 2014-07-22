package com.hatenablog.shoma2da.android.timer

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.hatenablog.shoma2da.android.timer.model.RemainTime
import com.hatenablog.shoma2da.android.timer.viewmodel.CountdownNotification
import java.io.Serializable
import com.hatenablog.shoma2da.android.timer.model.RemainTimeCounter

/**
 * Created by shoma2da on 2014/07/15.
 */
class CountdownService : Service() {

    class object {
        val PARAM_NAME_ACTION = "action_param"
        val PARAM_NAME_TIME = "time_param"
        val PARAM_NAME_STATUS = "status_param"

        val ACTION_UPDATE_REMAINTIME = "update_remaintime"
        val ACTION_FINISH_COUNTDOWN = "finish_countdown"
        val ACTION_BROADCAST_STATUS = "broadcast_status"
    }

    private var mNotification = CountdownNotification(this)
    private var mCurrentStatus = Status.UNKNOWN
    private var mCurrentTime = RemainTime(0, 0)

    enum class Action {
        START; STOP; CONFIRM_STATUS;
    }
    enum class Status {
        START; STOP; UNKNOWN;
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent:Intent, flags:Int, startId:Int):Int {
        if (intent == null) {
            return@onStartCommand super.onStartCommand(intent, flags, startId)
        }

        val action = intent.getSerializableExtra(PARAM_NAME_ACTION) as Action?
        if (action == null || action is Action == false) {
            throw RuntimeException("CowntdownServiceには{ACTION_PARAM_NAME:Cowntdown.Action}を渡す必要があります")
        }

        when (action) {
            Action.START -> {
                mCurrentStatus = Status.START
                val time = intent.getSerializableExtra(PARAM_NAME_TIME) as RemainTime
                startCowntdown(time)
            }
            Action.STOP -> {
                mCurrentStatus = Status.STOP
                mNotification.cancel()
                stopSelf()
            }
            Action.CONFIRM_STATUS -> {
                //情報をアプリ内全体に送信する
                val intent = Intent(ACTION_BROADCAST_STATUS)
                intent.putExtra(PARAM_NAME_STATUS, mCurrentStatus as Serializable)
                intent.putExtra(PARAM_NAME_TIME, mCurrentTime as Serializable)
                sendBroadcast(intent)
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun startCowntdown(time:RemainTime) {
        //通知表示（foreground）
        mNotification.notify(time)

        //カウントダウンを開始
        RemainTimeCounter(time).countdown(
                onTimeChanged = { time ->
                    //情報をアプリ内全体に送信する
                    val intent = Intent(ACTION_UPDATE_REMAINTIME)
                    intent.putExtra(PARAM_NAME_TIME, time)
                    sendBroadcast(intent)

                    //通知を書き換える
                    mNotification.update(time)
                },
                onFinish = {
                    //Activityを起動する
                    val intent = Intent(this, javaClass<CountdownActivity>())
                    intent.setAction(ACTION_FINISH_COUNTDOWN);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)

                    mNotification.cancel()
                    stopSelf()
                },
                continueCondition = { mCurrentStatus != Status.STOP },
                onCancel = {
                    //カウントダウンしているサービスを停止する
                    mNotification.cancel()
                    stopSelf()
                })
    }

}
