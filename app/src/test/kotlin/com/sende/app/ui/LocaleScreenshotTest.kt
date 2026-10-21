package com.sende.app.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.core.app.ApplicationProvider
import com.github.takahirom.roborazzi.captureRoboImage
import com.sende.app.ui.main.Level
import com.sende.app.ui.main.MainCallbacks
import com.sende.app.ui.main.MainContent
import com.sende.app.ui.theme.SendeTheme
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

private fun render(compose: androidx.compose.ui.test.junit4.ComposeContentTestRule, tmp: TemporaryFolder, name: String, stalled: Boolean) {
    val context = ApplicationProvider.getApplicationContext<android.content.Context>()
    val state = ScreenshotFixtures.state(tmp.newFolder()).copy(listenerStalled = stalled)
    val open = state.conversations.first().key
    compose.setContent {
        SendeTheme {
            ScreenshotFixtures.WithIcons(context) {
                MainContent(
                    state = state, menuOpen = false, diagnosticsUnlocked = false,
                    levelOf = { if (it == open) Level.Children else Level.Collapsed },
                    isLong = { _, default -> default }, canReply = { true }, actionTitles = { emptyList() },
                    snackbarHostState = SnackbarHostState(), callbacks = MainCallbacks(),
                    oemHint = if (stalled) context.getString(com.sende.app.R.string.oem_hint_samsung) else null,
                )
            }
        }
    }
    compose.onRoot().captureRoboImage("build/shots/dil/$name.png")
}

/** Right-to-left check: Arabic must mirror the whole layout, not just translate the words. */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34], qualifiers = "ar-rSA-w360dp-h780dp-xxhdpi", application = android.app.Application::class)
class ArabicRtlScreenshotTest {
    @get:Rule val compose = createComposeRule()
    @get:Rule val tmp = TemporaryFolder()
    @Test fun main_rtl() = render(compose, tmp, "ar-ana-ekran", stalled = false)
    @Test fun stalled_rtl() = render(compose, tmp, "ar-dinleme-duraklamis", stalled = true)
}

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34], qualifiers = "fr-rFR-w360dp-h780dp-xxhdpi", application = android.app.Application::class)
class FrenchScreenshotTest {
    @get:Rule val compose = createComposeRule()
    @get:Rule val tmp = TemporaryFolder()
    @Test fun main_fr() = render(compose, tmp, "fr-ana-ekran", stalled = false)
    @Test fun stalled_fr() = render(compose, tmp, "fr-dinleme-duraklamis", stalled = true)
}
