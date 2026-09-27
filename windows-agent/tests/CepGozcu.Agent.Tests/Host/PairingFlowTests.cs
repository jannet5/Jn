using System.Net.Http.Json;
using CepGozcu.Agent.Core.Protocol;
using CepGozcu.Agent.Host.Admin;
using Microsoft.AspNetCore.Mvc.Testing;
using Xunit;

namespace CepGozcu.Agent.Tests.Host;

/// <summary>
/// Full pairing round-trip through the real ASP.NET Core pipeline (middleware, DI, SQLite —
/// everything except an actual TCP socket and TLS handshake, which the manual smoke test in the
/// project already covers against a real Kestrel listener). Confirms an unauthenticated client
/// truly cannot mint a session without both the PIN and the local-admin approval step.
///
/// All JSON calls pass WireJson.Options explicitly: that's the same option set the server's
/// ConfigureHttpJsonOptions uses, and HttpClient's JSON extensions don't pick that up on their
/// own — a plain ReadFromJsonAsync&lt;T&gt;() here would use its own unrelated defaults and fail
/// to parse the server's camelCase string enums.
/// </summary>
public class PairingFlowTests : IClassFixture<AgentWebApplicationFactory>
{
    private readonly AgentWebApplicationFactory _factory;

    public PairingFlowTests(AgentWebApplicationFactory factory) => _factory = factory;

    [Fact]
    public async Task FullPairingFlow_RequiresPinAndLocalApproval()
    {
        var client = _factory.CreateClient();

        var begin = await client.PostAsync("/admin/pair/begin", null);
        begin.EnsureSuccessStatusCode();
        var beginBody = await begin.Content.ReadFromJsonAsync<BeginPairingResponse>(WireJson.Options);
        Assert.NotNull(beginBody);

        // Wrong PIN never advances the state.
        var wrongVerify = await client.PostAsJsonAsync("/pair/verify", new PairVerifyRequest(beginBody!.PairingId, "000000", "TestPhone", "pk"), WireJson.Options);
        var wrongResult = await wrongVerify.Content.ReadFromJsonAsync<PairVerifyResponse>(WireJson.Options);
        Assert.Equal(PairingState.AwaitingPin, wrongResult!.State);

        // Correct PIN -> PendingApproval, but still no token yet.
        var verify = await client.PostAsJsonAsync("/pair/verify", new PairVerifyRequest(beginBody.PairingId, beginBody.Pin, "TestPhone", "pk"), WireJson.Options);
        var verifyResult = await verify.Content.ReadFromJsonAsync<PairVerifyResponse>(WireJson.Options);
        Assert.Equal(PairingState.PendingApproval, verifyResult!.State);
        Assert.Null(verifyResult.SessionToken);

        // Polling before approval still yields no token.
        var beforeApprovalResponse = await client.GetAsync($"/pair/status/{beginBody.PairingId}");
        var beforeApproval = await beforeApprovalResponse.Content.ReadFromJsonAsync<PairVerifyResponse>(WireJson.Options);
        Assert.Equal(PairingState.PendingApproval, beforeApproval!.State);

        // The local admin approves (this is the loopback-only, human-in-the-loop step).
        var approve = await client.PostAsync($"/admin/pair/approve/{beginBody.PairingId}", null);
        approve.EnsureSuccessStatusCode();

        var afterApprovalResponse = await client.GetAsync($"/pair/status/{beginBody.PairingId}");
        var afterApproval = await afterApprovalResponse.Content.ReadFromJsonAsync<PairVerifyResponse>(WireJson.Options);
        Assert.Equal(PairingState.Approved, afterApproval!.State);
        Assert.False(string.IsNullOrEmpty(afterApproval.SessionToken));
        Assert.False(string.IsNullOrEmpty(afterApproval.DeviceId));

        var devicesResponse = await client.GetAsync("/admin/devices");
        var devices = await devicesResponse.Content.ReadFromJsonAsync<List<System.Text.Json.JsonElement>>(WireJson.Options);
        Assert.Contains(devices!, d => d.GetProperty("id").GetString() == afterApproval.DeviceId);
    }

    [Fact]
    public async Task RejectedPairingNeverYieldsAToken()
    {
        var client = _factory.CreateClient();

        var begin = await client.PostAsync("/admin/pair/begin", null);
        var beginBody = await begin.Content.ReadFromJsonAsync<BeginPairingResponse>(WireJson.Options);

        await client.PostAsJsonAsync("/pair/verify", new PairVerifyRequest(beginBody!.PairingId, beginBody.Pin, "TestPhone", "pk"), WireJson.Options);
        await client.PostAsync($"/admin/pair/reject/{beginBody.PairingId}", null);

        var statusResponse = await client.GetAsync($"/pair/status/{beginBody.PairingId}");
        var status = await statusResponse.Content.ReadFromJsonAsync<PairVerifyResponse>(WireJson.Options);
        Assert.Equal(PairingState.Rejected, status!.State);
        Assert.Null(status.SessionToken);
    }
}
