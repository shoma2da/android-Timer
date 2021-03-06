package com.hatenablog.shoma2da.android.timer.v2.entrypoint.presentation.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import com.hatenablog.shoma2da.android.timer.R
import com.hatenablog.shoma2da.android.timer.v2.domain.please_review.PleaseReviewCondition

class PleaseReviewDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val activity = activity!!

        val preferences = PreferenceManager.getDefaultSharedPreferences(activity)
        val condition = PleaseReviewCondition.create(preferences)

        isCancelable = false

        return AlertDialog.Builder(activity)
                .setTitle(activity.getString(R.string.app_name))
                .setMessage(R.string.please_review_message)
                .setPositiveButton(R.string.please_review_good, { dialog, which ->
                    //Google Playに遷移
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setData(Uri.parse("market://details?id=${activity.packageName}"))
                    activity.startActivity(intent)

                    //Toast表示
                    Toast.makeText(activity, R.string.please_review_google_play, Toast.LENGTH_LONG).show()

                    //レビューお願いはもう表示しない
                    condition?.setNeverShow()

                    dialog.dismiss()
                })
                .setNegativeButton(R.string.please_review_bad, { dialog, which ->
                    //Toast表示
                    Toast.makeText(activity, R.string.please_review_send_your_opinion, Toast.LENGTH_LONG).show()

                    //レビューお願いはもう表示しない
                    condition?.setNeverShow()

                    dialog.dismiss()
                })
                .create()
    }

}
