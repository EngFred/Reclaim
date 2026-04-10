package com.engineerfred.reclaim.feature.progress.domain.usecase

import com.engineerfred.reclaim.core.domain.model.Milestone
import com.engineerfred.reclaim.core.domain.model.Streak
import com.engineerfred.reclaim.core.domain.util.DateUtils

/**
 * Evaluates the current streak and returns the relevant milestone to display.
 * Since feature-progress does not depend on core-notifications, this provides
 * UI-specific motivational text based on the core Milestone model.
 */
class GetMilestoneMessageUseCase {
    operator fun invoke(streak: Streak): Milestone? {
        val current = streak.current

        return when {
            current >= 90 -> Milestone(
                days = 90,
                title = "Neurological Recalibration",
                message = "You have surpassed 90 days. Research shows your brain's reward system has fundamentally restructured. You've built lasting resilience.",
                achievedAt = null
            )
            current >= 30 -> Milestone(
                days = 30,
                title = "Physical Adaptation",
                message = "30 days clean. New neural pathways have stabilized. Your physical dependence has significantly dropped. The fog is lifting.",
                achievedAt = null
            )
            current >= 7 -> Milestone(
                days = 7,
                title = "Initial Withdrawal Cleared",
                message = "One week. The most intense physical cravings are behind you. Dopamine receptors are beginning to heal.",
                achievedAt = null
            )
            else -> {
                val next = DateUtils.nextMilestoneDays(current) ?: 7
                Milestone(
                    days = next,
                    title = "Keep Going",
                    message = "Every single day matters. You are ${next - current} days away from your next major biological milestone.",
                    achievedAt = null
                )
            }
        }
    }
}