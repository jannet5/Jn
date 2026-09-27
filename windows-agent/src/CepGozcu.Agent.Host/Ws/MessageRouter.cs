using System.Text.Json;
using CepGozcu.Agent.Core.Data;
using CepGozcu.Agent.Core.Disk;
using CepGozcu.Agent.Core.Launcher;
using CepGozcu.Agent.Core.Metrics;
using CepGozcu.Agent.Core.Processes;
using CepGozcu.Agent.Core.Protocol;
using CepGozcu.Agent.Core.Security;

namespace CepGozcu.Agent.Host.Ws;

/// <summary>
/// Every authenticated WebSocket request goes through exactly one method here. This is the
/// single place that (a) knows which message types exist, (b) enforces that state-changing
/// actions are audited, and (c) turns service calls into wire responses. Keeping all of that
/// in one file makes "what can a paired phone actually do to this PC" answerable by reading
/// one switch statement.
/// </summary>
public sealed class MessageRouter
{
    private readonly ISystemMetricsProvider _metrics;
    private readonly ProcessService _processes;
    private readonly AppLaunchService _launcher;
    private readonly WatchedRootsStore _watchedRoots;
    private readonly FileEventRepository _fileEvents;
    private readonly GrowthAnalyzer _growth;
    private readonly AlertRepository _alerts;
    private readonly AuditRepository _audit;
    private readonly TokenService _tokens;
    private readonly Func<WsHub> _hubAccessor; // lazy to avoid a circular DI dependency (WsHub doesn't depend back on this)

    public MessageRouter(
        ISystemMetricsProvider metrics,
        ProcessService processes,
        AppLaunchService launcher,
        WatchedRootsStore watchedRoots,
        FileEventRepository fileEvents,
        GrowthAnalyzer growth,
        AlertRepository alerts,
        AuditRepository audit,
        TokenService tokens,
        Func<WsHub> hubAccessor)
    {
        _metrics = metrics;
        _processes = processes;
        _launcher = launcher;
        _watchedRoots = watchedRoots;
        _fileEvents = fileEvents;
        _growth = growth;
        _alerts = alerts;
        _audit = audit;
        _tokens = tokens;
        _hubAccessor = hubAccessor;
    }

    public Task<Envelope?> RouteAsync(Envelope request, DeviceRecord device, CancellationToken ct)
    {
        object? result = request.Type switch
        {
            MessageType.Ping => new { pong = true },
            MessageType.MetricsGet => _metrics.GetMetrics(),
            MessageType.ProcessesList => _processes.ListProcesses(),
            MessageType.ProcessKill => HandleKill(request, device),
            MessageType.AppsList => _launcher.ListAllowed(),
            MessageType.AppLaunch => HandleLaunch(request, device),
            MessageType.DiskOverview => BuildDiskOverview(),
            MessageType.DiskEventsQuery => _fileEvents.Query(Payload<DiskEventsQuery>(request)),
            MessageType.DiskGrowthQuery => _growth.Analyze(Payload<DiskGrowthQuery>(request)),
            MessageType.AlertsQuery => _alerts.Query(Payload<AlertsQuery>(request)),
            MessageType.AuditQuery => _audit.Query(Payload<AuditQuery>(request)),
            MessageType.DeviceUnpair => HandleUnpair(device),
            _ => (object?)new ErrorMarker("unknown_type"),
        };

        if (result is ErrorMarker marker)
        {
            return Task.FromResult<Envelope?>(new Envelope
            {
                Id = request.Id,
                Type = request.Type,
                Ts = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds(),
                Error = new ErrorPayload { Code = marker.Code, Message = marker.Code },
            });
        }

        return Task.FromResult<Envelope?>(new Envelope
        {
            Id = request.Id,
            Type = request.Type,
            Ts = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds(),
            Payload = JsonSerializer.SerializeToElement(result, WireJson.Options),
        });
    }

    private sealed record ErrorMarker(string Code);

    private T Payload<T>(Envelope request)
    {
        if (request.Payload is null) throw new InvalidOperationException("missing_payload");
        return request.Payload.Value.Deserialize<T>(WireJson.Options)
            ?? throw new InvalidOperationException("invalid_payload");
    }

    private ProcessKillResult HandleKill(Envelope request, DeviceRecord device)
    {
        var req = Payload<ProcessKillRequest>(request);
        var result = _processes.Kill(req);
        _audit.Record(device.Id, device.Name, "process.kill", $"pid={req.Pid}", result.Success, result.Reason);
        return result;
    }

    private AppLaunchResult HandleLaunch(Envelope request, DeviceRecord device)
    {
        var req = Payload<AppLaunchRequest>(request);
        var result = _launcher.Launch(req);
        _audit.Record(device.Id, device.Name, "app.launch", req.AppId, result.Success, result.Reason);
        return result;
    }

    private object HandleUnpair(DeviceRecord device)
    {
        _tokens.Revoke(device.Id);
        _audit.Record(device.Id, device.Name, "device.unpair", null, true, null);
        _hubAccessor().ForceDisconnect(device.Id);
        return new { revoked = true };
    }

    private DiskOverview BuildDiskOverview()
    {
        var disks = _metrics.GetMetrics().Disks;
        var roots = _watchedRoots.List().Select(r => new WatchedRootStatus(r.Path, r.Enabled, null, 0)).ToList();
        return new DiskOverview(disks, roots);
    }
}
