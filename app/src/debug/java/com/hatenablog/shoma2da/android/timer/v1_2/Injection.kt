package com.hatenablog.shoma2da.android.timer.v1_2

import android.content.Context
import com.hatenablog.shoma2da.android.timer.v1_2.log.Logger

class Injection {

    companion object {

        fun logger(context: Context): Logger = Logger()

    }

}