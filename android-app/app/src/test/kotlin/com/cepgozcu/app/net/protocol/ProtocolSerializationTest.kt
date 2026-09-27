package com.cepgozcu.app.net.protocol

import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * Round-trips a handful of protocol DTOs (including every enum) through [WireJson] and checks
 * both structural equality after decode AND the exact camelCase wire text for the DTOs the task
 * calls out specifically (FileEventDto, AlertDto, PairVerifyResponse) — this is what guards the
 * Kotlin/.NET wire contract from silently drifting (e.g. someone renaming a field, or the enum
 * serial names drifting from the .NET JsonStringEnumConverter(CamelCase) output).
 */
class ProtocolSerializationTest {

    @Test
    fun `FileEventDto round-trips and encodes kind as camelCase`() {
        val dto = FileEventDto(
            id = 42,
            kind = FileEventKind.Created,
            path = "C:/Users/jake/Downloads/file.zip",
            oldPath = null,
            sizeBytes = 1024,
            sizeDeltaBytes = 1024,
            coalescedCount = 1,
            occurredAt = "2026-09-27T10:00:00Z",
            source = "watcher",
        )
        val json = WireJson.encodeToString(FileEventDto.serializer(), dto)
        assertThat(json).contains("\"kind\":\"created\"")

        val decoded = WireJson.decodeFromString(FileEventDto.serializer(), json)
        assertThat(decoded).isEqualTo(dto)
    }

    @Test
    fun `every FileEventKind serializes to its documented camelCase name`() {
        val expected = mapOf(
            FileEventKind.Created to "created",
            FileEventKind.Grew to "grew",
            FileEventKind.Shrank to "shrank",
            FileEventKind.Modified to "modified",
            FileEventKind.Deleted to "deleted",
            FileEventKind.Renamed to "renamed",
            FileEventKind.Moved to "moved",
        )
        expected.forEach { (kind, wireName) ->
            val json = WireJson.encodeToString(FileEventKind.serializer(), kind)
            assertThat(json).isEqualTo("\"$wireName\"")
        }
    }

    @Test
    fun `AlertDto round-trips and encodes kind and severity as camelCase`() {
        val dto = AlertDto(
            id = 7,
            kind = AlertKind.FastFolderGrowth,
            severity = AlertSeverity.Critical,
            message = "Disk hızla doluyor",
            path = "D:/Projeler",
            occurredAt = "2026-09-27T09:30:00Z",
            acknowledged = false,
        )
        val json = WireJson.encodeToString(AlertDto.serializer(), dto)
        assertThat(json).contains("\"kind\":\"fastFolderGrowth\"")
        assertThat(json).contains("\"severity\":\"critical\"")

        val decoded = WireJson.decodeFromString(AlertDto.serializer(), json)
        assertThat(decoded).isEqualTo(dto)
    }

    @Test
    fun `PairVerifyResponse round-trips and encodes state as camelCase`() {
        val dto = PairVerifyResponse(
            state = PairingState.PendingApproval,
            sessionToken = null,
            refreshToken = null,
            deviceId = null,
            expiresAt = null,
        )
        val json = WireJson.encodeToString(PairVerifyResponse.serializer(), dto)
        assertThat(json).contains("\"state\":\"pendingApproval\"")

        val decoded = WireJson.decodeFromString(PairVerifyResponse.serializer(), json)
        assertThat(decoded).isEqualTo(dto)

        val approved = WireJson.encodeToString(PairingState.serializer(), PairingState.Approved)
        assertThat(approved).isEqualTo("\"approved\"")
    }

    @Test
    fun `SystemMetrics round-trips with nested cpu, memory and disk lists`() {
        val dto = SystemMetrics(
            ts = 1_700_000_000_000,
            cpu = CpuMetrics(totalPercent = 42.5, perCoreProcess = listOf(10.0, 20.0), coreCount = 8),
            memory = MemoryMetrics(totalBytes = 16_000_000_000, usedBytes = 8_000_000_000, availableBytes = 8_000_000_000),
            disks = listOf(DiskMetrics("C:", "Windows", 500_000_000_000, 100_000_000_000, 400_000_000_000, isLowSpace = false)),
            machineName = "JAKE-PC",
            osVersion = "Windows 11 Pro",
            uptime = "2.05:12:00",
        )
        val json = WireJson.encodeToString(SystemMetrics.serializer(), dto)
        val decoded = WireJson.decodeFromString(SystemMetrics.serializer(), json)
        assertThat(decoded).isEqualTo(dto)
    }

    @Test
    fun `PairBeginResponse round-trips as sent inside the QR payload`() {
        val dto = PairBeginResponse(
            pairingId = "abc-123",
            hostCandidates = listOf("192.168.1.20", "192.168.1.21"),
            port = 8443,
            certSha256 = "aa".repeat(32),
            pinExpiresInSeconds = 300,
        )
        val json = WireJson.encodeToString(PairBeginResponse.serializer(), dto)
        val decoded = WireJson.decodeFromString(PairBeginResponse.serializer(), json)
        assertThat(decoded).isEqualTo(dto)
    }
}
