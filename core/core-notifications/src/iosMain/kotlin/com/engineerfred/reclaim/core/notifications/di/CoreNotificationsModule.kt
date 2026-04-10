package com.engineerfred.reclaim.core.notifications.di

import com.engineerfred.reclaim.core.notifications.NotificationScheduler
import org.koin.core.module.Module
import org.koin.dsl.module

actual val coreNotificationsModule: Module = module {
    factory { NotificationScheduler(Unit) }
}