using System.Text.Json;
using CepGozcu.Agent.Core.Protocol;

namespace CepGozcu.Agent.Core.Launcher;

/// <summary>
/// The list of apps a phone is allowed to launch. Intentionally has no network-facing write
/// path anywhere in the codebase — it is only ever edited by <see cref="Add"/>/<see cref="Remove"/>
/// calls made from the loopback-only local admin surface (see LoopbackOnlyFilter), never from a
/// paired device's WebSocket session. This is what keeps "launch" from becoming "run anything".
/// </summary>
public sealed class AppAllowlistStore
{
    private readonly string _filePath;
    private readonly object _gate = new();
    private List<AllowedApp> _apps = new();

    public AppAllowlistStore(string filePath)
    {
        _filePath = filePath;
        Load();
    }

    public IReadOnlyList<AllowedApp> List()
    {
        lock (_gate) { return _apps.ToList(); }
    }

    public AllowedApp? Find(string appId)
    {
        lock (_gate) { return _apps.FirstOrDefault(a => a.Id == appId); }
    }

    public AllowedApp Add(string displayName, string path, string? iconHint)
    {
        lock (_gate)
        {
            var app = new AllowedApp(Guid.NewGuid().ToString("N"), displayName, path, iconHint);
            _apps.Add(app);
            Save();
            return app;
        }
    }

    public bool Remove(string appId)
    {
        lock (_gate)
        {
            var removed = _apps.RemoveAll(a => a.Id == appId) > 0;
            if (removed) Save();
            return removed;
        }
    }

    private void Load()
    {
        lock (_gate)
        {
            if (!File.Exists(_filePath))
            {
                _apps = new List<AllowedApp>();
                return;
            }
            try
            {
                var json = File.ReadAllText(_filePath);
                _apps = JsonSerializer.Deserialize<List<AllowedApp>>(json) ?? new List<AllowedApp>();
            }
            catch (Exception ex) when (ex is JsonException or IOException)
            {
                _apps = new List<AllowedApp>();
            }
        }
    }

    private void Save()
    {
        var dir = Path.GetDirectoryName(_filePath);
        if (!string.IsNullOrEmpty(dir)) Directory.CreateDirectory(dir);
        var json = JsonSerializer.Serialize(_apps, new JsonSerializerOptions { WriteIndented = true });
        File.WriteAllText(_filePath, json);
    }
}
