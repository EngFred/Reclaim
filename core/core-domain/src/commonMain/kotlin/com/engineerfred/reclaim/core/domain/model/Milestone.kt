package com.engineerfred.reclaim.core.domain.model

data class Milestone(
    val days: Int,
    val title: String,
    val message: String,
    val achievedAt: Long?
)

object MilestoneDefaults {
    val DAY_7 = Milestone(
        days = 7,
        title = "One Week Clean",
        message = "At 7 days, your brain's dopamine receptors are beginning to recalibrate. " +
                "The intense cravings you felt in days 1–3 are measurably weaker now. " +
                "Your prefrontal cortex — the part responsible for decision-making — " +
                "is starting to regain control. One week is not a small thing. It is neurological proof " +
                "that you are capable of change.",
        achievedAt = null
    )

    val DAY_30 = Milestone(
        days = 30,
        title = "Thirty Days Strong",
        message = "At 30 days, your brain has formed new neural pathways that did not exist a month ago. " +
                "Sleep quality improves. Anxiety levels drop. Many people report clearer thinking and " +
                "stronger emotional regulation at this stage. Your body has physically adapted to life " +
                "without the substance or behavior. Thirty days is not willpower — it is biology working in your favor.",
        achievedAt = null
    )

    val DAY_90 = Milestone(
        days = 90,
        title = "Ninety Days — A New Identity",
        message = "At 90 days, research shows the risk of relapse drops significantly. " +
                "The brain's reward system has substantially rewired. Myelin — the substance that " +
                "reinforces new neural connections — has strengthened the pathways you have been " +
                "building every single day. You are no longer fighting the same battle you were " +
                "fighting on day one. You have become someone different. Someone stronger.",
        achievedAt = null
    )

    val ALL = listOf(DAY_7, DAY_30, DAY_90)

    fun forDays(days: Int): Milestone? = ALL.firstOrNull { it.days == days }
}