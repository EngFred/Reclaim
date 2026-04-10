package com.engineerfred.reclaim.core.data.local

import com.engineerfred.reclaim.core.data.db.ReclaimDatabase

/**
 * Wraps SQLDelight's generated [ReclaimDatabase], ensuring it is
 * created exactly once from the platform-appropriate driver.
 *
 * Injected as a singleton via Koin. All feature data sources
 * receive the same [database] instance.
 */
class DatabaseProvider(driverFactory: DatabaseDriverFactory) {
    val database: ReclaimDatabase = ReclaimDatabase(driverFactory.createDriver())
}