package com.engineerfred.reclaim.feature.dashboard.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.engineerfred.reclaim.core.domain.util.DateUtils
import com.engineerfred.reclaim.core.ui.components.ProgressBarWithLabel
import com.engineerfred.reclaim.core.ui.components.ReclaimCard
import com.engineerfred.reclaim.core.ui.components.ReclaimOutlinedButton
import com.engineerfred.reclaim.core.ui.components.StreakDisplay
import com.engineerfred.reclaim.core.ui.theme.Spacing
import com.engineerfred.reclaim.feature.dashboard.presentation.ActiveAddictionItem

@Composable
fun AddictionSummaryCard(
    item: ActiveAddictionItem,
    onClick: () -> Unit,
    onCheckInClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentStreak = item.streak.current
    val longestStreak = item.streak.longest
    val nextMilestone = DateUtils.nextMilestoneDays(currentStreak)

    // Calculate progress ratio. If milestone is 7 and current is 3, progress is 3/7 (0.42)
    val progress = if (nextMilestone != null && nextMilestone > 0) {
        currentStreak.toFloat() / nextMilestone.toFloat()
    } else {
        1f // All milestones hit
    }

    ReclaimCard(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.lg)
        ) {
            Text(
                text = item.addiction.displayName,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Started ${DateUtils.daysBetween(item.addiction.startDate, System.currentTimeMillis())} days ago",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(Spacing.xl))

            // From core-ui Phase 5
            StreakDisplay(
                currentStreak = currentStreak,
                longestStreak = longestStreak
            )

            Spacer(modifier = Modifier.height(Spacing.xl))

            // From core-ui Phase 5
            ProgressBarWithLabel(
                progress = progress,
                currentDays = currentStreak,
                nextMilestoneDays = nextMilestone
            )

            Spacer(modifier = Modifier.height(Spacing.lg))

            ReclaimOutlinedButton(
                text = "Log Daily Check-In",
                onClick = onCheckInClick
            )
        }
    }
}