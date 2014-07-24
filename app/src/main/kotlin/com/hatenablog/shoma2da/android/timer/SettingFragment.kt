package com.hatenablog.shoma2da.android.timer

import android.preference.PreferenceFragment
import android.os.Bundle

/**
 * Created by shoma2da on 2014/07/24.
 */
class SettingFragment : PreferenceFragment() {

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preference)

        //バージョン情報を設定
        val activity = getActivity()
        val packageManager = activity?.getPackageManager()
        val versionName = packageManager?.getPackageInfo(activity?.getPackageName(), 0)?.versionName;
        findPreference("version")?.setSummary(versionName)
    }

}
