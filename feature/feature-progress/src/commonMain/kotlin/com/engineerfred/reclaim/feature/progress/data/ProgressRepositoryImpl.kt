package com.engineerfred.reclaim.feature.progress.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.engineerfred.reclaim.core.data.local.DatabaseProvider
import com.engineerfred.reclaim.core.data.mapper.toDomain
import com.engineerfred.reclaim.core.data.mapper.toEntity
import com.engineerfred.reclaim.core.domain.model.Streak
import com.engineerfred.reclaim.core.domain.repository.ProgressRepository
import com.engineerfred.reclaim.core.domain.util.ReclaimResult
import com.engineerfred.reclaim.core.domain.util.reclaimRunCatching
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * SQLDelight implementation of ProgressRepository.
 */
class ProgressRepositoryImpl(
    private val databaseProvider: DatabaseProvider
) : ProgressRepository {

    private val queries = databaseProvider.database.streakEntityQueries

    override fun observeStreak(addictionId: String): Flow<Streak> {
        return queries.selectStreakForAddiction(addictionId)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
            .map { it?.toDomain() ?: emptyStreak(addictionId) }
    }

    override suspend fun getStreak(addictionId: String): Streak {
        return withContext(Dispatchers.IO) {
            queries.selectStreakForAddiction(addictionId)
                .executeAsOneOrNull()?.toDomain() ?: emptyStreak(addictionId)
        }
    }

    override suspend fun saveStreak(streak: Streak): ReclaimResult<Unit> =
        reclaimRunCatching {
            withContext(Dispatchers.IO) {
                val entity = streak.toEntity()
                queries.insertOrReplaceStreak(
                    addictionId = entity.addictionId,
                    current = entity.current,
                    longest = entity.longest,
                    totalDays = entity.totalDays,
                    successRate = entity.successRate
                )
            }
        }

    private fun emptyStreak(addictionId: String) = Streak(
        addictionId = addictionId,
        current = 0,
        longest = 0,
        totalDays = 0,
        successRate = 0.0f
    )
}