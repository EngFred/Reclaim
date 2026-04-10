package com.engineerfred.reclaim.feature.dashboard.domain.usecase

import com.engineerfred.reclaim.feature.dashboard.domain.repository.DashboardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTodayGreetingUseCase(
    private val dashboardRepository: DashboardRepository
) {
    operator fun invoke(): Flow<String> {
        return dashboardRepository.observeGreetingName().map { name ->
            // In a full implementation we would check device time for Morning/Afternoon/Evening.
            // For KMP without bringing in kotlinx-datetime just yet, we provide a warm,
            // static greeting that fits the app's compassionate tone.
            "Welcome back, $name."
        }
    }
}