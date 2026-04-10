package com.engineerfred.reclaim.feature.auth.domain.usecase

import com.engineerfred.reclaim.core.domain.repository.AuthRepository
import com.engineerfred.reclaim.core.domain.util.ReclaimResult

/**
 * Signs out the current user.
 * Delegates entirely to the repository — no input to validate.
 */
class LogoutUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(): ReclaimResult<Unit> = repository.logout()
}