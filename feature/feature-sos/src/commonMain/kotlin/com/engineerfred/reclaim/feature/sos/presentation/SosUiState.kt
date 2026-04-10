package com.engineerfred.reclaim.feature.sos.presentation

data class SosUiState(
    val streakDays: Int = 0,
    val whyIQuitNote: String? = null,
    val isTimerActive: Boolean = false,
    val isLoading: Boolean = true
)