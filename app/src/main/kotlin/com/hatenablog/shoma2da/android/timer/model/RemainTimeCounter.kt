package com.hatenablog.shoma2da.android.timer.model

import kotlin.concurrent.timer

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
