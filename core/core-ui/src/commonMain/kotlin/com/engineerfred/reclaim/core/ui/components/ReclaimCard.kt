package com.engineerfred.reclaim.core.ui.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.shape.RoundedCornerShape
import com.engineerfred.reclaim.core.ui.theme.CornerRadius
import com.engineerfred.reclaim.core.ui.theme.Elevation
import com.engineerfred.reclaim.core.ui.theme.Spacing

/**
 * Standard surface card used throughout the app.
 * Wraps Material3 Card with consistent shape, elevation, and padding.
 *
 * All addiction summary cards, milestone cards, and stats cards
 * are built on top of this component.
 */
@Composable
fun ReclaimCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    if (onClick != null) {
        Card(
            onClick    = onClick,
            modifier   = modifier,
            shape      = RoundedCornerShape(CornerRadius.lg),
            elevation  = CardDefaults.cardElevation(defaultElevation = Elevation.low),
            colors     = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            content    = content
        )
    } else {
        Card(
            modifier  = modifier,
            shape     = RoundedCornerShape(CornerRadius.lg),
            elevation = CardDefaults.cardElevation(defaultElevation = Elevation.low),
            colors    = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            content   = content
        )
    }
}

/**
 * Highlighted card variant — uses primaryContainer as background.
 * Used for milestone messages and SOS content panels.
 */
@Composable
fun ReclaimHighlightCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier  = modifier,
        shape     = RoundedCornerShape(CornerRadius.lg),
        elevation = CardDefaults.cardElevation(defaultElevation = Elevation.none),
        colors    = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        content   = content
    )
}