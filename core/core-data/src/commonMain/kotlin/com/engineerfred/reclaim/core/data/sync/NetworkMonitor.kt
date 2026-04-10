package com.engineerfred.reclaim.core.data.sync

import kotlinx.coroutines.flow.Flow

/**
 * expect class: monitors device network connectivity.
 *
 * Android actual uses [ConnectivityManager] + [NetworkCallback].
 * iOS actual uses [NWPathMonitor].
 *
 * [SyncManager] consumes [observeConnectivity] to trigger sync
 * whenever the device comes back online.
 */
expect class NetworkMonitor(context: Any) {
    /**
     * Synchronous snapshot of current connectivity.
     * Prefer [observeConnectivity] for reactive use.
     */
    fun isConnected(): Boolean

    /**
     * Emits true when a network with internet capability is available,
     * false when the device goes offline.
     * Stays active for the lifetime of the SyncManager's coroutine scope.
     */
    fun observeConnectivity(): Flow<Boolean>
}