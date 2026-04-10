package com.engineerfred.reclaim.feature.auth.presentation.model

sealed class AuthEvent {
    data object NavigateToOnboarding : AuthEvent()
    data object NavigateToDashboard : AuthEvent()
    data object NavigateToLogin : AuthEvent()
    data class ShowToast(val message: String) : AuthEvent()
}