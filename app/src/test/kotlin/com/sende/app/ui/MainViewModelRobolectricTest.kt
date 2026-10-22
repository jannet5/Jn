package com.sende.app.ui

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.sende.app.capture.LiveActionRegistry
import com.sende.app.data.apps.AppInventory
import com.sende.app.data.apps.InstalledApp
import com.sende.app.data.local.SendeDatabase
import com.sende.app.data.media.MediaStore
import com.sende.app.data.prefs.AppPreferences
import com.sende.app.data.repository.ChannelRepository
import com.sende.app.data.repository.InboxRepository
import com.sende.app.domain.model.CaptureMethod
import com.sende.app.domain.model.InboxMessage
import com.sende.app.domain.model.MessageState
import com.sende.app.domain.usecase.EraseAllDataUseCase
import com.sende.app.capture.DuplicateGuard
import com.sende.app.diagnostics.DiagnosticsRecorder
import com.sende.app.export.DataExporter
import com.sende.app.ui.main.Level
import com.sende.app.ui.main.MainViewModel
import com.sende.app.util.IntentLauncher
import com.sende.app.util.NotificationAccessChecker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], application = android.app.Application::class)
class MainViewModelRobolectricTest {

    private val context = ApplicationProvider.getApplicationContext<android.content.Context>()
    private lateinit var db: SendeDatabase
    private lateinit var inbox: InboxRepository
    private lateinit var prefs: AppPreferences
    private lateinit var viewModel: MainViewModel

    private class FakeInventory(context: android.content.Context) : AppInventory(context) {
        private val apps = listOf(InstalledApp("com.whatsapp", "WhatsApp"), InstalledApp("com.instagram.android", "Instagram"))
        override suspend fun refresh(): List<InstalledApp> = apps
    }

    @Before
    fun setUp() = runBlocking {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        prefs = AppPreferences(context).also { it.clearAll() }
        db = Room.inMemoryDatabaseBuilder(context, SendeDatabase::class.java).allowMainThreadQueries().build()
        inbox = InboxRepository(db.inboxMessageDao(), MediaStore(context))
        val channels = ChannelRepository(FakeInventory(context), prefs, inbox)
        val registry = LiveActionRegistry()
        viewModel = MainViewModel(
            inboxRepository = inbox,
            channelRepository = channels,
            accessChecker = NotificationAccessChecker(context),
            intentLauncher = IntentLauncher(context, registry),
            preferences = prefs,
            exporter = DataExporter(inbox),
            eraseAll = EraseAllDataUseCase(inbox, prefs, DuplicateGuard(), registry, DiagnosticsRecorder()),
            listenerHealthRestart = {},
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun msg(id: String, pkg: String = "com.whatsapp", chat: String = "ayse", at: Long = 1_000) = InboxMessage(
        id = id, notificationKey = "k", fingerprint = "f-$id", sourcePackage = pkg, conversationKey = "$pkg|$chat",
        conversationTitle = chat, isGroupConversation = false, senderName = chat, messageText = "t$id",
        receivedAt = Instant.ofEpochMilli(at), capturedAt = Instant.ofEpochMilli(at), captureMethod = CaptureMethod.NOTIFICATION,
        state = MessageState.UNREAD, avatarPath = null, picturePath = null,
    )

    /** Subscribes (the state flow is WhileSubscribed) and returns the next state that satisfies [until]. */
    private suspend fun awaitState(until: (com.sende.app.ui.main.MainUiState) -> Boolean) = viewModel.state.first(until)

    @Test
    fun `first launch preselects the usual chat apps and shows their messages grouped`() = runTest(timeout = 60.seconds) {
        inbox.insertIfNew(msg("1"))
        inbox.insertIfNew(msg("2", at = 2_000))
        inbox.insertIfNew(msg("3", chat = "mehmet", at = 3_000))

        val state = awaitState { it.conversations.size == 2 }

        assertThat(state.bar.map { it.label }).contains("WhatsApp")
        assertThat(state.conversations.first().title).isEqualTo("mehmet")
        assertThat(state.totalUnread).isEqualTo(3)
    }

    @Test
    fun `selecting a bubble filters to that app and an unselected app is not on the bar`() = runTest(timeout = 60.seconds) {
        inbox.insertIfNew(msg("1"))
        inbox.insertIfNew(msg("2", pkg = "com.instagram.android", chat = "x", at = 2_000))
        awaitState { it.conversations.size == 2 }

        viewModel.select("com.instagram.android")

        val state = awaitState { it.selectedPackage == "com.instagram.android" }
        assertThat(state.conversations.map { it.sourcePackage }).containsExactly("com.instagram.android")
    }

    @Test
    fun `a card of several messages steps closed to messages to full to closed`() = runTest(timeout = 60.seconds) {
        inbox.insertIfNew(msg("1"))
        inbox.insertIfNew(msg("2", at = 2_000))
        val conversation = awaitState { it.conversations.isNotEmpty() }.conversations.single()

        assertThat(viewModel.levels[conversation.key]).isNull()
        viewModel.cycle(conversation)
        assertThat(viewModel.levels[conversation.key]).isEqualTo(Level.Children)
        viewModel.cycle(conversation)
        assertThat(viewModel.levels[conversation.key]).isEqualTo(Level.Full)
        viewModel.cycle(conversation)
        assertThat(viewModel.levels[conversation.key]).isNull()
    }

    @Test
    fun `a single message card opens straight to full and closes again`() = runTest(timeout = 60.seconds) {
        inbox.insertIfNew(msg("1"))
        val conversation = awaitState { it.conversations.isNotEmpty() }.conversations.single()

        viewModel.cycle(conversation)
        assertThat(viewModel.levels[conversation.key]).isEqualTo(Level.Full)
        viewModel.cycle(conversation)
        assertThat(viewModel.levels[conversation.key]).isNull()
    }

    @Test
    fun `tapping one message flips only that message and a level change resets the overrides`() = runTest(timeout = 60.seconds) {
        inbox.insertIfNew(msg("1"))
        inbox.insertIfNew(msg("2", at = 2_000))
        val conversation = awaitState { it.conversations.isNotEmpty() }.conversations.single()

        viewModel.cycle(conversation) // Children
        viewModel.toggleLengthened("1", currentlyLong = false)
        assertThat(viewModel.lengthened["1"]).isTrue()
        assertThat(viewModel.lengthened["2"]).isNull()

        viewModel.cycle(conversation) // Full: overrides cleared so every message opens uniformly
        assertThat(viewModel.lengthened["1"]).isNull()
    }

    @Test
    fun `back closes open cards first and reports when there was nothing to close`() = runTest(timeout = 60.seconds) {
        inbox.insertIfNew(msg("1"))
        val conversation = awaitState { it.conversations.isNotEmpty() }.conversations.single()
        viewModel.cycle(conversation)

        assertThat(viewModel.collapseAll()).isTrue()
        assertThat(viewModel.collapseAll()).isFalse() // now back may send the app to the background
    }

    @Test
    fun `marking read removes the message and undo brings it back`() = runTest(timeout = 60.seconds) {
        inbox.insertIfNew(msg("1"))
        inbox.insertIfNew(msg("2", at = 2_000))
        awaitState { it.totalUnread == 2 }

        viewModel.markRead(listOf("1"), "label")
        awaitState { it.totalUnread == 1 }
        assertThat(viewModel.undo.value?.label).isEqualTo("label")

        viewModel.undo()
        awaitState { it.totalUnread == 2 }
    }

    @Test
    fun `cleaning the screen respects the selected app and can be undone`() = runTest(timeout = 60.seconds) {
        inbox.insertIfNew(msg("1"))
        inbox.insertIfNew(msg("2", pkg = "com.instagram.android", chat = "x", at = 2_000))
        awaitState { it.totalUnread == 2 }
        viewModel.select("com.instagram.android")
        awaitState { it.selectedPackage == "com.instagram.android" }

        viewModel.clearScreen("temizlendi")

        assertThat(awaitState { it.totalUnread == 1 }.bar.first { it.packageName == "com.whatsapp" }.unread).isEqualTo(1)
        viewModel.undo()
        awaitState { it.totalUnread == 2 }
    }

    @Test
    fun `cleaning the screen leaves the messages of switched-off channels alone`() = runTest(timeout = 60.seconds) {
        inbox.insertIfNew(msg("1"))
        inbox.insertIfNew(msg("2", pkg = "com.instagram.android", chat = "x", at = 2_000))
        awaitState { it.totalUnread == 2 }
        prefs.setChannelEnabled("com.instagram.android", false)
        awaitState { state -> state.bar.none { it.packageName == "com.instagram.android" } }

        viewModel.clearScreen("temizlendi")
        awaitState { it.totalUnread == 0 }

        prefs.setChannelEnabled("com.instagram.android", true)
        assertThat(awaitState { it.totalUnread == 1 }.conversations.single().sourcePackage).isEqualTo("com.instagram.android")
    }

    @Test
    fun `text shared into Sende is shown although it belongs to no channel`() = runTest(timeout = 60.seconds) {
        inbox.insertIfNew(msg("1", pkg = com.sende.app.capture.CaptureCoordinator.SHARE_FALLBACK_PACKAGE, chat = "Paylasilan").copy(captureMethod = CaptureMethod.SHARE))

        val state = awaitState { it.conversations.isNotEmpty() }

        assertThat(state.conversations.single().sourcePackage).isEqualTo(com.sende.app.capture.CaptureCoordinator.SHARE_FALLBACK_PACKAGE)
    }

    @Test
    fun `a card whose messages were all read forgets it was open`() = runTest(timeout = 60.seconds) {
        inbox.insertIfNew(msg("1"))
        val conversation = awaitState { it.conversations.isNotEmpty() }.conversations.single()
        viewModel.cycle(conversation)

        viewModel.markRead(listOf("1"), "label")
        awaitState { it.totalUnread == 0 }

        assertThat(viewModel.levels[conversation.key]).isNull()
        assertThat(viewModel.collapseAll()).isFalse() // back must not be swallowed by a card that no longer exists
    }

    @Test
    fun `a new message in a chat that was fully read arrives closed`() = runTest(timeout = 60.seconds) {
        inbox.insertIfNew(msg("1"))
        val conversation = awaitState { it.conversations.isNotEmpty() }.conversations.single()
        viewModel.cycle(conversation)
        viewModel.markRead(listOf("1"), "label")
        awaitState { it.totalUnread == 0 }

        inbox.insertIfNew(msg("2", at = 5_000))
        awaitState { it.totalUnread == 1 }

        assertThat(viewModel.levels[conversation.key]).isNull()
    }

    @Test
    fun `not now on the access card is remembered and can be taken back`() = runTest(timeout = 60.seconds) {
        assertThat(awaitState { it.loaded }.accessHintDismissed).isFalse()

        viewModel.setAccessHintDismissed(true)
        assertThat(awaitState { it.accessHintDismissed }.accessHintDismissed).isTrue()

        viewModel.setAccessHintDismissed(false)
        assertThat(awaitState { !it.accessHintDismissed }.accessHintDismissed).isFalse()
    }
}
