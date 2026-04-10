package com.engineerfred.reclaim.core.domain.model

data class Addiction(
    val id: String,
    val userId: String,
    val category: AddictionCategory,
    val type: AddictionType,
    val displayName: String,
    val startDate: Long,
    val isActive: Boolean,
    val whyIQuitNote: String?
)