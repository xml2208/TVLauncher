package com.xml.tvlauncher.repository

import android.content.Context
import android.content.Intent
import com.xml.tvlauncher.model.AppInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepository(private val context: Context) {

    suspend fun getInstalledApps(): List<AppInfo> = withContext(Dispatchers.IO) {
        val packageManager = context.packageManager
        val apps = mutableListOf<AppInfo>()

        val mainIntent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        val resolveInfoList = packageManager.queryIntentActivities(mainIntent, 0)

        resolveInfoList.forEach { resolveInfo ->
            val activityInfo = resolveInfo.activityInfo
            val packageName = activityInfo.packageName

            if (packageName == context.packageName) return@forEach

            try {
                val appName = resolveInfo.loadLabel(packageManager).toString()
                val icon = resolveInfo.loadIcon(packageManager)
                val launchIntent = packageManager.getLaunchIntentForPackage(packageName)

                apps.add(
                    AppInfo(
                        packageName = packageName,
                        appName = appName,
                        icon = icon,
                        launchIntent = launchIntent
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        apps.sortedBy { it.appName.lowercase() }
    }

    fun launchApp(appInfo: AppInfo): Boolean {
        return try {
            appInfo.launchIntent?.let { intent ->
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                true
            } ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}