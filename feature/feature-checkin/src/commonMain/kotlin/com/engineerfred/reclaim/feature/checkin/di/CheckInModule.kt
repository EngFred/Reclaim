package com.engineerfred.reclaim.feature.checkin.di

import com.engineerfred.reclaim.core.domain.repository.CheckInRepository
import com.engineerfred.reclaim.feature.checkin.data.CheckInRepositoryImpl
import com.engineerfred.reclaim.feature.checkin.domain.usecase.GetCheckInHistoryUseCase
import com.engineerfred.reclaim.feature.checkin.domain.usecase.HasCheckedInTodayUseCase
import com.engineerfred.reclaim.feature.checkin.domain.usecase.LogDailyCheckInUseCase
import com.engineerfred.reclaim.feature.checkin.presentation.CheckInViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val checkInModule = module {
    // Repository binding
    singleOf(::CheckInRepositoryImpl) bind CheckInRepository::class

    // Use cases
    factoryOf(::HasCheckedInTodayUseCase)
    factoryOf(::GetCheckInHistoryUseCase)
    factoryOf(::LogDailyCheckInUseCase)

    // ViewModel
    viewModelOf(::CheckInViewModel)
}