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

    // ── Private ───────────────────────────────────────────────────────────────

    private suspend fun handleLoginError(exception: Throwable) {
        val message = exception.message ?: "Something went wrong. Please try again."
        // Validation errors from LoginUseCase are routed to the relevant field.
        // Firebase errors (wrong password, user not found, etc.) go to generalError.
        when {
            message.contains("email", ignoreCase = true) ->
                _uiState.update { it.copy(emailError = message) }
            message.contains("password", ignoreCase = true) ->
                _uiState.update { it.copy(passwordError = message) }
            else ->
                _uiState.update { it.copy(generalError = message) }
        }
    }
}