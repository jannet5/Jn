using CepGozcu.Agent.Core.Data;
using CepGozcu.Agent.Core.Disk;
using CepGozcu.Agent.Core.Protocol;
using CepGozcu.Agent.Tests.TestSupport;
using Xunit;

namespace CepGozcu.Agent.Tests.Core;

public class GrowthAnalyzerTests : IDisposable
{
    private readonly TempDb _db = new();
    private readonly FolderSnapshotRepository _snapshots;
    private readonly FileEventRepository _events;
    private readonly GrowthAnalyzer _analyzer;
    private const string Root = "/root";

    public GrowthAnalyzerTests()
    {
        _snapshots = new FolderSnapshotRepository(_db.Factory);
        _events = new FileEventRepository(_db.Factory);
        _analyzer = new GrowthAnalyzer(_snapshots, _events);
    }

    public void Dispose() => _db.Dispose();

    [Fact]
    public void RanksFoldersByDeltaAndComputesRatePerHour()
    {
        var now = DateTimeOffset.UtcNow;
        var before = now.AddHours(-2); // outside the LastHour window: the "start" baseline
        var recent = now.AddMinutes(-1); // inside the window: the "end" snapshot

        // Folder A grew a lot, folder B shrank, folder C is unchanged (shouldn't appear in either list).
        Insert("/root/A", before, 100, recent, 900);
        Insert("/root/B", before, 500, recent, 50);
        Insert("/root/C", before, 42, recent, 42);
        Insert(Root, before, 642, recent, 992);

        var result = _analyzer.Analyze(new DiskGrowthQuery(Root, GrowthRange.LastHour, null, null, Top: 10));

        Assert.Equal(992 - 642, result.NetDeltaBytes);

        var growingA = Assert.Single(result.TopGrowingFolders, f => f.Path == "/root/A");
        Assert.Equal(800, growingA.DeltaBytes);
        Assert.True(growingA.BytesPerHour > 0);

        var shrinkingB = Assert.Single(result.TopShrinkingFolders, f => f.Path == "/root/B");
        Assert.Equal(-450, shrinkingB.DeltaBytes);

        Assert.DoesNotContain(result.TopGrowingFolders, f => f.Path == "/root/C");
        Assert.DoesNotContain(result.TopShrinkingFolders, f => f.Path == "/root/C");
    }

    [Fact]
    public void BiggestNewFilesComeFromRawEventsOrderedByDelta()
    {
        var now = DateTimeOffset.UtcNow;
        _events.Insert(Root, new FileEventDto(0, FileEventKind.Created, "/root/small.bin", null, 10, 10, 1, now.AddMinutes(-30), "scan"));
        _events.Insert(Root, new FileEventDto(0, FileEventKind.Created, "/root/huge.bin", null, 900_000, 900_000, 1, now.AddMinutes(-20), "scan"));
        _events.Insert(Root, new FileEventDto(0, FileEventKind.Grew, "/root/log.txt", null, 500_000, 400_000, 1, now.AddMinutes(-10), "watcher"));

        var result = _analyzer.Analyze(new DiskGrowthQuery(Root, GrowthRange.LastHour, null, null, Top: 2));

        Assert.Equal(2, result.BiggestNewFiles.Count);
        Assert.Equal("/root/huge.bin", result.BiggestNewFiles[0].Path);
        Assert.Equal("/root/log.txt", result.BiggestNewFiles[1].Path);
    }

    [Fact]
    public void NewFolderWithNoBaselineCountsAsPureGrowth()
    {
        var now = DateTimeOffset.UtcNow;
        var recent = now.AddMinutes(-1);
        // No snapshot before the window at all — folder appeared during it.
        _snapshots.Upsert(Root, "/root/NewFolder", SnapshotGranularity.FiveMinutes, recent, 12345, 3);

        var result = _analyzer.Analyze(new DiskGrowthQuery(Root, GrowthRange.LastHour, null, null, Top: 10));

        var entry = Assert.Single(result.TopGrowingFolders, f => f.Path == "/root/NewFolder");
        Assert.Equal(0, entry.SizeAtStartBytes);
        Assert.Equal(12345, entry.DeltaBytes);
    }

    private void Insert(string folder, DateTimeOffset atStart, long sizeStart, DateTimeOffset atEnd, long sizeEnd)
    {
        _snapshots.Upsert(Root, folder, SnapshotGranularity.FiveMinutes, atStart, sizeStart, 1);
        _snapshots.Upsert(Root, folder, SnapshotGranularity.FiveMinutes, atEnd, sizeEnd, 1);
    }
}
