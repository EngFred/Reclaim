package com.engineerfred.reclaim.feature.addiction.presentation.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engineerfred.reclaim.core.domain.model.AddictionCategory
import com.engineerfred.reclaim.core.domain.util.ReclaimResult
import com.engineerfred.reclaim.feature.addiction.domain.usecase.AddAddictionUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddAddictionViewModel(
    private val addAddictionUseCase: AddAddictionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddAddictionUiState())
    val uiState: StateFlow<AddAddictionUiState> = _uiState.asStateFlow()

    private val _events = Channel<AddAddictionEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onCategorySelected(category: AddictionCategory) {
        _uiState.update { it.copy(selectedCategory = category, error = null) }
    }

    fun onNoteChanged(note: String) {
        _uiState.update { it.copy(whyIQuitNote = note) }
    }

    fun onSaveClicked() {
        val state = _uiState.value
        val category = state.selectedCategory ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = addAddictionUseCase(category, state.whyIQuitNote)) {
                is ReclaimResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _events.send(AddAddictionEvent.NavigateBack)
                }
                is ReclaimResult.Failure -> {
                    _uiState.update {
                        it.copy(isLoading = false, error = result.exception.message ?: "Failed to save.")
                    }
                    _events.send(AddAddictionEvent.ShowToast("Failed to save addiction."))
                }
            }
        }
    }

    fun onBackClicked() {
        viewModelScope.launch { _events.send(AddAddictionEvent.NavigateBack) }
    }
}