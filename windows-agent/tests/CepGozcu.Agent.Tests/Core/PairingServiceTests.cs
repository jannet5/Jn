using CepGozcu.Agent.Core.Data;
using CepGozcu.Agent.Core.Protocol;
using CepGozcu.Agent.Core.Security;
using CepGozcu.Agent.Tests.TestSupport;
using Xunit;

namespace CepGozcu.Agent.Tests.Core;

public class PairingServiceTests : IDisposable
{
    private readonly TempDb _db = new();
    private readonly TempDirectory _certDir = new();
    private readonly PairingService _pairing;

    public PairingServiceTests()
    {
        var devices = new DeviceRepository(_db.Factory);
        var tokens = new TokenService(devices);
        var cert = new CertificateProvider(_certDir.Path).GetOrCreate();
        _pairing = new PairingService(devices, tokens, cert, 47811);
    }

    public void Dispose()
    {
        _db.Dispose();
        _certDir.Dispose();
    }

    [Fact]
    public void CorrectPinMovesToPendingApproval()
    {
        var (pending, _) = _pairing.BeginLocal();

        var result = _pairing.Verify(new PairVerifyRequest(pending.PairingId, pending.Pin, "Phone", "pubkey"));

        Assert.Equal(PairingState.PendingApproval, result.State);
    }

    [Fact]
    public void WrongPinStaysAwaitingAndDoesNotLeakState()
    {
        var (pending, _) = _pairing.BeginLocal();

        var result = _pairing.Verify(new PairVerifyRequest(pending.PairingId, "000000", "Phone", "pubkey"));

        Assert.Equal(PairingState.AwaitingPin, result.State);
        Assert.Null(result.SessionToken);
    }

    [Fact]
    public void UnknownPairingIdIsExpired()
    {
        var result = _pairing.Verify(new PairVerifyRequest("not-a-real-id", "123456", "Phone", "pubkey"));
        Assert.Equal(PairingState.Expired, result.State);
    }

    [Fact]
    public void ApprovalIssuesUsableSessionToken()
    {
        var (pending, _) = _pairing.BeginLocal();
        _pairing.Verify(new PairVerifyRequest(pending.PairingId, pending.Pin, "Phone", "pubkey"));

        var approved = _pairing.Approve(pending.PairingId);
        var status = _pairing.GetStatus(pending.PairingId);

        Assert.True(approved);
        Assert.Equal(PairingState.Approved, status.State);
        Assert.False(string.IsNullOrEmpty(status.SessionToken));
        Assert.False(string.IsNullOrEmpty(status.DeviceId));
    }

    [Fact]
    public void RejectionNeverIssuesATokenEvenIfPolledAgain()
    {
        var (pending, _) = _pairing.BeginLocal();
        _pairing.Verify(new PairVerifyRequest(pending.PairingId, pending.Pin, "Phone", "pubkey"));

        _pairing.Reject(pending.PairingId);
        var status = _pairing.GetStatus(pending.PairingId);

        Assert.Equal(PairingState.Rejected, status.State);
        Assert.Null(status.SessionToken);
    }

    [Fact]
    public void CannotApproveBeforePinIsVerified()
    {
        var (pending, _) = _pairing.BeginLocal();

        var approved = _pairing.Approve(pending.PairingId); // no Verify() call first

        Assert.False(approved);
    }

    [Fact]
    public void BeginLocalNeverExposesThePinOverTheWireContract()
    {
        var (pending, clientInfo) = _pairing.BeginLocal();

        // PairBeginResponse is exactly what would be encoded into the QR the phone scans —
        // it must not carry the PIN value itself, since the PIN's whole point is a separate,
        // human-read channel (the phone has to be told it by someone looking at the PC).
        var json = System.Text.Json.JsonSerializer.Serialize(clientInfo, WireJson.Options);
        Assert.DoesNotContain(pending.Pin, json);
    }
}
