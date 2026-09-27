package com.cepgozcu.app.net.protocol

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

/**
 * Wire envelope for every WebSocket text frame, in both directions. Mirrors
 * `CepGozcu.Agent.Core.Protocol.Envelope` on the Windows side field-for-field — keep the two
 * in sync; this is the one contract both apps must agree on byte-for-byte.
 */
@Serializable
data class Envelope(
    val v: Int = 1,
    val id: String? = null,
    val type: String,
    val ts: Long = 0,
    val payload: JsonElement? = null,
    val error: ErrorPayload? = null,
)

@Serializable
data class ErrorPayload(val code: String, val message: String)

object MessageType {
    const val PING = "ping"
    const val PONG = "pong"
    const val UNAUTHORIZED = "unauthorized"

    const val PAIR_BEGIN = "pair.begin"
    const val PAIR_VERIFY = "pair.verify"
    const val PAIR_STATUS = "pair.status"

    const val METRICS_GET = "metrics.get"
    const val METRICS_UPDATE = "metrics.update"

    const val PROCESSES_LIST = "processes.list"
    const val PROCESS_KILL = "process.kill"

    const val APPS_LIST = "apps.list"
    const val APP_LAUNCH = "app.launch"

    const val DISK_OVERVIEW = "disk.overview"
    const val DISK_EVENTS_QUERY = "disk.events.query"
    const val DISK_GROWTH_QUERY = "disk.growth.query"
    const val DISK_FILE_EVENT = "disk.fileEvent"

    const val ALERTS_QUERY = "alerts.query"
    const val ALERT_PUSH = "alert.push"

    const val AUDIT_QUERY = "audit.query"

    const val DEVICE_UNPAIR = "device.unpair"
    const val DEVICE_REVOKED = "device.revoked"
}

/** The one JSON configuration used for every wire message — mirrors CepGozcu.Agent.Core.Protocol.WireJson. */
val WireJson: Json = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
    isLenient = true
}
