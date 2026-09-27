using System.Collections.Concurrent;
using CepGozcu.Agent.Core.Data;
using CepGozcu.Agent.Core.Protocol;

namespace CepGozcu.Agent.Core.Disk;

/// <summary>
/// The "live" half of disk tracking. Wraps one <see cref="FileSystemWatcher"/> per watched root
/// and coalesces bursts of raw OS notifications (a single "save" in many apps fires several
/// Changed events) into one file_event per quiet period, so the history reads like "app.log grew
/// by 40 MB" instead of forty separate one-line entries. Never authoritative on its own — see
/// <see cref="DiskScanner"/> for the periodic reconciliation that catches anything this misses
/// (buffer overflow, the agent being offline, etc).
/// </summary>
public sealed class FileSystemWatcherManager : IDisposable
{
    private static readonly TimeSpan QuietPeriod = TimeSpan.FromSeconds(3);
    private static readonly TimeSpan MaxHold = TimeSpan.FromSeconds(15);

    private sealed class PendingChange
    {
        public required string RootPath;
        public required string Path;
        public string? OldPath;
        public FileEventKind Kind;
        public long LastKnownSizeBytes;
        public long BaselineSizeBytes;
        public int CoalescedCount = 1;
        public DateTimeOffset FirstSeenUtc;
        public DateTimeOffset LastUpdatedUtc;
    }

    private readonly NoiseFilter _noise;
    private readonly FileEventRepository _events;
    private readonly FileCacheRepository _cache;
    private readonly Action<FileEventDto> _onEvent;
    private readonly Action<string> _onOverflow;
    private readonly ConcurrentDictionary<string, PendingChange> _pending = new();
    private readonly ConcurrentDictionary<string, long> _sessionBaseline = new();
    private readonly List<FileSystemWatcher> _watchers = new();
    private readonly PeriodicTimer _flushTimer = new(TimeSpan.FromSeconds(1));
    private readonly CancellationTokenSource _cts = new();
    private Task? _flushLoop;

    public FileSystemWatcherManager(
        NoiseFilter noise,
        FileEventRepository events,
        FileCacheRepository cache,
        Action<FileEventDto> onEvent,
        Action<string> onOverflow)
    {
        _noise = noise;
        _events = events;
        _cache = cache;
        _onEvent = onEvent;
        _onOverflow = onOverflow;
    }

    public void Start(IEnumerable<string> rootPaths)
    {
        foreach (var root in rootPaths)
        {
            if (!Directory.Exists(root)) continue;

            var watcher = new FileSystemWatcher(root)
            {
                IncludeSubdirectories = true,
                InternalBufferSize = 64 * 1024,
                NotifyFilter = NotifyFilters.FileName | NotifyFilters.DirectoryName | NotifyFilters.LastWrite | NotifyFilters.Size,
            };
            watcher.Created += (_, e) => OnRaw(root, e.FullPath, FileEventKind.Created, null);
            watcher.Changed += (_, e) => OnRaw(root, e.FullPath, FileEventKind.Modified, null);
            watcher.Deleted += (_, e) => OnRaw(root, e.FullPath, FileEventKind.Deleted, null);
            watcher.Renamed += (_, e) =>
            {
                var kind = string.Equals(Path.GetDirectoryName(e.OldFullPath), Path.GetDirectoryName(e.FullPath), StringComparison.OrdinalIgnoreCase)
                    ? FileEventKind.Renamed
                    : FileEventKind.Moved;
                OnRaw(root, e.FullPath, kind, e.OldFullPath);
            };
            watcher.Error += (_, _) => _onOverflow(root);
            watcher.EnableRaisingEvents = true;
            _watchers.Add(watcher);
        }

        _flushLoop = Task.Run(() => FlushLoopAsync(_cts.Token));
    }

    private void OnRaw(string rootPath, string fullPath, FileEventKind kind, string? oldPath)
    {
        if (_noise.IsNoise(fullPath)) return;
        if (Directory.Exists(fullPath)) return; // directory-level create/delete noise; folder totals come from the scanner

        long size = 0;
        if (kind != FileEventKind.Deleted)
        {
            try { size = new FileInfo(fullPath).Length; }
            catch (IOException) { /* deleted again already, or transient lock — next scan will settle it */ }
        }

        var now = DateTimeOffset.UtcNow;
        _pending.AddOrUpdate(
            fullPath,
            _ => new PendingChange
            {
                RootPath = rootPath,
                Path = fullPath,
                OldPath = oldPath,
                Kind = kind,
                LastKnownSizeBytes = size,
                BaselineSizeBytes = ResolveBaseline(fullPath, size),
                FirstSeenUtc = now,
                LastUpdatedUtc = now,
            },
            (_, existing) =>
            {
                existing.LastKnownSizeBytes = size;
                existing.LastUpdatedUtc = now;
                existing.CoalescedCount++;
                // Escalate kind: a delete always wins (final state), otherwise keep the most descriptive kind.
                if (kind == FileEventKind.Deleted) existing.Kind = FileEventKind.Deleted;
                else if (existing.Kind == FileEventKind.Deleted) { /* a recreate after delete — keep Deleted->Created story simple by leaving as-is */ }
                if (oldPath is not null) existing.OldPath = oldPath;
                return existing;
            });
    }

    private long ResolveBaseline(string path, long currentSize)
    {
        if (_sessionBaseline.TryGetValue(path, out var known)) return known;
        var cached = _cache.Get(path);
        return cached?.SizeBytes ?? currentSize;
    }

    private async Task FlushLoopAsync(CancellationToken ct)
    {
        try
        {
            while (await _flushTimer.WaitForNextTickAsync(ct))
            {
                var now = DateTimeOffset.UtcNow;
                foreach (var kvp in _pending)
                {
                    var change = kvp.Value;
                    var quiet = now - change.LastUpdatedUtc >= QuietPeriod;
                    var tooOld = now - change.FirstSeenUtc >= MaxHold;
                    if (!quiet && !tooOld) continue;

                    if (_pending.TryRemove(kvp.Key, out _))
                    {
                        Flush(change);
                    }
                }
            }
        }
        catch (OperationCanceledException)
        {
            // shutting down
        }
    }

    private void Flush(PendingChange change)
    {
        var delta = change.Kind == FileEventKind.Deleted ? 0 : change.LastKnownSizeBytes - change.BaselineSizeBytes;
        var effectiveKind = change.Kind switch
        {
            FileEventKind.Deleted => FileEventKind.Deleted,
            FileEventKind.Created => FileEventKind.Created,
            FileEventKind.Renamed => FileEventKind.Renamed,
            FileEventKind.Moved => FileEventKind.Moved,
            _ when delta > 0 => FileEventKind.Grew,
            _ when delta < 0 => FileEventKind.Shrank,
            _ => FileEventKind.Modified,
        };

        var evt = new FileEventDto(
            Id: 0,
            Kind: effectiveKind,
            Path: change.Path,
            OldPath: change.OldPath,
            SizeBytes: change.LastKnownSizeBytes,
            SizeDeltaBytes: change.Kind == FileEventKind.Created ? change.LastKnownSizeBytes : delta,
            CoalescedCount: change.CoalescedCount,
            OccurredAt: change.LastUpdatedUtc,
            Source: "watcher");

        _events.Insert(change.RootPath, evt);
        _sessionBaseline[change.Path] = change.Kind == FileEventKind.Deleted ? 0 : change.LastKnownSizeBytes;
        if (change.Kind == FileEventKind.Deleted) _sessionBaseline.TryRemove(change.Path, out _);
        _onEvent(evt);
    }

    private int _disposed;

    public void Dispose()
    {
        if (Interlocked.Exchange(ref _disposed, 1) != 0) return; // idempotent: DI and any explicit caller may both dispose the shared singleton
        _cts.Cancel();
        try { _flushLoop?.Wait(TimeSpan.FromSeconds(2)); } catch (AggregateException) { }
        foreach (var w in _watchers)
        {
            w.EnableRaisingEvents = false;
            w.Dispose();
        }
        _flushTimer.Dispose();
        _cts.Dispose();
    }
}
