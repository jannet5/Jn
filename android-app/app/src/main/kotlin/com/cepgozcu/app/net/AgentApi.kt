package com.cepgozcu.app.net

import com.cepgozcu.app.net.protocol.*
import kotlinx.serialization.encodeToJsonElement

class AgentApiException(val code: String) : Exception(code)

/**
 * Narrow contract mirroring every [AgentApi] operation, so ViewModels depend on an interface
 * rather than the concrete OkHttp/connection-backed class — unit tests substitute a fake
 * implementation instead of driving a real socket.
 */
interface AgentApiContract {
    suspend fun getMetrics(): SystemMetrics
    suspend fun listProcesses(): List<ProcessInfo>
    suspend fun killProcess(pid: Int, confirm: Boolean): ProcessKillResult
    suspend fun listApps(): List<AllowedApp>
    suspend fun launchApp(appId: String): AppLaunchResult
    suspend fun diskOverview(): DiskOverview
    suspend fun diskEvents(query: DiskEventsQuery): DiskEventsPage
    suspend fun diskGrowth(query: DiskGrowthQuery): DiskGrowthResult
    suspend fun alerts(query: AlertsQuery): List<AlertDto>
    suspend fun audit(query: AuditQuery): List<AuditEntry>
    suspend fun unpair()
}

/** Typed request/response wrappers over [AgentConnection.call] — one function per wire message type, mirroring MessageRouter.cs on the agent. */
class AgentApi(private val connection: AgentConnection) : AgentApiContract {

    private suspend inline fun <reified TReq> encode(type: String, req: TReq): Envelope =
        connection.call(type, WireJson.encodeToJsonElement(req))

    private suspend fun raw(type: String): Envelope = connection.call(type)

    private inline fun <reified T> Envelope.decode(): T {
        error?.let { throw AgentApiException(it.code) }
        return WireJson.decodeFromJsonElement(payload ?: throw AgentApiException("empty_payload"))
    }

    override suspend fun getMetrics(): SystemMetrics = raw(MessageType.METRICS_GET).decode()

    override suspend fun listProcesses(): List<ProcessInfo> = raw(MessageType.PROCESSES_LIST).decode()

    override suspend fun killProcess(pid: Int, confirm: Boolean): ProcessKillResult =
        encode(MessageType.PROCESS_KILL, ProcessKillRequest(pid, confirm)).decode()

    override suspend fun listApps(): List<AllowedApp> = raw(MessageType.APPS_LIST).decode()

    override suspend fun launchApp(appId: String): AppLaunchResult =
        encode(MessageType.APP_LAUNCH, AppLaunchRequest(appId)).decode()

    override suspend fun diskOverview(): DiskOverview = raw(MessageType.DISK_OVERVIEW).decode()

    override suspend fun diskEvents(query: DiskEventsQuery): DiskEventsPage =
        encode(MessageType.DISK_EVENTS_QUERY, query).decode()

    override suspend fun diskGrowth(query: DiskGrowthQuery): DiskGrowthResult =
        encode(MessageType.DISK_GROWTH_QUERY, query).decode()

    override suspend fun alerts(query: AlertsQuery): List<AlertDto> =
        encode(MessageType.ALERTS_QUERY, query).decode()

    override suspend fun audit(query: AuditQuery): List<AuditEntry> =
        encode(MessageType.AUDIT_QUERY, query).decode()

    override suspend fun unpair() {
        raw(MessageType.DEVICE_UNPAIR)
    }
}
