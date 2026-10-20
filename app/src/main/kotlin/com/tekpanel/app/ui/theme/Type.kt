package com.tekpanel.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Sizes only; no fixed `lineHeight` or `maxLines` is applied to message bodies, so text reflows
 * under 200% font scaling instead of clipping.
 */
object BrandFonts {
    var heading: FontFamily = FontFamily.Default
    var body: FontFamily = FontFamily.Default
}

fun brandTypography(): Typography = Typography(
    titleLarge = TextStyle(fontFamily = BrandFonts.heading, fontWeight = FontWeight.SemiBold, fontSize = 22.sp, letterSpacing = (-0.2).sp),
    titleMedium = TextStyle(fontFamily = BrandFonts.heading, fontWeight = FontWeight.SemiBold, fontSize = 16.sp),
    titleSmall = TextStyle(fontFamily = BrandFonts.heading, fontWeight = FontWeight.SemiBold, fontSize = 14.sp),
    bodyLarge = TextStyle(fontFamily = BrandFonts.body, fontWeight = FontWeight.Normal, fontSize = 16.sp),
    bodyMedium = TextStyle(fontFamily = BrandFonts.body, fontWeight = FontWeight.Normal, fontSize = 15.sp),
    bodySmall = TextStyle(fontFamily = BrandFonts.body, fontWeight = FontWeight.Normal, fontSize = 13.sp),
    labelLarge = TextStyle(fontFamily = BrandFonts.body, fontWeight = FontWeight.Medium, fontSize = 14.sp),
    labelMedium = TextStyle(fontFamily = BrandFonts.body, fontWeight = FontWeight.Medium, fontSize = 12.sp),
    labelSmall = TextStyle(fontFamily = BrandFonts.body, fontWeight = FontWeight.Medium, fontSize = 11.sp),
)
