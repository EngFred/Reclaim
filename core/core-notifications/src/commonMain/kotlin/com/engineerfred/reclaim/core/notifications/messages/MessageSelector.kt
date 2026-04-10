package com.engineerfred.reclaim.core.notifications.messages

import com.engineerfred.reclaim.core.domain.model.AddictionCategory
import com.engineerfred.reclaim.core.domain.model.AddictionType
import com.engineerfred.reclaim.core.notifications.model.NotificationTriggerType

/**
 * Single entry-point for retrieving a notification message.
 *
 * Callers never touch the individual message bank objects directly —
 * they call [select] with a category, trigger type, and optional context
 * and receive a ready-to-display Pair<title, body>.
 *
 * This object is pure Kotlin — no platform dependencies — and is
 * safe to call from commonMain in both the notification scheduler
 * and any ViewModel that needs to preview a message.
 */
object MessageSelector {

    /**
     * Returns a Pair<title, body> for the given parameters.
     *
     * @param category     The addiction category the message is for.
     * @param triggerType  Why the notification is being sent.
     * @param streakDays   The user's current clean streak (injected into message text).
     * @param milestoneDays  Required only for [NotificationTriggerType.MILESTONE_CELEBRATION].
     */
    fun select(
        category: AddictionCategory,
        triggerType: NotificationTriggerType,
        streakDays: Int = 0,
        milestoneDays: Int? = null
    ): Pair<String, String> {
        return when (category.toAddictionType()) {
            AddictionType.BEHAVIORAL -> BehavioralMessages.get(
                category, triggerType, streakDays, milestoneDays
            )
            AddictionType.SUBSTANCE  -> SubstanceMessages.get(
                category, triggerType, streakDays, milestoneDays
            )
            AddictionType.DIGITAL    -> DigitalMessages.get(
                category, triggerType, streakDays, milestoneDays
            )
        }
    }
}