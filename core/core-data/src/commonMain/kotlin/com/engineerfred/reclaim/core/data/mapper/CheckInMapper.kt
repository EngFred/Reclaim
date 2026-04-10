package com.engineerfred.reclaim.core.data.mapper

import com.engineerfred.reclaim.core.data.db.CheckInEntity
import com.engineerfred.reclaim.core.domain.model.CheckIn
import com.engineerfred.reclaim.core.domain.model.CheckInStatus

/**
 * Maps between [CheckInEntity] (SQLDelight) and [CheckIn] (domain).
 *
 * [isSynced] is modelled as a Boolean in the domain but stored as
 * INTEGER (0/1) in SQLite — the mapper owns this conversion.
 */
fun CheckInEntity.toDomain(): CheckIn = CheckIn(
    id          = id,
    addictionId = addictionId,
    userId      = userId,
    date        = date,
    status      = CheckInStatus.valueOf(status),
    triggerNote = triggerNote,
    isSynced    = isSynced == 1L
)

fun CheckIn.toEntity(): CheckInEntity = CheckInEntity(
    id          = id,
    addictionId = addictionId,
    userId      = userId,
    date        = date,
    status      = status.name,
    triggerNote = triggerNote,
    isSynced    = if (isSynced) 1L else 0L
)