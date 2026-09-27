package com.jn.winremote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jn.winremote.ui.nav.WinRemoteApp
import com.jn.winremote.ui.theme.WinRemoteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val app = application as WinRemoteApplication
        val hasPairedDevice = app.secureStore.listDevices().isNotEmpty()
        setContent {
            WinRemoteTheme {
                WinRemoteApp(hasPairedDevice = hasPairedDevice)
            }
        }
    }
}
