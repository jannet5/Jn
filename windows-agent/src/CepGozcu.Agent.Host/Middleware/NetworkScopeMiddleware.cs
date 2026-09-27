using CepGozcu.Agent.Core.Security;

namespace CepGozcu.Agent.Host.Middleware;

/// <summary>
/// Defense-in-depth boundary for "yerel ağ dışından bağlantı yok": rejects every request whose
/// remote address isn't loopback or a private LAN range, regardless of what the Windows Firewall
/// is (mis)configured to allow. This runs before routing, for every endpoint.
/// </summary>
public sealed class LanOnlyMiddleware
{
    private readonly RequestDelegate _next;
    private readonly ILogger<LanOnlyMiddleware> _logger;

    public LanOnlyMiddleware(RequestDelegate next, ILogger<LanOnlyMiddleware> logger)
    {
        _next = next;
        _logger = logger;
    }

    public async Task InvokeAsync(HttpContext context)
    {
        var remote = context.Connection.RemoteIpAddress;
        if (!LanAddressHelper.IsAllowedRemote(remote))
        {
            _logger.LogWarning("Rejected out-of-LAN connection attempt from {Remote}", remote);
            context.Response.StatusCode = StatusCodes.Status403Forbidden;
            await context.Response.WriteAsync("Bu uygulama yalnızca yerel ağdan erişilebilir.");
            return;
        }
        await _next(context);
    }
}

/// <summary>Extra restriction for the local admin surface (pairing approval, allowlist, settings): loopback only, not even other LAN devices.</summary>
public sealed class LoopbackOnlyMiddleware
{
    private readonly RequestDelegate _next;

    public LoopbackOnlyMiddleware(RequestDelegate next) => _next = next;

    public async Task InvokeAsync(HttpContext context)
    {
        var remote = context.Connection.RemoteIpAddress;
        if (remote is null || !System.Net.IPAddress.IsLoopback(remote))
        {
            context.Response.StatusCode = StatusCodes.Status403Forbidden;
            await context.Response.WriteAsync("Bu sayfa yalnızca bu bilgisayardan açılabilir.");
            return;
        }
        await _next(context);
    }
}
