package com.engineerfred.reclaim.feature.auth.domain.usecase

import com.engineerfred.reclaim.core.domain.model.User
import com.engineerfred.reclaim.core.domain.repository.AuthRepository
import com.engineerfred.reclaim.core.domain.util.ReclaimResult

/**
 * Registers a new user with email and password.
 *
 * Validates that email is well-formed, password meets the minimum length,
 * and confirmation matches — before any network call is made.
 * All messages are user-friendly, not technical.
 */
class RegisterUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(
        email: String,
        password: String,
        confirmPassword: String
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
            return ReclaimResult.Failure(IllegalArgumentException("Please choose a password."))
        }
        if (trimmedPassword.length < 6) {
            return ReclaimResult.Failure(IllegalArgumentException("Password must be at least 6 characters long."))
        }
        if (trimmedPassword != confirmPassword.trim()) {
            return ReclaimResult.Failure(IllegalArgumentException("Passwords don't match. Please try again."))
        }

        return repository.register(trimmedEmail, trimmedPassword)
    }
}