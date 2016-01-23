package com.hatenablog.shoma2da.android.timer.v1_2.util.extensions

import android.content.Context
import android.media.AudioManager
import com.hatenablog.shoma2da.android.timer.v1_2.TimerApplication
import com.hatenablog.shoma2da.android.timer.v1_2.log.Logger

fun Context.getLogger(): Logger {
    return (applicationContext as TimerApplication).getLogger()
}

fun Context.getAudioManager():AudioManager {
    return getSystemService(Context.AUDIO_SERVICE) as AudioManager;
}