package com.hatenablog.shoma2da.android.bakusokutimer

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import android.app.Fragment
import android.widget.ListView
import android.widget.ArrayAdapter
import android.app.Activity

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

        val adapter = ArrayAdapter<String>(context, R.layout.column_timelist)

        //とりあえず固定でデータ挿入
        for (integer in 1..90) {
            adapter.add("${java.lang.String.format("%02d", integer)}:00")
        }

        val list = view.findViewById(R.id.list) as ListView
        list.setAdapter(adapter)

        return view
    }

}
