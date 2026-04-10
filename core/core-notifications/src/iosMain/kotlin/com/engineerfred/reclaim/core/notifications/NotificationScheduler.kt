package com.engineerfred.reclaim.core.notifications

import com.engineerfred.reclaim.core.notifications.messages.MessageSelector
import com.engineerfred.reclaim.core.notifications.model.ScheduledNotification
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNTimeIntervalNotificationTrigger
import platform.UserNotifications.UNUserNotificationCenter

actual class NotificationScheduler actual constructor(context: Any) {

    private val center: UNUserNotificationCenter =
        UNUserNotificationCenter.currentNotificationCenter()

    actual fun schedule(notification: ScheduledNotification) {
        val (title, body) = MessageSelector.select(
            category      = notification.category,
            triggerType   = notification.triggerType,
            streakDays    = notification.streakDays,
            milestoneDays = notification.milestoneDays
        )

        val content = UNMutableNotificationContent().apply {
            setTitle(title)
            setBody(body)
            setSound(platform.UserNotifications.UNNotificationSound.defaultSound())
        }

        val delaySeconds = (notification.delayMillis / 1000.0).coerceAtLeast(1.0)
        val trigger = UNTimeIntervalNotificationTrigger.triggerWithTimeInterval(
            timeInterval = delaySeconds,
            repeats = false
        )

        val request = UNNotificationRequest.requestWithIdentifier(
            identifier = notification.id,
            content    = content,
            trigger    = trigger
        )

        center.addNotificationRequest(request) { error ->
            // Error is logged silently — notifications are non-critical.
            // If permission was not granted, the OS will reject the request
            // without crashing the app.
        }
    }

    actual fun cancelAllForAddiction(addictionId: String) {
        // iOS does not support tag-based cancellation natively.
        // We use the convention that all notification IDs for an addiction
        // are prefixed with the addictionId — cancel any pending that match.
        center.getPendingNotificationRequestsWithCompletionHandler { requests ->
            val toRemove = requests
                ?.filterIsInstance<UNNotificationRequest>()
                ?.filter { it.identifier.startsWith(addictionId) }
                ?.map { it.identifier }
                ?: return@getPendingNotificationRequestsWithCompletionHandler

            if (toRemove.isNotEmpty()) {
                center.removePendingNotificationRequestsWithIdentifiers(toRemove)
            }
        }
    }

    actual fun cancelAll() {
        center.removeAllPendingNotificationRequests()
    }

    actual fun requestPermission() {
        center.requestAuthorizationWithOptions(
            UNAuthorizationOptionAlert or UNAuthorizationOptionSound or UNAuthorizationOptionBadge
        ) { _, _ -> }
    }
}