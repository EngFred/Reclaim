package com.engineerfred.reclaim.feature.auth.presentation.login

/**
 * Complete UI state for LoginScreen.
 *
 * A single data class — not multiple loose StateFlow fields.
 * The Composable renders exactly what this object describes.
 */
data class LoginUiState(
    val email: String           = "",
    val password: String        = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean      = false,
    val emailError: String?     = null,
    val passwordError: String?  = null,
    val generalError: String?   = null
)