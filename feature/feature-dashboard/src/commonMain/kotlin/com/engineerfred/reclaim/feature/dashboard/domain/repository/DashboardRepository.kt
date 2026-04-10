package com.engineerfred.reclaim.feature.dashboard.domain.repository

import kotlinx.coroutines.flow.Flow

interface DashboardRepository {
    /**
     * Observes the user's display name for the dashboard greeting.
     * Extracts the name from the email, or returns a fallback for anonymous users.
     */
    fun observeGreetingName(): Flow<String>
}