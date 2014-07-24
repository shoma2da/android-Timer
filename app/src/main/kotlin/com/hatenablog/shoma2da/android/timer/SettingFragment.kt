package com.hatenablog.shoma2da.android.timer

import android.preference.PreferenceFragment
import android.os.Bundle
import android.util.Log

/**
 * Created by shoma2da on 2014/07/24.
 */
class SettingFragment : PreferenceFragment() {

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preference)
    }

}
