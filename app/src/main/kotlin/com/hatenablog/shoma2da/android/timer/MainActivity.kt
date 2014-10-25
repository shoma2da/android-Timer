package com.hatenablog.shoma2da.android.timer

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.content.Intent
import com.google.android.gms.analytics.HitBuilders
import com.crashlytics.android.Crashlytics
import com.hatenablog.shoma2da.android.timer.viewmodel.please_review.PleaseReviewDialog
import com.hatenablog.shoma2da.android.timer.viewmodel.please_review.PleaseReviewCondition
import android.preference.PreferenceManager
import com.hatenablog.shoma2da.android.timer.util.VersionUpDetector
import android.app.AlertDialog
import com.parse.ParseAnalytics

/**
 * Created by shoma2da on 2014/06/28.
 */

class MainActivity : Activity() {

    override fun onCreate(savedInstance : Bundle?) {
        super.onCreate(savedInstance)
        setContentView(R.layout.activity_main)

        //Crashlytics & Parse Analytics
        if (BuildConfig.DEBUG == false) {
            Crashlytics.start(this);
            ParseAnalytics.trackAppOpened(getIntent());
        }

        //Analytics
        val tracker = (getApplication() as TimerApplication?)?.getTracker()
        tracker?.setScreenName("MainActivity")
        tracker?.send(HitBuilders.ScreenViewBuilder().build());

        //ランチャー起動
        startService(Intent(this, javaClass<NotificationLauncherService>()))

        //レビューお願いを表示
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val condition = PleaseReviewCondition.create(preferences)
        if (condition != null && condition.isTiming()) {
            PleaseReviewDialog().show(getFragmentManager(), "please_review")
        }

        //バージョンアップダイアログを表示
        val detector = VersionUpDetector(this)
        detector.detect(VersionUpDetector.VERSION,
            onDetect = {
                //ダイアログの表示
                AlertDialog.Builder(this)
                        .setTitle(getString(R.string.versionup_dialog_title))
                        .setMessage(getString(R.string.versionup_dialog_message))
                        .setCancelable(false)
                        .setNegativeButton(R.string.ok, { dialog, whichButton ->
                            dialog.dismiss()
                        }).create().show()

                //バージョンアップ判定はこれ以降行わない
                detector.finishUpdate()
            }
        )
    }

    override fun onCreateOptionsMenu(menu:Menu?):Boolean {
        val inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item:MenuItem?):Boolean {
        if (R.id.setting_menu == item?.getItemId()) {
            startActivity(Intent(this, javaClass<SettingActivity>()))
            return true;
        }
        return super.onOptionsItemSelected(item)
    }

}