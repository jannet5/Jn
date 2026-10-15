import { DatabaseSync } from 'node:sqlite';

const SCHEMA = `
CREATE TABLE IF NOT EXISTS apps (
  pkg     TEXT PRIMARY KEY,
  name    TEXT NOT NULL,
  blocked INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS channels (
  pkg           TEXT NOT NULL REFERENCES apps(pkg) ON DELETE CASCADE,
  id            TEXT NOT NULL,
  name          TEXT NOT NULL,
  description   TEXT NOT NULL DEFAULT '',
  importance    INTEGER NOT NULL,
  user_modified INTEGER NOT NULL DEFAULT 0,
  PRIMARY KEY (pkg, id)
);

CREATE TABLE IF NOT EXISTS notifications (
  id               INTEGER PRIMARY KEY AUTOINCREMENT,
  key              TEXT NOT NULL,
  pkg              TEXT NOT NULL,
  nid              INTEGER NOT NULL,
  tag              TEXT NOT NULL DEFAULT '',
  channel          TEXT NOT NULL,
  title            TEXT NOT NULL DEFAULT '',
  text             TEXT NOT NULL DEFAULT '',
  big_text         TEXT NOT NULL DEFAULT '',
  sub_text         TEXT NOT NULL DEFAULT '',
  category         TEXT NOT NULL DEFAULT '',
  grp              TEXT NOT NULL DEFAULT '',
  is_summary       INTEGER NOT NULL DEFAULT 0,
  ongoing          INTEGER NOT NULL DEFAULT 0,
  auto_cancel      INTEGER NOT NULL DEFAULT 1,
  only_alert_once  INTEGER NOT NULL DEFAULT 0,
  conversation     INTEGER NOT NULL DEFAULT 0,
  actions          TEXT NOT NULL DEFAULT '[]',
  content_intent   TEXT NOT NULL DEFAULT '',
  when_ms          INTEGER NOT NULL,
  posted_at        INTEGER NOT NULL,
  updated_at       INTEGER NOT NULL,
  expires_at       INTEGER,
  state            TEXT NOT NULL CHECK (state IN ('active','snoozed','removed')),
  snooze_until     INTEGER,
  removed_reason   TEXT,
  removed_at       INTEGER,
  deleted_by_sender INTEGER NOT NULL DEFAULT 0,
  search_norm      TEXT NOT NULL DEFAULT ''
);
CREATE UNIQUE INDEX IF NOT EXISTS ux_notifications_live_key ON notifications(key) WHERE state IN ('active','snoozed');
CREATE INDEX IF NOT EXISTS ix_notifications_state ON notifications(state);
CREATE INDEX IF NOT EXISTS ix_notifications_updated ON notifications(updated_at);
CREATE INDEX IF NOT EXISTS ix_notifications_pkg_title ON notifications(pkg, title);

CREATE TABLE IF NOT EXISTS rules (
  id      INTEGER PRIMARY KEY AUTOINCREMENT,
  pkg     TEXT NOT NULL DEFAULT '',
  keyword TEXT NOT NULL DEFAULT '',
  action  TEXT NOT NULL CHECK (action IN ('BLOCK','MUTE')),
  enabled INTEGER NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS settings (
  key   TEXT PRIMARY KEY,
  value TEXT NOT NULL
);
`;

/** SQLite dosyasını (ya da ':memory:') açar ve şemayı kurar. */
export function openDb(path = ':memory:') {
  const db = new DatabaseSync(path);
  db.exec('PRAGMA foreign_keys = ON;');
  if (path !== ':memory:') db.exec('PRAGMA journal_mode = WAL;');
  db.exec(SCHEMA);
  return db;
}
