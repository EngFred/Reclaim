package com.engineerfred.reclaim.feature.addiction.presentation.detail

import com.engineerfred.reclaim.core.domain.model.Addiction

data class AddictionDetailUiState(
    val addiction: Addiction? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)