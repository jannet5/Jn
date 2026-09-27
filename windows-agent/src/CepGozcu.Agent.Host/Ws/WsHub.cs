using System.Collections.Concurrent;
using System.Net.WebSockets;
using System.Text;
using System.Text.Json;
using CepGozcu.Agent.Core.Data;
using CepGozcu.Agent.Core.Protocol;

namespace CepGozcu.Agent.Host.Ws;

/// <summary>
/// Owns every live authenticated WebSocket connection: reads request envelopes and dispatches
/// them to <see cref="MessageRouter"/>, and lets background services (metrics, disk, alerts)
/// push envelopes out to whichever devices are currently connected.
/// </summary>
public sealed class WsHub
{
    private sealed record Session(string Id, string DeviceId, WebSocket Socket);

    private readonly ConcurrentDictionary<string, Session> _sessions = new();
    private readonly MessageRouter _router;
    private readonly ILogger<WsHub> _logger;

    public WsHub(MessageRouter router, ILogger<WsHub> logger)
    {
        _router = router;
        _logger = logger;
    }

    public async Task HandleAsync(WebSocket socket, DeviceRecord device, CancellationToken ct)
    {
        var session = new Session(Guid.NewGuid().ToString("N"), device.Id, socket);
        _sessions[session.Id] = session;
        try
        {
            var buffer = new byte[16 * 1024];
            while (socket.State == WebSocketState.Open && !ct.IsCancellationRequested)
            {
                using var ms = new MemoryStream();
                WebSocketReceiveResult result;
                do
                {
                    result = await socket.ReceiveAsync(buffer, ct);
                    if (result.MessageType == WebSocketMessageType.Close)
                    {
                        await socket.CloseAsync(WebSocketCloseStatus.NormalClosure, null, ct);
                        return;
                    }
                    ms.Write(buffer, 0, result.Count);
                }
                while (!result.EndOfMessage);

                Envelope? envelope;
                try
                {
                    envelope = JsonSerializer.Deserialize<Envelope>(ms.ToArray(), WireJson.Options);
                }
                catch (JsonException)
                {
                    continue; // malformed frame — ignore rather than tear down the connection
                }
                if (envelope is null) continue;

                var response = await _router.RouteAsync(envelope, device, ct);
                if (response is not null)
                {
                    await SendAsync(socket, response, ct);
                }
            }
        }
        catch (OperationCanceledException)
        {
            // server shutting down or client dropped — normal
        }
        catch (WebSocketException ex)
        {
            _logger.LogInformation("WebSocket session {Session} for device {Device} ended: {Message}", session.Id, device.Id, ex.Message);
        }
        finally
        {
            _sessions.TryRemove(session.Id, out _);
        }
    }

    public static async Task SendAsync(WebSocket socket, Envelope envelope, CancellationToken ct)
    {
        var bytes = JsonSerializer.SerializeToUtf8Bytes(envelope, WireJson.Options);
        await socket.SendAsync(bytes, WebSocketMessageType.Text, true, ct);
    }

    /// <summary>Push to every currently-connected device (metrics, file events, alerts are broadcast — every paired device is a home owner watching the same PC).</summary>
    public void Broadcast(string type, object payload)
    {
        var envelope = new Envelope
        {
            Type = type,
            Ts = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds(),
            Payload = JsonSerializer.SerializeToElement(payload, WireJson.Options),
        };
        var bytes = JsonSerializer.SerializeToUtf8Bytes(envelope, WireJson.Options);

        foreach (var session in _sessions.Values)
        {
            if (session.Socket.State != WebSocketState.Open) continue;
            _ = session.Socket.SendAsync(bytes, WebSocketMessageType.Text, true, CancellationToken.None)
                .ContinueWith(t =>
                {
                    if (t.IsFaulted) _logger.LogDebug("Broadcast send failed for session {Session}", session.Id);
                }, TaskScheduler.Default);
        }
    }

    public void ForceDisconnect(string deviceId)
    {
        foreach (var session in _sessions.Values.Where(s => s.DeviceId == deviceId))
        {
            _ = session.Socket.CloseAsync(WebSocketCloseStatus.PolicyViolation, "revoked", CancellationToken.None);
        }
    }
}
