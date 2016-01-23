package com.hatenablog.shoma2da.android.timer.v1_2.util.extensions

fun Int.format(disits: Int) = java.lang.String.format("%0${disits}d", this)
