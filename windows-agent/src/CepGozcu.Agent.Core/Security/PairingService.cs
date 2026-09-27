using System.Collections.Concurrent;
using System.Security.Cryptography;
using System.Security.Cryptography.X509Certificates;
using CepGozcu.Agent.Core.Data;
using CepGozcu.Agent.Core.Protocol;

namespace CepGozcu.Agent.Core.Security;

public sealed class PendingPairing
{
    public required string PairingId { get; init; }
    public required string Pin { get; init; }
    public required DateTimeOffset PinExpiresAt { get; set; }
    public required DateTimeOffset ApprovalExpiresAt { get; set; }
    public PairingState State { get; set; } = PairingState.AwaitingPin;
    public string? DeviceName { get; set; }
    public string? DevicePublicKeyBase64 { get; set; }
    public string? ApprovedDeviceId { get; set; }
}

/// <summary>
/// Two-factor pairing: (1) the phone must present a one-time PIN that is only ever shown on
/// the PC's own screen (proves the person is physically at the machine), and (2) even after
/// the correct PIN, a human must click Approve on that same local screen (explicit consent —
/// see product requirement "riskli işlemlerde açık kullanıcı onayı"). Neither factor alone is
/// enough to mint a session token.
/// </summary>
public sealed class PairingService
{
    private static readonly TimeSpan PinWindow = TimeSpan.FromMinutes(2);
    private static readonly TimeSpan ApprovalWindow = TimeSpan.FromMinutes(5);

    private readonly ConcurrentDictionary<string, PendingPairing> _pending = new();
    private readonly DeviceRepository _devices;
    private readonly TokenService _tokens;
    private readonly X509Certificate2 _certificate;
    private readonly int _port;

    public PairingService(DeviceRepository devices, TokenService tokens, X509Certificate2 certificate, int port)
    {
        _devices = devices;
        _tokens = tokens;
        _certificate = certificate;
        _port = port;
    }

    /// <summary>Loopback-only: starts a new pairing session and returns what the admin page renders as PIN/QR.</summary>
    public (PendingPairing Pending, PairBeginResponse ClientInfo) BeginLocal()
    {
        var pairingId = Guid.NewGuid().ToString("N");
        var pin = RandomNumberGenerator.GetInt32(0, 1_000_000).ToString("D6");
        var now = DateTimeOffset.UtcNow;

        var pending = new PendingPairing
        {
            PairingId = pairingId,
            Pin = pin,
            PinExpiresAt = now.Add(PinWindow),
            ApprovalExpiresAt = now.Add(PinWindow), // extended once the pin step succeeds
        };
        _pending[pairingId] = pending;

        var info = new PairBeginResponse(
            PairingId: pairingId,
            HostCandidates: LanAddressHelper.GetOwnPrivateIPv4Addresses().ToArray(),
            Port: _port,
            CertSha256: CertificateProvider.Fingerprint(_certificate),
            PinExpiresInSeconds: (int)PinWindow.TotalSeconds);

        return (pending, info);
    }

    /// <summary>LAN-reachable: the phone submits the PIN it was told to read off the PC screen.</summary>
    public PairVerifyResponse Verify(PairVerifyRequest request)
    {
        if (!_pending.TryGetValue(request.PairingId, out var pending))
        {
            return new PairVerifyResponse(PairingState.Expired, null, null, null, null);
        }

        var now = DateTimeOffset.UtcNow;
        if (pending.State == PairingState.AwaitingPin && now > pending.PinExpiresAt)
        {
            pending.State = PairingState.Expired;
        }

        if (pending.State != PairingState.AwaitingPin)
        {
            return new PairVerifyResponse(pending.State, null, null, null, null);
        }

        if (!FixedTimeEquals(pending.Pin, request.Pin))
        {
            // Deliberately don't distinguish "wrong pin" from "unknown id" in the response to
            // avoid helping an attacker brute-force the 6-digit PIN via a timing/enumeration oracle.
            return new PairVerifyResponse(PairingState.AwaitingPin, null, null, null, null);
        }

        pending.DeviceName = string.IsNullOrWhiteSpace(request.DeviceName) ? "Bilinmeyen cihaz" : request.DeviceName.Trim();
        pending.DevicePublicKeyBase64 = request.DevicePublicKeyBase64;
        pending.State = PairingState.PendingApproval;
        pending.ApprovalExpiresAt = now.Add(ApprovalWindow);
        return new PairVerifyResponse(PairingState.PendingApproval, null, null, null, null);
    }

    /// <summary>LAN-reachable: the phone polls this until a human approves/rejects on the PC.</summary>
    public PairVerifyResponse GetStatus(string pairingId)
    {
        if (!_pending.TryGetValue(pairingId, out var pending))
        {
            return new PairVerifyResponse(PairingState.Expired, null, null, null, null);
        }

        if (pending.State == PairingState.PendingApproval && DateTimeOffset.UtcNow > pending.ApprovalExpiresAt)
        {
            pending.State = PairingState.Expired;
        }

        if (pending.State == PairingState.Approved && pending.ApprovedDeviceId is { } deviceId)
        {
            var issued = _tokens.IssueFor(deviceId);
            _pending.TryRemove(pairingId, out _);
            return new PairVerifyResponse(PairingState.Approved, issued.SessionToken, issued.RefreshToken, deviceId, issued.ExpiresAt);
        }

        return new PairVerifyResponse(pending.State, null, null, null, null);
    }

    public IReadOnlyList<PendingPairing> ListPendingApprovals()
        => _pending.Values.Where(p => p.State == PairingState.PendingApproval).ToList();

    /// <summary>Loopback-only: a human clicked "Onayla" on the admin page.</summary>
    public bool Approve(string pairingId)
    {
        if (!_pending.TryGetValue(pairingId, out var pending) || pending.State != PairingState.PendingApproval)
        {
            return false;
        }

        var deviceId = Guid.NewGuid().ToString("N");
        _devices.Insert(new DeviceRecord(
            Id: deviceId,
            Name: pending.DeviceName ?? "Bilinmeyen cihaz",
            PublicKey: pending.DevicePublicKeyBase64 ?? "",
            TokenHash: null,
            RefreshTokenHash: null,
            Scopes: "read,control",
            CreatedAt: DateTimeOffset.UtcNow,
            ExpiresAt: null,
            Revoked: false,
            LastSeenAt: null));

        pending.ApprovedDeviceId = deviceId;
        pending.State = PairingState.Approved;
        return true;
    }

    /// <summary>Loopback-only: a human clicked "Reddet" on the admin page.</summary>
    public bool Reject(string pairingId)
    {
        if (!_pending.TryGetValue(pairingId, out var pending) || pending.State != PairingState.PendingApproval)
        {
            return false;
        }
        pending.State = PairingState.Rejected;
        return true;
    }

    private static bool FixedTimeEquals(string a, string b)
    {
        var ab = System.Text.Encoding.UTF8.GetBytes(a);
        var bb = System.Text.Encoding.UTF8.GetBytes(b);
        if (ab.Length != bb.Length) return false;
        return CryptographicOperations.FixedTimeEquals(ab, bb);
    }
}
