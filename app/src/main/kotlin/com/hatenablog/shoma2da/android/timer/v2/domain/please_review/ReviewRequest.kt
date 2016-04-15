package com.hatenablog.shoma2da.android.timer.v2.domain.please_review

import android.app.Activity
import android.preference.PreferenceManager
import com.hatenablog.shoma2da.android.timer.v2.entrypoint.presentation.dialog.PleaseReviewDialog

class ReviewRequest {
    fun show(activity: Activity) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(activity)
        val condition = PleaseReviewCondition.create(preferences)
        if (condition != null && condition.isTiming()) {
            PleaseReviewDialog().show(activity.fragmentManager, "please_review")
        }
    }
}