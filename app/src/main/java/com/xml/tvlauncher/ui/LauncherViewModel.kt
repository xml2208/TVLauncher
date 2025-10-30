package com.xml.tvlauncher.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.xml.tvlauncher.model.AppInfo
import com.xml.tvlauncher.repository.AppRepository
import com.xml.tvlauncher.ui.state.LauncherUiState
import com.xml.tvlauncher.utils.PreferencesManager
import kotlinx.coroutines.launch

class LauncherViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppRepository(application)
    private val prefsManager = PreferencesManager(application)

    var uiState by mutableStateOf(LauncherUiState())
        private set

    init {
        loadApps()
        prefsManager.incrementLaunchCount()
    }

    fun loadApps() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)
            
            try {
                val apps = repository.getInstalledApps()

                val lastSelectedPackage = prefsManager.getLastSelectedApp()
                val selectedIndex = if (lastSelectedPackage != null) {
                    apps.indexOfFirst { it.packageName == lastSelectedPackage }.takeIf { it >= 0 } ?: 0
                } else {
                    0
                }

                uiState = uiState.copy(
                    apps = apps,
                    isLoading = false,
                    selectedIndex = selectedIndex
                )
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    error = "Failed to load apps: ${e.message}"
                )
            }
        }
    }

    fun launchApp(appInfo: AppInfo) {
        val success = repository.launchApp(appInfo)
        if (success) {
            prefsManager.saveLastSelectedApp(appInfo.packageName)
        } else {
            uiState = uiState.copy(error = "Failed to launch ${appInfo.appName}")
        }
    }

    fun updateSelectedIndex(index: Int) {
        if (index in uiState.apps.indices) {
            uiState = uiState.copy(selectedIndex = index)
        }
    }

    fun clearError() {
        uiState = uiState.copy(error = null)
    }

    fun getLaunchCount(): Int = prefsManager.getLaunchCount()

    fun isFirstLaunch(): Boolean = prefsManager.isFirstLaunch()
}
