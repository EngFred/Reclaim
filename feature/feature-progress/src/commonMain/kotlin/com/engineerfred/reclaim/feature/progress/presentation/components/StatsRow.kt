package com.engineerfred.reclaim.feature.progress.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.engineerfred.reclaim.core.domain.model.Streak
import com.engineerfred.reclaim.core.ui.components.ReclaimCard
import com.engineerfred.reclaim.core.ui.theme.Spacing

/**
 * Displays secondary stats: success rate, total tracked days, and longest streak.
 */
@Composable
fun StatsRow(
    streak: Streak,
    modifier: Modifier = Modifier
) {
    ReclaimCard(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.lg),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatItem(
                value = "${(streak.successRate * 100).toInt()}%",
                label = "Success Rate"
            )
            StatItem(
                value = streak.totalDays.toString(),
                label = "Total Days"
            )
            StatItem(
                value = streak.longest.toString(),
                label = "Best Streak"
            )
        }
    }
}

@Composable
private fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}