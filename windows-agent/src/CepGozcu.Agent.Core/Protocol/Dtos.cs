namespace CepGozcu.Agent.Core.Protocol;

// ---- Metrics -----------------------------------------------------------

public sealed record CpuMetrics(double TotalPercent, double[] PerCoreProcess, int CoreCount);

public sealed record MemoryMetrics(long TotalBytes, long UsedBytes, long AvailableBytes);

public sealed record DiskMetrics(
    string Name,
    string VolumeLabel,
    long TotalBytes,
    long FreeBytes,
    long UsedBytes,
    bool IsLowSpace);

public sealed record SystemMetrics(
    long Ts,
    CpuMetrics Cpu,
    MemoryMetrics Memory,
    IReadOnlyList<DiskMetrics> Disks,
    string MachineName,
    string OsVersion,
    TimeSpan Uptime);

// ---- Processes -----------------------------------------------------------

public sealed record ProcessInfo(
    int Pid,
    string Name,
    string? DisplayName,
    double CpuPercent,
    long WorkingSetBytes,
    bool IsCritical,
    bool CanKill,
    DateTimeOffset? StartedAt);

public sealed record ProcessKillRequest(int Pid, bool Confirm);

public sealed record ProcessKillResult(int Pid, bool Success, string? Reason);

// ---- App launcher -----------------------------------------------------------

public sealed record AllowedApp(string Id, string DisplayName, string Path, string? IconHint);

public sealed record AppLaunchRequest(string AppId);

public sealed record AppLaunchResult(string AppId, bool Success, string? Reason, int? Pid);

// ---- Disk overview -----------------------------------------------------------

public sealed record DiskOverview(IReadOnlyList<DiskMetrics> Disks, IReadOnlyList<WatchedRootStatus> WatchedRoots);

public sealed record WatchedRootStatus(string Path, bool Enabled, DateTimeOffset? LastFullScanAt, long IndexedFileCount);

// ---- Disk events / history -----------------------------------------------------------

public enum FileEventKind { Created, Grew, Shrank, Modified, Deleted, Renamed, Moved }

public sealed record FileEventDto(
    long Id,
    FileEventKind Kind,
    string Path,
    string? OldPath,
    long SizeBytes,
    long SizeDeltaBytes,
    int CoalescedCount,
    DateTimeOffset OccurredAt,
    string Source); // "watcher" | "scan"

public sealed record DiskEventsQuery(
    string? RootPath,
    DateTimeOffset? Since,
    DateTimeOffset? Until,
    FileEventKind[]? Kinds,
    long? Cursor,
    int Limit = 100);

public sealed record DiskEventsPage(IReadOnlyList<FileEventDto> Items, long? NextCursor);

// ---- Growth analytics -----------------------------------------------------------

public enum GrowthRange { LastHour, LastDay, LastWeek, LastMonth, Custom }

public sealed record DiskGrowthQuery(string RootPath, GrowthRange Range, DateTimeOffset? CustomFrom, DateTimeOffset? CustomTo, int Top = 20);

public sealed record FolderGrowthEntry(
    string Path,
    long SizeAtStartBytes,
    long SizeAtEndBytes,
    long DeltaBytes,
    double BytesPerHour,
    int NewFileCount,
    int DeletedFileCount);

public sealed record BiggestFileEntry(string Path, long SizeBytes, DateTimeOffset SeenAt, FileEventKind Kind);

public sealed record DiskGrowthResult(
    string RootPath,
    DateTimeOffset From,
    DateTimeOffset To,
    long NetDeltaBytes,
    IReadOnlyList<FolderGrowthEntry> TopGrowingFolders,
    IReadOnlyList<FolderGrowthEntry> TopShrinkingFolders,
    IReadOnlyList<BiggestFileEntry> BiggestNewFiles,
    IReadOnlyList<GrowthPoint> Timeline);

public sealed record GrowthPoint(DateTimeOffset Bucket, long SizeBytes);

// ---- Alerts -----------------------------------------------------------

public enum AlertKind { BigFile, FastFolderGrowth, LowFreeSpace, SuddenDiskDrop }
public enum AlertSeverity { Info, Warning, Critical }

public sealed record AlertDto(
    long Id,
    AlertKind Kind,
    AlertSeverity Severity,
    string Message,
    string? Path,
    DateTimeOffset OccurredAt,
    bool Acknowledged);

public sealed record AlertsQuery(DateTimeOffset? Since, bool? UnacknowledgedOnly, int Limit = 100);

// ---- Audit -----------------------------------------------------------

public sealed record AuditEntry(
    long Id,
    string DeviceId,
    string DeviceName,
    string Action,
    string? Target,
    bool Success,
    string? Reason,
    DateTimeOffset OccurredAt);

public sealed record AuditQuery(DateTimeOffset? Since, int Limit = 100);

// ---- Pairing -----------------------------------------------------------

public sealed record PairBeginResponse(string PairingId, string[] HostCandidates, int Port, string CertSha256, int PinExpiresInSeconds);

public sealed record PairVerifyRequest(string PairingId, string Pin, string DeviceName, string DevicePublicKeyBase64);

public enum PairingState { AwaitingPin, PendingApproval, Approved, Rejected, Expired }

public sealed record PairVerifyResponse(PairingState State, string? SessionToken, string? RefreshToken, string? DeviceId, DateTimeOffset? ExpiresAt);
