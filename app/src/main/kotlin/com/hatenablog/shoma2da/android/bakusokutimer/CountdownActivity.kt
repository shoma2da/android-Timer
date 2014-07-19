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
import android.widget.Toast
import android.util.Log
import android.view.View.OnClickListener
import android.view.View
import android.widget.Button

/**
 * Created by shoma2da on 2014/06/30.
 */

public class CountdownActivity : Activity() {

    class OnPauseButtonClickListener(val timeText:TextView) : OnClickListener {
        override fun onClick(view : View) {
            val context = view.getContext()
            if (context == null) {
                return @onClick
            }

            val button = view as Button
            when(button.getText()) {
                "一時停止" -> {
                    //サービスを停止
                    val intent = Intent(context, javaClass<CountdownService>())
                    intent.putExtra(CountdownService.PARAM_NAME_ACTION, CountdownService.Action.STOP as Serializable)
                    context.startService(intent)

                    button.setText("スタート")
                }
                "スタート" -> {
                    //サービスを再開
                    val intent = Intent(context, javaClass<CountdownService>())
                    intent.putExtra(CountdownService.PARAM_NAME_ACTION, CountdownService.Action.START as Serializable)
                    intent.putExtra(CountdownService.PARAM_NAME_TIME, timeText.getTag() as Serializable)
                    context.startService(intent)

                    button.setText("一時停止")
                }
            }
        }
    }

    class object {
        val TIME_PARAM_NAME = "time_param"
    }

    private var mTimeText:TextView? = null
    private var mReceiver:BroadcastReceiver? = null

    override fun onCreate(savedInstance : Bundle?) {
        super.onCreate(savedInstance)
        setContentView(R.layout.activity_countdown)
        mTimeText = findViewById(R.id.timeText) as TextView

        //カウントダウン処理だったら初期設定を全て飛ばす
        val action = getIntent()?.getAction()
        Log.d("shomatsu", "action is ${action}")
        if (action == CountdownService.ACTION_FINISH_COUNTDOWN) {
            Log.d("shomatsu", "FINISH_COUNTDOWN")
            onNewIntent(getIntent())
            return@onCreate
        }

        //ボタンの動作設定
        if (mTimeText != null) {
            findViewById(R.id.pauseButton)?.setOnClickListener(CountdownActivity.OnPauseButtonClickListener(mTimeText as TextView))
        }
        findViewById(R.id.cancelButton)?.setOnClickListener({ view ->
            //サービスを停止
            val intent = Intent(this, javaClass<CountdownService>())
            intent.putExtra(CountdownService.PARAM_NAME_ACTION, CountdownService.Action.STOP as Serializable)
            startService(intent)

            finish()
        })

        //初期表示設定
        val time = getIntent()?.getSerializableExtra(TIME_PARAM_NAME) as RemainTime
        mTimeText?.setText(time.toString())

        //Receiverの設定
        mReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val time = intent.getSerializableExtra(CountdownService.PARAM_NAME_TIME) as RemainTime
                mTimeText?.setText(time.toString())
                mTimeText?.setTag(time)
            }
        }
        val filter = IntentFilter()
        filter.addAction(CountdownService.ACTION_UPDATE_REMAINTIME)
        registerReceiver(mReceiver, filter)

        //サービスを起動する
        val intent = Intent(this, javaClass<CountdownService>())
        intent.putExtra(CountdownService.PARAM_NAME_TIME, time)
        intent.putExtra(CountdownService.PARAM_NAME_ACTION, CountdownService.Action.START as Serializable)
        startService(intent)
    }

    protected override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        when (intent?.getAction()) {
            CountdownService.ACTION_FINISH_COUNTDOWN -> {
                mTimeText?.setText(RemainTime(0, 0).toString())
                Toast.makeText(this, "終了！", Toast.LENGTH_SHORT).show()
            }
            else -> {} //nothing
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mReceiver != null) {
            unregisterReceiver(mReceiver as BroadcastReceiver)
        }

        mReceiver = null
        mTimeText = null
    }

}
