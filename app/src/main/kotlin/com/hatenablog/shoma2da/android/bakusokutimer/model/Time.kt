package com.hatenablog.shoma2da.android.bakusokutimer.model

import java.io.Serializable
import android.content.Context

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
        context.getApplicationContext() //test
    }

    override fun toString() : String = "${java.lang.String.format("%02d", minutes)}:${java.lang.String.format("%02d", seconds)}"

}