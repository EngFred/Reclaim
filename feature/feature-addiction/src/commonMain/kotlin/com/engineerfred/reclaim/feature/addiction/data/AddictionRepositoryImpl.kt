package com.engineerfred.reclaim.feature.addiction.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.engineerfred.reclaim.core.data.local.DatabaseProvider
import com.engineerfred.reclaim.core.data.mapper.toDomain
import com.engineerfred.reclaim.core.data.mapper.toEntity
import com.engineerfred.reclaim.core.domain.model.Addiction
import com.engineerfred.reclaim.core.domain.repository.AddictionRepository
import com.engineerfred.reclaim.core.domain.util.ReclaimResult
import com.engineerfred.reclaim.core.domain.util.reclaimRunCatching
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * Offline-first implementation.
 * * ALL reads observe SQLDelight using Flow.
 * ALL writes immediately update SQLDelight and set isSynced = 0.
 * Background SyncManager (from Phase 3) will handle the Firebase push.
 */
class AddictionRepositoryImpl(
    private val databaseProvider: DatabaseProvider
) : AddictionRepository {

    private val queries = databaseProvider.database.addictionEntityQueries

    // ── Reads ─────────────────────────────────────────────────────────────────

    override fun observeActiveAddictions(userId: String): Flow<List<Addiction>> {
        return queries.selectActiveAddictions(userId)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list -> list.map { it.toDomain() } }
    }

    override fun observeAllAddictions(userId: String): Flow<List<Addiction>> {
        return queries.selectAllAddictions(userId)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list -> list.map { it.toDomain() } }
    }

    override fun observeAddiction(addictionId: String): Flow<Addiction?> {
        return queries.selectAddictionById(addictionId)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
            .map { it?.toDomain() }
    }

    // ── Writes ────────────────────────────────────────────────────────────────

    override suspend fun addAddiction(addiction: Addiction): ReclaimResult<Unit> =
        reclaimRunCatching {
            withContext(Dispatchers.IO) {
                val entity = addiction.toEntity(isSynced = false)
                queries.insertOrReplaceAddiction(
                    id = entity.id,
                    userId = entity.userId,
                    category = entity.category,
                    type = entity.type,
                    displayName = entity.displayName,
                    startDate = entity.startDate,
                    isActive = entity.isActive,
                    whyIQuitNote = entity.whyIQuitNote,
                    isSynced = entity.isSynced
                )
            }
        }

    override suspend fun pauseAddiction(addictionId: String): ReclaimResult<Unit> =
        reclaimRunCatching {
            withContext(Dispatchers.IO) {
                queries.updateAddictionIsActive(isActive = 0L, id = addictionId)
                queries.updateIsSynced(isSynced = 0L, id = addictionId) // Queue for sync
            }
        }

    override suspend fun completeAddiction(addictionId: String): ReclaimResult<Unit> =
        reclaimRunCatching {
            withContext(Dispatchers.IO) {
                queries.updateAddictionIsActive(isActive = 0L, id = addictionId)
                queries.updateIsSynced(isSynced = 0L, id = addictionId)
            }
        }

    override suspend fun updateWhyIQuitNote(addictionId: String, note: String): ReclaimResult<Unit> =
        reclaimRunCatching {
            withContext(Dispatchers.IO) {
                queries.updateWhyIQuitNote(whyIQuitNote = note, id = addictionId)
                queries.updateIsSynced(isSynced = 0L, id = addictionId)
            }
        }

    override suspend fun deleteAllForUser(userId: String): ReclaimResult<Unit> =
        reclaimRunCatching {
            withContext(Dispatchers.IO) {
                queries.deleteAllAddictionsForUser(userId)
            }
        }
}