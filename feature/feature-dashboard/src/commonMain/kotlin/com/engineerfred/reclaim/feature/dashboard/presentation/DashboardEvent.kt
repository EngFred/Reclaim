package com.engineerfred.reclaim.feature.dashboard.presentation

sealed interface DashboardEvent {
    data class NavigateToCheckIn(val addictionId: String) : DashboardEvent
    data class NavigateToAddictionDetail(val addictionId: String) : DashboardEvent
    data object NavigateToAddAddiction : DashboardEvent
}