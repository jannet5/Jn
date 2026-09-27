// Package agent is the wiring layer: it uses gopsutil, fsnotify,
// modernc.org/sqlite and gorilla/websocket to implement the actual running
// agent described by PROTOCOL.md, on top of the pure logic in
// internal/core.
package agent

import (
	"database/sql"
	"encoding/json"
	"fmt"

	_ "modernc.org/sqlite"

	"github.com/jake/winremotemonitor-agent/internal/core"
)

// Store is the sqlite-backed persistence layer: paired devices, file-event
// history, periodic directory-size snapshots, alerts, and the
// remote-operation history log. Everything the protocol says must be
// queryable/historical lives here, not in memory only.
type Store struct {
	db *sql.DB
}

// OpenStore opens (creating if needed) the sqlite database at path and
// applies the schema.
func OpenStore(path string) (*Store, error) {
	db, err := sql.Open("sqlite", path)
	if err != nil {
		return nil, fmt.Errorf("opening sqlite store: %w", err)
	}
	// sqlite driver: single-writer semantics matter under concurrent WS
	// connections, so cap concurrent writers.
	db.SetMaxOpenConns(1)
	s := &Store{db: db}
	if err := s.migrate(); err != nil {
		db.Close()
		return nil, err
	}
	return s, nil
}

func (s *Store) Close() error { return s.db.Close() }

const schema = `
CREATE TABLE IF NOT EXISTS devices (
	id TEXT PRIMARY KEY,
	name TEXT NOT NULL,
	secret_enc BLOB NOT NULL,
	created_at INTEGER NOT NULL,
	revoked INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS file_events (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	ts INTEGER NOT NULL,
	op TEXT NOT NULL,
	path TEXT NOT NULL,
	old_path TEXT,
	size_bytes INTEGER NOT NULL,
	is_dir INTEGER NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_file_events_ts ON file_events(ts);
CREATE INDEX IF NOT EXISTS idx_file_events_path ON file_events(path);

CREATE TABLE IF NOT EXISTS size_snapshots (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	ts INTEGER NOT NULL,
	path TEXT NOT NULL,
	is_dir INTEGER NOT NULL,
	size_bytes INTEGER NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_snapshots_ts_path ON size_snapshots(ts, path);

CREATE TABLE IF NOT EXISTS alerts (
	id TEXT PRIMARY KEY,
	ts INTEGER NOT NULL,
	kind TEXT NOT NULL,
	severity TEXT NOT NULL,
	message TEXT NOT NULL,
	context TEXT NOT NULL DEFAULT '{}'
);
CREATE INDEX IF NOT EXISTS idx_alerts_ts ON alerts(ts);

CREATE TABLE IF NOT EXISTS history (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	ts INTEGER NOT NULL,
	device_id TEXT NOT NULL,
	device_name TEXT NOT NULL,
	action TEXT NOT NULL,
	success INTEGER NOT NULL,
	detail TEXT NOT NULL,
	reason TEXT
);
CREATE INDEX IF NOT EXISTS idx_history_ts ON history(ts);

CREATE TABLE IF NOT EXISTS disk_free_history (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	ts INTEGER NOT NULL,
	volume TEXT NOT NULL,
	total_bytes INTEGER NOT NULL,
	free_bytes INTEGER NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_disk_free_ts ON disk_free_history(ts, volume);
`

func (s *Store) migrate() error {
	_, err := s.db.Exec(schema)
	if err != nil {
		return fmt.Errorf("applying schema: %w", err)
	}
	return nil
}

// ---- Devices ----

// DeviceRecord is a paired device as persisted at rest.
type DeviceRecord struct {
	ID        string
	Name      string
	SecretEnc []byte // AES-GCM ciphertext, see core.EncryptDeviceSecret
	CreatedAt int64
	Revoked   bool
}

func (s *Store) InsertDevice(d DeviceRecord) error {
	_, err := s.db.Exec(
		`INSERT INTO devices (id, name, secret_enc, created_at, revoked) VALUES (?, ?, ?, ?, ?)`,
		d.ID, d.Name, d.SecretEnc, d.CreatedAt, boolToInt(d.Revoked),
	)
	return err
}

func (s *Store) GetDevice(id string) (DeviceRecord, error) {
	var d DeviceRecord
	var revoked int
	row := s.db.QueryRow(`SELECT id, name, secret_enc, created_at, revoked FROM devices WHERE id = ?`, id)
	if err := row.Scan(&d.ID, &d.Name, &d.SecretEnc, &d.CreatedAt, &revoked); err != nil {
		return DeviceRecord{}, err
	}
	d.Revoked = revoked != 0
	return d, nil
}

func (s *Store) ListDevices() ([]DeviceRecord, error) {
	rows, err := s.db.Query(`SELECT id, name, secret_enc, created_at, revoked FROM devices ORDER BY created_at`)
	if err != nil {
		return nil, err
	}
	defer rows.Close()
	var out []DeviceRecord
	for rows.Next() {
		var d DeviceRecord
		var revoked int
		if err := rows.Scan(&d.ID, &d.Name, &d.SecretEnc, &d.CreatedAt, &revoked); err != nil {
			return nil, err
		}
		d.Revoked = revoked != 0
		out = append(out, d)
	}
	return out, rows.Err()
}

func (s *Store) RevokeDevice(id string) error {
	_, err := s.db.Exec(`UPDATE devices SET revoked = 1 WHERE id = ?`, id)
	return err
}

// ---- File events ----

func (s *Store) InsertFileEvent(e core.FileEventRecord) error {
	_, err := s.db.Exec(
		`INSERT INTO file_events (ts, op, path, old_path, size_bytes, is_dir) VALUES (?, ?, ?, ?, ?, ?)`,
		e.TS, e.Op, e.Path, e.OldPath, e.SizeBytes, boolToInt(e.IsDir),
	)
	return err
}

// FileEventQuery mirrors list_file_events (PROTOCOL.md §4.7).
type FileEventQuery struct {
	FromTS       int64
	ToTS         int64
	Op           *string
	MinSizeBytes int64
	PathPrefix   string
	Limit        int
	Offset       int
}

func (s *Store) QueryFileEvents(q FileEventQuery) (items []core.FileEventRecord, total int, err error) {
	where := `WHERE ts >= ? AND ts <= ? AND size_bytes >= ?`
	args := []interface{}{q.FromTS, q.ToTS, q.MinSizeBytes}
	if q.Op != nil && *q.Op != "" {
		where += ` AND op = ?`
		args = append(args, *q.Op)
	}
	if q.PathPrefix != "" {
		where += ` AND path LIKE ? ESCAPE '\'`
		args = append(args, likePrefix(q.PathPrefix))
	}

	countRow := s.db.QueryRow(`SELECT COUNT(*) FROM file_events `+where, args...)
	if err = countRow.Scan(&total); err != nil {
		return nil, 0, err
	}

	limit := q.Limit
	if limit <= 0 {
		limit = 100
	}
	queryArgs := append(append([]interface{}{}, args...), limit, q.Offset)
	rows, err := s.db.Query(
		`SELECT ts, op, path, old_path, size_bytes, is_dir FROM file_events `+where+` ORDER BY ts DESC LIMIT ? OFFSET ?`,
		queryArgs...,
	)
	if err != nil {
		return nil, 0, err
	}
	defer rows.Close()
	for rows.Next() {
		var e core.FileEventRecord
		var isDir int
		if err = rows.Scan(&e.TS, &e.Op, &e.Path, &e.OldPath, &e.SizeBytes, &isDir); err != nil {
			return nil, 0, err
		}
		e.IsDir = isDir != 0
		items = append(items, e)
	}
	return items, total, rows.Err()
}

// ---- Size snapshots (growth analysis) ----

func (s *Store) InsertSnapshot(ts int64, snaps []core.SizeSnapshot) error {
	tx, err := s.db.Begin()
	if err != nil {
		return err
	}
	stmt, err := tx.Prepare(`INSERT INTO size_snapshots (ts, path, is_dir, size_bytes) VALUES (?, ?, ?, ?)`)
	if err != nil {
		tx.Rollback()
		return err
	}
	defer stmt.Close()
	for _, sn := range snaps {
		if _, err := stmt.Exec(ts, sn.Path, boolToInt(sn.IsDir), sn.SizeBytes); err != nil {
			tx.Rollback()
			return err
		}
	}
	return tx.Commit()
}

// SnapshotAt returns the snapshot rows recorded at the given exact ts.
func (s *Store) SnapshotAt(ts int64) ([]core.SizeSnapshot, error) {
	rows, err := s.db.Query(`SELECT path, is_dir, size_bytes FROM size_snapshots WHERE ts = ?`, ts)
	if err != nil {
		return nil, err
	}
	defer rows.Close()
	var out []core.SizeSnapshot
	for rows.Next() {
		var sn core.SizeSnapshot
		var isDir int
		if err := rows.Scan(&sn.Path, &isDir, &sn.SizeBytes); err != nil {
			return nil, err
		}
		sn.IsDir = isDir != 0
		out = append(out, sn)
	}
	return out, rows.Err()
}

// LatestSnapshotBefore returns the most recent snapshot timestamp at or
// before ts (0 if none), used to find a "before" baseline for a growth
// window.
func (s *Store) LatestSnapshotTSBefore(ts int64) (int64, error) {
	row := s.db.QueryRow(`SELECT ts FROM size_snapshots WHERE ts <= ? ORDER BY ts DESC LIMIT 1`, ts)
	var out int64
	err := row.Scan(&out)
	if err == sql.ErrNoRows {
		return 0, nil
	}
	return out, err
}

// LatestSnapshotTS returns the most recent snapshot timestamp overall.
func (s *Store) LatestSnapshotTS() (int64, error) {
	row := s.db.QueryRow(`SELECT ts FROM size_snapshots ORDER BY ts DESC LIMIT 1`)
	var out int64
	err := row.Scan(&out)
	if err == sql.ErrNoRows {
		return 0, nil
	}
	return out, err
}

// PathSizeHistory returns SizeSamples for a single path since sinceTS, in
// chronological order, used to evaluate fast_growth over a rolling window.
func (s *Store) PathSizeHistory(path string, sinceTS int64) ([]core.SizeSample, error) {
	rows, err := s.db.Query(`SELECT ts, size_bytes FROM size_snapshots WHERE path = ? AND ts >= ? ORDER BY ts ASC`, path, sinceTS)
	if err != nil {
		return nil, err
	}
	defer rows.Close()
	var out []core.SizeSample
	for rows.Next() {
		var smp core.SizeSample
		if err := rows.Scan(&smp.TS, &smp.SizeBytes); err != nil {
			return nil, err
		}
		out = append(out, smp)
	}
	return out, rows.Err()
}

// ---- Disk free-space history (disk_fill_rate) ----

func (s *Store) InsertDiskFreeSample(ts int64, volume string, totalBytes, freeBytes int64) error {
	_, err := s.db.Exec(`INSERT INTO disk_free_history (ts, volume, total_bytes, free_bytes) VALUES (?, ?, ?, ?)`,
		ts, volume, totalBytes, freeBytes)
	return err
}

func (s *Store) DiskFreeHistory(volume string, sinceTS int64) ([]core.FreeSpaceSample, error) {
	rows, err := s.db.Query(`SELECT ts, free_bytes FROM disk_free_history WHERE volume = ? AND ts >= ? ORDER BY ts ASC`, volume, sinceTS)
	if err != nil {
		return nil, err
	}
	defer rows.Close()
	var out []core.FreeSpaceSample
	for rows.Next() {
		var smp core.FreeSpaceSample
		if err := rows.Scan(&smp.TS, &smp.FreeBytes); err != nil {
			return nil, err
		}
		out = append(out, smp)
	}
	return out, rows.Err()
}

// PruneOldSamples deletes snapshot/disk-history/file-event rows older than
// olderThanTS, so a long-running agent doesn't grow its DB unbounded.
func (s *Store) PruneOldSamples(olderThanTS int64) error {
	if _, err := s.db.Exec(`DELETE FROM size_snapshots WHERE ts < ?`, olderThanTS); err != nil {
		return err
	}
	if _, err := s.db.Exec(`DELETE FROM disk_free_history WHERE ts < ?`, olderThanTS); err != nil {
		return err
	}
	return nil
}

// ---- Alerts ----

func (s *Store) InsertAlert(a core.AlertRecord) error {
	ctxJSON, err := json.Marshal(a.Context)
	if err != nil {
		return err
	}
	_, err = s.db.Exec(`INSERT INTO alerts (id, ts, kind, severity, message, context) VALUES (?, ?, ?, ?, ?, ?)`,
		a.ID, a.TS, a.Kind, a.Severity, a.Message, string(ctxJSON))
	return err
}

func (s *Store) ListAlerts(limit int) ([]core.AlertRecord, error) {
	if limit <= 0 {
		limit = 50
	}
	rows, err := s.db.Query(`SELECT id, ts, kind, severity, message, context FROM alerts ORDER BY ts DESC LIMIT ?`, limit)
	if err != nil {
		return nil, err
	}
	defer rows.Close()
	var out []core.AlertRecord
	for rows.Next() {
		var a core.AlertRecord
		var ctxJSON string
		if err := rows.Scan(&a.ID, &a.TS, &a.Kind, &a.Severity, &a.Message, &ctxJSON); err != nil {
			return nil, err
		}
		_ = json.Unmarshal([]byte(ctxJSON), &a.Context)
		out = append(out, a)
	}
	return out, rows.Err()
}

// ---- History (remote-operation log) ----

// HistoryEntry is what gets written for every kill_process / launch_app /
// pair / unpair action, per the non-negotiable requirement that all of
// these are recorded with timestamp, device, success/failure, reason.
type HistoryEntry struct {
	TS         int64
	DeviceID   string
	DeviceName string
	Action     string
	Success    bool
	Detail     string
	Reason     string // empty if none
}

func (s *Store) InsertHistory(h HistoryEntry) error {
	var reason interface{}
	if h.Reason != "" {
		reason = h.Reason
	}
	_, err := s.db.Exec(`INSERT INTO history (ts, device_id, device_name, action, success, detail, reason) VALUES (?, ?, ?, ?, ?, ?, ?)`,
		h.TS, h.DeviceID, h.DeviceName, h.Action, boolToInt(h.Success), h.Detail, reason)
	return err
}

func (s *Store) QueryHistory(fromTS, toTS int64, limit, offset int) (items []core.HistoryItem, total int, err error) {
	if limit <= 0 {
		limit = 50
	}
	countRow := s.db.QueryRow(`SELECT COUNT(*) FROM history WHERE ts >= ? AND ts <= ?`, fromTS, toTS)
	if err = countRow.Scan(&total); err != nil {
		return nil, 0, err
	}
	rows, err := s.db.Query(
		`SELECT ts, device_id, device_name, action, success, detail, reason FROM history WHERE ts >= ? AND ts <= ? ORDER BY ts DESC LIMIT ? OFFSET ?`,
		fromTS, toTS, limit, offset,
	)
	if err != nil {
		return nil, 0, err
	}
	defer rows.Close()
	for rows.Next() {
		var h core.HistoryItem
		var success int
		var reason sql.NullString
		if err = rows.Scan(&h.TS, &h.DeviceID, &h.DeviceName, &h.Action, &success, &h.Detail, &reason); err != nil {
			return nil, 0, err
		}
		h.Success = success != 0
		if reason.Valid {
			r := reason.String
			h.Reason = &r
		}
		items = append(items, h)
	}
	return items, total, rows.Err()
}

// ---- helpers ----

func boolToInt(b bool) int {
	if b {
		return 1
	}
	return 0
}

func likePrefix(prefix string) string {
	escaped := ""
	for _, c := range prefix {
		switch c {
		case '%', '_', '\\':
			escaped += `\` + string(c)
		default:
			escaped += string(c)
		}
	}
	return escaped + "%"
}
