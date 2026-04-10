package com.engineerfred.reclaim.feature.settings.di

import com.engineerfred.reclaim.core.domain.repository.NotificationPreferencesRepository
import com.engineerfred.reclaim.feature.settings.data.SettingsRepositoryImpl
import com.engineerfred.reclaim.feature.settings.domain.usecase.DeleteAccountUseCase
import com.engineerfred.reclaim.feature.settings.domain.usecase.UpdateNotificationPrefsUseCase
import com.engineerfred.reclaim.feature.settings.domain.usecase.UpdateWhyIQuitNoteUseCase
import com.engineerfred.reclaim.feature.settings.presentation.SettingsViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val settingsModule = module {
    singleOf(::SettingsRepositoryImpl) bind NotificationPreferencesRepository::class

    factoryOf(::UpdateNotificationPrefsUseCase)
    factoryOf(::UpdateWhyIQuitNoteUseCase)
    factoryOf(::DeleteAccountUseCase)

    viewModelOf(::SettingsViewModel)
}