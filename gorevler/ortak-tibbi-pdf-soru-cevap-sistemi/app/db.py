import sqlite3
import threading
from pathlib import Path

SCHEMA = """
CREATE TABLE IF NOT EXISTS devices (
  id TEXT PRIMARY KEY,
  created_at TEXT NOT NULL,
  ip_hash TEXT NOT NULL,
  bonus_credits INTEGER NOT NULL DEFAULT 0
);
CREATE TABLE IF NOT EXISTS documents (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  sha256 TEXT UNIQUE NOT NULL,
  title TEXT NOT NULL,
  language TEXT NOT NULL,
  subject TEXT NOT NULL DEFAULT '',
  pages INTEGER NOT NULL,
  chunks INTEGER NOT NULL,
  uploader TEXT NOT NULL,
  created_at TEXT NOT NULL,
  reports INTEGER NOT NULL DEFAULT 0,
  hidden INTEGER NOT NULL DEFAULT 0
);
CREATE TABLE IF NOT EXISTS chunks (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  doc_id INTEGER NOT NULL REFERENCES documents(id) ON DELETE CASCADE,
  page INTEGER NOT NULL,
  text TEXT NOT NULL,
  vec BLOB NOT NULL
);
CREATE INDEX IF NOT EXISTS chunks_doc ON chunks(doc_id);
CREATE TABLE IF NOT EXISTS usage (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  device_id TEXT NOT NULL,
  period TEXT NOT NULL,
  kind TEXT NOT NULL,
  cost INTEGER NOT NULL,
  thread_id TEXT,
  grace INTEGER NOT NULL DEFAULT 0,
  created_at TEXT NOT NULL
);
CREATE INDEX IF NOT EXISTS usage_dev ON usage(device_id, period);
CREATE TABLE IF NOT EXISTS threads (
  id TEXT PRIMARY KEY,
  device_id TEXT NOT NULL,
  started_with_credit INTEGER NOT NULL,
  created_at TEXT NOT NULL
);
CREATE TABLE IF NOT EXISTS reports (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  doc_id INTEGER NOT NULL,
  device_id TEXT NOT NULL,
  reason TEXT NOT NULL,
  note TEXT NOT NULL DEFAULT '',
  created_at TEXT NOT NULL,
  UNIQUE(doc_id, device_id)
);
CREATE TABLE IF NOT EXISTS codes (
  code TEXT PRIMARY KEY,
  credits INTEGER NOT NULL,
  note TEXT NOT NULL DEFAULT '',
  redeemed_by TEXT,
  redeemed_at TEXT
);
"""


class DB:
    def __init__(self, path: Path):
        path.parent.mkdir(parents=True, exist_ok=True)
        self.conn = sqlite3.connect(path, check_same_thread=False)
        self.conn.row_factory = sqlite3.Row
        self.conn.execute("PRAGMA journal_mode=WAL")
        self.conn.execute("PRAGMA foreign_keys=ON")
        self.conn.executescript(SCHEMA)
        self.lock = threading.RLock()

    def q(self, sql: str, args=()):
        with self.lock:
            return self.conn.execute(sql, args).fetchall()

    def one(self, sql: str, args=()):
        with self.lock:
            return self.conn.execute(sql, args).fetchone()

    def run(self, sql: str, args=()):
        with self.lock:
            cur = self.conn.execute(sql, args)
            self.conn.commit()
            return cur
