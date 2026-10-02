package app.nokta.a.bubble

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(c: Context, i: Intent) {
        if (i.action == Intent.ACTION_BOOT_COMPLETED && BubbleService.isEnabled(c) && Settings.canDrawOverlays(c)) {
            runCatching { BubbleService.start(c) }
        }
    }
}
