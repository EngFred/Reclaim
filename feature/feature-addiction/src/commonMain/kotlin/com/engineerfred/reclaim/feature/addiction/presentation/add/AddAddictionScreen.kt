package com.engineerfred.reclaim.feature.addiction.presentation.add

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.engineerfred.reclaim.core.domain.model.AddictionCategory
import com.engineerfred.reclaim.core.ui.components.AddictionCategoryChip
import com.engineerfred.reclaim.core.ui.components.LoadingOverlay
import com.engineerfred.reclaim.core.ui.components.ReclaimNoteField
import com.engineerfred.reclaim.core.ui.components.ReclaimPrimaryButton
import com.engineerfred.reclaim.core.ui.theme.Spacing
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddAddictionScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddAddictionViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                AddAddictionEvent.NavigateBack -> onNavigateBack()
                is AddAddictionEvent.ShowToast -> { /* Handle Snackbar */ }
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
            Text(
                text = "Add a new journey",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(Spacing.lg))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
                verticalArrangement = Arrangement.spacedBy(Spacing.sm)
            ) {
                AddictionCategory.entries.forEach { category ->
                    AddictionCategoryChip(
                        category = category,
                        selected = uiState.selectedCategory == category,
                        onToggle = viewModel::onCategorySelected
                    )
                }
            }

            Spacer(modifier = Modifier.height(Spacing.xxl))

            ReclaimNoteField(
                value = uiState.whyIQuitNote,
                onValueChange = viewModel::onNoteChanged,
                label = "Why I Quit (Optional)",
                placeholder = "Write a note to your future self..."
            )

            Spacer(modifier = Modifier.height(Spacing.xxl))

            ReclaimPrimaryButton(
                text = "Start Journey",
                onClick = viewModel::onSaveClicked,
                enabled = uiState.selectedCategory != null && !uiState.isLoading
            )
        }

        if (uiState.isLoading) {
            LoadingOverlay()
        }
    }
}