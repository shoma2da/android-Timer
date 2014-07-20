package com.hatenablog.shoma2da.android.bakusokutimer.model

import java.io.Serializable
import android.os.Handler
import kotlin.concurrent.schedule
import java.util.Timer

/**
 * Created by shoma2da on 2014/06/30.
 */

class RemainTime(val minutes:Int, val seconds:Int) : Serializable {

    class object {
        private val serialVersionUID = 0L
    }

    fun countdown(onTimeChanged:(RemainTime) -> Unit) {
        val handler = Handler()
        Timer().schedule(1000) {
            handler.post { onTimeChanged(minus()) }
        }
    }

    fun minus(): RemainTime {
        if (isEmpty()) {
            return RemainTime(0, 0)
        }
        return when (seconds) {
            0 -> RemainTime(minutes - 1, 59)
            else -> RemainTime(minutes, seconds - 1)
        }
    }

    fun isEmpty() = (minutes == 0 && seconds == 0)

    override fun toString() : String {
        val hours = minutes / 60
        val minutes = minutes % 60
        when (hours) {
            0 ->    return                                           "${java.lang.String.format("%02d", minutes)}:${java.lang.String.format("%02d", seconds)}"
            else -> return "${java.lang.String.format("%02d", hours)}:${java.lang.String.format("%02d", minutes)}:${java.lang.String.format("%02d", seconds)}"
        }


    }

}