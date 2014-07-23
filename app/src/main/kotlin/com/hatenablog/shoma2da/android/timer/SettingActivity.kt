package com.hatenablog.shoma2da.android.timer

import android.app.Activity
import android.os.Bundle

/**
 * Created by shoma2da on 2014/07/24.
 */
class SettingActivity : Activity() {

    override fun onCreate(savedInstance : Bundle?) {
        super.onCreate(savedInstance)
        getActionBar()?.setDisplayHomeAsUpEnabled(true)
    }

}
