package com.engineerfred.reclaim.core.data.mapper

import com.engineerfred.reclaim.core.data.db.UserEntity
import com.engineerfred.reclaim.core.domain.model.User

/**
 * Maps between the SQLDelight-generated [UserEntity] and the
 * pure-domain [User] model.
 *
 * SQLDelight stores booleans as INTEGER (0 / 1) → Long in Kotlin.
 * All conversions are handled here so neither the domain model
 * nor the data sources need to know about the storage format.
 */
fun UserEntity.toDomain(): User = User(
    id          = id,
    email       = email,
    isAnonymous = isAnonymous == 1L,
    createdAt   = createdAt
)

fun User.toEntity(): UserEntity = UserEntity(
    id          = id,
    email       = email,
    isAnonymous = if (isAnonymous) 1L else 0L,
    createdAt   = createdAt
)