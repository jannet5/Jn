package com.tekpanel.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.tekpanel.app.ui.common.LocalAppIconLoader
import com.tekpanel.app.ui.common.LocalMediaLoader
import com.tekpanel.app.ui.main.MainScreen
import com.tekpanel.app.ui.theme.TekPanelTheme
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CompositionLocalProvider(
                LocalAppIconLoader provides koinInject(),
                LocalMediaLoader provides koinInject(),
            ) {
                TekPanelTheme {
                    MainScreen()
                }
            }
        }
    }
}
