package com.engineerfred.reclaim.core.notifications

import com.engineerfred.reclaim.core.notifications.model.ScheduledNotification

/**
 * Platform-agnostic interface for scheduling and cancelling notifications.
 *
 * Android actual:  WorkManager for periodic + delayed tasks.
 *                  FCM for server-pushed notifications (future).
 * iOS actual:      UNUserNotificationCenter for all local scheduling.
 *
 * All platform-specific scheduling logic lives in the actual implementations.
 * Callers in commonMain only see this expect class.
 *
 * Constructor takes [context: Any] to allow Android to receive a Context
 * while iOS ignores it. This is the standard KMP expect/actual pattern
 * for platform-context injection.
 */
expect class NotificationScheduler(context: Any) {

    /**
     * Schedules (or replaces) a notification described by [notification].
     * If a notification with the same [ScheduledNotification.id] already
     * exists, it is cancelled and replaced.
     */
    fun schedule(notification: ScheduledNotification)

    /**
     * Cancels all notifications for a specific addiction ID.
     * Called when an addiction is paused, completed, or deleted.
     */
    fun cancelAllForAddiction(addictionId: String)

    /**
     * Cancels every scheduled notification for this app.
     * Called on logout or account deletion.
     */
    fun cancelAll()

    /**
     * Requests the OS notification permission.
     * On Android 13+ this is required at runtime.
     * On iOS this triggers the system permission dialog.
     * On older Android it is a no-op.
     */
    fun requestPermission()
}