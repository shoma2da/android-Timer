package com.hatenablog.shoma2da.android.timer

import android.preference.PreferenceFragment
import android.os.Bundle
import android.content.Intent
import android.net.Uri
import net.app_c.cloud.sdk.AppCCloud

/**
 * Created by shoma2da on 2014/07/24.
 */
class SettingFragment : PreferenceFragment() {

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preference)

        val activity = getActivity()
        val appCloud = AppCCloud(activity).start()

        //バージョン情報を設定
        val packageManager = activity?.getPackageManager()
        val versionName = packageManager?.getPackageInfo(activity?.getPackageName(), 0)?.versionName;
        findPreference("version")?.setSummary(versionName)

        //通知バーランチャーの動作設定
        if (activity == null) {
            return@onCreate
        }
        findPreference("notification_launcher")?.setOnPreferenceChangeListener { (preference, checkedValue) ->
            when(checkedValue) {
                true -> activity.startService(Intent(activity, javaClass<NotificationLauncherService>()))
                false -> activity.stopService(Intent(activity, javaClass<NotificationLauncherService>()))
                else -> {} //nothing
            }
            true
        }

        //要望メニューの動作設定
        findPreference("request")?.setOnPreferenceClickListener { preference ->
            activity.startActivity(Intent(activity, javaClass<RequestActivity>()))
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
            intent.setData(Uri.parse("market://details?id=${activity.getPackageName()}"))
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
