using System.Diagnostics;
using CepGozcu.Agent.Core.Protocol;

namespace CepGozcu.Agent.Core.Processes;

/// <summary>
/// Lists real OS processes and performs guarded termination. Uses <see cref="Process"/>
/// directly (fully cross-platform in .NET), so both the CPU% delta math and the kill path
/// are exercised for real on whichever OS the tests run on, not simulated.
/// </summary>
public sealed class ProcessService
{
    private readonly int _selfPid = Environment.ProcessId;
    private readonly object _gate = new();
    private Dictionary<int, (TimeSpan Cpu, long AtMs)> _lastSample = new();
    private readonly Stopwatch _clock = Stopwatch.StartNew();

    public IReadOnlyList<ProcessInfo> ListProcesses()
    {
        var nowMs = _clock.ElapsedMilliseconds;
        var processes = Process.GetProcesses();
        var next = new Dictionary<int, (TimeSpan, long)>(processes.Length);
        var results = new List<ProcessInfo>(processes.Length);

        Dictionary<int, (TimeSpan Cpu, long AtMs)> previous;
        lock (_gate) { previous = _lastSample; }

        foreach (var p in processes)
        {
            try
            {
                TimeSpan cpuTime;
                try { cpuTime = p.TotalProcessorTime; }
                catch (Exception ex) when (ex is InvalidOperationException or System.ComponentModel.Win32Exception or NotSupportedException)
                {
                    // Access denied to a foreign/elevated process — still list it, just without CPU%.
                    cpuTime = TimeSpan.Zero;
                }

                next[p.Id] = (cpuTime, nowMs);

                double cpuPercent = 0;
                if (previous.TryGetValue(p.Id, out var prior) && nowMs > prior.AtMs)
                {
                    var cpuDeltaMs = (cpuTime - prior.Cpu).TotalMilliseconds;
                    var wallDeltaMs = nowMs - prior.AtMs;
                    if (cpuDeltaMs > 0 && wallDeltaMs > 0)
                    {
                        cpuPercent = 100.0 * cpuDeltaMs / wallDeltaMs / Environment.ProcessorCount;
                    }
                }

                long workingSet;
                try { workingSet = p.WorkingSet64; }
                catch (Exception ex) when (ex is InvalidOperationException or System.ComponentModel.Win32Exception)
                {
                    workingSet = 0;
                }

                DateTimeOffset? startedAt = null;
                try { startedAt = p.StartTime; }
                catch (Exception ex) when (ex is InvalidOperationException or System.ComponentModel.Win32Exception or NotSupportedException)
                {
                    // Denied for some system/elevated processes — expected, leave null.
                }

                var isCritical = CriticalProcessGuard.IsProtected(p.Id, p.ProcessName) || p.Id == _selfPid;

                results.Add(new ProcessInfo(
                    Pid: p.Id,
                    Name: p.ProcessName,
                    DisplayName: TryGetDisplayName(p),
                    CpuPercent: Math.Round(Math.Clamp(cpuPercent, 0, 100 * Environment.ProcessorCount), 1),
                    WorkingSetBytes: workingSet,
                    IsCritical: isCritical,
                    CanKill: !isCritical,
                    StartedAt: startedAt));
            }
            catch (Exception ex) when (ex is InvalidOperationException or System.ComponentModel.Win32Exception)
            {
                // Process exited between GetProcesses() and inspection — skip it.
            }
            finally
            {
                p.Dispose();
            }
        }

        lock (_gate) { _lastSample = next; }
        return results;
    }

    private static string? TryGetDisplayName(Process p)
    {
        try
        {
            return string.IsNullOrWhiteSpace(p.MainWindowTitle) ? null : p.MainWindowTitle;
        }
        catch (Exception ex) when (ex is InvalidOperationException or System.ComponentModel.Win32Exception)
        {
            return null;
        }
    }

    public ProcessKillResult Kill(ProcessKillRequest request)
    {
        Process process;
        try
        {
            process = Process.GetProcessById(request.Pid);
        }
        catch (ArgumentException)
        {
            return new ProcessKillResult(request.Pid, false, "process_not_found");
        }

        using (process)
        {
            if (request.Pid == _selfPid)
            {
                return new ProcessKillResult(request.Pid, false, "cannot_kill_self");
            }

            if (CriticalProcessGuard.IsProtected(process.Id, process.ProcessName))
            {
                return new ProcessKillResult(request.Pid, false, "protected_process");
            }

            if (!request.Confirm)
            {
                return new ProcessKillResult(request.Pid, false, "confirmation_required");
            }

            try
            {
                process.Kill(entireProcessTree: true);
                process.WaitForExit(5000);
                return new ProcessKillResult(request.Pid, true, null);
            }
            catch (Exception ex) when (ex is InvalidOperationException or System.ComponentModel.Win32Exception or NotSupportedException)
            {
                return new ProcessKillResult(request.Pid, false, "kill_failed: " + ex.Message);
            }
        }
    }
}
