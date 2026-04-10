package com.engineerfred.reclaim.feature.auth.presentation.register

sealed interface RegisterEvent {
    /** Registration succeeded — navigate to onboarding and clear back stack. */
    data object NavigateToOnboarding : RegisterEvent

    /** Go back to the login screen. */
    data object NavigateToLogin : RegisterEvent

    data class ShowToast(val message: String) : RegisterEvent
}