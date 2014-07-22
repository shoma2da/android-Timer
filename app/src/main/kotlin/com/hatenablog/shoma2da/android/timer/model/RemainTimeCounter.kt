package com.hatenablog.shoma2da.android.timer.model

import kotlin.concurrent.timer
import android.util.Log
import java.util.Timer

/**
 * Created by shoma2da on 2014/07/22.
 */
class RemainTimeCounter(val time:RemainTime) {

    private var mCurrentTime = time

    fun countdown(
            onTimeChanged:(RemainTime) -> Unit,
            onFinish:() -> Unit,
            continueCondition:() -> Boolean,
            onCancel:(RemainTime) -> Unit) {
        timer(initialDelay = 1000, period = 1000, action = {
            if (mCurrentTime.isEmpty()) { //カウントダウン終了
                onFinish()
                cancel()
            } else {
                mCurrentTime = mCurrentTime.minus()
                if (continueCondition()) { //カウントダウン
                    onTimeChanged(mCurrentTime)
                } else { //カウントダウンを停止
                    onCancel(mCurrentTime)
                    cancel()
                }
            }
        })
    }

}
