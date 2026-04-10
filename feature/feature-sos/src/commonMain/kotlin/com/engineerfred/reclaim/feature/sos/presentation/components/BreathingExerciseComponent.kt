package com.engineerfred.reclaim.feature.sos.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.engineerfred.reclaim.core.ui.theme.Spacing
import kotlinx.coroutines.delay

private enum class BreathingState(val instruction: String, val durationMs: Int, val targetScale: Float) {
    INHALE("Breathe In...", 4000, 1.5f),
    HOLD("Hold...", 7000, 1.5f),
    EXHALE("Breathe Out...", 8000, 1.0f)
}

/**
 * 4-7-8 Breathing Exercise.
 * A proven grounding technique for acute anxiety and craving spikes.
 */
@Composable
fun BreathingExerciseComponent(modifier: Modifier = Modifier) {
    var currentState by remember { mutableStateOf(BreathingState.INHALE) }

    // Run the infinite state machine loop
    LaunchedEffect(Unit) {
        while (true) {
            currentState = BreathingState.INHALE
            delay(currentState.durationMs.toLong())

            currentState = BreathingState.HOLD
            delay(currentState.durationMs.toLong())

            currentState = BreathingState.EXHALE
            delay(currentState.durationMs.toLong())
        }
    }

    val animatedScale by animateFloatAsState(
        targetValue = currentState.targetScale,
        animationSpec = tween(
            durationMillis = currentState.durationMs,
            easing = FastOutSlowInEasing
        ),
        label = "BreathingScale"
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(160.dp)
                .scale(animatedScale)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
            )
        }

        Spacer(modifier = Modifier.height(Spacing.xxxl))

        Text(
            text = currentState.instruction,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
    }
}