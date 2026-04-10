package com.engineerfred.reclaim.feature.settings.presentation


sealed interface SettingsEvent {
    data object NavigateToNotificationPrefs : SettingsEvent
    data object NavigateToLogin : SettingsEvent
    data class ShowToast(val message: String) : SettingsEvent
}