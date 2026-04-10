package com.engineerfred.reclaim.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.engineerfred.reclaim.core.ui.theme.Spacing

/**
 * Displays the current streak + personal best in the addiction summary card
 * and the progress screen header.
 *
 * Layout:
 *   [current streak number]   [longest streak number]
 *   "days clean"              "personal best"
 */
@Composable
fun StreakDisplay(
    currentStreak: Int,
    longestStreak: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier            = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StreakStat(
            value = currentStreak,
            label = "days clean"
        )
        StreakStat(
            value = longestStreak,
            label = "personal best"
        )
    }
}

@Composable
private fun StreakStat(
    value: Int,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier          = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text  = value.toString(),
            style = MaterialTheme.typography.displayLarge.copy(
                fontSize   = 48.sp,
                fontWeight = FontWeight.Bold,
                color      = MaterialTheme.colorScheme.primary
            )
        )
        Spacer(modifier = Modifier.height(Spacing.xs))
        Text(
            text  = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}