package app.kuranoku.ui.reader // okuma ekranı

import androidx.compose.foundation.Canvas // serbest çizim
import androidx.compose.foundation.layout.Arrangement // dizilim
import androidx.compose.foundation.layout.Box // kutu
import androidx.compose.foundation.layout.Column // dikey dizi
import androidx.compose.foundation.layout.Row // yatay dizi
import androidx.compose.foundation.layout.fillMaxSize // tam boyut
import androidx.compose.foundation.layout.fillMaxWidth // tam genişlik
import androidx.compose.foundation.layout.padding // iç boşluk
import androidx.compose.foundation.text.InlineTextContent // metin içi bileşen
import androidx.compose.material3.Text // metin
import androidx.compose.runtime.Composable // Compose işlevi
import androidx.compose.runtime.remember // hatırlama
import androidx.compose.ui.Alignment // hizalama
import androidx.compose.ui.Modifier // değiştirici
import androidx.compose.ui.draw.drawBehind // arkaya çizim
import androidx.compose.ui.geometry.CornerRadius // köşe
import androidx.compose.ui.geometry.Offset // nokta
import androidx.compose.ui.geometry.Size // boyut
import androidx.compose.ui.graphics.BlendMode // karışım modu
import androidx.compose.ui.graphics.Brush // fırça
import androidx.compose.ui.graphics.Color // renk
import androidx.compose.ui.graphics.ColorFilter // renk filtresi
import androidx.compose.ui.graphics.ColorMatrix // renk matrisi
import androidx.compose.ui.graphics.ImageBitmap // bitmap
import androidx.compose.ui.graphics.ImageShader // desen gölgelendirici
import androidx.compose.ui.graphics.Path // yol
import androidx.compose.ui.graphics.ShaderBrush // desen fırçası
import androidx.compose.ui.graphics.Shadow // yazı gölgesi
import androidx.compose.ui.graphics.TileMode // döşeme
import androidx.compose.ui.graphics.drawscope.DrawScope // çizim kapsamı
import androidx.compose.ui.graphics.drawscope.Stroke // çizgi stili
import androidx.compose.ui.graphics.drawscope.rotate // döndürme
import androidx.compose.ui.res.imageResource // bitmap kaynağı
import androidx.compose.ui.platform.LocalDensity // yoğunluk
import androidx.compose.ui.text.AnnotatedString // biçimli metin
import androidx.compose.ui.text.Placeholder // yer tutucu
import androidx.compose.ui.text.PlaceholderVerticalAlign // yer tutucu hizası
import androidx.compose.ui.text.SpanStyle // aralık stili
import androidx.compose.ui.text.TextStyle // yazı stili
import androidx.compose.foundation.text.appendInlineContent // metin içi bileşen ekleme
import androidx.compose.ui.text.buildAnnotatedString // biçimli metin kurucu
import androidx.compose.ui.text.font.FontFamily // aile
import androidx.compose.ui.text.font.FontWeight // kalınlık
import androidx.compose.ui.text.style.LineHeightStyle // satır yüksekliği stili
import androidx.compose.ui.text.style.TextAlign // hiza
import androidx.compose.ui.text.style.TextDirection // yön
import androidx.compose.ui.text.withStyle // stil uygulama
import androidx.compose.ui.unit.TextUnit // metin birimi
import androidx.compose.ui.unit.dp // dp
import androidx.compose.ui.unit.em // em
import androidx.compose.ui.unit.sp // sp
import app.kuranoku.R // kaynaklar
import app.kuranoku.data.PageBlock // sayfa blokları
import app.kuranoku.data.Quran // Kur'an
import app.kuranoku.data.ReaderSettings // ayarlar
import app.kuranoku.data.toArabicDigits // Arapça rakam
import app.kuranoku.ui.theme.Space // boşluklar
import app.kuranoku.ui.theme.family // yazı tipi ailesi
import app.kuranoku.ui.theme.isDarkPage // koyu sayfa mı
import app.kuranoku.ui.theme.lineHeightFactor // satır yüksekliği
import kotlin.math.PI // pi
import kotlin.math.cos // kosinüs
import kotlin.math.sin // sinüs

/** %100 boyuttaki Arapça yazı büyüklüğü. */
val BASE_FONT_SIZE = 22.sp

/** Vurgulanacak ayet (aramadan gelince). */
data class AyahRef(val sura: Int, val ayah: Int)

/** Gerçek kağıt görünümü: düz renk + çarpma karışımlı lif/tane dokusu + kenarlarda hafif kararma. */
@Composable
fun Modifier.paper(s: ReaderSettings): Modifier {
    val doku = ImageBitmap.imageResource(R.drawable.paper_texture) // doku karosu
    val firca = remember(doku) { ShaderBrush(ImageShader(doku, TileMode.Repeated, TileMode.Repeated)) } // tekrarlanan desen
    val koyu = s.isDarkPage() // koyu sayfa mı
    return drawBehind {
        drawRect(Color(s.pageColor)) // sayfa rengi
        if (s.texture) { // doku açıksa
            if (!koyu) drawRect(firca, blendMode = BlendMode.Multiply) // açık sayfada lifler koyulaştırır
            else drawRect(firca, alpha = 0.85f, colorFilter = InvertFilter, blendMode = BlendMode.Screen) // koyu sayfada lifler açık görünür
            drawRect( // kenarlarda hafif kararma (gerçek kağıt ışığı)
                Brush.radialGradient(
                    0.55f to Color.Transparent, 1f to Color.Black.copy(alpha = if (koyu) 0.28f else 0.10f), // merkez temiz, kenar koyu
                    center = center, radius = maxOf(size.width, size.height) * 0.72f, // tüm sayfayı kaplar
                ),
            )
        }
    }
}

private val InvertFilter = ColorFilter.colorMatrix(ColorMatrix(floatArrayOf(-1f, 0f, 0f, 0f, 255f, 0f, -1f, 0f, 0f, 255f, 0f, 0f, -1f, 0f, 255f, 0f, 0f, 0f, 1f, 0f))) // renkleri ters çevirir

/** Mushaf çerçevesi: dış kalın + iç ince çizgi ve köşelerde küçük baklava. */
fun DrawScope.mushafFrame(color: Color) {
    val dis = 1.6.dp.toPx() // dış çizgi kalınlığı
    val ic = 0.8.dp.toPx() // iç çizgi kalınlığı
    val ara = 4.dp.toPx() // iki çizgi arası
    drawRect(color, topLeft = Offset(dis / 2, dis / 2), size = Size(size.width - dis, size.height - dis), style = Stroke(dis)) // dış çerçeve
    drawRect(color.copy(alpha = 0.8f), topLeft = Offset(ara, ara), size = Size(size.width - 2 * ara, size.height - 2 * ara), style = Stroke(ic)) // iç çerçeve
    val k = 5.dp.toPx() // baklava yarıçapı
    for (p in listOf(Offset(0f, 0f), Offset(size.width, 0f), Offset(0f, size.height), Offset(size.width, size.height))) { // dört köşe
        val yol = Path().apply { moveTo(p.x, p.y - k); lineTo(p.x + k, p.y); lineTo(p.x, p.y + k); lineTo(p.x - k, p.y); close() } // baklava
        drawPath(yol, color) // çiziliyor
    }
}

/** Ayet sonu gülü: çift daire ve sekiz köşeli yıldız. */
@Composable
private fun AyahMarker(n: Int, ornament: Color, ink: Color, family: FontFamily, fontSize: TextUnit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { // ortalı kutu
        Canvas(Modifier.fillMaxSize()) { // süs çizimi
            val r = minOf(size.width, size.height) / 2f - 1.dp.toPx() // dış yarıçap
            drawCircle(ornament.copy(alpha = 0.10f), r) // hafif dolgu
            drawCircle(ornament, r, style = Stroke(1.2.dp.toPx())) // dış daire
            drawCircle(ornament.copy(alpha = 0.7f), r * 0.80f, style = Stroke(0.6.dp.toPx())) // iç daire
            for (i in 0 until 8) { // sekiz küçük nokta (yıldız uçları)
                val a = i * PI / 4 // açı
                drawCircle(ornament, 0.9.dp.toPx(), Offset(center.x + (r * cos(a)).toFloat(), center.y + (r * sin(a)).toFloat())) // nokta
            }
        }
        Text( // ayet numarası
            n.toArabicDigits(), // Arapça rakamlarla
            style = TextStyle(fontFamily = family, fontSize = fontSize * if (n >= 100) 0.36f else 0.42f, color = ink, lineHeight = fontSize * 0.5f, fontWeight = FontWeight.Normal), // küçük ve ortalı
        )
    }
}

/** Bir mushaf sayfası: üst bilgi, sure başlıkları, besmele, ayet paragrafları ve sayfa numarası. */
@Composable
fun QuranPage(
    quran: Quran, // Kur'an
    page: Int, // sayfa
    s: ReaderSettings, // görünüm
    highlight: AyahRef?, // vurgulanacak ayet
    highlightAlpha: Float, // vurgunun görünürlüğü (sönerek kaybolur)
    modifier: Modifier = Modifier, // değiştirici
) {
    val ink = Color(s.inkColor) // yazı rengi
    val ornament = Color(s.ornamentColor) // süs rengi
    val family = s.font.family() // yazı tipi
    val fontSize = BASE_FONT_SIZE * s.scale // ölçekli boyut
    val dens = LocalDensity.current.density // yoğunluk
    val koyu = s.isDarkPage() // koyu sayfa mı
    val golge = if (!s.texture) null // doku kapalıysa kabartma yok
    else if (koyu) Shadow(Color.Black.copy(alpha = 0.75f), Offset(0f, 1.1f * dens), 1.2f * dens) // koyu sayfada içe basılmış
    else Shadow(Color.White.copy(alpha = 0.7f), Offset(0f, 0.9f * dens), 0.6f * dens) // açık sayfada mürekkep kağıda basılmış gibi
    val ayetStili = TextStyle( // ayet metni stili
        fontFamily = family, fontSize = fontSize, lineHeight = fontSize * s.font.lineHeightFactor(), color = ink, // yazı tipi, boyut, renk
        textAlign = TextAlign.Justify, textDirection = TextDirection.Rtl, shadow = golge, // iki yana yaslı, sağdan sola
        lineHeightStyle = LineHeightStyle(LineHeightStyle.Alignment.Center, LineHeightStyle.Trim.None), // satır ortası
    )
    val bloklar = remember(quran, page) { quran.blocksOf(page) } // sayfa blokları
    val sure = remember(quran, page) { quran.surahOfPage(page) } // üst bilgi suresi
    val cuz = remember(quran, page) { quran.juzOfPage(page) } // üst bilgi cüzü
    val bilgiRengi = ink.copy(alpha = 0.62f) // üst/alt bilgi rengi

    Column(modifier, verticalArrangement = Arrangement.SpaceBetween) { // üstte içerik, altta sayfa no
        Column(verticalArrangement = Arrangement.spacedBy(Space.s)) { // içerik
            Row(Modifier.fillMaxWidth().padding(horizontal = Space.xs), horizontalArrangement = Arrangement.SpaceBetween) { // üst bilgi satırı
                Text(sure.turkishName, color = bilgiRengi, fontSize = 13.sp, fontWeight = FontWeight.Medium) // sure adı
                Text("$cuz. Cüz", color = bilgiRengi, fontSize = 13.sp, fontWeight = FontWeight.Medium) // cüz
            }
            Column( // çerçeveli metin alanı
                Modifier.fillMaxWidth().then(if (s.frame) Modifier.drawBehind { mushafFrame(ornament) } else Modifier) // çerçeve
                    .padding(horizontal = if (s.frame) Space.m + Space.xs else Space.xs, vertical = if (s.frame) Space.m else Space.xs), // çerçeve içi boşluk
                verticalArrangement = Arrangement.spacedBy(Space.xs), // bloklar arası
            ) {
                for (b in bloklar) when (b) { // her blok
                    is PageBlock.SurahHeader -> SurahHeader(b.surah.arabicName, ornament, ink, family, fontSize, golge) // sure başlığı
                    PageBlock.Basmala -> Text("بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ", Modifier.fillMaxWidth(), style = ayetStili.copy(textAlign = TextAlign.Center)) // besmele
                    is PageBlock.Paragraph -> { // ayet paragrafı
                        val metin = remember(b, highlight, highlightAlpha, ornament) { // biçimli metin
                            buildAnnotatedString {
                                for (a in b.ayahs) { // her ayet
                                    val vurgu = highlight != null && highlight.sura == a.sura && highlight.ayah == a.number && highlightAlpha > 0f // vurgulanacak mı
                                    if (vurgu) withStyle(SpanStyle(background = ornament.copy(alpha = 0.22f * highlightAlpha))) { append(a.text) } else append(a.text) // ayet metni
                                    append(' ') // ayet ile gül arasında bölünmeyen boşluk
                                    appendInlineContent("a${a.number}", "(${a.number})") // ayet gülü
                                    append(' ') // sonraki ayetten önce boşluk
                                }
                            }
                        }
                        val gul = remember(b, ornament, ink, family, fontSize) { // ayet gülleri
                            b.ayahs.associate { a ->
                                val gen = when { a.number >= 100 -> 1.75f; a.number >= 10 -> 1.45f; else -> 1.3f } // basamağa göre genişlik
                                "a${a.number}" to InlineTextContent(Placeholder(gen.em, 1.3.em, PlaceholderVerticalAlign.TextCenter)) { AyahMarker(a.number, ornament, ink, family, fontSize) } // gül bileşeni
                            }
                        }
                        Text(metin, Modifier.fillMaxWidth(), style = ayetStili, inlineContent = gul) // paragraf
                    }
                }
            }
        }
        Row(Modifier.fillMaxWidth().padding(top = Space.s, start = Space.xs, end = Space.xs), horizontalArrangement = Arrangement.End) { // alt bilgi: sayfa no sağ altta
            Text(page.toString(), color = bilgiRengi, fontSize = 14.sp, fontWeight = FontWeight.Bold) // sayfa numarası
        }
    }
}

/** Süslü sure başlığı bandı. */
@Composable
private fun SurahHeader(name: String, ornament: Color, ink: Color, family: FontFamily, fontSize: TextUnit, shadow: Shadow?) {
    Box(
        Modifier.fillMaxWidth().padding(vertical = Space.xs).drawBehind { // band çizimi
            val r = CornerRadius(size.height / 2) // hap şekli
            drawRoundRect(ornament.copy(alpha = 0.09f), cornerRadius = r) // hafif dolgu
            drawRoundRect(ornament, cornerRadius = r, style = Stroke(1.4.dp.toPx())) // dış çizgi
            val i = 3.dp.toPx() // iç çizgi aralığı
            drawRoundRect(ornament.copy(alpha = 0.7f), topLeft = Offset(i, i), size = Size(size.width - 2 * i, size.height - 2 * i), cornerRadius = CornerRadius(size.height / 2 - i), style = Stroke(0.7.dp.toPx())) // iç çizgi
            for (x in listOf(size.height * 0.75f, size.width - size.height * 0.75f)) rotate(45f, Offset(x, size.height / 2)) { // iki yanda baklava süs
                val k = 4.dp.toPx() // süs boyutu
                drawRect(ornament, Offset(x - k, size.height / 2 - k), Size(2 * k, 2 * k)) // kare (45° dönmüş = baklava)
            }
        }.padding(horizontal = Space.xxl, vertical = Space.xs), // iç boşluk
        contentAlignment = Alignment.Center, // ortalı
    ) {
        Text(name, style = TextStyle(fontFamily = family, fontSize = fontSize * 0.95f, color = ink, textDirection = TextDirection.Rtl, shadow = shadow, lineHeight = fontSize * 1.8f)) // sure adı
    }
}
