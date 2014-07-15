package com.hatenablog.shoma2da.android.bakusokutimer

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

/**
 * Created by shoma2da on 2014/07/15.
 */

class CowntdownService : Service() {

    class object {
        val TIME_PARAM_NAME = "time_param"
        val STOP_PARAM_NAME = "stop_param"
    }

    private var count = 0

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent:Intent, flags:Int, startId:Int):Int {
        val stopFlag = intent.getBooleanExtra(STOP_PARAM_NAME, false)
        Log.d("shomatsu", "start service" + count++)
        return super.onStartCommand(intent, flags, startId)
    }

}
