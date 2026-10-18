package com.jn.koken

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jn.koken.data.DeepInfo
import com.jn.koken.data.Lookup
import com.jn.koken.data.LookupResult
import com.jn.koken.data.Narrator
import com.jn.koken.data.Outcome
import com.jn.koken.data.Translator
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/** Ekranın alabileceği durumlar: boş, yükleniyor, sonuç, bulunamadı, hata */
sealed interface UiState {
    data object Idle : UiState
    data class Loading(val query: String) : UiState
    /** [busy]: kaynak sözcük sayfası ve çeviri hâlâ geliyor (anlatım geldikçe zenginleşir) */
    data class Result(
        val data: LookupResult,
        val deep: DeepInfo?,
        val translations: Map<String, String>,
        val busy: Boolean,
    ) : UiState
    data class NotFound(val query: String) : UiState
    data class Error(val query: String, val rateLimited: Boolean) : UiState
}

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val lookup = Lookup()
    private val translator = Translator()
    private val prefs = app.getSharedPreferences("koken", Context.MODE_PRIVATE)
    private var job: Job? = null

    var query by mutableStateOf("")
    var state by mutableStateOf<UiState>(UiState.Idle)
        private set
    var history by mutableStateOf(loadHistory())
        private set

    /** Yazılan (ya da seçilen) sözcüğü ara */
    fun submit(text: String = query) {
        val q = Lookup.normalize(text)
        if (q.isEmpty()) return
        query = q
        job?.cancel()
        state = UiState.Loading(q)
        job = viewModelScope.launch {
            when (val o = lookup.search(q)) {
                is Outcome.Found -> { remember(q); enrich(o.result) }
                is Outcome.NotFound -> state = UiState.NotFound(o.query)
                is Outcome.Failed -> state = UiState.Error(o.query, o.rateLimited)
            }
        }
    }

    /** Önce eldekini göster, sonra kaynak sözcüğün sayfasını ve çevirileri ekleyerek anlatımı zenginleştir */
    private suspend fun enrich(r: LookupResult) {
        state = UiState.Result(r, null, emptyMap(), busy = true)
        val deep = runCatching { lookup.deepen(r) }.getOrNull()
        state = UiState.Result(r, deep, emptyMap(), busy = true)
        val english = Narrator.englishStrings(r, deep)
        val tr = if (english.isEmpty()) emptyMap() else runCatching { translator.translate(english) }.getOrDefault(emptyMap())
        state = UiState.Result(r, deep, tr, busy = false)
    }

    fun clear() {
        job?.cancel()
        query = ""
        state = UiState.Idle
    }

    // Son aramalar: en yeni başta, tekrarsız, en çok 10
    private fun remember(q: String) {
        val list = (listOf(q) + history.filterNot { it.equals(q, ignoreCase = true) }).take(10)
        history = list
        prefs.edit().putString("history", list.joinToString("\n")).apply()
    }

    private fun loadHistory(): List<String> =
        prefs.getString("history", "").orEmpty().split("\n").filter { it.isNotBlank() }
}
