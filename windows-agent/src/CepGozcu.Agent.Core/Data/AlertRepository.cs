using CepGozcu.Agent.Core.Protocol;

namespace CepGozcu.Agent.Core.Data;

public sealed class AlertRepository
{
    private readonly SqliteConnectionFactory _factory;

    public AlertRepository(SqliteConnectionFactory factory) => _factory = factory;

    public long Insert(AlertKind kind, AlertSeverity severity, string message, string? path)
    {
        using var conn = _factory.Open();
        using var cmd = conn.CreateCommand();
        cmd.CommandText = """
            INSERT INTO alerts (kind, severity, message, path, occurred_at, acknowledged)
            VALUES ($kind, $severity, $message, $path, $occurredAt, 0)
            """;
        cmd.Parameters.AddWithValue("$kind", kind.ToString());
        cmd.Parameters.AddWithValue("$severity", severity.ToString());
        cmd.Parameters.AddWithValue("$message", message);
        cmd.Parameters.AddWithValue("$path", (object?)path ?? DBNull.Value);
        cmd.Parameters.AddWithValue("$occurredAt", DateTimeOffset.UtcNow.ToString("O"));
        cmd.ExecuteNonQuery();

        using var idCmd = conn.CreateCommand();
        idCmd.CommandText = "SELECT last_insert_rowid()";
        return (long)idCmd.ExecuteScalar()!;
    }

    /// <summary>True if a similar unacknowledged alert already fired recently — used to avoid repeat-spamming the same condition.</summary>
    public bool HasRecentSimilar(AlertKind kind, string? path, TimeSpan within)
    {
        using var conn = _factory.Open();
        using var cmd = conn.CreateCommand();
        cmd.CommandText = """
            SELECT 1 FROM alerts
            WHERE kind=$kind AND (path=$path OR ($path IS NULL AND path IS NULL))
              AND occurred_at >= $since
            LIMIT 1
            """;
        cmd.Parameters.AddWithValue("$kind", kind.ToString());
        cmd.Parameters.AddWithValue("$path", (object?)path ?? DBNull.Value);
        cmd.Parameters.AddWithValue("$since", DateTimeOffset.UtcNow.Subtract(within).ToString("O"));
        return cmd.ExecuteScalar() is not null;
    }

    public IReadOnlyList<AlertDto> Query(AlertsQuery query)
    {
        using var conn = _factory.Open();
        using var cmd = conn.CreateCommand();
        cmd.CommandText = """
            SELECT id, kind, severity, message, path, occurred_at, acknowledged
            FROM alerts
            WHERE ($since IS NULL OR occurred_at >= $since)
              AND ($unackOnly = 0 OR acknowledged = 0)
            ORDER BY occurred_at DESC
            LIMIT $limit
            """;
        cmd.Parameters.AddWithValue("$since", (object?)query.Since?.UtcDateTime.ToString("O") ?? DBNull.Value);
        cmd.Parameters.AddWithValue("$unackOnly", query.UnacknowledgedOnly == true ? 1 : 0);
        cmd.Parameters.AddWithValue("$limit", query.Limit);

        using var reader = cmd.ExecuteReader();
        var results = new List<AlertDto>();
        while (reader.Read())
        {
            results.Add(new AlertDto(
                Id: reader.GetInt64(0),
                Kind: Enum.Parse<AlertKind>(reader.GetString(1)),
                Severity: Enum.Parse<AlertSeverity>(reader.GetString(2)),
                Message: reader.GetString(3),
                Path: reader.IsDBNull(4) ? null : reader.GetString(4),
                OccurredAt: DateTimeOffset.Parse(reader.GetString(5)),
                Acknowledged: reader.GetInt32(6) != 0));
        }
        return results;
    }
}
