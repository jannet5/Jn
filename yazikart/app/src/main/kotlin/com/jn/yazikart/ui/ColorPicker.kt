package com.jn.yazikart.ui // arayüz paketi

import androidx.compose.foundation.background // arka plan boyama
import androidx.compose.foundation.border // çerçeve
import androidx.compose.foundation.clickable // tıklanabilirlik
import androidx.compose.foundation.layout.Arrangement // dizilim aralığı
import androidx.compose.foundation.layout.Box // üst üste yerleşim
import androidx.compose.foundation.layout.Column // alt alta yerleşim
import androidx.compose.foundation.layout.PaddingValues // iç boşluk değerleri
import androidx.compose.foundation.layout.fillMaxWidth // tam genişlik
import androidx.compose.foundation.layout.height // yükseklik
import androidx.compose.foundation.layout.size // boyut
import androidx.compose.foundation.lazy.LazyRow // yatay kaydırılabilir liste
import androidx.compose.foundation.lazy.items // listeye öğe ekleme
import androidx.compose.foundation.shape.CircleShape // daire şekli
import androidx.compose.foundation.shape.RoundedCornerShape // yuvarlak köşe
import androidx.compose.material.icons.Icons // ikon seti
import androidx.compose.material.icons.filled.Check // tik ikonu
import androidx.compose.material.icons.filled.Palette // palet ikonu
import androidx.compose.material3.AlertDialog // açılır pencere
import androidx.compose.material3.Icon // ikon bileşeni
import androidx.compose.material3.MaterialTheme // tema
import androidx.compose.material3.OutlinedTextField // yazı kutusu
import androidx.compose.material3.Slider // kaydırıcı
import androidx.compose.material3.Text // yazı
import androidx.compose.material3.TextButton // yazı buton
import androidx.compose.runtime.Composable // Compose bileşeni işareti
import androidx.compose.runtime.getValue // durum okuma
import androidx.compose.runtime.mutableFloatStateOf // ondalık durum
import androidx.compose.runtime.mutableStateOf // genel durum
import androidx.compose.runtime.remember // durumu hatırlama
import androidx.compose.runtime.setValue // durum yazma
import androidx.compose.ui.Alignment // hizalama
import androidx.compose.ui.Modifier // değiştiriciler
import androidx.compose.ui.draw.clip // kırpma
import androidx.compose.ui.graphics.Color // renk
import androidx.compose.ui.graphics.toArgb // rengi sayıya çevirme
import androidx.compose.ui.unit.dp // ölçü birimi

// Hazır renkler: ilk sırada en çok kullanılan siyah
val PALETTE: List<Int> = listOf(
    0xFF000000, 0xFFFFFFFF, 0xFF1C1C1E, 0xFF6B7280, // siyah, beyaz, koyu gri, gri
    0xFFE53935, 0xFFFF5A36, 0xFFFB8C00, 0xFFFDD835, // kırmızı, mercan, turuncu, sarı
    0xFF43A047, 0xFF00897B, 0xFF26C6DA, 0xFF1E88E5, // yeşil, deniz yeşili, turkuaz, mavi
    0xFF1A237E, 0xFF8E24AA, 0xFFEC407A, 0xFFF8BBD0, // lacivert, mor, pembe, açık pembe
    0xFF6D4C41, 0xFFD7CCC8, 0xFFFFF8E1, 0xFFB2DFDB, // kahve, bej, krem, mint
).map { it.toInt() } // Long sayılar Int renge çevriliyor

// Yatay renk şeridi + "özel renk" düğmesi
@Composable
fun ColorRow(selected: Int, onSelect: (Int) -> Unit) {
    var showCustom by remember { mutableStateOf(false) } // özel renk penceresi açık mı
    LazyRow( // yatay kaydırılabilir şerit
        horizontalArrangement = Arrangement.spacedBy(8.dp), // daireler arası 8dp
        contentPadding = PaddingValues(horizontal = 16.dp), // kenar boşlukları
    ) {
        item { // ilk öğe: özel renk
            Swatch(Color.Transparent, selected = selected !in PALETTE, custom = true) { showCustom = true } // palet ikonu
        }
        items(PALETTE) { c -> // her hazır renk
            Swatch(Color(c), selected = c == selected) { onSelect(c) } // renk dairesi
        }
    }
    if (showCustom) { // özel renk penceresi
        CustomColorDialog(initial = selected, onDismiss = { showCustom = false }) { onSelect(it); showCustom = false } // seçince kapanıyor
    }
}

// Tek bir renk dairesi
@Composable
private fun Swatch(color: Color, selected: Boolean, custom: Boolean = false, onClick: () -> Unit) {
    val ring = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline // seçiliyse vurgu çerçevesi
    Box(
        modifier = Modifier
            .size(44.dp) // dokunması kolay boyut
            .clip(CircleShape) // daire
            .background(if (custom) MaterialTheme.colorScheme.surfaceVariant else color) // dolgu
            .border(if (selected) 3.dp else 1.dp, ring, CircleShape) // çerçeve
            .clickable(onClick = onClick), // tıklanınca seç
        contentAlignment = Alignment.Center, // ikon ortada
    ) {
        if (custom) { // özel renk düğmesi
            Icon(Icons.Default.Palette, contentDescription = "Özel renk", tint = MaterialTheme.colorScheme.onSurface) // palet ikonu
        } else if (selected) { // seçili renk
            val light = (color.red * 0.299f + color.green * 0.587f + color.blue * 0.114f) > 0.6f // renk açık mı
            Icon(Icons.Default.Check, contentDescription = "Seçili", tint = if (light) Color.Black else Color.White) // okunur tik
        }
    }
}

// Ton/doygunluk/parlaklık kaydırıcılarıyla ve HEX koduyla renk seçme penceresi
@Composable
fun CustomColorDialog(initial: Int, onDismiss: () -> Unit, onPick: (Int) -> Unit) {
    val hsv = remember { FloatArray(3).also { android.graphics.Color.colorToHSV(initial, it) } } // başlangıç rengi HSV'ye çevriliyor
    var h by remember { mutableFloatStateOf(hsv[0]) } // ton (0-360)
    var s by remember { mutableFloatStateOf(hsv[1]) } // doygunluk (0-1)
    var v by remember { mutableFloatStateOf(hsv[2]) } // parlaklık (0-1)
    val color = Color.hsv(h, s, v) // seçilen renk
    var hex by remember(color) { mutableStateOf(toHex(color.toArgb())) } // HEX kodu kutusu

    AlertDialog(
        onDismissRequest = onDismiss, // dışına basınca kapanır
        title = { Text("Özel renk") }, // başlık
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) { // alt alta
                Box( // renk önizlemesi
                    Modifier.fillMaxWidth().height(56.dp).clip(RoundedCornerShape(12.dp)).background(color) // dolu dikdörtgen
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)), // ince çerçeve
                )
                Text("Ton", style = MaterialTheme.typography.labelMedium) // etiket
                Slider(value = h, onValueChange = { h = it }, valueRange = 0f..360f) // ton kaydırıcısı
                Text("Doygunluk", style = MaterialTheme.typography.labelMedium) // etiket
                Slider(value = s, onValueChange = { s = it }) // doygunluk kaydırıcısı
                Text("Parlaklık", style = MaterialTheme.typography.labelMedium) // etiket
                Slider(value = v, onValueChange = { v = it }) // parlaklık kaydırıcısı
                OutlinedTextField( // HEX kodu
                    value = hex, // kutudaki yazı
                    onValueChange = { t -> // yazı değişince
                        hex = t // kutu güncelleniyor
                        parseHex(t)?.let { c -> // geçerli bir kodsa
                            val arr = FloatArray(3) // HSV dizisi
                            android.graphics.Color.colorToHSV(c, arr) // koda göre HSV hesaplanıyor
                            h = arr[0]; s = arr[1]; v = arr[2] // kaydırıcılar güncelleniyor
                        }
                    },
                    label = { Text("HEX (örn. #FF5A36)") }, // etiket
                    singleLine = true, // tek satır
                )
            }
        },
        confirmButton = { TextButton(onClick = { onPick(color.toArgb()) }) { Text("Seç") } }, // onay
        dismissButton = { TextButton(onClick = onDismiss) { Text("Vazgeç") } }, // iptal
    )
}

// Renk sayısını "#RRGGBB" metnine çevirir
fun toHex(c: Int): String = String.format("#%06X", c and 0xFFFFFF) // ilk iki hane (saydamlık) atılıyor

// "#RRGGBB" ya da "RRGGBB" metnini renge çevirir; geçersizse null
fun parseHex(text: String): Int? {
    val t = text.trim().removePrefix("#") // # işareti atılıyor
    if (t.length != 6 || !t.all { it.isDigit() || it.lowercaseChar() in 'a'..'f' }) return null // 6 haneli onaltılık değilse geçersiz
    return (0xFF000000.toInt() or t.toInt(16)) // tam opak renk
}
