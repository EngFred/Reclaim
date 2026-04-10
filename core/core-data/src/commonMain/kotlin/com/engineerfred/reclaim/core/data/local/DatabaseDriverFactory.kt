package com.engineerfred.reclaim.core.data.local

import app.cash.sqldelight.db.SqlDriver

/**
 * expect class: each platform provides its own actual with the
 * platform-appropriate SqlDriver implementation.
 *
 * Android actual requires a Context parameter.
 * iOS actual requires no parameters.
 *
 * Koin's platformDataModule provides the correct actual per platform.
 */
expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}