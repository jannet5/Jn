package com.jn.oynatici.ui

import android.app.Activity
import android.content.pm.ActivityInfo
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import com.jn.oynatici.data.Zaman

// Alttaki küçük müzik oynatıcı (dokununca büyük ekran açılır)
@Composable
fun MiniOynatici(model: UygulamaModeli, durum: CalmaDurumu) {
    Surface(color = MaterialTheme.colorScheme.surfaceContainer, modifier = Modifier.fillMaxWidth()) { // alt şerit
        Row(
            Modifier.dokun { model.git(Sayfa.Calan) }.navigationBarsPadding().padding(horizontal = 16.dp, vertical = 8.dp), // dokununca büyük ekran
            verticalAlignment = Alignment.CenterVertically, // dikeyde ortala
        ) {
            Icon(Icons.Rounded.MusicNote, null, tint = MaterialTheme.colorScheme.primary) // nota ikonu
            Text(durum.baslik, Modifier.weight(1f).padding(horizontal = 12.dp), maxLines = 1, overflow = TextOverflow.Ellipsis) // şarkı adı
            IconButton(onClick = { model.oynatici?.let { if (durum.caliyor) it.pause() else it.play() } }) { // çal/duraklat
                Icon(if (durum.caliyor) Icons.Rounded.Pause else Icons.Rounded.PlayArrow, "Çal/Duraklat")
            }
            IconButton(onClick = { model.oynatici?.seekToNextMediaItem() }) { Icon(Icons.Rounded.SkipNext, "Sonraki") } // sonraki şarkı
        }
    }
}

// Büyük müzik çalma ekranı
@Composable
fun CalanEkrani(model: UygulamaModeli) {
    val durum by calmaDurumu(model.oynatici) // oynatıcı durumu
    var surukleniyor by remember { mutableStateOf<Float?>(null) } // çubuk sürüklenirken geçici değer
    val p = model.oynatici // kısa ad
    Column(Modifier.fillMaxSize().safeDrawingPadding().padding(horizontal = 24.dp)) { // sayfa
        IconButton(onClick = { model.geri() }, Modifier.padding(top = 4.dp)) { Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Geri") } // geri
        Spacer(Modifier.weight(1f)) // üst boşluk
        Surface( // büyük kapak alanı
            shape = RoundedCornerShape(32.dp), color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.fillMaxWidth().aspectRatio(1f),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Rounded.MusicNote, null, Modifier.size(120.dp), tint = MaterialTheme.colorScheme.onPrimaryContainer) // nota
            }
        }
        Spacer(Modifier.height(32.dp)) // boşluk
        Text( // şarkı adı
            durum.baslik.ifEmpty { "Çalan bir şey yok" }, fontSize = 22.sp, fontWeight = FontWeight.SemiBold,
            maxLines = 2, overflow = TextOverflow.Ellipsis, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(24.dp)) // boşluk
        Slider( // ilerleme çubuğu (sürükleyerek ileri/geri sar)
            value = surukleniyor ?: durum.konum.toFloat().coerceIn(0f, durum.sure.toFloat().coerceAtLeast(1f)),
            onValueChange = { surukleniyor = it }, // sürüklerken
            onValueChangeFinished = { surukleniyor?.let { p?.seekTo(it.toLong()) }; surukleniyor = null }, // bırakınca oraya git
            valueRange = 0f..durum.sure.toFloat().coerceAtLeast(1f), // 0 - süre
        )
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { // süre yazıları
            Text(Zaman.yaz(surukleniyor?.toLong() ?: durum.konum), color = MaterialTheme.colorScheme.onSurfaceVariant) // geçen süre
            Text(Zaman.yaz(durum.sure), color = MaterialTheme.colorScheme.onSurfaceVariant) // toplam süre
        }
        Spacer(Modifier.height(16.dp)) // boşluk
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) { // kontroller
            IconButton(onClick = { p?.seekToPrevious() }, Modifier.size(64.dp)) { Icon(Icons.Rounded.SkipPrevious, "Önceki", Modifier.size(40.dp)) } // önceki
            FilledIconButton( // büyük çal/duraklat
                onClick = { p?.let { if (durum.caliyor) it.pause() else it.play() } }, Modifier.size(84.dp),
                colors = IconButtonDefaults.filledIconButtonColors(),
            ) { Icon(if (durum.caliyor) Icons.Rounded.Pause else Icons.Rounded.PlayArrow, "Çal/Duraklat", Modifier.size(48.dp)) }
            IconButton(onClick = { p?.seekToNextMediaItem() }, Modifier.size(64.dp)) { Icon(Icons.Rounded.SkipNext, "Sonraki", Modifier.size(40.dp)) } // sonraki
        }
        Spacer(Modifier.weight(1f)) // alt boşluk
    }
}

// Tam ekran video oynatma
@OptIn(UnstableApi::class)
@Composable
fun VideoEkrani(model: UygulamaModeli) {
    val aktivite = LocalContext.current as Activity // ekran yönünü değiştirmek için
    var tamEkran by remember { mutableStateOf(false) } // yatay tam ekran mı

    // Tam ekranda sistem çubuklarını gizle, yatay çevir
    DisposableEffect(tamEkran) {
        val pencere = WindowCompat.getInsetsController(aktivite.window, aktivite.window.decorView) // sistem çubukları kontrolü
        if (tamEkran) {
            aktivite.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE // yatay
            pencere.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE // kaydırınca görünsün
            pencere.hide(WindowInsetsCompat.Type.systemBars()) // çubukları gizle
        } else {
            aktivite.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED // serbest yön
            pencere.show(WindowInsetsCompat.Type.systemBars()) // çubukları göster
        }
        onDispose { }
    }
    // Sayfadan çıkınca: videoyu durdur, ekranı eski haline getir
    DisposableEffect(Unit) {
        onDispose {
            aktivite.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED // yönü serbest bırak
            WindowCompat.getInsetsController(aktivite.window, aktivite.window.decorView).show(WindowInsetsCompat.Type.systemBars()) // çubukları göster
            model.oynatici?.run { stop(); clearMediaItems() } // videoyu kapat
        }
    }

    Box(Modifier.fillMaxSize().background(Color.Black)) { // siyah arka plan
        AndroidView( // Media3'ün hazır video ekranı (oynat/duraklat, ileri sar, sonraki video)
            factory = { c ->
                PlayerView(c).apply {
                    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT) // tam boyut
                    keepScreenOn = true // izlerken ekran kapanmasın
                    setShowNextButton(true) // sonraki video butonu
                    setShowPreviousButton(true) // önceki video butonu
                    setFullscreenButtonClickListener { tamEkran = it } // tam ekran butonu
                }
            },
            update = { it.player = model.oynatici }, // oynatıcıyı bağla
            modifier = Modifier.fillMaxSize(), // tam ekran
        )
        if (!tamEkran) IconButton(onClick = { model.geri() }, Modifier.statusBarsPadding().padding(4.dp)) { // geri butonu
            Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Geri", tint = Color.White)
        }
    }
}
