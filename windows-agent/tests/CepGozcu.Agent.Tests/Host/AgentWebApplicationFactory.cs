using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Mvc.Testing;
using Microsoft.AspNetCore.TestHost;
using Microsoft.Extensions.Hosting;

namespace CepGozcu.Agent.Tests.Host;

/// <summary>Boots the real Program (real DI graph, real SQLite, real pairing/token services) against an isolated temp data directory, with Kestrel swapped for TestServer.</summary>
public sealed class AgentWebApplicationFactory : WebApplicationFactory<Program>
{
    public string DataDir { get; } = Path.Combine(Path.GetTempPath(), $"cepgozcu-host-test-{Guid.NewGuid():N}");

    protected override void ConfigureWebHost(IWebHostBuilder builder)
    {
        Directory.CreateDirectory(DataDir);
        Environment.SetEnvironmentVariable("CEPGOZCU_DATA_DIR", DataDir);
        Environment.SetEnvironmentVariable("CEPGOZCU_PORT", "0");
        builder.UseEnvironment("Testing");
    }

    protected override IHost CreateHost(IHostBuilder builder)
    {
        builder.ConfigureWebHost(webHostBuilder => webHostBuilder.UseTestServer());
        return base.CreateHost(builder);
    }

    protected override void Dispose(bool disposing)
    {
        base.Dispose(disposing);
        try { Directory.Delete(DataDir, recursive: true); } catch (IOException) { } catch (UnauthorizedAccessException) { }
    }
}
