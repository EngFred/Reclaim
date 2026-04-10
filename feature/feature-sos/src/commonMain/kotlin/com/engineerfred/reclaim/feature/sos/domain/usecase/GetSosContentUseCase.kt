package com.engineerfred.reclaim.feature.sos.domain.usecase

import com.engineerfred.reclaim.feature.sos.domain.repository.SosRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

/**
 * Combines the streak and the note into a single flow for the UI.
 */
class GetSosContentUseCase(
    private val repository: SosRepository
) {
    operator fun invoke(addictionId: String): Flow<SosContent> {
        return combine(
            repository.observeCurrentStreak(addictionId),
            repository.observeWhyIQuitNote(addictionId)
        ) { streak, note ->
            SosContent(streakDays = streak, whyIQuitNote = note)
        }
    }
}

data class SosContent(
    val streakDays: Int,
    val whyIQuitNote: String?
)