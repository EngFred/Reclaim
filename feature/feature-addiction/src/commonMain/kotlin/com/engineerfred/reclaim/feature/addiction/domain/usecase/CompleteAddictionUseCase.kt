package com.engineerfred.reclaim.feature.addiction.domain.usecase

import com.engineerfred.reclaim.core.domain.repository.AddictionRepository
import com.engineerfred.reclaim.core.domain.util.ReclaimResult

/**
 * Marks an active addiction journey as completed.
 * * This delegates directly to the repository where it will update the local
 * SQLDelight database (setting isActive = false) and queue the change
 * for Firebase synchronization.
 */
class CompleteAddictionUseCase(
    private val repository: AddictionRepository
) {
    suspend operator fun invoke(addictionId: String): ReclaimResult<Unit> {
        return repository.completeAddiction(addictionId)
    }
}