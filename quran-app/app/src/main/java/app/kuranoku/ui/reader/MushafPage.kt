package app.kuranoku.ui.reader // okuma ekranı

import androidx.compose.foundation.background // zemin
import androidx.compose.foundation.layout.Arrangement // dizilim
import androidx.compose.foundation.layout.Box // kutu
import androidx.compose.foundation.layout.BoxWithConstraints // ölçülü kutu
import androidx.compose.foundation.layout.Column // dikey dizi
import androidx.compose.foundation.layout.Row // yatay dizi
import androidx.compose.foundation.layout.fillMaxSize // tam boyut
import androidx.compose.foundation.layout.fillMaxWidth // tam genişlik
import androidx.compose.foundation.layout.height // yükseklik
import androidx.compose.foundation.layout.padding // iç boşluk
import androidx.compose.foundation.layout.width // genişlik
import androidx.compose.material3.Text // metin
import androidx.compose.runtime.Composable // Compose işlevi
import androidx.compose.runtime.remember // hatırlama
import androidx.compose.ui.Alignment // hizalama
import androidx.compose.ui.Modifier // değiştirici
import androidx.compose.ui.draw.drawBehind // arkaya çizim
import androidx.compose.ui.geometry.CornerRadius // köşe
import androidx.compose.ui.geometry.Offset // nokta
import androidx.compose.ui.geometry.Size // boyut
import androidx.compose.ui.graphics.Color // renk
import androidx.compose.ui.graphics.Shadow // gölge
import androidx.compose.ui.graphics.TransformOrigin // dönüşüm merkezi
import androidx.compose.ui.graphics.drawscope.Stroke // çizgi
import androidx.compose.ui.graphics.drawscope.rotate // döndürme
import androidx.compose.ui.layout.Layout // özel yerleşim
import androidx.compose.ui.platform.LocalDensity // yoğunluk
import androidx.compose.ui.text.TextStyle // yazı stili
import androidx.compose.ui.text.font.FontFamily // aile
import androidx.compose.ui.text.font.FontWeight // kalınlık
import androidx.compose.ui.text.style.LineHeightStyle // satır yüksekliği stili
import androidx.compose.ui.text.style.TextDirection // yön
import androidx.compose.ui.unit.Constraints // ölçü sınırları
import androidx.compose.ui.unit.Dp // dp tipi
import androidx.compose.ui.unit.dp // dp
import androidx.compose.ui.unit.sp // sp
import app.kuranoku.data.MushafLine // mushaf satırı
import app.kuranoku.data.Quran // Kur'an
import app.kuranoku.data.ReaderSettings // ayarlar
import app.kuranoku.data.isAyahEnd // ayet sonu mu
import app.kuranoku.ui.theme.Space // boşluklar
import app.kuranoku.ui.theme.family // yazı tipi ailesi
import kotlin.math.roundToInt // yuvarlama

/** Medine mushafı sayfası: 15 satır, her satırda basılı mushaftaki kelimelerin aynısı. [zoom] ile net büyür. */
@Composable
fun MushafPage(
    quran: Quran, // Kur'an
    page: Int, // sayfa
    s: ReaderSettings, // görünüm
    highlight: AyahRef?, // vurgulu ayet
    highlightAlpha: Float, // vurgu görünürlüğü
    width: Dp, // sayfa genişliği (büyütülmüş)
    height: Dp, // sayfa yüksekliği (büyütülmüş)
    zoom: Float, // büyütme oranı (bilgi yazıları da büyüsün)
) {
    val ink = Color(s.inkColor) // yazı rengi
    val ornament = Color(s.ornamentColor) // süs rengi
    val bilgiRengi = ink.copy(alpha = 0.62f) // üst/alt bilgi rengi
    val yogunluk = LocalDensity.current // yoğunluk
    val golge = embossShadow(s, yogunluk.density) // kabartma
    val satirlar = remember(quran, page) { quran.mushafLines(page) } // sayfa satırları
    val sure = remember(quran, page) { quran.surahOfPage(page) } // üst bilgi suresi
    val cuz = remember(quran, page) { quran.juzOfPage(page) } // üst bilgi cüzü
    val ilkSayfalar = page <= 2 // Fâtiha ve Bakara başı: ortalanmış özel sayfa

    Column(Modifier.width(width).height(height).padding(horizontal = Space.l * zoom, vertical = Space.s * zoom)) { // sayfa
        Row(Modifier.fillMaxWidth().padding(horizontal = Space.xs * zoom), horizontalArrangement = Arrangement.SpaceBetween) { // üst bilgi
            Text(sure.turkishName, color = bilgiRengi, fontSize = 13.sp * zoom, fontWeight = FontWeight.Medium) // sure adı
            Text("$cuz. Cüz", color = bilgiRengi, fontSize = 13.sp * zoom, fontWeight = FontWeight.Medium) // cüz
        }
        BoxWithConstraints( // çerçeveli metin alanı
            Modifier.weight(1f).fillMaxWidth().padding(vertical = Space.s * zoom)
                .then(if (s.frame) Modifier.drawBehind { mushafFrame(ornament) } else Modifier) // çerçeve
                .padding(horizontal = (if (s.frame) Space.m else Space.xs) * zoom, vertical = (if (s.frame) Space.s else 0.dp) * zoom), // çerçeve içi
        ) {
            val icGenislikPx = with(yogunluk) { maxWidth.toPx() } // satır genişliği
            val satirYuksekligiPx = with(yogunluk) { maxHeight.toPx() } / 15f // 15 eşit satır
            val yaziPx = minOf(icGenislikPx / s.font.lineEm, satirYuksekligiPx / 1.45f) // genişliğe ve yüksekliğe sığan yazı boyutu
            val yazi = with(yogunluk) { yaziPx.toSp() } // sp'ye çevrildi
            val satirDp = with(yogunluk) { satirYuksekligiPx.toDp() } // satır yüksekliği
            val stil = TextStyle( // kelime stili
                fontFamily = s.font.family(), fontSize = yazi, lineHeight = with(yogunluk) { satirYuksekligiPx.toSp() }, color = ink, // KFGQPC Hafs
                textDirection = TextDirection.Rtl, shadow = golge, // sağdan sola, kabartma
                lineHeightStyle = LineHeightStyle(LineHeightStyle.Alignment.Center, LineHeightStyle.Trim.None), // satır ortası
            )
            Column(Modifier.fillMaxSize(), verticalArrangement = if (ilkSayfalar) Arrangement.Center else Arrangement.Top) { // satırlar
                val goster = if (ilkSayfalar) 8..15 else 1..15 // ilk iki sayfada üst yarı süs alanı
                val harita = satirlar.associateBy { it.line } // satır no → satır
                for (no in goster) {
                    val m = Modifier.fillMaxWidth().height(satirDp) // satır kutusu
                    when (val l = harita[no]) {
                        null -> Box(m) // boş satır
                        is MushafLine.Header -> MushafHeader(quran.surahs[l.sura - 1].arabicName, ornament, ink, s.font.family(), yazi.value, golge, m) // sure başlığı
                        is MushafLine.Basmala -> Box(m, contentAlignment = Alignment.Center) { Text(BASMALA, style = stil, softWrap = false) } // besmele
                        is MushafLine.Words -> MushafLineView(l, s.font::ayahMark, stil, highlight, highlightAlpha, ornament, centerOnly = ilkSayfalar, modifier = m) // kelimeler
                    }
                }
            }
        }
        Row(Modifier.fillMaxWidth().padding(horizontal = Space.xs * zoom), horizontalArrangement = Arrangement.End) { // alt bilgi: sağ altta sayfa no
            Text(page.toString(), color = bilgiRengi, fontSize = 14.sp * zoom, fontWeight = FontWeight.Bold) // sayfa numarası
        }
    }
}

/**
 * Tek mushaf satırı: kelimeler sağdan sola dizilir. Satır doğal haliyle dar ise kelime araları eşit açılır
 * (sure sonu gibi çok kısa satırlar ortalanır); genişse satır yatayda hafifçe sıkıştırılır. Harfler asla birbirine binmez.
 */
@Composable
private fun MushafLineView(l: MushafLine.Words, isaret: (String) -> String, stil: TextStyle, highlight: AyahRef?, alpha: Float, ornament: Color, centerOnly: Boolean, modifier: Modifier) {
    val ayetler = remember(l) { // her kelimenin ait olduğu ayet
        var a = l.ayah // satırın ilk ayeti
        l.words.map { w -> val bu = a; if (isAyahEnd(w)) a++; bu } // ayet sonu işaretinden sonra sonraki ayet
    }
    Layout(
        modifier = modifier,
        content = {
            l.words.forEachIndexed { i, w ->
                val vurgu = highlight != null && alpha > 0f && highlight.sura == l.sura && highlight.ayah == ayetler[i] // vurgulu mu
                Text(if (isAyahEnd(w)) isaret(w) else w, style = stil, softWrap = false, maxLines = 1, modifier = if (vurgu) Modifier.background(ornament.copy(alpha = 0.30f * alpha)) else Modifier) // kelime
            }
        },
    ) { olcumler, sinir ->
        val parcalar = olcumler.map { it.measure(Constraints()) } // her kelime doğal boyutunda
        val toplam = parcalar.sumOf { it.width } // kelimelerin toplam genişliği
        val enAzAra = (stil.fontSize.toPx() * 0.22f).roundToInt() // yazı tipindeki boşluk kadar en az ara
        val dogal = toplam + enAzAra * (parcalar.size - 1) // satırın doğal genişliği
        val W = sinir.maxWidth // kullanılabilir genişlik
        val H = sinir.maxHeight // satır yüksekliği
        layout(W, H) {
            val n = parcalar.size // kelime sayısı
            if (dogal > W) { // satır sığmıyor: yatayda sıkıştır
                val oran = W.toFloat() / dogal // sıkıştırma oranı
                var x = W.toFloat() // sağ kenardan başla
                parcalar.forEach { p ->
                    x -= p.width * oran // kelimenin sol kenarı
                    p.placeWithLayer(x.roundToInt(), (H - p.height) / 2) { scaleX = oran; transformOrigin = TransformOrigin(0f, 0.5f) } // sıkıştırılmış kelime
                    x -= enAzAra * oran // ara
                }
            } else {
                val yay = !centerOnly && n > 1 && dogal >= W * 0.78f // satırı iki yana yay (kısa satırlar ortalanır)
                val gerdir = if (yay) minOf(W.toFloat() / dogal, 1.06f) else 1f // önce harfleri en fazla %6 yatay genişlet (aralar fazla açılmasın)
                val ara = if (yay) (W - toplam * gerdir) / (n - 1) else enAzAra.toFloat() // kalan boşluk kelime aralarına eşit
                var x = if (yay) W.toFloat() else W - (W - dogal) / 2f // başlangıç (sağ)
                parcalar.forEach { p ->
                    x -= p.width * gerdir // kelimenin sol kenarı
                    if (gerdir == 1f) p.place(x.roundToInt(), (H - p.height) / 2) // yerleştir
                    else p.placeWithLayer(x.roundToInt(), (H - p.height) / 2) { scaleX = gerdir; transformOrigin = TransformOrigin(0f, 0.5f) } // hafif genişletilmiş kelime
                    x -= ara // ara
                }
            }
        }
    }
}

/** Mushaf satırına sığan süslü sure başlığı. */
@Composable
private fun MushafHeader(name: String, ornament: Color, ink: Color, family: FontFamily, fontSp: Float, shadow: Shadow?, modifier: Modifier) {
    Box(
        modifier.padding(vertical = 2.dp).drawBehind { // band
            val r = CornerRadius(size.height / 2) // hap şekli
            drawRoundRect(ornament.copy(alpha = 0.09f), cornerRadius = r) // hafif dolgu
            drawRoundRect(ornament, cornerRadius = r, style = Stroke(1.4.dp.toPx())) // dış çizgi
            val i = 3.dp.toPx() // iç çizgi aralığı
            drawRoundRect(ornament.copy(alpha = 0.7f), topLeft = Offset(i, i), size = Size(size.width - 2 * i, size.height - 2 * i), cornerRadius = CornerRadius(size.height / 2 - i), style = Stroke(0.7.dp.toPx())) // iç çizgi
            for (x in listOf(size.height * 0.9f, size.width - size.height * 0.9f)) rotate(45f, Offset(x, size.height / 2)) { // iki yanda baklava
                val k = 3.5.dp.toPx() // süs boyutu
                drawRect(ornament, Offset(x - k, size.height / 2 - k), Size(2 * k, 2 * k)) // kare (45° = baklava)
            }
        },
        contentAlignment = Alignment.Center, // ortalı
    ) {
        Text(name, style = TextStyle(fontFamily = family, fontSize = (fontSp * 0.95f).sp, color = ink, textDirection = TextDirection.Rtl, shadow = shadow), softWrap = false) // sure adı
    }
}
