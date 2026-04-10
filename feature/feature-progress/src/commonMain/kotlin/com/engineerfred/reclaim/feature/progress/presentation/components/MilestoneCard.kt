package com.engineerfred.reclaim.feature.progress.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.engineerfred.reclaim.core.domain.model.Milestone
import com.engineerfred.reclaim.core.ui.components.ReclaimHighlightCard
import com.engineerfred.reclaim.core.ui.theme.Spacing

@Composable
fun MilestoneCard(
    milestone: Milestone,
    modifier: Modifier = Modifier
) {
    ReclaimHighlightCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.lg)
        ) {
            Text(
                text = "Milestone: ${milestone.title}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(Spacing.xs))
            Text(
                text = milestone.message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            )
        }
    }
}