package com.cepgozcu.app.net.protocol

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Mirrors CepGozcu.Agent.Core.Protocol.Dtos.cs field-for-field (camelCase on the wire, matching
// System.Text.Json's Web defaults + JsonStringEnumConverter(CamelCase) on the .NET side).

// ---- Metrics -----------------------------------------------------------

@Serializable
data class CpuMetrics(val totalPercent: Double, val perCoreProcess: List<Double> = emptyList(), val coreCount: Int)

@Serializable
data class MemoryMetrics(val totalBytes: Long, val usedBytes: Long, val availableBytes: Long)

@Serializable
data class DiskMetrics(
    val name: String,
    val volumeLabel: String,
    val totalBytes: Long,
    val freeBytes: Long,
    val usedBytes: Long,
    val isLowSpace: Boolean,
)

@Serializable
data class SystemMetrics(
    val ts: Long,
    val cpu: CpuMetrics,
    val memory: MemoryMetrics,
    val disks: List<DiskMetrics>,
    val machineName: String,
    val osVersion: String,
    val uptime: String,
)

// ---- Processes -----------------------------------------------------------

@Serializable
data class ProcessInfo(
    val pid: Int,
    val name: String,
    val displayName: String? = null,
    val cpuPercent: Double,
    val workingSetBytes: Long,
    val isCritical: Boolean,
    val canKill: Boolean,
    val startedAt: String? = null,
)

@Serializable
data class ProcessKillRequest(val pid: Int, val confirm: Boolean)

@Serializable
data class ProcessKillResult(val pid: Int, val success: Boolean, val reason: String? = null)

// ---- App launcher -----------------------------------------------------------

@Serializable
data class AllowedApp(val id: String, val displayName: String, val path: String, val iconHint: String? = null)

@Serializable
data class AppLaunchRequest(val appId: String)

@Serializable
data class AppLaunchResult(val appId: String, val success: Boolean, val reason: String? = null, val pid: Int? = null)

// ---- Disk overview -----------------------------------------------------------

@Serializable
data class WatchedRootStatus(val path: String, val enabled: Boolean, val lastFullScanAt: String? = null, val indexedFileCount: Long = 0)

@Serializable
data class DiskOverview(val disks: List<DiskMetrics>, val watchedRoots: List<WatchedRootStatus>)

// ---- Disk events / history -----------------------------------------------------------

@Serializable
enum class FileEventKind {
    @SerialName("created") Created,
    @SerialName("grew") Grew,
    @SerialName("shrank") Shrank,
    @SerialName("modified") Modified,
    @SerialName("deleted") Deleted,
    @SerialName("renamed") Renamed,
    @SerialName("moved") Moved,
}

@Serializable
data class FileEventDto(
    val id: Long,
    val kind: FileEventKind,
    val path: String,
    val oldPath: String? = null,
    val sizeBytes: Long,
    val sizeDeltaBytes: Long,
    val coalescedCount: Int,
    val occurredAt: String,
    val source: String,
)

@Serializable
data class DiskEventsQuery(
    val rootPath: String? = null,
    val since: String? = null,
    val until: String? = null,
    val kinds: List<FileEventKind>? = null,
    val cursor: Long? = null,
    val limit: Int = 100,
)

@Serializable
data class DiskEventsPage(val items: List<FileEventDto>, val nextCursor: Long? = null)

// ---- Growth analytics -----------------------------------------------------------

@Serializable
enum class GrowthRange {
    @SerialName("lastHour") LastHour,
    @SerialName("lastDay") LastDay,
    @SerialName("lastWeek") LastWeek,
    @SerialName("lastMonth") LastMonth,
    @SerialName("custom") Custom,
}

@Serializable
data class DiskGrowthQuery(
    val rootPath: String,
    val range: GrowthRange,
    val customFrom: String? = null,
    val customTo: String? = null,
    val top: Int = 20,
)

@Serializable
data class FolderGrowthEntry(
    val path: String,
    val sizeAtStartBytes: Long,
    val sizeAtEndBytes: Long,
    val deltaBytes: Long,
    val bytesPerHour: Double,
    val newFileCount: Int,
    val deletedFileCount: Int,
)

@Serializable
data class BiggestFileEntry(val path: String, val sizeBytes: Long, val seenAt: String, val kind: FileEventKind)

@Serializable
data class GrowthPoint(val bucket: String, val sizeBytes: Long)

@Serializable
data class DiskGrowthResult(
    val rootPath: String,
    val from: String,
    val to: String,
    val netDeltaBytes: Long,
    val topGrowingFolders: List<FolderGrowthEntry>,
    val topShrinkingFolders: List<FolderGrowthEntry>,
    val biggestNewFiles: List<BiggestFileEntry>,
    val timeline: List<GrowthPoint>,
)

// ---- Alerts -----------------------------------------------------------

@Serializable
enum class AlertKind {
    @SerialName("bigFile") BigFile,
    @SerialName("fastFolderGrowth") FastFolderGrowth,
    @SerialName("lowFreeSpace") LowFreeSpace,
    @SerialName("suddenDiskDrop") SuddenDiskDrop,
}

@Serializable
enum class AlertSeverity {
    @SerialName("info") Info,
    @SerialName("warning") Warning,
    @SerialName("critical") Critical,
}

@Serializable
data class AlertDto(
    val id: Long,
    val kind: AlertKind,
    val severity: AlertSeverity,
    val message: String,
    val path: String? = null,
    val occurredAt: String,
    val acknowledged: Boolean,
)

@Serializable
data class AlertsQuery(val since: String? = null, val unacknowledgedOnly: Boolean? = null, val limit: Int = 100)

// ---- Audit -----------------------------------------------------------

@Serializable
data class AuditEntry(
    val id: Long,
    val deviceId: String,
    val deviceName: String,
    val action: String,
    val target: String? = null,
    val success: Boolean,
    val reason: String? = null,
    val occurredAt: String,
)

@Serializable
data class AuditQuery(val since: String? = null, val limit: Int = 100)

// ---- Pairing -----------------------------------------------------------

@Serializable
data class PairBeginResponse(
    val pairingId: String,
    val hostCandidates: List<String>,
    val port: Int,
    val certSha256: String,
    val pinExpiresInSeconds: Int,
)

@Serializable
data class PairVerifyRequest(
    val pairingId: String,
    val pin: String,
    val deviceName: String,
    val devicePublicKeyBase64: String,
)

@Serializable
enum class PairingState {
    @SerialName("awaitingPin") AwaitingPin,
    @SerialName("pendingApproval") PendingApproval,
    @SerialName("approved") Approved,
    @SerialName("rejected") Rejected,
    @SerialName("expired") Expired,
}

@Serializable
data class PairVerifyResponse(
    val state: PairingState,
    val sessionToken: String? = null,
    val refreshToken: String? = null,
    val deviceId: String? = null,
    val expiresAt: String? = null,
)
