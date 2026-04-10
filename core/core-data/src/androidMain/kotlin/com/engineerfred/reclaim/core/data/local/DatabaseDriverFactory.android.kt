package com.engineerfred.reclaim.core.data.local

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.engineerfred.reclaim.core.data.db.ReclaimDatabase

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver = AndroidSqliteDriver(
        schema  = ReclaimDatabase.Schema,
        context = context,
        name    = "reclaim.db"
    )
}