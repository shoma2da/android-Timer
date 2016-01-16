package com.hatenablog.shoma2da.android.timer.viewmodel.please_review

import android.content.SharedPreferences

/**
 * Created by shoma2da on 2014/07/29.
 */
class PleaseReviewCondition(val preferences:SharedPreferences) {

    companion object {
        val COUNT_KEY = "count"
        val NEVER_SHOW_KEY = "never_show"

        fun create(preferences:SharedPreferences?):PleaseReviewCondition? {
            if (preferences == null) {
                return null
            }
            return PleaseReviewCondition(preferences)
        }
    }

    fun isTiming():Boolean {
        val neverShow = preferences.getBoolean(NEVER_SHOW_KEY, false)
        return when(neverShow) {
            true -> false
            false -> 3 <= preferences.getInt(COUNT_KEY, 0)
            else -> false
        }
    }

    fun setNeverShow() = preferences.edit().putBoolean(NEVER_SHOW_KEY, true).apply()

    fun addCount() {
        val neverShow = preferences.getBoolean(NEVER_SHOW_KEY, false)
        if (neverShow) {
            return@addCount
        }

        val currentCount = preferences.getInt(COUNT_KEY, 0)
        preferences.edit().putInt(COUNT_KEY, currentCount + 1).apply()
    }

    fun resetCount() {
        preferences.edit().remove(COUNT_KEY).apply()
    }

}
