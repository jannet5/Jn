package com.sende.app.capture

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.sende.app.MainActivity
import com.sende.app.R
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

/**
 * CAP-15 fallback intake: `Share -> Sende` from any app that can share plain text.
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

        // Shared text always lands under its own "Shared" source. The referrer is deliberately not
        // used: any caller can put a referrer extra of its choosing into the intent, so it would let
        // another app file messages under, say, WhatsApp.
        val sourcePackageHint: String? = null

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
