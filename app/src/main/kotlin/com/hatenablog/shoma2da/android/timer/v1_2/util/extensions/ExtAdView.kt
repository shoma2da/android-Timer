package com.hatenablog.shoma2da.android.timer.v1_2.util.extensions

import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

fun AdView.load() {
    val request = AdRequest.Builder().build()
    this.loadAd(request)
}