package com.engineerfred.reclaim.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary          = ReclaimTeal500,
    onPrimary        = NeutralWhite,
    primaryContainer = ReclaimTeal100,
    onPrimaryContainer = ReclaimTeal900,

    secondary        = ReclaimAmber500,
    onSecondary      = Neutral900,
    secondaryContainer = ReclaimAmber200,
    onSecondaryContainer = Neutral900,

    background       = BackgroundLight,
    onBackground     = Neutral900,

    surface          = SurfaceLight,
    onSurface        = Neutral900,
    surfaceVariant   = Neutral100,
    onSurfaceVariant = Neutral600,

    outline          = Neutral300,
    outlineVariant   = Neutral100,

    error            = ColorRelapsed,
    onError          = NeutralWhite,
    errorContainer   = Color(0xFFFFCDD2),
    onErrorContainer = Color(0xFFB71C1C)
)

private val DarkColorScheme = darkColorScheme(
    primary          = ReclaimTeal300,
    onPrimary        = ReclaimTeal900,
    primaryContainer = ReclaimTeal700,
    onPrimaryContainer = ReclaimTeal100,

    secondary        = ReclaimAmber200,
    onSecondary      = Neutral900,
    secondaryContainer = ReclaimAmber500,
    onSecondaryContainer = Neutral900,

    background       = BackgroundDark,
    onBackground     = Neutral100,

    surface          = SurfaceDark,
    onSurface        = Neutral100,
    surfaceVariant   = Neutral800,
    onSurfaceVariant = Neutral300,

    outline          = Neutral600,
    outlineVariant   = Neutral800,

    error            = Color(0xFFEF9A9A),
    onError          = Color(0xFF690005),
    errorContainer   = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6)
)

@Composable
fun ReclaimTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = ReclaimTypography,
        content     = content
    )
}