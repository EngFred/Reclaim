package com.engineerfred.reclaim.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.engineerfred.reclaim.core.ui.theme.CornerRadius
import com.engineerfred.reclaim.core.ui.theme.Spacing
import androidx.compose.foundation.shape.RoundedCornerShape

/**
 * Primary CTA button — full width, filled, teal.
 * Used for the main action on every screen (e.g. "Continue", "Log Check-In").
 */
@Composable
fun ReclaimPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    Button(
        onClick  = onClick,
        enabled  = enabled && !isLoading,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape    = RoundedCornerShape(CornerRadius.md),
        colors   = ButtonDefaults.buttonColors(
            containerColor         = MaterialTheme.colorScheme.primary,
            contentColor           = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor   = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color    = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text  = text,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

/**
 * Secondary / outlined button — full width, bordered, transparent fill.
 * Used for secondary actions (e.g. "Skip", "Cancel").
 */
@Composable
fun ReclaimOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick  = onClick,
        enabled  = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape    = RoundedCornerShape(CornerRadius.md),
        colors   = ButtonDefaults.outlinedButtonColors(
            contentColor          = MaterialTheme.colorScheme.primary,
            disabledContentColor  = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Text(
            text  = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

/**
 * Destructive button — used only for dangerous actions (relapse log, account deletion).
 * Filled red to signal consequence without being gratuitously alarming.
 */
@Composable
fun ReclaimDestructiveButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick  = onClick,
        enabled  = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape    = RoundedCornerShape(CornerRadius.md),
        colors   = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor   = MaterialTheme.colorScheme.onError
        )
    ) {
        Text(
            text  = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

/**
 * Ghost / text button — for low-priority tertiary actions.
 */
@Composable
fun ReclaimTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    color: Color = Color.Unspecified
) {
    TextButton(
        onClick  = onClick,
        enabled  = enabled,
        modifier = modifier
    ) {
        Text(
            text  = text,
            style = MaterialTheme.typography.labelLarge,
            color = if (color != Color.Unspecified) color
            else MaterialTheme.colorScheme.primary
        )
    }
}