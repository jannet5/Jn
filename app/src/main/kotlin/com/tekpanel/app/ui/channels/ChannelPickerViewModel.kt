package com.tekpanel.app.ui.channels

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tekpanel.app.data.repository.ChannelRepository
import com.tekpanel.app.domain.logic.ChannelOrdering
import com.tekpanel.app.domain.model.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

@Immutable
data class PickerUiState(
    val items: List<Channel> = emptyList(),
    val query: String = "",
    val selectedCount: Int = 0,
    val allVisibleSelected: Boolean = false,
    val loading: Boolean = true,
)

/**
 * Drives the one-purpose "pick your channels" sheet. Ordering rule: the base order (chosen apps,
 * then apps with history, then known chat apps, then A-Z) is frozen when the sheet opens, and
 * every app the user touches floats to the top and stays there, even if it is switched off again.
 */
class ChannelPickerViewModel(private val channelRepository: ChannelRepository) : ViewModel() {

    private val query = MutableStateFlow("")
    private val baseOrder = MutableStateFlow<List<String>>(emptyList())
    private val touchedNewestFirst = MutableStateFlow<List<String>>(emptyList())

    private val turkish = Locale("tr", "TR")

    val state: StateFlow<PickerUiState> = combine(
        channelRepository.observeAllChannels(), baseOrder, touchedNewestFirst, query,
    ) { channels, base, touched, q ->
        val byPackage = channels.associateBy { it.packageName }
        val known = base.filter { it in byPackage }
        val newcomers = ChannelOrdering.pickerBaseOrder(channels).map { it.packageName }.filter { it !in known }
        val order = ChannelOrdering.withTouchedOnTop(known + newcomers, touched)
        val needle = q.trim().lowercase(turkish)
        val visible = order.mapNotNull { byPackage[it] }
            .filter { needle.isEmpty() || it.label.lowercase(turkish).contains(needle) }
        PickerUiState(
            items = visible,
            query = q,
            selectedCount = channels.count { it.enabled },
            allVisibleSelected = visible.isNotEmpty() && visible.all { it.enabled },
            loading = false,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), PickerUiState())

    /** Called every time the sheet opens: refreshes the installed apps and re-freezes the base order. */
    fun onOpen() {
        query.value = ""
        touchedNewestFirst.value = emptyList()
        viewModelScope.launch {
            channelRepository.refresh()
            baseOrder.value = ChannelOrdering.pickerBaseOrder(currentChannels()).map { it.packageName }
        }
    }

    private suspend fun currentChannels(): List<Channel> = channelRepository.observeAllChannels().first()

    fun setQuery(value: String) {
        query.value = value
    }

    fun toggle(channel: Channel) {
        touchedNewestFirst.value = listOf(channel.packageName) + touchedNewestFirst.value.filter { it != channel.packageName }
        viewModelScope.launch { channelRepository.setEnabled(channel.packageName, !channel.enabled) }
    }

    fun setAllVisible(enabled: Boolean) {
        val visible = state.value.items.map { it.packageName }
        viewModelScope.launch { channelRepository.setAllEnabled(visible, enabled) }
    }
}
