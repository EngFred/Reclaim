package com.engineerfred.reclaim.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engineerfred.reclaim.core.domain.util.ReclaimResult
import com.engineerfred.reclaim.feature.auth.domain.usecase.GetCurrentUserUseCase
import com.engineerfred.reclaim.feature.auth.domain.usecase.LoginUseCase
import com.engineerfred.reclaim.feature.auth.domain.usecase.LogoutUseCase
import com.engineerfred.reclaim.feature.auth.domain.usecase.RegisterUseCase
import com.engineerfred.reclaim.feature.auth.presentation.model.AuthEvent
import com.engineerfred.reclaim.feature.auth.presentation.model.AuthUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<AuthEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            getCurrentUserUseCase().collect { user ->
                if (user != null) {
                    _uiState.value = AuthUiState.Success(user)
                    _events.send(AuthEvent.NavigateToDashboard)
                }
            }
        }
    }

    fun login(email: String, password: String) {
        _uiState.value = AuthUiState.Loading
        viewModelScope.launch {
            when (val result = loginUseCase(email, password)) {
                is ReclaimResult.Success -> {
                    _uiState.value = AuthUiState.Success(result.data)
                    _events.send(AuthEvent.NavigateToDashboard)
                }
                is ReclaimResult.Failure -> {
                    _uiState.value = AuthUiState.Error(result.exception.message ?: "Login failed")
                    _events.send(AuthEvent.ShowToast("Invalid credentials"))
                }
            }
        }
    }

    fun register(email: String, password: String) {
        _uiState.value = AuthUiState.Loading
        viewModelScope.launch {
            when (val result = registerUseCase(email, password)) {
                is ReclaimResult.Success -> {
                    _uiState.value = AuthUiState.Success(result.data)
                    _events.send(AuthEvent.NavigateToOnboarding)
                }
                is ReclaimResult.Failure -> {
                    _uiState.value = AuthUiState.Error(result.exception.message ?: "Registration failed")
                    _events.send(AuthEvent.ShowToast("Registration failed"))
                }
            }
        }
    }

    fun loginAnonymously() {
        _uiState.value = AuthUiState.Loading
        viewModelScope.launch {
            // TODO: Later we can add a dedicated LoginAnonymouslyUseCase
            _events.send(AuthEvent.NavigateToOnboarding)
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            _events.send(AuthEvent.NavigateToLogin)
        }
    }
}