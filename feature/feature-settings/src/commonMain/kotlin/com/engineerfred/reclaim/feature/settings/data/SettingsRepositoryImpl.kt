package com.engineerfred.reclaim.feature.settings.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import com.engineerfred.reclaim.core.data.local.DatabaseProvider
import com.engineerfred.reclaim.core.data.mapper.toDomain
import com.engineerfred.reclaim.core.data.mapper.toEntity
import com.engineerfred.reclaim.core.domain.model.NotificationPreferences
import com.engineerfred.reclaim.core.domain.repository.NotificationPreferencesRepository
import com.engineerfred.reclaim.core.domain.util.ReclaimResult
import com.engineerfred.reclaim.core.domain.util.reclaimRunCatching
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class SettingsRepositoryImpl(
    private val databaseProvider: DatabaseProvider
) : NotificationPreferencesRepository {

    private val queries = databaseProvider.database.notificationPrefsEntityQueries

    override fun observePreferences(userId: String): Flow<NotificationPreferences> {
        return queries.selectPrefsForUser(userId)
            .asFlow()
            .mapToOne(Dispatchers.IO)
            .map { it.toDomain() }
    }

    override suspend fun savePreferences(preferences: NotificationPreferences): ReclaimResult<Unit> =
        reclaimRunCatching {
            withContext(Dispatchers.IO) {
                val entity = preferences.toEntity()
                queries.insertOrReplacePrefs(
                    userId = entity.userId,
                    morningReminderEnabled = entity.morningReminderEnabled,
                    morningReminderHour = entity.morningReminderHour,
                    eveningReminderEnabled = entity.eveningReminderEnabled,
                    eveningReminderHour = entity.eveningReminderHour,
                    highRiskAlertsEnabled = entity.highRiskAlertsEnabled,
                    milestoneAlertsEnabled = entity.milestoneAlertsEnabled,
                    postRelapseMessagesEnabled = entity.postRelapseMessagesEnabled,
                    weeklyProgressEnabled = entity.weeklyProgressEnabled
                )
            }
        }
}