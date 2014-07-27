package com.hatenablog.shoma2da.android.timer

import android.preference.PreferenceFragment
import android.os.Bundle
import android.preference.CheckBoxPreference
import android.content.Intent

/**
 * Created by shoma2da on 2014/07/24.
 */
class SettingFragment : PreferenceFragment() {

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preference)

        val activity = getActivity()

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
    }

}
