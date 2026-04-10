package com.engineerfred.reclaim.core.domain.model

data class User(
    val id: String,
    val email: String?,
    val isAnonymous: Boolean,
    val createdAt: Long
)