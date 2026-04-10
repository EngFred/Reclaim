package com.engineerfred.reclaim.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Full-screen loading overlay.
 * Displayed during auth operations and initial data loads.
 * Shown conditionally — never shown when data is already in the local DB.
 */
@Composable
fun LoadingOverlay(
    modifier: Modifier = Modifier
) {
    Box(
        modifier          = modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.background.copy(alpha = 0.7f)
            ),
        contentAlignment  = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier    = Modifier.size(48.dp),
            color       = MaterialTheme.colorScheme.primary,
            strokeWidth = 3.dp
        )
    }
}