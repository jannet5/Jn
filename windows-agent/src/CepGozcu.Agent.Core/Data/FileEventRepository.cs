using CepGozcu.Agent.Core.Protocol;

namespace CepGozcu.Agent.Core.Data;

public sealed class FileEventRepository
{
    private readonly SqliteConnectionFactory _factory;

    public FileEventRepository(SqliteConnectionFactory factory) => _factory = factory;

    public long Insert(string rootPath, FileEventDto evt)
    {
        using var conn = _factory.Open();
        using var cmd = conn.CreateCommand();
        cmd.CommandText = """
            INSERT INTO file_events (root_path, path, old_path, kind, size_bytes, size_delta_bytes, coalesced_count, occurred_at, source)
            VALUES ($root, $path, $oldPath, $kind, $size, $delta, $coalesced, $occurredAt, $source)
            """;
        cmd.Parameters.AddWithValue("$root", rootPath);
        cmd.Parameters.AddWithValue("$path", evt.Path);
        cmd.Parameters.AddWithValue("$oldPath", (object?)evt.OldPath ?? DBNull.Value);
        cmd.Parameters.AddWithValue("$kind", evt.Kind.ToString());
        cmd.Parameters.AddWithValue("$size", evt.SizeBytes);
        cmd.Parameters.AddWithValue("$delta", evt.SizeDeltaBytes);
        cmd.Parameters.AddWithValue("$coalesced", evt.CoalescedCount);
        cmd.Parameters.AddWithValue("$occurredAt", evt.OccurredAt.UtcDateTime.ToString("O"));
        cmd.Parameters.AddWithValue("$source", evt.Source);
        cmd.ExecuteNonQuery();

        using var idCmd = conn.CreateCommand();
        idCmd.CommandText = "SELECT last_insert_rowid()";
        return (long)idCmd.ExecuteScalar()!;
    }

    public bool HasEventSince(string path, DateTimeOffset since)
    {
        using var conn = _factory.Open();
        using var cmd = conn.CreateCommand();
        cmd.CommandText = "SELECT 1 FROM file_events WHERE path=$path AND occurred_at > $since LIMIT 1";
        cmd.Parameters.AddWithValue("$path", path);
        cmd.Parameters.AddWithValue("$since", since.UtcDateTime.ToString("O"));
        return cmd.ExecuteScalar() is not null;
    }

    public DiskEventsPage Query(DiskEventsQuery query)
    {
        using var conn = _factory.Open();
        using var cmd = conn.CreateCommand();
        var kindsFilter = query.Kinds is { Length: > 0 }
            ? "AND kind IN (" + string.Join(",", query.Kinds.Select((k, i) => $"$kind{i}")) + ")"
            : "";

        cmd.CommandText = $"""
            SELECT id, path, old_path, kind, size_bytes, size_delta_bytes, coalesced_count, occurred_at, source
            FROM file_events
            WHERE (($root IS NULL) OR root_path = $root)
              AND ($since IS NULL OR occurred_at >= $since)
              AND ($until IS NULL OR occurred_at <= $until)
              AND ($cursor IS NULL OR id < $cursor)
              {kindsFilter}
            ORDER BY id DESC
            LIMIT $limit
            """;
        cmd.Parameters.AddWithValue("$root", (object?)query.RootPath ?? DBNull.Value);
        cmd.Parameters.AddWithValue("$since", (object?)query.Since?.UtcDateTime.ToString("O") ?? DBNull.Value);
        cmd.Parameters.AddWithValue("$until", (object?)query.Until?.UtcDateTime.ToString("O") ?? DBNull.Value);
        cmd.Parameters.AddWithValue("$cursor", (object?)query.Cursor ?? DBNull.Value);
        cmd.Parameters.AddWithValue("$limit", query.Limit + 1); // fetch one extra to know if there's a next page
        if (query.Kinds is { Length: > 0 })
        {
            for (int i = 0; i < query.Kinds.Length; i++)
            {
                cmd.Parameters.AddWithValue($"$kind{i}", query.Kinds[i].ToString());
            }
        }

        using var reader = cmd.ExecuteReader();
        var items = new List<FileEventDto>();
        while (reader.Read())
        {
            items.Add(new FileEventDto(
                Id: reader.GetInt64(0),
                Path: reader.GetString(1),
                OldPath: reader.IsDBNull(2) ? null : reader.GetString(2),
                Kind: Enum.Parse<FileEventKind>(reader.GetString(3)),
                SizeBytes: reader.GetInt64(4),
                SizeDeltaBytes: reader.GetInt64(5),
                CoalescedCount: reader.GetInt32(6),
                OccurredAt: DateTimeOffset.Parse(reader.GetString(7)),
                Source: reader.GetString(8)));
        }

        long? nextCursor = null;
        if (items.Count > query.Limit)
        {
            items.RemoveAt(items.Count - 1);
            nextCursor = items[^1].Id;
        }
        return new DiskEventsPage(items, nextCursor);
    }

    public IReadOnlyList<BiggestFileEntry> QueryBiggest(string rootPath, DateTimeOffset from, DateTimeOffset to, int top)
    {
        using var conn = _factory.Open();
        using var cmd = conn.CreateCommand();
        cmd.CommandText = """
            SELECT path, size_bytes, occurred_at, kind
            FROM file_events
            WHERE root_path = $root AND occurred_at >= $from AND occurred_at <= $to
              AND kind IN ('Created', 'Grew')
            ORDER BY size_delta_bytes DESC
            LIMIT $top
            """;
        cmd.Parameters.AddWithValue("$root", rootPath);
        cmd.Parameters.AddWithValue("$from", from.UtcDateTime.ToString("O"));
        cmd.Parameters.AddWithValue("$to", to.UtcDateTime.ToString("O"));
        cmd.Parameters.AddWithValue("$top", top);

        using var reader = cmd.ExecuteReader();
        var results = new List<BiggestFileEntry>();
        while (reader.Read())
        {
            results.Add(new BiggestFileEntry(
                Path: reader.GetString(0),
                SizeBytes: reader.GetInt64(1),
                SeenAt: DateTimeOffset.Parse(reader.GetString(2)),
                Kind: Enum.Parse<FileEventKind>(reader.GetString(3))));
        }
        return results;
    }

    public (int created, int deleted) CountCreatedDeleted(string folderPrefix, DateTimeOffset from, DateTimeOffset to)
    {
        using var conn = _factory.Open();
        using var cmd = conn.CreateCommand();
        cmd.CommandText = """
            SELECT
              SUM(CASE WHEN kind = 'Created' THEN 1 ELSE 0 END),
              SUM(CASE WHEN kind = 'Deleted' THEN 1 ELSE 0 END)
            FROM file_events
            WHERE path LIKE $prefix AND occurred_at >= $from AND occurred_at <= $to
            """;
        cmd.Parameters.AddWithValue("$prefix", folderPrefix.TrimEnd('\\', '/') + "%");
        cmd.Parameters.AddWithValue("$from", from.UtcDateTime.ToString("O"));
        cmd.Parameters.AddWithValue("$to", to.UtcDateTime.ToString("O"));

        using var reader = cmd.ExecuteReader();
        if (!reader.Read()) return (0, 0);
        var created = reader.IsDBNull(0) ? 0 : (int)reader.GetInt64(0);
        var deleted = reader.IsDBNull(1) ? 0 : (int)reader.GetInt64(1);
        return (created, deleted);
    }
}
