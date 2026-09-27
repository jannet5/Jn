using System.Diagnostics;
using CepGozcu.Agent.Core.Protocol;

namespace CepGozcu.Agent.Core.Launcher;

public sealed class AppLaunchService
{
    private readonly AppAllowlistStore _store;

    public AppLaunchService(AppAllowlistStore store)
    {
        _store = store;
    }

    public IReadOnlyList<AllowedApp> ListAllowed() => _store.List();

    public AppLaunchResult Launch(AppLaunchRequest request)
    {
        var app = _store.Find(request.AppId);
        if (app is null)
        {
            return new AppLaunchResult(request.AppId, false, "not_in_allowlist", null);
        }

        if (!File.Exists(app.Path))
        {
            return new AppLaunchResult(request.AppId, false, "target_not_found", null);
        }

        try
        {
            var psi = new ProcessStartInfo
            {
                FileName = app.Path,
                UseShellExecute = true,
            };
            var process = Process.Start(psi);
            return new AppLaunchResult(request.AppId, true, null, process?.Id);
        }
        catch (Exception ex) when (ex is InvalidOperationException or System.ComponentModel.Win32Exception)
        {
            return new AppLaunchResult(request.AppId, false, "launch_failed: " + ex.Message, null);
        }
    }
}
