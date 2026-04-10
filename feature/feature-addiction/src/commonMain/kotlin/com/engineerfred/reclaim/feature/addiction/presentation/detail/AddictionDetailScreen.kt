package com.engineerfred.reclaim.feature.addiction.presentation.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.engineerfred.reclaim.core.domain.util.DateUtils
import com.engineerfred.reclaim.core.ui.components.LoadingOverlay
import com.engineerfred.reclaim.core.ui.components.ReclaimCard
import com.engineerfred.reclaim.core.ui.components.ReclaimOutlinedButton
import com.engineerfred.reclaim.core.ui.components.ReclaimPrimaryButton
import com.engineerfred.reclaim.core.ui.theme.Spacing
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddictionDetailScreen(
    addictionId: String,
    onNavigateBack: () -> Unit,
    viewModel: AddictionDetailViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Trigger data load on initial composition
    LaunchedEffect(addictionId) {
        viewModel.loadAddiction(addictionId)
    }

    // Handle one-time events
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                AddictionDetailEvent.NavigateBack -> onNavigateBack()
                is AddictionDetailEvent.ShowToast -> { /* Handle Snackbar */ }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        val addiction = uiState.addiction

        if (addiction != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(Spacing.screen)
            ) {
                Text(
                    text = addiction.displayName,
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(Spacing.xs))
                Text(
                    text = "Started ${DateUtils.daysBetween(addiction.startDate, System.currentTimeMillis())} days ago",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(Spacing.xxxl))

                // Optional "Why I Quit" note
                if (!addiction.whyIQuitNote.isNullOrBlank()) {
                    Text(
                        text = "Why I Quit",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(Spacing.sm))
                    ReclaimCard {
                        Text(
                            text = addiction.whyIQuitNote ?: "",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(Spacing.lg)
                        )
                    }
                    Spacer(modifier = Modifier.height(Spacing.xxxl))
                }

                Text(
                    text = "Manage Journey",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(Spacing.md))

                ReclaimPrimaryButton(
                    text = "Mark as Completed",
                    onClick = viewModel::onCompleteClicked
                )
                Spacer(modifier = Modifier.height(Spacing.md))
                ReclaimOutlinedButton(
                    text = "Pause Journey",
                    onClick = viewModel::onPauseClicked
                )
                Spacer(modifier = Modifier.height(Spacing.md))
                ReclaimOutlinedButton(
                    text = "Go Back",
                    onClick = viewModel::onBackClicked
                )
            }
        } else if (!uiState.isLoading && uiState.error != null) {
            // Error state
            Column(
                modifier = Modifier.padding(Spacing.screen)
            ) {
                Text(
                    text = uiState.error ?: "",
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(Spacing.md))
                ReclaimOutlinedButton(text = "Go Back", onClick = viewModel::onBackClicked)
            }
        }

        if (uiState.isLoading) {
            LoadingOverlay()
        }
    }
}