package com.engineerfred.reclaim.core.data.di

import com.engineerfred.reclaim.core.data.local.DatabaseProvider
import com.engineerfred.reclaim.core.data.remote.FirebaseProvider
import org.koin.dsl.module
import com.engineerfred.reclaim.core.data.sync.SyncManager

/**
 * Platform-agnostic Koin module for core-data.
 *
 * Provides:
 *  - [DatabaseProvider] — wraps the SQLDelight [ReclaimDatabase].
 *  - [FirebaseProvider] — wraps GitLive Firebase service instances.
 *  - [SyncManager]      — offline-sync engine, started by the app shell.
 *
 * Platform-specific bindings ([DatabaseDriverFactory], [NetworkMonitor])
 * live in [platformDataModule] (see PlatformDataModule.kt).
 *
 * Load both modules together at app startup:
 *   startKoin { modules(coreDataModule, platformDataModule) }
 */
val coreDataModule = module {

    // Wraps the SQLDelight ReclaimDatabase singleton.
    // DatabaseDriverFactory is provided by platformDataModule.
    single { DatabaseProvider(get()) }

    // Wraps Firebase.auth, Firebase.firestore, Firebase.messaging.
    single { FirebaseProvider() }

    // Offline-first sync engine. Call syncManager.start() from the
    // app shell after Koin is initialised.
    single { SyncManager(get(), get(), get()) }
}