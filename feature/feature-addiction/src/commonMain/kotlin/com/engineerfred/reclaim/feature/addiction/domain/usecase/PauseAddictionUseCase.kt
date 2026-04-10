package com.engineerfred.reclaim.feature.addiction.domain.usecase

import com.engineerfred.reclaim.core.domain.repository.AddictionRepository
import com.engineerfred.reclaim.core.domain.util.ReclaimResult

class PauseAddictionUseCase(
    private val repository: AddictionRepository
) {
    suspend operator fun invoke(addictionId: String): ReclaimResult<Unit> =
        repository.pauseAddiction(addictionId)
}

// CompleteAddictionUseCase.kt would be identical, calling repository.completeAddiction(addictionId)