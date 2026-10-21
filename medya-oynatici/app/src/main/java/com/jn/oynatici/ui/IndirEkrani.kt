package com.jn.oynatici.ui

import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ContentPaste
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jn.oynatici.data.LinkCozucu
import com.jn.oynatici.data.LinkTuru
import com.jn.oynatici.data.Platform
import com.jn.oynatici.data.Sekme
import com.jn.oynatici.data.Tur
import com.jn.oynatici.data.Zaman
import com.jn.oynatici.indir.Cerezler
import com.jn.oynatici.indir.IndirmeIsi
import com.jn.oynatici.indir.IndirmeMerkezi
import com.jn.oynatici.indir.ListeSonucu
import com.jn.oynatici.indir.Listeleyici
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File

// Video kalite seçenekleri (null = en yüksek)
private val kaliteler = listOf<Pair<String, Int?>>("144p" to 144, "240p" to 240, "360p" to 360, "480p" to 480, "720p" to 720, "1080p" to 1080, "En yüksek" to null)
// Profilden kaç içerik indirileceği için hazır seçenekler
private val hazirSayilar = listOf(5, 10, 20, 50)
// Giriş yapılmadan profil listesi alınamayan siteler
private val profilIcinGirisGerekli = setOf(Platform.X)

// İnternetten indirme sayfası: link yapıştır → ne olduğu anlaşılır → (liste ise seç, profil ise sayı seç) → indir
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun IndirEkrani(model: UygulamaModeli, tur: Tur, klasor: File, hazirLink: String) {
    val ctx = LocalContext.current // bağlam
    val kapsam = rememberCoroutineScope() // liste getirme işi için
    var link by rememberSaveable { mutableStateOf(hazirLink) } // yazılan link (giriş sayfasına gidip dönünce de kalır)
    var kalite by rememberSaveable { mutableStateOf<Int?>(720) } // video kalitesi (varsayılan 720p)
    var sayiYazi by rememberSaveable { mutableStateOf("10") } // profilden kaç içerik (yazı olarak)
    var sekme by rememberSaveable { mutableStateOf<Sekme?>(null) } // profil bölümü
    var listeIsi by remember { mutableStateOf<Job?>(null) } // süren liste getirme işi
    val listeIslemId = remember { java.util.UUID.randomUUID().toString() } // liste getiren yt-dlp/gallery-dl işleminin kimliği (durdurmak için)
    var liste by remember { mutableStateOf<ListeSonucu?>(null) } // getirilen oynatma listesi
    val secili = remember { mutableStateListOf<Int>() } // listede seçili öğelerin sırası
    var listeGetiriliyor by remember { mutableStateOf(false) } // liste yükleniyor mu
    var listeHatasi by remember { mutableStateOf<String?>(null) } // liste alınamadıysa sebebi

    // Panoda desteklenen bir link varsa kendiliğinden yapıştır (kullanıcı uğraşmasın)
    LaunchedEffect(Unit) { if (link.isBlank()) panoLinki(ctx)?.let { link = it } }

    // Kısa / paylaşım linklerini (pin.it, instagram.com/share/) asıl adrese çevir: pano mu pin mi, reel mi anlaşılsın
    LaunchedEffect(link) {
        val l = link.trim() // link
        val kisa = l.startsWith("http") && (Regex("""^https?://pin\.it/""").containsMatchIn(l) || Regex("""instagram\.com/share/""").containsMatchIn(l)) // çözülmesi gereken mi
        if (kisa) runCatching { com.jn.oynatici.indir.Motor.yonlendirmeyiCoz(l) }.getOrNull()?.takeIf { it != l && it.startsWith("http") }?.let { link = it } // asıl adres
    }
    val gecerli = link.trim().startsWith("http") // link gibi görünüyor mu
    val cozulmus = remember(link) { if (gecerli) LinkCozucu.coz(link) else null } // link ne
    var oncekiLink by rememberSaveable { mutableStateOf<String?>(null) } // en son hangi link için ayar yapıldı
    LaunchedEffect(cozulmus?.url, cozulmus?.turu) { // link değişince önceki seçimleri sıfırla
        listeyiDurdur(listeIsi, listeIslemId); listeIsi = null; listeGetiriliyor = false // süren liste getirmeyi durdur
        liste = null; secili.clear(); listeHatasi = null // liste temizle
        if (oncekiLink != cozulmus?.url) { // gerçekten başka bir link (geri dönüşte seçili bölüm korunur)
            sekme = cozulmus?.sekmeler?.firstOrNull() // ilk bölüm seçili gelsin
            oncekiLink = cozulmus?.url // hatırla
        }
    }
    DisposableEffect(Unit) { onDispose { listeyiDurdur(listeIsi, listeIslemId) } } // sayfadan çıkınca arka planda liste getirme kalmasın
    val sayi = sayiYazi.toIntOrNull()?.takeIf { it in 1..500 } // geçerli sayı

    // İndirmeyi başlat ve listeye dön
    fun baslat(isi: IndirmeIsi) {
        IndirmeMerkezi.ekle(ctx, isi) // kuyruğa ekle
        Toast.makeText(ctx, "İndirme başladı", Toast.LENGTH_SHORT).show() // bilgi ver
        model.geri() // klasöre dön (ilerleme orada görünür)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (tur == Tur.MUZIK) "İnternetten MP3 indir" else "İnternetten video indir") }, // başlık
                navigationIcon = { IconButton(onClick = { model.geri() }) { Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Geri") } }, // geri
            )
        },
        bottomBar = { // ana buton ekranın altında sabit (uzun listede de görünür)
            val c = cozulmus // çözülmüş link
            if (c != null) Surface(color = MaterialTheme.colorScheme.surfaceContainer) {
                Column(Modifier.fillMaxWidth().navigationBarsPadding().padding(16.dp)) {
                    val v = if (tur == Tur.VIDEO) kalite else null // müzikte kalite yok
                    val (yazi, etkin, tikla) = when (c.turu) {
                        LinkTuru.TEK -> Triple("İndir", true) {
                            baslat(IndirmeIsi(url = c.url, tur = tur, hedef = klasor, kalite = v, platform = c.platform))
                        }
                        LinkTuru.PROFIL -> Triple("Son ${sayi ?: "?"} içeriği indir", sayi != null) {
                            baslat(IndirmeIsi(url = c.url, tur = tur, hedef = klasor, kalite = v, platform = c.platform,
                                linkTuru = LinkTuru.PROFIL, sayi = sayi ?: 10, sekme = sekme))
                        }
                        LinkTuru.LISTE -> {
                            val l = liste // liste
                            Triple("Seçilenleri indir (${secili.size})", l != null && secili.isNotEmpty()) {
                                if (l != null) baslat(IndirmeIsi(url = c.url, tur = tur, hedef = klasor, kalite = v, platform = c.platform,
                                    linkTuru = LinkTuru.LISTE, listeAdi = l.ad, ogeler = secili.sorted().map { l.ogeler[it] }))
                            }
                        }
                    }
                    Button(onClick = tikla, enabled = etkin, modifier = Modifier.fillMaxWidth().height(52.dp)) { Text(yazi, fontWeight = FontWeight.SemiBold) }
                }
            }
        },
    ) { bosluk ->
        LazyColumn(
            Modifier.fillMaxSize().padding(bosluk).imePadding(), // klavye açılınca içerik yukarı kayar
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item { // desteklenen siteler
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    listOf(Platform.YOUTUBE, Platform.TIKTOK, Platform.INSTAGRAM, Platform.X, Platform.PINTEREST).forEach { PlatformIkonu(it, 28.dp) } // logolar
                    Spacer(Modifier.width(4.dp))
                    Text("ve daha fazlası", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall) // diğer siteler
                }
            }
            item { // link kutusu
                OutlinedTextField(
                    value = link, onValueChange = { link = it }, singleLine = true,
                    label = { Text("Video, liste, kanal ya da profil linki") },
                    trailingIcon = { IconButton(onClick = { panoLinki(ctx, herhangi = true)?.let { link = it } }) { Icon(Icons.Rounded.ContentPaste, "Yapıştır") } },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri, imeAction = ImeAction.Done),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            if (cozulmus != null) {
                item { // linkin ne olduğu
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        PlatformIkonu(cozulmus.platform, 36.dp) // site logosu
                        Spacer(Modifier.width(12.dp))
                        Text(linkAciklamasi(cozulmus.platform, cozulmus.turu, cozulmus.kullanici), style = MaterialTheme.typography.titleMedium) // "Instagram profili · @kullanici"
                    }
                }
                // Profil indirmek için giriş gerekiyorsa uyar
                if (cozulmus.turu == LinkTuru.PROFIL && cozulmus.platform in profilIcinGirisGerekli && !Cerezler.girisVar(cozulmus.platform)) item {
                    GirisUyarisi(cozulmus.platform) { model.git(Sayfa.Giris(cozulmus.platform)) }
                }
                if (cozulmus.turu == LinkTuru.PROFIL) {
                    if (cozulmus.sekmeler.isNotEmpty()) item { // bölüm seçimi (Reels / Gönderiler, Videolar / Shorts)
                        Baslik("Hangi bölüm?")
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            cozulmus.sekmeler.forEach { s -> FilterChip(selected = sekme == s, onClick = { sekme = s }, label = { Text(s.ad) }) }
                        }
                    }
                    item { // kaç içerik
                        Baslik("Son kaç içerik?")
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.Center) {
                            hazirSayilar.forEach { n -> FilterChip(selected = sayi == n, onClick = { sayiYazi = n.toString() }, label = { Text("$n") }) }
                        }
                        OutlinedTextField( // istediği sayıyı yazabilsin
                            value = sayiYazi, onValueChange = { y -> sayiYazi = y.filter(Char::isDigit).take(3) }, singleLine = true,
                            label = { Text("Sayı (1-500)") }, isError = sayi == null,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.padding(top = 8.dp).width(160.dp),
                        )
                    }
                }
                if (cozulmus.turu == LinkTuru.LISTE) {
                    val l = liste // getirilen liste
                    when {
                        l == null && listeGetiriliyor -> item { // yükleniyor
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(Modifier.size(24.dp)); Spacer(Modifier.width(12.dp)); Text("Liste getiriliyor…")
                            }
                        }
                        l == null -> item { // henüz getirilmedi
                            listeHatasi?.let { Text(it, color = MaterialTheme.colorScheme.error) } // önceki hata
                            OutlinedButton(onClick = {
                                listeGetiriliyor = true; listeHatasi = null // yükleniyor
                                val istenen = cozulmus // hangi link için getiriliyor
                                listeIsi = kapsam.launch {
                                    val sonuc = runCatching { Listeleyici.listeGetir(ctx, istenen, islemId = listeIslemId) } // listeyi getir
                                    if (!isActive) return@launch // bu arada link değiştiyse sonucu kullanma
                                    sonuc.onSuccess { s -> liste = s; secili.clear(); secili.addAll(s.ogeler.indices) } // hepsi seçili gelsin
                                        .onFailure { if (it !is kotlinx.coroutines.CancellationException) listeHatasi = Listeleyici.anlasilirHata(it.message ?: "", istenen.platform) } // hata
                                    listeGetiriliyor = false // bitti
                                }
                            }) { Text("Listeyi getir ve seç") }
                        }
                        else -> {
                            item { // liste başlığı ve hepsini seç
                                Text(l.ad, style = MaterialTheme.typography.titleMedium, maxLines = 2, overflow = TextOverflow.Ellipsis)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("${secili.size}/${l.ogeler.size} seçili", Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    TextButton(onClick = { secili.clear(); secili.addAll(l.ogeler.indices) }) { Text("Hepsi") } // hepsini seç
                                    TextButton(onClick = { secili.clear() }) { Text("Hiçbiri") } // seçimi kaldır
                                }
                            }
                            itemsIndexed(l.ogeler) { i, oge -> // her öğe işaretlenebilir satır
                                val isaretli = i in secili // seçili mi
                                ListItem(
                                    headlineContent = { Text(oge.baslik.ifBlank { oge.url }, maxLines = 2, overflow = TextOverflow.Ellipsis) }, // başlık
                                    supportingContent = { oge.sureSn?.let { s -> Text(Zaman.yaz((s * 1000).toLong())) } }, // süre (biliniyorsa)
                                    leadingContent = { Checkbox(checked = isaretli, onCheckedChange = { if (it) secili.add(i) else secili.remove(i) }) }, // kutucuk
                                    colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.background),
                                    modifier = Modifier.dokun { if (isaretli) secili.remove(i) else secili.add(i) }, // satıra dokununca da seç
                                )
                            }
                        }
                    }
                }
                if (tur == Tur.VIDEO && (cozulmus.turu != LinkTuru.LISTE || liste != null)) item { // video kalitesi
                    Baslik("Kalite")
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        kaliteler.forEach { (ad, deger) -> FilterChip(selected = kalite == deger, onClick = { kalite = deger }, label = { Text(ad) }) }
                    }
                }
                if (tur == Tur.MUZIK) item { // müzikte kalite sorulmaz
                    Text("En yüksek ses kalitesinde MP3 olarak iner.", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
                }
                if (cozulmus.turu != LinkTuru.TEK) item { // klasör bilgisi
                    Text(
                        "Hepsi tek bir klasöre iner: \"Ad, sayı içerik, ${cozulmus.platform.ad}\". İçine linklerin listesi (Linkler.txt) de konur.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}

// Süren liste getirme işini ve arkasındaki yt-dlp / gallery-dl işlemini durdur
private fun listeyiDurdur(is_: Job?, islemId: String) {
    is_?.cancel() // coroutine'i iptal et
    runCatching { com.yausername.youtubedl_android.YoutubeDL.getInstance().destroyProcessById(islemId) } // yt-dlp işlemi
    com.jn.oynatici.indir.Motor.galeriDurdur(islemId) // gallery-dl işlemi
}

// Küçük bölüm başlığı
@Composable
private fun Baslik(yazi: String) = Text(yazi, style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(bottom = 6.dp))

// "Bu profil için giriş gerekiyor" kutusu
@Composable
fun GirisUyarisi(platform: Platform, girisYap: () -> Unit) {
    Surface(shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.secondaryContainer, modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("${platform.ad} profilleri için giriş yapman gerekiyor. Bir kere giriş yapman yeter.", Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.width(8.dp))
            Button(onClick = girisYap) { Text("Giriş yap") }
        }
    }
}

// Linkin ne olduğunu anlatan kısa yazı
fun linkAciklamasi(platform: Platform, turu: LinkTuru, kullanici: String?): String = when (turu) {
    LinkTuru.TEK -> if (platform == Platform.DIGER) "Tek içerik" else "${platform.ad} içeriği" // "TikTok içeriği"
    LinkTuru.LISTE -> if (platform == Platform.PINTEREST) "Pinterest panosu" else "${platform.ad} oynatma listesi" // liste
    LinkTuru.PROFIL -> "${platform.ad} ${if (platform == Platform.YOUTUBE) "kanalı" else "profili"}" + (kullanici?.let { " · @$it" } ?: "") // profil
}

// Panodaki linki oku. herhangi=false ise sadece desteklenen sitelerin linklerini alır
fun panoLinki(ctx: Context, herhangi: Boolean = false): String? {
    val pano = ctx.getSystemService(ClipboardManager::class.java) // pano servisi
    val metin = pano.primaryClip?.takeIf { it.itemCount > 0 }?.getItemAt(0)?.coerceToText(ctx)?.toString() ?: return null // panodaki yazı
    val link = Regex("""https?://\S+""").find(metin)?.value ?: return if (herhangi) metin.trim() else null // içindeki link
    return if (herhangi || Platform.bul(link) != Platform.DIGER) link else null // bilinen site mi
}
