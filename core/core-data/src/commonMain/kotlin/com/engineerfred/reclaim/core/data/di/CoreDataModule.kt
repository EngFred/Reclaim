package com.engineerfred.reclaim.core.data.di

import com.engineerfred.reclaim.core.data.local.DatabaseProvider
import com.engineerfred.reclaim.core.data.remote.FirebaseProvider
import com.engineerfred.reclaim.core.data.sync.SyncManager
import com.engineerfred.reclaim.core.data.theme.ThemeRepositoryImpl
import com.engineerfred.reclaim.core.domain.repository.ThemeRepository
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    single { DatabaseProvider(get()) }
    single { FirebaseProvider() }
    single { SyncManager(get(), get(), get()) }

    // ThemeStorage is registered in platformDataModule (constructor differs per platform)
    single { ThemeRepositoryImpl(get()) } bind ThemeRepository::class
}