package com.engineerfred.reclaim.feature.onboarding.di

import com.engineerfred.reclaim.feature.onboarding.domain.usecase.CompleteOnboardingUseCase
import com.engineerfred.reclaim.feature.onboarding.presentation.OnboardingViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val onboardingModule = module {
    factoryOf(::CompleteOnboardingUseCase)
    viewModelOf(::OnboardingViewModel)
}