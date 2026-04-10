package com.engineerfred.reclaim.feature.dashboard.presentation

import com.engineerfred.reclaim.core.domain.model.Addiction
import com.engineerfred.reclaim.core.domain.model.Streak

data class DashboardUiState(
    val greeting: String = "Loading...",
    val activeAddictions: List<ActiveAddictionItem> = emptyList(),
    val isLoading: Boolean = true
)

/**
 * Merged domain data for the dashboard card.
 */
data class ActiveAddictionItem(
    val addiction: Addiction,
    val streak: Streak
)