namespace CepGozcu.Agent.Core.Protocol;

/// <summary>
/// Wire envelope for every WebSocket text frame, in both directions.
/// One JSON object per frame (no newline framing needed — OkHttp/Kestrel already frame messages).
/// Mirrored on the Android side by `Envelope` in `net/Protocol.kt`; keep the two in sync.
/// </summary>
public sealed class Envelope
{
    /// <summary>Protocol version, bumped on breaking wire changes.</summary>
    public int V { get; set; } = 1;

    /// <summary>Correlates a request with its response. Null for server-initiated pushes.</summary>
    public string? Id { get; set; }

    /// <summary>One of <see cref="MessageType"/>.</summary>
    public required string Type { get; set; }

    /// <summary>Unix millis, set by the sender.</summary>
    public long Ts { get; set; }

    /// <summary>Raw JSON payload, shape depends on <see cref="Type"/>. Deserialized on demand.</summary>
    public System.Text.Json.JsonElement? Payload { get; set; }

    /// <summary>Present only on responses that failed.</summary>
    public ErrorPayload? Error { get; set; }
}

public sealed class ErrorPayload
{
    public required string Code { get; set; }
    public required string Message { get; set; }
}

/// <summary>
/// All message type strings. Request types are answered by a response of the same
/// Type with matching Id; push types (Alert, FileEvent, PairingStatus, ...) have no request.
/// </summary>
public static class MessageType
{
    // Session
    public const string Ping = "ping";
    public const string Pong = "pong";
    public const string Unauthorized = "unauthorized";

    // Pairing (only reachable before/without a session token)
    public const string PairBegin = "pair.begin";
    public const string PairVerify = "pair.verify";
    public const string PairStatus = "pair.status";   // push: pending -> approved/rejected/expired
    public const string PairApprove = "pair.approve"; // local-admin only, not from phone
    public const string PairReject = "pair.reject";   // local-admin only

    // Metrics
    public const string MetricsGet = "metrics.get";
    public const string MetricsUpdate = "metrics.update"; // push, periodic

    // Processes
    public const string ProcessesList = "processes.list";
    public const string ProcessKill = "process.kill";

    // App launcher
    public const string AppsList = "apps.list";
    public const string AppLaunch = "app.launch";

    // Disk
    public const string DiskOverview = "disk.overview";
    public const string DiskEventsQuery = "disk.events.query";
    public const string DiskGrowthQuery = "disk.growth.query";
    public const string DiskFileEvent = "disk.fileEvent"; // push

    // Alerts
    public const string AlertsQuery = "alerts.query";
    public const string AlertPush = "alert.push"; // push

    // Audit
    public const string AuditQuery = "audit.query";

    // Device / session management
    public const string DeviceUnpair = "device.unpair";
    public const string DeviceRevoked = "device.revoked"; // push, server -> client, forces disconnect
}
