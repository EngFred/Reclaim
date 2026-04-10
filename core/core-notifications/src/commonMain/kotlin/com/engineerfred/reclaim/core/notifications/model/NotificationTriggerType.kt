package com.engineerfred.reclaim.core.notifications.model

/**
 * Every distinct reason the app may fire a notification.
 * The [MessageSelector] maps these to the correct pre-written string
 * from the message banks.
 */
enum class NotificationTriggerType {
    /** Fired at the user's configured morning hour (default 07:00). */
    MORNING_MOTIVATION,

    /** Fired at the user's configured evening hour (default 21:00) only
     *  if they have NOT checked in today. */
    EVENING_CHECK_IN_REMINDER,

    /** Fired at a high-risk time window that is specific to each addiction
     *  category (e.g. Friday 18:00 for ALCOHOL, late night for PORNOGRAPHY). */
    HIGH_RISK_WINDOW,

    /** Fired ~30 minutes after a RELAPSED check-in is logged. */
    POST_RELAPSE_COMPASSION,

    /** Fired on the morning of day 7, 30, or 90. */
    MILESTONE_CELEBRATION,

    /** Fired once per week with a streak + success-rate summary. */
    WEEKLY_PROGRESS_SUMMARY
}