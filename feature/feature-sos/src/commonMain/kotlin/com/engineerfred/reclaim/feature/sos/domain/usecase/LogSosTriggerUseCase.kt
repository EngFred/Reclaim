package com.engineerfred.reclaim.feature.sos.domain.usecase

import com.engineerfred.reclaim.core.domain.repository.AddictionRepository
import com.engineerfred.reclaim.core.domain.util.ReclaimResult
import com.engineerfred.reclaim.core.notifications.NotificationScheduler
import com.engineerfred.reclaim.core.notifications.model.NotificationTriggerType
import com.engineerfred.reclaim.core.notifications.model.ScheduledNotification
import com.engineerfred.reclaim.feature.sos.domain.repository.SosRepository
import kotlinx.coroutines.flow.firstOrNull

class LogSosTriggerUseCase(
    private val sosRepository: SosRepository,
    private val addictionRepository: AddictionRepository,
    private val notificationScheduler: NotificationScheduler
) {
    suspend operator fun invoke(addictionId: String): ReclaimResult<Unit> {
        val result = sosRepository.logSosTrigger(addictionId)

        if (result is ReclaimResult.Success) {
            val addiction = addictionRepository.observeAddiction(addictionId).firstOrNull()

            if (addiction != null) {
                // Schedule a gentle follow-up notification for 15 minutes from now.
                // We use HIGH_RISK_WINDOW as the trigger type to utilize the compassionate pre-written messages.
                notificationScheduler.schedule(
                    ScheduledNotification(
                        id = "sos_followup_${addictionId}_${System.currentTimeMillis()}",
                        addictionId = addictionId,
                        category = addiction.category,
                        triggerType = NotificationTriggerType.HIGH_RISK_WINDOW,
                        delayMillis = 15 * 60 * 1000L, // 15 minutes
                        streakDays = 0 // Injected by the scheduler later if needed
                    )
                )
            }
        }
        return result
    }
}