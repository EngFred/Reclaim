package com.engineerfred.reclaim.core.domain.repository

import com.engineerfred.reclaim.core.domain.model.Streak
import com.engineerfred.reclaim.core.domain.util.ReclaimResult
import kotlinx.coroutines.flow.Flow

interface ProgressRepository {

    /**
     * Observes the computed streak for a specific addiction.
     * Recalculates whenever check-in history changes.
     */
    fun observeStreak(addictionId: String): Flow<Streak>

    /**
     * Returns the current streak for a specific addiction once.
     */
    suspend fun getStreak(addictionId: String): Streak

    /**
     * Saves or updates the streak record for an addiction.
     */
    suspend fun saveStreak(streak: Streak): ReclaimResult<Unit>
}