package com.hatenablog.shoma2da.android.timer

import android.app.Activity
import android.os.Bundle
import com.hatenablog.shoma2da.android.timer.model.RemainTime
import android.widget.TextView
import android.content.Intent
import java.io.Serializable
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.content.Context
import android.util.Log
import android.view.View.OnClickListener
import android.view.View
import android.app.AlertDialog
import android.os.Vibrator
import android.view.WindowManager
import android.widget.Button
import android.content.res.Configuration
import com.google.android.gms.analytics.HitBuilders

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

            val tracker = (context.getApplicationContext() as TimerApplication?)?.getTracker()

            val pauseText = context.getString(R.string.pause)
            val restartText = context.getString(R.string.restart)

            val button = view as Button
            when(button.getText()) {
                pauseText -> {
                    //サービスを停止
                    val intent = Intent(context, javaClass<CountdownService>())
                    intent.putExtra(CountdownService.PARAM_NAME_ACTION, CountdownService.Action.STOP as Serializable)
                    context.startService(intent)

                    //Analytics
                    tracker?.send(HitBuilders.EventBuilder()
                            .setCategory("timer")
                            ?.setAction("pause")
                            ?.build())

                    button.setText(restartText)
                    button.setBackgroundResource(R.drawable.round_button_green)
                }
                restartText -> {
                    //サービスを再開
                    val intent = Intent(context, javaClass<CountdownService>())
                    intent.putExtra(CountdownService.PARAM_NAME_ACTION, CountdownService.Action.START as Serializable)
                    intent.putExtra(CountdownService.PARAM_NAME_TIME, timeText.getTag() as Serializable)
                    context.startService(intent)

                    button.setText(pauseText)
                    button.setBackgroundResource(R.drawable.round_button_yellow)

                    //Analytics
                    tracker?.send(HitBuilders.EventBuilder()
                            .setCategory("timer")
                            ?.setAction("restart")
                            ?.build())
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

        //Analytics
        val tracker = (getApplication() as TimerApplication?)?.getTracker()
        tracker?.setScreenName("CountdounActivity")
        tracker?.send(HitBuilders.ScreenViewBuilder().build());

        //カウントダウン処理だったら初期設定を全て飛ばす
        val action = getIntent()?.getAction()
        if (action == CountdownService.ACTION_FINISH_COUNTDOWN) {
            Log.d("shomatsu", "FINISH_COUNTDOWN")
            onNewIntent(getIntent())
            return@onCreate
        }

        //パラメータを受け取る
        val time = getIntent()?.getSerializableExtra(TIME_PARAM_NAME) as RemainTime

        //表示の初期設定
        setupViews(time)

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

        //Analytics
        tracker?.send(HitBuilders.EventBuilder()
                .setCategory("timer")
                ?.setAction("start")
                ?.setLabel(time.toString())
                ?.build())
    }

    private fun setupViews(time:RemainTime) {
        //ボタンの動作設定
        mTimeText = findViewById(R.id.timeText) as TextView
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

            //Analytics
            val tracker = (getApplication() as TimerApplication?)?.getTracker()
            tracker?.send(HitBuilders.EventBuilder()
                    .setCategory("timer")
                    ?.setAction("cancel")
                    ?.setLabel(time.toString())
                    ?.build())

            finish()
        })

        //初期表示設定
        mTimeText?.setText(time.toString())
        mTimeText?.setTag(time)
    }

    protected override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        when (intent?.getAction()) {
            CountdownService.ACTION_FINISH_COUNTDOWN -> {
                //バイブレーション開始
                val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrator.vibrate(longArray(1000L, 1000L), 0)

                //Analytics
                val tracker = (getApplication() as TimerApplication?)?.getTracker()
                tracker?.send(HitBuilders.EventBuilder()
                        .setCategory("timer")
                        ?.setAction("finish")
                        ?.build())

                //画面を点灯する
                val window = getWindow()
                window?.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

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

    override fun onConfigurationChanged(configuration:Configuration) {
        //一時停止ボタンを回転後の表示変更用に取っておく
        val pauseButton = findViewById(R.id.pauseButton) as Button?

        //表示の更新
        super.onConfigurationChanged(configuration)
        setContentView(R.layout.activity_countdown)

        //動作状況、残り時間を反映した表示の初期設定
        if (mTimeText != null) {
            setupViews(mTimeText!!.getTag() as RemainTime) //直前までの残り時間を設定する
        }

        //一時停止ボタンの表示を変更
        val restartText = getString(R.string.restart)
        if (restartText.equals(pauseButton?.getText())) {
            val button = findViewById(R.id.pauseButton) as Button?
            button?.setText(restartText)
            button?.setBackgroundResource(R.drawable.round_button_green)
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
