package com.hatenablog.shoma2da.android.bakusokutimer

import android.app.Activity
import android.os.Bundle
import com.hatenablog.shoma2da.android.bakusokutimer.model.RemainTime
import android.widget.TextView
import android.content.Intent
import java.io.Serializable
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.content.Context

/**
 * Created by shoma2da on 2014/06/30.
 */

public class CountdownActivity : Activity() {

    class object {
        val TIME_PARAM_NAME = "time_param"
    }

    private var mReceiver:BroadcastReceiver? = null

    override fun onCreate(savedInstance : Bundle?) {
        super.onCreate(savedInstance)
        setContentView(R.layout.activity_countdown)

        //初期表示設定
        val time = getIntent()?.getSerializableExtra(TIME_PARAM_NAME) as RemainTime
        val timeText = findViewById(R.id.timeText) as TextView
        timeText.setText(time.toString())

        //Receiverの設定
        mReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val time = intent.getSerializableExtra(CowntdownService.TIME_PARAM_NAME) as RemainTime
                timeText.setText(time.toString())
            }
        }
        val filter = IntentFilter()
        filter.addAction(CowntdownService.ACTION_UPDATE_REMAINTIME)
        registerReceiver(mReceiver, filter)

        //サービスを起動する
        val intent = Intent(this, javaClass<CowntdownService>())
        intent.putExtra(CowntdownService.TIME_PARAM_NAME, time)
        intent.putExtra(CowntdownService.ACTION_PARAM_NAME, CowntdownService.Action.START as Serializable)
        startService(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mReceiver != null) {
            unregisterReceiver(mReceiver as BroadcastReceiver)
            mReceiver = null
        }
    }

}
