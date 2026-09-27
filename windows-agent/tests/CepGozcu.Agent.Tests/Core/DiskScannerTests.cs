using CepGozcu.Agent.Core.Data;
using CepGozcu.Agent.Core.Disk;
using CepGozcu.Agent.Core.Protocol;
using CepGozcu.Agent.Tests.TestSupport;
using Xunit;

namespace CepGozcu.Agent.Tests.Core;

/// <summary>
/// Exercises DiskScanner against a real directory on disk — no fakes. This is the periodic
/// "ground truth" reconciliation pass, so these tests genuinely create/grow/rename/delete
/// files and assert on what the scanner recorded.
/// </summary>
public class DiskScannerTests : IDisposable
{
    private readonly TempDb _db = new();
    private readonly TempDirectory _dir = new();
    private readonly FileCacheRepository _cache;
    private readonly FileEventRepository _events;
    private readonly FolderSnapshotRepository _snapshots;
    private readonly DiskScanner _scanner;

    public DiskScannerTests()
    {
        _cache = new FileCacheRepository(_db.Factory);
        _events = new FileEventRepository(_db.Factory);
        _snapshots = new FolderSnapshotRepository(_db.Factory);
        _scanner = new DiskScanner(new NoiseFilter(), _cache, _events, _snapshots, maxTrackedDepth: 10, yieldEveryNEntries: 100_000);
    }

    public void Dispose()
    {
        _db.Dispose();
        _dir.Dispose();
    }

    [Fact]
    public async Task FirstScanReportsEveryFileAsCreated()
    {
        File.WriteAllText(Path.Combine(_dir.Path, "a.txt"), "hello");
        File.WriteAllText(Path.Combine(_dir.Path, "b.txt"), "world!!");

        var events = new List<FileEventDto>();
        var summary = await _scanner.ScanRootAsync(_dir.Path, DateTimeOffset.UtcNow.AddDays(-1), events.Add, CancellationToken.None);

        Assert.Equal(2, summary.TotalFileCount);
        Assert.Equal(2, events.Count);
        Assert.All(events, e => Assert.Equal(FileEventKind.Created, e.Kind));
    }

    [Fact]
    public async Task SecondScanDetectsGrowthAndIsIdempotentForUnchangedFiles()
    {
        var filePath = Path.Combine(_dir.Path, "growing.bin");
        File.WriteAllBytes(filePath, new byte[100]);
        var firstScan = await _scanner.ScanRootAsync(_dir.Path, DateTimeOffset.UtcNow.AddDays(-1), _ => { }, CancellationToken.None);

        // Touch the mtime forward so the scanner's change detection (which also checks LastWriteUtc) reliably sees a diff
        // even on filesystems with coarse timestamp resolution.
        await Task.Delay(50);
        File.WriteAllBytes(filePath, new byte[500]);
        File.SetLastWriteTimeUtc(filePath, DateTime.UtcNow.AddSeconds(2));

        // Mirrors DiskMonitoringService's real orchestration: each scan is handed the *previous*
        // scan's own StartedAt as its dedup cutoff, not a fixed value.
        var events = new List<FileEventDto>();
        var secondScan = await _scanner.ScanRootAsync(_dir.Path, firstScan.StartedAt, events.Add, CancellationToken.None);

        var grew = Assert.Single(events);
        Assert.Equal(FileEventKind.Grew, grew.Kind);
        Assert.Equal(400, grew.SizeDeltaBytes);

        // A third scan with nothing changed must not emit any event at all.
        var thirdScanEvents = new List<FileEventDto>();
        var thirdScan = await _scanner.ScanRootAsync(_dir.Path, secondScan.StartedAt, thirdScanEvents.Add, CancellationToken.None);
        Assert.Empty(thirdScanEvents);

        // A second, independent growth spotted two cycles later must NOT be swallowed by the
        // dedup check — regression test for a bug where a scan-sourced event's timestamp could
        // leak past its own scan's StartedAt and make the *following* scan think a *later*
        // change to the same file had already been reported.
        await Task.Delay(50);
        File.WriteAllBytes(filePath, new byte[900]);
        File.SetLastWriteTimeUtc(filePath, DateTime.UtcNow.AddSeconds(4));
        var fourthScanEvents = new List<FileEventDto>();
        await _scanner.ScanRootAsync(_dir.Path, thirdScan.StartedAt, fourthScanEvents.Add, CancellationToken.None);

        var grewAgain = Assert.Single(fourthScanEvents);
        Assert.Equal(FileEventKind.Grew, grewAgain.Kind);
        Assert.Equal(400, grewAgain.SizeDeltaBytes);
    }

    [Fact]
    public async Task DeletedFileIsReportedOnTheNextScan()
    {
        var filePath = Path.Combine(_dir.Path, "temp.bin");
        File.WriteAllBytes(filePath, new byte[10]);
        var firstScanTime = DateTimeOffset.UtcNow;
        await _scanner.ScanRootAsync(_dir.Path, firstScanTime.AddDays(-1), _ => { }, CancellationToken.None);

        File.Delete(filePath);

        var events = new List<FileEventDto>();
        await _scanner.ScanRootAsync(_dir.Path, firstScanTime, events.Add, CancellationToken.None);

        var deleted = Assert.Single(events);
        Assert.Equal(FileEventKind.Deleted, deleted.Kind);
        Assert.Equal(filePath, deleted.Path);
    }

    [Fact]
    public async Task NestedFolderSizesAggregateUpToTheRoot()
    {
        var sub = Directory.CreateDirectory(Path.Combine(_dir.Path, "sub"));
        File.WriteAllBytes(Path.Combine(sub.FullName, "file1.bin"), new byte[300]);
        File.WriteAllBytes(Path.Combine(_dir.Path, "file2.bin"), new byte[200]);

        var summary = await _scanner.ScanRootAsync(_dir.Path, DateTimeOffset.UtcNow.AddDays(-1), _ => { }, CancellationToken.None);

        Assert.Equal(500, summary.TotalSizeBytes);

        var snapshotForSub = _snapshots.SizesAsOf(_dir.Path, SnapshotGranularity.FiveMinutes, DateTimeOffset.UtcNow);
        Assert.Equal(300, snapshotForSub[sub.FullName].SizeBytes);
        Assert.Equal(500, snapshotForSub[_dir.Path].SizeBytes);
    }

    [Fact]
    public async Task NoiseMatchingFolderContributesSizeButNoPerFileEvents()
    {
        var noisyDir = Directory.CreateDirectory(Path.Combine(_dir.Path, "node_modules"));
        File.WriteAllBytes(Path.Combine(noisyDir.FullName, "left-pad.js"), new byte[1000]);
        File.WriteAllBytes(Path.Combine(_dir.Path, "app.js"), new byte[50]);

        var events = new List<FileEventDto>();
        var summary = await _scanner.ScanRootAsync(_dir.Path, DateTimeOffset.UtcNow.AddDays(-1), events.Add, CancellationToken.None);

        Assert.Equal(1050, summary.TotalSizeBytes); // noisy subtree still counted for real disk usage
        Assert.Single(events); // but only app.js produced a visible history entry
        Assert.Equal("app.js", Path.GetFileName(events[0].Path));
    }
}
