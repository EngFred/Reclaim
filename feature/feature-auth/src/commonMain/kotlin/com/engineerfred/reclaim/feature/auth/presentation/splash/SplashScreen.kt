package com.engineerfred.reclaim.feature.auth.presentation.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.engineerfred.reclaim.core.ui.navigation.ReclaimRoutes
import com.engineerfred.reclaim.core.ui.theme.ReclaimTeal500
import com.engineerfred.reclaim.core.ui.theme.ReclaimTeal700
import com.engineerfred.reclaim.core.ui.theme.ReclaimTeal900
import com.engineerfred.reclaim.core.ui.theme.Spacing
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

private const val MIN_SPLASH_DURATION_MS = 2_200L

@Composable
fun SplashScreen(
    onNavigate: (route: String, clearBackStack: Boolean) -> Unit,
    viewModel: SplashViewModel = koinViewModel()
) {
    val destination by viewModel.destination.collectAsStateWithLifecycle()

    var minTimeElapsed by remember { mutableStateOf(false) }

    // ── Minimum display timer ─────────────────────────────────────────────────
    LaunchedEffect(Unit) {
        delay(MIN_SPLASH_DURATION_MS)
        minTimeElapsed = true
    }

    // ── Navigate only when BOTH conditions are met ────────────────────────────
    LaunchedEffect(destination, minTimeElapsed) {
        if (!minTimeElapsed) return@LaunchedEffect
        when (destination) {
            SplashDestination.Login     -> onNavigate(ReclaimRoutes.LOGIN, true)
            SplashDestination.Dashboard -> onNavigate(ReclaimRoutes.DASHBOARD, true)
            SplashDestination.Undecided -> Unit
        }
    }

    // ── Entrance animation state ──────────────────────────────────────────────
    val logoScale  = remember { Animatable(0.4f) }
    val logoAlpha  = remember { Animatable(0f) }
    val titleAlpha = remember { Animatable(0f) }
    // Used as offsetY (dp): starts below, slides up to 0.
    // offset() — unlike padding() — accepts negative values safely,
    // so EaseOutBack's overshoot past zero is harmless here.
    val titleOffsetY = remember { Animatable(28f) }
    val tagAlpha   = remember { Animatable(0f) }
    val dotsAlpha  = remember { Animatable(0f) }

    // ── Pulsing outer ring ────────────────────────────────────────────────────
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val ringScale by infiniteTransition.animateFloat(
        initialValue  = 1f,
        targetValue   = 1.18f,
        animationSpec = infiniteRepeatable(
            animation  = tween(1400, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ringScale"
    )
    val ringAlpha by infiniteTransition.animateFloat(
        initialValue  = 0.18f,
        targetValue   = 0.38f,
        animationSpec = infiniteRepeatable(
            animation  = tween(1400, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ringAlpha"
    )

    // ── Staggered loading dots ────────────────────────────────────────────────
    val dot1Scale by infiniteTransition.animateFloat(
        initialValue  = 0.6f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation  = tween(600, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot1"
    )
    val dot2Scale by infiniteTransition.animateFloat(
        initialValue  = 0.6f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation  = tween(600, delayMillis = 150, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot2"
    )
    val dot3Scale by infiniteTransition.animateFloat(
        initialValue  = 0.6f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation  = tween(600, delayMillis = 300, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot3"
    )

    // ── Staggered entrance sequence ───────────────────────────────────────────
    LaunchedEffect(Unit) {
        launch {
            logoAlpha.animateTo(1f, tween(400, easing = EaseOutCubic))
            logoScale.animateTo(
                targetValue   = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness    = Spring.StiffnessMedium
                )
            )
        }
        launch {
            delay(400)
            titleAlpha.animateTo(1f, tween(450, easing = EaseOutCubic))
            // Animating offsetY from 28 → 0 with EaseOutBack is safe:
            // even if it briefly goes to e.g. -2dp that's fine for offset.
            titleOffsetY.animateTo(0f, tween(450, easing = EaseOutBack))
        }
        launch {
            delay(700)
            tagAlpha.animateTo(1f, tween(450))
        }
        launch {
            delay(950)
            dotsAlpha.animateTo(1f, tween(350))
        }
    }

    // ── UI ────────────────────────────────────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(ReclaimTeal900, ReclaimTeal700, ReclaimTeal500)
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // ── Logo mark ─────────────────────────────────────────────────────
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .scale(logoScale.value)
                    .alpha(logoAlpha.value)
            ) {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .scale(ringScale)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = ringAlpha))
                )
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f))
                )
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.92f))
                )
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(ReclaimTeal700)
                )
            }

            Spacer(Modifier.height(Spacing.xxl))

            // ── Wordmark ──────────────────────────────────────────────────────
            // offset() is used instead of padding() so that EaseOutBack's
            // brief overshoot past 0 never triggers "Padding must be non-negative".
            Text(
                text  = "RECLAIM",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color         = Color.White,
                    fontWeight    = FontWeight.ExtraBold,
                    letterSpacing = 8.sp,
                    fontSize      = 32.sp
                ),
                modifier = Modifier
                    .alpha(titleAlpha.value)
                    .offset(y = titleOffsetY.value.dp)
            )

            Spacer(Modifier.height(Spacing.sm))

            // ── Tagline ───────────────────────────────────────────────────────
            Text(
                text      = "Reclaim your life, one day at a time.",
                style     = MaterialTheme.typography.bodyMedium.copy(
                    color         = Color.White.copy(alpha = 0.72f),
                    letterSpacing = 0.5.sp
                ),
                textAlign = TextAlign.Center,
                modifier  = Modifier
                    .alpha(tagAlpha.value)
                    .padding(horizontal = Spacing.xxxl)
            )

            Spacer(Modifier.height(Spacing.huge))

            // ── Loading dots ──────────────────────────────────────────────────
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment     = Alignment.CenterVertically,
                modifier              = Modifier.alpha(dotsAlpha.value)
            ) {
                listOf(dot1Scale, dot2Scale, dot3Scale).forEach { dotScale ->
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .scale(dotScale)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.65f))
                    )
                }
            }
        }

        // ── Bottom attribution ────────────────────────────────────────────────
        Text(
            text  = "Your journey. Your data. Your privacy.",
            style = MaterialTheme.typography.labelSmall.copy(
                color         = Color.White.copy(alpha = 0.40f),
                letterSpacing = 0.3.sp
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 36.dp)
                .alpha(tagAlpha.value)
        )
    }
}