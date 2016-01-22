package com.hatenablog.shoma2da.android.timer.extensions

fun Int.format(disits: Int) = java.lang.String.format("%0${disits}d", this)
