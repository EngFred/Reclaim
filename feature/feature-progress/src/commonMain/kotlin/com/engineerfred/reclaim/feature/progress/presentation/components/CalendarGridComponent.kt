package com.engineerfred.reclaim.feature.progress.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.engineerfred.reclaim.core.ui.components.CalendarDayCell
import com.engineerfred.reclaim.core.ui.theme.Spacing
import com.engineerfred.reclaim.feature.progress.domain.model.CalendarDay

@Composable
fun CalendarGridComponent(
    days: List<CalendarDay>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(7), // Standard 7-day week
            horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
            verticalArrangement = Arrangement.spacedBy(Spacing.sm),
            modifier = Modifier.fillMaxWidth(),
            userScrollEnabled = false // Grid relies on the parent's vertical scroll
        ) {
            items(days) { day ->
                CalendarDayCell(
                    status = day.status,
                    isToday = day.isToday,
                    cellSize = 32.dp
                )
            }
        }
    }
}