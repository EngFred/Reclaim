package com.engineerfred.reclaim.feature.onboarding.presentation.selection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.engineerfred.reclaim.core.domain.model.AddictionCategory
import com.engineerfred.reclaim.core.ui.components.AddictionCategoryChip
import com.engineerfred.reclaim.core.ui.components.ReclaimPrimaryButton
import com.engineerfred.reclaim.core.ui.theme.Spacing

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddictionSelectionScreen(
    selectedCategories: Set<AddictionCategory>,
    onToggleCategory: (AddictionCategory) -> Unit,
    canProceed: Boolean,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier.Companion
            .fillMaxSize()
            .padding(Spacing.screen)
    ) {
        Text(
            text = "What are you fighting?",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.Companion.height(Spacing.xs))
        Text(
            text = "Select all that apply. You can track them independently.",
            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )
        Spacer(modifier = Modifier.Companion.height(Spacing.xxl))

        // UI trick: chunking enums to flow them properly if FlowRow isn't used
        FlowRow(
            modifier = Modifier.Companion.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
            verticalArrangement = Arrangement.spacedBy(Spacing.sm)
        ) {
            AddictionCategory.entries.forEach { category ->
                AddictionCategoryChip(
                    category = category,
                    selected = selectedCategories.contains(category),
                    onToggle = onToggleCategory
                )
            }
        }

        ReclaimPrimaryButton(
            text = "Continue",
            onClick = onContinue,
            enabled = canProceed
        )
    }
}