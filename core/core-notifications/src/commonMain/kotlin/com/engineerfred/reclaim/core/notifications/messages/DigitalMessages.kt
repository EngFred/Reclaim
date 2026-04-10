package com.engineerfred.reclaim.core.notifications.messages

import com.engineerfred.reclaim.core.domain.model.AddictionCategory
import com.engineerfred.reclaim.core.notifications.model.NotificationTriggerType

/**
 * Pre-written notification messages for DIGITAL addictions.
 */
object DigitalMessages {

    fun get(
        category: AddictionCategory,
        triggerType: NotificationTriggerType,
        streakDays: Int = 0,
        milestoneDays: Int? = null
    ): Pair<String, String> = when (category) {

        AddictionCategory.SOCIAL_MEDIA -> socialMediaMessage(triggerType, streakDays, milestoneDays)
        AddictionCategory.PHONE        -> phoneMessage(triggerType, streakDays, milestoneDays)
        AddictionCategory.STREAMING    -> streamingMessage(triggerType, streakDays, milestoneDays)
        else -> throw IllegalArgumentException(
            "DigitalMessages called with non-digital category: $category"
        )
    }

    // ─── Social Media ─────────────────────────────────────────────────────────

    private fun socialMediaMessage(
        type: NotificationTriggerType,
        streak: Int,
        milestone: Int?
    ): Pair<String, String> = when (type) {

        NotificationTriggerType.MORNING_MOTIVATION -> Pair(
            "Good morning — before you open any app.",
            "$streak days of intentional social media use. The comparison trap is strong in the morning. " +
                    "Give yourself 30 minutes of undistracted time first."
        )

        NotificationTriggerType.EVENING_CHECK_IN_REMINDER -> Pair(
            "Check in tonight.",
            "Log your screen time honestly. $streak days of data is building a picture of your progress."
        )

        NotificationTriggerType.HIGH_RISK_WINDOW -> Pair(
            "Boredom window — highest-risk for mindless scrolling.",
            "The algorithm is designed to trap you at exactly this moment. " +
                    "You have $streak days of resistance. Close the app. Do anything else."
        )

        NotificationTriggerType.POST_RELAPSE_COMPASSION -> Pair(
            "You logged a setback.",
            "Social media is engineered by teams whose job is to make you addicted. " +
                    "This is not a willpower failure — it's a design war. " +
                    "What triggered the session? Boredom? Loneliness? Anxiety?"
        )

        NotificationTriggerType.MILESTONE_CELEBRATION -> Pair(
            "Day ${milestone ?: streak} — your attention is yours again.",
            milestoneCelebrationBody(milestone ?: streak, "social media")
        )

        NotificationTriggerType.WEEKLY_PROGRESS_SUMMARY -> Pair(
            "$streak days of intentional use.",
            "Anxiety tied to social comparison drops measurably when screen time is controlled. " +
                    "You are reclaiming your attention."
        )
    }

    // ─── Phone / Screen ───────────────────────────────────────────────────────

    private fun phoneMessage(
        type: NotificationTriggerType,
        streak: Int,
        milestone: Int?
    ): Pair<String, String> = when (type) {

        NotificationTriggerType.MORNING_MOTIVATION -> Pair(
            "Good morning.",
            "$streak days of intentional phone use. " +
                    "The first 30 minutes of your morning without the screen belong to you. Protect them."
        )

        NotificationTriggerType.EVENING_CHECK_IN_REMINDER -> Pair(
            "Check in tonight.",
            "How intentional was your screen time today? Log it. $streak days in and growing."
        )

        NotificationTriggerType.HIGH_RISK_WINDOW -> Pair(
            "Compulsive check-in window.",
            "The urge to reach for your phone every few minutes is a trained reflex, not a need. " +
                    "You have $streak days. Put it face-down for the next hour."
        )

        NotificationTriggerType.POST_RELAPSE_COMPASSION -> Pair(
            "You logged a setback.",
            "Phone addiction is tied directly to anxiety and dopamine-seeking. " +
                    "Notice what you were feeling when the compulsive use started. " +
                    "That feeling is the real target."
        )

        NotificationTriggerType.MILESTONE_CELEBRATION -> Pair(
            "Day ${milestone ?: streak} — your focus is returning.",
            milestoneCelebrationBody(milestone ?: streak, "compulsive phone use")
        )

        NotificationTriggerType.WEEKLY_PROGRESS_SUMMARY -> Pair(
            "$streak days of intentional screen use.",
            "Deep focus and the ability to sit with boredom are returning. " +
                    "Those are the most valuable things the phone was taking from you."
        )
    }

    // ─── Doom-Scrolling / Streaming ───────────────────────────────────────────

    private fun streamingMessage(
        type: NotificationTriggerType,
        streak: Int,
        milestone: Int?
    ): Pair<String, String> = when (type) {

        NotificationTriggerType.MORNING_MOTIVATION -> Pair(
            "Good morning.",
            "$streak days without losing yourself to the feed or the stream. " +
                    "Your time is yours again. What will you do with it today?"
        )

        NotificationTriggerType.EVENING_CHECK_IN_REMINDER -> Pair(
            "Check in before you open anything.",
            "Log your day. Did you stay within your limits? $streak days of honest data tells your story."
        )

        NotificationTriggerType.HIGH_RISK_WINDOW -> Pair(
            "Evening — autoplay trap is active.",
            "Streaming platforms are designed for one more episode to become five more. " +
                    "You have $streak days. Set a hard stop time before you press play."
        )

        NotificationTriggerType.POST_RELAPSE_COMPASSION -> Pair(
            "You logged a setback. Honest logging takes discipline.",
            "Binge-watching is often emotional avoidance — escaping feelings by escaping into a screen. " +
                    "What were you avoiding? That's where the work is."
        )

        NotificationTriggerType.MILESTONE_CELEBRATION -> Pair(
            "Day ${milestone ?: streak} — you are present again.",
            milestoneCelebrationBody(milestone ?: streak, "doom-scrolling and binge-streaming")
        )

        NotificationTriggerType.WEEKLY_PROGRESS_SUMMARY -> Pair(
            "$streak days of intentional consumption.",
            "You are learning to choose content rather than consume it passively. " +
                    "That distinction is the whole battle."
        )
    }

    // ─── Shared helper ────────────────────────────────────────────────────────

    private fun milestoneCelebrationBody(days: Int, addictionLabel: String): String = when (days) {
        7  -> "Seven days of controlled $addictionLabel. Your brain's dopamine baseline — " +
                "which was chronically over-stimulated — is beginning to stabilise. " +
                "Real-world activities will start feeling more rewarding again."
        30 -> "Thirty days of intentional $addictionLabel. Concentration span, " +
                "sleep quality, and real-world motivation all improve measurably in this window. " +
                "You are not just breaking a habit — you are rebuilding your ability to focus."
        90 -> "Ninety days. At this stage, the neurological changes are structural. " +
                "The tolerance your brain built up to constant digital stimulation has recalibrated. " +
                "You have built a genuinely different relationship with technology."
        else -> "Day $days without compulsive $addictionLabel. Keep choosing presence over passive consumption."
    }
}