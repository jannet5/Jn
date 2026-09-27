namespace CepGozcu.Agent.Core.Processes;

/// <summary>
/// Decides whether a process is safe to expose for remote termination. This is the single
/// choke point that stands between "kill" requests coming off the network and the OS —
/// every code path that can terminate a process MUST go through <see cref="IsProtected"/> first.
/// Deny-by-default for anything not clearly identified as an ordinary user application.
/// </summary>
public static class CriticalProcessGuard
{
    // Windows core/session processes. Names are case-insensitive, without the .exe extension.
    private static readonly HashSet<string> ProtectedNames = new(StringComparer.OrdinalIgnoreCase)
    {
        "system", "system idle process", "registry", "smss", "csrss", "wininit", "winlogon",
        "services", "lsass", "lsaiso", "fontdrvhost", "dwm", "sihost", "taskhostw", "svchost",
        "explorer", "userinit", "logonui", "conhost", "spoolsv", "wudfhost", "dllhost",
        "memcompression", "runtimebroker", "shellexperiencehost", "searchindexer", "searchhost",
        "textinputhost", "ctfmon", "audiodg", "wmiprvse", "msmpeng", "securityhealthservice",
        "trustedinstaller", "tiworker", "wininit", "system32",
        // Our own agent must never be a remote-kill target.
        "cepgozcu.agent.host", "cepgozcu.agent.host.exe",
    };

    // PIDs 0 and 4 are always "System Idle Process" and "System" on Windows.
    private static readonly HashSet<int> ProtectedPids = new() { 0, 4 };

    public static bool IsProtected(int pid, string processName)
    {
        if (ProtectedPids.Contains(pid)) return true;

        var normalized = processName.Trim();
        if (normalized.EndsWith(".exe", StringComparison.OrdinalIgnoreCase))
        {
            normalized = normalized[..^4];
        }
        return ProtectedNames.Contains(normalized);
    }
}
