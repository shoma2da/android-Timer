package com.hatenablog.shoma2da.android.timer.viewmodel.please_review

import android.app.DialogFragment
import android.app.Dialog
import android.os.Bundle
import android.app.AlertDialog
import com.hatenablog.shoma2da.android.timer.R
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.hatenablog.shoma2da.android.timer.RequestActivity
import android.preference.PreferenceManager
import com.hatenablog.shoma2da.android.timer.TimerApplication
import com.google.android.gms.analytics.HitBuilders

/**
 * Created by shoma2da on 2014/07/29.
 */
class PleaseReviewDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState:Bundle?):Dialog {
        val activity = getActivity()!!

        //Analytics準備
        val tracker = (activity.getApplication() as TimerApplication?)?.getTracker()
        tracker?.send(HitBuilders.EventBuilder()
                .setCategory("please_review")
                ?.setAction("show")
                ?.build())

        val preferences = PreferenceManager.getDefaultSharedPreferences(activity)
        val condition = PleaseReviewCondition.create(preferences)

        return AlertDialog.Builder(activity)
                .setTitle(activity.getString(R.string.app_name))
                .setMessage("使い心地はいかがですか？")
                .setPositiveButton("良い", { dialog, which ->
                    //Google Playに遷移
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setData(Uri.parse("market://details?id=${activity.getPackageName()}"))
                    activity.startActivity(intent)

                    //Toast表示
                    Toast.makeText(activity, "ありがとうございます！是非ともGoogle Playでの評価もお願いします", Toast.LENGTH_LONG).show()

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
                .setNeutralButton("まだわからない", { dialog, which ->
                    //レビューお願いをリセット
                    condition?.resetCount()

                    //Analytics
                    tracker?.send(HitBuilders.EventBuilder()
                            .setCategory("please_review")
                            ?.setAction("tap")
                            ?.setLabel("not_now")
                            ?.build())

                    dialog.dismiss()
                })
                .setNegativeButton("良くない", { dialog, which ->
                    //要望画面へ遷移
                    activity.startActivity(Intent(activity, javaClass<RequestActivity>()))

                    //Toast表示
                    Toast.makeText(activity, "良くない点を教えて下さい。最善を尽くします！", Toast.LENGTH_LONG).show()

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
