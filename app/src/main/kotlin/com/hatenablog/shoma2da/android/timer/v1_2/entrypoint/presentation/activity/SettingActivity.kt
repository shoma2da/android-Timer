package com.hatenablog.shoma2da.android.timer.v1_2.entrypoint.presentation.activity

import android.os.Bundle
import android.preference.PreferenceActivity
import com.hatenablog.shoma2da.android.timer.R
import com.hatenablog.shoma2da.android.timer.v1_2.entrypoint.presentation.fragment.SettingFragment
import com.hatenablog.shoma2da.android.timer.v1_2.util.extensions.getLogger

class SettingActivity : PreferenceActivity() {

    override fun onCreate(savedInstance : Bundle?) {
        super.onCreate(savedInstance)

        fragmentManager.beginTransaction()
                .replace(android.R.id.content, SettingFragment()).commit();

        //Analytics
        val logger = application.getLogger()
        logger.sendScreenLog("SettingActivity")

        setTitle(R.string.setting)
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

}
