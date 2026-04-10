package com.engineerfred.reclaim.feature.auth.presentation.register

data class RegisterUiState(
    val email: String                    = "",
    val password: String                 = "",
    val confirmPassword: String          = "",
    val isPasswordVisible: Boolean       = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isLoading: Boolean               = false,
    val emailError: String?              = null,
    val passwordError: String?           = null,
    val confirmPasswordError: String?    = null,
    val generalError: String?            = null
)