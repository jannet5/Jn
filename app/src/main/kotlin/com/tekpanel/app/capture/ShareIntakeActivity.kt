package com.tekpanel.app.capture

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.tekpanel.app.MainActivity
import com.tekpanel.app.R
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

/**
 * CAP-15 fallback intake: `Share -> TekPanel` from any app that can share plain text.
 *
 * Deliberately has no visible UI of its own (transparent, no history, excluded from
 * recents): it validates the shared payload, stores it, and hands off to [MainActivity] so
 * the user immediately sees the new card in the one real screen the app has.
 */
class ShareIntakeActivity : ComponentActivity() {

    private val captureCoordinator: CaptureCoordinator by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedText = intent?.takeIf { it.action == Intent.ACTION_SEND }
            ?.getStringExtra(Intent.EXTRA_TEXT)

        if (sharedText.isNullOrBlank()) {
            Toast.makeText(this, getString(R.string.share_intake_empty), Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Activity.getReferrer() is the OS-supported way to learn the calling package for an
        // ACTION_SEND without trusting extras the caller could spoof.
        val sourcePackageHint = referrer?.host

        lifecycleScope.launch {
            val senderLabel = getString(R.string.share_sender_label)
            val accepted = captureCoordinator.handleSharedText(sharedText, sourcePackageHint, senderLabel)
            val message = if (accepted != null) R.string.share_intake_success else R.string.share_intake_duplicate
            Toast.makeText(this@ShareIntakeActivity, message, Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@ShareIntakeActivity, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
            finish()
        }
    }
}
