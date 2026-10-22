package com.sende.app.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
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
 * Ergonomics as a test, not a hope: every control on the main screen must be at least 48 dp tall and
 * wide to hit with a thumb (Material and Android accessibility guidance), at normal and at 200% text
 * size, in every state a customer can see. A control that shrinks below that fails the build.
 */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34], qualifiers = "tr-rTR-w360dp-h780dp-xxhdpi", application = android.app.Application::class)
class ErgonomicsTest {

    @get:Rule val compose = createComposeRule()
    @get:Rule val tmp = TemporaryFolder()

    private val context = ApplicationProvider.getApplicationContext<android.content.Context>()

    private fun render(state: MainUiState, levels: Map<String, Level> = emptyMap(), menuOpen: Boolean = false) = compose.setContent {
        SendeTheme {
            ScreenshotFixtures.WithIcons(context) {
                MainContent(
                    state = state,
                    menuOpen = menuOpen,
                    diagnosticsUnlocked = false,
                    levelOf = { levels[it] ?: Level.Collapsed },
                    isLong = { _, default -> default },
                    canReply = { true },
                    actionTitles = { listOf("Okundu işaretle", "Beğen") },
                    snackbarHostState = SnackbarHostState(),
                    callbacks = MainCallbacks(),
                )
            }
        }
    }

    private fun assertEveryControlIsThumbSized() {
        val controls = compose.onAllNodes(hasClickAction())
        val nodes = controls.fetchSemanticsNodes()
        check(nodes.isNotEmpty()) { "no controls found" }
        val density = context.resources.displayMetrics.density
        val tooSmall = nodes.mapIndexedNotNull { index, node ->
            // touchBoundsInRoot is the area a finger can actually hit (the control, grown to the minimum touch target).
            val bounds = node.touchBoundsInRoot
            // A card cut off by the screen edge is only partly on screen: judge controls that are fully visible.
            val clipped = node.boundsInRoot.height + 1f < node.size.height || node.boundsInRoot.width + 1f < node.size.width
            if (clipped) return@mapIndexedNotNull null
            val widthDp = bounds.width / density
            val heightDp = bounds.height / density
            if (widthDp < MIN_TOUCH_DP - 0.5f || heightDp < MIN_TOUCH_DP - 0.5f) {
                "#$index ${"%.0f".format(widthDp)}x${"%.0f".format(heightDp)} dp: ${node.config}".take(160)
            } else {
                null
            }
        }
        assertThat(tooSmall).isEmpty()
    }

    @Test fun `collapsed list controls are thumb sized`() {
        render(ScreenshotFixtures.state(tmp.newFolder()))
        assertEveryControlIsThumbSized()
    }

    @Test fun `opened card controls are thumb sized`() {
        val state = ScreenshotFixtures.state(tmp.newFolder())
        render(state, levels = mapOf(state.conversations.first().key to Level.Full))
        assertEveryControlIsThumbSized()
    }

    @Test fun `menu controls are thumb sized`() {
        render(ScreenshotFixtures.state(tmp.newFolder()), menuOpen = true)
        assertEveryControlIsThumbSized()
    }

    @Test fun `permission banner controls are thumb sized`() {
        render(ScreenshotFixtures.state(tmp.newFolder(), accessGranted = false, empty = true))
        assertEveryControlIsThumbSized()
    }

    @Test fun `controls stay thumb sized at 200 percent text`() {
        compose.setContent {
            val density = androidx.compose.ui.platform.LocalDensity.current
            androidx.compose.runtime.CompositionLocalProvider(
                androidx.compose.ui.platform.LocalDensity provides androidx.compose.ui.unit.Density(density.density, fontScale = 2f),
            ) {
                SendeTheme {
                    ScreenshotFixtures.WithIcons(context) {
                        MainContent(
                            state = ScreenshotFixtures.state(tmp.newFolder()),
                            menuOpen = false,
                            diagnosticsUnlocked = false,
                            levelOf = { Level.Collapsed },
                            isLong = { _, default -> default },
                            canReply = { true },
                            actionTitles = { emptyList() },
                            snackbarHostState = SnackbarHostState(),
                            callbacks = MainCallbacks(),
                        )
                    }
                }
            }
        }
        assertEveryControlIsThumbSized()
        compose.onNodeWithText("Sende").assertExists()
    }
}

private const val MIN_TOUCH_DP = 48f
