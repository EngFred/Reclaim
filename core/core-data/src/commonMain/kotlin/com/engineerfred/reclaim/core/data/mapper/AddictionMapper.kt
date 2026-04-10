package com.engineerfred.reclaim.core.data.mapper

import com.engineerfred.reclaim.core.data.db.AddictionEntity
import com.engineerfred.reclaim.core.domain.model.Addiction
import com.engineerfred.reclaim.core.domain.model.AddictionCategory
import com.engineerfred.reclaim.core.domain.model.AddictionType

/**
 * Maps between [AddictionEntity] (SQLDelight) and [Addiction] (domain).
 *
 * Enums are stored as their string name in SQLite. The mapper converts
 * both ways using [Enum.name] / [enumValueOf].
 *
 * [isSynced] is a data-layer concern only — it does not exist in the
 * domain model. It is handled here when constructing the entity.
 */
fun AddictionEntity.toDomain(): Addiction = Addiction(
    id           = id,
    userId       = userId,
    category     = AddictionCategory.valueOf(category),
    type         = AddictionType.valueOf(type),
    displayName  = displayName,
    startDate    = startDate,
    isActive     = isActive == 1L,
    whyIQuitNote = whyIQuitNote
)

/**
 * Converts a domain [Addiction] to a [AddictionEntity] ready for
 * insertion. [isSynced] defaults to 0 (false) because every new
 * write starts unsynced — the SyncManager marks it synced later.
 */
fun Addiction.toEntity(isSynced: Boolean = false): AddictionEntity = AddictionEntity(
    id           = id,
    userId       = userId,
    category     = category.name,
    type         = type.name,
    displayName  = displayName,
    startDate    = startDate,
    isActive     = if (isActive) 1L else 0L,
    whyIQuitNote = whyIQuitNote,
    isSynced     = if (isSynced) 1L else 0L
)