package com.engineerfred.reclaim.app

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
import com.engineerfred.reclaim.feature.progress.presentation.ProgressScreen
import com.engineerfred.reclaim.feature.settings.presentation.SettingsScreen
import com.engineerfred.reclaim.feature.sos.presentation.SosScreen

@Composable
fun ReclaimApp() {
    ReclaimTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        // Only show bottom nav on main app screens
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
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ) {
                        bottomNavItems.forEach { item ->
                            val isSelected = currentRoute?.startsWith(item.route) == true
                            NavigationBarItem(
                                icon = { Icon(item.icon, contentDescription = item.title) },
                                label = { Text(item.title) },
                                selected = isSelected,
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
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
                                            restoreState = true
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
                navController = navController,
                startDestination = ReclaimRoutes.SPLASH,
                modifier = Modifier.padding(innerPadding)
            ) {
                // ── Auth & Onboarding ─────────────────────────────────────────
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
                composable(ReclaimRoutes.WELCOME) { // Represents the entire onboarding flow
                    OnboardingFlowScreen(
                        onNavigateToDashboard = {
                            navController.navigate(ReclaimRoutes.DASHBOARD) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }

                // ── Dashboard & Addictions ────────────────────────────────────
                composable(ReclaimRoutes.DASHBOARD) {
                    DashboardScreen(
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }
                composable(ReclaimRoutes.ADD_ADDICTION) {
                    AddAddictionScreen(
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
                composable(
                    route = ReclaimRoutes.ADDICTION_DETAIL_TEMPLATE,
                    arguments = listOf(navArgument("addictionId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("addictionId") ?: return@composable
                    AddictionDetailScreen(
                        addictionId = id,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                // ── Check-In ──────────────────────────────────────────────────
                composable(
                    route = ReclaimRoutes.CHECK_IN_TEMPLATE,
                    arguments = listOf(navArgument("addictionId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("addictionId") ?: return@composable
                    CheckInScreen(
                        addictionId = id,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                // ── Progress & SOS (Base routing for Bottom Nav) ──────────────
                // Note: In a fully fleshed out app, clicking Progress/SOS from the bottom nav
                // would open a screen listing all active addictions to choose from.
                // For simplicity here, we assume navigation to these screens happens directly
                // via the Dashboard card which passes the explicit ID.
                composable(
                    route = ReclaimRoutes.PROGRESS_DETAIL_TEMPLATE,
                    arguments = listOf(navArgument("addictionId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("addictionId") ?: return@composable
                    ProgressScreen(
                        addictionId = id,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(
                    route = "sos/{addictionId}",
                    arguments = listOf(navArgument("addictionId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("addictionId") ?: return@composable
                    SosScreen(
                        addictionId = id,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                // ── Settings ──────────────────────────────────────────────────
                composable(ReclaimRoutes.SETTINGS) {
                    SettingsScreen(
                        onNavigateToNotifications = { /* Wire to a NotificationPrefs screen if built */ },
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