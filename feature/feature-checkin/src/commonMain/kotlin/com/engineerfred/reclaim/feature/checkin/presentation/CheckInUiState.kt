package com.engineerfred.reclaim.feature.checkin.presentation

import com.engineerfred.reclaim.core.domain.model.Addiction
import com.engineerfred.reclaim.core.domain.model.CheckInStatus

data class CheckInUiState(
    val addiction: Addiction? = null,
    val selectedStatus: CheckInStatus? = null,
    val triggerNote: String = "",
    val hasCheckedInToday: Boolean = false,
    val isLoading: Boolean = true,
    val error: String? = null
)