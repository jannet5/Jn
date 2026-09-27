using Microsoft.Data.Sqlite;

namespace CepGozcu.Agent.Core.Data;

internal static class Schema
{
    public const string CurrentVersion = "1";

    public static void EnsureCreated(SqliteConnection conn)
    {
        using var cmd = conn.CreateCommand();
        cmd.CommandText = """
            CREATE TABLE IF NOT EXISTS devices (
                id TEXT PRIMARY KEY,
                name TEXT NOT NULL,
                public_key TEXT NOT NULL,
                token_hash TEXT,
                refresh_token_hash TEXT,
                scopes TEXT NOT NULL,
                created_at TEXT NOT NULL,
                expires_at TEXT,
                revoked INTEGER NOT NULL DEFAULT 0,
                last_seen_at TEXT
            );

            CREATE TABLE IF NOT EXISTS file_events (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                root_path TEXT NOT NULL,
                path TEXT NOT NULL,
                old_path TEXT,
                kind TEXT NOT NULL,
                size_bytes INTEGER NOT NULL,
                size_delta_bytes INTEGER NOT NULL,
                coalesced_count INTEGER NOT NULL DEFAULT 1,
                occurred_at TEXT NOT NULL,
                source TEXT NOT NULL
            );
            CREATE INDEX IF NOT EXISTS ix_file_events_root_time ON file_events(root_path, occurred_at);
            CREATE INDEX IF NOT EXISTS ix_file_events_path ON file_events(path);

            CREATE TABLE IF NOT EXISTS file_cache (
                path TEXT PRIMARY KEY,
                root_path TEXT NOT NULL,
                size_bytes INTEGER NOT NULL,
                last_write_utc TEXT NOT NULL,
                last_seen_scan_id INTEGER NOT NULL
            );
            CREATE INDEX IF NOT EXISTS ix_file_cache_root ON file_cache(root_path);

            CREATE TABLE IF NOT EXISTS folder_snapshots (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                root_path TEXT NOT NULL,
                folder_path TEXT NOT NULL,
                size_bytes INTEGER NOT NULL,
                file_count INTEGER NOT NULL,
                granularity TEXT NOT NULL,
                bucket_start TEXT NOT NULL
            );
            CREATE INDEX IF NOT EXISTS ix_snapshots_folder_time ON folder_snapshots(folder_path, granularity, bucket_start);
            CREATE UNIQUE INDEX IF NOT EXISTS ux_snapshots_bucket ON folder_snapshots(folder_path, granularity, bucket_start);

            CREATE TABLE IF NOT EXISTS alerts (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                kind TEXT NOT NULL,
                severity TEXT NOT NULL,
                message TEXT NOT NULL,
                path TEXT,
                occurred_at TEXT NOT NULL,
                acknowledged INTEGER NOT NULL DEFAULT 0
            );
            CREATE INDEX IF NOT EXISTS ix_alerts_time ON alerts(occurred_at);

            CREATE TABLE IF NOT EXISTS audit_log (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                device_id TEXT NOT NULL,
                device_name TEXT NOT NULL,
                action TEXT NOT NULL,
                target TEXT,
                success INTEGER NOT NULL,
                reason TEXT,
                occurred_at TEXT NOT NULL
            );
            CREATE INDEX IF NOT EXISTS ix_audit_time ON audit_log(occurred_at);

            CREATE TABLE IF NOT EXISTS watched_roots (
                path TEXT PRIMARY KEY,
                enabled INTEGER NOT NULL DEFAULT 1,
                last_full_scan_at TEXT
            );
            """;
        cmd.ExecuteNonQuery();
    }
}
