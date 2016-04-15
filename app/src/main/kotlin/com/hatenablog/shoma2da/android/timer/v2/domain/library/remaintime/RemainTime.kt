package com.hatenablog.shoma2da.android.timer.v2.domain.library.remaintime

import com.hatenablog.shoma2da.android.timer.v2.util.extensions.format
import java.io.Serializable

class RemainTime(val minutes:Int, val seconds:Int) : Serializable {

    companion object {
        private val serialVersionUID = 0L
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
        return when (hours) {
            0 ->                       "${minutes.format(2)}:${seconds.format(2)}"
            else -> "${hours.format(2)}:${minutes.format(2)}:${seconds.format(2)}"
        }


    }

}