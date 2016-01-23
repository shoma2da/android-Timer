package com.hatenablog.shoma2da.android.timer.v1_2.entrypoint.presentation.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import com.crashlytics.android.Crashlytics
import com.google.android.gms.analytics.HitBuilders
import com.hatenablog.shoma2da.android.timer.BuildConfig
import com.hatenablog.shoma2da.android.timer.R
import com.hatenablog.shoma2da.android.timer.v1_2.TimerApplication
import com.hatenablog.shoma2da.android.timer.v1_2.domain.notificationlauncher.NotificationLauncherService
import com.hatenablog.shoma2da.android.timer.v1_2.domain.please_review.PleaseReviewCondition
import com.hatenablog.shoma2da.android.timer.v1_2.entrypoint.presentation.dialog.PleaseReviewDialog
import com.hatenablog.shoma2da.android.timer.v1_2.util.extensions.getLogger
import io.fabric.sdk.android.Fabric

class MainActivity : Activity() {

    override fun onCreate(savedInstance : Bundle?) {
        super.onCreate(savedInstance)
        setContentView(R.layout.activity_main)

        //Crashlytics & Parse Analytics
        if (BuildConfig.DEBUG == false) {
            Fabric.with(this, Crashlytics());
//            ParseAnalytics.trackAppOpenedInBackground(getIntent());
        }

        //Analytics
        val logger = getLogger()
        logger.sendScreenLog("MainActivity")

        //ランチャー起動
        startService(Intent(this, NotificationLauncherService::class.java))

        //レビューお願いを表示
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val condition = PleaseReviewCondition.create(preferences)
        if (condition != null && condition.isTiming()) {
            PleaseReviewDialog().show(fragmentManager, "please_review")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?):Boolean {
        val inflater = menuInflater;
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?):Boolean {
        if (R.id.setting_menu == item?.itemId) {
            startActivity(Intent(this, SettingActivity::class.java))
            return true;
        }
        return super.onOptionsItemSelected(item)
    }

}