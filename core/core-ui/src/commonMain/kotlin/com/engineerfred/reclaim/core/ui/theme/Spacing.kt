package com.engineerfred.reclaim.core.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Centralised spacing scale.
 * All padding/margin values in the app come from here — never magic numbers.
 * Based on a 4dp base unit (standard Material 3 practice).
 */
object Spacing {
    val xxs: Dp  = 2.dp
    val xs: Dp   = 4.dp
    val sm: Dp   = 8.dp
    val md: Dp   = 12.dp
    val lg: Dp   = 16.dp
    val xl: Dp   = 20.dp
    val xxl: Dp  = 24.dp
    val xxxl: Dp = 32.dp
    val huge: Dp = 48.dp
    val screen: Dp = 20.dp  // standard horizontal screen padding
}

/**
 * Corner radius scale.
 */
object CornerRadius {
    val xs: Dp  = 4.dp
    val sm: Dp  = 8.dp
    val md: Dp  = 12.dp
    val lg: Dp  = 16.dp
    val xl: Dp  = 24.dp
    val full: Dp = 50.dp    // pill shape
}

/**
 * Elevation scale.
 */
object Elevation {
    val none: Dp   = 0.dp
    val low: Dp    = 2.dp
    val medium: Dp = 4.dp
    val high: Dp   = 8.dp
}