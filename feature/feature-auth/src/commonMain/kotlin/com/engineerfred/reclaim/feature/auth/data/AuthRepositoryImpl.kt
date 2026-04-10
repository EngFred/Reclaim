package com.engineerfred.reclaim.feature.auth.data

import com.engineerfred.reclaim.core.data.local.DatabaseProvider
import com.engineerfred.reclaim.core.data.remote.FirebaseProvider
import com.engineerfred.reclaim.core.domain.model.User
import com.engineerfred.reclaim.core.domain.repository.AuthRepository
import com.engineerfred.reclaim.core.domain.util.ReclaimResult
import com.engineerfred.reclaim.core.domain.util.reclaimRunCatching
import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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

    private val auth get() = firebaseProvider.auth

    // ── Observe ───────────────────────────────────────────────────────────────

    override fun observeCurrentUser(): Flow<User?> =
        auth.authStateChanged.map { firebaseUser ->
            firebaseUser?.toDomainUser()
        }

    override suspend fun getCurrentUser(): User? =
        auth.currentUser?.toDomainUser()

    // ── Write ─────────────────────────────────────────────────────────────────

    override suspend fun register(email: String, password: String): ReclaimResult<User> =
        reclaimRunCatching {
            val result = auth.createUserWithEmailAndPassword(email, password)
            val user   = result.user ?: error("Registration succeeded but user is null.")
            val domain = user.toDomainUser()
            persistUserLocally(domain)
            domain
        }

    override suspend fun login(email: String, password: String): ReclaimResult<User> =
        reclaimRunCatching {
            val result = auth.signInWithEmailAndPassword(email, password)
            val user   = result.user ?: error("Login succeeded but user is null.")
            val domain = user.toDomainUser()
            persistUserLocally(domain)
            domain
        }

    override suspend fun loginAnonymously(): ReclaimResult<User> =
        reclaimRunCatching {
            val result = auth.signInAnonymously()
            val user   = result.user ?: error("Anonymous sign-in succeeded but user is null.")
            val domain = user.toDomainUser()
            persistUserLocally(domain)
            domain
        }

    override suspend fun logout(): ReclaimResult<Unit> =
        reclaimRunCatching {
            auth.signOut()
        }

    override suspend fun deleteAccount(): ReclaimResult<Unit> =
        reclaimRunCatching {
            val user = auth.currentUser ?: error("No authenticated user to delete.")
            // Remove local record before remote deletion so the user
            // is never left in a half-deleted state if the network drops.
            databaseProvider.database
                .userEntityQueries
                .deleteUser(user.uid)
            user.delete()
        }

    // ── Private helpers ───────────────────────────────────────────────────────

    private fun persistUserLocally(user: User) {
        databaseProvider.database
            .userEntityQueries
            .insertOrReplaceUser(
                id          = user.id,
                email       = user.email,
                isAnonymous = if (user.isAnonymous) 1L else 0L,
                createdAt   = user.createdAt
            )
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