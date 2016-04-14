package com.hatenablog.shoma2da.android.timer.v2.util.extensions

import android.app.AlertDialog
import android.content.Context
import android.media.AudioManager
import com.hatenablog.shoma2da.android.timer.v2.TimerApplication
import com.hatenablog.shoma2da.android.timer.v2.log.Logger

fun Context.getLogger(): Logger {
    return (applicationContext as TimerApplication).getLogger()
}

fun Context.getAudioManager():AudioManager {
    return getSystemService(Context.AUDIO_SERVICE) as AudioManager;
}

fun Context.isSilentMode():Boolean {
    val audioManager = getAudioManager()
    val volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
    return volume == 0
}

fun Context.showSimpleAlertDialog(message:String, buttonMessage:String) {
    AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton(buttonMessage, null)
            .create().show()
}
