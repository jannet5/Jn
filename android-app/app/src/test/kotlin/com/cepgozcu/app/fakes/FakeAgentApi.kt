package com.cepgozcu.app.fakes

import com.cepgozcu.app.net.AgentApiContract
import com.cepgozcu.app.net.protocol.AllowedApp
import com.cepgozcu.app.net.protocol.AlertDto
import com.cepgozcu.app.net.protocol.AlertsQuery
import com.cepgozcu.app.net.protocol.AppLaunchResult
import com.cepgozcu.app.net.protocol.AuditEntry
import com.cepgozcu.app.net.protocol.AuditQuery
import com.cepgozcu.app.net.protocol.DiskEventsPage
import com.cepgozcu.app.net.protocol.DiskEventsQuery
import com.cepgozcu.app.net.protocol.DiskGrowthQuery
import com.cepgozcu.app.net.protocol.DiskGrowthResult
import com.cepgozcu.app.net.protocol.DiskOverview
import com.cepgozcu.app.net.protocol.ProcessInfo
import com.cepgozcu.app.net.protocol.ProcessKillResult
import com.cepgozcu.app.net.protocol.SystemMetrics

/**
 * Test double for [AgentApiContract]: every method either returns the queued [Any] result or
 * throws the queued [Throwable], so ViewModel tests exercise real success/failure branching
 * without a socket. Call [nextMetrics] etc, or set [failureToThrow] to force the next call to fail.
 */
class FakeAgentApi : AgentApiContract {
    var nextMetrics: SystemMetrics? = null
    var nextProcesses: List<ProcessInfo> = emptyList()
    var nextApps: List<AllowedApp> = emptyList()
    var nextDiskOverview: DiskOverview? = null
    var nextDiskEventsPage: DiskEventsPage? = null
    var nextDiskGrowth: DiskGrowthResult? = null
    var nextAlerts: List<AlertDto> = emptyList()
    var nextAudit: List<AuditEntry> = emptyList()
    var failureToThrow: Throwable? = null

    var killResult: ProcessKillResult? = null
    var launchResult: AppLaunchResult? = null

    /** Checks [failureToThrow] BEFORE evaluating [value] — [value] is a lazy lambda so a not-yet-configured
     * fake result (which would itself throw via `error(...)`) never masks the failure a test actually queued. */
    private inline fun <T> resultOrThrow(value: () -> T): T {
        failureToThrow?.let { throw it }
        return value()
    }

    override suspend fun getMetrics(): SystemMetrics = resultOrThrow { nextMetrics ?: error("nextMetrics not set") }

    override suspend fun listProcesses(): List<ProcessInfo> = resultOrThrow { nextProcesses }

    override suspend fun killProcess(pid: Int, confirm: Boolean): ProcessKillResult =
        resultOrThrow { killResult ?: error("killResult not set") }

    override suspend fun listApps(): List<AllowedApp> = resultOrThrow { nextApps }

    override suspend fun launchApp(appId: String): AppLaunchResult =
        resultOrThrow { launchResult ?: error("launchResult not set") }

    override suspend fun diskOverview(): DiskOverview = resultOrThrow { nextDiskOverview ?: error("nextDiskOverview not set") }

    override suspend fun diskEvents(query: DiskEventsQuery): DiskEventsPage =
        resultOrThrow { nextDiskEventsPage ?: DiskEventsPage(emptyList(), null) }

    override suspend fun diskGrowth(query: DiskGrowthQuery): DiskGrowthResult =
        resultOrThrow { nextDiskGrowth ?: error("nextDiskGrowth not set") }

    override suspend fun alerts(query: AlertsQuery): List<AlertDto> = resultOrThrow { nextAlerts }

    override suspend fun audit(query: AuditQuery): List<AuditEntry> = resultOrThrow { nextAudit }

    override suspend fun unpair() {
        failureToThrow?.let { throw it }
    }
}
