package com.sende.app.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.core.app.ApplicationProvider
import com.github.takahirom.roborazzi.captureRoboImage
import com.sende.app.ui.channels.ChannelPickerContent
import com.sende.app.ui.channels.PickerUiState
import com.sende.app.ui.main.Level
import com.sende.app.ui.main.MainCallbacks
import com.sende.app.ui.main.MainContent
import com.sende.app.ui.main.MainUiState
import com.sende.app.ui.theme.Brand
import com.sende.app.ui.theme.SendeTheme
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * The screens used in store and ad graphics (tools/make_store_assets.py): same layouts as the real
 * app, but with invented channel names and colours, so no third-party app brand appears in marketing.
 * Output: app/build/shots/magaza/.
 */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34], qualifiers = "tr-rTR-w360dp-h780dp-xxhdpi", application = android.app.Application::class)
class StoreScreenshotTest {

    @get:Rule val compose = createComposeRule()
    @get:Rule val tmp = TemporaryFolder()

    private val context = ApplicationProvider.getApplicationContext<android.content.Context>()

    private fun shot(name: String) = compose.onRoot().captureRoboImage("build/shots/magaza/$name.png")

    private fun main(state: MainUiState, levels: Map<String, Level> = emptyMap(), dark: Boolean = false) = compose.setContent {
        SendeTheme(colors = if (dark) Brand.dark else Brand.light) {
            ScreenshotFixtures.WithIcons(context, ScreenshotFixtures.storeIconColors) {
                MainContent(
                    state = state,
                    menuOpen = false,
                    diagnosticsUnlocked = false,
                    levelOf = { levels[it] ?: Level.Collapsed },
                    isLong = { _, default -> default },
                    canReply = { true },
                    actionTitles = { listOf("Okundu işaretle") },
                    snackbarHostState = SnackbarHostState(),
                    callbacks = MainCallbacks(),
                )
            }
        }
    }

    @Test fun collapsed() {
        main(ScreenshotFixtures.storeState(tmp.newFolder()))
        shot("01-ana-ekran-kapali")
    }

    @Test fun childrenOpen() {
        val state = ScreenshotFixtures.storeState(tmp.newFolder())
        main(state, levels = mapOf(state.conversations.first().key to Level.Children))
        shot("02-konusma-acik-mesajlar")
    }

    @Test fun fullOpen() {
        val state = ScreenshotFixtures.storeState(tmp.newFolder())
        main(state, levels = mapOf(state.conversations.first().key to Level.Full))
        shot("03-konusma-tam-uzun")
    }

    @Test fun pictureOpen() {
        val state = ScreenshotFixtures.storeState(tmp.newFolder())
        val picture = state.conversations.first { it.sourcePackage == "store.vitrin" }.key
        main(state, levels = mapOf(picture to Level.Full))
        shot("04-resimli-mesaj")
    }

    @Test fun picker() {
        compose.setContent {
            SendeTheme {
                ScreenshotFixtures.WithIcons(context, ScreenshotFixtures.storeIconColors) {
                    ChannelPickerContent(
                        state = PickerUiState(items = ScreenshotFixtures.storePickerChannels(), selectedCount = 3, loading = false),
                        onQuery = {}, onToggle = {}, onSetAllVisible = {}, onDone = {},
                    )
                }
            }
        }
        shot("09-kanal-secici")
    }

    @Test fun dark() {
        val state = ScreenshotFixtures.storeState(tmp.newFolder())
        main(state, levels = mapOf(state.conversations.first().key to Level.Children), dark = true)
        shot("13-koyu-tema")
    }
}
