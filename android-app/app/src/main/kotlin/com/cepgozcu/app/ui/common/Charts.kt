package com.cepgozcu.app.ui.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.cepgozcu.app.net.protocol.GrowthPoint

/** Simple filled line chart of size-over-time, for [com.cepgozcu.app.net.protocol.DiskGrowthResult.timeline]. Pure Canvas, no external chart library. */
@Composable
fun SizeTimelineChart(
    points: List<GrowthPoint>,
    modifier: Modifier = Modifier,
) {
    val lineColor = MaterialTheme.colorScheme.primary
    val fillColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.18f)
    val gridColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.15f)

    Canvas(modifier = modifier.fillMaxWidth().height(160.dp)) {
        if (points.size < 2) return@Canvas
        val maxValue = (points.maxOfOrNull { it.sizeBytes } ?: 1L).coerceAtLeast(1L).toFloat()
        val minValue = (points.minOfOrNull { it.sizeBytes } ?: 0L).toFloat()
        val range = (maxValue - minValue).coerceAtLeast(1f)
        val stepX = size.width / (points.size - 1).coerceAtLeast(1)

        // gridlines
        val gridLines = 3
        repeat(gridLines + 1) { i ->
            val y = size.height * i / gridLines
            drawLine(gridColor, Offset(0f, y), Offset(size.width, y), strokeWidth = 1f)
        }

        fun yFor(value: Long): Float = size.height - ((value - minValue) / range) * size.height

        val linePath = Path()
        val fillPath = Path()
        points.forEachIndexed { index, point ->
            val x = stepX * index
            val y = yFor(point.sizeBytes)
            if (index == 0) {
                linePath.moveTo(x, y)
                fillPath.moveTo(x, size.height)
                fillPath.lineTo(x, y)
            } else {
                linePath.lineTo(x, y)
                fillPath.lineTo(x, y)
            }
        }
        fillPath.lineTo(stepX * (points.size - 1), size.height)
        fillPath.close()

        drawPath(fillPath, brush = Brush.verticalGradient(listOf(fillColor, fillColor.copy(alpha = 0f))))
        drawPath(linePath, color = lineColor, style = Stroke(width = 4f))
    }
}
