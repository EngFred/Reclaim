package com.engineerfred.reclaim.feature.dashboard.presentation.pick

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engineerfred.reclaim.feature.dashboard.domain.usecase.GetAllActiveAddictionsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class PickAddictionViewModel(
    private val getAllActiveAddictionsUseCase: GetAllActiveAddictionsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PickAddictionUiState())
    val uiState: StateFlow<PickAddictionUiState> = _uiState.asStateFlow()

    init {
        getAllActiveAddictionsUseCase()
            .onEach { addictions ->
                _uiState.update {
                    it.copy(addictions = addictions, isLoading = false)
                }
            }
            .launchIn(viewModelScope)
    }
}