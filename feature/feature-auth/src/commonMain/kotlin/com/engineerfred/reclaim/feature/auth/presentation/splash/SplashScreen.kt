package com.engineerfred.reclaim.feature.auth.presentation.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.engineerfred.reclaim.core.ui.navigation.ReclaimRoutes
import com.engineerfred.reclaim.core.ui.theme.Spacing
import org.koin.compose.viewmodel.koinViewModel

/**
 * Splash screen — shown only for the fraction of a second it takes
 * to read the first auth state emission from [SplashViewModel].
 *
 * Does NOT use a fixed delay. Navigates the moment [SplashDestination]
 * resolves to anything other than [SplashDestination.Undecided].
 *
 * @param onNavigate Callback that the :app nav host uses to push the
 *                   resolved route. Receives the target route string.
 */
@Composable
fun SplashScreen(
    onNavigate: (route: String, clearBackStack: Boolean) -> Unit,
    viewModel: SplashViewModel = koinViewModel()
) {
    val destination by viewModel.destination.collectAsStateWithLifecycle()

    LaunchedEffect(destination) {
        when (destination) {
            SplashDestination.Login     -> onNavigate(ReclaimRoutes.LOGIN, true)
            SplashDestination.Dashboard -> onNavigate(ReclaimRoutes.DASHBOARD, true)
            SplashDestination.Undecided -> Unit // still loading — stay put
        }
    }

    // Minimal brand splash while auth resolves.
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text  = "RECLAIM",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color      = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(Spacing.sm))
            Text(
                text  = "Reclaim your life, one day at a time.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}