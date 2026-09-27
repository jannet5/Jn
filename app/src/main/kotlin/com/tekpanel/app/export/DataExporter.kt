package com.tekpanel.app.export

import com.tekpanel.app.data.repository.InboxRepository
import com.tekpanel.app.domain.model.InboxMessage
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * CAP-17: user-initiated, on-device JSON export. Nothing here ever calls the network; the
 * caller decides where the resulting string goes (a share sheet to Files/Drive/email, entirely
 * the user's own choice, not something TekPanel uploads on its own).
 */
@Serializable
data class ExportedMessage(
    val channelId: String,
    val senderName: String,
    val messageText: String,
    val receivedAtEpochMillis: Long,
    val captureMethod: String,
    val state: String,
)

@Serializable
data class ExportPayload(
    val exportFormatVersion: Int = 1,
    val exportedAtEpochMillis: Long,
    val messages: List<ExportedMessage>,
)

class DataExporter(private val inboxRepository: InboxRepository) {

    private val json = Json { prettyPrint = true }

    suspend fun exportAsJson(): String {
        val messages = inboxRepository.exportSnapshot()
        val payload = ExportPayload(
            exportedAtEpochMillis = System.currentTimeMillis(),
            messages = messages.map { it.toExported() },
        )
        return json.encodeToString(payload)
    }

    private fun InboxMessage.toExported() = ExportedMessage(
        channelId = channelId,
        senderName = senderName,
        messageText = messageText,
        receivedAtEpochMillis = receivedAt.toEpochMilli(),
        captureMethod = captureMethod.name,
        state = state.name,
    )
}
