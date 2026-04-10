package com.engineerfred.reclaim.core.notifications

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.engineerfred.reclaim.core.notifications.model.ScheduledNotification
import java.util.concurrent.TimeUnit

actual class NotificationScheduler actual constructor(context: Any) {

    private val ctx: Context = context as Context
    private val workManager: WorkManager = WorkManager.getInstance(ctx)

    actual fun schedule(notification: ScheduledNotification) {
        val inputData: Data = workDataOf(
            KEY_ADDICTION_ID   to notification.addictionId,
            KEY_CATEGORY       to notification.category.name,
            KEY_TRIGGER_TYPE   to notification.triggerType.name,
            KEY_STREAK_DAYS    to notification.streakDays,
            KEY_MILESTONE_DAYS to (notification.milestoneDays ?: -1)
        )

        val request = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInputData(inputData)
            .setInitialDelay(notification.delayMillis, TimeUnit.MILLISECONDS)
            .addTag(notification.addictionId)           // enables cancelAllForAddiction
            .addTag(notification.id)                    // enables individual replacement
            .build()

        workManager.enqueueUniqueWork(
            notification.id,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    actual fun cancelAllForAddiction(addictionId: String) {
        workManager.cancelAllWorkByTag(addictionId)
    }

    actual fun cancelAll() {
        workManager.cancelAllWork()
    }

    actual fun requestPermission() {
        // Android 13+ (API 33) requires POST_NOTIFICATIONS permission.
        // Permission is requested from the UI layer (Activity/Composable)
        // via ActivityResultLauncher — not from a background class.
        // This method is intentionally a no-op here; the feature-onboarding
        // NotificationPermissionScreen handles the runtime request.
    }

    companion object {
        const val KEY_ADDICTION_ID   = "addiction_id"
        const val KEY_CATEGORY       = "category"
        const val KEY_TRIGGER_TYPE   = "trigger_type"
        const val KEY_STREAK_DAYS    = "streak_days"
        const val KEY_MILESTONE_DAYS = "milestone_days"
    }
}