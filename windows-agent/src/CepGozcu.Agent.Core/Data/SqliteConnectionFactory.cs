using Microsoft.Data.Sqlite;

namespace CepGozcu.Agent.Core.Data;

/// <summary>
/// Opens connections to the single agent SQLite database (devices, disk history, alerts, audit).
/// WAL mode keeps the periodic scanner's writes from blocking the WebSocket handlers' reads.
/// </summary>
public sealed class SqliteConnectionFactory
{
    private readonly string _connectionString;

    public SqliteConnectionFactory(string dbFilePath)
    {
        var dir = Path.GetDirectoryName(dbFilePath);
        if (!string.IsNullOrEmpty(dir)) Directory.CreateDirectory(dir);

        _connectionString = new SqliteConnectionStringBuilder
        {
            DataSource = dbFilePath,
            Mode = SqliteOpenMode.ReadWriteCreate,
            Cache = SqliteCacheMode.Shared,
        }.ToString();

        using var conn = Open();
        Schema.EnsureCreated(conn);
    }

    public SqliteConnection Open()
    {
        var conn = new SqliteConnection(_connectionString);
        conn.Open();
        using (var pragma = conn.CreateCommand())
        {
            pragma.CommandText = "PRAGMA journal_mode=WAL; PRAGMA foreign_keys=ON; PRAGMA busy_timeout=5000;";
            pragma.ExecuteNonQuery();
        }
        return conn;
    }
}
