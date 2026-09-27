package com.cepgozcu.app

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.cepgozcu.app.connection.ConnectionForegroundService
import com.cepgozcu.app.ui.nav.CepGozcuNavHost
import com.cepgozcu.app.ui.theme.CepGozcuTheme

class MainActivity : ComponentActivity() {

    private val notificationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { /* denied is handled gracefully everywhere notifications are posted */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as CepGozcuApp
        if (app.credentialStore.loadSession() != null) {
            startConnectionService()
        }

        setContent {
            CepGozcuTheme {
                CepGozcuNavHost(
                    onPairingApproved = {
                        requestNotificationPermissionIfNeeded()
                        startConnectionService()
                    },
                )
            }
        }
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
        val granted = ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        if (!granted) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun startConnectionService() {
        val intent = Intent(this, ConnectionForegroundService::class.java)
        ContextCompat.startForegroundService(this, intent)
    }

    companion object {
        /** Used by [com.cepgozcu.app.notifications.NotificationHelper] so both the ongoing and alert notifications open the app when tapped. */
        fun pendingIntent(context: Context): PendingIntent {
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            return PendingIntent.getActivity(context, 0, intent, flags)
        }
    }
}
