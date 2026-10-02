package com.notivo.app.ui

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
import com.notivo.app.data.Logic
import com.notivo.app.data.Notif
import androidx.compose.foundation.clickable
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

private val dayLabels = listOf("Bugün", "Dün", "Bu hafta", "Daha eski")

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun InboxScreen(vm: Vm, pkg: String?, clearPkg: () -> Unit) {
    val ctx = LocalContext.current
    val all by vm.all.collectAsState()
    var q by remember { mutableStateOf("") }
    var mode by remember { mutableIntStateOf(0) }   // 0 all, 1 unread, 2 starred, 3 messages
    var byConv by remember { mutableStateOf(false) }
    var menu by remember { mutableStateOf<Notif?>(null) }
    var thread by remember { mutableStateOf<Pair<String, String>?>(null) }
    val list = remember(all, q, mode, pkg) {
        all.filter { n ->
            (pkg == null || n.pkg == pkg) &&
            when (mode) { 1 -> !n.read; 2 -> n.starred; 3 -> n.isMessage; else -> true } &&
            (q.isBlank() || Logic.norm("${n.title} ${n.text} ${n.appName}").contains(Logic.norm(q)))
        }
    }
    // one row per conversation (app + sender/title): newest message, total count
    val convs = remember(list) { list.groupBy { it.pkg to it.title }.values.map { it.first() to it.size }.sortedByDescending { it.first.time } }
    val now = System.currentTimeMillis()
    val off = remember { java.util.TimeZone.getDefault().getOffset(now).toLong() }

    Column(Modifier.fillMaxSize()) {
        OutlinedTextField(q, { q = it }, Modifier.fillMaxWidth().padding(12.dp, 8.dp), singleLine = true,
            placeholder = { Text("Bildirimlerde ara") }, leadingIcon = { Icon(Icons.Default.Search, null) },
            trailingIcon = { Row { if (q.isNotEmpty()) IconButton({ q = "" }) { Icon(Icons.Default.Close, "Temizle") }
                IconButton({ vm.readAll() }) { Icon(Icons.Default.DoneAll, "Tümünü okundu yap") } } })
        Row(Modifier.padding(horizontal = 12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Tümü", "Okunmamış", "Yıldızlı", "Mesajlar").forEachIndexed { i, l -> FilterChip(mode == i, { mode = i }, { Text(l) }) }
        }
        Row(Modifier.padding(horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            if (pkg != null) AssistChip(onClick = clearPkg, label = { Text("Filtre: ${all.firstOrNull { it.pkg == pkg }?.appName ?: pkg}  ✕") })
            Spacer(Modifier.weight(1f))
            Text("Konuşmalar", style = MaterialTheme.typography.labelMedium); Spacer(Modifier.width(6.dp))
            Switch(byConv, { byConv = it })
        }
        if (list.isEmpty()) {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text(if (all.isEmpty()) "Henüz bildirim yok.\nYeni bildirimler burada toplanacak." else "Sonuç yok",
                    color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(32.dp))
            }
        } else if (byConv) LazyColumn {
            items(convs, key = { "${it.first.pkg}|${it.first.title}" }) { (n, count) ->
                ListItem(leadingContent = { AppIcon(n.pkg) },
                    overlineContent = { Text(n.appName) },
                    headlineContent = { Text(n.title.ifBlank { "(başlıksız)" }, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                    supportingContent = { Text(n.text, maxLines = 2, overflow = TextOverflow.Ellipsis) },
                    trailingContent = { Column(horizontalAlignment = Alignment.End) { Text(whenStr(n.time), style = MaterialTheme.typography.labelSmall)
                        if (count > 1) Badge { Text("$count") } } },
                    modifier = Modifier.clickable { thread = n.pkg to n.title })
                HorizontalDivider()
            }
        } else LazyColumn(state = rememberLazyListState()) {
            var last = -1
            list.forEach { n ->
                val b = Logic.dayBucket(now, n.time, off)
                if (b != last) { last = b; item(key = "h$b") { Text(dayLabels[b], Modifier.padding(16.dp, 12.dp, 16.dp, 4.dp),
                    style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary) } }
                item(key = n.id) {
                    val dismiss = rememberSwipeToDismissBoxState(confirmValueChange = { if (it != SwipeToDismissBoxValue.Settled) { vm.delete(n); true } else false })
                    SwipeToDismissBox(dismiss, backgroundContent = { Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.errorContainer)) }) {
                        NotifRow(n, Modifier.background(MaterialTheme.colorScheme.surface)
                            .combinedClickable(onClick = { if (!vm.open(n)) toast(ctx, "Uygulama açılamadı") }, onLongClick = { menu = n }))
                    }
                    HorizontalDivider()
                }
            }
        }
    }
    menu?.let { n ->
        ModalBottomSheet({ menu = null }) {
            Column(Modifier.padding(bottom = 24.dp)) {
                Text(n.title.ifBlank { n.appName }, Modifier.padding(16.dp, 0.dp), style = MaterialTheme.typography.titleMedium, maxLines = 1)
                SheetItem(Icons.Default.Forum, "Konuşmayı aç") { thread = n.pkg to n.title; menu = null }
                SheetItem(Icons.Default.Star, if (n.starred) "Yıldızı kaldır" else "Yıldızla") { vm.star(n); menu = null }
                SheetItem(Icons.Default.Done, "Okundu işaretle") { vm.read(n); menu = null }
                SheetItem(Icons.Default.Snooze, "15 dk sonra hatırlat") { vm.snooze(n, 15); menu = null }
                SheetItem(Icons.Default.Snooze, "1 saat sonra hatırlat") { vm.snooze(n, 60); menu = null }
                SheetItem(Icons.Default.Snooze, "12 saat sonra hatırlat") { vm.snooze(n, 720); menu = null }
                SheetItem(Icons.Default.Block, "${n.appName} bildirimlerini toplama") { vm.setExcluded(n.pkg, true); vm.deleteApp(n.pkg); menu = null }
                SheetItem(Icons.Default.Delete, "Sil") { vm.delete(n); menu = null }
            }
        }
    }
    thread?.let { (p, t) ->
        val msgs = remember(all, p, t) { all.filter { it.pkg == p && it.title == t }.sortedBy { it.time } }
        var reply by remember { mutableStateOf("") }
        ModalBottomSheet({ thread = null }, sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)) {
            Column(Modifier.fillMaxHeight(0.9f)) {
                Row(Modifier.padding(16.dp, 0.dp, 16.dp, 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    AppIcon(p, 32); Spacer(Modifier.width(12.dp))
                    Text(t.ifBlank { "(başlıksız)" }, style = MaterialTheme.typography.titleMedium, maxLines = 1)
                }
                LazyColumn(Modifier.weight(1f).padding(horizontal = 16.dp), reverseLayout = false) {
                    items(msgs, key = { it.id }) { m ->
                        Column(Modifier.padding(vertical = 6.dp)) {
                            Text(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(Date(m.time)), style = MaterialTheme.typography.labelSmall)
                            Text(m.text)
                            if (m.deletedBySender) Text("🗑 Gönderen bu mesajı silmiş olabilir", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
                if (vm.canReply(p, t)) Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(reply, { reply = it }, Modifier.weight(1f), placeholder = { Text("Yanıtla") }, singleLine = true)
                    IconButton({ if (reply.isNotBlank() && vm.reply(p, t, reply)) { reply = ""; toast(ctx, "Gönderildi") } else toast(ctx, "Yanıt gönderilemedi") }) { Icon(Icons.Default.Send, "Gönder") }
                } else Text("Yanıt, yalnızca bildirim hâlâ canlıyken (ve uygulama destekliyorsa) mümkün.", Modifier.padding(16.dp), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotifRow(n: Notif, modifier: Modifier) {
    Row(modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.Top) {
        AppIcon(n.pkg)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (!n.read) Box(Modifier.size(8.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary)).also { Spacer(Modifier.width(6.dp)) }
                Text(n.appName, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary, modifier = Modifier.weight(1f), maxLines = 1)
                if (n.starred) Icon(Icons.Default.Star, null, Modifier.size(14.dp), tint = MaterialTheme.colorScheme.tertiary)
                Spacer(Modifier.width(4.dp)); Text(whenStr(n.time), style = MaterialTheme.typography.labelSmall)
            }
            val w = if (n.read) FontWeight.Normal else FontWeight.Bold
            if (n.title.isNotBlank()) Text(n.title, fontWeight = w, maxLines = 1, overflow = TextOverflow.Ellipsis)
            if (n.text.isNotBlank()) Text(n.text, maxLines = 3, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (n.deletedBySender) Text("🗑 Gönderen silmiş olabilir", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.error)
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
