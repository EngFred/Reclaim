package com.engineerfred.reclaim.feature.sos.di

import com.engineerfred.reclaim.feature.sos.data.SosRepositoryImpl
import com.engineerfred.reclaim.feature.sos.domain.repository.SosRepository
import com.engineerfred.reclaim.feature.sos.domain.usecase.GetSosContentUseCase
import com.engineerfred.reclaim.feature.sos.domain.usecase.GetWhyIQuitNoteUseCase
import com.engineerfred.reclaim.feature.sos.domain.usecase.LogSosTriggerUseCase
import com.engineerfred.reclaim.feature.sos.presentation.SosViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val sosModule = module {
    // Repository
    singleOf(::SosRepositoryImpl) bind SosRepository::class

    // Use cases
    factoryOf(::GetWhyIQuitNoteUseCase)
    factoryOf(::GetSosContentUseCase)
    factoryOf(::LogSosTriggerUseCase)

    // ViewModel
    viewModelOf(::SosViewModel)
}