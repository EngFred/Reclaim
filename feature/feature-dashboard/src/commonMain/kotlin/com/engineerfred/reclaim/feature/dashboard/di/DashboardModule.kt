package com.engineerfred.reclaim.feature.dashboard.di

import com.engineerfred.reclaim.feature.dashboard.data.DashboardRepositoryImpl
import com.engineerfred.reclaim.feature.dashboard.domain.repository.DashboardRepository
import com.engineerfred.reclaim.feature.dashboard.domain.usecase.GetAllActiveAddictionsUseCase
import com.engineerfred.reclaim.feature.dashboard.domain.usecase.GetStreakForAddictionUseCase
import com.engineerfred.reclaim.feature.dashboard.domain.usecase.GetTodayGreetingUseCase
import com.engineerfred.reclaim.feature.dashboard.presentation.DashboardViewModel
import com.engineerfred.reclaim.feature.dashboard.presentation.pick.PickAddictionViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dashboardModule = module {
    // Repository
    singleOf(::DashboardRepositoryImpl) bind DashboardRepository::class

    // Use cases
    factoryOf(::GetAllActiveAddictionsUseCase)
    factoryOf(::GetStreakForAddictionUseCase)
    factoryOf(::GetTodayGreetingUseCase)

    // ViewModel
    viewModelOf(::DashboardViewModel)
    viewModelOf(::PickAddictionViewModel)
}