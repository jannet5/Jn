package com.jn.winremote.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

/**
 * A small, dependency-free line chart for a rolling history of values
 * (CPU%, RAM%, …). No axis chrome — this is a glanceable trend indicator,
 * not an analytical chart.
 *
 * @param values oldest-first; only the visual shape matters, so any size
 *   (including 0 or 1) renders without crashing.
 * @param minValue/maxValue fixed scale (e.g. 0f..100f for a percentage);
 *   when null the min/max of [values] itself is used.
 */
@Composable
fun Sparkline(
    values: List<Float>,
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colorScheme.primary,
    fillColor: Color = lineColor.copy(alpha = 0.15f),
    minValue: Float? = null,
    maxValue: Float? = null,
    strokeWidthDp: Float = 2f,
) {
    Canvas(modifier = modifier.fillMaxWidth().height(48.dp)) {
        if (values.isEmpty()) return@Canvas
        val lo = minValue ?: values.min()
        val hi = maxValue ?: values.max()
        val range = (hi - lo).let { if (it <= 0f) 1f else it }
        val width = size.width
        val height = size.height
        val stepX = if (values.size > 1) width / (values.size - 1) else 0f

        fun yFor(v: Float): Float = height - ((v - lo) / range) * height

        val points = values.mapIndexed { index, v -> Offset(index * stepX, yFor(v).coerceIn(0f, height)) }

        if (points.size == 1) {
            drawCircle(color = lineColor, radius = 3f, center = points[0])
            return@Canvas
        }

        val linePath = androidx.compose.ui.graphics.Path().apply {
            moveTo(points[0].x, points[0].y)
            for (i in 1 until points.size) lineTo(points[i].x, points[i].y)
        }
        val fillPath = androidx.compose.ui.graphics.Path().apply {
            addPath(linePath)
            lineTo(points.last().x, height)
            lineTo(points.first().x, height)
            close()
        }
        drawPath(fillPath, color = fillColor)
        drawPath(linePath, color = lineColor, style = Stroke(width = strokeWidthDp.dp.toPx()))
    }
}
