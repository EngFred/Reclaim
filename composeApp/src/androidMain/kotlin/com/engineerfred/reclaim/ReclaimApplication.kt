package com.engineerfred.reclaim

import android.app.Application
import com.engineerfred.reclaim.core.data.di.coreDataModule
import com.engineerfred.reclaim.core.data.di.platformDataModule
import com.engineerfred.reclaim.core.data.remote.FirebaseInitializer
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import com.engineerfred.reclaim.core.data.sync.SyncManager

class ReclaimApplication : Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()

        // 1. Initialise Firebase (no-op until google-services.json is present)
        FirebaseInitializer.initialize(this)

        // 2. Start Koin
        startKoin {
            androidContext(this@ReclaimApplication)
            modules(
                coreDataModule,
                platformDataModule
                // feature modules added here as they are built
            )
        }

        // 3. Start the background sync engine
        get<SyncManager>().start()
    }
}