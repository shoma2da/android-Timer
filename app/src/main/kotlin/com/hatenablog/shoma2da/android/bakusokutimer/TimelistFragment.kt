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
import com.hatenablog.shoma2da.android.bakusokutimer.model.Time

/**
 * Created by shoma2da on 2014/06/29.
 */

public class TimelistFragment : Fragment() {

    public override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_timelist, null)
        val context = getActivity()

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
                val time = Time.createFromString(text.toString())

                val clazz:Class<CountdownActivity> = javaClass<CountdownActivity>()
                val intent = Intent(context, clazz)
                intent.putExtra(CountdownActivity.TIME_PARAM_NAME, time)
                view.getContext()?.startActivity(intent)
            }
        })

        return view
    }

}
