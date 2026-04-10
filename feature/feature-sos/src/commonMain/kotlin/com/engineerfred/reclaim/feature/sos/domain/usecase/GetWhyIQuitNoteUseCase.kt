package com.engineerfred.reclaim.feature.sos.domain.usecase

import com.engineerfred.reclaim.feature.sos.domain.repository.SosRepository
import kotlinx.coroutines.flow.Flow

class GetWhyIQuitNoteUseCase(
    private val repository: SosRepository
) {
    operator fun invoke(addictionId: String): Flow<String?> {
        return repository.observeWhyIQuitNote(addictionId)
    }
}