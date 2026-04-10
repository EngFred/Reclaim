package com.engineerfred.reclaim.feature.addiction.presentation.add

import com.engineerfred.reclaim.core.domain.model.AddictionCategory

data class AddAddictionUiState(
    val selectedCategory: AddictionCategory? = null,
    val whyIQuitNote: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface AddAddictionEvent {
    data object NavigateBack : AddAddictionEvent
    data class ShowToast(val message: String) : AddAddictionEvent
}