using CepGozcu.Agent.Core.Protocol;

namespace CepGozcu.Agent.Core.Data;

public sealed class AuditRepository
{
    private readonly SqliteConnectionFactory _factory;

    public AuditRepository(SqliteConnectionFactory factory) => _factory = factory;

    public void Record(string deviceId, string deviceName, string action, string? target, bool success, string? reason)
    {
        using var conn = _factory.Open();
        using var cmd = conn.CreateCommand();
        cmd.CommandText = """
            INSERT INTO audit_log (device_id, device_name, action, target, success, reason, occurred_at)
            VALUES ($deviceId, $deviceName, $action, $target, $success, $reason, $occurredAt)
            """;
        cmd.Parameters.AddWithValue("$deviceId", deviceId);
        cmd.Parameters.AddWithValue("$deviceName", deviceName);
        cmd.Parameters.AddWithValue("$action", action);
        cmd.Parameters.AddWithValue("$target", (object?)target ?? DBNull.Value);
        cmd.Parameters.AddWithValue("$success", success ? 1 : 0);
        cmd.Parameters.AddWithValue("$reason", (object?)reason ?? DBNull.Value);
        cmd.Parameters.AddWithValue("$occurredAt", DateTimeOffset.UtcNow.ToString("O"));
        cmd.ExecuteNonQuery();
    }

    public IReadOnlyList<AuditEntry> Query(AuditQuery query)
    {
        using var conn = _factory.Open();
        using var cmd = conn.CreateCommand();
        cmd.CommandText = """
            SELECT id, device_id, device_name, action, target, success, reason, occurred_at
            FROM audit_log
            WHERE ($since IS NULL OR occurred_at >= $since)
            ORDER BY occurred_at DESC
            LIMIT $limit
            """;
        cmd.Parameters.AddWithValue("$since", (object?)query.Since?.UtcDateTime.ToString("O") ?? DBNull.Value);
        cmd.Parameters.AddWithValue("$limit", query.Limit);

        using var reader = cmd.ExecuteReader();
        var results = new List<AuditEntry>();
        while (reader.Read())
        {
            results.Add(new AuditEntry(
                Id: reader.GetInt64(0),
                DeviceId: reader.GetString(1),
                DeviceName: reader.GetString(2),
                Action: reader.GetString(3),
                Target: reader.IsDBNull(4) ? null : reader.GetString(4),
                Success: reader.GetInt32(5) != 0,
                Reason: reader.IsDBNull(6) ? null : reader.GetString(6),
                OccurredAt: DateTimeOffset.Parse(reader.GetString(7))));
        }
        return results;
    }
}
