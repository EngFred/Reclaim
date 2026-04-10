package com.engineerfred.reclaim.feature.dashboard.domain.usecase

import com.engineerfred.reclaim.core.domain.model.Streak
import com.engineerfred.reclaim.core.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.Flow

class GetStreakForAddictionUseCase(
    private val progressRepository: ProgressRepository
) {
    operator fun invoke(addictionId: String): Flow<Streak> {
        return progressRepository.observeStreak(addictionId)
    }
}