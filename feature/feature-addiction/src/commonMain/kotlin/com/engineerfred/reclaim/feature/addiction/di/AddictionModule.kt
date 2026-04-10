package com.engineerfred.reclaim.feature.addiction.di

import com.engineerfred.reclaim.core.domain.repository.AddictionRepository
import com.engineerfred.reclaim.feature.addiction.data.AddictionRepositoryImpl
import com.engineerfred.reclaim.feature.addiction.domain.usecase.AddAddictionUseCase
import com.engineerfred.reclaim.feature.addiction.domain.usecase.CompleteAddictionUseCase
import com.engineerfred.reclaim.feature.addiction.domain.usecase.GetAddictionDetailUseCase
import com.engineerfred.reclaim.feature.addiction.domain.usecase.PauseAddictionUseCase
import com.engineerfred.reclaim.feature.addiction.presentation.add.AddAddictionViewModel
import com.engineerfred.reclaim.feature.addiction.presentation.detail.AddictionDetailViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val addictionModule = module {
    // Repository binding. This satisfies dependencies in feature-onboarding as well!
    singleOf(::AddictionRepositoryImpl) bind AddictionRepository::class

    // Use cases
    factoryOf(::AddAddictionUseCase)
    factoryOf(::PauseAddictionUseCase)
    factoryOf(::CompleteAddictionUseCase)
    factoryOf(::GetAddictionDetailUseCase)

    // ViewModels
    viewModelOf(::AddAddictionViewModel)
    viewModelOf(::AddictionDetailViewModel)
}