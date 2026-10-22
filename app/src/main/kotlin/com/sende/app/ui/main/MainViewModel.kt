package com.sende.app.ui.main

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sende.app.capture.CaptureCoordinator
import com.sende.app.data.prefs.AppPreferences
import com.sende.app.data.repository.ChannelRepository
import com.sende.app.data.repository.InboxRepository
import com.sende.app.domain.logic.ChannelOrdering
import com.sende.app.domain.model.Conversation
import com.sende.app.domain.usecase.EraseAllDataUseCase
import com.sende.app.export.DataExporter
import com.sende.app.util.IntentLauncher
import com.sende.app.util.NotificationAccessChecker
import com.sende.app.util.OpenSourceResult
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
    private val listenerHealthRestart: () -> Unit,
) : ViewModel() {

    private val accessGranted = MutableStateFlow(accessChecker.isGranted())
    private val listenerStalled = MutableStateFlow(false)
    private var disconnectedSince = 0L
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
        listenerStalled,
    ) { granted, channels, conversations, selected, stalled ->
        val enabled = channels.filter { it.enabled }
        val bar = ChannelOrdering.barOrder(enabled).map { BarItem(it.packageName, it.label, it.unreadMessages) }
        val effectiveSelection = selected?.takeIf { sel -> bar.any { it.packageName == sel } }
        val labels = channels.associate { it.packageName to it.label }
        val enabledPackages = enabled.map { it.packageName }.toSet()
        val visibleConversations = conversations.filter { conversation ->
            // Text the user shared into Sende has no channel to switch on: it is always shown.
            (conversation.sourcePackage in enabledPackages || conversation.sourcePackage == CaptureCoordinator.SHARE_FALLBACK_PACKAGE) &&
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
            listenerStalled = granted && stalled,
        )
    }.flowOn(Dispatchers.Default).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), MainUiState())

    init {
        viewModelScope.launch { channelRepository.refresh() }
        viewModelScope.launch { inboxRepository.prune() }
    }

    fun refreshAccess() {
        accessGranted.value = accessChecker.isGranted()
        // Right after a launch the system needs a few seconds to bind; only call it stalled after a grace period.
        if (accessChecker.isListenerBound()) {
            disconnectedSince = 0L
            listenerStalled.value = false
        } else {
            val now = System.currentTimeMillis()
            if (disconnectedSince == 0L) disconnectedSince = now
            listenerStalled.value = now - disconnectedSince > STALL_GRACE_MS
        }
    }

    fun restartListener() {
        disconnectedSince = System.currentTimeMillis()
        listenerStalled.value = false
        listenerHealthRestart()
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
        // Entries of cards or messages that no longer exist must not make the back button do nothing.
        val shown = state.value.conversations
        levels.keys.retainAll(shown.map { it.key }.toSet())
        lengthened.keys.retainAll(shown.flatMap { c -> c.messages.map { it.id } }.toSet())
        if (levels.isEmpty() && lengthened.isEmpty()) return false
        levels.clear()
        lengthened.clear()
        return true
    }

    /** Messages that were handled take their open/closed state with them; a card whose messages all went closes. */
    private fun forgetOpenState(messageIds: Collection<String>) {
        val gone = messageIds.toSet()
        gone.forEach { lengthened.remove(it) }
        state.value.conversations.filter { c -> c.messages.all { it.id in gone } }.forEach { levels.remove(it.key) }
    }

    // ---- handling messages -------------------------------------------------------------

    fun markRead(ids: List<String>, label: String) {
        if (ids.isEmpty()) return
        forgetOpenState(ids)
        viewModelScope.launch {
            val previous = ids.associateWith { "UNREAD" }
            inboxRepository.markRead(ids)
            _undo.value = UndoRequest(previous, label)
        }
    }

    /** Hides exactly the messages on screen (the selected channel, or everything shown), nothing else. */
    fun clearScreen(label: String) {
        val ids = state.value.conversations.flatMap { c -> c.messages.map { it.id } }
        if (ids.isEmpty()) return
        forgetOpenState(ids)
        viewModelScope.launch {
            val previous = inboxRepository.hideMessages(ids)
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

    /** Labels of the source app's own buttons for this chat (e.g. "Mark as read"), while its notification is alive. */
    fun actionTitles(conversationKey: String): List<String> = intentLauncher.actionTitles(conversationKey)

    fun fireAction(conversationKey: String, index: Int) {
        if (!intentLauncher.fireAction(conversationKey, index)) eventChannel.trySend(UiEvent.OpenFailed)
    }

    /** @return true if the source app accepted the reply; the caller keeps the draft when it did not. */
    fun reply(conversationKey: String, text: String): Boolean {
        val sent = text.isNotBlank() && intentLauncher.reply(conversationKey, text.trim())
        eventChannel.trySend(if (sent) UiEvent.ReplySent else UiEvent.ReplyFailed)
        return sent
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

    private companion object {
        const val STALL_GRACE_MS = 10_000L
    }
}
