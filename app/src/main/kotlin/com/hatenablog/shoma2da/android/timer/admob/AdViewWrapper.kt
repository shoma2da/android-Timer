package com.hatenablog.shoma2da.android.timer.admob

import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.AdRequest
import android.view.View

class AdViewWrapper(val adView:AdView) {

    companion object {
        val NO_AD = false
    }

    fun loadAd() {
        if (NO_AD) {
            adView.visibility = View.GONE
            return
        }
        val request = AdRequest.Builder().build()
        adView.loadAd(request)
    }

}
