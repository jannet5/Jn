package com.tekpanel.app.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.tekpanel.app.data.local.TekPanelDatabase
import com.tekpanel.app.data.media.MediaStore
import com.tekpanel.app.data.repository.InboxRepository
import com.tekpanel.app.domain.model.CaptureMethod
import com.tekpanel.app.domain.model.InboxMessage
import com.tekpanel.app.domain.model.MessageState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File
import java.time.Duration
import java.time.Instant

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], application = android.app.Application::class)
class InboxRepositoryRobolectricTest {

    private val context = ApplicationProvider.getApplicationContext<android.content.Context>()
    private lateinit var db: TekPanelDatabase
    private lateinit var media: MediaStore
    private lateinit var repo: InboxRepository

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(context, TekPanelDatabase::class.java).allowMainThreadQueries().build()
        media = MediaStore(context)
        repo = InboxRepository(db.inboxMessageDao(), media)
    }

    private fun message(
        id: String,
        conversation: String = "ayse",
        pkg: String = "com.whatsapp",
        at: Long = 1_000,
        state: MessageState = MessageState.UNREAD,
        avatar: String? = null,
    ) = InboxMessage(
        id = id, notificationKey = "k", fingerprint = "fp-$id", sourcePackage = pkg,
        conversationKey = "$pkg|$conversation", conversationTitle = conversation, isGroupConversation = false,
        senderName = conversation, messageText = "text $id", receivedAt = Instant.ofEpochMilli(at),
        capturedAt = Instant.ofEpochMilli(at), captureMethod = CaptureMethod.NOTIFICATION, state = state,
        avatarPath = avatar, picturePath = null,
    )

    @Test
    fun `conversations group by chat and the newest conversation comes first`() = runTest {
        repo.insertIfNew(message("1", "ayse", at = 1_000))
        repo.insertIfNew(message("2", "mehmet", at = 2_000))
        repo.insertIfNew(message("3", "ayse", at = 3_000))

        val conversations = repo.observeConversations().first()

        assertThat(conversations.map { it.title }).containsExactly("ayse", "mehmet").inOrder()
        assertThat(conversations.first().messages.map { it.id }).containsExactly("1", "3").inOrder()
    }

    @Test
    fun `marking read removes only those messages from the screen`() = runTest {
        repo.insertIfNew(message("1"))
        repo.insertIfNew(message("2", at = 2_000))

        repo.markRead(listOf("1"))

        assertThat(repo.observeConversations().first().single().messages.map { it.id }).containsExactly("2")
    }

    @Test
    fun `clearing the screen can be undone and honours the app filter`() = runTest {
        repo.insertIfNew(message("w1", pkg = "com.whatsapp"))
        repo.insertIfNew(message("i1", pkg = "com.instagram.android", conversation = "x"))

        val previous = repo.clearUnread(listOf("com.whatsapp"))

        assertThat(repo.observeConversations().first().map { it.sourcePackage }).containsExactly("com.instagram.android")
        repo.restoreStates(previous)
        assertThat(repo.observeConversations().first()).hasSize(2)
    }

    @Test
    fun `clearing everything with a null filter hides all unread`() = runTest {
        repo.insertIfNew(message("1"))
        repo.insertIfNew(message("2", pkg = "com.instagram.android", conversation = "x"))

        repo.clearUnread(null)

        assertThat(repo.observeConversations().first()).isEmpty()
    }

    @Test
    fun `package counters report total and unread for ranking`() = runTest {
        repo.insertIfNew(message("1"))
        repo.insertIfNew(message("2", at = 2_000))
        repo.markRead(listOf("1"))

        val counts = repo.observePackageCounts().first().single()

        assertThat(counts.total).isEqualTo(2)
        assertThat(counts.unread).isEqualTo(1)
    }

    @Test
    fun `pruning drops old handled messages and unreferenced pictures but never unread ones`() = runTest {
        val kept = media.save(byteArrayOf(1))
        val orphan = media.save(byteArrayOf(2))
        repo.insertIfNew(message("old-read", at = 1_000, state = MessageState.READ))
        repo.insertIfNew(message("old-unread", at = 1_000, avatar = kept))

        repo.prune(Duration.ofDays(14))

        assertThat(repo.countAll()).isEqualTo(1) // only the unread one survives
        assertThat(File(kept).exists()).isTrue()
        assertThat(File(orphan).exists()).isFalse()
    }

    @Test
    fun `erasing everything removes messages and picture files`() = runTest {
        val path = media.save(byteArrayOf(7))
        repo.insertIfNew(message("1", avatar = path))

        repo.eraseAllMessages()

        assertThat(repo.countAll()).isEqualTo(0)
        assertThat(File(path).exists()).isFalse()
    }
}
