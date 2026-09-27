package com.jn.winremote.protocol

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

/**
 * Wire protocol data classes for WinRemoteMonitor, matching docs/PROTOCOL.md
 * EXACTLY. Field names are idiomatic Kotlin camelCase; [SerialName] pins the
 * on-the-wire JSON key to the snake_case name the spec defines. Do not rename
 * a [SerialName] value without re-checking PROTOCOL.md.
 *
 * Two separate sealed hierarchies (client->server, server->client) are used
 * because kotlinx.serialization resolves sealed-class polymorphism purely
 * from the compile-time declared type + "type" discriminator, with no
 * runtime registration needed.
 */

const val PROTOCOL_VERSION = 1
const val CLIENT_KIND = "android"
const val APP_VERSION = "1.0.0"

val protocolJson: Json = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
    isLenient = false
    classDiscriminator = "type"
    // Defense in depth: a server-side "empty list" can legitimately arrive
    // as JSON null (e.g. Go's encoding/json marshals a nil slice as null,
    // not []) even though every `items` field here is meant to always be a
    // list. Without this, such a null would throw during decode -
    // decodeServerMessage would silently swallow that exception and drop
    // the whole message, and the caller's request would hang until its
    // own timeout instead of just seeing an empty list. coerceInputValues
    // makes a null (or any other invalid value) for a field that has a
    // default fall back to that default instead of failing decode.
    coerceInputValues = true
}

// ---------------------------------------------------------------------------
// Shared (non-discriminated) payload objects, embedded inside messages
// ---------------------------------------------------------------------------

@Serializable
data class RamInfo(
    @SerialName("total_bytes") val totalBytes: Long,
    @SerialName("used_bytes") val usedBytes: Long,
)

@Serializable
data class DiskInfo(
    @SerialName("volume") val volume: String,
    @SerialName("total_bytes") val totalBytes: Long,
    @SerialName("used_bytes") val usedBytes: Long,
    @SerialName("free_bytes") val freeBytes: Long,
)

@Serializable
data class ProcessItem(
    @SerialName("pid") val pid: Int,
    @SerialName("name") val name: String,
    @SerialName("exe") val exe: String,
    @SerialName("user") val user: String,
    @SerialName("cpu_percent") val cpuPercent: Double,
    @SerialName("ram_bytes") val ramBytes: Long,
    @SerialName("protected") val isProtected: Boolean,
)

@Serializable
data class AllowedAppItem(
    @SerialName("app_id") val appId: String,
    @SerialName("label") val label: String,
)

/** Shape shared by the `file_event` push message and `file_events.items[]`. */
@Serializable
data class FileEventData(
    @SerialName("ts") val ts: Long,
    @SerialName("op") val op: String,
    @SerialName("path") val path: String,
    @SerialName("old_path") val oldPath: String? = null,
    @SerialName("size_bytes") val sizeBytes: Long,
    @SerialName("is_dir") val isDir: Boolean,
)

/** Known values of [FileEventData.op] / [ClientMessage.ListFileEvents.op]. */
object FileOp {
    const val CREATED = "created"
    const val MODIFIED = "modified"
    const val DELETED = "deleted"
    const val RENAMED = "renamed"
    const val MOVED = "moved"
    val ALL = listOf(CREATED, MODIFIED, DELETED, RENAMED, MOVED)
}

/** Shape shared by the `alert` push message and `alerts.items[]`. */
@Serializable
data class AlertData(
    @SerialName("ts") val ts: Long,
    @SerialName("id") val id: String,
    @SerialName("kind") val kind: String,
    @SerialName("severity") val severity: String,
    @SerialName("message") val message: String,
    @SerialName("context") val context: JsonElement? = null,
)

object AlertKind {
    const val LARGE_FILE = "large_file"
    const val FAST_GROWTH = "fast_growth"
    const val DISK_FILL_RATE = "disk_fill_rate"
    const val LOW_FREE_SPACE = "low_free_space"
}

object AlertSeverity {
    const val WARNING = "warning"
    const val CRITICAL = "critical"
}

@Serializable
data class TopGrowthItem(
    @SerialName("path") val path: String,
    @SerialName("is_dir") val isDir: Boolean,
    @SerialName("delta_bytes") val deltaBytes: Long,
    @SerialName("size_before") val sizeBefore: Long,
    @SerialName("size_after") val sizeAfter: Long,
)

@Serializable
data class HistoryItem(
    @SerialName("ts") val ts: Long,
    @SerialName("device_id") val deviceId: String,
    @SerialName("device_name") val deviceName: String,
    @SerialName("action") val action: String,
    @SerialName("success") val success: Boolean,
    @SerialName("detail") val detail: String? = null,
    @SerialName("reason") val reason: String? = null,
)

object HistoryAction {
    const val KILL_PROCESS = "kill_process"
    const val LAUNCH_APP = "launch_app"
    const val PAIR = "pair"
    const val UNPAIR = "unpair"
}

// ---------------------------------------------------------------------------
// Client -> Server
// ---------------------------------------------------------------------------

@Serializable
sealed class ClientMessage {

    @Serializable
    @SerialName("hello")
    data class Hello(
        @SerialName("proto_version") val protoVersion: Int = PROTOCOL_VERSION,
        @SerialName("client") val client: String = CLIENT_KIND,
        @SerialName("app_version") val appVersion: String = APP_VERSION,
    ) : ClientMessage()

    @Serializable
    @SerialName("pair_request")
    data class PairRequest(
        @SerialName("request_id") val requestId: String,
        @SerialName("pairing_code") val pairingCode: String,
        @SerialName("device_name") val deviceName: String,
    ) : ClientMessage()

    @Serializable
    @SerialName("auth_request")
    data class AuthRequest(
        @SerialName("request_id") val requestId: String,
        @SerialName("device_id") val deviceId: String,
    ) : ClientMessage()

    @Serializable
    @SerialName("auth_response")
    data class AuthResponse(
        @SerialName("request_id") val requestId: String,
        @SerialName("hmac_b64") val hmacB64: String,
    ) : ClientMessage()

    @Serializable
    @SerialName("subscribe")
    data class Subscribe(
        @SerialName("topics") val topics: List<String>,
    ) : ClientMessage()

    @Serializable
    @SerialName("list_processes")
    data class ListProcesses(
        @SerialName("request_id") val requestId: String,
        @SerialName("query") val query: String? = null,
    ) : ClientMessage()

    @Serializable
    @SerialName("kill_process")
    data class KillProcess(
        @SerialName("request_id") val requestId: String,
        @SerialName("pid") val pid: Int,
    ) : ClientMessage()

    @Serializable
    @SerialName("list_allowed_apps")
    data class ListAllowedApps(
        @SerialName("request_id") val requestId: String,
    ) : ClientMessage()

    @Serializable
    @SerialName("launch_app")
    data class LaunchApp(
        @SerialName("request_id") val requestId: String,
        @SerialName("app_id") val appId: String,
    ) : ClientMessage()

    @Serializable
    @SerialName("list_file_events")
    data class ListFileEvents(
        @SerialName("request_id") val requestId: String,
        @SerialName("from_ts") val fromTs: Long? = null,
        @SerialName("to_ts") val toTs: Long? = null,
        @SerialName("op") val op: String? = null,
        @SerialName("min_size_bytes") val minSizeBytes: Long = 0,
        @SerialName("path_prefix") val pathPrefix: String? = null,
        @SerialName("limit") val limit: Int = 100,
        @SerialName("offset") val offset: Int = 0,
    ) : ClientMessage()

    @Serializable
    @SerialName("top_growth")
    data class TopGrowth(
        @SerialName("request_id") val requestId: String,
        @SerialName("window") val window: String,
        @SerialName("limit") val limit: Int = 20,
    ) : ClientMessage()

    @Serializable
    @SerialName("list_alerts")
    data class ListAlerts(
        @SerialName("request_id") val requestId: String,
        @SerialName("limit") val limit: Int = 50,
    ) : ClientMessage()

    @Serializable
    @SerialName("list_history")
    data class ListHistory(
        @SerialName("request_id") val requestId: String,
        @SerialName("from_ts") val fromTs: Long? = null,
        @SerialName("to_ts") val toTs: Long? = null,
        @SerialName("limit") val limit: Int = 50,
        @SerialName("offset") val offset: Int = 0,
    ) : ClientMessage()
}

fun encodeClientMessage(message: ClientMessage): String =
    protocolJson.encodeToString(ClientMessage.serializer(), message)

// ---------------------------------------------------------------------------
// Server -> Client
// ---------------------------------------------------------------------------

@Serializable
sealed class ServerMessage {

    @Serializable
    @SerialName("hello_ack")
    data class HelloAck(
        @SerialName("proto_version") val protoVersion: Int,
        @SerialName("agent_version") val agentVersion: String,
        @SerialName("hostname") val hostname: String,
    ) : ServerMessage()

    @Serializable
    @SerialName("pair_success")
    data class PairSuccess(
        @SerialName("request_id") val requestId: String,
        @SerialName("device_id") val deviceId: String,
        @SerialName("device_secret_b64") val deviceSecretB64: String,
    ) : ServerMessage()

    @Serializable
    @SerialName("pair_failed")
    data class PairFailed(
        @SerialName("request_id") val requestId: String,
        @SerialName("reason") val reason: String,
    ) : ServerMessage()

    @Serializable
    @SerialName("auth_challenge")
    data class AuthChallenge(
        @SerialName("request_id") val requestId: String,
        @SerialName("nonce") val nonce: String,
    ) : ServerMessage()

    @Serializable
    @SerialName("auth_success")
    data class AuthSuccess(
        @SerialName("request_id") val requestId: String,
    ) : ServerMessage()

    @Serializable
    @SerialName("auth_failed")
    data class AuthFailed(
        @SerialName("request_id") val requestId: String,
        @SerialName("reason") val reason: String,
    ) : ServerMessage()

    @Serializable
    @SerialName("metrics")
    data class Metrics(
        @SerialName("ts") val ts: Long,
        @SerialName("cpu_percent") val cpuPercent: Double,
        @SerialName("ram") val ram: RamInfo,
        @SerialName("disks") val disks: List<DiskInfo> = emptyList(),
    ) : ServerMessage()

    @Serializable
    @SerialName("file_event")
    data class FileEvent(
        @SerialName("ts") val ts: Long,
        @SerialName("op") val op: String,
        @SerialName("path") val path: String,
        @SerialName("old_path") val oldPath: String? = null,
        @SerialName("size_bytes") val sizeBytes: Long,
        @SerialName("is_dir") val isDir: Boolean,
    ) : ServerMessage()

    @Serializable
    @SerialName("alert")
    data class Alert(
        @SerialName("ts") val ts: Long,
        @SerialName("id") val id: String,
        @SerialName("kind") val kind: String,
        @SerialName("severity") val severity: String,
        @SerialName("message") val message: String,
        @SerialName("context") val context: JsonElement? = null,
    ) : ServerMessage()

    @Serializable
    @SerialName("process_list")
    data class ProcessList(
        @SerialName("request_id") val requestId: String,
        @SerialName("items") val items: List<ProcessItem> = emptyList(),
    ) : ServerMessage()

    @Serializable
    @SerialName("action_result")
    data class ActionResult(
        @SerialName("request_id") val requestId: String,
        @SerialName("action") val action: String,
        @SerialName("success") val success: Boolean,
        @SerialName("reason") val reason: String? = null,
        @SerialName("pid") val pid: Int? = null,
    ) : ServerMessage()

    @Serializable
    @SerialName("allowed_apps")
    data class AllowedApps(
        @SerialName("request_id") val requestId: String,
        @SerialName("items") val items: List<AllowedAppItem> = emptyList(),
    ) : ServerMessage()

    @Serializable
    @SerialName("file_events")
    data class FileEventsResult(
        @SerialName("request_id") val requestId: String,
        @SerialName("total") val total: Int,
        @SerialName("items") val items: List<FileEventData> = emptyList(),
    ) : ServerMessage()

    @Serializable
    @SerialName("top_growth_result")
    data class TopGrowthResult(
        @SerialName("request_id") val requestId: String,
        @SerialName("window") val window: String,
        @SerialName("items") val items: List<TopGrowthItem> = emptyList(),
    ) : ServerMessage()

    @Serializable
    @SerialName("alerts")
    data class AlertsResult(
        @SerialName("request_id") val requestId: String,
        @SerialName("items") val items: List<AlertData> = emptyList(),
    ) : ServerMessage()

    @Serializable
    @SerialName("history")
    data class HistoryResult(
        @SerialName("request_id") val requestId: String,
        @SerialName("total") val total: Int,
        @SerialName("items") val items: List<HistoryItem> = emptyList(),
    ) : ServerMessage()

    @Serializable
    @SerialName("error")
    data class Error(
        @SerialName("request_id") val requestId: String? = null,
        @SerialName("reason") val reason: String,
        @SerialName("message") val message: String? = null,
    ) : ServerMessage()
}

/** Every reply that carries a `request_id` back, for pending-request correlation; null for pushes. */
fun ServerMessage.requestIdOrNull(): String? = when (this) {
    is ServerMessage.PairSuccess -> requestId
    is ServerMessage.PairFailed -> requestId
    is ServerMessage.AuthChallenge -> requestId
    is ServerMessage.AuthSuccess -> requestId
    is ServerMessage.AuthFailed -> requestId
    is ServerMessage.ProcessList -> requestId
    is ServerMessage.ActionResult -> requestId
    is ServerMessage.AllowedApps -> requestId
    is ServerMessage.FileEventsResult -> requestId
    is ServerMessage.TopGrowthResult -> requestId
    is ServerMessage.AlertsResult -> requestId
    is ServerMessage.HistoryResult -> requestId
    is ServerMessage.Error -> requestId
    is ServerMessage.HelloAck -> null
    is ServerMessage.Metrics -> null
    is ServerMessage.FileEvent -> null
    is ServerMessage.Alert -> null
}

/**
 * Decodes one WS text frame. Returns null (instead of throwing) for a frame
 * whose "type" the client doesn't recognize, so a future/unknown message
 * added on the server side degrades gracefully instead of tearing down the
 * connection.
 */
fun decodeServerMessage(text: String): ServerMessage? = try {
    protocolJson.decodeFromString(ServerMessage.serializer(), text)
} catch (_: Exception) {
    null
}
