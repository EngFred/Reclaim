package com.engineerfred.reclaim.feature.dashboard.presentation.pick

import com.engineerfred.reclaim.core.domain.model.Addiction

data class PickAddictionUiState(
    val addictions: List<Addiction> = emptyList(),
    val isLoading: Boolean = true
)