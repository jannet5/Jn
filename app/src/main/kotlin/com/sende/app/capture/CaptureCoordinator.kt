package com.sende.app.capture

import com.sende.app.data.media.MediaStore
import com.sende.app.data.prefs.AppPreferences
import com.sende.app.data.repository.InboxRepository
import com.sende.app.diagnostics.DiagnosticsRecorder
import com.sende.app.domain.model.CaptureMethod
import com.sende.app.domain.model.FilterReason
import com.sende.app.domain.model.InboxMessage
import com.sende.app.domain.model.MessageState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.util.UUID

/**
 * Orchestrates the whole capture pipeline: normalize -> filter -> dedupe -> media -> persist ->
 * diagnostics -> live actions. This is the only coroutine-aware class in `capture/`; every
 * decision it delegates to is plain, unit-tested Kotlin.
 */
class CaptureCoordinator(
    private val filterEngine: MessageFilterEngine,
    private val duplicateGuard: DuplicateGuard,
    private val inboxRepository: InboxRepository,
    private val appPreferences: AppPreferences,
    private val diagnosticsRecorder: DiagnosticsRecorder,
    private val liveActionRegistry: LiveActionRegistry,
    private val mediaStore: MediaStore,
) {

    private val untimedLines = UntimedLineTracker()

    /** The notification left the shade: its next appearance (same words included) is a new message. */
    fun forget(notificationKey: String) = untimedLines.forget(notificationKey)

    /** @return the messages that were newly stored (empty when filtered or already known). */
    suspend fun handleNotification(payload: RawNotificationPayload, live: LiveConversation?): List<InboxMessage> {
        val channelEnabled = appPreferences.isChannelEnabledNow(payload.packageName)
        val normalized = MessageNormalizer.normalize(payload)

        val contentReason = filterEngine.decide(payload, normalized, channelEnabled)
        if (contentReason != FilterReason.ACCEPTED || normalized == null) {
            diagnosticsRecorder.onDecision(payload.packageName, contentReason)
            return emptyList()
        }

        val conversationKey = Fingerprint.conversationKey(payload.packageName, normalized.conversationTitle)
        live?.let { liveActionRegistry.put(conversationKey, it) }

        val stored = mutableListOf<InboxMessage>()
        val avatarBySender = HashMap<String, String?>()

        // Lines without their own timestamp: only the ones this delivery added to the notification are new.
        val untimedAt = normalized.messages.indices.filter { normalized.messages[it].timestampMillis == null }
        val untimedDiff = if (untimedAt.isEmpty()) null else untimedLines.diff(
            payload.notificationKey,
            untimedAt.map { normalized.messages[it].let { m -> m.senderName + "\u0000" + m.messageText } },
        )
        val freshUntimed = untimedDiff?.newIndices?.map { untimedAt[it] }?.toSet().orEmpty()

        for ((position, message) in normalized.messages.withIndex()) {
            if (message.timestampMillis == null) {
                val alreadySeen = position !in freshUntimed ||
                    // After a restart the old lines of a multi-line notification look new: skip the ones stored recently.
                    (untimedDiff!!.firstSighting && untimedAt.size > 1 && inboxRepository.hasSameContentSince(
                        conversationKey, message.senderName, message.messageText, System.currentTimeMillis() - REPLAY_WINDOW_MILLIS,
                    ))
                if (alreadySeen) {
                    diagnosticsRecorder.onDecision(payload.packageName, FilterReason.DUPLICATE_BY_FINGERPRINT)
                    continue
                }
            }
            val fingerprint = if (message.timestampMillis != null) {
                Fingerprint.forTimedMessage(payload.packageName, conversationKey, message.senderName, message.messageText, message.timestampMillis)
            } else {
                Fingerprint.forUntimedMessage(payload.packageName, conversationKey, message.senderName, message.messageText, payload.postTimeEpochMillis)
            }
            if (duplicateGuard.isKnown(fingerprint) || inboxRepository.exists(fingerprint)) {
                duplicateGuard.remember(fingerprint)
                diagnosticsRecorder.onDecision(payload.packageName, FilterReason.DUPLICATE_BY_FINGERPRINT)
                continue
            }

            val (avatarPath, picturePath) = withContext(Dispatchers.IO) {
                // A full disk or a broken image must never cost the customer's words.
                val avatar = avatarBySender.getOrPut(message.senderName) {
                    runCatching { payload.media?.avatar(message.senderName)?.let { mediaStore.save(it) } }.getOrNull()
                }
                val picture = if (message.hasPicture) {
                    runCatching { payload.media?.picture(message.lineIndex)?.let { mediaStore.save(it) } }.getOrNull()
                } else {
                    null
                }
                avatar to picture
            }

            val received = message.timestampMillis ?: payload.postTimeEpochMillis
            val entity = InboxMessage(
                id = UUID.randomUUID().toString(),
                notificationKey = payload.notificationKey,
                fingerprint = fingerprint,
                sourcePackage = payload.packageName,
                conversationKey = conversationKey,
                conversationTitle = normalized.conversationTitle,
                isGroupConversation = normalized.isGroupConversation,
                senderName = message.senderName,
                messageText = message.messageText,
                receivedAt = Instant.ofEpochMilli(received),
                capturedAt = Instant.now(),
                captureMethod = CaptureMethod.NOTIFICATION,
                state = MessageState.UNREAD,
                avatarPath = avatarPath,
                picturePath = picturePath,
            )

            if (inboxRepository.insertIfNew(entity)) {
                duplicateGuard.remember(fingerprint)
                diagnosticsRecorder.onDecision(payload.packageName, FilterReason.ACCEPTED)
                stored += entity
            } else {
                duplicateGuard.remember(fingerprint)
                diagnosticsRecorder.onDecision(payload.packageName, FilterReason.DUPLICATE_BY_FINGERPRINT)
            }
        }
        return stored
    }

    /**
     * Text shared into Sende from another app via ACTION_SEND. [sourcePackageHint] comes from
     * `Activity.getReferrer()`, the supported way to identify the calling app; [senderLabel] is a
     * caller-supplied, already-localized placeholder shown when there is no real sender identity.
     */
    suspend fun handleSharedText(sharedText: String, sourcePackageHint: String?, senderLabel: String): InboxMessage? {
        val trimmed = sharedText.trim()
        if (trimmed.isEmpty() || trimmed.length > MAX_SHARED_TEXT_LENGTH) return null

        val sourcePackage = sourcePackageHint ?: SHARE_FALLBACK_PACKAGE
        val now = Instant.now()
        val conversationKey = Fingerprint.conversationKey(sourcePackage, senderLabel)
        val fingerprint = Fingerprint.forUntimedMessage(sourcePackage, conversationKey, senderLabel, trimmed, now.toEpochMilli())

        if (duplicateGuard.isKnown(fingerprint) || inboxRepository.exists(fingerprint)) return null

        val message = InboxMessage(
            id = UUID.randomUUID().toString(),
            notificationKey = null,
            fingerprint = fingerprint,
            sourcePackage = sourcePackage,
            conversationKey = conversationKey,
            conversationTitle = senderLabel,
            isGroupConversation = false,
            senderName = senderLabel,
            messageText = trimmed,
            receivedAt = now,
            capturedAt = now,
            captureMethod = CaptureMethod.SHARE,
            state = MessageState.UNREAD,
            avatarPath = null,
            picturePath = null,
        )

        if (!inboxRepository.insertIfNew(message)) return null
        duplicateGuard.remember(fingerprint)
        diagnosticsRecorder.onDecision(message.sourcePackage, FilterReason.ACCEPTED)
        return message
    }

    companion object {
        private const val MAX_SHARED_TEXT_LENGTH = 10_000
        private const val REPLAY_WINDOW_MILLIS = 6 * 60 * 60 * 1000L
        const val SHARE_FALLBACK_PACKAGE = "sende.share"
    }
}
