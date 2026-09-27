using System.Diagnostics;
using CepGozcu.Agent.Core.Processes;
using CepGozcu.Agent.Core.Protocol;
using Xunit;

namespace CepGozcu.Agent.Tests.Core;

/// <summary>
/// Spawns and kills real OS processes through <see cref="ProcessService"/> — the exact same
/// .NET Process API this class uses is what runs unmodified on Windows; only the test's own
/// throwaway child process ("sleep", a portable stand-in for a real Windows app) is OS-specific.
/// </summary>
public class ProcessServiceTests
{
    [Fact]
    public void ListedProcessesIncludeARealSpawnedChildProcess()
    {
        using var child = Process.Start(new ProcessStartInfo("sleep", "5") { UseShellExecute = false });
        try
        {
            var service = new ProcessService();
            var list = service.ListProcesses();

            var found = Assert.Single(list, p => p.Pid == child!.Id);
            Assert.False(found.IsCritical);
            Assert.True(found.CanKill);
        }
        finally
        {
            if (!child!.HasExited) child.Kill();
        }
    }

    [Fact]
    public void KillTerminatesARealProcess()
    {
        using var child = Process.Start(new ProcessStartInfo("sleep", "30") { UseShellExecute = false });
        var service = new ProcessService();

        var result = service.Kill(new ProcessKillRequest(child!.Id, Confirm: true));

        Assert.True(result.Success);
        child.WaitForExit(3000);
        Assert.True(child.HasExited);
    }

    [Fact]
    public void KillWithoutConfirmDoesNotTerminateTheProcess()
    {
        using var child = Process.Start(new ProcessStartInfo("sleep", "5") { UseShellExecute = false });
        var service = new ProcessService();

        var result = service.Kill(new ProcessKillRequest(child!.Id, Confirm: false));

        Assert.False(result.Success);
        Assert.Equal("confirmation_required", result.Reason);
        Assert.False(child.HasExited);
        child.Kill();
    }

    [Fact]
    public void KillOfNonExistentPidFails()
    {
        var service = new ProcessService();
        // A PID essentially guaranteed not to be in use.
        var result = service.Kill(new ProcessKillRequest(999_999, Confirm: true));
        Assert.False(result.Success);
        Assert.Equal("process_not_found", result.Reason);
    }

    [Fact]
    public void CannotKillItsOwnAgentProcess()
    {
        var service = new ProcessService();
        var result = service.Kill(new ProcessKillRequest(Environment.ProcessId, Confirm: true));
        Assert.False(result.Success);
        Assert.Equal("cannot_kill_self", result.Reason);
    }
}
