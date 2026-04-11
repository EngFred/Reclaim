package com.engineerfred.reclaim.feature.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engineerfred.reclaim.core.domain.util.ReclaimResult
import com.engineerfred.reclaim.feature.auth.domain.usecase.LoginUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    // ── State ─────────────────────────────────────────────────────────────────

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // ── Events ────────────────────────────────────────────────────────────────

    private val _events = Channel<LoginEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    // ── User actions ──────────────────────────────────────────────────────────

    fun onEmailChanged(value: String) {
        _uiState.update { it.copy(email = value, emailError = null, generalError = null) }
    }

    fun onPasswordChanged(value: String) {
        _uiState.update { it.copy(password = value, passwordError = null, generalError = null) }
    }

    fun onTogglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun onLoginClicked() {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, generalError = null) }

            when (val result = loginUseCase(state.email, state.password)) {
                is ReclaimResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _events.send(LoginEvent.NavigateToDashboard)
                }
                is ReclaimResult.Failure -> {
                    _uiState.update { it.copy(isLoading = false) }
                    handleLoginError(result.exception)
                }
            }
        }
    }

    fun onRegisterClicked() {
        viewModelScope.launch { _events.send(LoginEvent.NavigateToRegister) }
    }

//    fun onForgotPasswordClicked() {
//        viewModelScope.launch { _events.send(LoginEvent.NavigateToForgotPassword) }
//    }

    // ── Private ───────────────────────────────────────────────────────────────

    /**
     * Maps raw Firebase / use-case exceptions to friendly, non-technical
     * messages routed to the appropriate UI field.
     *
     * Firebase error codes arrive embedded in the exception message.
     * We match on known fragments and fall back to a generic message.
     */
    private suspend fun handleLoginError(exception: Throwable) {
        val raw = exception.message?.lowercase() ?: ""

        when {
            // ── Field-level errors from LoginUseCase (already friendly) ──────
            raw.contains("please enter your email") ||
                    raw.contains("valid email") ->
                _uiState.update { it.copy(emailError = exception.message) }

            raw.contains("please enter your password") ->
                _uiState.update { it.copy(passwordError = exception.message) }

            // ── Firebase: no account with this email ─────────────────────────
            raw.contains("no user record") ||
                    raw.contains("user-not-found") ||
                    raw.contains("there is no user") ->
                _uiState.update {
                    it.copy(emailError = "No account found with this email address.")
                }

            // ── Firebase: wrong password ──────────────────────────────────────
            raw.contains("password is invalid") ||
                    raw.contains("wrong-password") ||
                    raw.contains("invalid credential") ->
                _uiState.update {
                    it.copy(passwordError = "Incorrect password. Please try again.")
                }

            // ── Firebase: too many attempts ───────────────────────────────────
            raw.contains("too-many-requests") ||
                    raw.contains("too many") ->
                _uiState.update {
                    it.copy(generalError = "Too many failed attempts. Please wait a moment and try again.")
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