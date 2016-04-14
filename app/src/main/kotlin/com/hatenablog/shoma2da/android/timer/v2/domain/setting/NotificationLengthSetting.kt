package com.hatenablog.shoma2da.android.timer.v2.domain.setting

import android.content.Context
import android.preference.PreferenceManager

class NotificationLengthSetting(val context:Context) {

    companion object {
        fun load(context: Context): NotificationLengthSetting {
            return NotificationLengthSetting(context)
        }
    }

    fun action(callback:(isLimitLess:Boolean, length:Int) -> Unit) {
        val pref = PreferenceManager.getDefaultSharedPreferences(context) ?: return

        val length = Integer.parseInt(pref.getString("notification_length", "-1"))
        val isLimitLess = (length == -1)
        callback.invoke(isLimitLess, length)
    }
}
