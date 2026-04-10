package com.engineerfred.reclaim.core.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.engineerfred.reclaim.core.data.db.ReclaimDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver = NativeSqliteDriver(
        schema   = ReclaimDatabase.Schema,
        name     = "reclaim.db"
    )
}