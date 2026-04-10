package com.engineerfred.reclaim.core.domain.model

data class NotificationPreferences(
    val userId: String,
    val morningReminderEnabled: Boolean = true,
    val morningReminderHour: Int = 7,
    val eveningReminderEnabled: Boolean = true,
    val eveningReminderHour: Int = 21,
    val highRiskAlertsEnabled: Boolean = true,
    val milestoneAlertsEnabled: Boolean = true,
    val postRelapseMessagesEnabled: Boolean = true,
    val weeklyProgressEnabled: Boolean = true
)