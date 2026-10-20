package com.tekpanel.app.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.test.core.app.ApplicationProvider
import com.github.takahirom.roborazzi.captureRoboImage
import com.tekpanel.app.ui.main.Level
import com.tekpanel.app.ui.main.MainCallbacks
import com.tekpanel.app.ui.main.MainContent
import com.tekpanel.app.ui.theme.TekPanelTheme
import com.tekpanel.app.ui.theme.brandTypography
import org.junit.Assume.assumeTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File

/**
 * Renders the REAL app screen once per brand concept (colours, corner radii and fonts from
 * docs/marka/konsept-N-belirtecler.json), so the founder can pick a concept by looking at the
 * actual product and not only at the mood board. Output: app/build/shots/marka/konsept-N-uygulama.png
 */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34], qualifiers = "tr-rTR-w360dp-h780dp-xxhdpi", application = android.app.Application::class)
class BrandConceptScreenshotTest {

    @get:Rule val compose = createComposeRule()
    @get:Rule val tmp = TemporaryFolder()
    private val context = ApplicationProvider.getApplicationContext<android.content.Context>()

    private fun family(name: String): FontFamily? {
        val key = name.replace(" ", "")
        val file = File(System.getProperty("user.home"), ".fonts").listFiles()?.firstOrNull { it.name.startsWith(key) && it.extension == "ttf" && !it.name.contains("Italic") }
            ?: return null
        return FontFamily(
            listOf(400, 500, 600, 700, 800).map { w ->
                Font(file, FontWeight(w), variationSettings = FontVariation.Settings(FontVariation.weight(w)))
            },
        )
    }

    private fun render(number: Int) {
        val concept = BrandConcepts.all.firstOrNull { it.number == number }
        assumeTrue("concept $number not generated yet", concept != null)
        concept!!
        val heading = family(concept.headingFont) ?: FontFamily.Default
        val body = family(concept.bodyFont) ?: FontFamily.Default
        val state = ScreenshotFixtures.state(tmp.newFolder())
        val open = state.conversations.first().key
        compose.setContent {
            TekPanelTheme(concept.colors, concept.shapes, concept.motion, brandTypography(heading, body)) {
                ScreenshotFixtures.WithIcons(context) {
                    MainContent(
                        state = state,
                        menuOpen = false,
                        diagnosticsUnlocked = false,
                        levelOf = { if (it == open) Level.Children else Level.Collapsed },
                        isLong = { _, default -> default },
                        canReply = { true },
                        actionTitles = { emptyList() },
                        snackbarHostState = SnackbarHostState(),
                        callbacks = MainCallbacks(),
                    )
                }
            }
        }
        compose.onRoot().captureRoboImage("build/shots/marka/konsept-$number-uygulama.png")
    }

    @Test fun concept1() = render(1)
    @Test fun concept2() = render(2)
    @Test fun concept3() = render(3)
    @Test fun concept4() = render(4)
    @Test fun concept5() = render(5)
}
