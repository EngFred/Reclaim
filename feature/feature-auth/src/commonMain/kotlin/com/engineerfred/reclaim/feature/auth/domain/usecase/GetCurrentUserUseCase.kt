package com.engineerfred.reclaim.feature.auth.domain.usecase

import com.engineerfred.reclaim.core.domain.model.User
import com.engineerfred.reclaim.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

/**
 * Observes the currently authenticated user as a reactive stream.
 *
 * Emits null when no user is signed in.
 * Used by SplashViewModel to decide the initial navigation destination.
 */
class GetCurrentUserUseCase(private val repository: AuthRepository) {

    operator fun invoke(): Flow<User?> = repository.observeCurrentUser()
}