package com.xml.tvlauncher

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val action = intent.action
        Log.d(TAG, "Boot action received: $action")

        when (action) {
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_LOCKED_BOOT_COMPLETED,
            "android.intent.action.QUICKBOOT_POWERON" -> {
                Log.d(TAG, "Device boot detected, launching TV Launcher")
                launchHomeScreen(context)
            }
        }
    }

    private fun launchHomeScreen(context: Context) {
        try {
            val homeIntent = Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_HOME)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
            }
            context.startActivity(homeIntent)
            Log.d(TAG, "Home screen launch triggered successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to launch home screen on boot", e)

            try {
                val launchIntent = Intent(context, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                }
                context.startActivity(launchIntent)
                Log.d(TAG, "Direct launcher started as fallback")
            } catch (ex: Exception) {
                Log.e(TAG, "Fallback launch also failed", ex)
            }
        }
    }

    companion object {
        private const val TAG = "TVLauncher_BootReceiver"
    }
}