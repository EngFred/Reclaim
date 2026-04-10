package com.engineerfred.reclaim.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector
import com.engineerfred.reclaim.core.ui.navigation.ReclaimRoutes

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    data object Dashboard : BottomNavItem(ReclaimRoutes.DASHBOARD, "Home", Icons.Default.Home)
    // We use standard base routes for the nav bar to keep it active, even if the actual route needs an ID
    data object Progress : BottomNavItem("progress_base", "Progress", Icons.Default.Timeline)
    data object Sos : BottomNavItem("sos_base", "SOS", Icons.Default.Warning)
    data object Settings : BottomNavItem(ReclaimRoutes.SETTINGS, "Settings", Icons.Default.Settings)
}