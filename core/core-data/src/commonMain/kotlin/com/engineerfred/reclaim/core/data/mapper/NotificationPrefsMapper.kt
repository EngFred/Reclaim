package com.engineerfred.reclaim.core.data.mapper

import com.engineerfred.reclaim.core.data.db.NotificationPrefsEntity
import com.engineerfred.reclaim.core.domain.model.NotificationPreferences

/**
 * Maps between [NotificationPrefsEntity] (SQLDelight) and
 * [NotificationPreferences] (domain).
 *
 * All boolean fields are stored as INTEGER (0 / 1) → Long.
 * Hour fields are stored as INTEGER → Long, converted to Int in domain.
 */
fun NotificationPrefsEntity.toDomain(): NotificationPreferences = NotificationPreferences(
    userId                    = userId,
    morningReminderEnabled    = morningReminderEnabled == 1L,
    morningReminderHour       = morningReminderHour.toInt(),
    eveningReminderEnabled    = eveningReminderEnabled == 1L,
    eveningReminderHour       = eveningReminderHour.toInt(),
    highRiskAlertsEnabled     = highRiskAlertsEnabled == 1L,
    milestoneAlertsEnabled    = milestoneAlertsEnabled == 1L,
    postRelapseMessagesEnabled = postRelapseMessagesEnabled == 1L,
    weeklyProgressEnabled     = weeklyProgressEnabled == 1L
)

fun NotificationPreferences.toEntity(): NotificationPrefsEntity = NotificationPrefsEntity(
    userId                    = userId,
    morningReminderEnabled    = if (morningReminderEnabled) 1L else 0L,
    morningReminderHour       = morningReminderHour.toLong(),
    eveningReminderEnabled    = if (eveningReminderEnabled) 1L else 0L,
    eveningReminderHour       = eveningReminderHour.toLong(),
    highRiskAlertsEnabled     = if (highRiskAlertsEnabled) 1L else 0L,
    milestoneAlertsEnabled    = if (milestoneAlertsEnabled) 1L else 0L,
    postRelapseMessagesEnabled = if (postRelapseMessagesEnabled) 1L else 0L,
    weeklyProgressEnabled     = if (weeklyProgressEnabled) 1L else 0L
)