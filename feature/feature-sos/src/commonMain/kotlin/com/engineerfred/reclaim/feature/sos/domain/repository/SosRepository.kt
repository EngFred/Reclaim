package com.engineerfred.reclaim.feature.sos.domain.repository

import com.engineerfred.reclaim.core.domain.util.ReclaimResult
import kotlinx.coroutines.flow.Flow

interface SosRepository {
    /**
     * Retrieves the "Why I Quit" note for a specific addiction.
     */
    fun observeWhyIQuitNote(addictionId: String): Flow<String?>

    /**
     * Retrieves the current clean streak in days to remind the user what they are protecting.
     */
    fun observeCurrentStreak(addictionId: String): Flow<Int>

    /**
     * Logs the fact that an SOS was triggered.
     * In v1, this prepares the ground for Phase 2 AI-trigger analysis.
     */
    suspend fun logSosTrigger(addictionId: String): ReclaimResult<Unit>
}