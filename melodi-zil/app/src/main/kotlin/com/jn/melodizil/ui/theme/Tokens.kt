package com.jn.melodizil.ui.theme // Tasarım token'ları: DESIGN.md ile birebir aynı değerler

import androidx.compose.ui.graphics.Color // Renk
import androidx.compose.ui.unit.dp // dp birimi

/** Renk token'ları (açık). Kontrast değerleri DESIGN.md §3'te. */
object LightColors {
    val background = Color(0xFFFFFFFF) // Ekran arka planı
    val foreground = Color(0xFF16151D) // Ana metin
    val card = Color(0xFFF4F3F9) // Kart yüzeyi
    val primary = Color(0xFF5B3DF5) // Marka, birincil eylem (beyaz üstünde 6.1:1)
    val primaryForeground = Color(0xFFFFFFFF) // Birincil üstü metin
    val secondary = Color(0xFFECE8FF) // İkincil yüzey (chip seçili arka planı)
    val secondaryForeground = Color(0xFF3B23B8) // İkincil üstü metin
    val muted = Color(0xFFEDECF3) // Sönük yüzey (skeleton)
    val mutedForeground = Color(0xFF6B6880) // İkincil metin, ikon (5.4:1)
    val border = Color(0xFFE3E1EC) // Çerçeve
    val destructive = Color(0xFFD63B3B) // Hata
    val success = Color(0xFF1F9D5B) // Başarı
}

/** Renk token'ları (koyu). */
object DarkColors {
    val background = Color(0xFF121218) // Ekran arka planı (saf siyah değil)
    val foreground = Color(0xFFF2F1F7) // Ana metin
    val card = Color(0xFF1C1B24) // Kart yüzeyi
    val primary = Color(0xFFA596FF) // Marka (koyu üstünde 7.5:1)
    val primaryForeground = Color(0xFF1B0F5C) // Birincil üstü metin
    val secondary = Color(0xFF2A2347) // İkincil yüzey
    val secondaryForeground = Color(0xFFD8D0FF) // İkincil üstü metin
    val muted = Color(0xFF26252F) // Sönük yüzey
    val mutedForeground = Color(0xFFA09EB3) // İkincil metin (7.1:1)
    val border = Color(0xFF2C2B38) // Çerçeve
    val destructive = Color(0xFFFF6B6B) // Hata
    val success = Color(0xFF4CCB85) // Başarı
}

/** Boşluk skalası: 4·8·12·16·24·32·48·64. */
object Space {
    val xs = 4.dp // 4
    val sm = 8.dp // 8
    val md = 12.dp // 12
    val lg = 16.dp // 16 — ekran kenarı, kart padding
    val xl = 24.dp // 24 — bölümler arası
    val xxl = 32.dp // 32
    val xxxl = 48.dp // 48
}

/** Köşe yarıçapları: en fazla 3 değer. */
object Radius {
    val sm = 8.dp // Input, badge, chip
    val md = 16.dp // Kart, buton
    val lg = 24.dp // Sheet, büyük görsel
}

/** Boyut token'ları. */
object Size {
    val buttonHeight = 52.dp // Birincil/ikincil buton yüksekliği
    val inputHeight = 56.dp // Input yüksekliği
    val rowHeight = 64.dp // Liste satırı
    val iconSm = 20.dp // Liste ve buton ikonları
    val iconMd = 24.dp // Header ve sekme ikonları
    val touchMin = 48.dp // En küçük dokunma alanı
    val borderWidth = 1.dp // Kart çerçevesi
}
