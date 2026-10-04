package app.kuranoku.ui.components // ortak bileşenler

import androidx.compose.material3.Icon // Material ikon çizici
import androidx.compose.material3.LocalContentColor // varsayılan içerik rengi
import androidx.compose.runtime.Composable // Compose işlevi
import androidx.compose.ui.Modifier // değiştirici
import androidx.compose.ui.graphics.Color // renk
import androidx.compose.ui.graphics.SolidColor // düz boya
import androidx.compose.ui.graphics.StrokeCap // çizgi ucu
import androidx.compose.ui.graphics.StrokeJoin // çizgi birleşimi
import androidx.compose.ui.graphics.vector.ImageVector // vektör ikon
import androidx.compose.ui.graphics.vector.PathParser // SVG yol çözücü
import androidx.compose.ui.unit.Dp // dp tipi
import androidx.compose.ui.unit.dp // dp birimi
import androidx.compose.foundation.layout.size // boyut değiştirici
import app.kuranoku.ui.theme.IconSize // ikon boyut token'ları

/** Lucide ikon setinden (ISC lisansı) gerekenler; hepsi 24'lük ızgarada, strokeWidth 2. */
object Lucide {
    val Search = lucide("search", "M19 11a8 8 0 1 1-16 0a8 8 0 1 1 16 0", "m21 21-4.3-4.3") // ara
    val List = lucide("list", "M3 12h.01", "M3 18h.01", "M3 6h.01", "M8 12h13", "M8 18h13", "M8 6h13") // içindekiler
    val Bookmark = lucide("bookmark", "m19 21-7-4-7 4V5a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2v16z") // yer imi
    val BookmarkCheck = lucide("bookmark-check", "m19 21-7-4-7 4V5a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2Z", "m9 10 2 2 4-4") // yer imli
    val Type = lucide("a-large-small", "M21 14h-5", "M16 16v-3.5a2.5 2.5 0 0 1 5 0V16", "M4.5 13h6", "m3 16 4.5-9 4.5 9") // görünüm (Aa)
    val X = lucide("x", "M18 6 6 18", "m6 6 12 12") // kapat / temizle
    val ChevronRight = lucide("chevron-right", "m9 18 6-6-6-6") // liste oku
    val ChevronLeft = lucide("chevron-left", "m15 18-6-6 6-6") // geri
    val Minus = lucide("minus", "M5 12h14") // küçült
    val Plus = lucide("plus", "M5 12h14", "M12 5v14") // büyüt
    val Check = lucide("check", "M20 6 9 17l-5-5") // seçili
    val Trash = lucide("trash-2", "M3 6h18", "M19 6v14c0 1-1 2-2 2H7c-1 0-2-1-2-2V6", "M8 6V4c0-1 1-2 2-2h4c1 0 2 1 2 2v2") // sil
    val Info = lucide("info", "M22 12a10 10 0 1 1-20 0a10 10 0 1 1 20 0", "M12 16v-4", "M12 8h.01") // hakkında
    val BookOpen = lucide("book-open", "M12 7v14", "M3 18a1 1 0 0 1-1-1V4a1 1 0 0 1 1-1h5a4 4 0 0 1 4 4 4 4 0 0 1 4-4h5a1 1 0 0 1 1 1v13a1 1 0 0 1-1 1h-6a3 3 0 0 0-3 3 3 3 0 0 0-3-3z") // oku
    val RotateCcw = lucide("rotate-ccw", "M3 12a9 9 0 1 0 9-9 9.75 9.75 0 0 0-6.74 2.74L3 8", "M3 3v5h5") // sıfırla
    val Palette = lucide("pipette", "m2 22 1-1h3l9-9", "M3 21v-3l9-9", "m15 6 3.4-3.4a2.1 2.1 0 1 1 3 3L18 9l.4.4a2.1 2.1 0 1 1-3 3l-3.8-3.8a2.1 2.1 0 1 1 3-3l.4.4Z") // özel renk
    val TriangleAlert = lucide("triangle-alert", "m21.73 18-8-14a2 2 0 0 0-3.48 0l-8 14A2 2 0 0 0 4 21h16a2 2 0 0 0 1.73-3", "M12 9v4", "M12 17h.01") // uyarı

    private fun lucide(name: String, vararg paths: String): ImageVector { // SVG yollarından ikon üretir
        val b = ImageVector.Builder(name, 24.dp, 24.dp, 24f, 24f) // 24x24 tuval
        for (d in paths) b.addPath( // her yol
            pathData = PathParser().parsePathString(d).toNodes(), // SVG yol verisi
            fill = null, // dolgu yok
            stroke = SolidColor(Color.Black), // çizgi (Icon tint ile boyanır)
            strokeLineWidth = 2f, // tek kalınlık
            strokeLineCap = StrokeCap.Round, // yuvarlak uç
            strokeLineJoin = StrokeJoin.Round, // yuvarlak birleşim
        )
        return b.build() // ikon
    }
}

/** Tek ikon çizici: boyut sadece 16/20/24, renk metinle aynı ya da muted. */
@Composable
fun Ico(icon: ImageVector, contentDescription: String?, modifier: Modifier = Modifier, size: Dp = IconSize.l, tint: Color = LocalContentColor.current) {
    Icon(icon, contentDescription, modifier.size(size), tint) // Material Icon ile çiziliyor
}
