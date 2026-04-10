package com.engineerfred.reclaim.core.data.sync

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

actual class NetworkMonitor actual constructor(context: Any) {

    private val connectivityManager: ConnectivityManager =
        (context as Context).getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
    actual fun isConnected(): Boolean {
        val network      = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
    actual fun observeConnectivity(): Flow<Boolean> = callbackFlow {
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            .build()

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(true)
            }
            override fun onLost(network: Network) {
                trySend(false)
            }
            override fun onUnavailable() {
                trySend(false)
            }
        }

        connectivityManager.registerNetworkCallback(request, callback)

        // Emit the current state immediately so collectors don't have to wait
        // for the next network event to know the starting connectivity state.
        trySend(isConnected())

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }
}