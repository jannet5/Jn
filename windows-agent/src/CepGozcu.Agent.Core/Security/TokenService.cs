using System.Security.Cryptography;
using CepGozcu.Agent.Core.Data;

namespace CepGozcu.Agent.Core.Security;

public sealed record IssuedTokens(string SessionToken, string RefreshToken, DateTimeOffset ExpiresAt);

/// <summary>
/// Opaque bearer tokens for paired devices. Tokens are random bytes, never JWTs — there is
/// nothing here that benefits from being self-describing, and an opaque token can't be forged
/// without the server's database, only stolen (which is what revocation and expiry are for).
/// Only a SHA-256 hash of each token is ever persisted, mirroring password-hash hygiene.
/// </summary>
public sealed class TokenService
{
    private static readonly TimeSpan SessionLifetime = TimeSpan.FromDays(90);
    private readonly DeviceRepository _devices;

    public TokenService(DeviceRepository devices) => _devices = devices;

    public IssuedTokens IssueFor(string deviceId)
    {
        var session = GenerateToken();
        var refresh = GenerateToken();
        var expiresAt = DateTimeOffset.UtcNow.Add(SessionLifetime);

        _devices.UpdateTokens(deviceId, Hash(session), Hash(refresh), expiresAt);
        return new IssuedTokens(session, refresh, expiresAt);
    }

    public DeviceRecord? ValidateSession(string rawToken)
    {
        var device = _devices.FindByTokenHash(Hash(rawToken));
        if (device is null) return null;
        if (device.Revoked) return null;
        if (device.ExpiresAt is { } exp && exp < DateTimeOffset.UtcNow) return null;
        return device;
    }

    public IssuedTokens? Refresh(string deviceId, string rawRefreshToken)
    {
        var device = _devices.FindById(deviceId);
        if (device is null || device.Revoked) return null;
        if (device.RefreshTokenHash != Hash(rawRefreshToken)) return null;
        return IssueFor(deviceId);
    }

    public void Revoke(string deviceId) => _devices.Revoke(deviceId);

    private static string GenerateToken() => Convert.ToBase64String(RandomNumberGenerator.GetBytes(32))
        .Replace('+', '-').Replace('/', '_').TrimEnd('=');

    private static string Hash(string token)
        => Convert.ToHexString(SHA256.HashData(System.Text.Encoding.UTF8.GetBytes(token)));
}
