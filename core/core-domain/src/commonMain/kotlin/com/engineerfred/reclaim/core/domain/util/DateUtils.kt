package com.engineerfred.reclaim.core.domain.util

object DateUtils {

    const val MILLIS_IN_A_DAY = 86_400_000L

    /**
     * Strips the time component from an epoch millis value,
     * returning midnight (00:00:00) of that day in UTC.
     */
    fun toStartOfDayUtc(epochMillis: Long): Long {
        return (epochMillis / MILLIS_IN_A_DAY) * MILLIS_IN_A_DAY
    }

    /**
     * Returns true if two epoch millis values fall on the same UTC day.
     */
    fun isSameDay(epochMillis1: Long, epochMillis2: Long): Boolean {
        return toStartOfDayUtc(epochMillis1) == toStartOfDayUtc(epochMillis2)
    }

    /**
     * Returns true if the given epoch millis is today.
     * Requires the current time to be passed in — keeps this pure and testable.
     */
    fun isToday(epochMillis: Long, nowMillis: Long): Boolean {
        return isSameDay(epochMillis, nowMillis)
    }

    /**
     * Returns the number of full days between two epoch millis values.
     */
    fun daysBetween(startMillis: Long, endMillis: Long): Int {
        val start = toStartOfDayUtc(startMillis)
        val end = toStartOfDayUtc(endMillis)
        return ((end - start) / MILLIS_IN_A_DAY).toInt()
    }

    /**
     * Returns the next milestone day count (7, 30, or 90) after the given streak.
     * Returns null if the user has passed all milestones.
     */
    fun nextMilestoneDays(currentStreak: Int): Int? {
        return listOf(7, 30, 90).firstOrNull { it > currentStreak }
    }

    /**
     * Returns how many days remain until the next milestone.
     */
    fun daysUntilNextMilestone(currentStreak: Int): Int? {
        return nextMilestoneDays(currentStreak)?.let { it - currentStreak }
    }
}