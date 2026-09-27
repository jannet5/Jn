using Microsoft.Data.Sqlite;

namespace CepGozcu.Agent.Core.Data;

public sealed record DeviceRecord(
    string Id,
    string Name,
    string PublicKey,
    string? TokenHash,
    string? RefreshTokenHash,
    string Scopes,
    DateTimeOffset CreatedAt,
    DateTimeOffset? ExpiresAt,
    bool Revoked,
    DateTimeOffset? LastSeenAt);

public sealed class DeviceRepository
{
    private readonly SqliteConnectionFactory _factory;

    public DeviceRepository(SqliteConnectionFactory factory) => _factory = factory;

    public void Insert(DeviceRecord device)
    {
        using var conn = _factory.Open();
        using var cmd = conn.CreateCommand();
        cmd.CommandText = """
            INSERT INTO devices (id, name, public_key, token_hash, refresh_token_hash, scopes, created_at, expires_at, revoked, last_seen_at)
            VALUES ($id, $name, $pk, $th, $rth, $scopes, $created, $expires, $revoked, $lastSeen)
            """;
        Bind(cmd, device);
        cmd.ExecuteNonQuery();
    }

    public void UpdateTokens(string deviceId, string? tokenHash, string? refreshTokenHash, DateTimeOffset? expiresAt)
    {
        using var conn = _factory.Open();
        using var cmd = conn.CreateCommand();
        cmd.CommandText = """
            UPDATE devices SET token_hash=$th, refresh_token_hash=$rth, expires_at=$expires WHERE id=$id
            """;
        cmd.Parameters.AddWithValue("$th", (object?)tokenHash ?? DBNull.Value);
        cmd.Parameters.AddWithValue("$rth", (object?)refreshTokenHash ?? DBNull.Value);
        cmd.Parameters.AddWithValue("$expires", (object?)expiresAt?.UtcDateTime.ToString("O") ?? DBNull.Value);
        cmd.Parameters.AddWithValue("$id", deviceId);
        cmd.ExecuteNonQuery();
    }

    public void TouchLastSeen(string deviceId, DateTimeOffset at)
    {
        using var conn = _factory.Open();
        using var cmd = conn.CreateCommand();
        cmd.CommandText = "UPDATE devices SET last_seen_at=$at WHERE id=$id";
        cmd.Parameters.AddWithValue("$at", at.UtcDateTime.ToString("O"));
        cmd.Parameters.AddWithValue("$id", deviceId);
        cmd.ExecuteNonQuery();
    }

    public void Revoke(string deviceId)
    {
        using var conn = _factory.Open();
        using var cmd = conn.CreateCommand();
        cmd.CommandText = "UPDATE devices SET revoked=1 WHERE id=$id";
        cmd.Parameters.AddWithValue("$id", deviceId);
        cmd.ExecuteNonQuery();
    }

    public DeviceRecord? FindById(string deviceId)
    {
        using var conn = _factory.Open();
        using var cmd = conn.CreateCommand();
        cmd.CommandText = "SELECT * FROM devices WHERE id=$id";
        cmd.Parameters.AddWithValue("$id", deviceId);
        using var reader = cmd.ExecuteReader();
        return reader.Read() ? Read(reader) : null;
    }

    public DeviceRecord? FindByTokenHash(string tokenHash)
    {
        using var conn = _factory.Open();
        using var cmd = conn.CreateCommand();
        cmd.CommandText = "SELECT * FROM devices WHERE token_hash=$th AND revoked=0";
        cmd.Parameters.AddWithValue("$th", tokenHash);
        using var reader = cmd.ExecuteReader();
        return reader.Read() ? Read(reader) : null;
    }

    public IReadOnlyList<DeviceRecord> ListAll()
    {
        using var conn = _factory.Open();
        using var cmd = conn.CreateCommand();
        cmd.CommandText = "SELECT * FROM devices ORDER BY created_at DESC";
        using var reader = cmd.ExecuteReader();
        var results = new List<DeviceRecord>();
        while (reader.Read()) results.Add(Read(reader));
        return results;
    }

    private static void Bind(SqliteCommand cmd, DeviceRecord d)
    {
        cmd.Parameters.AddWithValue("$id", d.Id);
        cmd.Parameters.AddWithValue("$name", d.Name);
        cmd.Parameters.AddWithValue("$pk", d.PublicKey);
        cmd.Parameters.AddWithValue("$th", (object?)d.TokenHash ?? DBNull.Value);
        cmd.Parameters.AddWithValue("$rth", (object?)d.RefreshTokenHash ?? DBNull.Value);
        cmd.Parameters.AddWithValue("$scopes", d.Scopes);
        cmd.Parameters.AddWithValue("$created", d.CreatedAt.UtcDateTime.ToString("O"));
        cmd.Parameters.AddWithValue("$expires", (object?)d.ExpiresAt?.UtcDateTime.ToString("O") ?? DBNull.Value);
        cmd.Parameters.AddWithValue("$revoked", d.Revoked ? 1 : 0);
        cmd.Parameters.AddWithValue("$lastSeen", (object?)d.LastSeenAt?.UtcDateTime.ToString("O") ?? DBNull.Value);
    }

    private static DeviceRecord Read(SqliteDataReader r) => new(
        Id: r.GetString(r.GetOrdinal("id")),
        Name: r.GetString(r.GetOrdinal("name")),
        PublicKey: r.GetString(r.GetOrdinal("public_key")),
        TokenHash: r.IsDBNull(r.GetOrdinal("token_hash")) ? null : r.GetString(r.GetOrdinal("token_hash")),
        RefreshTokenHash: r.IsDBNull(r.GetOrdinal("refresh_token_hash")) ? null : r.GetString(r.GetOrdinal("refresh_token_hash")),
        Scopes: r.GetString(r.GetOrdinal("scopes")),
        CreatedAt: DateTimeOffset.Parse(r.GetString(r.GetOrdinal("created_at"))),
        ExpiresAt: r.IsDBNull(r.GetOrdinal("expires_at")) ? null : DateTimeOffset.Parse(r.GetString(r.GetOrdinal("expires_at"))),
        Revoked: r.GetInt32(r.GetOrdinal("revoked")) != 0,
        LastSeenAt: r.IsDBNull(r.GetOrdinal("last_seen_at")) ? null : DateTimeOffset.Parse(r.GetString(r.GetOrdinal("last_seen_at"))));
}
