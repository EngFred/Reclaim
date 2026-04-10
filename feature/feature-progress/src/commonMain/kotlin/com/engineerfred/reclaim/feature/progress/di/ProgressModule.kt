package com.engineerfred.reclaim.feature.progress.di

import com.engineerfred.reclaim.core.domain.repository.ProgressRepository
import com.engineerfred.reclaim.feature.progress.data.ProgressRepositoryImpl
import com.engineerfred.reclaim.feature.progress.domain.usecase.GetCalendarDataUseCase
import com.engineerfred.reclaim.feature.progress.domain.usecase.GetMilestoneMessageUseCase
import com.engineerfred.reclaim.feature.progress.domain.usecase.GetStreakStatsUseCase
import com.engineerfred.reclaim.feature.progress.presentation.ProgressViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val progressModule = module {
    // Repository binding
    singleOf(::ProgressRepositoryImpl) bind ProgressRepository::class

    // Use cases
    factoryOf(::GetCalendarDataUseCase)
    factoryOf(::GetStreakStatsUseCase)
    factoryOf(::GetMilestoneMessageUseCase)

    // ViewModel
    viewModelOf(::ProgressViewModel)
}