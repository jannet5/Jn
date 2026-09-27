package com.cepgozcu.app.connection

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.ServiceCompat
import com.cepgozcu.app.CepGozcuApp
import com.cepgozcu.app.R
import com.cepgozcu.app.net.ConnectionState
import com.cepgozcu.app.net.protocol.AlertDto
import com.cepgozcu.app.net.protocol.MessageType
import com.cepgozcu.app.net.protocol.WireJson
import com.cepgozcu.app.notifications.NotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.serialization.json.decodeFromJsonElement

/**
 * Keeps [com.cepgozcu.app.net.AgentConnection] alive while the app is in use / briefly
 * backgrounded, and turns any live `alert.push` into a system notification. It shows a low
 * priority ongoing notification with the current connection status so the user always knows
 * whether the app is actually watching their PC right now.
 *
 * HONEST LIMITATION, not a bug: CepGözcü has no push-relay server (a deliberate LAN-first,
 * no-cloud-dependency design choice — see the Windows agent's README). That means once Android
 * fully kills this process (user swipes it away and the OS reclaims it, battery optimization,
 * a long period with the screen off, etc.), there is no mechanism to wake it back up for a new
 * alert — no FCM, no exact alarm, nothing. We do NOT try to work around this with exact alarms,
 * WorkManager polling loops, or other battery-hostile hacks: that would fight the platform and
 * drain the user's battery for a guarantee we can't actually deliver without a server component.
 * The accepted trade-off is: alerts arrive live whenever the app/service is running, and the
 * Alerts/Audit screens show the real history (via Room + a fresh `alerts()`/`audit()` fetch)
 * whenever the user reopens the app.
 */
class ConnectionForegroundService : Service() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var job: Job? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        val app = application as CepGozcuApp
        val initialText = getString(R.string.status_connecting)
        val notification = NotificationHelper.ongoingNotification(this, initialText)
        val serviceType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC else 0
        ServiceCompat.startForeground(
            this,
            NotificationHelper.ONGOING_NOTIFICATION_ID,
            notification,
            serviceType,
        )

        job = scope.launch {
            launch {
                app.connectionRepository.connectionState.collect { state ->
                    val host = app.connectionRepository.currentSessionHost.orEmpty()
                    val text = when (state) {
                        is ConnectionState.Connected -> getString(R.string.notif_connected_text, host)
                        is ConnectionState.Connecting -> getString(R.string.notif_connecting_text, host)
                        is ConnectionState.Disconnected -> getString(R.string.notif_offline_text, host)
                        is ConnectionState.Unauthorized -> getString(R.string.status_unauthorized)
                        ConnectionState.Idle -> getString(R.string.status_offline)
                    }
                    NotificationHelper.updateOngoing(this@ConnectionForegroundService, text)
                }
            }
            launch {
                app.connectionRepository.pushes?.collect { envelope ->
                    if (envelope.type == MessageType.ALERT_PUSH) {
                        val payload = envelope.payload ?: return@collect
                        val alert = runCatching { WireJson.decodeFromJsonElement<AlertDto>(payload) }.getOrNull()
                        if (alert != null) NotificationHelper.postAlert(this@ConnectionForegroundService, alert)
                    }
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY

    override fun onDestroy() {
        job?.cancel()
        super.onDestroy()
    }
}
