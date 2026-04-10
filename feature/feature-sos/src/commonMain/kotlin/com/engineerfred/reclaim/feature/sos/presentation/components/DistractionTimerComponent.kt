package com.engineerfred.reclaim.feature.sos.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.engineerfred.reclaim.core.ui.components.ReclaimPrimaryButton
import kotlinx.coroutines.delay

/**
 * A 5-minute countdown timer.
 * Urges typically peak and subside within 5 to 15 minutes.
 */
@Composable
fun DistractionTimerComponent(
    isActive: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    var timeLeftSeconds by remember { mutableIntStateOf(300) } // 5 minutes

    LaunchedEffect(isActive) {
        if (isActive) {
            while (timeLeftSeconds > 0) {
                delay(1000)
                timeLeftSeconds -= 1
            }
            if (timeLeftSeconds == 0) {
                onToggle() // Auto-stop when done
                timeLeftSeconds = 300 // Reset
            }
        }
    }

    val minutes = timeLeftSeconds / 60
    val seconds = timeLeftSeconds % 60
    val timeFormatted = "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = timeFormatted,
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        ReclaimPrimaryButton(
            text = if (isActive) "Stop Timer" else "Start 5-Minute Timer",
            onClick = {
                if (isActive) {
                    timeLeftSeconds = 300 // Reset if stopped manually
                }
                onToggle()
            }
        )
    }
}