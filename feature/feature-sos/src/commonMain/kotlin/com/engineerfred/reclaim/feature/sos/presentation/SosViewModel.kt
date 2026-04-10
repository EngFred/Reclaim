package com.engineerfred.reclaim.feature.sos.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engineerfred.reclaim.feature.sos.domain.usecase.GetSosContentUseCase
import com.engineerfred.reclaim.feature.sos.domain.usecase.LogSosTriggerUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SosViewModel(
    private val getSosContentUseCase: GetSosContentUseCase,
    private val logSosTriggerUseCase: LogSosTriggerUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SosUiState())
    val uiState: StateFlow<SosUiState> = _uiState.asStateFlow()

    private val _events = Channel<SosEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var isInitialized = false

    fun initialize(addictionId: String) {
        if (isInitialized) return
        isInitialized = true

        // Log that the user requested emergency help (schedules 15-min follow-up)
        viewModelScope.launch {
            logSosTriggerUseCase(addictionId)
        }

        // Observe the current streak and personal note
        getSosContentUseCase(addictionId).onEach { content ->
            _uiState.update {
                it.copy(
                    streakDays = content.streakDays,
                    whyIQuitNote = content.whyIQuitNote,
                    isLoading = false
                )
            }
        }.launchIn(viewModelScope)
    }

    fun toggleTimer() {
        _uiState.update { it.copy(isTimerActive = !it.isTimerActive) }
    }

    fun onBackClicked() {
        viewModelScope.launch { _events.send(SosEvent.NavigateBack) }
    }
}