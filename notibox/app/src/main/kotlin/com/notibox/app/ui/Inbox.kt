package com.notibox.app.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.notibox.app.data.Notif
import java.text.DateFormat
import java.util.Date

@Composable
fun AppIcon(pkg: String, size: Int = 40) {
    val pm = LocalContext.current.packageManager
    val bmp = remember(pkg) { try { pm.getApplicationIcon(pkg).toBitmap(96, 96).asImageBitmap() } catch (_: Exception) { null } }
    if (bmp != null) Image(bmp, null, Modifier.size(size.dp).clip(CircleShape))
    else Box(Modifier.size(size.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer))
}

fun whenStr(t: Long): String {
    val d = System.currentTimeMillis() - t
    return when {
        d < 60_000 -> "şimdi"
        d < 3_600_000 -> "${d / 60_000} dk"
        d < 86_400_000 -> "${d / 3_600_000} sa"
        else -> DateFormat.getDateInstance(DateFormat.SHORT).format(Date(t))
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun InboxScreen(vm: Vm, pkg: String?, clearPkg: () -> Unit) {
    val ctx = LocalContext.current
    val all by vm.all.collectAsState()
    var q by remember { mutableStateOf("") }
    var mode by remember { mutableIntStateOf(0) }   // 0 all, 1 unread, 2 starred, 3 messages
    var menu by remember { mutableStateOf<Notif?>(null) }
    var group by remember { mutableStateOf(false) }
    val list = remember(all, q, mode, pkg) {
        all.filter { n ->
            (pkg == null || n.pkg == pkg) &&
            when (mode) { 1 -> !n.read; 2 -> n.starred; 3 -> n.isMessage; else -> true } &&
            (q.isBlank() || "${n.title} ${n.text} ${n.appName}".contains(q, true))
        }
    }
    Column(Modifier.fillMaxSize()) {
        OutlinedTextField(q, { q = it }, Modifier.fillMaxWidth().padding(12.dp, 8.dp), singleLine = true,
            placeholder = { Text("Bildirimlerde ara") }, leadingIcon = { Icon(Icons.Default.Search, null) },
            trailingIcon = { Row { if (q.isNotEmpty()) IconButton({ q = "" }) { Icon(Icons.Default.Close, "Temizle") }
                IconButton({ vm.readAll() }) { Icon(Icons.Default.DoneAll, "Tümünü okundu yap") } } })
        Row(Modifier.padding(horizontal = 12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Tümü", "Okunmamış", "Yıldızlı", "Mesajlar").forEachIndexed { i, l ->
                FilterChip(mode == i, { mode = i }, { Text(l) })
            }
        }
        if (pkg != null) AssistChip(onClick = clearPkg, label = { Text("Filtre: ${all.firstOrNull { it.pkg == pkg }?.appName ?: pkg}  ✕") },
            modifier = Modifier.padding(horizontal = 12.dp))
        if (list.isEmpty()) {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text(if (all.isEmpty()) "Henüz bildirim yok.\nYeni bildirimler burada toplanacak." else "Sonuç yok",
                    color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(32.dp))
            }
        } else LazyColumn(state = rememberLazyListState()) {
            items(list, key = { it.id }) { n ->
                val dismiss = rememberSwipeToDismissBoxState(confirmValueChange = { if (it != SwipeToDismissBoxValue.Settled) { vm.delete(n); true } else false })
                SwipeToDismissBox(dismiss, backgroundContent = { Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.errorContainer)) }) {
                    Row(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface)
                        .combinedClickable(onClick = { if (!vm.open(n)) toast(ctx, "Uygulama açılamadı") }, onLongClick = { menu = n })
                        .padding(12.dp), verticalAlignment = Alignment.Top) {
                        AppIcon(n.pkg)
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Row {
                                Text(n.appName, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary, modifier = Modifier.weight(1f), maxLines = 1)
                                if (n.starred) Icon(Icons.Default.Star, null, Modifier.size(14.dp), tint = MaterialTheme.colorScheme.tertiary)
                                Spacer(Modifier.width(4.dp)); Text(whenStr(n.time), style = MaterialTheme.typography.labelSmall)
                            }
                            val w = if (n.read) FontWeight.Normal else FontWeight.Bold
                            if (n.title.isNotBlank()) Text(n.title, fontWeight = w, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            if (n.text.isNotBlank()) Text(n.text, maxLines = 3, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
                HorizontalDivider()
            }
        }
    }
    menu?.let { n ->
        ModalBottomSheet({ menu = null }) {
            Column(Modifier.padding(bottom = 24.dp)) {
                Text(n.title.ifBlank { n.appName }, Modifier.padding(16.dp, 0.dp), style = MaterialTheme.typography.titleMedium, maxLines = 1)
                SheetItem(Icons.Default.Star, if (n.starred) "Yıldızı kaldır" else "Yıldızla") { vm.star(n); menu = null }
                SheetItem(Icons.Default.Done, "Okundu işaretle") { vm.read(n); menu = null }
                SheetItem(Icons.Default.Snooze, "15 dk sonra hatırlat") { vm.snooze(n, 15); menu = null }
                SheetItem(Icons.Default.Snooze, "1 saat sonra hatırlat") { vm.snooze(n, 60); menu = null }
                SheetItem(Icons.Default.Snooze, "Yarın sabaha hatırlat (12 sa)") { vm.snooze(n, 720); menu = null }
                SheetItem(Icons.Default.Block, "${n.appName} bildirimlerini toplama") { vm.setExcluded(n.pkg, true); vm.deleteApp(n.pkg); menu = null }
                SheetItem(Icons.Default.Delete, "Sil") { vm.delete(n); menu = null }
            }
        }
    }
}

@Composable
fun SheetItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
    ListItem(headlineContent = { Text(label) }, leadingContent = { Icon(icon, null) },
        modifier = Modifier.combinedClickableNoRipple(onClick))
}

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.combinedClickableNoRipple(onClick: () -> Unit) = this.combinedClickable(onClick = onClick)

fun toast(c: Context, s: String) = Toast.makeText(c, s, Toast.LENGTH_SHORT).show()
