package com.hatenablog.shoma2da.android.timer.v2.entrypoint.presentation.fragment

import android.app.Activity
import android.app.Fragment
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.android.gms.ads.AdView
import com.hatenablog.shoma2da.android.timer.R
import com.hatenablog.shoma2da.android.timer.v2.domain.countdown.CountdownService
import com.hatenablog.shoma2da.android.timer.v2.domain.library.remaintime.RemainTime
import com.hatenablog.shoma2da.android.timer.v2.domain.setting.NotificationMethodSetting
import com.hatenablog.shoma2da.android.timer.v2.entrypoint.presentation.activity.CountdownActivity
import com.hatenablog.shoma2da.android.timer.v2.util.extensions.isSilentMode
import com.hatenablog.shoma2da.android.timer.v2.util.extensions.load
import com.hatenablog.shoma2da.android.timer.v2.util.extensions.showSimpleAlertDialog

class TimeListFragment : Fragment() {

    private var mReceiver: BroadcastReceiver? = null
    private var mActivity: Activity? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_timelist, null)
        val context: Activity? = activity

        if (context == null || view == null) {
            return view
        }

        //とりあえず固定でデータ挿入
        var remainTimes = Array(120, { RemainTime(it + 1, 0) }) //残り時間オブジェクトを無理やり用意
        val adapter = ArrayAdapter<String>(context, R.layout.column_timelist)
        for (integer in 1..120) {
            val remainTime = RemainTime(integer, 0)
            adapter.add(remainTime.toString())
        }

        //リストの初期設定
        val list = view.findViewById(R.id.list) as ListView
        list.adapter = adapter
        val listener = createListItemClickListener(context, remainTimes)
        list.setOnItemClickListener(listener)

        //広告設定
        (view.findViewById(R.id.adView) as AdView).load()

        return view
    }

    private fun createListItemClickListener(context: Activity?, remainTimes: Array<RemainTime>):
                                                (AdapterView<*>, View, Int, Long) -> Unit {
        return { parent, view, position, id ->
            NotificationMethodSetting.load(mActivity!!).action(
                onSound = {
                    val result = mActivity?.isSilentMode()
                    if (result != null && result) {
                        mActivity?.showSimpleAlertDialog("音量がゼロです。設定を変更してから再度タイマーを設定しください。", "OK")
                    } else {
                        startCountdownActivity(context, position, remainTimes, view)
                    }
                },
                onBoth = { startCountdownActivity(context, position, remainTimes, view) },
                onVibration = { startCountdownActivity(context, position, remainTimes, view) }
            )
        }
    }

    private fun startCountdownActivity(context: Activity?, position: Int, remainTimes: Array<RemainTime>, view: View) {
        val time = remainTimes[position]
        CountdownActivity.start(context, time, view)
        mActivity?.finish()
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)

        mActivity = activity
        if (activity == null) {
            return
        }

        //サービスの稼働状況を問い合わせ
        val intent = Intent(activity, CountdownService::class.java)
        intent.putExtra(CountdownService.PARAM_NAME_ACTION, CountdownService.Action.CONFIRM_STATUS.name)
        activity.startService(intent)

        //サービスの稼働状況を受け取れるようにしておく
        mReceiver = object: BroadcastReceiver() {
            override fun onReceive(context: Context, paramIntent: Intent) {
                val statusString = paramIntent.getStringExtra(CountdownService.PARAM_NAME_STATUS) ?: return

                val status = CountdownService.Status.valueOf(statusString)
                when (status) {
                    CountdownService.Status.START -> {
                        //既にタイマー設定されているならカウントダウン画面に遷移する
                        val time = paramIntent.getSerializableExtra(CountdownService.PARAM_NAME_TIME) as RemainTime
                        val countdownIntent = Intent(context, CountdownActivity::class.java)
                        countdownIntent.putExtra(CountdownActivity.TIME_PARAM_NAME, time)
                        countdownIntent.putExtra(CountdownActivity.START_COUNTDOWN_PARAM_NAME, false)
                        context.startActivity(countdownIntent)

                        mActivity?.finish()
                    }
                    else -> {} //nothing
                }
            }
        }
        activity.registerReceiver(mReceiver, IntentFilter(CountdownService.ACTION_BROADCAST_STATUS))
    }

    override fun onDetach() {
        super.onDetach()

        if (mActivity != null && mReceiver != null) {
            mActivity?.unregisterReceiver(mReceiver as BroadcastReceiver)
        }
        mActivity = null
        mReceiver = null
    }

}
