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
import android.app.AlertDialog
import android.os.Vibrator

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

            val pauseText = context.getString(R.string.pause)
            val restartText = context.getString(R.string.restart)

            val button = view as Button
            when(button.getText()) {
                pauseText -> {
                    //サービスを停止
                    val intent = Intent(context, javaClass<CountdownService>())
                    intent.putExtra(CountdownService.PARAM_NAME_ACTION, CountdownService.Action.STOP as Serializable)
                    context.startService(intent)

                    button.setText(restartText)
                }
                restartText -> {
                    //サービスを再開
                    val intent = Intent(context, javaClass<CountdownService>())
                    intent.putExtra(CountdownService.PARAM_NAME_ACTION, CountdownService.Action.START as Serializable)
                    intent.putExtra(CountdownService.PARAM_NAME_TIME, timeText.getTag() as Serializable)
                    context.startService(intent)

                    button.setText(pauseText)
                }
            }
        }
    }

    class object {
        val TIME_PARAM_NAME = "time_param"
        val START_COUNTDOWN_PARAM_NAME = "countdown_param"
    }

    private var mTimeText:TextView? = null
    private var mReceiver:BroadcastReceiver? = null

    override fun onCreate(savedInstance : Bundle?) {
        super.onCreate(savedInstance)
        setContentView(R.layout.activity_countdown)
        mTimeText = findViewById(R.id.timeText) as TextView

        //カウントダウン処理だったら初期設定を全て飛ばす
        val action = getIntent()?.getAction()
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

            //リストページに戻る
            startActivity(Intent(this, javaClass<MainActivity>()))

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

        //パラメータでカウントダウンを始めるか判断する
        val startCountdown = getIntent()?.getBooleanExtra(START_COUNTDOWN_PARAM_NAME, true)
        if (startCountdown == false) {
            return @onCreate
        }

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
                //バイブレーション開始
                val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrator.vibrate(longArray(1000L, 1000L), 0)

                mTimeText?.setText(RemainTime(0, 0).toString())
                AlertDialog.Builder(this).
                        setCancelable(false).
                        setTitle("タイマー終了しました").
                        setPositiveButton("OK", { dialog, which ->
                            vibrator.cancel()
                            dialog.dismiss()

                            //リストページに戻る
                            startActivity(Intent(this, javaClass<MainActivity>()))

                            finish()
                        }).
                        create().show()
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
