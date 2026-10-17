package com.jn.koken

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jn.koken.data.Lookup
import com.jn.koken.data.LookupResult
import com.jn.koken.data.Outcome
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/** Ekranın alabileceği durumlar: boş, yükleniyor, sonuç, bulunamadı, hata */
sealed interface UiState {
    data object Idle : UiState
    data class Loading(val query: String) : UiState
    data class Result(val data: LookupResult) : UiState
    data class NotFound(val query: String) : UiState
    data class Error(val query: String, val rateLimited: Boolean) : UiState
}

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val lookup = Lookup()
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
            state = when (val o = lookup.search(q)) {
                is Outcome.Found -> { remember(q); UiState.Result(o.result) }
                is Outcome.NotFound -> UiState.NotFound(o.query)
                is Outcome.Failed -> UiState.Error(o.query, o.rateLimited)
            }
        }
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
