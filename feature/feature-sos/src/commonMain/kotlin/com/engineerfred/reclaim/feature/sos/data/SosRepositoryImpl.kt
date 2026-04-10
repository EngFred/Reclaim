package com.engineerfred.reclaim.feature.sos.data

import com.engineerfred.reclaim.core.domain.repository.AddictionRepository
import com.engineerfred.reclaim.core.domain.repository.ProgressRepository
import com.engineerfred.reclaim.core.domain.util.ReclaimResult
import com.engineerfred.reclaim.feature.sos.domain.repository.SosRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SosRepositoryImpl(
    private val addictionRepository: AddictionRepository,
    private val progressRepository: ProgressRepository
) : SosRepository {

    override fun observeWhyIQuitNote(addictionId: String): Flow<String?> {
        return addictionRepository.observeAddiction(addictionId).map { it?.whyIQuitNote }
    }

    override fun observeCurrentStreak(addictionId: String): Flow<Int> {
        return progressRepository.observeStreak(addictionId).map { it.current }
    }

    override suspend fun logSosTrigger(addictionId: String): ReclaimResult<Unit> {
        // v1 Implementation: Returns success.
        // In Phase 2, this will write to a local SQLDelight trigger pattern table.
        return ReclaimResult.Success(Unit)
    }
}