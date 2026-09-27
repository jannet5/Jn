using CepGozcu.Agent.Core.Protocol;

namespace CepGozcu.Agent.Core.Data;

public enum SnapshotGranularity { FiveMinutes, Hourly, Daily }

public sealed record SnapshotPoint(string FolderPath, DateTimeOffset BucketStart, long SizeBytes, int FileCount);

public sealed class FolderSnapshotRepository
{
    private readonly SqliteConnectionFactory _factory;

    public FolderSnapshotRepository(SqliteConnectionFactory factory) => _factory = factory;

    public void Upsert(string rootPath, string folderPath, SnapshotGranularity granularity, DateTimeOffset bucketStart, long sizeBytes, int fileCount)
    {
        using var conn = _factory.Open();
        using var cmd = conn.CreateCommand();
        cmd.CommandText = """
            INSERT INTO folder_snapshots (root_path, folder_path, size_bytes, file_count, granularity, bucket_start)
            VALUES ($root, $folder, $size, $count, $gran, $bucket)
            ON CONFLICT(folder_path, granularity, bucket_start) DO UPDATE SET
                size_bytes = excluded.size_bytes,
                file_count = excluded.file_count
            """;
        cmd.Parameters.AddWithValue("$root", rootPath);
        cmd.Parameters.AddWithValue("$folder", folderPath);
        cmd.Parameters.AddWithValue("$size", sizeBytes);
        cmd.Parameters.AddWithValue("$count", fileCount);
        cmd.Parameters.AddWithValue("$gran", granularity.ToString());
        cmd.Parameters.AddWithValue("$bucket", bucketStart.UtcDateTime.ToString("O"));
        cmd.ExecuteNonQuery();
    }

    /// <summary>Latest known size for every tracked folder under <paramref name="rootPath"/> at or before <paramref name="at"/>.</summary>
    public IReadOnlyDictionary<string, SnapshotPoint> SizesAsOf(string rootPath, SnapshotGranularity granularity, DateTimeOffset at)
    {
        using var conn = _factory.Open();
        using var cmd = conn.CreateCommand();
        cmd.CommandText = """
            SELECT folder_path, MAX(bucket_start) as latest, size_bytes, file_count
            FROM folder_snapshots
            WHERE root_path = $root AND granularity = $gran AND bucket_start <= $at
            GROUP BY folder_path
            """;
        // SQLite doesn't guarantee size_bytes corresponds to the MAX(bucket_start) row without
        // this trick: re-select using a correlated join instead of relying on GROUP BY column bleed.
        cmd.CommandText = """
            SELECT fs.folder_path, fs.bucket_start, fs.size_bytes, fs.file_count
            FROM folder_snapshots fs
            INNER JOIN (
                SELECT folder_path, MAX(bucket_start) AS max_bucket
                FROM folder_snapshots
                WHERE root_path = $root AND granularity = $gran AND bucket_start <= $at
                GROUP BY folder_path
            ) latest ON latest.folder_path = fs.folder_path AND latest.max_bucket = fs.bucket_start
            WHERE fs.root_path = $root AND fs.granularity = $gran
            """;
        cmd.Parameters.AddWithValue("$root", rootPath);
        cmd.Parameters.AddWithValue("$gran", granularity.ToString());
        cmd.Parameters.AddWithValue("$at", at.UtcDateTime.ToString("O"));

        using var reader = cmd.ExecuteReader();
        var results = new Dictionary<string, SnapshotPoint>();
        while (reader.Read())
        {
            var folder = reader.GetString(0);
            results[folder] = new SnapshotPoint(
                folder,
                DateTimeOffset.Parse(reader.GetString(1)),
                reader.GetInt64(2),
                reader.GetInt32(3));
        }
        return results;
    }

    public IReadOnlyList<GrowthPoint> Timeline(string folderPath, SnapshotGranularity granularity, DateTimeOffset from, DateTimeOffset to)
    {
        using var conn = _factory.Open();
        using var cmd = conn.CreateCommand();
        cmd.CommandText = """
            SELECT bucket_start, size_bytes FROM folder_snapshots
            WHERE folder_path = $folder AND granularity = $gran AND bucket_start BETWEEN $from AND $to
            ORDER BY bucket_start ASC
            """;
        cmd.Parameters.AddWithValue("$folder", folderPath);
        cmd.Parameters.AddWithValue("$gran", granularity.ToString());
        cmd.Parameters.AddWithValue("$from", from.UtcDateTime.ToString("O"));
        cmd.Parameters.AddWithValue("$to", to.UtcDateTime.ToString("O"));

        using var reader = cmd.ExecuteReader();
        var results = new List<GrowthPoint>();
        while (reader.Read())
        {
            results.Add(new GrowthPoint(DateTimeOffset.Parse(reader.GetString(0)), reader.GetInt64(1)));
        }
        return results;
    }

    public void DeleteOlderThan(SnapshotGranularity granularity, DateTimeOffset cutoff)
    {
        using var conn = _factory.Open();
        using var cmd = conn.CreateCommand();
        cmd.CommandText = "DELETE FROM folder_snapshots WHERE granularity=$gran AND bucket_start < $cutoff";
        cmd.Parameters.AddWithValue("$gran", granularity.ToString());
        cmd.Parameters.AddWithValue("$cutoff", cutoff.UtcDateTime.ToString("O"));
        cmd.ExecuteNonQuery();
    }

    /// <summary>All distinct folders with at least one snapshot at this granularity — used by the rollup job.</summary>
    public IReadOnlyList<string> DistinctFolders(SnapshotGranularity granularity)
    {
        using var conn = _factory.Open();
        using var cmd = conn.CreateCommand();
        cmd.CommandText = "SELECT DISTINCT folder_path FROM folder_snapshots WHERE granularity=$gran";
        cmd.Parameters.AddWithValue("$gran", granularity.ToString());
        using var reader = cmd.ExecuteReader();
        var results = new List<string>();
        while (reader.Read()) results.Add(reader.GetString(0));
        return results;
    }
}
