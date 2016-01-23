package com.hatenablog.shoma2da.android.timer.v1_2.entrypoint.presentation.activity

import android.os.Bundle
import android.preference.PreferenceActivity
import com.google.android.gms.ads.AdView
import com.google.android.gms.analytics.HitBuilders
import com.hatenablog.shoma2da.android.timer.R
import com.hatenablog.shoma2da.android.timer.v1_2.TimerApplication
import com.hatenablog.shoma2da.android.timer.v1_2.util.extensions.getLogger
import com.hatenablog.shoma2da.android.timer.v1_2.util.extensions.load

class SettingActivity : PreferenceActivity() {

    override fun onCreate(savedInstance : Bundle?) {
        super.onCreate(savedInstance)
        setContentView(R.layout.activity_setting)
        setTitle(R.string.setting)

        //Analytics
        val logger = application.getLogger()
        logger.sendScreenLog("SettingActivity")

        //広告設定
        (findViewById(R.id.adView) as AdView).load()

        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

}
