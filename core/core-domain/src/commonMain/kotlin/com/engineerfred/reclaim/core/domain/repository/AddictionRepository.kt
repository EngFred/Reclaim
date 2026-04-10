package com.engineerfred.reclaim.core.domain.repository

import com.engineerfred.reclaim.core.domain.model.Addiction
import com.engineerfred.reclaim.core.domain.util.ReclaimResult
import kotlinx.coroutines.flow.Flow

interface AddictionRepository {

    /**
     * Observes all active addictions for the given user.
     * Reads from local DB first — always offline-first.
     */
    fun observeActiveAddictions(userId: String): Flow<List<Addiction>>

    /**
     * Observes a single addiction by its ID.
     */
    fun observeAddiction(addictionId: String): Flow<Addiction?>

    /**
     * Returns all addictions (active and inactive) for the user.
     */
    fun observeAllAddictions(userId: String): Flow<List<Addiction>>

    /**
     * Adds a new addiction journey for the user.
     */
    suspend fun addAddiction(addiction: Addiction): ReclaimResult<Unit>

    /**
     * Pauses an active addiction (sets isActive = false without deleting).
     */
    suspend fun pauseAddiction(addictionId: String): ReclaimResult<Unit>

    /**
     * Marks an addiction as completed.
     */
    suspend fun completeAddiction(addictionId: String): ReclaimResult<Unit>

    /**
     * Updates the "why I quit" note for a specific addiction.
     */
    suspend fun updateWhyIQuitNote(addictionId: String, note: String): ReclaimResult<Unit>

    /**
     * Deletes all addictions for a user — called on account deletion.
     */
    suspend fun deleteAllForUser(userId: String): ReclaimResult<Unit>
}