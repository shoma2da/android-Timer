package com.hatenablog.shoma2da.android.timer.v1_2.entrypoint.presentation.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import co.meyasuba.android.sdk.Meyasubaco
import com.hatenablog.shoma2da.android.timer.R
import com.hatenablog.shoma2da.android.timer.v1_2.domain.notificationlauncher.NotificationLauncherService
import com.hatenablog.shoma2da.android.timer.v1_2.domain.please_review.ReviewRequest
import com.hatenablog.shoma2da.android.timer.v1_2.util.extensions.getLogger

class MainActivity : Activity() {

    override fun onCreate(savedInstance : Bundle?) {
        super.onCreate(savedInstance)
        setContentView(R.layout.activity_main)

        Meyasubaco.getInstance(this).initialize("66da0d8fb3108d066ee7069dc05db63f")

        //Analytics
        val logger = getLogger()
        logger.sendScreenLog("MainActivity")

        //ランチャー起動
        startService(Intent(this, NotificationLauncherService::class.java))

        //レビューお願いを表示
        val reviewRequest = ReviewRequest()
        reviewRequest.show(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?):Boolean {
        val inflater = menuInflater;
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?):Boolean {
        when (item?.itemId) {
            R.id.menu_settings -> {
                startActivity(Intent(this, SettingActivity::class.java))
                return true;
            }
        }
        return super.onOptionsItemSelected(item)
    }

}