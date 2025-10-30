package com.xml.tvlauncher

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xml.tvlauncher.ui.LauncherViewModel
import com.xml.tvlauncher.ui.screen.LauncherScreen
import com.xml.tvlauncher.ui.theme.TVLauncherTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TVLauncherTheme {
                val viewModel: LauncherViewModel = viewModel()

                LauncherScreen(
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    override fun onBackPressed() {
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                Log.d("xml22", "onKeyDown: Back button blocked")
                true
            }
            KeyEvent.KEYCODE_HOME -> {
                false
            }
            else -> super.onKeyDown(keyCode, event)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("xml22", "Launcher resumed")
    }

}