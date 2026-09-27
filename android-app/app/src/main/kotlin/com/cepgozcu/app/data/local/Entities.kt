package com.cepgozcu.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cepgozcu.app.net.protocol.AlertDto
import com.cepgozcu.app.net.protocol.AlertKind
import com.cepgozcu.app.net.protocol.AlertSeverity
import com.cepgozcu.app.net.protocol.AuditEntry
import com.cepgozcu.app.net.protocol.FileEventDto
import com.cepgozcu.app.net.protocol.FileEventKind

/**
 * Small local cache so the Disk history / Alerts / Audit screens can show real recent data
 * immediately on cold start or while briefly offline, instead of a blank screen — reconciled
 * with live data once connected. Processes/apps/metrics are intentionally NOT cached here (see
 * task notes): they are always-live and stale values would misrepresent current system state.
 * Each table is trimmed to the most recent [MAX_ROWS_PER_TABLE] rows after every insert.
 */
const val MAX_ROWS_PER_TABLE = 500

@Entity(tableName = "file_events")
data class FileEventEntity(
    @PrimaryKey val id: Long,
    val kind: FileEventKind,
    val path: String,
    val oldPath: String?,
    val sizeBytes: Long,
    val sizeDeltaBytes: Long,
    val coalescedCount: Int,
    val occurredAt: String,
    val source: String,
) {
    fun toDto(): FileEventDto = FileEventDto(id, kind, path, oldPath, sizeBytes, sizeDeltaBytes, coalescedCount, occurredAt, source)

    companion object {
        fun fromDto(dto: FileEventDto): FileEventEntity = FileEventEntity(
            dto.id, dto.kind, dto.path, dto.oldPath, dto.sizeBytes, dto.sizeDeltaBytes,
            dto.coalescedCount, dto.occurredAt, dto.source,
        )
    }
}

@Entity(tableName = "alerts")
data class AlertEntity(
    @PrimaryKey val id: Long,
    val kind: AlertKind,
    val severity: AlertSeverity,
    val message: String,
    val path: String?,
    val occurredAt: String,
    val acknowledged: Boolean,
) {
    fun toDto(): AlertDto = AlertDto(id, kind, severity, message, path, occurredAt, acknowledged)

    companion object {
        fun fromDto(dto: AlertDto): AlertEntity = AlertEntity(dto.id, dto.kind, dto.severity, dto.message, dto.path, dto.occurredAt, dto.acknowledged)
    }
}

@Entity(tableName = "audit_entries")
data class AuditEntity(
    @PrimaryKey val id: Long,
    val deviceId: String,
    val deviceName: String,
    val action: String,
    val target: String?,
    val success: Boolean,
    val reason: String?,
    val occurredAt: String,
) {
    fun toDto(): AuditEntry = AuditEntry(id, deviceId, deviceName, action, target, success, reason, occurredAt)

    companion object {
        fun fromDto(dto: AuditEntry): AuditEntity = AuditEntity(dto.id, dto.deviceId, dto.deviceName, dto.action, dto.target, dto.success, dto.reason, dto.occurredAt)
    }
}
