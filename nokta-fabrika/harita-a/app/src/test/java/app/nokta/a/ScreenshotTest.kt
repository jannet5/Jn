package app.nokta.a

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import app.nokta.a.model.ListState
import app.nokta.a.model.Removal
import app.nokta.a.ui.NoktaScreen
import app.nokta.a.ui.ScreenActions
import app.nokta.a.ui.UiState
import app.nokta.a.ui.theme.NoktaTheme
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34], qualifiers = "w360dp-h720dp-xhdpi")
class ScreenshotTest {
    @get:Rule val rule = createComposeRule()

    private fun sample(): ListState =
        ListState().add("Süt").add("Ekmek").add("Yumurta, on adet").add("Çay").add("Peynir")
            .toggle(2).toggle(4)

    private fun shot(name: String, dark: Boolean, empty: Boolean = false, undo: Boolean = false) {
        val list = if (empty) ListState() else sample()
        val u = if (undo) Removal(listOf(), Removal.Kind.DELETE) else null
        rule.setContent {
            NoktaTheme(dark = dark) {
                NoktaScreen(UiState(list, u, 1), bubbleOn = true, overlayGranted = true, actions = ScreenActions(), autoFocus = false)
            }
        }
        rule.onRoot().captureRoboImage("build/screens/$name.png")
    }

    @Test fun light() = shot("light", false)
    @Test fun dark() = shot("dark", true)
    @Test fun empty() = shot("empty_light", false, empty = true)
    @Test fun undoBar() = shot("undo_dark", true, undo = true)
}
