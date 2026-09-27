using CepGozcu.Agent.Core.Data;
using CepGozcu.Agent.Core.Protocol;

namespace CepGozcu.Agent.Core.Disk;

public sealed record DiskScanSummary(string RootPath, DateTimeOffset StartedAt, long TotalSizeBytes, int TotalFileCount);

/// <summary>
/// The "ground truth" half of disk tracking: walks a root bottom-up, diffs every file against
/// <see cref="FileCacheRepository"/>, and persists both individual file_events and per-folder
/// size snapshots. Runs periodically (see DiskMonitoringService) and self-heals anything the
/// live FileSystemWatcher missed — watcher events are cheap and immediate but can be dropped
/// under load or while the agent is offline; this scan is slower but authoritative.
/// Throttled deliberately (yields every N entries) so a multi-terabyte drive doesn't pin a core.
/// </summary>
public sealed class DiskScanner
{
    private readonly NoiseFilter _noise;
    private readonly FileCacheRepository _cache;
    private readonly FileEventRepository _events;
    private readonly FolderSnapshotRepository _snapshots;
    private readonly int _maxTrackedDepth;
    private readonly int _yieldEveryNEntries;
    private readonly TimeSpan _yieldDelay;

    public DiskScanner(
        NoiseFilter noise,
        FileCacheRepository cache,
        FileEventRepository events,
        FolderSnapshotRepository snapshots,
        int maxTrackedDepth = 4,
        int yieldEveryNEntries = 500,
        TimeSpan? yieldDelay = null)
    {
        _noise = noise;
        _cache = cache;
        _events = events;
        _snapshots = snapshots;
        _maxTrackedDepth = maxTrackedDepth;
        _yieldEveryNEntries = yieldEveryNEntries;
        _yieldDelay = yieldDelay ?? TimeSpan.FromMilliseconds(20);
    }

    private sealed class ScanContext
    {
        public required string RootPath;
        public required long ScanId;
        public required DateTimeOffset BucketStart;
        public required DateTimeOffset LastScanTime;
        public required Action<FileEventDto> OnEvent;
        public int ProcessedSinceYield;
    }

    public async Task<DiskScanSummary> ScanRootAsync(
        string rootPath,
        DateTimeOffset lastScanTime,
        Action<FileEventDto> onEvent,
        CancellationToken ct)
    {
        var startedAt = DateTimeOffset.UtcNow;
        var ctx = new ScanContext
        {
            RootPath = rootPath,
            ScanId = startedAt.ToUnixTimeMilliseconds(),
            BucketStart = AlignToFiveMinutes(startedAt),
            LastScanTime = lastScanTime,
            OnEvent = onEvent,
        };

        var (size, fileCount) = await WalkAsync(rootPath, rootPath, depth: 0, trackEvents: true, ctx, ct);

        foreach (var stalePath in _cache.FindStale(rootPath, ctx.ScanId))
        {
            _cache.Delete(stalePath);
            if (!_events.HasEventSince(stalePath, lastScanTime))
            {
                var evt = new FileEventDto(0, FileEventKind.Deleted, stalePath, null, 0, 0, 1, startedAt, "scan");
                _events.Insert(rootPath, evt);
                onEvent(evt);
            }
        }

        return new DiskScanSummary(rootPath, startedAt, size, fileCount);
    }

    private async Task<(long size, int fileCount)> WalkAsync(
        string rootPath, string currentPath, int depth, bool trackEvents, ScanContext ctx, CancellationToken ct)
    {
        long totalSize = 0;
        int totalFiles = 0;
        IEnumerable<FileSystemInfo> entries;
        try
        {
            entries = new DirectoryInfo(currentPath).EnumerateFileSystemInfos();
        }
        catch (Exception ex) when (ex is UnauthorizedAccessException or DirectoryNotFoundException or IOException)
        {
            return (0, 0); // permission-denied / vanished mid-scan — not fatal to the overall scan
        }

        foreach (var entry in entries)
        {
            ct.ThrowIfCancellationRequested();

            if ((entry.Attributes & FileAttributes.ReparsePoint) != 0)
            {
                continue; // never follow symlinks/junctions — avoids cycles and double-counting
            }

            var isNoise = trackEvents && _noise.IsNoise(entry.FullName);
            var childTrackEvents = trackEvents && !isNoise;

            if (entry is DirectoryInfo)
            {
                var (childSize, childFiles) = await WalkAsync(rootPath, entry.FullName, depth + 1, childTrackEvents, ctx, ct);
                totalSize += childSize;
                totalFiles += childFiles;

                if (childTrackEvents && depth + 1 <= _maxTrackedDepth)
                {
                    _snapshots.Upsert(rootPath, entry.FullName, SnapshotGranularity.FiveMinutes, ctx.BucketStart, childSize, childFiles);
                }
            }
            else if (entry is FileInfo file)
            {
                long length;
                DateTime lastWriteUtc;
                try
                {
                    length = file.Length;
                    lastWriteUtc = file.LastWriteTimeUtc;
                }
                catch (Exception ex) when (ex is IOException or UnauthorizedAccessException)
                {
                    continue;
                }

                totalSize += length;
                totalFiles += 1;

                if (childTrackEvents)
                {
                    ProcessFile(rootPath, file.FullName, length, lastWriteUtc, ctx);
                }
                else
                {
                    // Still record presence so a later "un-noise" (excludes changed) doesn't
                    // treat this file as freshly deleted.
                    _cache.Upsert(rootPath, file.FullName, length, lastWriteUtc, ctx.ScanId);
                }

                if (++ctx.ProcessedSinceYield >= _yieldEveryNEntries)
                {
                    ctx.ProcessedSinceYield = 0;
                    await Task.Delay(_yieldDelay, ct);
                }
            }
        }

        if (depth <= _maxTrackedDepth)
        {
            _snapshots.Upsert(rootPath, currentPath, SnapshotGranularity.FiveMinutes, ctx.BucketStart, totalSize, totalFiles);
        }

        return (totalSize, totalFiles);
    }

    private void ProcessFile(string rootPath, string path, long length, DateTime lastWriteUtc, ScanContext ctx)
    {
        var cached = _cache.Get(path);
        _cache.Upsert(rootPath, path, length, lastWriteUtc, ctx.ScanId);

        if (cached is null)
        {
            EmitIfNotDuplicate(rootPath, ctx, path, new FileEventDto(0, FileEventKind.Created, path, null, length, length, 1, ctx.BucketStart, "scan"));
            return;
        }

        if (cached.SizeBytes == length && cached.LastWriteUtc == lastWriteUtc)
        {
            return; // unchanged — the overwhelmingly common case, no event, no write beyond the upsert above
        }

        var delta = length - cached.SizeBytes;
        var kind = delta switch
        {
            > 0 => FileEventKind.Grew,
            < 0 => FileEventKind.Shrank,
            _ => FileEventKind.Modified,
        };
        // Timestamped at the scan's (floored) bucket start, not DateTimeOffset.UtcNow: it must stay
        // <= this scan's own StartedAt, because StartedAt becomes next cycle's dedup cutoff
        // (ctx.LastScanTime) — an event timestamped later than that would make the *next* scan
        // think this file's *next* change was already reported, and silently swallow it.
        EmitIfNotDuplicate(rootPath, ctx, path, new FileEventDto(0, kind, path, null, length, delta, 1, ctx.BucketStart, "scan"));
    }

    private void EmitIfNotDuplicate(string rootPath, ScanContext ctx, string path, FileEventDto evt)
    {
        if (_events.HasEventSince(path, ctx.LastScanTime))
        {
            return; // the live watcher already reported this change — don't double-log it
        }
        _events.Insert(rootPath, evt);
        ctx.OnEvent(evt);
    }

    public static DateTimeOffset AlignToFiveMinutes(DateTimeOffset t)
    {
        var minutes = t.Minute - (t.Minute % 5);
        return new DateTimeOffset(t.Year, t.Month, t.Day, t.Hour, minutes, 0, t.Offset);
    }
}
