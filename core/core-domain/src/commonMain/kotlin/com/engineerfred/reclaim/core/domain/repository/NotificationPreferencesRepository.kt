package com.engineerfred.reclaim.core.domain.repository

import com.engineerfred.reclaim.core.domain.model.NotificationPreferences
import com.engineerfred.reclaim.core.domain.util.ReclaimResult
import kotlinx.coroutines.flow.Flow

interface NotificationPreferencesRepository {

    /**
     * Observes notification preferences for the current user.
     */
    fun observePreferences(userId: String): Flow<NotificationPreferences>

    /**
     * Saves updated notification preferences.
     */
    suspend fun savePreferences(preferences: NotificationPreferences): ReclaimResult<Unit>
}