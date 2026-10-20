package com.sende.app.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.core.app.ApplicationProvider
import com.github.takahirom.roborazzi.captureRoboImage
import com.sende.app.ui.access.AccessGuideContent
import com.sende.app.ui.channels.ChannelPickerContent
import com.sende.app.ui.channels.PickerUiState
import com.sende.app.ui.main.Level
import com.sende.app.ui.main.MainCallbacks
import com.sende.app.ui.main.MainContent
import com.sende.app.ui.main.MainUiState
import com.sende.app.ui.theme.SendeTheme
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * Renders the real Compose UI on the JVM and writes PNGs to app/build/shots, so the design can be
 * looked at (and reviewed) without a phone or emulator.
 */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34], qualifiers = "tr-rTR-w360dp-h780dp-xxhdpi", application = android.app.Application::class)
class ScreenshotTest {

    @get:Rule val compose = createComposeRule()
    @get:Rule val tmp = TemporaryFolder()

    private val context = ApplicationProvider.getApplicationContext<android.content.Context>()

    private fun shot(name: String) = compose.onRoot().captureRoboImage("build/shots/$name.png")

    private fun main(
        state: MainUiState,
        levels: Map<String, Level> = emptyMap(),
        long: Set<String> = emptySet(),
        menuOpen: Boolean = false,
        canReply: Boolean = true,
    ) = compose.setContent {
        SendeTheme { ScreenshotFixtures.WithIcons(context) {
            MainContent(
                state = state,
                menuOpen = menuOpen,
                diagnosticsUnlocked = false,
                levelOf = { levels[it] ?: Level.Collapsed },
                isLong = { id, default -> if (id in long) true else default },
                canReply = { canReply },
                actionTitles = { listOf("Okundu işaretle", "Beğen") },
                snackbarHostState = SnackbarHostState(),
                callbacks = MainCallbacks(),
            )
        } }
    }

    @Test fun main_collapsed() {
        main(ScreenshotFixtures.state(tmp.newFolder()))
        shot("01-ana-ekran-kapali")
    }

    @Test fun main_children_open() {
        val state = ScreenshotFixtures.state(tmp.newFolder())
        val first = state.conversations.first().key
        main(state, levels = mapOf(first to Level.Children))
        shot("02-konusma-acik-mesajlar")
    }

    @Test fun main_full_open() {
        val state = ScreenshotFixtures.state(tmp.newFolder())
        val first = state.conversations.first().key
        main(state, levels = mapOf(first to Level.Full))
        shot("03-konusma-tam-uzun")
    }

    @Test fun main_picture_open() {
        val state = ScreenshotFixtures.state(tmp.newFolder())
        val insta = state.conversations.first { it.sourcePackage == ScreenshotFixtures.INSTAGRAM }.key
        main(state, levels = mapOf(insta to Level.Full))
        shot("04-resimli-mesaj")
    }

    @Test fun main_filtered() {
        main(ScreenshotFixtures.state(tmp.newFolder(), selected = ScreenshotFixtures.TELEGRAM))
        shot("05-kanal-filtreli")
    }

    @Test fun main_menu_open() {
        main(ScreenshotFixtures.state(tmp.newFolder()), menuOpen = true)
        shot("06-menu-acik")
    }

    @Test fun main_access_missing() {
        main(ScreenshotFixtures.state(tmp.newFolder(), accessGranted = false, empty = true))
        shot("07-izin-yok")
    }

    @Test fun main_stalled() {
        main(ScreenshotFixtures.state(tmp.newFolder()).copy(listenerStalled = true))
        shot("11-dinleme-duraklamis")
    }

    @Test fun support_sheet() {
        val plans = listOf(
            com.sende.app.monetization.SupportPlan("monthly", 1, "₺189,99", null),
            com.sende.app.monetization.SupportPlan("quarterly", 3, "₺379,99", 33),
            com.sende.app.monetization.SupportPlan("semiannual", 6, "₺759,99", 33),
            com.sende.app.monetization.SupportPlan("yearly", 12, "₺1.709,99", 25),
            com.sende.app.monetization.SupportPlan("tip_small", 0, "₺39,99", null),
            com.sende.app.monetization.SupportPlan("tip_medium", 0, "₺119,99", null),
            com.sende.app.monetization.SupportPlan("tip_large", 0, "₺319,99", null),
        )
        compose.setContent {
            SendeTheme {
                com.sende.app.ui.support.SupportContent(plans = plans, supporter = false, trialEnded = false, onBuy = {}, onDismiss = {})
            }
        }
        shot("12-destek-plani")
    }

    @Test fun main_empty() {
        main(ScreenshotFixtures.state(tmp.newFolder(), empty = true))
        shot("08-bos")
    }

    @Test fun picker() {
        compose.setContent {
            SendeTheme { ScreenshotFixtures.WithIcons(context) {
                ChannelPickerContent(
                    state = PickerUiState(items = ScreenshotFixtures.pickerChannels(), selectedCount = 3, loading = false),
                    onQuery = {}, onToggle = {}, onSetAllVisible = {}, onDone = {},
                )
            } }
        }
        shot("09-kanal-secici")
    }

    @Test fun access_guide() {
        compose.setContent { SendeTheme { AccessGuideContent(onOpenSettings = {}, onOpenAppInfo = {}) } }
        shot("10-izin-rehberi")
    }
}
