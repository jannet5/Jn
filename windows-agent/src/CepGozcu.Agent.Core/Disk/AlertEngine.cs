using CepGozcu.Agent.Core.Data;
using CepGozcu.Agent.Core.Protocol;

namespace CepGozcu.Agent.Core.Disk;

/// <summary>
/// Turns raw signals (a file event, a metrics sample, a growth-analysis result) into the small
/// number of alerts a person actually wants to see. De-duplicates via
/// <see cref="AlertRepository.HasRecentSimilar"/> so a folder that stays over threshold doesn't
/// re-fire every scan tick.
/// </summary>
public sealed class AlertEngine
{
    public const long BigFileThresholdBytes = 500L * 1024 * 1024;
    public const double FastGrowthBytesPerHourThreshold = 2L * 1024 * 1024 * 1024; // 2 GB/hour
    public const double SuddenDropFractionThreshold = 0.05; // 5% of a disk's total size disappearing between samples

    private static readonly TimeSpan RepeatSuppressionWindow = TimeSpan.FromHours(1);

    private readonly AlertRepository _repo;
    private readonly Action<AlertDto> _onAlert;
    private readonly Dictionary<string, long> _lastFreeBytesByDisk = new();

    public AlertEngine(AlertRepository repo, Action<AlertDto> onAlert)
    {
        _repo = repo;
        _onAlert = onAlert;
    }

    public void OnFileEvent(FileEventDto evt)
    {
        if (evt.Kind is FileEventKind.Created or FileEventKind.Grew && evt.SizeBytes >= BigFileThresholdBytes)
        {
            Raise(AlertKind.BigFile, AlertSeverity.Warning,
                $"Büyük dosya tespit edildi: {evt.Path} ({FormatBytes(evt.SizeBytes)})", evt.Path);
        }
    }

    public void OnDiskMetrics(IReadOnlyList<DiskMetrics> disks)
    {
        foreach (var disk in disks)
        {
            if (disk.IsLowSpace)
            {
                Raise(AlertKind.LowFreeSpace, AlertSeverity.Critical,
                    $"{disk.Name} sürücüsünde boş alan azaldı: {FormatBytes(disk.FreeBytes)} kaldı", disk.Name);
            }

            if (_lastFreeBytesByDisk.TryGetValue(disk.Name, out var lastFree) && disk.TotalBytes > 0)
            {
                var dropped = lastFree - disk.FreeBytes;
                if (dropped > 0 && dropped / (double)disk.TotalBytes >= SuddenDropFractionThreshold)
                {
                    Raise(AlertKind.SuddenDiskDrop, AlertSeverity.Warning,
                        $"{disk.Name} sürücüsünde ani alan düşüşü: {FormatBytes(dropped)}", disk.Name);
                }
            }
            _lastFreeBytesByDisk[disk.Name] = disk.FreeBytes;
        }
    }

    public void OnGrowthResult(DiskGrowthResult result)
    {
        foreach (var folder in result.TopGrowingFolders)
        {
            if (folder.BytesPerHour >= FastGrowthBytesPerHourThreshold)
            {
                Raise(AlertKind.FastFolderGrowth, AlertSeverity.Warning,
                    $"Hızlı büyüyen klasör: {folder.Path} (~{FormatBytes((long)folder.BytesPerHour)}/saat)", folder.Path);
            }
        }
    }

    private void Raise(AlertKind kind, AlertSeverity severity, string message, string? path)
    {
        if (_repo.HasRecentSimilar(kind, path, RepeatSuppressionWindow)) return;

        var id = _repo.Insert(kind, severity, message, path);
        _onAlert(new AlertDto(id, kind, severity, message, path, DateTimeOffset.UtcNow, false));
    }

    private static string FormatBytes(long bytes)
    {
        string[] units = ["B", "KB", "MB", "GB", "TB"];
        double value = bytes;
        int unit = 0;
        while (value >= 1024 && unit < units.Length - 1)
        {
            value /= 1024;
            unit++;
        }
        return $"{value:0.#} {units[unit]}";
    }
}
