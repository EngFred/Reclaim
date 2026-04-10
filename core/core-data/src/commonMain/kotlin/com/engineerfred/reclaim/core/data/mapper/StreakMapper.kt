package com.engineerfred.reclaim.core.data.mapper

import com.engineerfred.reclaim.core.data.db.StreakEntity
import com.engineerfred.reclaim.core.domain.model.Streak

/**
 * Maps between [StreakEntity] (SQLDelight) and [Streak] (domain).
 *
 * SQLDelight maps INTEGER → Long and REAL → Double.
 * The domain model uses Int and Float respectively.
 * Conversions are explicit here to avoid silent precision loss.
 */
fun StreakEntity.toDomain(): Streak = Streak(
    addictionId = addictionId,
    current     = current.toInt(),
    longest     = longest.toInt(),
    totalDays   = totalDays.toInt(),
    successRate = successRate.toFloat()
)

fun Streak.toEntity(): StreakEntity = StreakEntity(
    addictionId = addictionId,
    current     = current.toLong(),
    longest     = longest.toLong(),
    totalDays   = totalDays.toLong(),
    successRate = successRate.toDouble()
)