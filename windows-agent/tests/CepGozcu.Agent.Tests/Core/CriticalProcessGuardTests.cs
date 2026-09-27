using CepGozcu.Agent.Core.Processes;
using Xunit;

namespace CepGozcu.Agent.Tests.Core;

public class CriticalProcessGuardTests
{
    [Theory]
    [InlineData(100, "explorer")]
    [InlineData(200, "explorer.exe")]
    [InlineData(300, "csrss")]
    [InlineData(400, "lsass")]
    [InlineData(500, "wininit")]
    [InlineData(0, "anything")]
    [InlineData(4, "anything")]
    public void ProtectsCriticalNamesAndPids(int pid, string name)
    {
        Assert.True(CriticalProcessGuard.IsProtected(pid, name));
    }

    [Theory]
    [InlineData(1234, "notepad")]
    [InlineData(1234, "chrome")]
    [InlineData(1234, "MyGame.exe")]
    public void AllowsOrdinaryApps(int pid, string name)
    {
        Assert.False(CriticalProcessGuard.IsProtected(pid, name));
    }

    [Fact]
    public void IsCaseInsensitive()
    {
        Assert.True(CriticalProcessGuard.IsProtected(999, "EXPLORER.EXE"));
    }
}
