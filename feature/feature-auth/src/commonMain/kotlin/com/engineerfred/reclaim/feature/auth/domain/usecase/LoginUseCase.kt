package com.engineerfred.reclaim.feature.auth.domain.usecase

import com.engineerfred.reclaim.core.domain.model.User
import com.engineerfred.reclaim.core.domain.repository.AuthRepository
import com.engineerfred.reclaim.core.domain.util.ReclaimResult

/**
 * Authenticates an existing user with email and password.
 *
 * Validates input before touching the repository so the data layer
 * never receives blank credentials.
 */
class LoginUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(
        email: String,
        password: String
    ): ReclaimResult<User> {
        val trimmedEmail    = email.trim()
        val trimmedPassword = password.trim()

        if (trimmedEmail.isBlank()) {
            return ReclaimResult.Failure(IllegalArgumentException("Email cannot be empty."))
        }
        if (!trimmedEmail.contains('@')) {
            return ReclaimResult.Failure(IllegalArgumentException("Enter a valid email address."))
        }
        if (trimmedPassword.isBlank()) {
            return ReclaimResult.Failure(IllegalArgumentException("Password cannot be empty."))
        }
        if (trimmedPassword.length < 6) {
            return ReclaimResult.Failure(IllegalArgumentException("Password must be at least 6 characters."))
        }

        return repository.login(trimmedEmail, trimmedPassword)
    }
}