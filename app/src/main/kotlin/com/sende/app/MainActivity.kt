package com.sende.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.sende.app.ui.common.LocalAppIconLoader
import com.sende.app.ui.common.LocalMediaLoader
import com.sende.app.ui.main.MainScreen
import com.sende.app.ui.theme.SendeTheme
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
                SendeTheme {
                    MainScreen()
                }
            }
        }
    }
}
