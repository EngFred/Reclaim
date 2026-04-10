package com.engineerfred.reclaim.core.data.sync

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import platform.Network.NWPathMonitor
import platform.Network.nw_path_status_satisfied
import platform.darwin.dispatch_get_main_queue

actual class NetworkMonitor actual constructor(context: Any) {

    private val monitor = NWPathMonitor()
    private val _connectivity = MutableStateFlow(false)

    init {
        monitor.setUpdateHandler { path ->
            _connectivity.value = path.status == nw_path_status_satisfied
        }
        monitor.setQueue(dispatch_get_main_queue())
        monitor.start()
    }

    actual fun isConnected(): Boolean = _connectivity.value

    actual fun observeConnectivity(): Flow<Boolean> = _connectivity.asStateFlow()
}