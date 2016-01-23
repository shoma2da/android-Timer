package com.hatenablog.shoma2da.android.timer.v1_2.entrypoint.presentation.activity

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Vibrator
import android.preference.PreferenceManager
import android.util.Log
import android.view.*
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdView
import com.hatenablog.shoma2da.android.timer.R
import com.hatenablog.shoma2da.android.timer.v1_2.domain.countdown.CountdownService
import com.hatenablog.shoma2da.android.timer.v1_2.domain.library.remaintime.RemainTime
import com.hatenablog.shoma2da.android.timer.v1_2.domain.notificationlauncher.NotificationMethodSetting
import com.hatenablog.shoma2da.android.timer.v1_2.domain.please_review.PleaseReviewCondition
import com.hatenablog.shoma2da.android.timer.v1_2.util.extensions.getLogger
import com.hatenablog.shoma2da.android.timer.v1_2.util.extensions.load

public class CountdownActivity : Activity() {

    class OnPauseButtonClickListener(val timeText: TextView) : OnClickListener {
        override fun onClick(view : View) {
            val context = view.context ?: return

            val logger = context.getLogger()

            val pauseText = context.getString(R.string.pause)
            val restartText = context.getString(R.string.restart)

            val button = view as Button
            when(button.text) {
                pauseText -> {
                    //サービスを停止
                    val intent = Intent(context, CountdownService::class.java)
                    intent.putExtra(CountdownService.PARAM_NAME_ACTION, CountdownService.Action.STOP.name)
                    context.startService(intent)

                    //Analytics
                    logger.sendEvent("timer", "pause")

                    button.text = restartText
                    button.setBackgroundResource(R.drawable.round_button_green)
                }
                restartText -> {
                    //サービスを再開
                    val intent = Intent(context, CountdownService::class.java)
                    intent.putExtra(CountdownService.PARAM_NAME_ACTION, CountdownService.Action.START.name)
                    intent.putExtra(CountdownService.PARAM_NAME_TIME, timeText.tag as RemainTime)
                    context.startService(intent)

                    button.text = pauseText
                    button.setBackgroundResource(R.drawable.round_button_yellow)

                    //Analytics
                    logger.sendEvent("timer", "restart")
                }
            }
        }
    }

    companion object {
        val TIME_PARAM_NAME = "time_param"
        val START_COUNTDOWN_PARAM_NAME = "countdown_param"
    }

    private var mTimeText: TextView? = null
    private var mReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstance : Bundle?) {
        super.onCreate(savedInstance)
        setContentView(R.layout.activity_countdown)

        //Analytics
        val logger = application.getLogger()
        logger.sendScreenLog("CountdounActivity")

        //カウントダウン処理だったら初期設定を全て飛ばす
        val action = intent?.action
        if (action == CountdownService.ACTION_FINISH_COUNTDOWN) {
            Log.d("shomatsu", "FINISH_COUNTDOWN")
            onNewIntent(intent)
            return@onCreate
        }

        //パラメータを受け取る
        val time = intent?.getSerializableExtra(TIME_PARAM_NAME) as RemainTime

        //表示の初期設定
        setupViews(time)

        //Receiverの設定
        mReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val receivedTime = intent.getSerializableExtra(CountdownService.PARAM_NAME_TIME) as RemainTime
                mTimeText?.text = receivedTime.toString()
                mTimeText?.tag = receivedTime
            }
        }
        val filter = IntentFilter()
        filter.addAction(CountdownService.ACTION_UPDATE_REMAINTIME)
        registerReceiver(mReceiver, filter)

        //パラメータでカウントダウンを始めるか判断する
        val startCountdown = intent?.getBooleanExtra(START_COUNTDOWN_PARAM_NAME, true)
        if (startCountdown == false) {
            return
        }

        //サービスを起動する
        val intent = Intent(this, CountdownService::class.java)
        intent.putExtra(CountdownService.PARAM_NAME_TIME, time)
        intent.putExtra(CountdownService.PARAM_NAME_ACTION, CountdownService.Action.START.name)
        startService(intent)

        //Analytics
        logger.sendEvent("timer", "start", time.toString())
    }

    private fun setupViews(time: RemainTime) {
        //ボタンの動作設定
        mTimeText = findViewById(R.id.timeText) as TextView
        if (mTimeText != null) {
            findViewById(R.id.pauseButton)?.setOnClickListener(OnPauseButtonClickListener(mTimeText as TextView))
        }
        findViewById(R.id.cancelButton)?.setOnClickListener({ view ->
            //ダイアログを表示
            AlertDialog.Builder(this)
                    .setMessage(R.string.stop_confirm_message)
                    .setPositiveButton(R.string.yes, { dialog, which ->
                        //サービスを停止
                        val intent = Intent(this, CountdownService::class.java)
                        intent.putExtra(CountdownService.PARAM_NAME_ACTION, CountdownService.Action.STOP.name)
                        startService(intent)

                        //リストページに戻る
                        startActivity(Intent(this, MainActivity::class.java))

                        //Analytics
                        val logger = application.getLogger()
                        logger.sendEvent("timer", "cancel", time.toString())

                        finish()
                    })
                    .setNegativeButton(R.string.no, { dialog, whici ->
                        dialog.dismiss()
                    })
                    .create().show()
        })

        //初期表示設定
        mTimeText?.text = time.toString()
        mTimeText?.tag = time

        //広告設定
        (findViewById(R.id.adView) as AdView).load()
    }

    protected override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        when (intent?.action) {
            CountdownService.ACTION_FINISH_COUNTDOWN -> {

                //開始処理
                val player = MediaPlayer.create(this, R.raw.alarm)
                val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                NotificationMethodSetting.load(this).action(
                    onSound = {
                        //音の開始
                        player?.isLooping = true
                        player?.start()
                    },
                    onVibration = {
                        //バイブレーション開始
                        vibrator.vibrate(longArrayOf(1000L, 1000L), 0)
                    },
                    onBoth = {
                        //音の開始
                        player?.isLooping = true
                        player?.start()

                        //バイブレーション開始
                        vibrator.vibrate(longArrayOf(1000L, 1000L), 0)
                    }
                )

                //Analytics
                val logger = application.getLogger()
                logger.sendEvent("timer", "finish")

                //画面を点灯する
                val window = window
                window?.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

                mTimeText?.text = RemainTime(0, 0).toString()

                //ダイアログ準備
                val dialog = Dialog(this)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false)
                val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater;
                val dialogLayout = inflater.inflate(R.layout.dialog_finish_timer_dialog, null);
                dialogLayout.findViewById(R.id.button_ok).setOnClickListener {
                    stopNotification(player, vibrator)
                }
                dialog.setContentView(dialogLayout)

                //広告読み込み
                val adView = dialogLayout.findViewById(R.id.adView) as AdView;
                adView.load()
                adView.adListener = object:AdListener() {
                    override fun onAdOpened() {
                        super.onAdOpened()
                        stopNotification(player, vibrator)
                    }
                }

                //ダイアログ表示
                dialog.show()

                //レビューお願い用のイベントカウント
                val preferences = PreferenceManager.getDefaultSharedPreferences(this)
                val condition = PleaseReviewCondition.create(preferences)
                condition?.addCount()
            }
            else -> {} //nothing
        }
    }

    private fun stopNotification(player:MediaPlayer, vibrator:Vibrator) {
        player.stop()
        vibrator.cancel()

        //リストページに戻る
        startActivity(Intent(this, MainActivity::class.java))

        finish()
    }

    override fun onConfigurationChanged(configuration: Configuration) {
        //一時停止ボタンを回転後の表示変更用に取っておく
        val pauseButton = findViewById(R.id.pauseButton) as Button?

        //表示の更新
        super.onConfigurationChanged(configuration)
        setContentView(R.layout.activity_countdown)

        //動作状況、残り時間を反映した表示の初期設定
        if (mTimeText != null) {
            setupViews(mTimeText!!.tag as RemainTime) //直前までの残り時間を設定する
        }

        //一時停止ボタンの表示を変更
        val restartText = getString(R.string.restart)
        if (restartText.equals(pauseButton?.text)) {
            val button = findViewById(R.id.pauseButton) as Button?
            button?.text = restartText
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

    override fun onKeyDown(keyCode:Int, event: KeyEvent):Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            findViewById(R.id.cancelButton)?.performClick()
            return@onKeyDown true
        }
        return false
    }

}
