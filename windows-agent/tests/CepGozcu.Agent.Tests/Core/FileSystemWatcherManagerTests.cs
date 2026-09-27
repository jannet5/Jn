using CepGozcu.Agent.Core.Data;
using CepGozcu.Agent.Core.Disk;
using CepGozcu.Agent.Core.Protocol;
using CepGozcu.Agent.Tests.TestSupport;
using Xunit;

namespace CepGozcu.Agent.Tests.Core;

/// <summary>
/// Real FileSystemWatcher against a real temp directory (.NET's FileSystemWatcher works on
/// Linux via inotify and on Windows via ReadDirectoryChangesW — same class, same behavior
/// we depend on, so this genuinely exercises the live-event pipeline, not a simulation).
/// </summary>
public class FileSystemWatcherManagerTests : IDisposable
{
    private readonly TempDb _db = new();
    private readonly TempDirectory _dir = new();

    public void Dispose()
    {
        _db.Dispose();
        _dir.Dispose();
    }

    [Fact]
    public async Task RapidWritesToOneFileCoalesceIntoASingleEvent()
    {
        var events = new List<FileEventDto>();
        var overflowed = new List<string>();
        var cache = new FileCacheRepository(_db.Factory);
        var eventsRepo = new FileEventRepository(_db.Factory);

        using var manager = new FileSystemWatcherManager(new NoiseFilter(), eventsRepo, cache, events.Add, overflowed.Add);
        manager.Start(new[] { _dir.Path });

        var filePath = Path.Combine(_dir.Path, "burst.log");
        for (int i = 0; i < 10; i++)
        {
            await File.AppendAllTextAsync(filePath, new string('x', 100));
            await Task.Delay(50);
        }

        // Quiet period is 3s; wait comfortably past it for the coalesced flush.
        await WaitUntilAsync(() => events.Count > 0, TimeSpan.FromSeconds(8));

        Assert.Single(events);
        Assert.True(events[0].CoalescedCount >= 2, "multiple rapid writes should coalesce into one event with a count > 1");
    }

    [Fact]
    public async Task NoiseFilesNeverProduceEvents()
    {
        var events = new List<FileEventDto>();
        var cache = new FileCacheRepository(_db.Factory);
        var eventsRepo = new FileEventRepository(_db.Factory);

        using var manager = new FileSystemWatcherManager(new NoiseFilter(), eventsRepo, cache, events.Add, _ => { });
        manager.Start(new[] { _dir.Path });

        var tempDir = Directory.CreateDirectory(Path.Combine(_dir.Path, "AppData", "Local", "Temp"));
        await File.WriteAllTextAsync(Path.Combine(tempDir.FullName, "scratch.tmp"), "noise");

        await Task.Delay(TimeSpan.FromSeconds(5));

        Assert.Empty(events);
    }

    private static async Task WaitUntilAsync(Func<bool> condition, TimeSpan timeout)
    {
        var deadline = DateTime.UtcNow + timeout;
        while (DateTime.UtcNow < deadline)
        {
            if (condition()) return;
            await Task.Delay(200);
        }
        Assert.True(condition(), "condition was not met within the timeout");
    }
}
