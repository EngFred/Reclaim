package com.engineerfred.reclaim.feature.auth.di

import com.engineerfred.reclaim.core.domain.repository.AuthRepository
import com.engineerfred.reclaim.feature.auth.data.AuthRepositoryImpl
import com.engineerfred.reclaim.feature.auth.domain.usecase.GetCurrentUserUseCase
import com.engineerfred.reclaim.feature.auth.domain.usecase.LoginUseCase
import com.engineerfred.reclaim.feature.auth.domain.usecase.LogoutUseCase
import com.engineerfred.reclaim.feature.auth.domain.usecase.RegisterUseCase
import com.engineerfred.reclaim.feature.auth.presentation.login.LoginViewModel
import com.engineerfred.reclaim.feature.auth.presentation.register.RegisterViewModel
import com.engineerfred.reclaim.feature.auth.presentation.splash.SplashViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Koin module for feature-auth.
 *
 * Load this alongside [coreDataModule] and [platformDataModule] at startup.
 *
 * Binding order:
 * AuthRepositoryImpl → bound as AuthRepository interface
 * Use cases → factory (new instance per injection site, stateless)
 * ViewModels → registered for koinViewModel() in Composables
 */
val authModule = module {

    // Repository
    singleOf(::AuthRepositoryImpl) bind AuthRepository::class

    // Use cases
    factoryOf(::LoginUseCase)
    factoryOf(::RegisterUseCase)
    factoryOf(::LogoutUseCase)
    factoryOf(::GetCurrentUserUseCase)

    // ViewModels
    viewModelOf(::SplashViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
}