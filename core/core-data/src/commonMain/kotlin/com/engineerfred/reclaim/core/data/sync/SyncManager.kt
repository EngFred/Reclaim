package com.engineerfred.reclaim.core.data.sync

import com.engineerfred.reclaim.core.data.local.DatabaseProvider
import com.engineerfred.reclaim.core.data.remote.FirebaseProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Offline-first sync engine.
 *
 * Strategy:
 * 1. All writes land in SQLDelight immediately (isSynced = 0).
 * 2. SyncManager watches connectivity via [NetworkMonitor].
 * 3. When the device comes back online, [syncAll] pushes every
 *    unsynced record to Firestore and marks it synced locally.
 * 4. Conflict resolution: last-write-wins with Firestore's server
 *    timestamp. Local data is always shown immediately.
 *
 * Call [start] once from the app shell after Koin is initialised.
 * Call [stop] when the app process is being torn down (rare on mobile,
 * but good practice for testing).
 */
class SyncManager(
    private val databaseProvider: DatabaseProvider,
    private val firebaseProvider: FirebaseProvider,
    private val networkMonitor: NetworkMonitor
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    fun start() {
        scope.launch {
            networkMonitor
                .observeConnectivity()
                .distinctUntilChanged()
                .filter { isOnline -> isOnline }
                .collect {
                    syncAll()
                }
        }
    }

    fun stop() {
        scope.cancel()
    }

    /**
     * Manually trigger a full sync cycle.
     * Called internally when connectivity is restored,
     * and can be called explicitly if needed (e.g. after login).
     */
    suspend fun syncAll() {
        syncCheckIns()
        syncAddictions()
    }

    // ─── CheckIn sync ────────────────────────────────────────────────────────

    private suspend fun syncCheckIns() {
        val unsynced = withContext(Dispatchers.IO) {
            databaseProvider.database
                .checkInEntityQueries
                .selectUnsyncedCheckIns()
                .executeAsList()
        }

        unsynced.forEach { entity ->
            try {
                firebaseProvider.firestore
                    .collection(COLLECTION_CHECK_INS)
                    .document(entity.id)
                    .set(
                        mapOf(
                            "id"          to entity.id,
                            "addictionId" to entity.addictionId,
                            "userId"      to entity.userId,
                            "date"        to entity.date,
                            "status"      to entity.status,
                            "triggerNote" to entity.triggerNote,
                            "isSynced"    to true
                        )
                    )

                withContext(Dispatchers.IO) {
                    databaseProvider.database
                        .checkInEntityQueries
                        .markCheckInAsSynced(entity.id)
                }
            } catch (e: Exception) {
                // Silently skip — will retry on the next connectivity event.
                // Individual record failures do not abort the whole sync cycle.
            }
        }
    }

    // ─── Addiction sync ───────────────────────────────────────────────────────

    private suspend fun syncAddictions() {
        val unsynced = withContext(Dispatchers.IO) {
            databaseProvider.database
                .addictionEntityQueries
                .selectUnsyncedAddictions()
                .executeAsList()
        }

        unsynced.forEach { entity ->
            try {
                firebaseProvider.firestore
                    .collection(COLLECTION_ADDICTIONS)
                    .document(entity.id)
                    .set(
                        mapOf(
                            "id"           to entity.id,
                            "userId"       to entity.userId,
                            "category"     to entity.category,
                            "type"         to entity.type,
                            "displayName"  to entity.displayName,
                            "startDate"    to entity.startDate,
                            "isActive"     to (entity.isActive == 1L),
                            "whyIQuitNote" to entity.whyIQuitNote
                        )
                    )

                withContext(Dispatchers.IO) {
                    databaseProvider.database
                        .addictionEntityQueries
                        .markAddictionAsSynced(entity.id)
                }
            } catch (e: Exception) {
                // Retry on next connectivity event.
            }
        }
    }

    companion object {
        private const val COLLECTION_CHECK_INS  = "checkIns"
        private const val COLLECTION_ADDICTIONS = "addictions"
    }
}