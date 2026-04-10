package com.engineerfred.reclaim.core.domain.model

data class Streak(
    val addictionId: String,
    val current: Int,
    val longest: Int,
    val totalDays: Int,
    val successRate: Float
)