package com.hatenablog.shoma2da.android.timer.setting

import android.content.Context
import android.preference.PreferenceManager

/**
 * Created by shoma2da on 2014/10/22.
 */

class NotificationMethodSetting(val context:Context) {

    companion object {
        fun load(context:Context): NotificationMethodSetting {
            return NotificationMethodSetting(context)
        }
    }

    fun action(onSound:() -> Unit = {}, onVibration:() -> Unit = {}, onBoth:() -> Unit = {}) {
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        if (pref == null) {
            return
        }

        val currentValue = pref.getString("notification_method", "vibration")
        if (currentValue == "vibration") {
            onVibration()
            return
        }
        if (currentValue == "sound") {
            onSound()
            return
        }
        if (currentValue == "both") {
            onBoth()
            return
        }
        onVibration() //デフォルトはバイブレーションということにする
    }

}
