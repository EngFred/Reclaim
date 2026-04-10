package com.engineerfred.reclaim.feature.addiction.domain.usecase

import com.engineerfred.reclaim.core.domain.model.Addiction
import com.engineerfred.reclaim.core.domain.repository.AddictionRepository
import kotlinx.coroutines.flow.Flow

class GetAddictionDetailUseCase(
    private val repository: AddictionRepository
) {
    operator fun invoke(addictionId: String): Flow<Addiction?> =
        repository.observeAddiction(addictionId)
}