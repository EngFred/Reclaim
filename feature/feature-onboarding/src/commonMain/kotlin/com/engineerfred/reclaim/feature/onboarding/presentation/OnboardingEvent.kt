package com.engineerfred.reclaim.feature.onboarding.presentation

sealed interface OnboardingEvent {
    /** Navigates to the Dashboard and clears the back stack. */
    data object CompleteAndNavigateToDashboard : OnboardingEvent
    data class ShowToast(val message: String) : OnboardingEvent
}