using CepGozcu.Agent.Core.Data;

namespace CepGozcu.Agent.Tests.TestSupport;

/// <summary>A throwaway SQLite database file per test, cleaned up on dispose.</summary>
public sealed class TempDb : IDisposable
{
    public string Path { get; }
    public SqliteConnectionFactory Factory { get; }

    public TempDb()
    {
        Path = System.IO.Path.Combine(System.IO.Path.GetTempPath(), $"cepgozcu-test-{Guid.NewGuid():N}.db");
        Factory = new SqliteConnectionFactory(Path);
    }

    public void Dispose()
    {
        try
        {
            Microsoft.Data.Sqlite.SqliteConnection.ClearAllPools();
            if (File.Exists(Path)) File.Delete(Path);
            foreach (var extra in new[] { Path + "-wal", Path + "-shm" })
            {
                if (File.Exists(extra)) File.Delete(extra);
            }
        }
        catch (IOException)
        {
            // best-effort cleanup — leftover temp files don't fail the test run
        }
    }
}

public sealed class TempDirectory : IDisposable
{
    public string Path { get; }

    public TempDirectory()
    {
        Path = System.IO.Path.Combine(System.IO.Path.GetTempPath(), $"cepgozcu-test-dir-{Guid.NewGuid():N}");
        Directory.CreateDirectory(Path);
    }

    public void Dispose()
    {
        try { Directory.Delete(Path, recursive: true); }
        catch (IOException) { }
        catch (UnauthorizedAccessException) { }
    }
}
