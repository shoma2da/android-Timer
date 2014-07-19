package com.hatenablog.shoma2da.android.bakusokutimer

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import android.app.Fragment
import android.widget.ListView
import android.widget.ArrayAdapter
import android.app.Activity
import android.widget.TextView
import android.util.Log
import android.content.Intent
import com.hatenablog.shoma2da.android.bakusokutimer.model.RemainTime
import java.io.Serializable
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter

/**
 * Created by shoma2da on 2014/06/29.
 */

public class TimelistFragment : Fragment() {

    private var mReceiver:BroadcastReceiver? = null
    private var mActivity:Activity? = null

    public override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_timelist, null)
        val context: Activity? = getActivity()

        if (context == null || view == null) {
            return view
        }

        //とりあえず固定でデータ挿入
        val adapter = ArrayAdapter<String>(context, R.layout.column_timelist)
        for (integer in 1..90) {
            adapter.add("${java.lang.String.format("%02d", integer)}:00")
        }

        //リストの初期設定
        val list = view.findViewById(R.id.list) as ListView
        list.setAdapter(adapter)
        list.setOnItemClickListener({parent, view, position, id ->
            val text = (view as TextView).getText()

            if (text != null) {
                val time = RemainTime.createFromString(text.toString())

                val clazz:Class<CountdownActivity> = javaClass<CountdownActivity>()
                val intent = Intent(context, clazz)
                intent.putExtra(CountdownActivity.TIME_PARAM_NAME, time)
                view.getContext()?.startActivity(intent)

                mActivity?.finish()
            }
        })

        return view
    }

    public override fun onAttach(activity: Activity?) {
        super.onAttach(activity)

        mActivity = activity
        if (activity == null) {
            return
        }

        //サービスの稼働状況を問い合わせ
        val intent = Intent(activity, javaClass<CountdownService>())
        intent.putExtra(CountdownService.PARAM_NAME_ACTION, CountdownService.Action.CONFIRM_STATUS as Serializable)
        activity.startService(intent)

        //サービスの稼働状況を受け取れるようにしておく
        mReceiver = object:BroadcastReceiver() {
            override fun onReceive(context: Context, paramIntent: Intent) {
                val status = paramIntent.getSerializableExtra(CountdownService.PARAM_NAME_STATUS) as CountdownService.Status
                when (status) {
                    CountdownService.Status.START -> {
                        //既にタイマー設定されているならカウントダウン画面に遷移する
                        val time = paramIntent.getSerializableExtra(CountdownService.PARAM_NAME_TIME) as RemainTime
                        val intent = Intent(context, javaClass<CountdownActivity>())
                        intent.putExtra(CountdownActivity.TIME_PARAM_NAME, time)
                        intent.putExtra(CountdownActivity.START_COUNTDOWN_PARAM_NAME, false)
                        context.startActivity(intent)

                        mActivity?.finish()
                    }
                    else -> {} //nothing
                }
            }
        }
        activity.registerReceiver(mReceiver, IntentFilter(CountdownService.ACTION_BROADCAST_STATUS))
    }

    public override fun onDetach() {
        super.onDetach()

        if (mActivity != null && mReceiver != null) {
            mActivity?.unregisterReceiver(mReceiver as BroadcastReceiver)
        }
        mActivity = null
        mReceiver = null
    }

}
