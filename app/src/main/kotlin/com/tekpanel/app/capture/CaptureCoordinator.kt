package com.tekpanel.app.capture

import android.app.PendingIntent
import com.tekpanel.app.data.catalog.SourceAppCatalog
import com.tekpanel.app.data.prefs.AppPreferences
import com.tekpanel.app.data.repository.InboxRepository
import com.tekpanel.app.diagnostics.DiagnosticsRecorder
import com.tekpanel.app.domain.model.CaptureMethod
import com.tekpanel.app.domain.model.FilterReason
import com.tekpanel.app.domain.model.InboxMessage
import com.tekpanel.app.domain.model.MessageState
import java.time.Instant
import java.util.UUID

/**
 * Orchestrates the full CAP-04..07 pipeline: extract -> normalize -> filter -> dedupe ->
 * persist -> diagnostics -> live intent registry. This is the only Android/coroutine-aware
 * class in `capture/`; every decision it delegates to ([MessageNormalizer],
 * [MessageFilterEngine], [Fingerprint], [DuplicateGuard]) is plain, unit-tested Kotlin.
 */
class CaptureCoordinator(
    private val filterEngine: MessageFilterEngine,
    private val duplicateGuard: DuplicateGuard,
    private val inboxRepository: InboxRepository,
    private val appPreferences: AppPreferences,
    private val diagnosticsRecorder: DiagnosticsRecorder,
    private val liveIntentRegistry: LiveIntentRegistry,
) {

    /** @return the accepted [InboxMessage], or null if the capture was filtered/duplicate. */
    suspend fun handleNotification(payload: RawNotificationPayload, contentIntent: PendingIntent?): InboxMessage? {
        val app = SourceAppCatalog.byPackageName(payload.packageName)
        val channelId = app?.id
        val normalized = MessageNormalizer.normalize(payload)
        val channelEnabled = channelId != null && appPreferences.isChannelEnabledNow(channelId)

        val contentReason = filterEngine.decide(payload, normalized, channelEnabled)
        if (contentReason != FilterReason.ACCEPTED || normalized == null || channelId == null) {
            diagnosticsRecorder.onDecision(payload.packageName, channelId, contentReason)
            return null
        }

        val fingerprint = Fingerprint.of(payload.packageName, normalized.senderName, normalized.messageText, payload.postTimeEpochMillis)
        if (duplicateGuard.isLikelyDuplicate(payload.notificationKey, fingerprint)) {
            diagnosticsRecorder.onDecision(payload.packageName, channelId, FilterReason.DUPLICATE_BY_KEY)
            return null
        }

        val message = InboxMessage(
            id = UUID.randomUUID().toString(),
            notificationKey = payload.notificationKey,
            fingerprint = fingerprint,
            sourcePackage = payload.packageName,
            channelId = channelId,
            senderName = normalized.senderName,
            messageText = normalized.messageText,
            receivedAt = Instant.ofEpochMilli(payload.postTimeEpochMillis),
            capturedAt = Instant.now(),
            captureMethod = CaptureMethod.NOTIFICATION,
            state = MessageState.UNREAD,
            canOpenLiveConversation = contentIntent != null,
        )

        val inserted = inboxRepository.insertIfNew(message)
        if (!inserted) {
            diagnosticsRecorder.onDecision(payload.packageName, channelId, FilterReason.DUPLICATE_BY_FINGERPRINT)
            return null
        }

        duplicateGuard.remember(payload.notificationKey, fingerprint)
        contentIntent?.let { liveIntentRegistry.put(message.id, it) }
        diagnosticsRecorder.onDecision(payload.packageName, channelId, FilterReason.ACCEPTED)
        return message
    }

    /**
     * CAP-15: text shared into TekPanel from another app via ACTION_SEND.
     *
     * [sourcePackageHint] should come from `Activity.getReferrer()`, the officially supported
     * way to identify the calling app for an ACTION_SEND without relying on that app to pass
     * extras. [senderLabel] is a caller-supplied, already-localized placeholder (e.g.
     * "Paylaşım") shown when TekPanel has no real sender identity to display.
     */
    suspend fun handleSharedText(sharedText: String, sourcePackageHint: String?, senderLabel: String): InboxMessage? {
        val trimmed = sharedText.trim()
        if (trimmed.isEmpty() || trimmed.length > MAX_SHARED_TEXT_LENGTH) return null

        val app = sourcePackageHint?.let { SourceAppCatalog.byPackageName(it) }
        val channelId = app?.id ?: SHARE_FALLBACK_CHANNEL_ID
        val sourcePackage = sourcePackageHint ?: SHARE_FALLBACK_CHANNEL_ID
        val now = Instant.now()
        val fingerprint = Fingerprint.of(sourcePackage, senderLabel, trimmed, now.toEpochMilli())

        if (duplicateGuard.isLikelyDuplicate(null, fingerprint)) return null

        val message = InboxMessage(
            id = UUID.randomUUID().toString(),
            notificationKey = null,
            fingerprint = fingerprint,
            sourcePackage = sourcePackage,
            channelId = channelId,
            senderName = senderLabel,
            messageText = trimmed,
            receivedAt = now,
            capturedAt = now,
            captureMethod = CaptureMethod.SHARE,
            state = MessageState.UNREAD,
            canOpenLiveConversation = false,
        )

        val inserted = inboxRepository.insertIfNew(message)
        if (!inserted) return null
        duplicateGuard.remember(null, fingerprint)
        diagnosticsRecorder.onDecision(message.sourcePackage, channelId, FilterReason.ACCEPTED)
        return message
    }

    companion object {
        private const val MAX_SHARED_TEXT_LENGTH = 10_000
        const val SHARE_FALLBACK_CHANNEL_ID = "share"
    }
}
