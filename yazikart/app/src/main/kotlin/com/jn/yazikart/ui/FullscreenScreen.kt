package com.jn.yazikart.ui // arayüz paketi

import android.app.Activity // pencereye erişmek için
import android.graphics.Bitmap // arka plan resmi
import androidx.activity.compose.BackHandler // geri tuşunu yakalamak için
import androidx.compose.foundation.background // arka plan boyama
import androidx.compose.foundation.clickable // tıklanabilirlik
import androidx.compose.foundation.interaction.MutableInteractionSource // dokunma dalgasını kapatmak için
import androidx.compose.foundation.layout.Box // üst üste yerleşim
import androidx.compose.foundation.layout.BoxWithConstraints // eldeki alanı ölçen kutu
import androidx.compose.foundation.layout.Column // alt alta yerleşim
import androidx.compose.foundation.layout.fillMaxSize // tüm alan
import androidx.compose.foundation.layout.fillMaxWidth // tam genişlik
import androidx.compose.foundation.layout.height // yükseklik
import androidx.compose.foundation.layout.imePadding // klavye boşluğu
import androidx.compose.foundation.layout.navigationBarsPadding // alt çubuk boşluğu
import androidx.compose.foundation.layout.padding // dış boşluk
import androidx.compose.foundation.layout.size // boyut
import androidx.compose.foundation.layout.statusBarsPadding // üst çubuk boşluğu
import androidx.compose.foundation.layout.width // genişlik
import androidx.compose.foundation.shape.CircleShape // daire şekli
import androidx.compose.foundation.shape.RoundedCornerShape // yuvarlak köşe
import androidx.compose.material.icons.Icons // ikon seti
import androidx.compose.material.icons.filled.Close // kapat ikonu
import androidx.compose.material3.Icon // ikon
import androidx.compose.material3.IconButton // ikon buton
import androidx.compose.material3.MaterialTheme // tema
import androidx.compose.material3.OutlinedTextField // yazı kutusu
import androidx.compose.material3.Text // yazı
import androidx.compose.runtime.Composable // Compose bileşeni işareti
import androidx.compose.runtime.DisposableEffect // kapanışta iş yapmak için
import androidx.compose.runtime.LaunchedEffect // değişince iş yapmak için
import androidx.compose.runtime.getValue // durum okuma
import androidx.compose.runtime.mutableStateOf // durum
import androidx.compose.runtime.remember // hatırlama
import androidx.compose.runtime.saveable.rememberSaveable // ekran dönse de hatırlama
import androidx.compose.runtime.setValue // durum yazma
import androidx.compose.ui.Alignment // hizalama
import androidx.compose.ui.Modifier // değiştiriciler
import androidx.compose.ui.draw.clip // kırpma
import androidx.compose.ui.graphics.Color // renk
import androidx.compose.ui.platform.LocalFocusManager // klavyeyi kapatmak için
import androidx.compose.ui.platform.LocalView // pencere görünümü
import androidx.compose.ui.unit.dp // ölçü birimi
import androidx.compose.ui.unit.min // iki ölçünün küçüğü
import androidx.core.view.WindowCompat // pencere yardımcıları
import androidx.core.view.WindowInsetsCompat // sistem çubukları türleri
import androidx.core.view.WindowInsetsControllerCompat // sistem çubuklarını gizleme
import com.jn.yazikart.data.MAX_TEXT_SIZE // en büyük yazı boyu
import com.jn.yazikart.data.MIN_TEXT_SIZE // en küçük yazı boyu
import com.jn.yazikart.data.PostStyle // görsel ayarları
import androidx.compose.foundation.gestures.detectTapGestures // dokunma algılama
import androidx.compose.foundation.gestures.awaitEachGesture // her dokunuşu baştan dinleme
import androidx.compose.foundation.gestures.awaitFirstDown // ilk parmak değişi
import androidx.compose.foundation.gestures.calculatePan // kayma hesabı
import androidx.compose.foundation.gestures.calculateZoom // iki parmak büyütme hesabı
import androidx.compose.ui.input.pointer.positionChanged // parmak kıpırdadı mı
import androidx.compose.ui.geometry.Offset // nokta
import androidx.compose.runtime.mutableFloatStateOf // ondalık durum
import androidx.compose.runtime.rememberUpdatedState // güncel değeri izleme
import androidx.compose.ui.platform.LocalContext // uygulama bağlamı
import com.jn.yazikart.data.FontCatalog // yazı tipleri
import com.jn.yazikart.render.PostRenderer // yazı kutusu hesabı
import com.jn.yazikart.render.Snap // ortaya yapışma
import androidx.compose.ui.input.pointer.pointerInput // parmak olayları

// Tam ekran: görsel Reels/hikâyede nasıl görünecekse ekranı öyle kaplar, yazı burada da yazılır.
// Görsele dokununca düğmeler ve yazı kutusu gizlenir/görünür (tamamen temiz görünüm için).
@Composable
fun FullscreenScreen(
    style: PostStyle, // güncel stil
    image: Bitmap?, // arka plan resmi
    onStyle: ((PostStyle) -> PostStyle) -> Unit, // stil değiştir
    onClose: () -> Unit, // tam ekrandan çık
) {
    BackHandler(onBack = onClose) // telefonun geri tuşu tam ekrandan çıkarır
    var controls by rememberSaveable { mutableStateOf(true) } // düğmeler görünüyor mu
    val focus = LocalFocusManager.current // klavyeyi kapatmak için
    HideSystemBars(controls) // üst saat çubuğu ve alt gezinme çubuğu gizleniyor (düğmeler değişince yeniden)
    val currentStyle by rememberUpdatedState(style) // parmak hareketi sırasında en güncel stil
    var rawX by remember { mutableFloatStateOf(0f) } // parmağın getirdiği ham yatay kayma (yapışmadan önce)
    var rawY by remember { mutableFloatStateOf(0f) } // ham dikey kayma
    var guideV by remember { mutableStateOf(false) } // dikey orta çizgi görünüyor mu
    var guideH by remember { mutableStateOf(false) } // yatay orta çizgi görünüyor mu
    val context = LocalContext.current // uygulama bağlamı
    val typeface = remember(style.fontIndex, style.bold) { FontCatalog.typeface(context.assets, style.fontIndex, style.bold) } // yazı tipi (kutu hesabı için)
    val fakeBold = FontCatalog.needsFakeBold(style.fontIndex, style.bold) // yapay kalınlık

    Column(Modifier.fillMaxSize().background(Color.Black).imePadding()) { // siyah zemin; klavye açılınca görsel yukarı sığar
        BoxWithConstraints(
            Modifier.weight(1f).fillMaxWidth() // kalan tüm alan
                .clickable( // görsele dokununca düğmeler aç/kapa
                    interactionSource = remember { MutableInteractionSource() }, // dokunma kaynağı
                    indication = null, // gri dalga efekti yok (görüntü bozulmasın)
                ) { focus.clearFocus(); controls = !controls }, // klavye kapanıp düğmeler değişiyor
            contentAlignment = Alignment.Center, // görsel ortada
        ) {
            val ratio = style.aspect.width.toFloat() / style.aspect.height // en/boy oranı
            val w = min(maxWidth, maxHeight * ratio) // ekrana sığan en büyük genişlik
            Box(Modifier.width(w).height(w / ratio)) { // görsel + kılavuz çizgileri üst üste
                PostCanvas(
                    style, image, // görsel ve ayarları
                    Modifier.fillMaxSize() // görsel ekranı kaplıyor
                        .pointerInput(Unit) { // dokunmalar
                            detectTapGestures( // tek / çift dokunuş
                                onTap = { focus.clearFocus(); controls = !controls }, // tek dokun: düğmeleri gizle/göster
                                onDoubleTap = { onStyle { it.copy(offsetX = 0f, offsetY = 0f) } }, // çift dokun: yazıyı yerine geri koy
                            )
                        }
                        .pointerInput(Unit) { // sürükleme, iki parmakla büyütme ve ortaya yapışma
                            awaitEachGesture { // her yeni dokunuşta baştan
                                awaitFirstDown(requireUnconsumed = false) // ilk parmak değdi
                                rawX = currentStyle.offsetX // parmağın taşıdığı ham kayma, şu anki yerden başlar
                                rawY = currentStyle.offsetY // dikey ham kayma
                                do { // parmaklar ekrandayken
                                    val event = awaitPointerEvent() // parmak hareketi
                                    val pan = event.calculatePan() // kayma miktarı
                                    val zoom = event.calculateZoom() // iki parmak açılma oranı
                                    if (pan != Offset.Zero || zoom != 1f) { // gerçekten hareket varsa
                                        rawX = (rawX + pan.x / size.width).coerceIn(-1f, 1f) // ham yatay kayma
                                        rawY = (rawY + pan.y / size.height).coerceIn(-1f, 1f) // ham dikey kayma
                                        val s0 = currentStyle // güncel stil
                                        val sized = s0.copy( // yeni boy ve ham konum
                                            textSize = (s0.textSize * zoom).coerceIn(MIN_TEXT_SIZE, MAX_TEXT_SIZE), // iki parmak: büyüt/küçült
                                            offsetX = rawX, offsetY = rawY, // parmağın getirdiği yer
                                        )
                                        val box = PostRenderer.textBox(size.width, size.height, sized, typeface, fakeBold, PLACEHOLDER) // yazının kutusu
                                        var snapped = sized // yapıştırılmış hal
                                        guideV = false; guideH = false // çizgiler önce kapalı
                                        if (box != null) { // yazı varsa
                                            val (sx, okX) = Snap.snap(rawX, (box.left + box.right) / 2f, size.width / 2f, size.width.toFloat()) // yatay ortaya yapış
                                            val (sy, okY) = Snap.snap(rawY, (box.top + box.bottom) / 2f, size.height / 2f, size.height.toFloat()) // dikey ortaya yapış
                                            snapped = sized.copy(offsetX = sx, offsetY = sy) // yapışmış konum
                                            guideV = okX; guideH = okY // yapıştıysa çizgi görünür
                                        }
                                        onStyle { snapped } // stil güncelleniyor
                                        event.changes.forEach { if (it.positionChanged()) it.consume() } // hareket tüketildi (tek dokunuş sayılmasın)
                                    }
                                } while (event.changes.any { it.pressed }) // parmak kalkana kadar
                                guideV = false; guideH = false // parmak kalkınca çizgiler kaybolur
                            }
                        },
                )
                GuideLines(guideV, guideH, Modifier.fillMaxSize()) // ortalama kılavuz çizgileri
            }

            if (controls) { // düğmeler görünürse
                IconButton( // sağ üstte kapat
                    onClick = onClose, // tam ekrandan çık
                    modifier = Modifier.align(Alignment.TopEnd).statusBarsPadding().padding(8.dp), // sağ üst köşe
                ) {
                    Box(
                        Modifier.size(40.dp).clip(CircleShape).background(Color.Black.copy(alpha = 0.55f)), // koyu yarı saydam daire
                        contentAlignment = Alignment.Center, // ikon ortada
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Tam ekrandan çık", tint = Color.White) // X ikonu
                    }
                }
            }
        }

        if (controls) { // yazı kutusu görünürse
            Column(
                Modifier.fillMaxWidth().background(Color.Black.copy(alpha = 0.85f)) // koyu alt şerit
                    .navigationBarsPadding().padding(horizontal = 16.dp, vertical = 8.dp), // kenar boşlukları
            ) {
                OutlinedTextField( // tam ekranda yazı yazma kutusu
                    value = style.text, // güncel yazı
                    onValueChange = { t -> onStyle { it.copy(text = t) } }, // yazı değişince görsel anında güncellenir
                    modifier = Modifier.fillMaxWidth(), // tam genişlik
                    placeholder = { Text("Yazını buraya yaz…") }, // ipucu
                    maxLines = 3, // en çok 3 satır görünür, görseli kapatmasın
                    shape = RoundedCornerShape(12.dp), // yuvarlak köşe
                )
                Text( // kullanım ipucu
                    "Sürükle: yazıyı taşı · İki parmak: büyüt/küçült · Çift dokun: yerine koy · Tek dokun: düğmeleri gizle", // açıklama
                    style = MaterialTheme.typography.bodySmall, // küçük yazı
                    color = MaterialTheme.colorScheme.onSurfaceVariant, // soluk
                    modifier = Modifier.padding(top = 4.dp), // üst boşluk
                )
            }
        }
    }
}

// Canva'daki gibi pembe orta çizgiler: yazı ortaya yapıştığında görünür (kaydedilen görselde yoktur)
@Composable
private fun GuideLines(vertical: Boolean, horizontal: Boolean, modifier: Modifier) {
    androidx.compose.foundation.Canvas(modifier) { // çizim alanı
        val stroke = 2.dp.toPx() // çizgi kalınlığı
        if (vertical) drawLine(GUIDE, Offset(size.width / 2, 0f), Offset(size.width / 2, size.height), stroke) // dikey orta çizgi
        if (horizontal) drawLine(GUIDE, Offset(0f, size.height / 2), Offset(size.width, size.height / 2), stroke) // yatay orta çizgi
    }
}

private val GUIDE = Color(0xFFFF3DA6) // kılavuz çizgisi rengi (Canva pembesi)

// Tam ekrandayken saat/pil çubuğunu ve alt gezinme çubuğunu gizler; çıkınca geri getirir
@Composable
private fun HideSystemBars(key: Any) {
    val view = LocalView.current // ekran görünümü
    val ctrl = remember { // sistem çubukları denetleyicisi (bir kez alınıyor)
        WindowCompat.getInsetsController((view.context as Activity).window, view).apply { // uygulamanın penceresi
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE // kenardan kaydırınca geçici görünür
        }
    }
    LaunchedEffect(key) { ctrl.hide(WindowInsetsCompat.Type.systemBars()) } // açılınca ve her dokunuşta gizle (klavye geri getirmiş olabilir)
    DisposableEffect(Unit) { onDispose { ctrl.show(WindowInsetsCompat.Type.systemBars()) } } // tam ekrandan çıkınca geri getir
}
