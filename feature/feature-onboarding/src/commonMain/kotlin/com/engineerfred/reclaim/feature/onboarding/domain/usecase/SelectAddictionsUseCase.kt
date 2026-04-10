package com.engineerfred.reclaim.feature.onboarding.domain.usecase

import com.engineerfred.reclaim.core.domain.model.Addiction
import com.engineerfred.reclaim.core.domain.repository.AddictionRepository
import com.engineerfred.reclaim.core.domain.util.ReclaimResult
import com.engineerfred.reclaim.core.domain.util.reclaimRunCatching

class SelectAddictionsUseCase(
    private val addictionRepository: AddictionRepository
) {
    suspend operator fun invoke(
        userId: String,
        addictions: List<Addiction>
    ): ReclaimResult<Unit> = reclaimRunCatching {
        addictions.forEach { addiction ->
            addictionRepository.addAddiction(addiction)
        }
    }
}