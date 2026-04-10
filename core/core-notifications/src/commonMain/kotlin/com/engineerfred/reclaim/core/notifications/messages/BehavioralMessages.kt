package com.engineerfred.reclaim.core.notifications.messages

import com.engineerfred.reclaim.core.domain.model.AddictionCategory
import com.engineerfred.reclaim.core.notifications.model.NotificationTriggerType

/**
 * Pre-written notification messages for BEHAVIORAL addictions.
 * Each function returns a Pair<title, body> ready for the OS notification API.
 *
 * Messages are specific to the addiction category AND the trigger type —
 * this specificity is the core product value.
 *
 * No AI API. No runtime cost. Works fully offline.
 */
object BehavioralMessages {

    fun get(
        category: AddictionCategory,
        triggerType: NotificationTriggerType,
        streakDays: Int = 0,
        milestoneDays: Int? = null
    ): Pair<String, String> = when (category) {

        AddictionCategory.PORNOGRAPHY -> pornographyMessage(triggerType, streakDays, milestoneDays)
        AddictionCategory.GAMBLING    -> gamblingMessage(triggerType, streakDays, milestoneDays)
        AddictionCategory.BINGE_EATING -> bingeEatingMessage(triggerType, streakDays, milestoneDays)
        AddictionCategory.GAMING      -> gamingMessage(triggerType, streakDays, milestoneDays)
        AddictionCategory.SHOPPING    -> shoppingMessage(triggerType, streakDays, milestoneDays)
        AddictionCategory.SELF_HARM   -> selfHarmMessage(triggerType, streakDays, milestoneDays)
        else -> throw IllegalArgumentException(
            "BehavioralMessages called with non-behavioral category: $category"
        )
    }

    // ─── Pornography ─────────────────────────────────────────────────────────

    private fun pornographyMessage(
        type: NotificationTriggerType,
        streak: Int,
        milestone: Int?
    ): Pair<String, String> = when (type) {

        NotificationTriggerType.MORNING_MOTIVATION -> Pair(
            "Good morning.",
            "Last night was your highest-risk window and you made it through. " +
                    "That matters more than you know. Today is day ${streak + 1}."
        )

        NotificationTriggerType.EVENING_CHECK_IN_REMINDER -> Pair(
            "Check in before you sleep.",
            "How did today go? Log your status — your streak is counting on it. $streak days and holding."
        )

        NotificationTriggerType.HIGH_RISK_WINDOW -> Pair(
            "High-risk window active.",
            "Late nights are your hardest hours. You know that. Close the browser. " +
                    "Put the phone down. You have $streak clean days to protect."
        )

        NotificationTriggerType.POST_RELAPSE_COMPASSION -> Pair(
            "You logged a setback. That took courage.",
            "Your streak resets — but your brain's healing doesn't. " +
                    "Pornography rewires neural pathways, and recovery rewires them back. " +
                    "What triggered it? Use that data. Start again today."
        )

        NotificationTriggerType.MILESTONE_CELEBRATION -> Pair(
            "Day ${milestone ?: streak} — something real is happening.",
            milestoneCelebrationBody(milestone ?: streak, "pornography")
        )

        NotificationTriggerType.WEEKLY_PROGRESS_SUMMARY -> Pair(
            "Your week in review.",
            "You have $streak clean days. Every day you didn't give in, " +
                    "your brain rewired a little more. Keep the data. Keep going."
        )
    }

    // ─── Gambling ─────────────────────────────────────────────────────────────

    private fun gamblingMessage(
        type: NotificationTriggerType,
        streak: Int,
        milestone: Int?
    ): Pair<String, String> = when (type) {

        NotificationTriggerType.MORNING_MOTIVATION -> Pair(
            "Good morning.",
            "The house always wins — except on days you don't play. " +
                    "You haven't played in $streak days. That's $streak wins in a row."
        )

        NotificationTriggerType.EVENING_CHECK_IN_REMINDER -> Pair(
            "Check in tonight.",
            "Log how today went. $streak days of data is building a picture of your strength."
        )

        NotificationTriggerType.HIGH_RISK_WINDOW -> Pair(
            "Big game day. High-risk window.",
            "Your highest-risk trigger is active right now. " +
                    "What's your distraction plan for the next 3 hours? " +
                    "You have $streak clean days. Know your exit before you need it."
        )

        NotificationTriggerType.POST_RELAPSE_COMPASSION -> Pair(
            "You logged a setback. Honest admission takes strength.",
            "Gambling activates the same dopamine pathways as hard drugs. " +
                    "This is a hard fight. Your streak resets — your knowledge doesn't. " +
                    "What was the trigger? Write it down."
        )

        NotificationTriggerType.MILESTONE_CELEBRATION -> Pair(
            "Day ${milestone ?: streak} — your brain is changing.",
            milestoneCelebrationBody(milestone ?: streak, "gambling")
        )

        NotificationTriggerType.WEEKLY_PROGRESS_SUMMARY -> Pair(
            "Weekly check: $streak days clean.",
            "Every day away from gambling is a day your dopamine system heals. Keep going."
        )
    }

    // ─── Binge Eating ─────────────────────────────────────────────────────────

    private fun bingeEatingMessage(
        type: NotificationTriggerType,
        streak: Int,
        milestone: Int?
    ): Pair<String, String> = when (type) {

        NotificationTriggerType.MORNING_MOTIVATION -> Pair(
            "Good morning.",
            "Today is about eating to live, not to cope. " +
                    "You've had $streak days of intentional choices. This is one more."
        )

        NotificationTriggerType.EVENING_CHECK_IN_REMINDER -> Pair(
            "Evening check-in.",
            "How did today feel around food? Log it — even the hard days count."
        )

        NotificationTriggerType.HIGH_RISK_WINDOW -> Pair(
            "Evening hours — your highest-risk window.",
            "Stress, boredom, and loneliness peak at night. " +
                    "If you feel the urge, open the SOS button before you open the fridge."
        )

        NotificationTriggerType.POST_RELAPSE_COMPASSION -> Pair(
            "You logged a setback. That's honesty, not failure.",
            "Binge eating is driven by emotion, not hunger. " +
                    "What feeling was underneath it? Understanding the trigger is the work."
        )

        NotificationTriggerType.MILESTONE_CELEBRATION -> Pair(
            "Day ${milestone ?: streak} — your relationship with food is shifting.",
            milestoneCelebrationBody(milestone ?: streak, "binge eating")
        )

        NotificationTriggerType.WEEKLY_PROGRESS_SUMMARY -> Pair(
            "$streak days of intentional eating.",
            "You are learning to separate emotion from food. That is real progress."
        )
    }

    // ─── Gaming ───────────────────────────────────────────────────────────────

    private fun gamingMessage(
        type: NotificationTriggerType,
        streak: Int,
        milestone: Int?
    ): Pair<String, String> = when (type) {

        NotificationTriggerType.MORNING_MOTIVATION -> Pair(
            "Good morning.",
            "You have $streak days of real-world progress. " +
                    "No respawn timers. No loading screens. This streak is permanent."
        )

        NotificationTriggerType.EVENING_CHECK_IN_REMINDER -> Pair(
            "Check in tonight.",
            "Did you stay within your limits today? Log it. $streak days of data is power."
        )

        NotificationTriggerType.HIGH_RISK_WINDOW -> Pair(
            "Evening — your highest-risk gaming window.",
            "The pull to open the game is strongest right now. " +
                    "You have $streak clean days. Step away for 10 minutes first. " +
                    "If the urge is overwhelming, open SOS."
        )

        NotificationTriggerType.POST_RELAPSE_COMPASSION -> Pair(
            "You logged a setback. No judgment here.",
            "Gaming addiction hijacks the brain's reward circuit the same way substances do. " +
                    "What triggered the session? Use that information."
        )

        NotificationTriggerType.MILESTONE_CELEBRATION -> Pair(
            "Day ${milestone ?: streak} — you're reclaiming your time.",
            milestoneCelebrationBody(milestone ?: streak, "gaming")
        )

        NotificationTriggerType.WEEKLY_PROGRESS_SUMMARY -> Pair(
            "$streak days of balanced living.",
            "Every hour not lost to compulsive gaming is an hour invested in your real life."
        )
    }

    // ─── Shopping ─────────────────────────────────────────────────────────────

    private fun shoppingMessage(
        type: NotificationTriggerType,
        streak: Int,
        milestone: Int?
    ): Pair<String, String> = when (type) {

        NotificationTriggerType.MORNING_MOTIVATION -> Pair(
            "Good morning.",
            "$streak days of buying only what you need. " +
                    "Your bank account and your self-respect are both growing."
        )

        NotificationTriggerType.EVENING_CHECK_IN_REMINDER -> Pair(
            "Check in tonight.",
            "How did today go? Logging the hard days is just as important as the good ones."
        )

        NotificationTriggerType.HIGH_RISK_WINDOW -> Pair(
            "Payday or stress trigger detected.",
            "Compulsive shopping spikes when we're stressed, bored, or sad. " +
                    "If you feel the urge, wait 24 hours before any purchase. " +
                    "You have $streak days of discipline to protect."
        )

        NotificationTriggerType.POST_RELAPSE_COMPASSION -> Pair(
            "You logged a setback. Honesty is the hardest part.",
            "Shopping addiction fills emotional gaps with physical objects. " +
                    "The relief is always temporary. What were you feeling before the purchase?"
        )

        NotificationTriggerType.MILESTONE_CELEBRATION -> Pair(
            "Day ${milestone ?: streak} — you're in control of your money.",
            milestoneCelebrationBody(milestone ?: streak, "compulsive shopping")
        )

        NotificationTriggerType.WEEKLY_PROGRESS_SUMMARY -> Pair(
            "$streak days of intentional spending.",
            "Every day you didn't shop compulsively is a day you chose yourself over the habit."
        )
    }

    // ─── Self-Harm ────────────────────────────────────────────────────────────

    private fun selfHarmMessage(
        type: NotificationTriggerType,
        streak: Int,
        milestone: Int?
    ): Pair<String, String> = when (type) {

        NotificationTriggerType.MORNING_MOTIVATION -> Pair(
            "Good morning.",
            "You got through yesterday. That is not nothing — " +
                    "that is $streak days of choosing to stay. We're glad you're here."
        )

        NotificationTriggerType.EVENING_CHECK_IN_REMINDER -> Pair(
            "Check in tonight.",
            "How are you feeling right now? Log your day. " +
                    "If things feel difficult, the SOS button is one tap away."
        )

        NotificationTriggerType.HIGH_RISK_WINDOW -> Pair(
            "Checking in on you.",
            "Sometimes evenings get heavy. If the urge is present, open the SOS button — " +
                    "there's a breathing exercise waiting for you. You are not alone in this."
        )

        NotificationTriggerType.POST_RELAPSE_COMPASSION -> Pair(
            "You logged a setback. Thank you for telling us.",
            "Pain that can't be spoken finds another way out. " +
                    "You are not broken — you are hurting. Please consider speaking to someone you trust. " +
                    "Your streak resets. Your life does not."
        )

        NotificationTriggerType.MILESTONE_CELEBRATION -> Pair(
            "Day ${milestone ?: streak} — you are still here.",
            milestoneCelebrationBody(milestone ?: streak, "self-harm")
        )

        NotificationTriggerType.WEEKLY_PROGRESS_SUMMARY -> Pair(
            "$streak days. We're counting every one.",
            "Every day you chose a different way to cope is a day your nervous system healed a little more."
        )
    }

    // ─── Shared helper ────────────────────────────────────────────────────────

    private fun milestoneCelebrationBody(days: Int, addictionLabel: String): String = when (days) {
        7  -> "One week free from $addictionLabel. Your brain's dopamine receptors are " +
                "already beginning to recalibrate. The intense cravings of days 1–3 are " +
                "measurably weaker now. This is biology confirming what you already know: you can do this."
        30 -> "Thirty days free from $addictionLabel. New neural pathways have formed " +
                "that did not exist a month ago. Sleep is improving. Anxiety is lower. " +
                "Your brain has physically adapted. This is not willpower — this is neuroscience."
        90 -> "Ninety days free from $addictionLabel. Research shows relapse risk drops " +
                "significantly at this stage. The reward system has substantially rewired. " +
                "You are not the same person who started. You are someone stronger."
        else -> "Day $days. Every day you stay clean is a day your brain heals. Keep going."
    }
}