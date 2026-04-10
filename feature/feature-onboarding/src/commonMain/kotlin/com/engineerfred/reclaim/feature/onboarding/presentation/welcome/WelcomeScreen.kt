package com.engineerfred.reclaim.feature.onboarding.presentation.welcome

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.engineerfred.reclaim.core.ui.components.ReclaimPrimaryButton
import com.engineerfred.reclaim.core.ui.theme.Spacing

@Composable
fun WelcomeScreen(
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.screen),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome to Reclaim.",
            style = MaterialTheme.typography.headlineLarge.copy(color = MaterialTheme.colorScheme.primary)
        )
        Spacer(modifier = Modifier.height(Spacing.md))
        Text(
            text = "This is a private, judgment-free space.\nWe don't track you for ads. We just track your progress.",
            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(Spacing.huge))
        ReclaimPrimaryButton(
            text = "Begin Setup",
            onClick = onContinue
        )
    }
}