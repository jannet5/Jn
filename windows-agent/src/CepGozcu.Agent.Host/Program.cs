using System.Security.Cryptography.X509Certificates;
using CepGozcu.Agent.Core.Data;
using CepGozcu.Agent.Core.Disk;
using CepGozcu.Agent.Core.Launcher;
using CepGozcu.Agent.Core.Metrics;
using CepGozcu.Agent.Core.Processes;
using CepGozcu.Agent.Core.Protocol;
using CepGozcu.Agent.Core.Security;
using CepGozcu.Agent.Host.Admin;
using CepGozcu.Agent.Host.Middleware;
using CepGozcu.Agent.Host.Pairing;
using CepGozcu.Agent.Host.Ws;
using Microsoft.Extensions.Hosting.WindowsServices;

var builder = WebApplication.CreateBuilder(new WebApplicationOptions
{
    Args = args,
    ContentRootPath = WindowsServiceHelpers.IsWindowsService() ? AppContext.BaseDirectory : default,
});

builder.Services.AddWindowsService(options => options.ServiceName = "CepGozcuAgent");
builder.Logging.AddSimpleConsole(o => { o.SingleLine = true; o.TimestampFormat = "HH:mm:ss "; });

// Every minimal-API endpoint (pairing, admin) must serialize exactly like the WS hub does
// (see Protocol.WireJson) — otherwise the same DTO (e.g. PairingState) comes back as a plain
// int over REST but as a camelCase string over the socket, and the Android client can only
// deserialize one of them.
builder.Services.ConfigureHttpJsonOptions(options =>
{
    options.SerializerOptions.PropertyNamingPolicy = System.Text.Json.JsonNamingPolicy.CamelCase;
    options.SerializerOptions.Converters.Add(new System.Text.Json.Serialization.JsonStringEnumConverter(System.Text.Json.JsonNamingPolicy.CamelCase));
});

// ---- Paths & configuration ------------------------------------------------

var dataDir = builder.Configuration["CEPGOZCU_DATA_DIR"]
    ?? Environment.GetEnvironmentVariable("CEPGOZCU_DATA_DIR")
    ?? (OperatingSystem.IsWindows()
        ? Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.CommonApplicationData), "CepGozcuAgent")
        : Path.Combine(AppContext.BaseDirectory, "data"));
Directory.CreateDirectory(dataDir);

var port = int.TryParse(builder.Configuration["CEPGOZCU_PORT"] ?? Environment.GetEnvironmentVariable("CEPGOZCU_PORT"), out var p) ? p : 47811;

var defaultRoots = OperatingSystem.IsWindows()
    ? DriveInfo.GetDrives().Where(d => d.IsReady && d.DriveType == DriveType.Fixed).Select(d => d.RootDirectory.FullName).ToList()
    : new List<string> { AppContext.BaseDirectory };

// ---- Core singletons (built once, before DI, because pairing/cert are interdependent) ------

var certificateProvider = new CertificateProvider(Path.Combine(dataDir, "tls"));
X509Certificate2 certificate = certificateProvider.GetOrCreate();

builder.WebHost.ConfigureKestrel(options =>
{
    options.ListenAnyIP(port, listenOptions => listenOptions.UseHttps(certificate));
});

// ---- DI registrations -------------------------------------------------------

builder.Services.AddSingleton(certificate);
builder.Services.AddSingleton(new SqliteConnectionFactory(Path.Combine(dataDir, "agent.db")));
builder.Services.AddSingleton<DeviceRepository>();
builder.Services.AddSingleton<FileEventRepository>();
builder.Services.AddSingleton<FileCacheRepository>();
builder.Services.AddSingleton<FolderSnapshotRepository>();
builder.Services.AddSingleton<AlertRepository>();
builder.Services.AddSingleton<AuditRepository>();

builder.Services.AddSingleton(new NoiseFilter());
builder.Services.AddSingleton(new WatchedRootsStore(Path.Combine(dataDir, "watched-roots.json"), defaultRoots));
builder.Services.AddSingleton(new AppAllowlistStore(Path.Combine(dataDir, "allowlist.json")));

builder.Services.AddSingleton<ISystemMetricsProvider, SystemMetricsProvider>();
builder.Services.AddSingleton<ProcessService>();
builder.Services.AddSingleton<AppLaunchService>();

builder.Services.AddSingleton<TokenService>();
builder.Services.AddSingleton(sp => new PairingService(
    sp.GetRequiredService<DeviceRepository>(),
    sp.GetRequiredService<TokenService>(),
    certificate,
    port));

builder.Services.AddSingleton<GrowthAnalyzer>();
builder.Services.AddSingleton(sp => new DiskScanner(
    sp.GetRequiredService<NoiseFilter>(),
    sp.GetRequiredService<FileCacheRepository>(),
    sp.GetRequiredService<FileEventRepository>(),
    sp.GetRequiredService<FolderSnapshotRepository>()));

builder.Services.AddSingleton<WsHub>();
builder.Services.AddSingleton<Func<WsHub>>(sp => sp.GetRequiredService<WsHub>);
builder.Services.AddSingleton<MessageRouter>();

builder.Services.AddSingleton(sp =>
{
    var alerts = sp.GetRequiredService<AlertRepository>();
    var hub = sp.GetRequiredService<WsHub>();
    return new AlertEngine(alerts, alert => hub.Broadcast(MessageType.AlertPush, alert));
});

builder.Services.AddSingleton(sp =>
{
    var alertEngine = sp.GetRequiredService<AlertEngine>();
    var hub = sp.GetRequiredService<WsHub>();
    void Publish(FileEventDto evt)
    {
        alertEngine.OnFileEvent(evt);
        hub.Broadcast(MessageType.DiskFileEvent, evt);
    }

    var logger = sp.GetRequiredService<ILoggerFactory>().CreateLogger("FileSystemWatcherManager");
    return new FileSystemWatcherManager(
        sp.GetRequiredService<NoiseFilter>(),
        sp.GetRequiredService<FileEventRepository>(),
        sp.GetRequiredService<FileCacheRepository>(),
        Publish,
        root => logger.LogWarning("FileSystemWatcher buffer overflow on {Root} — relying on next periodic scan to reconcile", root));
});

builder.Services.AddHostedService(sp =>
{
    var hub = sp.GetRequiredService<WsHub>();
    var alertEngine = sp.GetRequiredService<AlertEngine>();
    return new DiskMonitoringService(
        sp.GetRequiredService<WatchedRootsStore>(),
        sp.GetRequiredService<FileSystemWatcherManager>(),
        sp.GetRequiredService<DiskScanner>(),
        sp.GetRequiredService<GrowthAnalyzer>(),
        alertEngine,
        sp.GetRequiredService<FolderSnapshotRepository>(),
        sp.GetRequiredService<ISystemMetricsProvider>(),
        evt => hub.Broadcast(MessageType.DiskFileEvent, evt),
        sp.GetRequiredService<ILogger<DiskMonitoringService>>());
});

builder.Services.AddHostedService(sp =>
{
    var hub = sp.GetRequiredService<WsHub>();
    var alertEngine = sp.GetRequiredService<AlertEngine>();
    return new MetricsBroadcastService(sp.GetRequiredService<ISystemMetricsProvider>(), metrics =>
    {
        alertEngine.OnDiskMetrics(metrics.Disks);
        hub.Broadcast(MessageType.MetricsUpdate, metrics);
    });
});

var app = builder.Build();

if (app.Environment.EnvironmentName == "Testing")
{
    // WebApplicationFactory's in-memory TestServer never populates Connection.RemoteIpAddress
    // (there's no real socket). Only the "Testing" environment (set exclusively by the test
    // fixture) fills it in as loopback so the LAN/loopback middlewares below can be exercised
    // for real instead of being bypassed in every integration test.
    app.Use(async (ctx, next) =>
    {
        ctx.Connection.RemoteIpAddress ??= System.Net.IPAddress.Loopback;
        await next();
    });
}

app.UseMiddleware<LanOnlyMiddleware>();
app.UseWebSockets();

app.MapPairingEndpoints();
app.MapWsEndpoint();

app.UseWhen(ctx => ctx.Request.Path.StartsWithSegments("/admin"), branch => branch.UseMiddleware<LoopbackOnlyMiddleware>());
app.MapAdminEndpoints();

app.UseDefaultFiles();
app.UseWhen(ctx => !ctx.Request.Path.StartsWithSegments("/admin") && !ctx.Request.Path.StartsWithSegments("/pair") && ctx.Request.Path != "/ws",
    branch =>
    {
        branch.UseMiddleware<LoopbackOnlyMiddleware>();
        branch.UseStaticFiles();
    });

app.MapGet("/health", () => Results.Ok(new { status = "ok" }));

Console.WriteLine();
Console.WriteLine("=========================================================");
Console.WriteLine(" CepGözcü Agent çalışıyor.");
Console.WriteLine($" Yönetim paneli:  https://127.0.0.1:{port}/");
Console.WriteLine($" Veri klasörü:    {dataDir}");
Console.WriteLine(" Yeni bir telefon eşlemek için yönetim panelini açın.");
Console.WriteLine("=========================================================");
Console.WriteLine();

app.Run();

public partial class Program; // exposed for WebApplicationFactory-based integration tests
