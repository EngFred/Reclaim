package com.engineerfred.reclaim.core.notifications.di

import com.engineerfred.reclaim.core.notifications.NotificationScheduler
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Koin module for core-notifications.
 *
 * [NotificationScheduler] is registered as a factory (not singleton)
 * because the Android actual requires a Context and on iOS the class is
 * lightweight and stateless beyond the UNUserNotificationCenter reference.
 *
 * The platform-appropriate actual class is resolved automatically by KMP —
 * callers always inject [NotificationScheduler] and receive the correct
 * platform implementation.
 *
 * Usage at startup:
 *   startKoin { modules(coreDataModule, platformDataModule, coreNotificationsModule) }
 */
expect val coreNotificationsModule: Module