package com.hatenablog.shoma2da.android.bakusokutimer.model

import java.io.Serializable
import android.content.Context
import android.app.AlarmManager
import android.content.Intent
import com.hatenablog.shoma2da.android.bakusokutimer.MainActivity
import android.app.PendingIntent

/**
 * Created by shoma2da on 2014/06/30.
 */

class Time(val minutes:Int, val seconds:Int) : Serializable {

    class object {
        private val serialVersionUID = 0L

        public fun createFromString(string:String) : Time {
            val numbers = string.split(':').map{ it.toInt() }
            return Time(numbers[0], numbers[1])
        }
    }

    fun setAlarm(context:Context) {
        val intent = Intent(context, javaClass<MainActivity>())

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 3000, PendingIntent.getActivity(context, 0, intent, 0))
    }

    override fun toString() : String = "${java.lang.String.format("%02d", minutes)}:${java.lang.String.format("%02d", seconds)}"

}