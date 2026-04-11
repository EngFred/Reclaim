package com.engineerfred.reclaim.feature.settings.presentation

import com.engineerfred.reclaim.core.domain.model.NotificationPreferences

data class SettingsUiState(
    val preferences: NotificationPreferences? = null,
    val isLoading: Boolean = true,
    val isDeletingAccount: Boolean = false,
    val isDarkTheme: Boolean = false,
    val error: String? = null
)