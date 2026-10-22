package com.jn.yazikart.ui // arayüz paketi

import android.graphics.Bitmap // küçük resimler ve kareler
import androidx.compose.foundation.Image // kare gösterimi
import androidx.compose.foundation.background // arka plan boyama
import androidx.compose.foundation.clickable // dokunma
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress // uzun basıp sürükleme
import androidx.compose.foundation.gestures.detectHorizontalDragGestures // şerit tutamacı sürükleme
import androidx.compose.foundation.horizontalScroll // yatay kaydırma
import androidx.compose.foundation.layout.Arrangement // dizilim aralığı
import androidx.compose.foundation.layout.Box // üst üste yerleşim
import androidx.compose.foundation.layout.BoxWithConstraints // eldeki alanı ölçen kutu
import androidx.compose.foundation.layout.Column // alt alta
import androidx.compose.foundation.layout.Row // yan yana
import androidx.compose.foundation.layout.Spacer // boşluk
import androidx.compose.foundation.layout.fillMaxHeight // tam yükseklik
import androidx.compose.foundation.layout.fillMaxSize // tüm alan
import androidx.compose.foundation.layout.fillMaxWidth // tam genişlik
import androidx.compose.foundation.layout.height // yükseklik
import androidx.compose.foundation.layout.padding // iç boşluk
import androidx.compose.foundation.layout.size // boyut
import androidx.compose.foundation.layout.width // genişlik
import androidx.compose.foundation.layout.navigationBarsPadding // alt çubuk boşluğu
import androidx.compose.foundation.rememberScrollState // kaydırma durumu
import androidx.compose.foundation.shape.CircleShape // daire
import androidx.compose.foundation.shape.RoundedCornerShape // yuvarlak köşe
import androidx.compose.material.icons.Icons // ikon seti
import androidx.compose.material.icons.automirrored.filled.ArrowBack // sola ok
import androidx.compose.material.icons.automirrored.filled.ArrowForward // sağa ok
import androidx.compose.material.icons.filled.AddPhotoAlternate // galeri ekle
import androidx.compose.material.icons.filled.Delete // sil
import androidx.compose.material.icons.filled.Movie // video
import androidx.compose.material.icons.filled.Image as ImageIcon // fotoğraf ikonu (composable Image ile çakışmasın)
import androidx.compose.material.icons.filled.MusicNote // müzik
import androidx.compose.material3.Button // birincil buton
import androidx.compose.material3.ExperimentalMaterial3Api // alt pencere deneysel
import androidx.compose.material3.Icon // ikon
import androidx.compose.material3.MaterialTheme // tema
import androidx.compose.material3.ModalBottomSheet // alttan pencere
import androidx.compose.material3.OutlinedButton // ikincil buton
import androidx.compose.material3.Slider // kaydırıcı
import androidx.compose.material3.Switch // aç/kapa
import androidx.compose.material3.Text // yazı
import androidx.compose.material3.TextButton // yazı buton
import androidx.compose.material3.rememberModalBottomSheetState // alt pencere durumu
import androidx.compose.runtime.Composable // Compose bileşeni
import androidx.compose.runtime.LaunchedEffect // değişince iş
import androidx.compose.runtime.getValue // durum okuma
import androidx.compose.runtime.mutableFloatStateOf // ondalık durum
import androidx.compose.runtime.mutableStateOf // durum
import androidx.compose.runtime.remember // hatırlama
import androidx.compose.runtime.rememberCoroutineScope // iş kapsamı
import androidx.compose.runtime.rememberUpdatedState // güncel değer
import androidx.compose.runtime.setValue // durum yazma
import androidx.compose.ui.Alignment // hizalama
import androidx.compose.ui.Modifier // değiştiriciler
import androidx.compose.ui.draw.clip // kırpma
import androidx.compose.ui.geometry.Offset // nokta
import androidx.compose.ui.graphics.Color // renk
import androidx.compose.ui.graphics.asImageBitmap // Bitmap → ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput // parmak olayları
import androidx.compose.ui.layout.ContentScale // resim ölçekleme
import androidx.compose.foundation.layout.offset // piksel kaydırma (lambda ile)
import androidx.compose.foundation.border // çerçeve
import androidx.compose.ui.platform.LocalDensity // piksel-dp çevirisi
import androidx.compose.ui.unit.IntOffset // tam sayı kaydırma
import androidx.compose.ui.unit.dp // ölçü birimi
import androidx.compose.ui.zIndex // sürüklenen öğe üstte
import com.jn.yazikart.data.Clips // süre hesapları
import com.jn.yazikart.data.VideoClip // parça
import kotlinx.coroutines.delay // bekleme
import kotlin.math.roundToInt // yuvarlama

// Sıra ve müzik sekmesi: parçalar yan yana (sürükleyip sırala), dokununca düzenle, altta müzik
@Composable
fun SequencePanel(video: VideoUi, online: Boolean) {
    var openIndex by remember { mutableStateOf<Int?>(null) } // düzenlenen parça
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) { // alt alta
        Label("Fotoğraf ve videolar (arka arkaya)") // başlık
        Row(Modifier.padding(horizontal = SIDE), horizontalArrangement = Arrangement.spacedBy(12.dp)) { // yan yana iki buton
            OutlinedButton(onClick = video.onAddMedia, modifier = Modifier.weight(1f).height(48.dp)) { // galeriden çoklu seçim
                Icon(Icons.Default.AddPhotoAlternate, contentDescription = null) // ikon
                Spacer(Modifier.width(8.dp)) // boşluk
                Text("Galeriden ekle") // etiket
            }
            Button(onClick = video.onSearchVideo, enabled = online, modifier = Modifier.weight(1f).height(48.dp)) { // internetten video
                Icon(Icons.Default.Movie, contentDescription = null) // ikon
                Spacer(Modifier.width(8.dp)) // boşluk
                Text(if (online) "Video ara" else "İnternet yok") // etiket
            }
        }
        if (video.hasVideo) { // parça varsa
            Text(
                "${video.clips.size} parça · toplam ${Clips.format(Clips.totalMs(video.clips))} · Sırayı değiştirmek için basılı tutup kaydır, düzenlemek için dokun", // açıklama
                style = MaterialTheme.typography.bodySmall, // küçük yazı
                color = MaterialTheme.colorScheme.onSurfaceVariant, // soluk
                modifier = Modifier.padding(horizontal = SIDE), // kenar boşluğu
            )
            SequenceStrip(video.clips, video.thumbs, video.onMove) { openIndex = it } // parçalar
            Row(Modifier.padding(horizontal = SIDE), verticalAlignment = Alignment.CenterVertically) { // video sesi
                Text("Videoların kendi sesi", Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge) // etiket
                Switch(checked = video.videoSound, onCheckedChange = video.onVideoSound) // aç/kapa
            }
        } else { // parça yoksa
            Text(
                "Henüz parça yok. Galeriden fotoğraf ya da video ekle, ya da internetten video ara.", // ipucu
                style = MaterialTheme.typography.bodyMedium, // gövde yazısı
                color = MaterialTheme.colorScheme.onSurfaceVariant, // soluk
                modifier = Modifier.padding(horizontal = SIDE), // kenar boşluğu
            )
        }

        Spacer(Modifier.height(8.dp)) // bölümler arası boşluk
        Label("Müzik") // başlık
        MusicSection(video) // müzik ayarları
    }

    openIndex?.let { i -> // düzenleme penceresi açıksa
        val clip = video.clips.getOrNull(i) // o parça (silinmiş olabilir)
        if (clip != null) ClipSheet(i, clip, video) { openIndex = null } else openIndex = null // kapat
    }
}

// Parçalar yan yana. Basılı tutup sürükleyince yer değiştirir, dokununca düzenleme penceresi açılır.
@Composable
private fun SequenceStrip(clips: List<VideoClip>, thumbs: Map<String, Bitmap>, onMove: (Int, Int) -> Unit, onOpen: (Int) -> Unit) {
    val density = LocalDensity.current // piksel/dp
    val itemDp = 84.dp // bir parçanın genişliği
    val itemPx = with(density) { (itemDp + 8.dp).toPx() } // öğe + aralık (piksel)
    Row(
        Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = SIDE), // yatay kaydırılabilir
        horizontalArrangement = Arrangement.spacedBy(8.dp), // öğeler arası
    ) {
        clips.forEachIndexed { i, clip -> // her parça
            ClipTile(
                index = i,
                count = clips.size,
                clip = clip,
                thumb = thumbs[clip.uri],
                itemDp = itemDp,
                itemPx = itemPx,
                onMove = onMove,
                onOpen = onOpen,
            )
        }
    }
}

// Tek parça kutusu: dokunma = düzenle, uzun basıp kaydırma = sırayı değiştir
@Composable
private fun ClipTile(
    index: Int,
    count: Int,
    clip: VideoClip,
    thumb: Bitmap?,
    itemDp: androidx.compose.ui.unit.Dp,
    itemPx: Float,
    onMove: (Int, Int) -> Unit,
    onOpen: (Int) -> Unit,
) {
    val curIndex by rememberUpdatedState(index) // sürüklerken güncel sıra
    val curCount by rememberUpdatedState(count) // sürüklerken güncel sayı
    val onMoveNow by rememberUpdatedState(onMove) // güncel taşıma işlevi
    val onOpenNow by rememberUpdatedState(onOpen) // güncel açma işlevi
    var dragX by remember { mutableFloatStateOf(0f) } // sürükleme miktarı (piksel)
    var dragging by remember { mutableStateOf(false) } // sürükleniyor mu
    Box(
        Modifier
            .size(itemDp, 100.dp) // kutu
            .zIndex(if (dragging) 1f else 0f) // sürüklenen öğe üstte görünsün
            .offset { IntOffset(dragX.roundToInt(), 0) } // parmağı izler
            .clip(RoundedCornerShape(10.dp)) // yuvarlak köşe
            .background(Color(0xFF1C1C1E)) // zemin
            .clickable { onOpenNow(curIndex) } // dokunma: düzenle
            .pointerInput(clip.uri) { // uzun basıp sürükleme: sırayı değiştir
                detectDragGesturesAfterLongPress(
                    onDragStart = { dragging = true; dragX = 0f }, // başladı
                    onDragEnd = { // bırakıldı: yeni sıra hesaplanır
                        val shift = (dragX / itemPx).roundToInt() // kaç kutu kaydı
                        val to = (curIndex + shift).coerceIn(0, curCount - 1) // yeni yer
                        dragging = false; dragX = 0f // sıfırla
                        if (to != curIndex) onMoveNow(curIndex, to) // sırayı değiştir
                    },
                    onDragCancel = { dragging = false; dragX = 0f }, // iptal
                    onDrag = { change, amount -> change.consume(); dragX += amount.x }, // parmakla birlikte kaydır
                )
            },
    ) {
        if (thumb != null) { // küçük resim varsa
            Image(bitmap = thumb.asImageBitmap(), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()) // kare
        } else { // yoksa ikon
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { // ortalı
                Icon(if (clip.isPhoto) Icons.Default.ImageIcon else Icons.Default.Movie, contentDescription = null, tint = Color.White.copy(alpha = 0.6f)) // tür ikonu
            }
        }
        Box( // altta: sıra ve süre
            Modifier.align(androidx.compose.ui.Alignment.BottomStart).fillMaxWidth().background(Color.Black.copy(alpha = 0.55f)).padding(horizontal = 6.dp, vertical = 2.dp), // şerit
        ) {
            Text("${index + 1} · ${Clips.format(clip.lengthMs)}", color = Color.White, style = MaterialTheme.typography.labelSmall) // yazı
        }
    }
}

// Düzenleme penceresi: video ise kare şeridiyle kırpma, fotoğraf ise süre; altta taşıma ve silme
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ClipSheet(index: Int, clip: VideoClip, video: VideoUi, onDismiss: () -> Unit) {
    val state = rememberModalBottomSheetState(skipPartiallyExpanded = true) // tam yükseklikte açılsın
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = state) { // alt pencere
        Column(Modifier.navigationBarsPadding().padding(horizontal = SIDE).padding(bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) { // içerik
            Text(if (clip.isPhoto) "${index + 1}. fotoğraf" else "${index + 1}. video", style = MaterialTheme.typography.titleMedium) // başlık
            if (clip.isPhoto) { // fotoğraf: ekranda kalma süresi
                Text("Ekranda kalma süresi: ${Clips.seconds(clip.lengthMs)}", style = MaterialTheme.typography.bodyMedium) // süre
                Slider( // süre kaydırıcısı
                    value = clip.endMs.toFloat(), // güncel süre
                    onValueChange = { video.onRange(index, 0L, it.toLong()) }, // canlı
                    onValueChangeFinished = { video.onRangeDone(index) }, // bırakınca önizleme güncellenir
                    valueRange = Clips.MIN_LENGTH_MS.toFloat()..Clips.PHOTO_MAX_MS.toFloat(), // 0,5 - 10 sn
                )
            } else { // video: kare şeridiyle kırpma
                Text(
                    "Başlangıç ${Clips.format(clip.startMs)} · Bitiş ${Clips.format(clip.endMs)} · Oynayacak ${Clips.seconds(clip.lengthMs)}", // aralık
                    style = MaterialTheme.typography.bodyMedium, // gövde yazısı
                )
                FilmStrip( // kare şeridi: tutamaçları sürükle
                    clip = clip,
                    strip = { c, n -> video.strip(c, n) },
                    frameAt = { c, ms -> video.frameAt(c, ms) },
                    onRange = { s, e -> video.onRange(index, s, e) },
                    onDone = { video.onRangeDone(index) },
                )
                Text("Soldaki ve sağdaki tutamacı sürükleyerek videonun hangi kısmını oynatacağını seç.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) // ipucu
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { // taşıma ve silme
                if (index > 0) OutlinedButton(onClick = { video.onMove(index, index - 1); onDismiss() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Öne al") } // öne al
                if (index < video.clips.lastIndex) OutlinedButton(onClick = { video.onMove(index, index + 1); onDismiss() }) { Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Arkaya al") } // arkaya al
                Spacer(Modifier.weight(1f)) // boşluk
                OutlinedButton(onClick = { video.onRemove(index); onDismiss() }) { Icon(Icons.Default.Delete, contentDescription = "Sil") } // sil
            }
            Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth().height(48.dp)) { Text("Tamam") } // kapat
        }
    }
}

// Videonun kareleri ve iki tutamaç: sol tutamaç başlangıç, sağ tutamaç bitiş. Sürüklerken üstte o karenin önizlemesi çıkar.
@Composable
private fun FilmStrip(
    clip: VideoClip,
    strip: suspend (VideoClip, Int) -> List<Bitmap?>,
    frameAt: suspend (VideoClip, Long) -> Bitmap?,
    onRange: (Long, Long) -> Unit,
    onDone: () -> Unit,
) {
    var frames by remember(clip.uri) { mutableStateOf<List<Bitmap?>>(emptyList()) } // şeritteki kareler
    LaunchedEffect(clip.uri) { frames = strip(clip, 12) } // 12 kare yüklenir
    val latest by rememberUpdatedState(clip) // güncel parça
    val onRangeNow by rememberUpdatedState(onRange) // güncel aralık işlevi
    val onDoneNow by rememberUpdatedState(onDone) // güncel bitiş işlevi
    var active by remember { mutableStateOf<Boolean?>(null) } // true: başlangıç tutamacı, false: bitiş, null: yok
    var preview by remember(clip.uri) { mutableStateOf<Bitmap?>(null) } // seçili karenin büyük önizlemesi
    LaunchedEffect(active, latest.startMs, latest.endMs) { // tutamaç hareket edince önizleme (sık istenmesin diye kısa gecikme)
        val t = if (active == false) latest.endMs else latest.startMs // hangi tutamaç
        delay(120) // hızlı kaydırmada sadece son hali çekilir
        preview = frameAt(latest, (t - 1).coerceIn(0L, latest.durationMs - 1)) // o kare
    }
    Box(Modifier.fillMaxWidth().height(180.dp).clip(RoundedCornerShape(10.dp)).background(Color.Black), contentAlignment = Alignment.Center) { // önizleme
        preview?.let { Image(bitmap = it.asImageBitmap(), contentDescription = null, contentScale = ContentScale.Fit, modifier = Modifier.fillMaxSize()) } // kare
    }
    BoxWithConstraints(Modifier.fillMaxWidth().height(60.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFF1C1C1E))) { // şerit
        val widthPx = constraints.maxWidth.toFloat() // şeridin genişliği (piksel)
        val d = latest.durationMs.coerceAtLeast(1L).toFloat() // toplam süre
        Row(Modifier.fillMaxSize()) { // kareler yan yana
            frames.forEach { f -> Box(Modifier.weight(1f).fillMaxHeight()) { f?.let { Image(bitmap = it.asImageBitmap(), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()) } } }
        }
        val sx = latest.startMs / d * widthPx // başlangıç (piksel)
        val ex = latest.endMs / d * widthPx // bitiş (piksel)
        val density = LocalDensity.current // piksel-dp
        Box(Modifier.width(with(density) { sx.toDp() }).fillMaxHeight().background(Color.Black.copy(alpha = 0.65f))) // seçim dışı (sol)
        Box(Modifier.offset { IntOffset(ex.roundToInt(), 0) }.width(with(density) { (widthPx - ex).coerceAtLeast(0f).toDp() }).fillMaxHeight().background(Color.Black.copy(alpha = 0.65f))) // seçim dışı (sağ)
        Box(Modifier.offset { IntOffset(sx.roundToInt(), 0) }.width(with(density) { (ex - sx).coerceAtLeast(0f).toDp() }).fillMaxHeight().border(2.dp, Color.White)) // seçim çerçevesi
        Handle(
            x = sx, widthPx = widthPx, d = d, isStart = true,
            getBase = { latest.startMs }, onRange = { ms -> onRangeNow(ms, latest.endMs) },
            onStart = { active = true }, onEnd = { active = null; onDoneNow() },
        )
        Handle(
            x = ex, widthPx = widthPx, d = d, isStart = false,
            getBase = { latest.endMs }, onRange = { ms -> onRangeNow(latest.startMs, ms) },
            onStart = { active = false }, onEnd = { active = null; onDoneNow() },
        )
    }
}

// Tek bir tutamaç: yatay sürüklenince zamanı hesaplar
@Composable
private fun Handle(
    x: Float,
    widthPx: Float,
    d: Float,
    isStart: Boolean,
    getBase: () -> Long,
    onRange: (Long) -> Unit,
    onStart: () -> Unit,
    onEnd: () -> Unit,
) {
    val density = LocalDensity.current // piksel-dp
    val onRangeNow by rememberUpdatedState(onRange) // güncel işlev
    val onStartNow by rememberUpdatedState(onStart) // güncel
    val onEndNow by rememberUpdatedState(onEnd) // güncel
    val baseNow by rememberUpdatedState(getBase) // güncel başlangıç değeri
    val handleDp = 14.dp // tutamaç genişliği
    val handlePx = with(density) { handleDp.toPx() } // piksel
    var base by remember { mutableFloatStateOf(0f) } // sürükleme başlangıcındaki zaman (ms)
    var acc by remember { mutableFloatStateOf(0f) } // parmağın kat ettiği mesafe (piksel)
    Box(
        Modifier
            .offset { IntOffset((x - handlePx / 2).roundToInt(), 0) } // tutamaç çerçevenin kenarında
            .width(handleDp)
            .fillMaxHeight()
            .background(Color.White)
            .pointerInput(isStart) { // sürükleme
                detectHorizontalDragGestures(
                    onDragStart = { base = baseNow().toFloat(); acc = 0f; onStartNow() }, // başladı
                    onDragEnd = { onEndNow() }, // bırakıldı
                    onDragCancel = { onEndNow() }, // iptal
                    onHorizontalDrag = { change, dragAmount -> // parmak hareketi
                        change.consume() // kaydırma tüketildi
                        acc += dragAmount // toplam mesafe
                        val ms = (base + acc / widthPx * d).coerceIn(0f, d).toLong() // yeni zaman
                        onRangeNow(ms) // aralık güncellenir
                    },
                )
            },
    )
}

// Müzik: seç, başlangıç saniyesi, ses seviyesi, kaldır
@Composable
private fun MusicSection(video: VideoUi) {
    val m = video.music // müzik
    if (m == null) { // müzik yoksa
        OutlinedButton(onClick = video.onPickMusic, modifier = Modifier.padding(horizontal = SIDE).fillMaxWidth().height(48.dp)) { // seç
            Icon(Icons.Default.MusicNote, contentDescription = null) // ikon
            Spacer(Modifier.width(8.dp)) // boşluk
            Text("Müzik ekle (telefondan)") // etiket
        }
        return
    }
    Column(Modifier.padding(horizontal = SIDE), verticalArrangement = Arrangement.spacedBy(6.dp)) { // alt alta
        Text("${m.name} · ${Clips.format(m.durationMs)}", style = MaterialTheme.typography.bodyLarge) // ad ve süre
        Text("Şarkının ${Clips.format(m.startMs)}'inden başlar", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) // başlangıç
        Slider( // başlangıç kaydırıcısı
            value = m.startMs.toFloat(), // güncel
            onValueChange = { video.onMusicStart(it.toLong()) }, // canlı
            onValueChangeFinished = video.onMusicStartDone, // bırakınca müzik oradan çalar
            valueRange = 0f..kotlin.math.max(1f, (m.durationMs - 1000).toFloat()), // en az 1 sn kalsın
        )
        Text("Müzik sesi", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) // etiket
        Slider(value = m.volume, onValueChange = video.onMusicVolume, valueRange = 0f..1f) // ses seviyesi
        TextButton(onClick = video.onMusicRemove) { Icon(Icons.Default.Delete, contentDescription = null); Spacer(Modifier.width(6.dp)); Text("Müziği kaldır") } // kaldır
    }
}
