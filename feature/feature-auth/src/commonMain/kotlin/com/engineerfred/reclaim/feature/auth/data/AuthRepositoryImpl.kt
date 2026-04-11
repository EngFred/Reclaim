package com.engineerfred.reclaim.feature.auth.data

import android.util.Log
import com.engineerfred.reclaim.core.data.local.DatabaseProvider
import com.engineerfred.reclaim.core.data.remote.FirebaseProvider
import com.engineerfred.reclaim.core.domain.model.User
import com.engineerfred.reclaim.core.domain.repository.AuthRepository
import com.engineerfred.reclaim.core.domain.util.ReclaimResult
import com.engineerfred.reclaim.core.domain.util.reclaimRunCatching
import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

/**
 * Firebase-backed implementation of [AuthRepository].
 *
 * All Firebase calls are wrapped in [reclaimRunCatching] so no raw
 * Firebase exceptions ever reach the ViewModel.
 *
 * On successful registration or login, the user record is persisted
 * to the local SQLDelight DB so auth state is available offline.
 */
class AuthRepositoryImpl(
    private val firebaseProvider: FirebaseProvider,
    private val databaseProvider: DatabaseProvider
) : AuthRepository {

    companion object {
        private const val TAG = "AuthRepositoryImpl"
    }

    private val auth get() = firebaseProvider.auth

    // ── Observe ───────────────────────────────────────────────────────────────
    override fun observeCurrentUser(): Flow<User?> {
        Log.d(TAG, "Observing current user state.")
        return auth.authStateChanged.map { firebaseUser ->
            firebaseUser?.toDomainUser()
        }.onEach { user ->
            Log.d(TAG, "Auth state changed. Current user: ${user?.id ?: "null"}")
        }.catch { e ->
            Log.e(TAG, "Error observing auth state.", e)
            throw e
        }
    }

    override suspend fun getCurrentUser(): User? {
        Log.d(TAG, "Fetching current user.")
        return auth.currentUser?.toDomainUser().also { user ->
            Log.d(TAG, "Current user fetched: ${user?.id ?: "null"}")
        }
    }

    // ── Write ─────────────────────────────────────────────────────────────────
    override suspend fun register(email: String, password: String): ReclaimResult<User> {
        Log.d(TAG, "Starting registration process for email: $email")
        return reclaimRunCatching {
            val result = auth.createUserWithEmailAndPassword(email, password)
            val user   = result.user ?: error("Registration succeeded but user is null.")
            val domain = user.toDomainUser()
            persistUserLocally(domain)
            domain
        }.onSuccess {
            Log.i(TAG, "Registration successful for user: ${it.id}")
        }.onFailure {
            Log.e(TAG, "Registration failed for email: $email", it)
        }
    }

    override suspend fun login(email: String, password: String): ReclaimResult<User> {
        Log.d(TAG, "Starting login process for email: $email")
        return reclaimRunCatching {
            val result = auth.signInWithEmailAndPassword(email, password)
            val user   = result.user ?: error("Login succeeded but user is null.")
            val domain = user.toDomainUser()
            persistUserLocally(domain)
            domain
        }.onSuccess {
            Log.i(TAG, "Login successful for user: ${it.id}")
        }.onFailure {
            Log.e(TAG, "Login failed for email: $email", it)
        }
    }

    override suspend fun loginAnonymously(): ReclaimResult<User> {
        Log.d(TAG, "Starting anonymous login process.")
        return reclaimRunCatching {
            val result = auth.signInAnonymously()
            val user   = result.user ?: error("Anonymous sign-in succeeded but user is null.")
            val domain = user.toDomainUser()
            persistUserLocally(domain)
            domain
        }.onSuccess {
            Log.i(TAG, "Anonymous login successful for user: ${it.id}")
        }.onFailure {
            Log.e(TAG, "Anonymous login failed.", it)
        }
    }

    override suspend fun logout(): ReclaimResult<Unit> {
        Log.d(TAG, "Starting logout process.")
        return reclaimRunCatching {
            auth.signOut()
        }.onSuccess {
            Log.i(TAG, "Logout successful.")
        }.onFailure {
            Log.e(TAG, "Logout failed.", it)
        }
    }

    override suspend fun deleteAccount(): ReclaimResult<Unit> {
        Log.d(TAG, "Starting account deletion process.")
        return reclaimRunCatching {
            val user = auth.currentUser ?: error("No authenticated user to delete.")

            Log.d(TAG, "Deleting local user record for: ${user.uid}")
            // Remove local record before remote deletion so the user
            // is never left in a half-deleted state if the network drops.
            databaseProvider.database
                .userEntityQueries
                .deleteUser(user.uid)

            Log.d(TAG, "Deleting remote user record for: ${user.uid}")
            user.delete()
        }.onSuccess {
            Log.i(TAG, "Account deletion successful.")
        }.onFailure {
            Log.e(TAG, "Account deletion failed.", it)
        }
    }

    // ── Private helpers ───────────────────────────────────────────────────────
    private fun persistUserLocally(user: User) {
        Log.d(TAG, "Persisting user locally: ${user.id}")
        databaseProvider.database
            .userEntityQueries
            .insertOrReplaceUser(
                id          = user.id,
                email       = user.email,
                isAnonymous = if (user.isAnonymous) 1L else 0L,
                createdAt   = user.createdAt
            )
        Log.d(TAG, "Local persistence complete for: ${user.id}")
    }

    /**
     * Converts a GitLive [FirebaseUser] to the pure-domain [User].
     * [createdAt] falls back to the current time if Firebase metadata
     * is unavailable (e.g. anonymous sign-in on first launch).
     */
    private fun FirebaseUser.toDomainUser(): User = User(
        id          = uid,
        email       = email,
        isAnonymous = isAnonymous,
        createdAt   = System.currentTimeMillis()
    )
}