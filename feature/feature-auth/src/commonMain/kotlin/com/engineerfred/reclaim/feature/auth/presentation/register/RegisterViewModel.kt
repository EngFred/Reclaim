package com.engineerfred.reclaim.feature.auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engineerfred.reclaim.core.domain.util.ReclaimResult
import com.engineerfred.reclaim.feature.auth.domain.usecase.RegisterUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _events = Channel<RegisterEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    // ── User actions ──────────────────────────────────────────────────────────

    fun onEmailChanged(value: String) {
        _uiState.update { it.copy(email = value, emailError = null, generalError = null) }
    }

    fun onPasswordChanged(value: String) {
        _uiState.update { it.copy(password = value, passwordError = null, generalError = null) }
    }

    fun onConfirmPasswordChanged(value: String) {
        _uiState.update { it.copy(confirmPassword = value, confirmPasswordError = null) }
    }

    fun onTogglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun onToggleConfirmPasswordVisibility() {
        _uiState.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
    }

    fun onRegisterClicked() {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, generalError = null) }

            when (val result = registerUseCase(state.email, state.password, state.confirmPassword)) {
                is ReclaimResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _events.send(RegisterEvent.NavigateToOnboarding)
                }
                is ReclaimResult.Failure -> {
                    _uiState.update { it.copy(isLoading = false) }
                    handleRegisterError(result.exception)
                }
            }
        }
    }

    fun onBackToLoginClicked() {
        viewModelScope.launch { _events.send(RegisterEvent.NavigateToLogin) }
    }

    // ── Private ───────────────────────────────────────────────────────────────

    private suspend fun handleRegisterError(exception: Throwable) {
        val raw = exception.message?.lowercase() ?: ""

        when {
            // ── Field-level errors from RegisterUseCase (already friendly) ───
            raw.contains("please enter your email") ||
                    raw.contains("valid email") ->
                _uiState.update { it.copy(emailError = exception.message) }

            raw.contains("please choose a password") ||
                    raw.contains("at least 6") ->
                _uiState.update { it.copy(passwordError = exception.message) }

            raw.contains("don't match") ->
                _uiState.update { it.copy(confirmPasswordError = exception.message) }

            // ── Firebase: email already in use ────────────────────────────────
            raw.contains("email-already-in-use") ||
                    raw.contains("already in use") ||
                    raw.contains("already registered") ->
                _uiState.update {
                    it.copy(emailError = "An account already exists with this email. Try signing in instead.")
                }

            // ── Firebase: invalid email format ────────────────────────────────
            raw.contains("invalid-email") ||
                    raw.contains("badly formatted") ->
                _uiState.update {
                    it.copy(emailError = "That doesn't look like a valid email address.")
                }

            // ── Firebase: weak password ───────────────────────────────────────
            raw.contains("weak-password") ||
                    raw.contains("password should be") ->
                _uiState.update {
                    it.copy(passwordError = "Password must be at least 6 characters long.")
                }

            // ── Firebase: network error ───────────────────────────────────────
            raw.contains("network") ||
                    raw.contains("unable to resolve") ||
                    raw.contains("timeout") ->
                _uiState.update {
                    it.copy(generalError = "No internet connection. Please check your network and try again.")
                }

            // ── Catch-all ─────────────────────────────────────────────────────
            else ->
                _uiState.update {
                    it.copy(generalError = "Something went wrong. Please try again.")
                }
        }
    }
}