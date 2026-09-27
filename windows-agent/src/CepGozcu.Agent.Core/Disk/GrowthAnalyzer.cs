using CepGozcu.Agent.Core.Data;
using CepGozcu.Agent.Core.Protocol;

namespace CepGozcu.Agent.Core.Disk;

/// <summary>
/// Answers "what filled up my disk?" from the snapshot history: which folders grew or shrank
/// the most in a time window, the biggest individual files added/grown, and a size-over-time
/// timeline for the root. This is read-only analytics over data DiskScanner/FileSystemWatcherManager
/// already collected — no scanning happens here.
/// </summary>
public sealed class GrowthAnalyzer
{
    private readonly FolderSnapshotRepository _snapshots;
    private readonly FileEventRepository _events;

    public GrowthAnalyzer(FolderSnapshotRepository snapshots, FileEventRepository events)
    {
        _snapshots = snapshots;
        _events = events;
    }

    public DiskGrowthResult Analyze(DiskGrowthQuery query)
    {
        var to = query.CustomTo ?? DateTimeOffset.UtcNow;
        var from = query.Range switch
        {
            GrowthRange.LastHour => to.AddHours(-1),
            GrowthRange.LastDay => to.AddDays(-1),
            GrowthRange.LastWeek => to.AddDays(-7),
            GrowthRange.LastMonth => to.AddDays(-30),
            GrowthRange.Custom => query.CustomFrom ?? to.AddDays(-1),
            _ => to.AddDays(-1),
        };

        var granularity = GranularityFor(to - from);

        var atFrom = _snapshots.SizesAsOf(query.RootPath, granularity, from);
        var atTo = _snapshots.SizesAsOf(query.RootPath, granularity, to);
        var hours = Math.Max((to - from).TotalHours, 1.0 / 60);

        var allFolders = atFrom.Keys.Union(atTo.Keys);
        var deltas = new List<(string Path, long SizeAtStart, long SizeAtEnd)>();
        foreach (var folder in allFolders)
        {
            var start = atFrom.TryGetValue(folder, out var s) ? s.SizeBytes : 0;
            var end = atTo.TryGetValue(folder, out var e) ? e.SizeBytes : 0;
            deltas.Add((folder, start, end));
        }

        var growing = deltas
            .Where(d => d.SizeAtEnd - d.SizeAtStart > 0)
            .OrderByDescending(d => d.SizeAtEnd - d.SizeAtStart)
            .Take(query.Top)
            .Select(d => ToEntry(d, from, to, hours))
            .ToList();

        var shrinking = deltas
            .Where(d => d.SizeAtEnd - d.SizeAtStart < 0)
            .OrderBy(d => d.SizeAtEnd - d.SizeAtStart)
            .Take(query.Top)
            .Select(d => ToEntry(d, from, to, hours))
            .ToList();

        var biggestFiles = _events.QueryBiggest(query.RootPath, from, to, query.Top);

        var rootStart = atFrom.TryGetValue(query.RootPath, out var rs) ? rs.SizeBytes : 0;
        var rootEnd = atTo.TryGetValue(query.RootPath, out var re) ? re.SizeBytes : 0;
        var timeline = _snapshots.Timeline(query.RootPath, granularity, from, to);

        return new DiskGrowthResult(
            RootPath: query.RootPath,
            From: from,
            To: to,
            NetDeltaBytes: rootEnd - rootStart,
            TopGrowingFolders: growing,
            TopShrinkingFolders: shrinking,
            BiggestNewFiles: biggestFiles,
            Timeline: timeline);
    }

    private FolderGrowthEntry ToEntry((string Path, long SizeAtStart, long SizeAtEnd) d, DateTimeOffset from, DateTimeOffset to, double hours)
    {
        var (created, deleted) = _events.CountCreatedDeleted(d.Path, from, to);
        var delta = d.SizeAtEnd - d.SizeAtStart;
        return new FolderGrowthEntry(
            Path: d.Path,
            SizeAtStartBytes: d.SizeAtStart,
            SizeAtEndBytes: d.SizeAtEnd,
            DeltaBytes: delta,
            BytesPerHour: delta / hours,
            NewFileCount: created,
            DeletedFileCount: deleted);
    }

    private static SnapshotGranularity GranularityFor(TimeSpan span)
    {
        if (span <= TimeSpan.FromDays(2)) return SnapshotGranularity.FiveMinutes;
        if (span <= TimeSpan.FromDays(60)) return SnapshotGranularity.Hourly;
        return SnapshotGranularity.Daily;
    }
}
