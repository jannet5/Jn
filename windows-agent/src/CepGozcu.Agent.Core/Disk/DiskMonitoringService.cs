using CepGozcu.Agent.Core.Data;
using CepGozcu.Agent.Core.Metrics;
using CepGozcu.Agent.Core.Protocol;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;

namespace CepGozcu.Agent.Core.Disk;

/// <summary>
/// Owns the whole disk-tracking pipeline for the process lifetime: starts the live watcher,
/// runs the periodic reconciling scan per enabled root (one root at a time — deliberately
/// sequential so a big drive doesn't compete with others for the same disk I/O), downsamples
/// old snapshots, and feeds both file events and disk metrics into the alert engine.
/// </summary>
public sealed class DiskMonitoringService : BackgroundService
{
    private readonly WatchedRootsStore _roots;
    private readonly FileSystemWatcherManager _watcherManager;
    private readonly DiskScanner _scanner;
    private readonly GrowthAnalyzer _growth;
    private readonly AlertEngine _alerts;
    private readonly FolderSnapshotRepository _snapshots;
    private readonly ISystemMetricsProvider _metricsProvider;
    private readonly ILogger<DiskMonitoringService> _logger;
    private readonly Action<FileEventDto> _publishFileEvent;
    private readonly TimeSpan _scanInterval;
    private readonly Dictionary<string, DateTimeOffset> _lastScanTime = new();
    private DateTimeOffset _lastRollup = DateTimeOffset.MinValue;

    public DiskMonitoringService(
        WatchedRootsStore roots,
        FileSystemWatcherManager watcherManager,
        DiskScanner scanner,
        GrowthAnalyzer growth,
        AlertEngine alerts,
        FolderSnapshotRepository snapshots,
        ISystemMetricsProvider metricsProvider,
        Action<FileEventDto> publishFileEvent,
        ILogger<DiskMonitoringService> logger,
        TimeSpan? scanInterval = null)
    {
        _roots = roots;
        _watcherManager = watcherManager;
        _scanner = scanner;
        _growth = growth;
        _alerts = alerts;
        _snapshots = snapshots;
        _metricsProvider = metricsProvider;
        _publishFileEvent = publishFileEvent;
        _logger = logger;
        _scanInterval = scanInterval ?? TimeSpan.FromMinutes(5);
    }

    protected override async Task ExecuteAsync(CancellationToken stoppingToken)
    {
        _watcherManager.Start(_roots.EnabledPaths());

        // Prime a first pass immediately so a freshly-installed agent has data to show right away.
        await RunCycleAsync(stoppingToken);

        using var timer = new PeriodicTimer(_scanInterval);
        while (!stoppingToken.IsCancellationRequested && await timer.WaitForNextTickAsync(stoppingToken))
        {
            await RunCycleAsync(stoppingToken);
        }
    }

    private async Task RunCycleAsync(CancellationToken ct)
    {
        foreach (var root in _roots.EnabledPaths())
        {
            if (ct.IsCancellationRequested) break;
            try
            {
                var lastScan = _lastScanTime.GetValueOrDefault(root, DateTimeOffset.UtcNow.AddDays(-1));
                var summary = await _scanner.ScanRootAsync(root, lastScan, OnFileEvent, ct);
                _lastScanTime[root] = summary.StartedAt;

                var growth = _growth.Analyze(new DiskGrowthQuery(root, GrowthRange.LastHour, null, null, Top: 10));
                _alerts.OnGrowthResult(growth);
            }
            catch (OperationCanceledException)
            {
                throw;
            }
            catch (Exception ex) when (ex is IOException or UnauthorizedAccessException)
            {
                _logger.LogWarning(ex, "Disk scan failed for root {Root}", root);
            }
        }

        _alerts.OnDiskMetrics(_metricsProvider.GetMetrics().Disks);
        MaybeRollup();
    }

    // Scan-sourced events flow through the same publish callback as watcher-sourced ones
    // (see composition root), which already runs them past the alert engine before broadcasting.
    private void OnFileEvent(FileEventDto evt) => _publishFileEvent(evt);

    private void MaybeRollup()
    {
        var now = DateTimeOffset.UtcNow;
        if (now - _lastRollup < TimeSpan.FromHours(1)) return;
        _lastRollup = now;

        // Hourly buckets are cheap to keep — just retain a bounded window per granularity.
        _snapshots.DeleteOlderThan(SnapshotGranularity.FiveMinutes, now.AddDays(-2));
        _snapshots.DeleteOlderThan(SnapshotGranularity.Hourly, now.AddDays(-60));
        _snapshots.DeleteOlderThan(SnapshotGranularity.Daily, now.AddYears(-2));

        RollupInto(SnapshotGranularity.FiveMinutes, SnapshotGranularity.Hourly, TimeSpan.FromHours(1));
        RollupInto(SnapshotGranularity.Hourly, SnapshotGranularity.Daily, TimeSpan.FromDays(1));
    }

    private void RollupInto(SnapshotGranularity fromGranularity, SnapshotGranularity intoGranularity, TimeSpan bucketSize)
    {
        var now = DateTimeOffset.UtcNow;
        var bucketStart = AlignTo(now, bucketSize);
        var windowStart = bucketStart - bucketSize;
        foreach (var folder in _snapshots.DistinctFolders(fromGranularity))
        {
            var points = _snapshots.Timeline(folder, fromGranularity, windowStart, bucketStart);
            if (points.Count == 0) continue;
            var last = points[^1];
            var root = _roots.EnabledPaths().FirstOrDefault(r => folder.StartsWith(r, StringComparison.OrdinalIgnoreCase)) ?? folder;
            _snapshots.Upsert(root, folder, intoGranularity, windowStart, last.SizeBytes, 0);
        }
    }

    private static DateTimeOffset AlignTo(DateTimeOffset t, TimeSpan bucket)
    {
        if (bucket == TimeSpan.FromDays(1)) return new DateTimeOffset(t.Year, t.Month, t.Day, 0, 0, 0, t.Offset);
        return new DateTimeOffset(t.Year, t.Month, t.Day, t.Hour, 0, 0, t.Offset);
    }

    // Deliberately no Dispose() override here: FileSystemWatcherManager is its own DI-registered
    // singleton (see composition root), so the container disposes it independently — disposing
    // it again from here would just be redundant (FileSystemWatcherManager.Dispose() is
    // idempotent, but owning what you don't exclusively hold is still the wrong shape).
}
