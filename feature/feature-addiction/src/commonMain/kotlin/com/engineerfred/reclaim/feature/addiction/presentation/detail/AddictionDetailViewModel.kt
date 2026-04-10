package com.engineerfred.reclaim.feature.addiction.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engineerfred.reclaim.core.domain.util.ReclaimResult
import com.engineerfred.reclaim.feature.addiction.domain.usecase.CompleteAddictionUseCase
import com.engineerfred.reclaim.feature.addiction.domain.usecase.GetAddictionDetailUseCase
import com.engineerfred.reclaim.feature.addiction.domain.usecase.PauseAddictionUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddictionDetailViewModel(
    private val getAddictionDetailUseCase: GetAddictionDetailUseCase,
    private val pauseAddictionUseCase: PauseAddictionUseCase,
    private val completeAddictionUseCase: CompleteAddictionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddictionDetailUiState())
    val uiState: StateFlow<AddictionDetailUiState> = _uiState.asStateFlow()

    private val _events = Channel<AddictionDetailEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var currentAddictionId: String? = null

    fun loadAddiction(addictionId: String) {
        if (currentAddictionId == addictionId) return
        currentAddictionId = addictionId

        // Observe the addiction from the local database.
        // It updates automatically if the underlying data changes.
        getAddictionDetailUseCase(addictionId)
            .onEach { addiction ->
                _uiState.update {
                    it.copy(
                        addiction = addiction,
                        isLoading = false,
                        error = if (addiction == null) "Addiction not found." else null
                    )
                }
            }.launchIn(viewModelScope)
    }

    fun onPauseClicked() {
        val id = currentAddictionId ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = pauseAddictionUseCase(id)) {
                is ReclaimResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _events.send(AddictionDetailEvent.NavigateBack)
                }
                is ReclaimResult.Failure -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _events.send(AddictionDetailEvent.ShowToast("Failed to pause journey."))
                }
            }
        }
    }

    fun onCompleteClicked() {
        val id = currentAddictionId ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = completeAddictionUseCase(id)) {
                is ReclaimResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _events.send(AddictionDetailEvent.NavigateBack)
                }
                is ReclaimResult.Failure -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _events.send(AddictionDetailEvent.ShowToast("Failed to complete journey."))
                }
            }
        }
    }

    fun onBackClicked() {
        viewModelScope.launch { _events.send(AddictionDetailEvent.NavigateBack) }
    }
}