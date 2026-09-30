package com.notibox.app.ui

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.notibox.app.data.Rule
import java.io.File

@Composable
fun AppsScreen(vm: Vm, open: (String) -> Unit) {
    val apps by vm.apps.collectAsState()
    val ex by vm.excluded.collectAsState()
    Column(Modifier.fillMaxSize()) {
        Text("Uygulamalar", Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
        if (apps.isEmpty() && ex.isEmpty()) Text("Henüz kayıt yok.", Modifier.padding(16.dp))
        LazyColumn {
            items(apps, key = { it.pkg }) { a ->
                ListItem(leadingContent = { AppIcon(a.pkg) },
                    headlineContent = { Text(a.appName) },
                    supportingContent = { Text("${a.n} bildirim · ${a.unread} okunmamış") },
                    trailingContent = { IconButton({ vm.setExcluded(a.pkg, true); vm.deleteApp(a.pkg) }) { Icon(Icons.Default.Block, "Toplama") } },
                    modifier = Modifier.clickable { open(a.pkg) })
            }
            if (ex.isNotEmpty()) {
                item { Text("Toplanmayan uygulamalar", Modifier.padding(16.dp, 16.dp, 16.dp, 4.dp), style = MaterialTheme.typography.titleSmall) }
                items(ex, key = { "x$it" }) { p ->
                    ListItem(leadingContent = { AppIcon(p) }, headlineContent = { Text(p) },
                        trailingContent = { TextButton({ vm.setExcluded(p, false) }) { Text("Tekrar topla") } })
                }
            }
        }
    }
}

@Composable
fun RulesScreen(vm: Vm) {
    val rules by vm.rules.collectAsState()
    val apps by vm.apps.collectAsState()
    var show by remember { mutableStateOf(false) }
    Box(Modifier.fillMaxSize()) {
        Column {
            Text("Kurallar", Modifier.padding(16.dp, 16.dp, 16.dp, 0.dp), style = MaterialTheme.typography.titleLarge)
            Text("Anahtar kelime / uygulama bazlı filtreler. ENGELLE: bildirim çubuğundan kaldırılır ve kaydedilmez. SESSİZ: çubuktan kaldırılır, NotiBox'ta saklanır.",
                Modifier.padding(16.dp, 4.dp), style = MaterialTheme.typography.bodySmall)
            LazyColumn {
                items(rules, key = { it.id }) { r ->
                    ListItem(headlineContent = { Text(if (r.keyword.isEmpty()) "Tüm bildirimler" else "“${r.keyword}”") },
                        supportingContent = { Text("${if (r.action == "BLOCK") "Engelle" else "Sessize al"} · ${if (r.pkg.isEmpty()) "tüm uygulamalar" else apps.firstOrNull { it.pkg == r.pkg }?.appName ?: r.pkg}") },
                        trailingContent = { Row(verticalAlignment = Alignment.CenterVertically) {
                            Switch(r.enabled, { vm.toggleRule(r) }); IconButton({ vm.delRule(r) }) { Icon(Icons.Default.Delete, "Sil") } } })
                }
            }
        }
        FloatingActionButton({ show = true }, Modifier.align(Alignment.BottomEnd).padding(16.dp)) { Icon(Icons.Default.Add, "Kural ekle") }
    }
    if (show) {
        var kw by remember { mutableStateOf("") }
        var pkg by remember { mutableStateOf("") }
        var act by remember { mutableStateOf("MUTE") }
        var dd by remember { mutableStateOf(false) }
        AlertDialog(onDismissRequest = { show = false }, title = { Text("Yeni kural") },
            text = { Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(kw, { kw = it }, label = { Text("Anahtar kelime (boş = hepsi)") }, singleLine = true)
                Box {
                    OutlinedButton({ dd = true }) { Text(apps.firstOrNull { it.pkg == pkg }?.appName ?: "Tüm uygulamalar") }
                    DropdownMenu(dd, { dd = false }) {
                        DropdownMenuItem({ Text("Tüm uygulamalar") }, { pkg = ""; dd = false })
                        apps.forEach { a -> DropdownMenuItem({ Text(a.appName) }, { pkg = a.pkg; dd = false }) }
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(act == "MUTE", { act = "MUTE" }); Text("Sessize al")
                    Spacer(Modifier.width(12.dp)); RadioButton(act == "BLOCK", { act = "BLOCK" }); Text("Engelle")
                }
            } },
            confirmButton = { TextButton({ if (kw.isNotBlank() || pkg.isNotEmpty()) { vm.addRule(pkg, kw, act); show = false } }) { Text("Ekle") } },
            dismissButton = { TextButton({ show = false }) { Text("İptal") } })
    }
}

@Composable
fun StatsScreen(vm: Vm) {
    val days by vm.perDay.collectAsState()
    val apps by vm.apps.collectAsState()
    val all by vm.all.collectAsState()
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        Text("İstatistik", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("Toplam", all.size.toString(), Modifier.weight(1f))
            StatCard("Okunmamış", all.count { !it.read }.toString(), Modifier.weight(1f))
            StatCard("Uygulama", apps.size.toString(), Modifier.weight(1f))
        }
        Spacer(Modifier.height(20.dp))
        Text("Son 14 gün", style = MaterialTheme.typography.titleMedium)
        val max = (days.maxOfOrNull { it.n } ?: 1).coerceAtLeast(1)
        days.forEach { d ->
            Row(Modifier.padding(vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(d.day.takeLast(5), Modifier.width(48.dp), style = MaterialTheme.typography.labelSmall)
                Box(Modifier.height(14.dp).fillMaxWidth(d.n / max.toFloat() * 0.8f).background(MaterialTheme.colorScheme.primary))
                Spacer(Modifier.width(6.dp)); Text("${d.n}", style = MaterialTheme.typography.labelSmall)
            }
        }
        Spacer(Modifier.height(20.dp))
        Text("En gürültülü uygulamalar", style = MaterialTheme.typography.titleMedium)
        apps.take(8).forEach { a ->
            ListItem(leadingContent = { AppIcon(a.pkg, 32) }, headlineContent = { Text(a.appName) }, trailingContent = { Text("${a.n}") })
        }
    }
}

@Composable
fun StatCard(label: String, v: String, m: Modifier) = Card(m) {
    Column(Modifier.padding(12.dp)) { Text(v, style = MaterialTheme.typography.headlineSmall); Text(label, style = MaterialTheme.typography.labelMedium) }
}

@Composable
fun SettingsScreen(vm: Vm) {
    val ctx = LocalContext.current
    val p = vm.prefs
    var cap by remember { mutableStateOf(p.capturing) }
    var ongoing by remember { mutableStateOf(p.hideOngoing) }
    var sens by remember { mutableStateOf(p.hideSensitive) }
    var lock by remember { mutableStateOf(p.appLock) }
    var ret by remember { mutableIntStateOf(p.retentionDays) }
    var policy by remember { mutableStateOf(false) }
    var confirm by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Text("Ayarlar", Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
        SwitchRow("Bildirimleri topla", "Kapatırsan yeni bildirim kaydedilmez", cap) { cap = it; p.capturing = it }
        SwitchRow("Süregelen bildirimleri atla", "Müzik çalar, navigasyon gibi kalıcı bildirimler", ongoing) { ongoing = it; p.hideOngoing = it }
        SwitchRow("Gizli bildirimleri atla", "Uygulamanın \"gizli\" işaretlediği içerik (ör. OTP)", sens) { sens = it; p.hideSensitive = it }
        SwitchRow("Uygulama kilidi", "Parmak izi / yüz / ekran kilidi ile aç", lock) { lock = it; p.appLock = it }
        ListItem(headlineContent = { Text("Saklama süresi") }, supportingContent = { Text(if (ret == 0) "Süresiz" else "$ret gün (yıldızlılar silinmez)") },
            trailingContent = { TextButton({ ret = when (ret) { 7 -> 30; 30 -> 90; 90 -> 0; else -> 7 }; p.retentionDays = ret }) { Text("Değiştir") } })
        HorizontalDivider()
        ListItem(headlineContent = { Text("Bildirim erişimi ayarları") }, leadingContent = { Icon(Icons.Default.Security, null) },
            modifier = Modifier.clickable { ctx.startActivity(Intent(android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)) })
        ListItem(headlineContent = { Text("CSV olarak dışa aktar") }, leadingContent = { Icon(Icons.Default.Share, null) },
            modifier = Modifier.clickable {
                val f = File(ctx.cacheDir, "exports").apply { mkdirs() }.let { File(it, "notibox.csv") }
                f.writeText(vm.exportCsv())
                val uri = FileProvider.getUriForFile(ctx, "${ctx.packageName}.files", f)
                ctx.startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).setType("text/csv")
                    .putExtra(Intent.EXTRA_STREAM, uri).addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION), "Dışa aktar"))
            })
        ListItem(headlineContent = { Text("Tümünü temizle (yıldızlılar hariç)") }, leadingContent = { Icon(Icons.Default.DeleteSweep, null) },
            modifier = Modifier.clickable { confirm = true })
        ListItem(headlineContent = { Text("Gizlilik politikası") }, leadingContent = { Icon(Icons.Default.PrivacyTip, null) },
            modifier = Modifier.clickable { policy = true })
        Text("NotiBox 1.0.0", Modifier.padding(16.dp), style = MaterialTheme.typography.labelMedium)
    }
    if (confirm) AlertDialog({ confirm = false }, title = { Text("Emin misin?") }, text = { Text("Yıldızsız tüm kayıtlı bildirimler silinecek.") },
        confirmButton = { TextButton({ vm.clear(); confirm = false }) { Text("Sil") } }, dismissButton = { TextButton({ confirm = false }) { Text("Vazgeç") } })
    if (policy) AlertDialog({ policy = false }, title = { Text("Gizlilik politikası") },
        text = { Column(Modifier.verticalScroll(rememberScrollState())) { Text(PRIVACY) } },
        confirmButton = { TextButton({ policy = false }) { Text("Kapat") } })
}

@Composable
fun SwitchRow(t: String, s: String, v: Boolean, on: (Boolean) -> Unit) =
    ListItem(headlineContent = { Text(t) }, supportingContent = { Text(s) }, trailingContent = { Switch(v, on) })

const val PRIVACY = "NotiBox, cihazına gelen bildirimlerin başlık ve metnini yalnızca bu cihazdaki özel depolama alanına kaydeder. " +
    "Uygulamanın INTERNET izni yoktur; hiçbir veri geliştiriciye veya üçüncü taraflara gönderilmez, reklam/analiz SDK'sı içermez.\n\n" +
    "Bildirim erişimi izni yalnızca bildirimleri toplayıp listelemek, filtrelemek ve ertelemek için kullanılır. " +
    "Bu izni Android Ayarları'ndan istediğin an kapatabilir, uygulamayı kaldırarak tüm verileri silebilirsin. " +
    "Ayarlar'dan tüm kayıtları silebilir, belirli uygulamaları toplamadan hariç tutabilirsin. " +
    "CSV dışa aktarma yalnızca senin seçtiğin uygulamayla paylaşılır."
