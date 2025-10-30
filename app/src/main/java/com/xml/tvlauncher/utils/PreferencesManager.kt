package com.xml.tvlauncher.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class PreferencesManager(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    companion object {
        private const val PREFS_NAME = "tv_launcher_prefs"
        private const val KEY_LAST_SELECTED_PACKAGE = "last_selected_package"
        private const val KEY_LAUNCH_COUNT = "launch_count"
        private const val KEY_FIRST_LAUNCH = "first_launch"
    }

    fun saveLastSelectedApp(packageName: String) {
        prefs.edit { putString(KEY_LAST_SELECTED_PACKAGE, packageName) }
    }

    fun getLastSelectedApp(): String? {
        return prefs.getString(KEY_LAST_SELECTED_PACKAGE, null)
    }

    fun incrementLaunchCount() {
        val currentCount = prefs.getInt(KEY_LAUNCH_COUNT, 0)
        prefs.edit { putInt(KEY_LAUNCH_COUNT, currentCount + 1) }
    }

    fun getLaunchCount(): Int {
        return prefs.getInt(KEY_LAUNCH_COUNT, 0)
    }

    fun isFirstLaunch(): Boolean {
        val isFirst = prefs.getBoolean(KEY_FIRST_LAUNCH, true)
        if (isFirst) {
            prefs.edit { putBoolean(KEY_FIRST_LAUNCH, false) }
        }
        return isFirst
    }

    fun clearAll() {
        prefs.edit { clear() }
    }
}