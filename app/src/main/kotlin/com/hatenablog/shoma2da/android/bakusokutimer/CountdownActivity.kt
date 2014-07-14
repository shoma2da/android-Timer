package com.hatenablog.shoma2da.android.bakusokutimer

import android.app.Activity
import android.os.Bundle
import com.hatenablog.shoma2da.android.bakusokutimer.model.RemainTime
import android.widget.TextView

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

        time.countdown { time -> timeText.setText(time.toString()) }
    }

}
