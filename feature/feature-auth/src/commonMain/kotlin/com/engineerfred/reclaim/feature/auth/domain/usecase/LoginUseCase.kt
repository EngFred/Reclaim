package com.engineerfred.reclaim.feature.auth.domain.usecase

import com.engineerfred.reclaim.core.domain.model.User
import com.engineerfred.reclaim.core.domain.repository.AuthRepository
import com.engineerfred.reclaim.core.domain.util.ReclaimResult

/**
 * Authenticates an existing user with email and password.
 *
 * Only validates that fields are non-blank and email is well-formed.
 * We deliberately do NOT enforce password length here — the user
 * already set their password at registration time. Enforcing length
 * on login would lock out users whose passwords were set under a
 * different policy.
 */
class LoginUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(
        email: String,
        password: String
    ): ReclaimResult<User> {
        val trimmedEmail    = email.trim()
        val trimmedPassword = password.trim()

        if (trimmedEmail.isBlank()) {
            return ReclaimResult.Failure(IllegalArgumentException("Please enter your email address."))
        }
        if (!trimmedEmail.contains('@')) {
            return ReclaimResult.Failure(IllegalArgumentException("That doesn't look like a valid email address."))
        }
        if (trimmedPassword.isBlank()) {
            return ReclaimResult.Failure(IllegalArgumentException("Please enter your password."))
        }

        return repository.login(trimmedEmail, trimmedPassword)
    }
}