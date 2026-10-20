package com.sende.app.ui.theme

import androidx.compose.animation.core.Spring
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * The whole brand in one file. Everything the UI paints or animates comes from here, so choosing
 * another brand concept means replacing the values below and nothing else. Values mirror
 * `docs/marka/konsept-N-belirtecler.json` of the chosen concept.
 */
@Immutable
data class BrandColors(
    val background: Color,
    val surface: Color,
    val surfaceHigh: Color,
    val line: Color,
    val text: Color,
    val textSecondary: Color,
    val accent: Color,
    val onAccent: Color,
    val danger: Color,
    val success: Color,
    val isDark: Boolean,
)

@Immutable
data class BrandShapes(val card: Dp, val bubble: Dp, val button: Dp)

@Immutable
data class BrandMotion(
    val expandMillis: Int,
    val dismissMillis: Int,
    val springDamping: Float,
    val springStiffness: Float,
)

object Brand {
    /** Provisional concept 1 "Gece Mürekkebi" (near-black ink, one warm amber accent). */
    val colors = BrandColors(
        background = Color(0xFF0C0D10),
        surface = Color(0xFF15171C),
        surfaceHigh = Color(0xFF1D2027),
        line = Color(0xFF2B2E37),
        text = Color(0xFFF2F0EA),
        textSecondary = Color(0xFFA6A7AF),
        accent = Color(0xFFF2B544),
        onAccent = Color(0xFF1A1405),
        danger = Color(0xFFE5675F),
        success = Color(0xFF58C58B),
        isDark = true,
    )
    val shapes = BrandShapes(card = 20.dp, bubble = 28.dp, button = 14.dp)
    val motion = BrandMotion(
        expandMillis = 260,
        dismissMillis = 220,
        springDamping = Spring.DampingRatioLowBouncy,
        springStiffness = Spring.StiffnessMediumLow,
    )
}

val LocalBrandColors = staticCompositionLocalOf { Brand.colors }
val LocalBrandShapes = staticCompositionLocalOf { Brand.shapes }
val LocalBrandMotion = staticCompositionLocalOf { Brand.motion }

/** Short access to the active brand tokens inside composables: `Tek.colors.accent`. */
object Tek {
    val colors: BrandColors @androidx.compose.runtime.Composable get() = LocalBrandColors.current
    val shapes: BrandShapes @androidx.compose.runtime.Composable get() = LocalBrandShapes.current
    val motion: BrandMotion @androidx.compose.runtime.Composable get() = LocalBrandMotion.current
}
