package com.engineerfred.reclaim.feature.progress.domain.usecase

import com.engineerfred.reclaim.core.domain.repository.CheckInRepository
import com.engineerfred.reclaim.core.domain.util.DateUtils
import com.engineerfred.reclaim.feature.progress.domain.model.CalendarDay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetCalendarDataUseCase(
    private val checkInRepository: CheckInRepository
) {
    /**
     * Generates the last [daysToFetch] days of calendar data, matching actual
     * check-ins to their respective dates.
     */
    operator fun invoke(addictionId: String, daysToFetch: Int = 90): Flow<List<CalendarDay>> {
        return checkInRepository.observeCheckIns(addictionId).map { checkIns ->
            val now = System.currentTimeMillis()
            val todayStart = DateUtils.toStartOfDayUtc(now)

            val checkInMap = checkIns.associateBy { it.date }

            // Generate a list of days going backward from today
            (0 until daysToFetch).map { daysAgo ->
                val targetDate = todayStart - (daysAgo * DateUtils.MILLIS_IN_A_DAY)
                CalendarDay(
                    dateMillis = targetDate,
                    status = checkInMap[targetDate]?.status,
                    isToday = daysAgo == 0
                )
            }.reversed() // Reverse so the oldest day is first (reading left-to-right, top-to-bottom)
        }
    }
}