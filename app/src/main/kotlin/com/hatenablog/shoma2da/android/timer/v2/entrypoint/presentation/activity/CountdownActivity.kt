package com.hatenablog.shoma2da.android.timer.v2.entrypoint.presentation.activity

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
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.TextView
import co.meyasuba.android.sdk.Meyasubaco
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdView
import com.hatenablog.shoma2da.android.timer.R
import com.hatenablog.shoma2da.android.timer.v2.domain.countdown.CountdownService
import com.hatenablog.shoma2da.android.timer.v2.domain.library.remaintime.RemainTime
import com.hatenablog.shoma2da.android.timer.v2.domain.please_review.PleaseReviewCondition
import com.hatenablog.shoma2da.android.timer.v2.domain.setting.NotificationLengthSetting
import com.hatenablog.shoma2da.android.timer.v2.domain.setting.NotificationMethodSetting
import com.hatenablog.shoma2da.android.timer.v2.util.extensions.getLogger
import com.hatenablog.shoma2da.android.timer.v2.util.extensions.isSilentMode
import com.hatenablog.shoma2da.android.timer.v2.util.extensions.load
import com.hatenablog.shoma2da.android.timer.v2.util.extensions.tweet
import org.json.JSONObject
import kotlin.concurrent.thread

public class CountdownActivity : AppCompatActivity() {

    companion object {
        val TIME_PARAM_NAME = "time_param"

        val START_COUNTDOWN_PARAM_NAME = "countdown_param"

        fun start(context: Activity?, time: RemainTime, view: View) {
            val clazz: Class<CountdownActivity> = CountdownActivity::class.java
            val intent = Intent(context, clazz)
            intent.putExtra(TIME_PARAM_NAME, time)
            view.context?.startActivity(intent)
        }
    }

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
                    Meyasubaco.getInstance(context).sendEvent("timer_pause", JSONObject())

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
                    Meyasubaco.getInstance(context).sendEvent("restart", JSONObject())
                }
            }
        }
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
        Meyasubaco.getInstance(this).sendEvent("timer_start", JSONObject().apply {
            put("time", time.toString())
        })
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
                        Meyasubaco.getInstance(this).sendEvent("timer_cancel", JSONObject().apply {
                            put("time", time.toString())
                        })

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
                        startSound(player)

                        //端末音量がゼロの場合は場合はバイブレーションを出す
                        if (isSilentMode()) {
                            startVibration(vibrator)
                        }
                    },
                    onVibration = {
                        startVibration(vibrator)
                    },
                    onBoth = {
                        startSound(player)
                        startVibration(vibrator)
                    }
                )

                NotificationLengthSetting.load(this).action { isLimitLess, length ->
                    if (isLimitLess) {
                        return@action
                    }

                    thread {
                        Thread.sleep(length * 1000L)

                        if (player.isPlaying) {
                            player.stop()
                        }
                        vibrator.cancel()
                    }
                }

                //Analytics
                val logger = application.getLogger()
                logger.sendEvent("timer", "finish")
                Meyasubaco.getInstance(this).sendEvent("timer_finish", JSONObject())

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
                val pref = PreferenceManager.getDefaultSharedPreferences(this);
                if (pref.getBoolean("already_click_share_button", false)) {
                    dialogLayout.findViewById(R.id.button_share).visibility = View.GONE
                }
                dialogLayout.findViewById(R.id.button_share).setOnClickListener {
                    pref.edit().putBoolean("already_click_share_button", true).apply();

                    application.tweet("タイマーはこれぐらいの簡単さがちょうど良いかも! %23瞬間タイマー", "https://play.google.com/store/apps/details?id=com.hatenablog.shoma2da.android.timer&hl=ja&utm_source=app_share&utm_medium=app&utm_campaign=app_share")
                    logger.sendEvent("timer", "share")

                    if (player.isPlaying) {
                        player.stop()
                    }
                    vibrator.cancel()
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

    private fun startVibration(vibrator: Vibrator) {
        vibrator.vibrate(longArrayOf(1000L, 1000L), 0)
    }

    private fun startSound(player: MediaPlayer) {
        player?.isLooping = true
        player?.start()
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
