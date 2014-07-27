package com.hatenablog.shoma2da.android.timer

import android.app.Application
import com.parse.Parse
import android.util.Log
import com.parse.ParseObject

class TimerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Parse.initialize(this, "JD6OCOsnYojTL0yQLqoYYBINmf7s9ugK6uHZgfBa", "GZvBpMmBaMG7ejDatcTUcBCsq9kxDOir4LYZAIIX")

        //Parseテスト
        val testObject = ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();

        Log.d("shoma2da", "*****************************************")
        Log.d("shoma2da", "parse saved!!")
        Log.d("shoma2da", "*****************************************")
    }

}