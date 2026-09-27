using CepGozcu.Agent.Core.Protocol;
using CepGozcu.Agent.Core.Security;

namespace CepGozcu.Agent.Host.Pairing;

/// <summary>LAN-reachable pairing endpoints — the only unauthenticated surface the agent exposes to the network.</summary>
public static class PairingEndpoints
{
    public static void MapPairingEndpoints(this WebApplication app)
    {
        app.MapPost("/pair/verify", (PairVerifyRequest req, PairingService pairing) =>
        {
            if (string.IsNullOrWhiteSpace(req.PairingId) || string.IsNullOrWhiteSpace(req.Pin))
            {
                return Results.BadRequest();
            }
            return Results.Ok(pairing.Verify(req));
        });

        app.MapGet("/pair/status/{pairingId}", (string pairingId, PairingService pairing) =>
        {
            return Results.Ok(pairing.GetStatus(pairingId));
        });
    }
}
