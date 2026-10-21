package com.sende.app.ui

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.sende.app.domain.model.CaptureMethod
import com.sende.app.domain.model.Conversation
import com.sende.app.domain.model.InboxMessage
import com.sende.app.domain.model.MessageState
import com.sende.app.ui.main.Level
import com.sende.app.ui.main.MainCallbacks
import com.sende.app.ui.main.MainContent
import com.sende.app.ui.main.MainUiState
import com.sende.app.ui.theme.SendeTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.time.Instant

/**
 * The founder's bug report: "I tap a message and the list jumps back to the top." This renders a
 * long list, scrolls deep into it, taps a card and asserts the scroll position does not move.
 */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34], qualifiers = "tr-rTR-w360dp-h780dp-xxhdpi", application = android.app.Application::class)
class ScrollStabilityTest {

    @get:Rule val compose = createComposeRule()
    private val context = ApplicationProvider.getApplicationContext<android.content.Context>()

    private fun conversation(i: Int): Conversation {
        val at = Instant.now().minusSeconds(60L * i)
        val messages = (1..2).map { m ->
            InboxMessage(
                id = "c$i-m$m", notificationKey = null, fingerprint = "f$i-$m", sourcePackage = "com.whatsapp",
                conversationKey = "com.whatsapp|kisi$i", conversationTitle = "Kişi $i", isGroupConversation = false,
                senderName = "Kişi $i", messageText = "Merhaba, bu $i. kişiden gelen $m. mesaj", receivedAt = at, capturedAt = at,
                captureMethod = CaptureMethod.NOTIFICATION, state = MessageState.UNREAD, avatarPath = null, picturePath = null,
            )
        }
        return Conversation("com.whatsapp|kisi$i", "com.whatsapp", "Kişi $i", false, null, messages)
    }

    @Test
    fun `tapping a card deep in the list does not move the scroll position`() {
        val conversations = (1..14).map(::conversation)
        val state = MainUiState(
            loaded = true, conversations = conversations, hasEnabledChannels = true, totalUnread = 28,
            labels = mapOf("com.whatsapp" to "WhatsApp"),
        )
        val levels = mutableStateMapOf<String, Level>()
        lateinit var listState: androidx.compose.foundation.lazy.LazyListState

        compose.setContent {
            listState = rememberLazyListState()
            SendeTheme {
                ScreenshotFixtures.WithIcons(context) {
                    MainContent(
                        state = state, menuOpen = false, diagnosticsUnlocked = false,
                        levelOf = { levels[it] ?: Level.Collapsed },
                        isLong = { _, default -> default }, canReply = { false }, actionTitles = { emptyList() },
                        snackbarHostState = SnackbarHostState(),
                        callbacks = MainCallbacks(onCycle = { c ->
                            levels[c.key] = if ((levels[c.key] ?: Level.Collapsed) == Level.Collapsed) Level.Children else Level.Collapsed
                        }),
                        listState = listState,
                    )
                }
            }
        }

        compose.runOnIdle { kotlinx.coroutines.runBlocking { listState.scrollToItem(8) } }
        compose.waitForIdle()
        val before = listState.firstVisibleItemIndex
        assertThat(before).isAtLeast(7)

        compose.onNodeWithText("Kişi 9").performClick()
        compose.waitForIdle()
        assertThat(levels["com.whatsapp|kisi9"]).isEqualTo(Level.Children)
        assertThat(listState.firstVisibleItemIndex).isEqualTo(before)

        compose.onNodeWithText("Kişi 9").performClick() // close again
        compose.waitForIdle()
        assertThat(levels["com.whatsapp|kisi9"]).isEqualTo(Level.Collapsed)
        assertThat(listState.firstVisibleItemIndex).isEqualTo(before)
    }
}
