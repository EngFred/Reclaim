package com.engineerfred.reclaim.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.engineerfred.reclaim.core.domain.model.CheckInStatus
import com.engineerfred.reclaim.core.ui.theme.ColorClean
import com.engineerfred.reclaim.core.ui.theme.ColorNoData
import com.engineerfred.reclaim.core.ui.theme.ColorRelapsed
import com.engineerfred.reclaim.core.ui.theme.ColorStruggled
import com.engineerfred.reclaim.core.ui.theme.CornerRadius

/**
 * Single cell in the GitHub-style contribution calendar.
 *
 * @param status   null = no check-in (gray), non-null = logged status
 * @param dayLabel Optional day-of-month label rendered inside the cell (e.g. "1", "15")
 * @param cellSize Size of the square cell — default 28dp for the full calendar view
 * @param isToday  Draws a border around today's cell
 */
@Composable
fun CalendarDayCell(
    status: CheckInStatus?,
    modifier: Modifier = Modifier,
    dayLabel: String = "",
    cellSize: Dp = 28.dp,
    isToday: Boolean = false
) {
    val backgroundColor: Color = when (status) {
        CheckInStatus.SUCCESS   -> ColorClean
        CheckInStatus.STRUGGLED -> ColorStruggled
        CheckInStatus.RELAPSED  -> ColorRelapsed
        null                    -> ColorNoData.copy(alpha = 0.3f)
    }

    Box(
        modifier          = modifier
            .size(cellSize)
            .clip(RoundedCornerShape(CornerRadius.xs))
            .background(backgroundColor)
            .then(
                if (isToday) Modifier.border(
                    width = 1.5.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(CornerRadius.xs)
                ) else Modifier
            ),
        contentAlignment  = Alignment.Center
    ) {
        if (dayLabel.isNotEmpty()) {
            Text(
                text  = dayLabel,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = if (status != null) Color.White
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

/**
 * Legend row displayed below the calendar.
 * Shows what each colour means.
 */
@Composable
fun CalendarLegend(modifier: Modifier = Modifier) {
    androidx.compose.foundation.layout.Row(
        modifier              = modifier,
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp),
        verticalAlignment     = Alignment.CenterVertically
    ) {
        LegendItem(color = ColorClean,                label = "Clean")
        LegendItem(color = ColorStruggled,             label = "Struggled")
        LegendItem(color = ColorRelapsed,              label = "Relapsed")
        LegendItem(color = ColorNoData.copy(alpha = 0.3f), label = "No data")
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    androidx.compose.foundation.layout.Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(color)
        )
        Text(
            text  = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}