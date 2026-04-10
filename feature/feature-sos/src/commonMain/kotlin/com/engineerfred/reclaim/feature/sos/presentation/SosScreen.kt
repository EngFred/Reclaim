package com.engineerfred.reclaim.feature.sos.presentation

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.engineerfred.reclaim.core.ui.components.LoadingOverlay
import com.engineerfred.reclaim.core.ui.components.ReclaimHighlightCard
import com.engineerfred.reclaim.core.ui.components.ReclaimTextButton
import com.engineerfred.reclaim.core.ui.theme.Spacing
import com.engineerfred.reclaim.feature.sos.presentation.components.BreathingExerciseComponent
import com.engineerfred.reclaim.feature.sos.presentation.components.DistractionTimerComponent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SosScreen(
    addictionId: String,
    onNavigateBack: () -> Unit,
    viewModel: SosViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(addictionId) {
        viewModel.initialize(addictionId)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            if (event is SosEvent.NavigateBack) {
                onNavigateBack()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            // Subtle grounding background color shift for SOS mode
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        if (!uiState.isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(Spacing.screen)
            ) {
                // Grounding Header
                Text(
                    text = "Breathe.",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(Spacing.sm))
                Text(
                    text = "This feeling will pass. You have ${uiState.streakDays} days of progress. Don't lose them.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(Spacing.huge))

                // Breathing Tool
                BreathingExerciseComponent()

                Spacer(modifier = Modifier.height(Spacing.huge))

                // Timer Tool
                DistractionTimerComponent(
                    isActive = uiState.isTimerActive,
                    onToggle = viewModel::toggleTimer
                )

                Spacer(modifier = Modifier.height(Spacing.xxxl))

                // Personal Note
                if (!uiState.whyIQuitNote.isNullOrBlank()) {
                    Text(
                        text = "Remember why you started:",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(Spacing.md))
                    ReclaimHighlightCard(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = uiState.whyIQuitNote!!,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(Spacing.lg)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(Spacing.xxxl))
                ReclaimTextButton(
                    text = "I'm okay now. Go Back.",
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