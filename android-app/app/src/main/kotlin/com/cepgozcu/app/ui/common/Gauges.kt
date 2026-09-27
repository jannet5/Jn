package com.cepgozcu.app.ui.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.min

/** A radial "donut" gauge for a single 0-100 percentage (CPU, or any single-value metric). Pure Canvas — no chart library needed. */
@Composable
fun RadialGauge(
    percent: Float,
    label: String,
    modifier: Modifier = Modifier,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    progressColor: Color = MaterialTheme.colorScheme.primary,
    size: androidx.compose.ui.unit.Dp = 120.dp,
) {
    val clamped = percent.coerceIn(0f, 100f)
    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(size)) {
            val strokeWidth = this.size.minDimension * 0.12f
            val diameter = this.size.minDimension - strokeWidth
            val topLeft = androidx.compose.ui.geometry.Offset(
                (this.size.width - diameter) / 2f,
                (this.size.height - diameter) / 2f,
            )
            val arcSize = Size(diameter, diameter)
            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
            )
            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = 360f * (clamped / 100f),
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
            )
        }
        Box(contentAlignment = Alignment.Center) {
            androidx.compose.foundation.layout.Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(formatPercent(clamped.toDouble()), style = MaterialTheme.typography.headlineSmall)
                Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

/** A horizontal usage bar (e.g. per-disk used/free), with a label row above. */
@Composable
fun UsageBar(
    percent: Float,
    modifier: Modifier = Modifier,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    progressColor: Color = MaterialTheme.colorScheme.primary,
    height: androidx.compose.ui.unit.Dp = 10.dp,
) {
    val clamped = percent.coerceIn(0f, 100f) / 100f
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(50))
            .background(trackColor),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(min(clamped, 1f))
                .height(height)
                .clip(RoundedCornerShape(50))
                .background(progressColor),
        )
    }
}
