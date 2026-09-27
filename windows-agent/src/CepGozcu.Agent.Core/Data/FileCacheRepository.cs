namespace CepGozcu.Agent.Core.Data;

public sealed record FileCacheRow(string Path, long SizeBytes, DateTimeOffset LastWriteUtc);

/// <summary>
/// Ground truth for "what did we last see on disk for this file" — the periodic <c>DiskScanner</c>
/// diffs against this table to detect changes the live FileSystemWatcher might have missed
/// (buffer overflow, app was offline, etc.) and to notice deletions.
/// </summary>
public sealed class FileCacheRepository
{
    private readonly SqliteConnectionFactory _factory;

    public FileCacheRepository(SqliteConnectionFactory factory) => _factory = factory;

    public FileCacheRow? Get(string path)
    {
        using var conn = _factory.Open();
        using var cmd = conn.CreateCommand();
        cmd.CommandText = "SELECT path, size_bytes, last_write_utc FROM file_cache WHERE path=$path";
        cmd.Parameters.AddWithValue("$path", path);
        using var reader = cmd.ExecuteReader();
        if (!reader.Read()) return null;
        return new FileCacheRow(reader.GetString(0), reader.GetInt64(1), DateTimeOffset.Parse(reader.GetString(2)));
    }

    public void Upsert(string rootPath, string path, long sizeBytes, DateTimeOffset lastWriteUtc, long scanId)
    {
        using var conn = _factory.Open();
        using var cmd = conn.CreateCommand();
        cmd.CommandText = """
            INSERT INTO file_cache (path, root_path, size_bytes, last_write_utc, last_seen_scan_id)
            VALUES ($path, $root, $size, $lastWrite, $scanId)
            ON CONFLICT(path) DO UPDATE SET
                size_bytes=excluded.size_bytes,
                last_write_utc=excluded.last_write_utc,
                last_seen_scan_id=excluded.last_seen_scan_id
            """;
        cmd.Parameters.AddWithValue("$path", path);
        cmd.Parameters.AddWithValue("$root", rootPath);
        cmd.Parameters.AddWithValue("$size", sizeBytes);
        cmd.Parameters.AddWithValue("$lastWrite", lastWriteUtc.UtcDateTime.ToString("O"));
        cmd.Parameters.AddWithValue("$scanId", scanId);
        cmd.ExecuteNonQuery();
    }

    public void Delete(string path)
    {
        using var conn = _factory.Open();
        using var cmd = conn.CreateCommand();
        cmd.CommandText = "DELETE FROM file_cache WHERE path=$path";
        cmd.Parameters.AddWithValue("$path", path);
        cmd.ExecuteNonQuery();
    }

    /// <summary>Paths under <paramref name="rootPath"/> not touched by the given scan id — i.e. deleted since last scan.</summary>
    public IReadOnlyList<string> FindStale(string rootPath, long currentScanId)
    {
        using var conn = _factory.Open();
        using var cmd = conn.CreateCommand();
        cmd.CommandText = "SELECT path FROM file_cache WHERE root_path=$root AND last_seen_scan_id < $scanId";
        cmd.Parameters.AddWithValue("$root", rootPath);
        cmd.Parameters.AddWithValue("$scanId", currentScanId);
        using var reader = cmd.ExecuteReader();
        var results = new List<string>();
        while (reader.Read()) results.Add(reader.GetString(0));
        return results;
    }
}
