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
import com.engineerfred.reclaim.core.notifications.di.coreNotificationsModule
import com.engineerfred.reclaim.feature.addiction.di.addictionModule
import com.engineerfred.reclaim.feature.auth.di.authModule
import com.engineerfred.reclaim.feature.checkin.di.checkInModule
import com.engineerfred.reclaim.feature.dashboard.di.dashboardModule
import com.engineerfred.reclaim.feature.onboarding.di.onboardingModule
import com.engineerfred.reclaim.feature.progress.di.progressModule
import com.engineerfred.reclaim.feature.settings.di.settingsModule
import com.engineerfred.reclaim.feature.sos.di.sosModule

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
                platformDataModule,
                coreNotificationsModule,
                authModule,
                onboardingModule,
                dashboardModule,
                checkInModule,
                progressModule,
                sosModule,
                settingsModule,
                addictionModule
            )
        }

        // 3. Start the background sync engine
        get<SyncManager>().start()
    }
}