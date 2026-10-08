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
import androidx.compose.ui.text.TextLayoutResult // metin yerleşimi
import androidx.compose.ui.layout.onGloballyPositioned // ekrandaki konum
import androidx.compose.ui.layout.positionInRoot // köke göre konum
import androidx.compose.runtime.getValue // durum okuma
import androidx.compose.runtime.setValue // durum yazma
import androidx.compose.runtime.mutableStateOf // durum
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
            if (!koyu) drawRect(firca) // açık sayfada lifler ve tane kağıdı hafifçe koyulaştırır
            else drawRect(firca, alpha = 0.9f, colorFilter = LightFibers) // koyu sayfada aynı desen açık renkte
            drawRect( // kenarlarda hafif kararma (gerçek kağıt ışığı)
                Brush.radialGradient(
                    0.55f to Color.Transparent, 1f to Color.Black.copy(alpha = if (koyu) 0.28f else 0.10f), // merkez temiz, kenar koyu
                    center = center, radius = maxOf(size.width, size.height) * 0.72f, // tüm sayfayı kaplar
                ),
            )
        }
    }
}

private val LightFibers = ColorFilter.tint(Color(0xFFFFF4DC), BlendMode.SrcIn) // dokuyu açık krem renge boyar (saydamlık korunur)

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

/** Kabartma (mürekkep kağıda basılmış) gölgesi: doku açıkken açık sayfada beyaz, koyu sayfada siyah. */
fun embossShadow(s: ReaderSettings, density: Float): Shadow? = when {
    !s.texture -> null // doku kapalıysa kabartma yok
    s.isDarkPage() -> Shadow(Color.Black.copy(alpha = 0.75f), Offset(0f, 1.1f * density), 1.2f * density) // koyu sayfada içe basılmış
    else -> Shadow(Color.White.copy(alpha = 0.7f), Offset(0f, 0.9f * density), 0.6f * density) // açık sayfada mürekkep kağıda basılmış gibi
}

/** KFGQPC yazımıyla besmele (yazı tipiyle birebir uyumlu). */
const val BASMALA = "بِسۡمِ ٱللَّهِ ٱلرَّحۡمَٰنِ ٱلرَّحِيمِ"

/** Bir mushaf sayfası: üst bilgi, sure başlıkları, besmele, ayet paragrafları ve sayfa numarası. */
@Composable
fun QuranPage(
    quran: Quran, // Kur'an
    page: Int, // sayfa
    s: ReaderSettings, // görünüm
    highlight: AyahRef?, // vurgulanacak ayet
    highlightAlpha: Float, // vurgunun görünürlüğü (sönerek kaybolur)
    modifier: Modifier = Modifier, // değiştirici
    onHighlightAt: (Float) -> Unit = {}, // vurgulu ayetin ekrandaki dikey konumu (otomatik kaydırma için)
) {
    val ink = Color(s.inkColor) // yazı rengi
    val ornament = Color(s.ornamentColor) // süs rengi
    val family = s.font.family() // yazı tipi
    val fontSize = BASE_FONT_SIZE * s.scale // ölçekli boyut
    val golge = embossShadow(s, LocalDensity.current.density) // kabartma
    val ayetStili = TextStyle( // ayet metni stili
        fontFamily = family, fontSize = fontSize, lineHeight = fontSize * s.font.lineHeightFactor(), color = ink, // yazı tipi, boyut, renk
        textAlign = TextAlign.Start, textDirection = TextDirection.Rtl, shadow = golge, // sağdan başlar; aralar zorla açılmaz
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
                    PageBlock.Basmala -> Text(BASMALA, Modifier.fillMaxWidth(), style = ayetStili.copy(textAlign = TextAlign.Center)) // besmele
                    is PageBlock.Paragraph -> { // ayet paragrafı
                        val metin = remember(b, highlight, highlightAlpha, ornament) { // biçimli metin
                            buildAnnotatedString {
                                for (a in b.ayahs) { // her ayet
                                    val vurgu = highlight != null && highlight.sura == a.sura && highlight.ayah == a.number && highlightAlpha > 0f // vurgulanacak mı
                                    if (vurgu) withStyle(SpanStyle(background = ornament.copy(alpha = 0.30f * highlightAlpha))) { append(a.text) } else append(a.text) // ayet metni
                                    append('\u00A0') // ayet ile gülü ayırmayan boşluk
                                    append(a.number.toArabicDigits()) // ayet gülü: yazı tipi rakamı süslü daire olarak çizer
                                    append(' ') // sonraki ayetten önce boşluk
                                }
                            }
                        }
                        val vurguYeri = remember(b, highlight) { // vurgulu ayetin metindeki başlangıcı
                            if (highlight == null) -1 else {
                                var i = 0 // karakter sayacı
                                var bulundu = -1 // sonuç
                                for (a in b.ayahs) { if (a.sura == highlight.sura && a.number == highlight.ayah) { bulundu = i; break }; i += a.text.length + 1 + a.number.toArabicDigits().length + 1 } // ayet + boşluk + gül + boşluk
                                bulundu
                            }
                        }
                        var duzen by remember { mutableStateOf<TextLayoutResult?>(null) } // metin yerleşimi
                        Text(
                            metin, // paragraf
                            Modifier.fillMaxWidth().onGloballyPositioned { k -> // ekrandaki yer
                                val d = duzen // yerleşim
                                if (vurguYeri >= 0 && d != null && highlightAlpha > 0.99f) onHighlightAt(k.positionInRoot().y + d.getBoundingBox(vurguYeri.coerceAtMost(d.layoutInput.text.length - 1)).top) // vurgunun ekrandaki yeri
                            },
                            style = ayetStili, onTextLayout = { duzen = it }, // stil ve yerleşim
                        )
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
internal fun SurahHeader(name: String, ornament: Color, ink: Color, family: FontFamily, fontSize: TextUnit, shadow: Shadow?) {
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
