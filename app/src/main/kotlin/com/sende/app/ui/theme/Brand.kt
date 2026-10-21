package com.sende.app.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * The brand vocabulary. Everything the UI paints or animates comes from these tokens; the values
 * live in the generated `BrandTokens.kt` (tools/apply_brand.py), mirroring
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

val LocalBrandColors = staticCompositionLocalOf { Brand.light }
val LocalBrandShapes = staticCompositionLocalOf { Brand.shapes }
val LocalBrandMotion = staticCompositionLocalOf { Brand.motion }

/** Short access to the active brand tokens inside composables: `Tek.colors.accent`. */
object Tek {
    val colors: BrandColors @androidx.compose.runtime.Composable get() = LocalBrandColors.current
    val shapes: BrandShapes @androidx.compose.runtime.Composable get() = LocalBrandShapes.current
    val motion: BrandMotion @androidx.compose.runtime.Composable get() = LocalBrandMotion.current
}
