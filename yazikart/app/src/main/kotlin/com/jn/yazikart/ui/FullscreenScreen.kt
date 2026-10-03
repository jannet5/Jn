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
import androidx.compose.foundation.gestures.detectTransformGestures // sürükleme + iki parmak algılama
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
            PostCanvas(
                style, image, // görsel ve ayarları
                Modifier.width(w).height(w / ratio) // görsel ekranı kaplıyor
                    .pointerInput(Unit) { // dokunmalar
                        detectTapGestures( // tek / çift dokunuş
                            onTap = { focus.clearFocus(); controls = !controls }, // tek dokun: düğmeleri gizle/göster
                            onDoubleTap = { onStyle { it.copy(offsetX = 0f, offsetY = 0f) } }, // çift dokun: yazıyı yerine geri koy
                        )
                    }
                    .pointerInput(Unit) { // sürükleme ve iki parmakla büyütme
                        detectTransformGestures { _, pan, zoom, _ -> // her parmak hareketinde
                            onStyle { // stil güncelleniyor
                                it.copy(
                                    offsetX = (it.offsetX + pan.x / size.width).coerceIn(-1f, 1f), // yatay kaydırma (genişliğe oranla)
                                    offsetY = (it.offsetY + pan.y / size.height).coerceIn(-1f, 1f), // dikey kaydırma (yüksekliğe oranla)
                                    textSize = (it.textSize * zoom).coerceIn(MIN_TEXT_SIZE, MAX_TEXT_SIZE), // iki parmak açılınca büyür, kapanınca küçülür
                                )
                            }
                        }
                    },
            )

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
