package com.engineerfred.reclaim.core.data.di

import com.engineerfred.reclaim.core.data.local.DatabaseDriverFactory
import com.engineerfred.reclaim.core.data.sync.NetworkMonitor
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformDataModule: Module = module {
    single { DatabaseDriverFactory() }
    single { NetworkMonitor() }
}