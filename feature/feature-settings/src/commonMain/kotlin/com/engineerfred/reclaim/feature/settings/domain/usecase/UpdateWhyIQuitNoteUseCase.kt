package com.engineerfred.reclaim.feature.settings.domain.usecase

import com.engineerfred.reclaim.core.domain.repository.AddictionRepository
import com.engineerfred.reclaim.core.domain.util.ReclaimResult

class UpdateWhyIQuitNoteUseCase(
    private val addictionRepository: AddictionRepository
) {
    suspend operator fun invoke(addictionId: String, note: String): ReclaimResult<Unit> {
        if (note.length > 500) {
            return ReclaimResult.Failure(IllegalArgumentException("Note is too long. Please keep it under 500 characters."))
        }
        return addictionRepository.updateWhyIQuitNote(addictionId, note)
    }
}