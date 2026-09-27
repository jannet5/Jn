package com.tekpanel.app.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * TekPanel is deliberately a single, always-dark brand surface (spec 3.4/5.1) rather than a
 * theme that follows the system light/dark switch: the near-black background and quiet
 * outline language are the product's identity, not a preference.
 */
object TekPanelColors {
    val Background = Color(0xFF0B0C0E)
    val Surface = Color(0xFF15161A)
    val SurfaceRaised = Color(0xFF1C1D22)
    val Outline = Color(0xFF33343A)
    val OutlineStrong = Color(0xFF4A4B52)
    val TextPrimary = Color(0xFFF3F4F6)
    val TextSecondary = Color(0xFFA7A9B4)
    val TextTertiary = Color(0xFF6E707A)
    val Accent = Color(0xFFFFFFFF)
    val Danger = Color(0xFFE5484D)
    val Success = Color(0xFF3DD68C)
}
