package com.cepgozcu.app.net

import com.cepgozcu.app.net.protocol.*
import kotlinx.serialization.encodeToJsonElement

class AgentApiException(val code: String) : Exception(code)

/** Typed request/response wrappers over [AgentConnection.call] — one function per wire message type, mirroring MessageRouter.cs on the agent. */
class AgentApi(private val connection: AgentConnection) {

    private suspend inline fun <reified TReq> encode(type: String, req: TReq): Envelope =
        connection.call(type, WireJson.encodeToJsonElement(req))

    private suspend fun raw(type: String): Envelope = connection.call(type)

    private inline fun <reified T> Envelope.decode(): T {
        error?.let { throw AgentApiException(it.code) }
        return WireJson.decodeFromJsonElement(payload ?: throw AgentApiException("empty_payload"))
    }

    suspend fun getMetrics(): SystemMetrics = raw(MessageType.METRICS_GET).decode()

    suspend fun listProcesses(): List<ProcessInfo> = raw(MessageType.PROCESSES_LIST).decode()

    suspend fun killProcess(pid: Int, confirm: Boolean): ProcessKillResult =
        encode(MessageType.PROCESS_KILL, ProcessKillRequest(pid, confirm)).decode()

    suspend fun listApps(): List<AllowedApp> = raw(MessageType.APPS_LIST).decode()

    suspend fun launchApp(appId: String): AppLaunchResult =
        encode(MessageType.APP_LAUNCH, AppLaunchRequest(appId)).decode()

    suspend fun diskOverview(): DiskOverview = raw(MessageType.DISK_OVERVIEW).decode()

    suspend fun diskEvents(query: DiskEventsQuery): DiskEventsPage =
        encode(MessageType.DISK_EVENTS_QUERY, query).decode()

    suspend fun diskGrowth(query: DiskGrowthQuery): DiskGrowthResult =
        encode(MessageType.DISK_GROWTH_QUERY, query).decode()

    suspend fun alerts(query: AlertsQuery): List<AlertDto> =
        encode(MessageType.ALERTS_QUERY, query).decode()

    suspend fun audit(query: AuditQuery): List<AuditEntry> =
        encode(MessageType.AUDIT_QUERY, query).decode()

    suspend fun unpair() {
        raw(MessageType.DEVICE_UNPAIR)
    }
}
