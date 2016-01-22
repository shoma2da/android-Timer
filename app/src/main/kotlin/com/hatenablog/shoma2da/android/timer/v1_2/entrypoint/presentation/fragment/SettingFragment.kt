package com.hatenablog.shoma2da.android.timer.v1_2.entrypoint.presentation.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.ListPreference
import android.preference.Preference
import android.preference.PreferenceFragment
import com.hatenablog.shoma2da.android.timer.R
import com.hatenablog.shoma2da.android.timer.v1_2.domain.notificationlauncher.NotificationLauncherService
import com.hatenablog.shoma2da.android.timer.v1_2.entrypoint.presentation.activity.RequestActivity
import net.app_c.cloud.sdk.AppCCloud

class SettingFragment : PreferenceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preference)

        val activity = activity
        val appCloud = AppCCloud(activity).start()

        //バージョン情報を設定
        val packageManager = activity?.packageManager
        val versionName = packageManager?.getPackageInfo(activity?.packageName, 0)?.versionName;
        findPreference("version")?.summary = versionName

        //通知バーランチャーの動作設定
        if (activity == null) {
            return@onCreate
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
        noticationMethods.summary = noticationMethods.getEntry();
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
            activity.startActivity(Intent(activity, RequestActivity::class.java))
            true
        }

        //開発者メニューの動作設定
        findPreference("developer")?.setOnPreferenceClickListener { preference ->
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse("https://twitter.com/intent/user?screen_name=shoma2da"))
            activity.startActivity(intent)
            true
        }

        //Google Playの動作設定
        findPreference("google_play")?.setOnPreferenceClickListener { preference ->
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse("market://details?id=${activity.packageName}"))
            activity.startActivity(intent)
            true
        }

        //おすすめアプリの動作設定
        findPreference("recommend_apps")?.setOnPreferenceClickListener { preference ->
            appCloud?.Ad?.callWebActivity();
            true
        }
    }

}
