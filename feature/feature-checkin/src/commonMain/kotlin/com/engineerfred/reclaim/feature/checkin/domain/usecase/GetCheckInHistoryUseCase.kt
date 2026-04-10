package com.engineerfred.reclaim.feature.checkin.domain.usecase

import com.engineerfred.reclaim.core.domain.model.CheckIn
import com.engineerfred.reclaim.core.domain.repository.CheckInRepository
import kotlinx.coroutines.flow.Flow

class GetCheckInHistoryUseCase(
    private val checkInRepository: CheckInRepository
) {
    operator fun invoke(addictionId: String): Flow<List<CheckIn>> {
        return checkInRepository.observeCheckIns(addictionId)
    }
}