package com.engineerfred.reclaim.feature.checkin.presentation

sealed interface CheckInEvent {
    data object NavigateBack : CheckInEvent
    data class ShowToast(val message: String) : CheckInEvent
}