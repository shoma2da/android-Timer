package com.hatenablog.shoma2da.android.timer.v1_2.entrypoint.presentation.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v14.preference.PreferenceFragment
import android.support.v7.preference.ListPreference
import co.meyasuba.android.sdk.Meyasubaco
import com.hatenablog.shoma2da.android.timer.R
import com.hatenablog.shoma2da.android.timer.v1_2.domain.notificationlauncher.NotificationLauncherService

class SettingFragment : PreferenceFragment() {

    override fun onCreatePreferences(p0: Bundle?, p1: String?) {
        addPreferencesFromResource(R.xml.preference)

        val activity = activity

        //バージョン情報を設定
        val packageManager = activity?.packageManager
        val versionName = packageManager?.getPackageInfo(activity?.packageName, 0)?.versionName;
        findPreference("version")?.summary = versionName

        //通知バーランチャーの動作設定
        if (activity == null) {
            return@onCreatePreferences
        }
        findPreference("notification_launcher")?.setOnPreferenceChangeListener { preference, checkedValue ->
            when (checkedValue) {
                true -> activity.startService(Intent(activity, NotificationLauncherService::class.java))
                false -> activity.stopService(Intent(activity, NotificationLauncherService::class.java))
                else -> {
                } //nothing
            }
            true
        }

        //お知らせ方法の設定
        val noticationMethods = findPreference("notification_method") as ListPreference
        noticationMethods.summary = noticationMethods.entry;
        noticationMethods.setOnPreferenceChangeListener({preference, newValue ->
            val index = noticationMethods.findIndexOfValue(newValue.toString())
            val entries = noticationMethods.entries
            if (entries != null) {
                val entry = entries[index]
                noticationMethods.summary = entry
            }
            true
        })

        //要望メニューの動作設定
        findPreference("request")?.setOnPreferenceClickListener { preference ->
            Meyasubaco.showCommentActivity(activity);
            true
        }

        //よくある質問メニューの動作設定
        findPreference("question")?.setOnPreferenceClickListener { preference ->
            Meyasubaco.showHelpListActivity(activity)
            true
        }

        //Google Playの動作設定
        findPreference("google_play")?.setOnPreferenceClickListener { preference ->
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse("market://details?id=${activity.packageName}"))
            activity.startActivity(intent)
            true
        }
    }

}
