package com.hatenablog.shoma2da.android.timer

import android.os.Bundle
import android.preference.PreferenceActivity
import android.content.Intent

/**
 * Created by shoma2da on 2014/07/24.
 */
class SettingActivity : PreferenceActivity() {

    override fun onCreate(savedInstance : Bundle?) {
        super.onCreate(savedInstance)
        setContentView(R.layout.activity_setting)

        getActionBar()?.setDisplayHomeAsUpEnabled(true)
    }

}
