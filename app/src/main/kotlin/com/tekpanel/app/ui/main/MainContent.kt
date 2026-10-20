package com.tekpanel.app.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tekpanel.app.domain.model.Conversation
import com.tekpanel.app.ui.access.AccessBanner
import com.tekpanel.app.ui.access.StalledBanner
import com.tekpanel.app.ui.theme.Tek

/** Every user action on the main screen, in one place, so the layout below stays a pure function of state. */
class MainCallbacks(
    val onTitleTap: () -> Unit = {},
    val onClear: () -> Unit = {},
    val onToggleMenu: () -> Unit = {},
    val onChannels: () -> Unit = {},
    val onExport: () -> Unit = {},
    val onPrivacy: () -> Unit = {},
    val onErase: () -> Unit = {},
    val onDiagnostics: () -> Unit = {},
    val onSelect: (String?) -> Unit = {},
    val onAdd: () -> Unit = {},
    val onOpenSettings: () -> Unit = {},
    val onHowTo: () -> Unit = {},
    val onRestartListener: () -> Unit = {},
    val onBatterySettings: () -> Unit = {},
    val onCycle: (Conversation) -> Unit = {},
    val onToggleMessage: (messageId: String, currentlyLong: Boolean) -> Unit = { _, _ -> },
    val onMarkRead: (List<String>) -> Unit = {},
    val onOpenApp: (Conversation) -> Unit = {},
    val onReply: (conversationKey: String, text: String) -> Unit = { _, _ -> },
    val onCopy: (String) -> Unit = {},
    val onPicture: (String) -> Unit = {},
)

/** Layout of the one screen. No view model, no side effects: screenshot tests render it with fake state. */
@Composable
fun MainContent(
    state: MainUiState,
    menuOpen: Boolean,
    diagnosticsUnlocked: Boolean,
    levelOf: (conversationKey: String) -> Level,
    isLong: (messageId: String, default: Boolean) -> Boolean,
    canReply: (conversationKey: String) -> Boolean,
    snackbarHostState: SnackbarHostState,
    callbacks: MainCallbacks,
) {
    val bottomInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val listState = rememberLazyListState()

    Scaffold(
        containerColor = Tek.colors.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(snackbarHostState, modifier = Modifier.padding(bottom = bottomInset)) },
    ) { inner ->
        Column(Modifier.fillMaxSize().padding(inner)) {
            TopBar(
                menuOpen = menuOpen,
                canClear = state.conversations.isNotEmpty(),
                onTitleTap = callbacks.onTitleTap,
                onClear = callbacks.onClear,
                onToggleMenu = callbacks.onToggleMenu,
            )
            MenuPanel(
                open = menuOpen,
                diagnosticsUnlocked = diagnosticsUnlocked,
                onChannels = callbacks.onChannels,
                onExport = callbacks.onExport,
                onPrivacy = callbacks.onPrivacy,
                onErase = callbacks.onErase,
                onDiagnostics = callbacks.onDiagnostics,
            )

            if (state.hasEnabledChannels) {
                ChannelBar(
                    items = state.bar,
                    selected = state.selectedPackage,
                    totalUnread = state.totalUnread,
                    onSelect = callbacks.onSelect,
                    onAdd = callbacks.onAdd,
                    modifier = Modifier.padding(vertical = 8.dp),
                )
            }

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = bottomInset + 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                if (!state.accessGranted) {
                    item(key = "access") {
                        AccessBanner(onOpenSettings = callbacks.onOpenSettings, onHowTo = callbacks.onHowTo)
                    }
                }
                if (state.listenerStalled) {
                    item(key = "stalled") {
                        StalledBanner(onRestart = callbacks.onRestartListener, onBatterySettings = callbacks.onBatterySettings)
                    }
                }
                if (state.loaded) {
                    when {
                        !state.hasEnabledChannels -> item(key = "empty-nochannel") {
                            NoChannelsChosen(onPick = callbacks.onChannels, modifier = Modifier.padding(top = 48.dp))
                        }
                        state.conversations.isEmpty() && state.accessGranted && state.selectedPackage == null -> item(key = "empty") {
                            EmptyInbox(modifier = Modifier.padding(top = 48.dp))
                        }
                        state.conversations.isEmpty() && state.accessGranted -> item(key = "empty-filter") {
                            EmptyForFilter(modifier = Modifier.padding(top = 48.dp))
                        }
                    }
                }
                items(state.conversations, key = { it.key }) { conversation ->
                    ConversationCard(
                        conversation = conversation,
                        appLabel = state.labels[conversation.sourcePackage] ?: "",
                        level = levelOf(conversation.key),
                        isLong = isLong,
                        canReply = canReply(conversation.key),
                        onCycle = { callbacks.onCycle(conversation) },
                        onToggleMessage = callbacks.onToggleMessage,
                        onMarkRead = callbacks.onMarkRead,
                        onOpenApp = { callbacks.onOpenApp(conversation) },
                        onReply = { text -> callbacks.onReply(conversation.key, text) },
                        onCopy = callbacks.onCopy,
                        onPicture = callbacks.onPicture,
                        modifier = Modifier.animateItem(),
                    )
                }
            }
        }
    }
}
