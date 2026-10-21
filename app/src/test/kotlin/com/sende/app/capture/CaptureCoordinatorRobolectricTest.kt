package com.sende.app.capture

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.sende.app.data.local.SendeDatabase
import com.sende.app.data.media.MediaStore
import com.sende.app.data.prefs.AppPreferences
import com.sende.app.data.repository.InboxRepository
import com.sende.app.diagnostics.DiagnosticsRecorder
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlin.time.Duration.Companion.seconds
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * End-to-end test of the whole capture pipeline against a real in-memory Room database, a real
 * DataStore-backed [AppPreferences] and real `Notification` objects: the closest this sandbox gets
 * to "does a chat-shaped notification really end up in the inbox exactly once".
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], application = android.app.Application::class)
class CaptureCoordinatorRobolectricTest {

    private val context = ApplicationProvider.getApplicationContext<android.content.Context>()
    private lateinit var db: SendeDatabase
    private lateinit var repository: InboxRepository
    private lateinit var preferences: AppPreferences
    private lateinit var mediaStore: MediaStore

    @Before
    fun setUp() = runBlocking {
        preferences = AppPreferences(context)
        preferences.clearAll()
        db = Room.inMemoryDatabaseBuilder(context, SendeDatabase::class.java).allowMainThreadQueries().build()
        mediaStore = MediaStore(context)
        mediaStore.deleteAll()
        repository = InboxRepository(db.inboxMessageDao(), mediaStore)
        preferences.setChannelEnabled("com.whatsapp", true)
    }

    /** A fresh coordinator with an empty in-memory guard, like after a process restart. */
    private fun coordinator() = CaptureCoordinator(
        filterEngine = MessageFilterEngine(ownPackage = "com.sende.app"),
        duplicateGuard = DuplicateGuard(),
        inboxRepository = repository,
        appPreferences = preferences,
        diagnosticsRecorder = DiagnosticsRecorder(),
        liveActionRegistry = LiveActionRegistry(),
        mediaStore = mediaStore,
    )

    private suspend fun CaptureCoordinator.feed(sbn: android.service.notification.StatusBarNotification) =
        handleNotification(NotificationExtractor.extract(context, sbn), LiveActionRegistry.fromNotification(sbn.notification))

    @Test
    fun `a real chat-shaped notification lands in the inbox`() = runTest(timeout = 60.seconds) {
        val stored = coordinator().feed(FakeSbn.plain(context, "com.whatsapp", "Ayşe Yılmaz", "Merhaba, siparişim ne zaman gelir?"))

        assertThat(stored).hasSize(1)
        assertThat(repository.countAll()).isEqualTo(1)
    }

    @Test
    fun `re-processing the same notification does not duplicate it`() = runTest(timeout = 60.seconds) {
        val coordinator = coordinator()
        val sbn = FakeSbn.plain(context, "com.whatsapp", "Ayşe Yılmaz", "Merhaba, siparişim ne zaman gelir?")

        repeat(3) { coordinator.feed(sbn) }

        assertThat(repository.countAll()).isEqualTo(1)
    }

    @Test
    fun `REGRESSION - the same notification key carrying a new message stores the new message`() = runTest(timeout = 60.seconds) {
        // Chat apps keep ONE notification per chat (same key) and update it for every message.
        val coordinator = coordinator()
        coordinator.feed(FakeSbn.chat(context, "com.whatsapp", "Ayşe Yılmaz", listOf("İlk mesaj" to 1_000L), id = 42))
        coordinator.feed(FakeSbn.chat(context, "com.whatsapp", "Ayşe Yılmaz", listOf("İlk mesaj" to 1_000L, "İkinci mesaj" to 2_000L), id = 42))
        coordinator.feed(FakeSbn.chat(context, "com.whatsapp", "Ayşe Yılmaz", listOf("İlk mesaj" to 1_000L, "İkinci mesaj" to 2_000L, "Üçüncü mesaj" to 3_000L), id = 42))

        val conversations = repository.observeConversations().first()
        assertThat(conversations).hasSize(1)
        assertThat(conversations.single().messages.map { it.messageText }).containsExactly("İlk mesaj", "İkinci mesaj", "Üçüncü mesaj").inOrder()
    }

    @Test
    fun `a burst of messages in one notification is stored once each, even after a process restart`() = runTest(timeout = 60.seconds) {
        val sbn = FakeSbn.chat(context, "com.whatsapp", "Ayşe Yılmaz", listOf("bir" to 1_000L, "iki" to 2_000L, "üç" to 3_000L))

        coordinator().feed(sbn)
        coordinator().feed(sbn) // brand-new in-memory guard: only the Room unique index can stop duplicates now

        assertThat(repository.countAll()).isEqualTo(3)
    }

    @Test
    fun `the same words sent twice at different times are two messages`() = runTest(timeout = 60.seconds) {
        val coordinator = coordinator()
        coordinator.feed(FakeSbn.chat(context, "com.whatsapp", "Ayşe", listOf("Tamam" to 1_000L), id = 1))
        coordinator.feed(FakeSbn.chat(context, "com.whatsapp", "Ayşe", listOf("Tamam" to 1_000L, "Tamam" to 9_000L), id = 1))

        assertThat(repository.countAll()).isEqualTo(2)
    }

    @Test
    fun `messages of different people become different conversations`() = runTest(timeout = 60.seconds) {
        val coordinator = coordinator()
        coordinator.feed(FakeSbn.plain(context, "com.whatsapp", "Ayşe", "selam", id = 1))
        coordinator.feed(FakeSbn.plain(context, "com.whatsapp", "Mehmet", "merhaba", id = 2))

        assertThat(repository.observeConversations().first()).hasSize(2)
    }

    @Test
    fun `disabling an app stops new captures without touching existing ones`() = runTest(timeout = 60.seconds) {
        val coordinator = coordinator()
        coordinator.feed(FakeSbn.plain(context, "com.whatsapp", "Ayşe", "Merhaba", id = 1))
        preferences.setChannelEnabled("com.whatsapp", false)

        val second = coordinator.feed(FakeSbn.plain(context, "com.whatsapp", "Ayşe", "İkinci mesaj", id = 2, postTime = 5_000_000L))

        assertThat(second).isEmpty()
        assertThat(repository.countAll()).isEqualTo(1)
    }

    @Test
    fun `any app the user picked is captured, not only well-known chat apps`() = runTest(timeout = 60.seconds) {
        preferences.setChannelEnabled("com.some.shop.app", true)

        val stored = coordinator().feed(FakeSbn.plain(context, "com.some.shop.app", "Yeni sipariş", "Siparişiniz hazır"))

        assertThat(stored).hasSize(1)
        assertThat(stored.single().sourcePackage).isEqualTo("com.some.shop.app")
    }

    @Test
    fun `an app the user did not pick is ignored`() = runTest(timeout = 60.seconds) {
        val stored = coordinator().feed(FakeSbn.plain(context, "com.random.game", "Oyun", "Yeni görev!"))

        assertThat(stored).isEmpty()
        assertThat(repository.countAll()).isEqualTo(0)
    }

    @Test
    fun `avatar and picture are stored once and referenced from the message`() = runTest(timeout = 60.seconds) {
        val media = object : NotificationMedia {
            override fun avatar(senderName: String?) = byteArrayOf(1, 2, 3)
            override fun picture(lineIndex: Int?) = byteArrayOf(9, 9, 9, 9)
        }
        val coordinator = coordinator()
        val raw = payloadOf(
            title = "Ayşe",
            lines = listOf(
                MessagingLine("Ayşe", "bak", 1_000L, hasPicture = true, index = 0),
                MessagingLine("Ayşe", "ikincisi", 2_000L, hasPicture = false, index = 1),
            ),
            media = media,
        )

        val stored = coordinator.handleNotification(raw, null)

        assertThat(stored).hasSize(2)
        assertThat(stored[0].avatarPath).isNotNull()
        assertThat(stored[0].avatarPath).isEqualTo(stored[1].avatarPath) // same bytes, one file
        assertThat(stored[0].picturePath).isNotNull()
        assertThat(stored[1].picturePath).isNull()
        assertThat(java.io.File(stored[0].picturePath!!).readBytes()).isEqualTo(byteArrayOf(9, 9, 9, 9))
    }

    @Test
    fun `a picture-only message is kept`() = runTest(timeout = 60.seconds) {
        val media = object : NotificationMedia {
            override fun avatar(senderName: String?): ByteArray? = null
            override fun picture(lineIndex: Int?) = byteArrayOf(5, 5)
        }
        val raw = payloadOf(title = "Selin", lines = listOf(MessagingLine("Selin", "", 1_000L, hasPicture = true, index = 0)), media = media)

        val stored = coordinator().handleNotification(raw, null)

        assertThat(stored).hasSize(1)
        assertThat(stored.single().picturePath).isNotNull()
    }

    @Test
    fun `shared text becomes a message in its own conversation`() = runTest(timeout = 60.seconds) {
        val message = coordinator().handleSharedText("Bu müşteri sipariş istiyor", "com.whatsapp", "Paylaşım")

        assertThat(message).isNotNull()
        assertThat(repository.observeConversations().first()).hasSize(1)
    }
}
