package com.hatenablog.shoma2da.android.timer.util

import android.content.Context

/**
 * Created by shoma2da on 2014/10/23.
 */
class VersionUpDetector(context:Context) {

    class object {
        //TODO ここを変更していって運用すること！
        val VERSION = "sound_and_vibration"
    }

    val FILE_NAME = "previous_version"
    val PREVIOUS_VERSION_KEY = "key"
    val mPreference = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    fun detect(currentVersion:String, onDetect:() -> Unit = {}, onNotDetect:() -> Unit = {}) {
        val previosValue = mPreference.getString(PREVIOUS_VERSION_KEY, null)

        //初回起動時はバージョンアップではない
        if (previosValue == null) {
            onNotDetect()
            mPreference.edit().putString(PREVIOUS_VERSION_KEY, "first_lauch").apply()
            return
        }

        //前回のバージョンと同じならバージョンアップではない
        if (previosValue.equals(currentVersion)) {
            onNotDetect()
            return
        }

        onDetect()
    }

    fun save(currentVersion:String) {
        mPreference.edit().putString(PREVIOUS_VERSION_KEY, currentVersion).apply()
    }

}
