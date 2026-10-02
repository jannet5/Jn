package app.nokta.a.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import app.nokta.a.R

/** DESIGN.md §2'deki tokenlar. */
@Immutable
data class NoktaColors(
    val bg: Color, val ink: Color, val inkSoft: Color, val line: Color,
    val dot: Color, val onDot: Color, val inverse: Color, val onInverse: Color,
)

val LightColors = NoktaColors(
    bg = Color(0xFFF2F3EF), ink = Color(0xFF181B19),
    inkSoft = Color(0xFF181B19).copy(alpha = 0.55f), line = Color(0xFF181B19).copy(alpha = 0.10f),
    dot = Color(0xFFE5481C), onDot = Color(0xFF181B19),
    inverse = Color(0xFF181B19), onInverse = Color(0xFFF2F3EF),
)
val DarkColors = NoktaColors(
    bg = Color(0xFF111312), ink = Color(0xFFE9ECE7),
    inkSoft = Color(0xFFE9ECE7).copy(alpha = 0.55f), line = Color(0xFFE9ECE7).copy(alpha = 0.12f),
    dot = Color(0xFFFF6A3A), onDot = Color(0xFF111312),
    inverse = Color(0xFFE9ECE7), onInverse = Color(0xFF111312),
)

val LocalNokta = staticCompositionLocalOf { LightColors }

private fun grotesk(res: Int, w: Int) = FontFamily(
    Font(res, FontWeight(w), variationSettings = FontVariation.Settings(FontVariation.weight(w)))
)

val Familjen700 = grotesk(R.font.familjen_grotesk, 700)
val Familjen500 = grotesk(R.font.familjen_grotesk, 500)
val Hanken400 = grotesk(R.font.hanken_grotesk, 400)
val Hanken500 = grotesk(R.font.hanken_grotesk, 500)

object NoktaType {
    val wordmark = TextStyle(fontFamily = Familjen700, fontSize = 28.sp, lineHeight = 32.sp, letterSpacing = (-0.5).sp)
    val row = TextStyle(fontFamily = Hanken400, fontSize = 17.sp, lineHeight = 24.sp)
    val meta = TextStyle(fontFamily = Hanken500, fontSize = 13.sp, lineHeight = 16.sp, letterSpacing = 0.2.sp)
    val snack = TextStyle(fontFamily = Hanken500, fontSize = 15.sp, lineHeight = 20.sp)
    val menu = TextStyle(fontFamily = Hanken400, fontSize = 16.sp, lineHeight = 24.sp)
}

@Composable
fun NoktaTheme(dark: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val c = if (dark) DarkColors else LightColors
    val scheme = if (dark) darkColorScheme(
        primary = c.dot, onPrimary = c.onDot, background = c.bg, onBackground = c.ink,
        surface = c.bg, onSurface = c.ink, outlineVariant = c.line, surfaceContainer = c.bg,
    ) else lightColorScheme(
        primary = c.dot, onPrimary = c.onDot, background = c.bg, onBackground = c.ink,
        surface = c.bg, onSurface = c.ink, outlineVariant = c.line, surfaceContainer = c.bg,
    )
    CompositionLocalProvider(LocalNokta provides c) {
        MaterialTheme(colorScheme = scheme, typography = Typography(), content = content)
    }
}
