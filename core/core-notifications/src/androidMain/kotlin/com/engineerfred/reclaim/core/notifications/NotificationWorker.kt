package com.engineerfred.reclaim.core.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.engineerfred.reclaim.core.domain.model.AddictionCategory
import com.engineerfred.reclaim.core.notifications.messages.MessageSelector
import com.engineerfred.reclaim.core.notifications.model.NotificationTriggerType

/**
 * WorkManager worker that fires a single notification.
 * Receives all required data via [WorkerParameters.inputData].
 * Builds the notification title and body by calling [MessageSelector].
 */
class NotificationWorker(
    private val appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val addictionId   = inputData.getString(NotificationScheduler.KEY_ADDICTION_ID) ?: return Result.failure()
        val categoryName  = inputData.getString(NotificationScheduler.KEY_CATEGORY)     ?: return Result.failure()
        val triggerName   = inputData.getString(NotificationScheduler.KEY_TRIGGER_TYPE) ?: return Result.failure()
        val streakDays    = inputData.getInt(NotificationScheduler.KEY_STREAK_DAYS, 0)
        val milestoneDays = inputData.getInt(NotificationScheduler.KEY_MILESTONE_DAYS, -1)
            .takeIf { it > 0 }

        val category    = AddictionCategory.valueOf(categoryName)
        val triggerType = NotificationTriggerType.valueOf(triggerName)

        val (title, body) = MessageSelector.select(
            category       = category,
            triggerType    = triggerType,
            streakDays     = streakDays,
            milestoneDays  = milestoneDays
        )

        showNotification(
            id    = addictionId.hashCode(),
            title = title,
            body  = body
        )

        return Result.success()
    }

    private fun showNotification(id: Int, title: String, body: String) {
        val manager = appContext.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        // Create channel on API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "RECLAIM recovery reminders and milestone celebrations"
            }
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(appContext, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // replace with app icon in composeApp
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        manager.notify(id, notification)
    }

    companion object {
        private const val CHANNEL_ID   = "reclaim_reminders"
        private const val CHANNEL_NAME = "Recovery Reminders"
    }
}