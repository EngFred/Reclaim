package com.engineerfred.reclaim.feature.dashboard.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.engineerfred.reclaim.core.ui.components.LoadingOverlay
import com.engineerfred.reclaim.core.ui.components.ReclaimPrimaryButton
import com.engineerfred.reclaim.core.ui.navigation.ReclaimRoutes
import com.engineerfred.reclaim.core.ui.theme.Spacing
import com.engineerfred.reclaim.feature.dashboard.presentation.components.AddictionSummaryCard
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DashboardScreen(
    onNavigate: (route: String) -> Unit,
    viewModel: DashboardViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is DashboardEvent.NavigateToCheckIn -> {
                    onNavigate(ReclaimRoutes.checkIn(event.addictionId))
                }
                is DashboardEvent.NavigateToAddictionDetail -> {
                    onNavigate(ReclaimRoutes.addictionDetail(event.addictionId))
                }
                DashboardEvent.NavigateToAddAddiction -> {
                    onNavigate(ReclaimRoutes.ADD_ADDICTION)
                }
            }
        }
    }

    if (uiState.isLoading) {
        LoadingOverlay()
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = Spacing.screen,
            end = Spacing.screen,
            top = Spacing.xxxl,
            bottom = Spacing.huge // padding for bottom nav bar
        ),
        verticalArrangement = Arrangement.spacedBy(Spacing.lg)
    ) {
        // ── Header ──────────────────────────────────────────────────────────
        item {
            Column(modifier = Modifier.padding(bottom = Spacing.md)) {
                Text(
                    text = uiState.greeting,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(Spacing.xs))
                Text(
                    text = "Here is your progress.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // ── Empty State ─────────────────────────────────────────────────────
        if (uiState.activeAddictions.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(top = Spacing.huge),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "You have no active journeys.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(Spacing.lg))
                    ReclaimPrimaryButton(
                        text = "Add an Addiction",
                        onClick = viewModel::onAddAddictionClicked
                    )
                }
            }
        }

        // ── Active Addictions ───────────────────────────────────────────────
        items(
            items = uiState.activeAddictions,
            key = { it.addiction.id } // Stable keys for optimal recomposition
        ) { item ->
            AddictionSummaryCard(
                item = item,
                onClick = { viewModel.onAddictionCardClicked(item.addiction.id) },
                onCheckInClick = { viewModel.onCheckInClicked(item.addiction.id) }
            )
        }
    }
}