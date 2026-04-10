package com.engineerfred.reclaim.feature.checkin.domain.usecase

import com.engineerfred.reclaim.core.domain.repository.CheckInRepository
import com.engineerfred.reclaim.core.domain.util.DateUtils

class HasCheckedInTodayUseCase(
    private val checkInRepository: CheckInRepository
) {
    suspend operator fun invoke(addictionId: String): Boolean {
        val todayStart = DateUtils.toStartOfDayUtc(System.currentTimeMillis())
        return checkInRepository.hasCheckedInToday(addictionId, todayStart)
    }
}