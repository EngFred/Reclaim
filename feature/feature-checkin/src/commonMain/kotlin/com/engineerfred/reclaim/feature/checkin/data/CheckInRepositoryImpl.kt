package com.engineerfred.reclaim.feature.checkin.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.engineerfred.reclaim.core.data.local.DatabaseProvider
import com.engineerfred.reclaim.core.data.mapper.toDomain
import com.engineerfred.reclaim.core.data.mapper.toEntity
import com.engineerfred.reclaim.core.domain.model.CheckIn
import com.engineerfred.reclaim.core.domain.repository.CheckInRepository
import com.engineerfred.reclaim.core.domain.util.ReclaimResult
import com.engineerfred.reclaim.core.domain.util.reclaimRunCatching
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * Offline-first implementation of CheckInRepository.
 * ALL writes go straight to SQLDelight and are queued for Firebase via SyncManager.
 */
class CheckInRepositoryImpl(
    private val databaseProvider: DatabaseProvider
) : CheckInRepository {

    private val queries = databaseProvider.database.checkInEntityQueries

    override fun observeCheckIns(addictionId: String): Flow<List<CheckIn>> {
        return queries.selectCheckInsForAddiction(addictionId)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list -> list.map { it.toDomain() } }
    }

    override fun observeAllCheckIns(userId: String): Flow<List<CheckIn>> {
        return queries.selectAllCheckInsForUser(userId)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getTodayCheckIn(addictionId: String, todayMillis: Long): CheckIn? {
        return withContext(Dispatchers.IO) {
            queries.selectCheckInForDate(addictionId, todayMillis)
                .executeAsOneOrNull()?.toDomain()
        }
    }

    override suspend fun hasCheckedInToday(addictionId: String, todayMillis: Long): Boolean {
        return getTodayCheckIn(addictionId, todayMillis) != null
    }

    override suspend fun logCheckIn(checkIn: CheckIn): ReclaimResult<Unit> =
        reclaimRunCatching {
            withContext(Dispatchers.IO) {
                val entity = checkIn.toEntity()
                queries.insertOrReplaceCheckIn(
                    id = entity.id,
                    addictionId = entity.addictionId,
                    userId = entity.userId,
                    date = entity.date,
                    status = entity.status,
                    triggerNote = entity.triggerNote,
                    isSynced = 0L // Force 0L for the SyncManager to pick it up
                )
            }
        }

    override suspend fun getUnsyncedCheckIns(): List<CheckIn> {
        return withContext(Dispatchers.IO) {
            queries.selectUnsyncedCheckIns().executeAsList().map { it.toDomain() }
        }
    }

    override suspend fun markAsSynced(checkInId: String): ReclaimResult<Unit> =
        reclaimRunCatching {
            withContext(Dispatchers.IO) {
                queries.markCheckInAsSynced(checkInId)
            }
        }

    override suspend fun deleteAllForUser(userId: String): ReclaimResult<Unit> =
        reclaimRunCatching {
            withContext(Dispatchers.IO) {
                queries.deleteAllCheckInsForUser(userId)
            }
        }
}