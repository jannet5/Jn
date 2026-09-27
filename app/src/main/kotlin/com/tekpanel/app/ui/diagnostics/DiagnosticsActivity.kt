package com.tekpanel.app.ui.diagnostics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.tekpanel.app.ui.theme.TekPanelTheme

/** CAP-16: reachable only via the hidden unlock gesture, never linked from the main screen's normal UI. */
class DiagnosticsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TekPanelTheme {
                DiagnosticsScreen()
            }
        }
    }
}
