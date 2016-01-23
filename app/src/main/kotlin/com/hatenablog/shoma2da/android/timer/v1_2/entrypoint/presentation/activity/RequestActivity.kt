package com.hatenablog.shoma2da.android.timer.v1_2.entrypoint.presentation.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
//import com.parse.ParseObject
import com.google.android.gms.analytics.HitBuilders
import com.hatenablog.shoma2da.android.timer.R
import com.hatenablog.shoma2da.android.timer.v1_2.TimerApplication
import com.hatenablog.shoma2da.android.timer.v1_2.util.extensions.getLogger
import kotlin.text.isEmpty

class RequestActivity : Activity() {

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_request)
        setTitle(R.string.request)

        //Analytics
        val logger = application.getLogger()
        logger.sendScreenLog("RequestActivity")

        //送信ボタン
        findViewById(R.id.sendButton).setOnClickListener { view ->
            val content = (findViewById(R.id.requestText) as EditText?)?.text.toString()

            if (content.isEmpty() == false) {
                //中身あり
                //データ送信
                //                val request = ParseObject("Request");
                //                request.put("content", content);
                //                request.saveInBackground();

                //トーストメッセージ
                val thanks = this@RequestActivity.getString(R.string.request_thanks)
                Toast.makeText(this@RequestActivity, thanks, Toast.LENGTH_LONG).show()

                finish()
            } else {
                //中身なし
                val emptyString = this@RequestActivity.getString(R.string.request_empty)
                Toast.makeText(this@RequestActivity, emptyString, Toast.LENGTH_LONG).show()
            }
        }

        //キャンセルボタン
        findViewById(R.id.cancelButton).setOnClickListener { view ->
            finish()
        }
    }

}
