package com.hatenablog.shoma2da.android.bakusokutimer

import android.app.Activity
import android.os.Bundle
import com.hatenablog.shoma2da.android.bakusokutimer.model.RemainTime
import android.widget.TextView
import android.content.Intent
import java.io.Serializable

/**
 * Created by shoma2da on 2014/06/30.
 */

public class CountdownActivity : Activity() {

    class object {
        val TIME_PARAM_NAME = "time_param"
    }

    override fun onCreate(savedInstance : Bundle?) {
        super.onCreate(savedInstance)
        setContentView(R.layout.activity_countdown)

        val timeText = findViewById(R.id.timeText) as TextView

        val time = getIntent()?.getSerializableExtra(TIME_PARAM_NAME) as RemainTime
        timeText.setText(time.toString())

        //サービスを起動する
        val intent = Intent(this, javaClass<CowntdownService>())
        intent.putExtra(CowntdownService.TIME_PARAM_NAME, time)
        intent.putExtra(CowntdownService.ACTION_PARAM_NAME, CowntdownService.Action.START as Serializable)
        startService(intent)

        //カウントダウン
        fun countdownToZero(time:RemainTime) {
            timeText.setText(time.toString())
            when (time.isEmpty()) {
                true -> {}//nothing
                false -> {
                    time.countdown { countdownToZero(it) }
                }
            }
        }
        time.countdown{ countdownToZero(it) }
    }

}
