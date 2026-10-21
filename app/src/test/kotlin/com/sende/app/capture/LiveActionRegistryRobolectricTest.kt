package com.sende.app.capture

import android.app.PendingIntent
import android.app.RemoteInput
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

/** The inline-reply and app-button paths, driven through real PendingIntents. */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], application = android.app.Application::class)
class LiveActionRegistryRobolectricTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private fun receiver(action: String, onIntent: (Intent) -> Unit): BroadcastReceiver {
        val r = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) = onIntent(intent)
        }
        ContextCompat.registerReceiver(context, r, IntentFilter(action), ContextCompat.RECEIVER_EXPORTED)
        return r
    }

    @Test
    fun `a reply typed in Sende reaches the sending app's own reply action with the text`() {
        var received: CharSequence? = null
        receiver("test.REPLY") { intent -> received = RemoteInput.getResultsFromIntent(intent)?.getCharSequence("reply_key") }

        val remoteInput = androidx.core.app.RemoteInput.Builder("reply_key").setLabel("Yanıtla").build()
        val pending = PendingIntent.getBroadcast(context, 0, Intent("test.REPLY").setPackage(context.packageName), PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = NotificationCompat.Builder(context, "c")
            .setSmallIcon(android.R.drawable.ic_dialog_info).setContentTitle("Can").setContentText("selam")
            .addAction(NotificationCompat.Action.Builder(android.R.drawable.ic_menu_send, "Yanıtla", pending).addRemoteInput(remoteInput).build())
            .build()
        val registry = LiveActionRegistry()
        registry.put("wa|can", LiveActionRegistry.fromNotification(notification))

        val sent = registry.sendReply(context, "wa|can", "Yarın 14:00 uygun")
        shadowOf(android.os.Looper.getMainLooper()).idle()

        assertThat(sent).isTrue()
        assertThat(received.toString()).isEqualTo("Yarın 14:00 uygun")
    }

    @Test
    fun `replying to a chat that is no longer alive reports failure instead of crashing`() {
        assertThat(LiveActionRegistry().sendReply(context, "unknown", "merhaba")).isFalse()
    }

    @Test
    fun `a canceled reply intent reports failure`() {
        val remoteInput = androidx.core.app.RemoteInput.Builder("k").build()
        val pending = PendingIntent.getBroadcast(context, 1, Intent("test.CANCELED").setPackage(context.packageName), PendingIntent.FLAG_MUTABLE)
        val notification = NotificationCompat.Builder(context, "c").setSmallIcon(android.R.drawable.ic_dialog_info)
            .addAction(NotificationCompat.Action.Builder(android.R.drawable.ic_menu_send, "Yanıtla", pending).addRemoteInput(remoteInput).build()).build()
        val registry = LiveActionRegistry()
        registry.put("k", LiveActionRegistry.fromNotification(notification))
        pending.cancel()

        assertThat(registry.sendReply(context, "k", "x")).isFalse()
    }

    @Test
    fun `an app button fires its pending intent`() {
        var fired = false
        receiver("test.LIKE") { fired = true }
        val pending = PendingIntent.getBroadcast(context, 2, Intent("test.LIKE").setPackage(context.packageName), PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(context, "c").setSmallIcon(android.R.drawable.ic_dialog_info)
            .addAction(NotificationCompat.Action.Builder(android.R.drawable.ic_menu_add, "Beğen", pending).build()).build()
        val registry = LiveActionRegistry()
        registry.put("k", LiveActionRegistry.fromNotification(notification))

        val ok = registry.fireAction("k", 0)
        shadowOf(android.os.Looper.getMainLooper()).idle()

        assertThat(ok).isTrue()
        assertThat(fired).isTrue()
        assertThat(registry.fireAction("k", 5)).isFalse()
    }

    @Test
    fun `the registry keeps only the newest conversations`() {
        val registry = LiveActionRegistry(maxEntries = 2)
        registry.put("a", LiveConversation(null, null, null))
        registry.put("b", LiveConversation(null, null, null))
        registry.put("c", LiveConversation(null, null, null))

        assertThat(registry.get("a")).isNull()
        assertThat(registry.get("c")).isNotNull()
    }
}
