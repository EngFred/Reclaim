package com.engineerfred.reclaim.feature.checkin.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engineerfred.reclaim.core.domain.model.CheckInStatus
import com.engineerfred.reclaim.core.domain.repository.AddictionRepository
import com.engineerfred.reclaim.core.domain.util.ReclaimResult
import com.engineerfred.reclaim.feature.checkin.domain.usecase.HasCheckedInTodayUseCase
import com.engineerfred.reclaim.feature.checkin.domain.usecase.LogDailyCheckInUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CheckInViewModel(
    private val addictionRepository: AddictionRepository,
    private val hasCheckedInTodayUseCase: HasCheckedInTodayUseCase,
    private val logDailyCheckInUseCase: LogDailyCheckInUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckInUiState())
    val uiState: StateFlow<CheckInUiState> = _uiState.asStateFlow()

    private val _events = Channel<CheckInEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var currentAddictionId: String? = null

    fun loadAddiction(addictionId: String) {
        if (currentAddictionId == addictionId) return
        currentAddictionId = addictionId

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val addiction = addictionRepository.observeAddiction(addictionId).firstOrNull()
            val alreadyLogged = hasCheckedInTodayUseCase(addictionId)

            _uiState.update {
                it.copy(
                    addiction = addiction,
                    hasCheckedInToday = alreadyLogged,
                    isLoading = false,
                    error = if (addiction == null) "Addiction not found." else null
                )
            }
        }
    }

    fun onStatusSelected(status: CheckInStatus) {
        if (_uiState.value.hasCheckedInToday) return
        _uiState.update { it.copy(selectedStatus = status) }
    }

    fun onNoteChanged(note: String) {
        if (_uiState.value.hasCheckedInToday) return
        _uiState.update { it.copy(triggerNote = note) }
    }

    fun onSubmitClicked() {
        val state = _uiState.value
        val addictionId = currentAddictionId ?: return
        val status = state.selectedStatus ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = logDailyCheckInUseCase(addictionId, status, state.triggerNote)) {
                is ReclaimResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _events.send(CheckInEvent.NavigateBack)
                }
                is ReclaimResult.Failure -> {
                    _uiState.update {
                        it.copy(isLoading = false, error = result.exception.message)
                    }
                    _events.send(CheckInEvent.ShowToast("Failed to log check-in."))
                }
            }
        }
    }

    fun onBackClicked() {
        viewModelScope.launch { _events.send(CheckInEvent.NavigateBack) }
    }
}