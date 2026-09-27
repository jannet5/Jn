using System.Text.Json;
using CepGozcu.Agent.Core.Data;
using CepGozcu.Agent.Core.Launcher;
using CepGozcu.Agent.Core.Disk;
using CepGozcu.Agent.Core.Protocol;
using CepGozcu.Agent.Core.Security;
using CepGozcu.Agent.Host.Ws;
using QRCoder;

namespace CepGozcu.Agent.Host.Admin;

public sealed record BeginPairingResponse(string PairingId, string Pin, int ExpiresInSeconds, string QrPngBase64);
public sealed record AddAllowedAppRequest(string DisplayName, string Path);
public sealed record ToggleWatchedRootRequest(string Path, bool Enabled);

/// <summary>
/// Loopback-only admin surface: pairing approval, the app allowlist, watched roots, paired
/// devices and the audit log. Nothing here is reachable from the phone or anywhere else on
/// the LAN — see <see cref="Middleware.LoopbackOnlyMiddleware"/>.
/// </summary>
public static class AdminEndpoints
{
    public static void MapAdminEndpoints(this WebApplication app)
    {
        var admin = app.MapGroup("/admin");

        admin.MapPost("/pair/begin", (PairingService pairing) =>
        {
            var (pending, info) = pairing.BeginLocal();
            var qrPayload = JsonSerializer.Serialize(info, WireJson.Options);
            var qrGenerator = new QRCodeGenerator();
            var qrData = qrGenerator.CreateQrCode(qrPayload, QRCodeGenerator.ECCLevel.M);
            var png = new PngByteQRCode(qrData).GetGraphic(8);
            return Results.Ok(new BeginPairingResponse(pending.PairingId, pending.Pin, info.PinExpiresInSeconds, Convert.ToBase64String(png)));
        });

        admin.MapGet("/pair/pending", (PairingService pairing) =>
            Results.Ok(pairing.ListPendingApprovals().Select(p => new { p.PairingId, p.DeviceName, p.ApprovalExpiresAt })));

        admin.MapPost("/pair/approve/{pairingId}", (string pairingId, PairingService pairing) =>
            pairing.Approve(pairingId) ? Results.Ok() : Results.NotFound());

        admin.MapPost("/pair/reject/{pairingId}", (string pairingId, PairingService pairing) =>
            pairing.Reject(pairingId) ? Results.Ok() : Results.NotFound());

        admin.MapGet("/devices", (DeviceRepository devices) =>
            Results.Ok(devices.ListAll().Select(d => new { d.Id, d.Name, d.CreatedAt, d.LastSeenAt, d.Revoked })));

        admin.MapPost("/devices/{id}/revoke", (string id, TokenService tokens, WsHub hub, AuditRepository audit) =>
        {
            tokens.Revoke(id);
            hub.ForceDisconnect(id);
            audit.Record(id, id, "device.revoke.local", null, true, null);
            return Results.Ok();
        });

        admin.MapGet("/allowlist", (AppAllowlistStore store) => Results.Ok(store.List()));

        admin.MapPost("/allowlist", (AddAllowedAppRequest req, AppAllowlistStore store) =>
        {
            if (string.IsNullOrWhiteSpace(req.DisplayName) || string.IsNullOrWhiteSpace(req.Path))
            {
                return Results.BadRequest("displayName ve path zorunludur.");
            }
            return Results.Ok(store.Add(req.DisplayName, req.Path, null));
        });

        admin.MapDelete("/allowlist/{id}", (string id, AppAllowlistStore store) =>
            store.Remove(id) ? Results.Ok() : Results.NotFound());

        admin.MapGet("/watched-roots", (WatchedRootsStore store) => Results.Ok(store.List()));

        admin.MapPost("/watched-roots/toggle", (ToggleWatchedRootRequest req, WatchedRootsStore store) =>
        {
            store.SetEnabled(req.Path, req.Enabled);
            return Results.Ok();
        });

        admin.MapGet("/audit", (AuditRepository audit) => Results.Ok(audit.Query(new AuditQuery(null, 200))));
    }
}
