package com.jn.melodizil.ui.components // Ortak bileşenler

import androidx.compose.foundation.Canvas // Çizim
import androidx.compose.foundation.background // Arka plan
import androidx.compose.foundation.layout.fillMaxWidth // Genişlik
import androidx.compose.foundation.layout.height // Yükseklik
import androidx.compose.foundation.shape.RoundedCornerShape // Köşe
import androidx.compose.material3.MaterialTheme // Tema
import androidx.compose.runtime.Composable // Composable
import androidx.compose.ui.Modifier // Modifier
import androidx.compose.ui.draw.clip // Kırpma
import androidx.compose.ui.geometry.CornerRadius // Köşe
import androidx.compose.ui.geometry.Offset // Konum
import androidx.compose.ui.geometry.Size // Boyut
import androidx.compose.ui.graphics.Color // Renk
import androidx.compose.ui.unit.dp // dp
import com.jn.melodizil.core.Melody // Melodi
import com.jn.melodizil.ui.theme.Radius // Köşe token'ı

/**
 * Seçili pencerenin piyano rulosu: her nota, zamana göre yatay, perdeye göre dikey bir çubuk.
 * Oynatma konumu dikey çizgiyle gösterilir. Kart yüzeyi üstünde çizilir (iç içe kart değil, çizim alanı).
 */
@Composable
fun PianoRoll(melody: Melody, startSec: Float, lengthSec: Float, playheadSec: Float?, modifier: Modifier = Modifier) {
    val primary = MaterialTheme.colorScheme.primary // Nota rengi
    val faint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.25f) // Kılavuz rengi
    val bg = MaterialTheme.colorScheme.surfaceVariant // Arka plan
    val notes = melody.notes.filter { it.endSec > startSec && it.startSec < startSec + lengthSec } // Penceredeki notalar
    val minMidi = (notes.minOfOrNull { it.midi } ?: 60) - 2 // Alt perde
    val maxMidi = (notes.maxOfOrNull { it.midi } ?: 72) + 2 // Üst perde
    val range = (maxMidi - minMidi).coerceAtLeast(12) // En az bir oktav
    Canvas(modifier.fillMaxWidth().height(140.dp).clip(RoundedCornerShape(Radius.sm)).background(bg)) { // Çizim alanı
        val w = size.width; val h = size.height // Boyut
        val rowH = h / range // Perde satırı yüksekliği
        for (i in 0..range step 12) drawLine(faint, Offset(0f, h - i * rowH), Offset(w, h - i * rowH), strokeWidth = 1f) // Oktav kılavuzları
        for (n in notes) { // Notalar
            val x0 = ((n.startSec - startSec) / lengthSec * w).coerceIn(0f, w) // Sol
            val x1 = ((n.endSec - startSec) / lengthSec * w).coerceIn(0f, w) // Sağ
            val y = h - (n.midi - minMidi + 1) * rowH // Üst
            drawRoundRect(primary.copy(alpha = 0.55f + 0.45f * n.velocity), Offset(x0, y), Size((x1 - x0).coerceAtLeast(3f), rowH * 0.9f), CornerRadius(3f, 3f)) // Çubuk
        }
        if (playheadSec != null && playheadSec > 0f) { // Oynatma konumu
            val x = (playheadSec / lengthSec * w).coerceIn(0f, w) // Konum
            drawLine(Color.White.copy(alpha = 0.9f), Offset(x, 0f), Offset(x, h), strokeWidth = 2f) // Çizgi
        }
    }
}
