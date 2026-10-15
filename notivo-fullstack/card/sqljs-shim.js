// node:sqlite DatabaseSync ile uyumlu, sql.js (tarayıcıda çalışan SQLite) üzerinde bir sarmalayıcı.
// Shade (server/shade.js) yalnızca prepare/run/get/all/exec kullanır; bu sayede AYNI çekirdek kod kartta da çalışır.
import { SCHEMA } from '../server/schema.js';

const clean = (params) => params.map((v) => (v === undefined ? null : typeof v === 'boolean' ? (v ? 1 : 0) : v));

class Statement {
  constructor(owner, sql) {
    this.owner = owner;
    this.sql = sql;
  }
  #open(params) {
    const st = this.owner.raw.prepare(this.sql);
    if (params.length) st.bind(clean(params));
    return st;
  }
  run(...params) {
    const st = this.#open(params);
    try {
      st.step();
    } finally {
      st.free();
    }
    const changes = this.owner.raw.getRowsModified();
    const lastInsertRowid = this.owner.raw.exec('SELECT last_insert_rowid()')[0].values[0][0];
    this.owner.onWrite?.();
    return { changes, lastInsertRowid };
  }
  get(...params) {
    const st = this.#open(params);
    try {
      return st.step() ? st.getAsObject() : undefined;
    } finally {
      st.free();
    }
  }
  all(...params) {
    const st = this.#open(params);
    try {
      const rows = [];
      while (st.step()) rows.push(st.getAsObject());
      return rows;
    } finally {
      st.free();
    }
  }
}

export class SqlJsDatabase {
  /** @param {*} SQL initSqlJs() sonucu  @param {Uint8Array=} data daha önce dışa aktarılmış veritabanı */
  constructor(SQL, data) {
    this.raw = data ? new SQL.Database(data) : new SQL.Database();
    this.onWrite = null;
  }
  exec(sql) {
    this.raw.run(sql);
    if (!/^\s*(BEGIN|COMMIT|ROLLBACK|PRAGMA)/i.test(sql)) this.onWrite?.();
  }
  prepare(sql) {
    return new Statement(this, sql);
  }
  /** sql.js export() bağlantıyı yeniden açar; PRAGMA'lar sıfırlanır, tekrar kurulur. */
  export() {
    const bytes = this.raw.export();
    this.raw.run('PRAGMA foreign_keys = ON;');
    return bytes;
  }
  close() {
    this.raw.close();
  }
}

export function openSqlJsDb(SQL, data) {
  const db = new SqlJsDatabase(SQL, data);
  db.raw.run('PRAGMA foreign_keys = ON;');
  db.raw.run(SCHEMA);
  return db;
}
