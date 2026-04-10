package com.engineerfred.reclaim.core.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.engineerfred.reclaim.core.ui.theme.CornerRadius
import com.engineerfred.reclaim.core.ui.theme.Spacing

/**
 * Animated progress bar showing how close the user is to their next milestone.
 *
 * @param progress       0f–1f (fraction of the way to the next milestone)
 * @param currentDays    The user's current streak
 * @param nextMilestoneDays  The next milestone to hit (7, 30, or 90), or null if all passed
 */
@Composable
fun ProgressBarWithLabel(
    progress: Float,
    currentDays: Int,
    nextMilestoneDays: Int?,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue  = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 600),
        label        = "progress_animation"
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier          = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text     = "$currentDays days",
                style    = MaterialTheme.typography.labelMedium,
                color    = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f)
            )
            if (nextMilestoneDays != null) {
                Text(
                    text  = "Next: day $nextMilestoneDays",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                Text(
                    text  = "All milestones reached",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        Spacer(modifier = Modifier.height(Spacing.xs))

        LinearProgressIndicator(
            progress     = { animatedProgress },
            modifier     = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(CornerRadius.full)),
            color        = MaterialTheme.colorScheme.primary,
            trackColor   = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}