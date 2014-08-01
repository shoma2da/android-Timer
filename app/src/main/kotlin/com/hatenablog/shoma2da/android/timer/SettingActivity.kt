package com.hatenablog.shoma2da.android.timer

import android.os.Bundle
import android.preference.PreferenceActivity
import com.google.android.gms.analytics.HitBuilders
import com.hatenablog.shoma2da.android.timer.admob.AdViewWrapper
import com.google.android.gms.ads.AdView

/**
 * Created by shoma2da on 2014/07/24.
 */
class SettingActivity : PreferenceActivity() {

    override fun onCreate(savedInstance : Bundle?) {
        super.onCreate(savedInstance)
        setContentView(R.layout.activity_setting)
        setTitle(getString(R.string.setting))

        //Analytics
        val tracker = (getApplication() as TimerApplication?)?.getTracker()
        tracker?.setScreenName("SettingActivity")
        tracker?.send(HitBuilders.ScreenViewBuilder().build());

        //広告設定
        AdViewWrapper(findViewById(R.id.adView) as AdView).loadAd()

        getActionBar()?.setDisplayHomeAsUpEnabled(true)
    }

}
