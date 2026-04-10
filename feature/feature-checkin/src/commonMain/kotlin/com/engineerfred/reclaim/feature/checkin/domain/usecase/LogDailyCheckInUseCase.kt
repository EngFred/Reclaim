package com.engineerfred.reclaim.feature.checkin.domain.usecase

import com.engineerfred.reclaim.core.domain.model.CheckIn
import com.engineerfred.reclaim.core.domain.model.CheckInStatus
import com.engineerfred.reclaim.core.domain.model.Streak
import com.engineerfred.reclaim.core.domain.repository.AddictionRepository
import com.engineerfred.reclaim.core.domain.repository.AuthRepository
import com.engineerfred.reclaim.core.domain.repository.CheckInRepository
import com.engineerfred.reclaim.core.domain.repository.ProgressRepository
import com.engineerfred.reclaim.core.domain.util.DateUtils
import com.engineerfred.reclaim.core.domain.util.ReclaimResult
import com.engineerfred.reclaim.core.notifications.NotificationScheduler
import com.engineerfred.reclaim.core.notifications.model.NotificationTriggerType
import com.engineerfred.reclaim.core.notifications.model.ScheduledNotification
import kotlinx.coroutines.flow.firstOrNull

/**
 * Core business logic for accountability.
 * Handles the CheckIn write, computes the new Streak, and schedules compassion notifications if needed.
 */
class LogDailyCheckInUseCase(
    private val authRepository: AuthRepository,
    private val checkInRepository: CheckInRepository,
    private val progressRepository: ProgressRepository,
    private val addictionRepository: AddictionRepository,
    private val notificationScheduler: NotificationScheduler
) {
    suspend operator fun invoke(
        addictionId: String,
        status: CheckInStatus,
        triggerNote: String?
    ): ReclaimResult<Unit> {
        val user = authRepository.getCurrentUser()
            ?: return ReclaimResult.Failure(IllegalStateException("User not authenticated."))

        val addiction = addictionRepository.observeAddiction(addictionId).firstOrNull()
            ?: return ReclaimResult.Failure(IllegalArgumentException("Addiction not found."))

        val now = System.currentTimeMillis()
        val todayStart = DateUtils.toStartOfDayUtc(now)

        // 1. Create and save the CheckIn
        val checkIn = CheckIn(
            id = generateId(),
            addictionId = addictionId,
            userId = user.id,
            date = todayStart,
            status = status,
            triggerNote = triggerNote?.trim()?.takeIf { it.isNotEmpty() },
            isSynced = false
        )

        val checkInResult = checkInRepository.logCheckIn(checkIn)
        if (checkInResult is ReclaimResult.Failure) return checkInResult

        // 2. Compute and save the new Streak
        val currentStreakData = progressRepository.getStreak(addictionId)
        val newStreak = calculateNewStreak(currentStreakData, status)
        progressRepository.saveStreak(newStreak)

        // 3. Handle Relapse Compassion Notification (Fires 30 minutes after logging)
        if (status == CheckInStatus.RELAPSED) {
            notificationScheduler.schedule(
                ScheduledNotification(
                    id = "relapse_${addictionId}_$todayStart",
                    addictionId = addictionId,
                    category = addiction.category,
                    triggerType = NotificationTriggerType.POST_RELAPSE_COMPASSION,
                    delayMillis = 30 * 60 * 1000L, // 30 minutes
                    streakDays = 0,
                    milestoneDays = null
                )
            )
        }

        // 4. Milestone Evaluation (if success/struggled) can be handled globally or here.
        // For v1, milestones are scheduled at morning time by a daily worker,
        // so we don't schedule milestone pushes directly on button press here.

        return ReclaimResult.Success(Unit)
    }

    private fun calculateNewStreak(old: Streak, status: CheckInStatus): Streak {
        val newCurrent = if (status == CheckInStatus.RELAPSED) 0 else old.current + 1
        val newLongest = maxOf(old.longest, newCurrent)
        val newTotalDays = old.totalDays + 1

        // Calculate success rate. RELAPSED counts against success rate. SUCCESS/STRUGGLED count for it.
        // Recover successful days from the old rate: (totalDays * successRate)
        val pastSuccessfulDays = old.totalDays * old.successRate
        val todaySuccessValue = if (status == CheckInStatus.RELAPSED) 0 else 1
        val newSuccessRate = (pastSuccessfulDays + todaySuccessValue) / newTotalDays.toFloat()

        return Streak(
            addictionId = old.addictionId,
            current = newCurrent,
            longest = newLongest,
            totalDays = newTotalDays,
            successRate = newSuccessRate
        )
    }

    private fun generateId(): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        val randomString = (1..10).map { allowedChars.random() }.joinToString("")
        return "${System.currentTimeMillis()}_$randomString"
    }
}