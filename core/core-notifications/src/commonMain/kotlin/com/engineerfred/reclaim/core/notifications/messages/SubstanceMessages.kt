package com.engineerfred.reclaim.core.notifications.messages

import com.engineerfred.reclaim.core.domain.model.AddictionCategory
import com.engineerfred.reclaim.core.notifications.model.NotificationTriggerType

/**
 * Pre-written notification messages for SUBSTANCE addictions.
 */
object SubstanceMessages {

    fun get(
        category: AddictionCategory,
        triggerType: NotificationTriggerType,
        streakDays: Int = 0,
        milestoneDays: Int? = null
    ): Pair<String, String> = when (category) {

        AddictionCategory.ALCOHOL           -> alcoholMessage(triggerType, streakDays, milestoneDays)
        AddictionCategory.COCAINE           -> cocaineMessage(triggerType, streakDays, milestoneDays)
        AddictionCategory.MARIJUANA         -> marijuanaMessage(triggerType, streakDays, milestoneDays)
        AddictionCategory.NICOTINE          -> nicotineMessage(triggerType, streakDays, milestoneDays)
        AddictionCategory.PRESCRIPTION_DRUGS -> prescriptionMessage(triggerType, streakDays, milestoneDays)
        else -> throw IllegalArgumentException(
            "SubstanceMessages called with non-substance category: $category"
        )
    }

    // ─── Alcohol ──────────────────────────────────────────────────────────────

    private fun alcoholMessage(
        type: NotificationTriggerType,
        streak: Int,
        milestone: Int?
    ): Pair<String, String> = when (type) {

        NotificationTriggerType.MORNING_MOTIVATION -> Pair(
            "Good morning — clear-headed.",
            "You woke up clear-headed for the ${streak}th day in a row. " +
                    "Your liver, your sleep, and your relationships all notice."
        )

        NotificationTriggerType.EVENING_CHECK_IN_REMINDER -> Pair(
            "Check in tonight.",
            "Log your day before you sleep. $streak days of honest data is your most powerful tool."
        )

        NotificationTriggerType.HIGH_RISK_WINDOW -> Pair(
            "It's Friday evening. Peer pressure peaks tonight.",
            "You have $streak clean days — know your plan before you need it. " +
                    "Identify the exit before you walk into the room."
        )

        NotificationTriggerType.POST_RELAPSE_COMPASSION -> Pair(
            "You logged a setback. That took courage to admit.",
            "Alcohol withdrawal is one of the most physically demanding recoveries there is. " +
                    "Your streak resets, but your knowledge doesn't. What triggered it? Use that data."
        )

        NotificationTriggerType.MILESTONE_CELEBRATION -> Pair(
            "Day ${milestone ?: streak} — your body is healing.",
            milestoneCelebrationBody(milestone ?: streak, "alcohol")
        )

        NotificationTriggerType.WEEKLY_PROGRESS_SUMMARY -> Pair(
            "$streak days alcohol-free.",
            "One week of clear mornings. Your cortisol is dropping. " +
                    "Your REM sleep is returning. Your body is repairing itself."
        )
    }

    // ─── Cocaine / Hard Drugs ─────────────────────────────────────────────────

    private fun cocaineMessage(
        type: NotificationTriggerType,
        streak: Int,
        milestone: Int?
    ): Pair<String, String> = when (type) {

        NotificationTriggerType.MORNING_MOTIVATION -> Pair(
            "Good morning.",
            "$streak days clean. The dopamine crash that once drove you back is weakening. " +
                    "Your brain is producing its own rewards again."
        )

        NotificationTriggerType.EVENING_CHECK_IN_REMINDER -> Pair(
            "Check in tonight.",
            "Log your day. Every entry is data about your triggers — and your strength."
        )

        NotificationTriggerType.HIGH_RISK_WINDOW -> Pair(
            "High-risk moment detected.",
            "Social environments, stress, and certain people are your strongest triggers. " +
                    "You have $streak days. If the pull is strong right now, open SOS."
        )

        NotificationTriggerType.POST_RELAPSE_COMPASSION -> Pair(
            "You logged a setback. That took courage to admit.",
            "Your streak resets, but your knowledge doesn't. " +
                    "What triggered it? Use that data. " +
                    "Cocaine recovery is one of the hardest. You being here at all says something."
        )

        NotificationTriggerType.MILESTONE_CELEBRATION -> Pair(
            "Day ${milestone ?: streak} — your brain chemistry is shifting.",
            milestoneCelebrationBody(milestone ?: streak, "cocaine")
        )

        NotificationTriggerType.WEEKLY_PROGRESS_SUMMARY -> Pair(
            "$streak days clean from cocaine.",
            "Every clean day, your prefrontal cortex reclaims a little more control from the addiction."
        )
    }

    // ─── Marijuana ────────────────────────────────────────────────────────────

    private fun marijuanaMessage(
        type: NotificationTriggerType,
        streak: Int,
        milestone: Int?
    ): Pair<String, String> = when (type) {

        NotificationTriggerType.MORNING_MOTIVATION -> Pair(
            "Good morning — present and alert.",
            "$streak days without the fog. Your memory, motivation, and emotional clarity " +
                    "are all returning. Notice the difference."
        )

        NotificationTriggerType.EVENING_CHECK_IN_REMINDER -> Pair(
            "Check in tonight.",
            "Log your day. Notice: is the evening anxiety easing? $streak days in, it should be."
        )

        NotificationTriggerType.HIGH_RISK_WINDOW -> Pair(
            "Evening — highest-risk window for relapse.",
            "Boredom and social pressure spike at night. " +
                    "You have $streak days. The urge will pass in 15–20 minutes. Outlast it."
        )

        NotificationTriggerType.POST_RELAPSE_COMPASSION -> Pair(
            "You logged a setback. Honest logging is hard.",
            "Marijuana dependency is often emotional — it fills a gap. " +
                    "What were you feeling before you used? That's the real work."
        )

        NotificationTriggerType.MILESTONE_CELEBRATION -> Pair(
            "Day ${milestone ?: streak} — the fog is lifting.",
            milestoneCelebrationBody(milestone ?: streak, "marijuana")
        )

        NotificationTriggerType.WEEKLY_PROGRESS_SUMMARY -> Pair(
            "$streak days clear.",
            "Motivation and short-term memory improve measurably in the first 30 days. " +
                    "You're in that window now."
        )
    }

    // ─── Nicotine / Vaping ────────────────────────────────────────────────────

    private fun nicotineMessage(
        type: NotificationTriggerType,
        streak: Int,
        milestone: Int?
    ): Pair<String, String> = when (type) {

        NotificationTriggerType.MORNING_MOTIVATION -> Pair(
            "Good morning.",
            "Day $streak nicotine-free. Your lungs began healing within 24 hours of quitting. " +
                    "By now, your circulation is measurably improving."
        )

        NotificationTriggerType.EVENING_CHECK_IN_REMINDER -> Pair(
            "Check in tonight.",
            "Log your day. Even cravings that you resisted count — they're proof you're stronger."
        )

        NotificationTriggerType.HIGH_RISK_WINDOW -> Pair(
            "Craving window active.",
            "Nicotine cravings peak at 3–5 minutes and then drop. " +
                    "You have $streak days. Time this one. It will pass."
        )

        NotificationTriggerType.POST_RELAPSE_COMPASSION -> Pair(
            "You logged a setback.",
            "Nicotine is one of the most addictive substances known. " +
                    "A slip is not a failure — it's data. " +
                    "Most people quit successfully after multiple attempts. You are still in this."
        )

        NotificationTriggerType.MILESTONE_CELEBRATION -> Pair(
            "Day ${milestone ?: streak} — your lungs are healing.",
            milestoneCelebrationBody(milestone ?: streak, "nicotine")
        )

        NotificationTriggerType.WEEKLY_PROGRESS_SUMMARY -> Pair(
            "$streak nicotine-free days.",
            "Carbon monoxide levels in your blood are normal. " +
                    "Cilia in your airways are regrowing. Your body is actively repairing itself."
        )
    }

    // ─── Prescription Drug Abuse ──────────────────────────────────────────────

    private fun prescriptionMessage(
        type: NotificationTriggerType,
        streak: Int,
        milestone: Int?
    ): Pair<String, String> = when (type) {

        NotificationTriggerType.MORNING_MOTIVATION -> Pair(
            "Good morning.",
            "$streak days of using medication only as prescribed — or none at all. " +
                    "Your nervous system is recalibrating. That discomfort is temporary."
        )

        NotificationTriggerType.EVENING_CHECK_IN_REMINDER -> Pair(
            "Check in tonight.",
            "Log your day. Prescription recovery is rarely linear — every honest entry matters."
        )

        NotificationTriggerType.HIGH_RISK_WINDOW -> Pair(
            "Stress or pain spike — high-risk window.",
            "Physical pain and emotional stress are your highest triggers. " +
                    "You have $streak days. If you feel the pull, open SOS before you act."
        )

        NotificationTriggerType.POST_RELAPSE_COMPASSION -> Pair(
            "You logged a setback. That honesty is everything.",
            "Prescription dependency often starts from genuine pain or anxiety. " +
                    "This is not a character flaw. Your streak resets — your healing doesn't stop."
        )

        NotificationTriggerType.MILESTONE_CELEBRATION -> Pair(
            "Day ${milestone ?: streak} — your body is adapting.",
            milestoneCelebrationBody(milestone ?: streak, "prescription drug misuse")
        )

        NotificationTriggerType.WEEKLY_PROGRESS_SUMMARY -> Pair(
            "$streak days clean.",
            "Your brain's GABA or opioid receptors are slowly rebuilding sensitivity. " +
                    "The discomfort of early recovery is proof that healing is happening."
        )
    }

    // ─── Shared helper ────────────────────────────────────────────────────────

    private fun milestoneCelebrationBody(days: Int, addictionLabel: String): String = when (days) {
        7  -> "Seven days free from $addictionLabel. Your brain has begun rebuilding the " +
                "receptor sensitivity that the substance was suppressing. " +
                "The worst withdrawal symptoms are almost certainly behind you now."
        30 -> "Thirty days free from $addictionLabel. New synaptic connections have formed. " +
                "Your body is producing natural neurochemicals at levels it hasn't in months or years. " +
                "Thirty days is not a small thing. It is a biological transformation."
        90 -> "Ninety days free from $addictionLabel. The research is clear: at 90 days, " +
                "the risk of relapse has dropped significantly and new neural pathways have stabilised. " +
                "You built this. One day at a time."
        else -> "Day $days clean from $addictionLabel. Keep going. Every day matters."
    }
}