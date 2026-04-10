package com.engineerfred.reclaim.feature.addiction.presentation.detail

sealed interface AddictionDetailEvent {
    data object NavigateBack : AddictionDetailEvent
    data class ShowToast(val message: String) : AddictionDetailEvent
}