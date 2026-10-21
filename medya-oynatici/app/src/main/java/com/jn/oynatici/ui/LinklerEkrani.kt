package com.jn.oynatici.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jn.oynatici.data.Platform
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

// Klasördeki Linkler.txt dosyasını okunur gösterir: dokun = içeriği aç, kopyala ikonu = linki kopyala
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LinklerEkrani(model: UygulamaModeli, klasor: File) {
    val ctx = LocalContext.current // bağlam
    val linkler by produceState(emptyList<String>(), klasor) { value = withContext(Dispatchers.IO) { linkleriOku(klasor) } } // dosyadaki linkler (arka planda okunur)
    // Panoya kopyala ve haber ver
    fun kopyala(metin: String, mesaj: String) {
        ctx.getSystemService(ClipboardManager::class.java).setPrimaryClip(ClipData.newPlainText("link", metin)) // panoya koy
        Toast.makeText(ctx, mesaj, Toast.LENGTH_SHORT).show() // bilgi ver
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Linkler", maxLines = 1) }, // başlık
                navigationIcon = { IconButton(onClick = { model.geri() }) { Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Geri") } }, // geri
            )
        },
    ) { bosluk ->
        Column(Modifier.fillMaxSize().padding(bosluk)) {
            Text( // klasör adı ve link sayısı
                "${klasor.name} · ${linkler.size} link", color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp), maxLines = 2, overflow = TextOverflow.Ellipsis,
            )
            FilledTonalButton( // hepsini kopyala
                onClick = { kopyala(linkler.joinToString("\n"), "${linkler.size} link kopyalandı") },
                enabled = linkler.isNotEmpty(),
                modifier = Modifier.fillMaxWidth().padding(16.dp),
            ) { Text("Hepsini kopyala") }
            LazyColumn(contentPadding = PaddingValues(bottom = 24.dp)) { // linkler alt alta
                itemsIndexed(linkler) { i, link ->
                    ListItem(
                        headlineContent = { Text(link, maxLines = 2, overflow = TextOverflow.Ellipsis) }, // link
                        leadingContent = { Text("${i + 1}", color = MaterialTheme.colorScheme.onSurfaceVariant) }, // sıra
                        trailingContent = { IconButton(onClick = { kopyala(link, "Link kopyalandı") }) { Icon(Icons.Rounded.ContentCopy, "Kopyala") } }, // kopyala
                        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.background), // arka plan
                        modifier = Modifier.dokun { // dokununca içeriği aç (YouTube/Instagram uygulaması ya da tarayıcı)
                            runCatching { ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link))) }
                                .onFailure { Toast.makeText(ctx, "Açılamadı", Toast.LENGTH_SHORT).show() }
                        },
                    )
                }
            }
        }
    }
}

// Linkler.txt içindeki https linklerini sırayla oku
fun linkleriOku(klasor: File): List<String> =
    runCatching { File(klasor, Platform.LINK_DOSYASI).readLines() }.getOrDefault(emptyList()) // dosyayı oku
        .map { it.trim() }.filter { it.startsWith("http") } // sadece linkler
