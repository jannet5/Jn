"""SQLite durum deposu: ne kontrol edildi, ne zaman tekrar bakılacak, ne bulundu."""
import sqlite3
import threading
import time
from typing import Iterable, List, Optional

SCHEMA = """
CREATE TABLE IF NOT EXISTS names (
  name TEXT PRIMARY KEY,
  length INTEGER NOT NULL,
  src TEXT,
  score REAL DEFAULT 0,
  hot INTEGER DEFAULT 0,
  status TEXT DEFAULT 'pending',   -- pending | taken | found
  checks INTEGER DEFAULT 0,
  last_checked REAL,
  next_check REAL DEFAULT 0,
  note TEXT
);
CREATE INDEX IF NOT EXISTS idx_pending ON names(status, score DESC);
CREATE INDEX IF NOT EXISTS idx_due ON names(status, next_check);
CREATE INDEX IF NOT EXISTS idx_hot ON names(hot, next_check);
CREATE TABLE IF NOT EXISTS meta (key TEXT PRIMARY KEY, val INTEGER DEFAULT 0);
CREATE TABLE IF NOT EXISTS events (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  ts REAL, name TEXT, kind TEXT, detail TEXT
);
"""


class Store:
    def __init__(self, path: str = "avci.db"):
        self.db = sqlite3.connect(path, check_same_thread=False, timeout=30)
        self.db.row_factory = sqlite3.Row
        self.lock = threading.Lock()
        with self.lock:
            self.db.execute("PRAGMA journal_mode=WAL")
            self.db.executescript(SCHEMA)
            self.db.commit()

    # --- ekleme -----------------------------------------------------------
    def add_many(self, cands: Iterable[tuple], hot: bool = False) -> int:
        """(isim, kaynak, puan) listesini ekler. Var olanı bozmaz (hot ise yükseltir)."""
        rows = [(n, len(n), src, score, 1 if hot else 0) for n, src, score in cands]
        with self.lock:
            before = self.db.execute("SELECT COUNT(*) FROM names").fetchone()[0]
            self.db.executemany(
                "INSERT OR IGNORE INTO names(name,length,src,score,hot) VALUES (?,?,?,?,?)", rows)
            if hot:
                self.db.executemany("UPDATE names SET hot=1 WHERE name=?", [(r[0],) for r in rows])
            self.db.commit()
            return self.db.execute("SELECT COUNT(*) FROM names").fetchone()[0] - before

    # --- iş dağıtımı --------------------------------------------------------
    def lease(self, lease_seconds: float = 120.0, now: Optional[float] = None) -> Optional[str]:
        """Sıradaki ismi verir: sıcak liste > bekleyenler (yüksek puan) > dolu isimlerin tekrar zamanı.
        Büyük havuzda yavaşlamasın diye her adım kendi indeksini kullanır. Çifte iş olmasın diye kiralar."""
        now = time.time() if now is None else now
        with self.lock:
            row = (self.db.execute(
                "SELECT name FROM names WHERE hot=1 AND status!='found' AND next_check<=? "
                "ORDER BY next_check LIMIT 1", (now,)).fetchone()
                or self.db.execute(
                "SELECT name FROM names WHERE status='pending' AND next_check<=? "
                "ORDER BY score DESC LIMIT 1", (now,)).fetchone()
                or self.db.execute(
                "SELECT name FROM names WHERE status='taken' AND next_check<=? "
                "ORDER BY next_check LIMIT 1", (now,)).fetchone())
            if not row:
                return None
            self.db.execute("UPDATE names SET next_check=? WHERE name=?", (now + lease_seconds, row[0]))
            self.db.commit()
            return row[0]

    def record(self, name: str, status: str, next_check: float, note: str = None,
               count_check: bool = True):
        with self.lock:
            self.db.execute(
                "UPDATE names SET status=?, next_check=?, last_checked=?, note=?, "
                "checks=checks+? WHERE name=?",
                (status, next_check, time.time(), note, 1 if count_check else 0, name))
            if count_check:
                self.db.execute("INSERT INTO meta(key,val) VALUES('checks',1) "
                                "ON CONFLICT(key) DO UPDATE SET val=val+1")
            self.db.commit()

    def release_lease(self, name: str, next_check: float):
        with self.lock:
            self.db.execute("UPDATE names SET next_check=? WHERE name=?", (next_check, name))
            self.db.commit()

    def set_hot(self, name: str, hot: bool = True):
        with self.lock:
            self.db.execute("UPDATE names SET hot=? WHERE name=?", (1 if hot else 0, name))
            self.db.commit()

    def event(self, name: str, kind: str, detail: str = ""):
        with self.lock:
            self.db.execute("INSERT INTO events(ts,name,kind,detail) VALUES (?,?,?,?)",
                            (time.time(), name, kind, detail))
            self.db.commit()

    # --- raporlama ----------------------------------------------------------
    def stats(self) -> dict:
        with self.lock:
            by = dict(self.db.execute("SELECT status, COUNT(*) FROM names GROUP BY status").fetchall())
            hot = self.db.execute("SELECT COUNT(*) FROM names WHERE hot=1 AND status!='found'").fetchone()[0]
            chk = self.db.execute("SELECT val FROM meta WHERE key='checks'").fetchone()
            return {
                "toplam": sum(by.values()),
                "bekleyen": by.get("pending", 0),
                "alinmis": by.get("taken", 0),
                "bulunan": by.get("found", 0),
                "sicak_liste": hot,
                "toplam_kontrol": chk[0] if chk else 0,
            }

    def found(self) -> List[sqlite3.Row]:
        with self.lock:
            return self.db.execute(
                "SELECT name, length, src, last_checked FROM names WHERE status='found' "
                "ORDER BY length, name").fetchall()

    def by_length(self) -> List[sqlite3.Row]:
        with self.lock:
            return self.db.execute(
                "SELECT length, COUNT(*) n, SUM(status='pending') p, SUM(status='taken') t, "
                "SUM(status='found') f FROM names GROUP BY length ORDER BY length").fetchall()

    def top_pending(self, limit: int = 10) -> List[str]:
        with self.lock:
            return [r[0] for r in self.db.execute(
                "SELECT name FROM names WHERE status='pending' ORDER BY hot DESC, score DESC LIMIT ?",
                (limit,))]
