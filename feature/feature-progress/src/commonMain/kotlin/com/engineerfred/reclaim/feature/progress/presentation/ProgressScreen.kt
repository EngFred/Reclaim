package com.engineerfred.reclaim.feature.progress.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.engineerfred.reclaim.core.ui.components.CalendarLegend
import com.engineerfred.reclaim.core.ui.components.LoadingOverlay
import com.engineerfred.reclaim.core.ui.components.ReclaimTextButton
import com.engineerfred.reclaim.core.ui.components.StreakDisplay
import com.engineerfred.reclaim.core.ui.theme.Spacing
import com.engineerfred.reclaim.feature.progress.presentation.components.CalendarGridComponent
import com.engineerfred.reclaim.feature.progress.presentation.components.MilestoneCard
import com.engineerfred.reclaim.feature.progress.presentation.components.StatsRow
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProgressScreen(
    addictionId: String,
    onNavigateBack: () -> Unit,
    viewModel: ProgressViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(addictionId) {
        viewModel.loadProgress(addictionId)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            if (event is ProgressEvent.NavigateBack) {
                onNavigateBack()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (!uiState.isLoading && uiState.addiction != null && uiState.streak != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(Spacing.screen)
            ) {
                // Header
                Text(
                    text = "${uiState.addiction!!.displayName} Progress",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(Spacing.xxl))

                // Current & Longest Streak Display (from core-ui)
                StreakDisplay(
                    currentStreak = uiState.streak!!.current,
                    longestStreak = uiState.streak!!.longest
                )
                Spacer(modifier = Modifier.height(Spacing.xxl))

                // Stats Row
                StatsRow(streak = uiState.streak!!)
                Spacer(modifier = Modifier.height(Spacing.xl))

                // Milestone Card
                if (uiState.currentMilestone != null) {
                    MilestoneCard(milestone = uiState.currentMilestone!!)
                    Spacer(modifier = Modifier.height(Spacing.xl))
                }

                // Calendar Section
                Text(
                    text = "Last 90 Days",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(Spacing.md))

                CalendarGridComponent(
                    days = uiState.calendarDays,
                    // Give the grid a fixed height since userScrollEnabled=false inside a scrollable Column
                    modifier = Modifier.height(480.dp)
                )

                Spacer(modifier = Modifier.height(Spacing.md))
                CalendarLegend(modifier = Modifier.fillMaxWidth().padding(bottom = Spacing.xxxl))

                ReclaimTextButton(
                    text = "Go Back",
                    onClick = viewModel::onBackClicked,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }

        if (uiState.isLoading) {
            LoadingOverlay()
        }
    }
}