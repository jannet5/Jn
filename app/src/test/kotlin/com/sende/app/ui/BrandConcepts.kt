package com.sende.app.ui

import androidx.compose.animation.core.Spring
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sende.app.ui.theme.BrandColors
import com.sende.app.ui.theme.BrandMotion
import com.sende.app.ui.theme.BrandShapes

/** GENERATED from docs/marka/konsept-N-belirtecler.json by scratchpad/gen_concepts.py. Test-only: used to render the real app in every brand concept. */
data class BrandConcept(
    val number: Int,
    val name: String,
    val colors: BrandColors,
    val shapes: BrandShapes,
    val motion: BrandMotion,
    val headingFont: String,
    val bodyFont: String,
)

object BrandConcepts {
    val all: List<BrandConcept> = listOf(
        BrandConcept(
            number = 1,
            name = "Gece Mürekkebi",
            colors = BrandColors(
                background = Color(0xFF0C0E12), surface = Color(0xFF14171C), surfaceHigh = Color(0xFF1C2027),
                line = Color(0xFF2A2F38), text = Color(0xFFF3EFE7), textSecondary = Color(0xFFA8A397),
                accent = Color(0xFFEDB04C), onAccent = Color(0xFF1A1207), danger = Color(0xFFEC7A6E), success = Color(0xFF74C79C),
                isDark = true,
            ),
            shapes = BrandShapes(card = 16.dp, bubble = 18.dp, button = 10.dp),
            motion = BrandMotion(expandMillis = 460, dismissMillis = 360, springDamping = 0.75f, springStiffness = 200.0f),
            headingFont = "Manrope",
            bodyFont = "Inter",
        ),
        BrandConcept(
            number = 2,
            name = "Sıcak Esnaf",
            colors = BrandColors(
                background = Color(0xFFF5EDDD), surface = Color(0xFFFCF8EF), surfaceHigh = Color(0xFFFFFDF9),
                line = Color(0xFFE4D8C0), text = Color(0xFF0D4A46), textSecondary = Color(0xFF566560),
                accent = Color(0xFFAE4B27), onAccent = Color(0xFFFFF8EE), danger = Color(0xFFB3283F), success = Color(0xFF2A7249),
                isDark = false,
            ),
            shapes = BrandShapes(card = 18.dp, bubble = 20.dp, button = 14.dp),
            motion = BrandMotion(expandMillis = 320, dismissMillis = 240, springDamping = 0.6f, springStiffness = 400.0f),
            headingFont = "Bricolage Grotesque",
            bodyFont = "DM Sans",
        ),
        BrandConcept(
            number = 3,
            name = "Keskin Sinyal",
            colors = BrandColors(
                background = Color(0xFF0D0F0E), surface = Color(0xFF151816), surfaceHigh = Color(0xFF1E2320),
                line = Color(0xFF2E3531), text = Color(0xFFEDF0EA), textSecondary = Color(0xFF98A29A),
                accent = Color(0xFFC8F135), onAccent = Color(0xFF10140A), danger = Color(0xFFFF6B5B), success = Color(0xFF4FD8A4),
                isDark = true,
            ),
            shapes = BrandShapes(card = 8.dp, bubble = 6.dp, button = 4.dp),
            motion = BrandMotion(expandMillis = 180, dismissMillis = 140, springDamping = 1.0f, springStiffness = 1500.0f),
            headingFont = "Space Grotesk",
            bodyFont = "Inter",
        ),
        BrandConcept(
            number = 4,
            name = "Editoryal Masa",
            colors = BrandColors(
                background = Color(0xFFF3EEE3), surface = Color(0xFFFAF7F0), surfaceHigh = Color(0xFFFFFCF5),
                line = Color(0xFFD3C9B6), text = Color(0xFF1C1915), textSecondary = Color(0xFF5E564A),
                accent = Color(0xFFC93219), onAccent = Color(0xFFFFF8EE), danger = Color(0xFF951A2D), success = Color(0xFF2F6B4B),
                isDark = false,
            ),
            shapes = BrandShapes(card = 6.dp, bubble = 17.dp, button = 4.dp),
            motion = BrandMotion(expandMillis = 320, dismissMillis = 240, springDamping = 0.9f, springStiffness = 380.0f),
            headingFont = "Fraunces",
            bodyFont = "Inter",
        ),
    )
}
