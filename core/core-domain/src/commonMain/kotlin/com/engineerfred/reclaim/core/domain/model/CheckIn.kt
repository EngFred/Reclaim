package com.engineerfred.reclaim.core.domain.model

data class CheckIn(
    val id: String,
    val addictionId: String,
    val userId: String,
    val date: Long,
    val status: CheckInStatus,
    val triggerNote: String?,
    val isSynced: Boolean
)