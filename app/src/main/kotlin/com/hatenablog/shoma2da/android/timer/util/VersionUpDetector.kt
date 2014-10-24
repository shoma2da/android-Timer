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

    val FILE_NAME = "version_up_data"
    val PREVIOUS_VERSION_KEY = "previous_key"
    val CURRENT_VERSION_KEY = "current_key"
    val mPreference = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    fun detect(currentVersion:String, onDetect:() -> Unit = {}, onNotDetect:() -> Unit = {}, onInitial:() -> Unit = {}) {
        val previosValue = mPreference.getString(PREVIOUS_VERSION_KEY, null)

        //まだ何もない
        val savedCurrentVersion = mPreference.getString(CURRENT_VERSION_KEY, null)
        if (savedCurrentVersion == null) {
            onInitial()
            return
        }

        //バージョンが一緒なら更新ではない
        if (currentVersion.equals(previosValue)) {
            onNotDetect()
            return
        }

        onDetect()
    }

    fun finishUpdate() {
        //現在のバージョンを過去のものとして保存→更新判定はこれ移行されない
        mPreference.edit().putString(PREVIOUS_VERSION_KEY, mPreference.getString(CURRENT_VERSION_KEY, "")).apply()
    }

    fun noteUpdate(currentVersion:String = VERSION) {
        mPreference.edit().putString(CURRENT_VERSION_KEY, currentVersion).apply()
    }

}
