package com.jn.koken.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jn.koken.MainViewModel
import com.jn.koken.UiState
import com.jn.koken.data.EtymologyBlock
import com.jn.koken.data.LanguageSection
import com.jn.koken.data.LookupResult
import com.jn.koken.data.Mention
import com.jn.koken.data.Narrator
import com.jn.koken.data.Story
import com.jn.koken.data.PosEntry
import com.jn.koken.data.SenseGroup
import com.jn.koken.data.Source
import com.jn.koken.data.WikiClient
import com.jn.koken.data.WikipediaResult
import com.jn.koken.data.WiktionaryResult
import java.util.Locale

private val EXAMPLES = listOf("Ahmet", "kitap", "salary", "Elif", "yoğurt", "telefon")

@Composable
fun KokenApp(vm: MainViewModel) {
    val focus = LocalFocusManager.current
    Scaffold(containerColor = MaterialTheme.colorScheme.background) { pad ->
        Column(Modifier.padding(pad).imePadding().fillMaxSize()) {
            Header()
            SearchField(
                value = vm.query,
                onChange = { vm.query = it },
                onSearch = { focus.clearFocus(); vm.submit() },
                onClear = { vm.clear() },
            )
            Spacer(Modifier.size(Sp.md))
            when (val s = vm.state) {
                UiState.Idle -> IdleView(vm.history, onPick = { focus.clearFocus(); vm.submit(it) })
                is UiState.Loading -> LoadingView(s.query)
                is UiState.Result -> ResultView(s, onRetry = { vm.submit(s.data.query) })
                is UiState.NotFound -> NotFoundView(s.query)
                is UiState.Error -> ErrorView(s.rateLimited, onRetry = { vm.submit(s.query) })
            }
        }
    }
}

@Composable
private fun Header() {
    Column(Modifier.padding(horizontal = Sp.screen, vertical = Sp.lg), verticalArrangement = Arrangement.spacedBy(Sp.xs)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            LogoMark()
            Spacer(Modifier.width(Sp.md))
            Text("Köken", fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 30.sp, color = MaterialTheme.colorScheme.primary)
        }
        Text("Sözcüğün ve ismin kökenini tek yerden bul", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun SearchField(value: String, onChange: (String) -> Unit, onSearch: () -> Unit, onClear: () -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        modifier = Modifier.fillMaxWidth().padding(horizontal = Sp.screen).heightIn(min = 56.dp),
        placeholder = { Text("Sözcük ya da isim yaz (örn. Ahmet)") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = onClear) { Icon(Icons.Default.Close, contentDescription = "Temizle") }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.None, autoCorrect = false, imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
        ),
    )
}

@Composable
private fun IdleView(history: List<String>, onPick: (String) -> Unit) {
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = Sp.screen, vertical = Sp.sm),
        verticalArrangement = Arrangement.spacedBy(Sp.md),
    ) {
        SectionLabel("Dene")
        PillRow(EXAMPLES, onPick)
        if (history.isNotEmpty()) {
            Spacer(Modifier.size(Sp.sm))
            SectionLabel("Son aramalar")
            PillRow(history, onPick)
        }
        Spacer(Modifier.size(Sp.sm))
        KokenCard {
            Text("Nasıl çalışır?", style = MaterialTheme.typography.titleMedium, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold)
            Text(
                "Yazdığın sözcük ya da ismin kökenini Vikisözlük, Wiktionary ve Vikipedi'den canlı olarak getirir: hangi dilden geldiği, ne anlama geldiği ve zaman içinde nasıl değiştiği. Hesap ya da anahtar gerekmez; internet yeterli.",
                style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun LoadingView(query: String) {
    StateMessage(
        icon = { CircularProgressIndicator(color = MaterialTheme.colorScheme.primary) },
        title = "Aranıyor…",
        body = "“$query” için kaynaklar taranıyor",
    )
}

@Composable
private fun NotFoundView(query: String) {
    val ctx = LocalContext.current
    StateMessage(
        icon = { Icon(Icons.Default.Search, null, Modifier.size(40.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant) },
        title = "“$query” bulunamadı",
        body = "Yazımı kontrol et ya da başka bir yazılışını dene. Aşağıdan diğer sözlüklerde de arayabilirsin.",
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            TextButton(onClick = { open(ctx, "https://tr.wiktionary.org/w/index.php?search=" + Uri.encode(query)) }) { Text("Vikisözlük'te ara ↗") }
            TextButton(onClick = { open(ctx, nisanyan(query)) }) { Text("Nişanyan Sözlük'te ara ↗") }
        }
    }
}

@Composable
private fun ErrorView(rateLimited: Boolean, onRetry: () -> Unit) {
    StateMessage(
        icon = { Icon(Icons.Default.Warning, null, Modifier.size(40.dp), tint = MaterialTheme.colorScheme.error) },
        title = if (rateLimited) "Sunucu şu an yoğun" else "İnternete ulaşılamadı",
        body = if (rateLimited) "Kaynak site kısa süreliğine istek sınırına ulaştı. Birkaç saniye sonra tekrar dene."
        else "Bağlantını kontrol edip tekrar dene.",
    ) {
        Button(onClick = onRetry, modifier = Modifier.heightIn(min = 48.dp)) { Text("Tekrar dene") }
    }
}

private fun WiktionaryResult?.hasEtymology(): Boolean =
    this != null && languages.any { l -> l.groups.any { it.etymology != null } }

private enum class SourceTab(val label: String) { WT("Vikisözlük"), WE("Wiktionary EN"), WP("Vikipedi") }

@Composable
private fun ResultView(s: UiState.Result, onRetry: () -> Unit) {
    val data = s.data
    // Anlatım: kaynak sayfa ve çeviriler geldikçe yeniden üretilir
    val story = remember(data, s.deep, s.translations, s.busy) { Narrator.compose(data, s.deep, s.translations, s.busy) }
    var details by rememberSaveable(data.query) { mutableStateOf(false) }
    val tabs = buildList {
        if (data.wiktTr != null) add(SourceTab.WT)
        if (data.wiktEn != null) add(SourceTab.WE)
        if (data.wiki != null) add(SourceTab.WP)
    }
    // Ham kaynaklarda köken bilgisi olan ilk sekme açık gelsin
    val initial = tabs.indexOfFirst {
        (it == SourceTab.WT && data.wiktTr.hasEtymology()) || (it == SourceTab.WE && data.wiktEn.hasEtymology())
    }.coerceAtLeast(0)
    var selected by rememberSaveable(data.query) { mutableIntStateOf(initial) }
    var showAll by rememberSaveable(data.query, selected) { mutableStateOf(false) }
    val tab = tabs[selected.coerceIn(0, tabs.size - 1)]
    val sourceUrl = when (tab) {
        SourceTab.WT -> data.wiktTr?.url
        SourceTab.WE -> data.wiktEn?.url
        SourceTab.WP -> data.wiki?.url
    }.orEmpty()

    LazyColumn(
        Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = Sp.screen, vertical = Sp.sm),
        verticalArrangement = Arrangement.spacedBy(Sp.lg),
    ) {
        if (data.incomplete && !s.busy) item { IncompleteBanner(onRetry) }
        item { StoryCard(story, s.busy) }
        if (story.timeline.isNotEmpty()) item { JourneyCard(story) }
        if (story.related.isNotEmpty()) item { RelatedCard(story.related) }
        item {
            TextButton(onClick = { details = !details }, modifier = Modifier.heightIn(min = 48.dp)) {
                Text(if (details) "Kaynak ayrıntılarını gizle ▴" else "Kaynak ayrıntılarını göster ▾")
            }
        }
        if (details) {
            if (tabs.size > 1) {
                item {
                    TabRow(
                        selectedTabIndex = selected.coerceIn(0, tabs.size - 1),
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.primary,
                    ) {
                        tabs.forEachIndexed { i, t ->
                            Tab(selected = i == selected, onClick = { selected = i }, text = { Text(t.label, fontWeight = FontWeight.Bold, maxLines = 1) })
                        }
                    }
                }
            }
            when (tab) {
                SourceTab.WT, SourceTab.WE -> {
                    val r: WiktionaryResult = if (tab == SourceTab.WT) data.wiktTr!! else data.wiktEn!!
                    val langs = r.languages.sortedBy { LookupResult.languageRank(it.language) }
                    val shown = if (showAll) langs else langs.take(5)
                    items(shown.size) { LanguageCard(shown[it], r) }
                    if (langs.size > shown.size) {
                        item {
                            TextButton(onClick = { showAll = true }, modifier = Modifier.heightIn(min = 48.dp)) {
                                Text("Diğer ${langs.size - shown.size} dili de göster")
                            }
                        }
                    }
                }
                SourceTab.WP -> item { WikipediaCard(data.wiki!!) }
            }
        }
        item { SourceFooter(tab.label, sourceUrl, data.query) }
    }
}

/** Ana kart: sözcük + "şu anlama geliyor, kökü şu, şöyle gelmiş" diye düz Türkçe anlatım */
@Composable
private fun StoryCard(story: Story, busy: Boolean) {
    KokenCard {
        Text(
            story.word,
            fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 36.sp, lineHeight = 42.sp,
            color = MaterialTheme.colorScheme.onSurface,
        )
        story.badge?.let {
            Pill(it, container = MaterialTheme.colorScheme.primary, content = MaterialTheme.colorScheme.onPrimary)
        }
        story.paras.forEachIndexed { i, p ->
            Column(verticalArrangement = Arrangement.spacedBy(Sp.xs)) {
                p.label?.let { SectionLabel(it) }
                // ⟦kalın⟧ işaretli kısımlar (anlam, kök harfleri, sözcükler) vurgulanır
                Text(
                    markup(p.text),
                    fontSize = if (i == 0) 19.sp else 17.sp,
                    lineHeight = if (i == 0) 28.sp else 27.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
        if (busy) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Sp.sm)) {
                CircularProgressIndicator(Modifier.size(16.dp), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.primary)
                Text("Ayrıntılar getiriliyor…", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

/** Bir kaynağa ulaşılamadıysa: sonuç eksik olabilir, tekrar dene */
@Composable
private fun IncompleteBanner(onRetry: () -> Unit) {
    KokenCard {
        Text(
            "Bazı kaynaklara ulaşılamadı (internet yavaş olabilir). Aşağıdaki bilgi eksik olabilir.",
            style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface,
        )
        TextButton(onClick = onRetry, modifier = Modifier.heightIn(min = 48.dp)) { Text("Tekrar dene") }
    }
}

/** "⟦kalın⟧" işaretlerini kalın + vurgu rengiyle gösterir */
@Composable
private fun markup(text: String): AnnotatedString {
    val color = MaterialTheme.colorScheme.primary
    return buildAnnotatedString {
        var i = 0
        while (i < text.length) {
            val open = text.indexOf('⟦', i)
            if (open < 0) { append(text.substring(i)); break }
            append(text.substring(i, open))
            val close = text.indexOf('⟧', open)
            if (close < 0) { append(text.substring(open + 1)); break }
            withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = color)) { append(text.substring(open + 1, close)) }
            i = close + 1
        }
    }
}

@Composable
private fun JourneyCard(story: Story) {
    KokenCard {
        SectionLabel("Yolculuğu")
        Timeline(story.timeline, story.endLanguage, story.word)
    }
}

@Composable
private fun RelatedCard(related: List<Mention>) {
    KokenCard {
        SectionLabel("Akraba sözcükler")
        Text(
            related.take(8).joinToString("  ·  ") { "${it.text} (${it.langName})" },
            style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun LanguageCard(section: LanguageSection, r: WiktionaryResult) {
    KokenCard {
        Pill(
            section.language,
            container = MaterialTheme.colorScheme.secondaryContainer,
            content = MaterialTheme.colorScheme.onSecondaryContainer,
        )
        section.groups.forEach { g -> GroupView(g, section.language, r) }
    }
}

@Composable
private fun GroupView(g: SenseGroup, language: String, r: WiktionaryResult) {
    Column(verticalArrangement = Arrangement.spacedBy(Sp.md)) {
        if (g.heading.isNotEmpty()) {
            Text(g.heading, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
        g.etymology?.let { EtymologyView(it, language, g.entries.firstOrNull()?.headword ?: r.title, r.source) }
        g.entries.forEach { EntryView(it) }
    }
}

@Composable
private fun EtymologyView(e: EtymologyBlock, language: String, word: String, source: Source) {
    Column(verticalArrangement = Arrangement.spacedBy(Sp.sm)) {
        SectionLabel("Köken")
        if (e.chain.isNotEmpty()) Timeline(e.chain, language, word)
        if (e.parts.isNotEmpty()) {
            Text(
                "Parçalar: " + e.parts.joinToString(" + ") { "${it.text} (${it.langName})" },
                style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium,
            )
        }
        Text(e.text, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
        if (source == Source.WIKT_EN) {
            Text("Açıklama İngilizce kaynaktan olduğu gibi alınmıştır.", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        if (e.related.isNotEmpty()) {
            Text(
                "Akraba sözcükler: " + e.related.take(8).joinToString(", ") { "${it.text} (${it.langName})" },
                style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun EntryView(p: PosEntry) {
    Column(verticalArrangement = Arrangement.spacedBy(Sp.xs)) {
        SectionLabel("Anlam · ${p.pos}")
        p.definitions.forEachIndexed { i, d ->
            Text("${i + 1}. $d", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Composable
private fun WikipediaCard(w: WikipediaResult) {
    KokenCard {
        Text(w.title, style = MaterialTheme.typography.titleMedium, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold)
        if (w.facts.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(Sp.xs)) {
                w.facts.forEach { (k, v) ->
                    Row {
                        Text(k, Modifier.width(96.dp), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(v, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
        w.paragraphs.forEach { Text(it, style = MaterialTheme.typography.bodyMedium) }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SourceFooter(sourceLabel: String, url: String, query: String) {
    val ctx = LocalContext.current
    Column(verticalArrangement = Arrangement.spacedBy(Sp.xs)) {
        FlowRow {
            TextButton(onClick = { open(ctx, url) }, modifier = Modifier.heightIn(min = 48.dp)) { Text("Kaynak sayfayı aç ↗") }
            TextButton(onClick = { open(ctx, nisanyan(query)) }, modifier = Modifier.heightIn(min = 48.dp)) { Text("Nişanyan Sözlük ↗") }
            TextButton(onClick = { open(ctx, "https://www.etymonline.com/search?q=" + Uri.encode(query)) }, modifier = Modifier.heightIn(min = 48.dp)) { Text("Etymonline ↗") }
        }
        Text(
            "Veriler $sourceLabel sayfasından canlı alınır (CC BY-SA 4.0).",
            style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = Sp.sm),
        )
    }
}

private fun nisanyan(q: String) = "https://www.nisanyansozluk.com/kelime/" + Uri.encode(q.lowercase(Locale("tr", "TR")))

private fun open(ctx: Context, url: String) {
    runCatching { ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) }
}
