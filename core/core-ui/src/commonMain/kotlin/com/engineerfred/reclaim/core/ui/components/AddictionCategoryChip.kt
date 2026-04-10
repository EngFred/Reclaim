package com.engineerfred.reclaim.core.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.engineerfred.reclaim.core.domain.model.AddictionCategory
import com.engineerfred.reclaim.core.ui.theme.CornerRadius

/**
 * Selectable chip used in the AddictionSelectionScreen during onboarding
 * and the AddAddictionScreen in settings.
 *
 * When [selected] is true the chip fills with the primary container colour.
 */
@Composable
fun AddictionCategoryChip(
    category: AddictionCategory,
    selected: Boolean,
    onToggle: (AddictionCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = selected,
        onClick  = { onToggle(category) },
        label    = {
            Text(
                text  = category.displayName(),
                style = MaterialTheme.typography.labelMedium
            )
        },
        modifier = modifier,
        shape    = RoundedCornerShape(CornerRadius.full),
        colors   = FilterChipDefaults.filterChipColors(
            selectedContainerColor    = MaterialTheme.colorScheme.primaryContainer,
            selectedLabelColor        = MaterialTheme.colorScheme.onPrimaryContainer,
            containerColor            = MaterialTheme.colorScheme.surface,
            labelColor                = MaterialTheme.colorScheme.onSurface
        ),
        border   = FilterChipDefaults.filterChipBorder(
            enabled               = true,
            selected              = selected,
            borderColor           = MaterialTheme.colorScheme.outline,
            selectedBorderColor   = MaterialTheme.colorScheme.primary,
            borderWidth           = 1.dp,
            selectedBorderWidth   = 1.5.dp
        )
    )
}