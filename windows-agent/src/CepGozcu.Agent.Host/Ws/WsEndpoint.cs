using CepGozcu.Agent.Core.Data;
using CepGozcu.Agent.Core.Security;

namespace CepGozcu.Agent.Host.Ws;

public static class WsEndpoint
{
    public static void MapWsEndpoint(this WebApplication app)
    {
        app.MapGet("/ws", async (HttpContext context, TokenService tokens, DeviceRepository devices, WsHub hub) =>
        {
            if (!context.WebSockets.IsWebSocketRequest)
            {
                context.Response.StatusCode = StatusCodes.Status400BadRequest;
                return;
            }

            var token = ExtractToken(context);
            var device = string.IsNullOrEmpty(token) ? null : tokens.ValidateSession(token);
            if (device is null)
            {
                context.Response.StatusCode = StatusCodes.Status401Unauthorized;
                return;
            }

            devices.TouchLastSeen(device.Id, DateTimeOffset.UtcNow);
            using var socket = await context.WebSockets.AcceptWebSocketAsync();
            await hub.HandleAsync(socket, device, context.RequestAborted);
        });
    }

    private static string? ExtractToken(HttpContext context)
    {
        var header = context.Request.Headers.Authorization.ToString();
        if (header.StartsWith("Bearer ", StringComparison.OrdinalIgnoreCase))
        {
            return header["Bearer ".Length..];
        }
        var queryToken = context.Request.Query["token"].ToString();
        return string.IsNullOrEmpty(queryToken) ? null : queryToken;
    }
}
