package com.engineerfred.reclaim.core.notifications.model

import com.engineerfred.reclaim.core.domain.model.AddictionCategory

/**
 * Everything the platform-specific [NotificationScheduler] needs
 * to schedule a single notification.
 *
 * @param id             Stable unique ID — used to cancel or replace
 *                       an existing schedule for the same slot.
 * @param addictionId    The addiction this notification belongs to.
 * @param category       Drives message selection.
 * @param triggerType    The reason for the notification.
 * @param delayMillis    How many milliseconds from now to fire.
 *                       0 = fire immediately (post-relapse compassion).
 * @param streakDays     Current clean streak — injected into messages
 *                       that reference it (e.g. "You have X days").
 * @param milestoneDays  Populated only for MILESTONE_CELEBRATION (7/30/90).
 */
data class ScheduledNotification(
    val id: String,
    val addictionId: String,
    val category: AddictionCategory,
    val triggerType: NotificationTriggerType,
    val delayMillis: Long,
    val streakDays: Int = 0,
    val milestoneDays: Int? = null
)