package com.tekpanel.app.ui.inbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tekpanel.app.data.repository.ChannelRepository
import com.tekpanel.app.data.repository.InboxRepository
import com.tekpanel.app.domain.model.InboxMessage
import com.tekpanel.app.domain.model.MessageState
import com.tekpanel.app.util.IntentLauncher
import com.tekpanel.app.util.NotificationAccessChecker
import com.tekpanel.app.util.OpenSourceResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InboxViewModel(
    private val inboxRepository: InboxRepository,
    private val channelRepository: ChannelRepository,
    private val notificationAccessChecker: NotificationAccessChecker,
    private val intentLauncher: IntentLauncher,
) : ViewModel() {

    private val notificationAccessGranted = MutableStateFlow(notificationAccessChecker.isGranted())
    private val selectedChannelId = MutableStateFlow<String?>(null)

    private val _pendingUndo = MutableStateFlow<PendingClearUndo?>(null)
    val pendingUndo: StateFlow<PendingClearUndo?> = _pendingUndo

    private val _openSourceFailure = MutableStateFlow(0)
    val openSourceFailureTick: StateFlow<Int> = _openSourceFailure

    val screenState: StateFlow<InboxScreenState> = combine(
        notificationAccessGranted,
        channelRepository.observeInstalledChannels(),
        inboxRepository.observeActive(),
        selectedChannelId,
    ) { accessGranted, channels, messages, selected ->
        toScreenState(accessGranted, channels, messages, selected)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), InboxScreenState.Loading)

    private fun toScreenState(
        accessGranted: Boolean,
        channels: List<com.tekpanel.app.data.repository.ChannelUiEntry>,
        messages: List<InboxMessage>,
        selectedChannelId: String?,
    ): InboxScreenState {
        if (!accessGranted) return InboxScreenState.NotificationAccessMissing
        if (channels.isEmpty()) return InboxScreenState.NoChannelsInstalled

        // "Tümü" shows every active message, including ones captured before their channel was
        // last disabled (CAP-03: disabling a channel stops new capture, it never auto-hides
        // history). Selecting a specific pill narrows to just that channel.
        val enabledChannelIds = channels.filter { it.enabled }.map { it.app.id }.toSet()
        val filtered = if (selectedChannelId != null) {
            messages.filter { it.channelId == selectedChannelId }
        } else {
            messages
        }

        return InboxScreenState.Content(
            channels = channels,
            selectedChannelId = selectedChannelId,
            allChannelsSelectedButNoneEnabled = selectedChannelId == null && enabledChannelIds.isEmpty(),
            messages = filtered,
        )
    }

    fun refreshNotificationAccess() {
        notificationAccessGranted.value = notificationAccessChecker.isGranted()
    }

    fun selectChannel(channelId: String?) {
        selectedChannelId.value = channelId
    }

    fun markRead(message: InboxMessage) {
        viewModelScope.launch { inboxRepository.markState(message.id, MessageState.READ) }
    }

    fun clearActive(label: String) {
        viewModelScope.launch {
            val current = (screenState.value as? InboxScreenState.Content)?.messages ?: return@launch
            val previousStates = inboxRepository.clearActive(current)
            _pendingUndo.value = PendingClearUndo(previousStates, label)
        }
    }

    fun undoClear() {
        val undo = _pendingUndo.value ?: return
        viewModelScope.launch {
            inboxRepository.undoClear(undo.previousStates)
            _pendingUndo.value = null
        }
    }

    fun dismissUndo() {
        _pendingUndo.value = null
    }

    fun openSource(message: InboxMessage) {
        val result = intentLauncher.openSource(message.id, message.sourcePackage)
        if (result == OpenSourceResult.FAILED) _openSourceFailure.value += 1
    }
}
