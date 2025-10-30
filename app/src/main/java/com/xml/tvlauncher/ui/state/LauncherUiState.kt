package com.xml.tvlauncher.ui.state

import com.xml.tvlauncher.model.AppInfo

data class LauncherUiState(
    val apps: List<AppInfo> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedIndex: Int = 0
)