import { DatabaseSync } from 'node:sqlite';
import { SCHEMA } from './schema.js';


/** SQLite dosyasını (ya da ':memory:') açar ve şemayı kurar. */
export function openDb(path = ':memory:') {
  const db = new DatabaseSync(path);
  db.exec('PRAGMA foreign_keys = ON;');
  if (path !== ':memory:') db.exec('PRAGMA journal_mode = WAL;');
  db.exec(SCHEMA);
  return db;
}
