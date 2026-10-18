package com.jn.koken.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jn.koken.data.Mention

/** Uygulama simgesinin küçük çizimi: kökten dallanan iki dal */
@Composable
fun LogoMark(modifier: Modifier = Modifier) {
    val bg = MaterialTheme.colorScheme.primary
    val fg = MaterialTheme.colorScheme.onPrimary
    Canvas(modifier.size(40.dp)) {
        drawCircle(bg)
        val w = size.width
        val stroke = Stroke(width = w * 0.075f, cap = StrokeCap.Round)
        val stem = Offset(w * 0.5f, w * 0.78f)
        val fork = Offset(w * 0.5f, w * 0.54f)
        drawLine(fg, stem, fork, stroke.width, StrokeCap.Round)
        drawLine(fg, fork, Offset(w * 0.34f, w * 0.34f), stroke.width, StrokeCap.Round)
        drawLine(fg, fork, Offset(w * 0.66f, w * 0.34f), stroke.width, StrokeCap.Round)
        drawCircle(fg, w * 0.07f, Offset(w * 0.34f, w * 0.30f))
        drawCircle(fg, w * 0.07f, Offset(w * 0.66f, w * 0.30f))
    }
}

/** Küçük, sönük başlık (KÖKEN, ANLAM...) */
@Composable
fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text.uppercase(java.util.Locale("tr", "TR")),
        modifier = modifier,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

/** Dokunulabilir ya da salt görsel hap biçimli etiket */
@Composable
fun Pill(
    text: String,
    modifier: Modifier = Modifier,
    container: Color = MaterialTheme.colorScheme.primaryContainer,
    content: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    onClick: (() -> Unit)? = null,
) {
    // 48dp'lik dokunma alanı, 36dp'lik görünür hap
    val inner: @Composable () -> Unit = {
        Text(
            text,
            modifier = Modifier.padding(horizontal = Sp.lg, vertical = Sp.sm),
            style = MaterialTheme.typography.labelLarge,
            color = content,
        )
    }
    if (onClick != null) {
        Box(modifier.heightIn(min = 48.dp), contentAlignment = Alignment.Center) {
            Surface(onClick = onClick, shape = CircleShape, color = container, content = inner)
        }
    } else {
        Surface(modifier, shape = CircleShape, color = container, content = inner)
    }
}

/** Tek tip kart: ince çerçeve, gölge yok (tasarım kuralı: çerçeve YA DA gölge) */
@Composable
fun KokenCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Column(Modifier.padding(Sp.lg), verticalArrangement = Arrangement.spacedBy(Sp.md)) { content() }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PillRow(items: List<String>, onPick: (String) -> Unit) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(Sp.sm)) {
        items.forEach { Pill(it, onClick = { onPick(it) }) }
    }
}

/**
 * Köken zinciri: en eski dil üstte, sözcüğün kendisi altta.
 * Her basamakta dil, yazılış, okunuş, anlam ve bir sonrakine nasıl geçtiği gösterilir.
 */
@Composable
fun Timeline(steps: List<Mention>, endLanguage: String, endWord: String) {
    val ordered = steps.reversed()
    Column {
        ordered.forEach { m ->
            TimelineRow(last = false, dot = MaterialTheme.colorScheme.secondary) {
                Text(if (m.isRoot) "${m.langName} · kök" else m.langName, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
                Text(
                    m.text,
                    fontFamily = FontFamily.Serif, fontSize = 22.sp, lineHeight = 30.sp,
                    textAlign = TextAlign.Left, color = MaterialTheme.colorScheme.onSurface,
                )
                m.translit?.let {
                    Text(it, style = MaterialTheme.typography.bodyMedium, fontStyle = FontStyle.Italic, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                m.gloss?.let {
                    Text("“$it”", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                m.relation?.let {
                    Text("↓ ${it.label}", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
        TimelineRow(last = true, dot = MaterialTheme.colorScheme.primary) {
            Text(endLanguage, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            Text(
                endWord,
                fontFamily = FontFamily.Serif, fontSize = 26.sp, lineHeight = 32.sp, fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun TimelineRow(last: Boolean, dot: Color, content: @Composable () -> Unit) {
    val line = MaterialTheme.colorScheme.outlineVariant
    Row(Modifier.height(IntrinsicSize.Min)) {
        // sol ray: nokta + aşağı inen çizgi
        Box(
            Modifier.width(24.dp).fillMaxHeight().drawBehind {
                val x = 8.dp.toPx()
                val y = 12.dp.toPx()
                if (!last) drawLine(line, Offset(x, y), Offset(x, size.height), 2.dp.toPx())
                drawCircle(dot, 6.dp.toPx(), Offset(x, y))
            },
        )
        Column(
            Modifier.fillMaxWidth().padding(bottom = if (last) 0.dp else Sp.lg),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) { content() }
    }
}

/** Boş / bulunamadı / hata ekranları için ortak yerleşim */
@Composable
fun StateMessage(
    icon: @Composable () -> Unit,
    title: String,
    body: String,
    modifier: Modifier = Modifier,
    actions: @Composable () -> Unit = {},
) {
    Column(
        modifier.fillMaxWidth().padding(horizontal = Sp.xl, vertical = Sp.xxl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Sp.md),
    ) {
        icon()
        Text(title, style = MaterialTheme.typography.titleLarge, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Text(body, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
        Spacer(Modifier.height(Sp.xs))
        actions()
    }
}
