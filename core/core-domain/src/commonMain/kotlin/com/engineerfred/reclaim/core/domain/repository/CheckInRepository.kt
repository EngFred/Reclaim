package com.engineerfred.reclaim.core.domain.repository

import com.engineerfred.reclaim.core.domain.model.CheckIn
import com.engineerfred.reclaim.core.domain.util.ReclaimResult
import kotlinx.coroutines.flow.Flow

interface CheckInRepository {

    /**
     * Observes all check-ins for a specific addiction, ordered by date descending.
     */
    fun observeCheckIns(addictionId: String): Flow<List<CheckIn>>

    /**
     * Observes all check-ins for a user across all addictions.
     */
    fun observeAllCheckIns(userId: String): Flow<List<CheckIn>>

    /**
     * Returns the check-in for today for a specific addiction, or null if not yet logged.
     */
    suspend fun getTodayCheckIn(addictionId: String, todayMillis: Long): CheckIn?

    /**
     * Returns true if the user has already checked in today for this addiction.
     */
    suspend fun hasCheckedInToday(addictionId: String, todayMillis: Long): Boolean

    /**
     * Logs a daily check-in. Writes to local DB immediately.
     * Queues for Firebase sync in background.
     */
    suspend fun logCheckIn(checkIn: CheckIn): ReclaimResult<Unit>

    /**
     * Returns all unsynced check-ins — used by SyncManager.
     */
    suspend fun getUnsyncedCheckIns(): List<CheckIn>

    /**
     * Marks a check-in as synced after successful Firebase write.
     */
    suspend fun markAsSynced(checkInId: String): ReclaimResult<Unit>

    /**
     * Deletes all check-ins for a user — called on account deletion.
     */
    suspend fun deleteAllForUser(userId: String): ReclaimResult<Unit>
}