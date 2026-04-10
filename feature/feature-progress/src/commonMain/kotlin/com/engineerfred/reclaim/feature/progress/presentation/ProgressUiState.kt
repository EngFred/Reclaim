package com.engineerfred.reclaim.feature.progress.presentation

import com.engineerfred.reclaim.core.domain.model.Addiction
import com.engineerfred.reclaim.core.domain.model.Milestone
import com.engineerfred.reclaim.core.domain.model.Streak
import com.engineerfred.reclaim.feature.progress.domain.model.CalendarDay

data class ProgressUiState(
    val addiction: Addiction? = null,
    val streak: Streak? = null,
    val calendarDays: List<CalendarDay> = emptyList(),
    val currentMilestone: Milestone? = null,
    val isLoading: Boolean = true
)

sealed interface ProgressEvent {
    data object NavigateBack : ProgressEvent
}