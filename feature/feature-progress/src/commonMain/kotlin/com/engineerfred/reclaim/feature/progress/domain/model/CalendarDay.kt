package com.engineerfred.reclaim.feature.progress.domain.model

import com.engineerfred.reclaim.core.domain.model.CheckInStatus

/**
 * Represents a single cell in the progress calendar grid.
 */
data class CalendarDay(
    val dateMillis: Long,
    val status: CheckInStatus?, // null means no check-in logged for this day
    val isToday: Boolean
)