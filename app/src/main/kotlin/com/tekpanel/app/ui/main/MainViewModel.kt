package com.tekpanel.app.ui.main

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tekpanel.app.data.prefs.AppPreferences
import com.tekpanel.app.data.repository.ChannelRepository
import com.tekpanel.app.data.repository.InboxRepository
import com.tekpanel.app.domain.logic.ChannelOrdering
import com.tekpanel.app.domain.model.Conversation
import com.tekpanel.app.domain.usecase.EraseAllDataUseCase
import com.tekpanel.app.export.DataExporter
import com.tekpanel.app.util.IntentLauncher
import com.tekpanel.app.util.NotificationAccessChecker
import com.tekpanel.app.util.OpenSourceResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val inboxRepository: InboxRepository,
    private val channelRepository: ChannelRepository,
    private val accessChecker: NotificationAccessChecker,
    private val intentLauncher: IntentLauncher,
    private val preferences: AppPreferences,
    private val exporter: DataExporter,
    private val eraseAll: EraseAllDataUseCase,
) : ViewModel() {

    private val accessGranted = MutableStateFlow(accessChecker.isGranted())
    private val selectedPackage = MutableStateFlow<String?>(null)

    /** Per-card open level and per-message "lengthened" flags; snapshot maps so only the touched card recomposes. */
    val levels = mutableStateMapOf<String, Level>()
    val lengthened = mutableStateMapOf<String, Boolean>()

    private val _undo = MutableStateFlow<UndoRequest?>(null)
    val undo: StateFlow<UndoRequest?> = _undo

    private val eventChannel = Channel<UiEvent>(Channel.BUFFERED)
    val events = eventChannel.receiveAsFlow()

    val diagnosticsUnlocked: StateFlow<Boolean> = preferences.diagnosticsUnlocked
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    val state: StateFlow<MainUiState> = combine(
        accessGranted,
        channelRepository.observeAllChannels(),
        inboxRepository.observeConversations(),
        selectedPackage,
    ) { granted, channels, conversations, selected ->
        val enabled = channels.filter { it.enabled }
        val bar = ChannelOrdering.barOrder(enabled).map { BarItem(it.packageName, it.label, it.unreadMessages) }
        val effectiveSelection = selected?.takeIf { sel -> bar.any { it.packageName == sel } }
        val labels = channels.associate { it.packageName to it.label }
        val visibleConversations = conversations.filter { conversation ->
            conversation.sourcePackage in enabled.map { it.packageName }.toSet() &&
                (effectiveSelection == null || conversation.sourcePackage == effectiveSelection)
        }
        MainUiState(
            loaded = true,
            accessGranted = granted,
            bar = bar,
            selectedPackage = effectiveSelection,
            totalUnread = bar.sumOf { it.unread },
            conversations = visibleConversations,
            labels = labels,
            hasEnabledChannels = enabled.isNotEmpty(),
        )
    }.flowOn(Dispatchers.Default).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), MainUiState())

    init {
        viewModelScope.launch { channelRepository.refresh() }
        viewModelScope.launch { inboxRepository.prune() }
    }

    fun refreshAccess() {
        accessGranted.value = accessChecker.isGranted()
    }

    fun refreshInstalledApps() {
        viewModelScope.launch { channelRepository.refresh() }
    }

    fun select(packageName: String?) {
        selectedPackage.value = packageName
    }

    // ---- open / close ------------------------------------------------------------------

    /** Tapping a card header: single message toggles open/closed, a chat steps Collapsed -> Children -> Full -> Collapsed. */
    fun cycle(conversation: Conversation) {
        val current = levels[conversation.key] ?: Level.Collapsed
        val next = if (conversation.messages.size <= 1) {
            if (current == Level.Collapsed) Level.Full else Level.Collapsed
        } else {
            when (current) {
                Level.Collapsed -> Level.Children
                Level.Children -> Level.Full
                Level.Full -> Level.Collapsed
            }
        }
        // A level change resets the per-message overrides so the card always opens uniformly.
        conversation.messages.forEach { lengthened.remove(it.id) }
        if (next == Level.Collapsed) levels.remove(conversation.key) else levels[conversation.key] = next
    }

    /** Tapping one message: flips it between short and full, whatever the card's level currently shows. */
    fun toggleLengthened(messageId: String, currentlyLong: Boolean) {
        lengthened[messageId] = !currentlyLong
    }

    /** Back-button step: closes whatever is open. @return true if something was closed. */
    fun collapseAll(): Boolean {
        if (levels.isEmpty() && lengthened.isEmpty()) return false
        levels.clear()
        lengthened.clear()
        return true
    }

    // ---- handling messages -------------------------------------------------------------

    fun markRead(ids: List<String>, label: String) {
        if (ids.isEmpty()) return
        viewModelScope.launch {
            val previous = ids.associateWith { "UNREAD" }
            inboxRepository.markRead(ids)
            _undo.value = UndoRequest(previous, label)
        }
    }

    fun clearScreen(label: String) {
        viewModelScope.launch {
            val previous = inboxRepository.clearUnread(selectedPackage.value?.let { listOf(it) })
            if (previous.isNotEmpty()) _undo.value = UndoRequest(previous, label)
        }
    }

    fun undo() {
        val request = _undo.value ?: return
        viewModelScope.launch {
            inboxRepository.restoreStates(request.previousStates)
            _undo.value = null
        }
    }

    fun dismissUndo() {
        _undo.value = null
    }

    // ---- talking to the source app -----------------------------------------------------

    fun openConversation(conversation: Conversation) {
        val result = intentLauncher.openConversation(conversation.key, conversation.sourcePackage)
        if (result == OpenSourceResult.FAILED) eventChannel.trySend(UiEvent.OpenFailed)
    }

    fun canReply(conversationKey: String): Boolean = intentLauncher.canReply(conversationKey)

    fun reply(conversationKey: String, text: String) {
        val sent = text.isNotBlank() && intentLauncher.reply(conversationKey, text.trim())
        eventChannel.trySend(if (sent) UiEvent.ReplySent else UiEvent.ReplyFailed)
    }

    fun onCopied() {
        eventChannel.trySend(UiEvent.Copied)
    }

    // ---- menu --------------------------------------------------------------------------

    fun requestExport() {
        viewModelScope.launch { eventChannel.trySend(UiEvent.ExportReady(exporter.exportAsJson())) }
    }

    fun eraseEverything() {
        viewModelScope.launch {
            eraseAll()
            channelRepository.refresh()
        }
    }

    fun unlockDiagnostics() {
        viewModelScope.launch { preferences.setDiagnosticsUnlocked(true) }
    }
}
