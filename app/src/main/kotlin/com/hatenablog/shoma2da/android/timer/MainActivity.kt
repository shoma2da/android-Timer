package com.hatenablog.shoma2da.android.timer

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.content.Intent
import com.google.android.gms.analytics.HitBuilders

/**
 * Created by shoma2da on 2014/06/28.
 */

class MainActivity : Activity() {

    override fun onCreate(savedInstance : Bundle?) {
        super.onCreate(savedInstance)
        setContentView(R.layout.activity_main)

        //Analytics
        val tracker = (getApplication() as TimerApplication?)?.getTracker()
        tracker?.setScreenName("MainActivity")
        tracker?.send(HitBuilders.ScreenViewBuilder().build());

        //ランチャー起動
        startService(Intent(this, javaClass<NotificationLauncherService>()))
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