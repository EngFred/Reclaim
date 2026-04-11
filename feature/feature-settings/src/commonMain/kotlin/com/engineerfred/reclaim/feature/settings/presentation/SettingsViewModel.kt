package com.engineerfred.reclaim.feature.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engineerfred.reclaim.core.domain.model.NotificationPreferences
import com.engineerfred.reclaim.core.domain.repository.AuthRepository
import com.engineerfred.reclaim.core.domain.repository.NotificationPreferencesRepository
import com.engineerfred.reclaim.core.domain.repository.ThemeRepository
import com.engineerfred.reclaim.core.domain.util.ReclaimResult
import com.engineerfred.reclaim.feature.settings.domain.usecase.DeleteAccountUseCase
import com.engineerfred.reclaim.feature.settings.domain.usecase.UpdateNotificationPrefsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val authRepository: AuthRepository,
    private val prefsRepository: NotificationPreferencesRepository,
    private val updateNotificationPrefsUseCase: UpdateNotificationPrefsUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val themeRepository: ThemeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SettingsUiState(isDarkTheme = themeRepository.isDarkTheme())
    )
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val _events = Channel<SettingsEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        observePreferences()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observePreferences() {
        authRepository.observeCurrentUser()
            .flatMapLatest { user ->
                if (user == null) flowOf(null)
                else prefsRepository.observePreferences(user.id)
            }
            .onEach { prefs ->
                _uiState.update { it.copy(preferences = prefs, isLoading = false) }
            }.launchIn(viewModelScope)
    }

    fun onToggleDarkTheme(enabled: Boolean) {
        themeRepository.setDarkTheme(enabled)
        _uiState.update { it.copy(isDarkTheme = enabled) }
        _events.trySend(SettingsEvent.ThemeChanged(enabled))
    }

    fun onUpdatePrefs(updated: NotificationPreferences) {
        viewModelScope.launch {
            val result = updateNotificationPrefsUseCase(updated)
            if (result is ReclaimResult.Failure) {
                _events.send(SettingsEvent.ShowToast(result.exception.message ?: "Update failed"))
            }
        }
    }

    fun onDeleteAccountClicked() {
        viewModelScope.launch {
            _uiState.update { it.copy(isDeletingAccount = true) }
            val result = deleteAccountUseCase()
            _uiState.update { it.copy(isDeletingAccount = false) }
            when (result) {
                is ReclaimResult.Success -> _events.send(SettingsEvent.NavigateToLogin)
                is ReclaimResult.Failure -> _events.send(SettingsEvent.ShowToast("Could not delete account. Try again later."))
            }
        }
    }

    fun onLogoutClicked() {
        viewModelScope.launch {
            authRepository.logout()
            _events.send(SettingsEvent.NavigateToLogin)
        }
    }
}