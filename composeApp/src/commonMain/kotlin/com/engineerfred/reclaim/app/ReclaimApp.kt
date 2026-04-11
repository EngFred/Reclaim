package com.engineerfred.reclaim.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.engineerfred.reclaim.app.navigation.BottomNavItem
import com.engineerfred.reclaim.app.onboarding.OnboardingFlowScreen
import com.engineerfred.reclaim.core.ui.navigation.ReclaimRoutes
import com.engineerfred.reclaim.core.ui.theme.ReclaimTheme
import com.engineerfred.reclaim.feature.addiction.presentation.add.AddAddictionScreen
import com.engineerfred.reclaim.feature.addiction.presentation.detail.AddictionDetailScreen
import com.engineerfred.reclaim.feature.auth.presentation.login.LoginScreen
import com.engineerfred.reclaim.feature.auth.presentation.register.RegisterScreen
import com.engineerfred.reclaim.feature.auth.presentation.splash.SplashScreen
import com.engineerfred.reclaim.feature.checkin.presentation.CheckInScreen
import com.engineerfred.reclaim.feature.dashboard.presentation.DashboardScreen
import com.engineerfred.reclaim.feature.dashboard.presentation.pick.PickAddictionScreen
import com.engineerfred.reclaim.feature.progress.presentation.ProgressScreen
import com.engineerfred.reclaim.feature.settings.presentation.SettingsScreen
import com.engineerfred.reclaim.feature.sos.presentation.SosScreen

@Composable
fun ReclaimApp() {
    ReclaimTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val showBottomNav = currentRoute in listOf(
            BottomNavItem.Dashboard.route,
            BottomNavItem.Progress.route,
            BottomNavItem.Sos.route,
            BottomNavItem.Settings.route
        )

        val bottomNavItems = listOf(
            BottomNavItem.Dashboard,
            BottomNavItem.Progress,
            BottomNavItem.Sos,
            BottomNavItem.Settings
        )

        Scaffold(
            bottomBar = {
                if (showBottomNav) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor   = MaterialTheme.colorScheme.onSurface
                    ) {
                        bottomNavItems.forEach { item ->
                            val isSelected = currentRoute?.startsWith(item.route) == true
                            NavigationBarItem(
                                icon     = { Icon(item.icon, contentDescription = item.title) },
                                label    = { Text(item.title) },
                                selected = isSelected,
                                colors   = NavigationBarItemDefaults.colors(
                                    selectedIconColor   = MaterialTheme.colorScheme.onPrimaryContainer,
                                    selectedTextColor   = MaterialTheme.colorScheme.primary,
                                    indicatorColor      = MaterialTheme.colorScheme.primaryContainer,
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                onClick = {
                                    if (!isSelected) {
                                        navController.navigate(item.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState    = true
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController    = navController,
                startDestination = ReclaimRoutes.SPLASH,
                modifier         = Modifier.padding(innerPadding)
            ) {

                // ── Auth ──────────────────────────────────────────────────────
                composable(ReclaimRoutes.SPLASH) {
                    SplashScreen(
                        onNavigate = { route, clear ->
                            navController.navigate(route) {
                                if (clear) popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }
                composable(ReclaimRoutes.LOGIN) {
                    LoginScreen(
                        onNavigate = { route, clear ->
                            navController.navigate(route) {
                                if (clear) popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }
                composable(ReclaimRoutes.REGISTER) {
                    RegisterScreen(
                        onNavigate = { route, clear ->
                            navController.navigate(route) {
                                if (clear) popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }

                // ── Onboarding ────────────────────────────────────────────────
                composable(ReclaimRoutes.WELCOME) {
                    OnboardingFlowScreen(
                        onNavigateToDashboard = {
                            navController.navigate(ReclaimRoutes.DASHBOARD) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }

                // ── Dashboard ─────────────────────────────────────────────────
                composable(ReclaimRoutes.DASHBOARD) {
                    DashboardScreen(
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }

                // ── Addiction ─────────────────────────────────────────────────
                composable(ReclaimRoutes.ADD_ADDICTION) {
                    AddAddictionScreen(
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
                composable(
                    route     = ReclaimRoutes.ADDICTION_DETAIL_TEMPLATE,
                    arguments = listOf(navArgument("addictionId") { type = NavType.StringType })
                ) { entry ->
                    val id = entry.arguments?.getString("addictionId") ?: return@composable
                    AddictionDetailScreen(
                        addictionId    = id,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                // ── Check-In ──────────────────────────────────────────────────
                composable(
                    route     = ReclaimRoutes.CHECK_IN_TEMPLATE,
                    arguments = listOf(navArgument("addictionId") { type = NavType.StringType })
                ) { entry ->
                    val id = entry.arguments?.getString("addictionId") ?: return@composable
                    CheckInScreen(
                        addictionId    = id,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                // ── Progress ──────────────────────────────────────────────────
                // ── Progress ──────────────────────────────────────────────────────────
                composable(BottomNavItem.Progress.route) {
                    PickAddictionScreen(
                        title    = "Progress",
                        subtitle = "Select a journey to view its progress.",
                        onAddictionSelected = { addictionId ->
                            navController.navigate(ReclaimRoutes.progressDetail(addictionId))
                        }
                    )
                }
                composable(
                    route     = ReclaimRoutes.PROGRESS_DETAIL_TEMPLATE,
                    arguments = listOf(navArgument("addictionId") { type = NavType.StringType })
                ) { entry ->
                    val id = entry.arguments?.getString("addictionId") ?: return@composable
                    ProgressScreen(
                        addictionId    = id,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                // ── SOS ───────────────────────────────────────────────────────────────
                composable(BottomNavItem.Sos.route) {
                    PickAddictionScreen(
                        title    = "SOS",
                        subtitle = "Select a journey to get support.",
                        onAddictionSelected = { addictionId ->
                            navController.navigate("sos/$addictionId")
                        }
                    )
                }
                composable(
                    route     = "sos/{addictionId}",
                    arguments = listOf(navArgument("addictionId") { type = NavType.StringType })
                ) { entry ->
                    val id = entry.arguments?.getString("addictionId") ?: return@composable
                    SosScreen(
                        addictionId    = id,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                // ── Settings ──────────────────────────────────────────────────
                composable(ReclaimRoutes.SETTINGS) {
                    SettingsScreen(
                        onNavigateToNotifications = { },
                        onNavigateToLogin = {
                            navController.navigate(ReclaimRoutes.LOGIN) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}
