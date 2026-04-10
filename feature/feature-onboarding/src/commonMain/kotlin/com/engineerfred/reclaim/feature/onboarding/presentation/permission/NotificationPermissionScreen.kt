package com.engineerfred.reclaim.feature.onboarding.presentation.permission

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.engineerfred.reclaim.core.notifications.NotificationScheduler
import com.engineerfred.reclaim.core.ui.components.ReclaimOutlinedButton
import com.engineerfred.reclaim.core.ui.components.ReclaimPrimaryButton
import com.engineerfred.reclaim.core.ui.theme.Spacing

@Composable
fun NotificationPermissionScreen(
    notificationScheduler: NotificationScheduler,
    onComplete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.screen),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Stay on track.",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(Spacing.md))
        Text(
            text = "RECLAIM uses personalized, scientifically grounded notifications to help you survive high-risk triggers and celebrate milestones. \n\nWe never spam. Enable notifications to unlock this core feature.",
            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(Spacing.huge))

        ReclaimPrimaryButton(
            text = "Allow Notifications",
            onClick = {
                // OS permission trigger -> then complete flow
                notificationScheduler.requestPermission()
                onComplete()
            }
        )
        Spacer(modifier = Modifier.height(Spacing.sm))
        ReclaimOutlinedButton(
            text = "Skip for now",
            onClick = onComplete
        )
    }
}