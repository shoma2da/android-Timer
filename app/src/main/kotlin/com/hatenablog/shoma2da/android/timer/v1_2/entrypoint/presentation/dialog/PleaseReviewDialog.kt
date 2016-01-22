package com.hatenablog.shoma2da.android.timer.v1_2.entrypoint.presentation.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import com.google.android.gms.analytics.HitBuilders
import com.hatenablog.shoma2da.android.timer.R
import com.hatenablog.shoma2da.android.timer.v1_2.entrypoint.presentation.activity.RequestActivity
import com.hatenablog.shoma2da.android.timer.TimerApplication
import com.hatenablog.shoma2da.android.timer.v1_2.domain.please_review.PleaseReviewCondition

class PleaseReviewDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val activity = activity!!

        //Analytics準備
        val tracker = (activity.application as TimerApplication?)?.getTracker()
        tracker?.send(HitBuilders.EventBuilder()
                .setCategory("please_review")
                ?.setAction("show")
                ?.build())

        val preferences = PreferenceManager.getDefaultSharedPreferences(activity)
        val condition = PleaseReviewCondition.create(preferences)

        isCancelable = false

        return AlertDialog.Builder(activity)
                .setTitle(activity.getString(R.string.app_name))
                .setMessage(R.string.please_review_message)
                .setPositiveButton(R.string.please_review_good, { dialog, which ->
                    //Google Playに遷移
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setData(Uri.parse("market://details?id=${activity.getPackageName()}"))
                    activity.startActivity(intent)

                    //Toast表示
                    Toast.makeText(activity, R.string.please_review_google_play, Toast.LENGTH_LONG).show()

                    //レビューお願いはもう表示しない
                    condition?.setNeverShow()

                    //Analytics
                    tracker?.send(HitBuilders.EventBuilder()
                            .setCategory("please_review")
                            ?.setAction("tap")
                            ?.setLabel("good")
                            ?.build())

                    dialog.dismiss()
                })
                .setNegativeButton(R.string.please_review_bad, { dialog, which ->
                    //要望画面へ遷移
                    activity.startActivity(Intent(activity, javaClass<RequestActivity>()))

                    //Toast表示
                    Toast.makeText(activity, R.string.please_review_send_your_opinion, Toast.LENGTH_LONG).show()

                    //レビューお願いはもう表示しない
                    condition?.setNeverShow()

                    //Analytics
                    tracker?.send(HitBuilders.EventBuilder()
                            .setCategory("please_review")
                            ?.setAction("tap")
                            ?.setLabel("bad")
                            ?.build())

                    dialog.dismiss()
                })
                .create()
    }

}
