package com.engineerfred.reclaim.feature.auth.presentation.login

/**
 * One-time events emitted from [LoginViewModel] via SharedFlow.
 *
 * These are consumed exactly once — navigation and toasts
 * must never be driven by StateFlow (they would re-fire on recomposition).
 */
sealed interface LoginEvent {
    /** Navigate to the dashboard and clear the back stack. */
    data object NavigateToDashboard : LoginEvent

    /** Navigate to the registration screen. */
    data object NavigateToRegister : LoginEvent

    /** Show a short toast/snackbar with the given message. */
    data class ShowToast(val message: String) : LoginEvent
}