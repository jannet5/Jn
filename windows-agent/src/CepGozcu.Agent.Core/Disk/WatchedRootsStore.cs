using System.Text.Json;

namespace CepGozcu.Agent.Core.Disk;

public sealed record WatchedRootConfig(string Path, bool Enabled);

/// <summary>
/// Which top-level roots (normally one per fixed drive) get watched + periodically scanned.
/// Defaults to every fixed drive found at first run; editable later from the local admin page.
/// </summary>
public sealed class WatchedRootsStore
{
    private readonly string _filePath;
    private readonly object _gate = new();
    private List<WatchedRootConfig> _roots;

    public WatchedRootsStore(string filePath, IReadOnlyList<string> defaultRoots)
    {
        _filePath = filePath;
        _roots = Load() ?? defaultRoots.Select(p => new WatchedRootConfig(p, true)).ToList();
        if (!File.Exists(_filePath)) Save();
    }

    public IReadOnlyList<WatchedRootConfig> List()
    {
        lock (_gate) return _roots.ToList();
    }

    public IReadOnlyList<string> EnabledPaths()
    {
        lock (_gate) return _roots.Where(r => r.Enabled).Select(r => r.Path).ToList();
    }

    public void SetEnabled(string path, bool enabled)
    {
        lock (_gate)
        {
            var idx = _roots.FindIndex(r => string.Equals(r.Path, path, StringComparison.OrdinalIgnoreCase));
            if (idx < 0) return;
            _roots[idx] = _roots[idx] with { Enabled = enabled };
            Save();
        }
    }

    private List<WatchedRootConfig>? Load()
    {
        if (!File.Exists(_filePath)) return null;
        try
        {
            return JsonSerializer.Deserialize<List<WatchedRootConfig>>(File.ReadAllText(_filePath));
        }
        catch (Exception ex) when (ex is JsonException or IOException)
        {
            return null;
        }
    }

    private void Save()
    {
        var dir = Path.GetDirectoryName(_filePath);
        if (!string.IsNullOrEmpty(dir)) Directory.CreateDirectory(dir);
        File.WriteAllText(_filePath, JsonSerializer.Serialize(_roots, new JsonSerializerOptions { WriteIndented = true }));
    }
}
