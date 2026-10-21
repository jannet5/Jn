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
            name = "Sende",
            colors = BrandColors(
                background = Color(0xFF0A0E1B), surface = Color(0xFF111628), surfaceHigh = Color(0xFF1A2036),
                line = Color(0xFF2B3552), text = Color(0xFFF3EFE7), textSecondary = Color(0xFFA4AABF),
                accent = Color(0xFFF0B240), onAccent = Color(0xFF1A1204), danger = Color(0xFFF2877A), success = Color(0xFF6CC08B),
                isDark = true,
            ),
            shapes = BrandShapes(card = 20.dp, bubble = 20.dp, button = 18.dp),
            motion = BrandMotion(expandMillis = 420, dismissMillis = 320, springDamping = 0.8f, springStiffness = 380.0f),
            headingFont = "Manrope",
            bodyFont = "Inter",
        ),
        BrandConcept(
            number = 2,
            name = "Sende",
            colors = BrandColors(
                background = Color(0xFFF6EEE1), surface = Color(0xFFFFFBF3), surfaceHigh = Color(0xFFEFE3CF),
                line = Color(0xFFC9B393), text = Color(0xFF2B1D14), textSecondary = Color(0xFF6A5646),
                accent = Color(0xFF0E4F4A), onAccent = Color(0xFFF6EEE1), danger = Color(0xFFB03F1B), success = Color(0xFF0E4F4A),
                isDark = false,
            ),
            shapes = BrandShapes(card = 18.dp, bubble = 22.dp, button = 14.dp),
            motion = BrandMotion(expandMillis = 320, dismissMillis = 220, springDamping = 0.6f, springStiffness = 400.0f),
            headingFont = "Bricolage Grotesque",
            bodyFont = "DM Sans",
        ),
        BrandConcept(
            number = 3,
            name = "Sende",
            colors = BrandColors(
                background = Color(0xFF121416), surface = Color(0xFF1A1D1F), surfaceHigh = Color(0xFF23272A),
                line = Color(0xFF30363B), text = Color(0xFFEEF1F3), textSecondary = Color(0xFF9CA5AD),
                accent = Color(0xFFC8F23A), onAccent = Color(0xFF0D0F11), danger = Color(0xFFFF6B5B), success = Color(0xFF3FD9A0),
                isDark = true,
            ),
            shapes = BrandShapes(card = 12.dp, bubble = 8.dp, button = 8.dp),
            motion = BrandMotion(expandMillis = 220, dismissMillis = 160, springDamping = 0.9f, springStiffness = 800.0f),
            headingFont = "Space Grotesk",
            bodyFont = "Inter",
        ),
        BrandConcept(
            number = 4,
            name = "Sende",
            colors = BrandColors(
                background = Color(0xFFF4EFE4), surface = Color(0xFFFBF8F1), surfaceHigh = Color(0xFFEBE4D3),
                line = Color(0xFFC9BFA7), text = Color(0xFF1B1814), textSecondary = Color(0xFF5C554A),
                accent = Color(0xFFC4361B), onAccent = Color(0xFFFFFBF2), danger = Color(0xFF8A1F2D), success = Color(0xFF2D6A4F),
                isDark = false,
            ),
            shapes = BrandShapes(card = 10.dp, bubble = 8.dp, button = 8.dp),
            motion = BrandMotion(expandMillis = 320, dismissMillis = 260, springDamping = 0.9f, springStiffness = 400.0f),
            headingFont = "Fraunces",
            bodyFont = "Instrument Sans",
        ),
        BrandConcept(
            number = 5,
            name = "Sende",
            colors = BrandColors(
                background = Color(0xFFF2EDE1), surface = Color(0xFFFFFFFF), surfaceHigh = Color(0xFFE9E3D4),
                line = Color(0xFFD9D1C0), text = Color(0xFF0E1330), textSecondary = Color(0xFF555A74),
                accent = Color(0xFF1E35C8), onAccent = Color(0xFFFFFFFF), danger = Color(0xFFBA2727), success = Color(0xFF087048),
                isDark = false,
            ),
            shapes = BrandShapes(card = 18.dp, bubble = 14.dp, button = 14.dp),
            motion = BrandMotion(expandMillis = 320, dismissMillis = 220, springDamping = 0.62f, springStiffness = 380.0f),
            headingFont = "Sora",
            bodyFont = "Instrument Sans",
        ),
    )
}
