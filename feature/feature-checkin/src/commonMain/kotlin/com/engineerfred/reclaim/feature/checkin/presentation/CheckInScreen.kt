package com.engineerfred.reclaim.feature.checkin.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.engineerfred.reclaim.core.domain.model.CheckInStatus
import com.engineerfred.reclaim.core.ui.components.LoadingOverlay
import com.engineerfred.reclaim.core.ui.components.ReclaimNoteField
import com.engineerfred.reclaim.core.ui.components.ReclaimPrimaryButton
import com.engineerfred.reclaim.core.ui.components.ReclaimTextButton
import com.engineerfred.reclaim.core.ui.theme.ColorClean
import com.engineerfred.reclaim.core.ui.theme.ColorRelapsed
import com.engineerfred.reclaim.core.ui.theme.ColorStruggled
import com.engineerfred.reclaim.core.ui.theme.CornerRadius
import com.engineerfred.reclaim.core.ui.theme.Spacing
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CheckInScreen(
    addictionId: String,
    onNavigateBack: () -> Unit,
    viewModel: CheckInViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(addictionId) {
        viewModel.loadAddiction(addictionId)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                CheckInEvent.NavigateBack -> onNavigateBack()
                is CheckInEvent.ShowToast -> { /* Handle Snackbar */ }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.screen)
                .verticalScroll(rememberScrollState())
        ) {
            val title = uiState.addiction?.displayName ?: ""

            Text(
                text = "Daily Check-In",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(Spacing.xs))
            Text(
                text = "How did you do today with $title?",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(Spacing.xxxl))

            if (uiState.hasCheckedInToday) {
                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "You've already checked in for today.\nRest up and come back tomorrow.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                // ── Status Selectors ────────────────────────────────────────────────
                StatusSelectionButton(
                    text = "I stayed clean today",
                    subText = "Streak continues",
                    color = ColorClean,
                    isSelected = uiState.selectedStatus == CheckInStatus.SUCCESS,
                    onClick = { viewModel.onStatusSelected(CheckInStatus.SUCCESS) }
                )
                Spacer(modifier = Modifier.height(Spacing.md))

                StatusSelectionButton(
                    text = "I had urges, but held on",
                    subText = "Streak continues. Good fight.",
                    color = ColorStruggled,
                    isSelected = uiState.selectedStatus == CheckInStatus.STRUGGLED,
                    onClick = { viewModel.onStatusSelected(CheckInStatus.STRUGGLED) }
                )
                Spacer(modifier = Modifier.height(Spacing.md))

                StatusSelectionButton(
                    text = "I relapsed",
                    subText = "Streak resets. It's okay, start again.",
                    color = ColorRelapsed,
                    isSelected = uiState.selectedStatus == CheckInStatus.RELAPSED,
                    onClick = { viewModel.onStatusSelected(CheckInStatus.RELAPSED) }
                )

                Spacer(modifier = Modifier.height(Spacing.xxxl))

                // ── Trigger Note ────────────────────────────────────────────────────
                ReclaimNoteField(
                    value = uiState.triggerNote,
                    onValueChange = viewModel::onNoteChanged,
                    label = "Trigger Note (Optional)",
                    placeholder = "What helped you today? Or what triggered you?"
                )

                Spacer(modifier = Modifier.height(Spacing.xxl))

                // ── Submit ──────────────────────────────────────────────────────────
                ReclaimPrimaryButton(
                    text = "Log Check-In",
                    onClick = viewModel::onSubmitClicked,
                    enabled = uiState.selectedStatus != null && !uiState.isLoading
                )
            }

            Spacer(modifier = Modifier.height(Spacing.md))
            ReclaimTextButton(
                text = "Go Back",
                onClick = viewModel::onBackClicked,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        if (uiState.isLoading) {
            LoadingOverlay()
        }
    }
}

@Composable
private fun StatusSelectionButton(
    text: String,
    subText: String,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val containerColor = if (isSelected) color.copy(alpha = 0.15f) else Color.Transparent
    val borderColor = if (isSelected) color else MaterialTheme.colorScheme.outline

    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(CornerRadius.md),
        border = BorderStroke(if (isSelected) 2.dp else 1.dp, borderColor),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = containerColor,
            contentColor = if (isSelected) color else MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                color = if (isSelected) color else MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subText,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}