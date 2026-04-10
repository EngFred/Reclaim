package com.engineerfred.reclaim.feature.settings.domain.usecase

import com.engineerfred.reclaim.core.domain.model.NotificationPreferences
import com.engineerfred.reclaim.core.domain.repository.NotificationPreferencesRepository
import com.engineerfred.reclaim.core.domain.util.ReclaimResult

class UpdateNotificationPrefsUseCase(
    private val repository: NotificationPreferencesRepository
) {
    suspend operator fun invoke(preferences: NotificationPreferences): ReclaimResult<Unit> {
        // Business Rule: Morning and Evening hours must be distinct.
        if (preferences.morningReminderHour == preferences.eveningReminderHour) {
            return ReclaimResult.Failure(IllegalArgumentException("Morning and evening reminders cannot be at the same time."))
        }
        return repository.savePreferences(preferences)
    }
}