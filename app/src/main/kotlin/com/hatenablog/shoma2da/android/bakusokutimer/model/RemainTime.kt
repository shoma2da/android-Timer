package com.hatenablog.shoma2da.android.bakusokutimer.model

import java.io.Serializable

/**
 * Created by shoma2da on 2014/06/30.
 */

class RemainTime(val minutes:Int, val seconds:Int) : Serializable {

    class object {
        private val serialVersionUID = 0L

        public fun createFromString(string:String) : RemainTime {
            val numbers = string.split(':').map{ it.toInt() }
            return RemainTime(numbers[0], numbers[1])
        }
    }

    override fun toString() : String = "${java.lang.String.format("%02d", minutes)}:${java.lang.String.format("%02d", seconds)}"

}