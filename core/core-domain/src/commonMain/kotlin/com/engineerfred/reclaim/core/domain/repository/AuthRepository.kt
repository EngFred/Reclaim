package com.engineerfred.reclaim.core.domain.repository

import com.engineerfred.reclaim.core.domain.model.User
import com.engineerfred.reclaim.core.domain.util.ReclaimResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    /**
     * Emits the currently signed-in user, or null if signed out.
     * Stays active and emits on auth state changes.
     */
    fun observeCurrentUser(): Flow<User?>

    /**
     * Returns the current user once, without observing.
     */
    suspend fun getCurrentUser(): User?

    /**
     * Registers a new user with email and password.
     */
    suspend fun register(email: String, password: String): ReclaimResult<User>

    /**
     * Signs in an existing user with email and password.
     */
    suspend fun login(email: String, password: String): ReclaimResult<User>

    /**
     * Signs in anonymously for privacy-sensitive users.
     */
    suspend fun loginAnonymously(): ReclaimResult<User>

    /**
     * Signs out the current user.
     */
    suspend fun logout(): ReclaimResult<Unit>

    /**
     * Permanently deletes the current user's account and all associated data.
     */
    suspend fun deleteAccount(): ReclaimResult<Unit>
}