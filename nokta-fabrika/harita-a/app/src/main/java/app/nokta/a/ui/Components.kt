package app.nokta.a.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import app.nokta.a.ui.theme.LocalNokta

/** Satırdaki "nokta": boşken halka, alınınca 200ms'de dolar, içinde onay çizgisi. 22dp, 1.5dp halka. */
@Composable
fun DoneDot(done: Boolean, modifier: Modifier = Modifier) {
    val c = LocalNokta.current
    val fill by animateFloatAsState(if (done) 1f else 0f, tween(200, easing = FastOutSlowInEasing), label = "fill")
    val ring by animateColorAsState(if (done) c.dot else c.inkSoft, tween(200), label = "ring")
    Canvas(modifier.size(22.dp)) {
        val r = size.minDimension / 2
        val sw = 1.5.dp.toPx()
        drawCircle(ring, radius = r - sw / 2, style = Stroke(sw))
        if (fill > 0f) {
            drawCircle(c.dot, radius = (r - sw / 2) * fill)
            val a = fill
            val s = Stroke(1.75.dp.toPx(), cap = StrokeCap.Round)
            val p1 = Offset(size.width * 0.30f, size.height * 0.52f)
            val p2 = Offset(size.width * 0.44f, size.height * 0.66f)
            val p3 = Offset(size.width * 0.71f, size.height * 0.36f)
            drawLine(c.onDot.copy(alpha = a), p1, p2, s.width, StrokeCap.Round)
            drawLine(c.onDot.copy(alpha = a), p2, p3, s.width, StrokeCap.Round)
        }
    }
}

/** Tutamaç: 3x2 nokta, 4dp çap, 4dp aralık. */
@Composable
fun GripIcon(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier.size(12.dp, 20.dp)) {
        val r = 2.dp.toPx()
        for (row in 0..2) for (col in 0..1) {
            drawCircle(color, r, Offset(r + col * 8.dp.toPx(), r + row * 8.dp.toPx()))
        }
    }
}

/** Sil "x": 20dp ikon, 1.75dp çizgi. */
@Composable
fun CrossIcon(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier.size(20.dp)) {
        val w = 1.75.dp.toPx()
        val m = 4.dp.toPx()
        drawLine(color, Offset(m, m), Offset(size.width - m, size.height - m), w, StrokeCap.Round)
        drawLine(color, Offset(size.width - m, m), Offset(m, size.height - m), w, StrokeCap.Round)
    }
}

/** Üç nokta menü ikonu. */
@Composable
fun MoreIcon(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier.size(20.dp)) {
        val r = 1.75.dp.toPx()
        for (i in 0..2) drawCircle(color, r, Offset(size.width / 2, r + 2.dp.toPx() + i * 6.dp.toPx()))
    }
}

@Composable
fun CenterBox(modifier: Modifier = Modifier, content: @Composable () -> Unit) =
    Box(modifier, contentAlignment = Alignment.Center) { content() }
