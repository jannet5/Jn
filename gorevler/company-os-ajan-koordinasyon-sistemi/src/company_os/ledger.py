"""SQLite tabanlı görev defteri: lease + fencing token + kanıt bağlama.

Denetim bulgularına karşılık gelen kurallar:

1. ``running`` durumuna tek giriş ``claim()``'dir; ``transition()`` hiçbir
   zaman ``running`` ya da ``succeeded`` hedefine izin vermez.
2. Her worker mutasyonu (``heartbeat``, ``put_artifact``, ``run_evidence``,
   ``post_message``, ``complete``, ``fail``) bir ``Lease`` (görev, fencing token)
   ister ve bunu ``BEGIN IMMEDIATE`` işlemi içinde koşullu olarak doğrular.
   Token global ve monoton artar; süresi dolmuş/yer değiştirmiş worker yazamaz.
3. ``reconcile()`` tek ``BEGIN IMMEDIATE`` işleminde, satır başına
   ``lease_token`` ve ``lease_expires <= now`` koşuluyla güncelleme yapar;
   o arada yeniden alınmış (yeni token'lı) canlı lease'e dokunamaz.
4. Kabul kapısı (``qa.acceptance_gate``) yalnız mevcut deneme+token'a ait
   artifact ve kanıtları sayar; kanıt referansları artifact hash'lerine bağlıdır.
5. Artifact dosyaları ``artifacts.SafeRoot`` ile deneme başına ayrı dizine yazılır.
"""
from __future__ import annotations

import json
import os
import sqlite3
import subprocess
import threading
import time
import uuid
from contextlib import contextmanager
from dataclasses import dataclass
from typing import Any, Callable, Iterable

from .artifacts import SafeRoot, sha256_bytes

STATES = ("pending", "ready", "running", "succeeded", "failed", "blocked", "cancelled")
TERMINAL = ("succeeded", "failed", "cancelled")

# transition() ile izin verilen yönetici geçişleri. running/succeeded YOK.
ADMIN_TRANSITIONS = {
    ("pending", "ready"),
    ("pending", "cancelled"),
    ("ready", "cancelled"),
    ("blocked", "ready"),
    ("blocked", "cancelled"),
    ("failed", "ready"),
}

SCHEMA = """
CREATE TABLE IF NOT EXISTS meta(k TEXT PRIMARY KEY, v INTEGER NOT NULL);
INSERT OR IGNORE INTO meta(k, v) VALUES ('fence', 0);
CREATE TABLE IF NOT EXISTS tasks(
  id TEXT PRIMARY KEY,
  title TEXT NOT NULL,
  role TEXT NOT NULL,
  spec TEXT NOT NULL DEFAULT '{}',
  depends_on TEXT NOT NULL DEFAULT '[]',
  state TEXT NOT NULL CHECK(state IN ('pending','ready','running','succeeded','failed','blocked','cancelled')),
  attempt INTEGER NOT NULL DEFAULT 0,
  max_attempts INTEGER NOT NULL DEFAULT 3,
  lease_owner TEXT,
  lease_token INTEGER,
  lease_expires REAL,
  result TEXT,
  error TEXT,
  created REAL NOT NULL,
  updated REAL NOT NULL
);
CREATE TABLE IF NOT EXISTS artifacts(
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  task_id TEXT NOT NULL REFERENCES tasks(id),
  attempt INTEGER NOT NULL,
  token INTEGER NOT NULL,
  relpath TEXT NOT NULL,
  sha256 TEXT NOT NULL,
  size INTEGER NOT NULL,
  created REAL NOT NULL,
  UNIQUE(task_id, attempt, relpath)
);
CREATE TABLE IF NOT EXISTS evidence(
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  task_id TEXT NOT NULL REFERENCES tasks(id),
  attempt INTEGER NOT NULL,
  token INTEGER NOT NULL,
  argv TEXT NOT NULL,
  exit_code INTEGER NOT NULL,
  output TEXT NOT NULL,
  artifact_refs TEXT NOT NULL,
  created REAL NOT NULL
);
CREATE TABLE IF NOT EXISTS messages(
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  sender TEXT NOT NULL,
  recipient TEXT NOT NULL,
  task_id TEXT,
  body TEXT NOT NULL,
  created REAL NOT NULL
);
CREATE TABLE IF NOT EXISTS events(
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  task_id TEXT,
  kind TEXT NOT NULL,
  detail TEXT NOT NULL,
  created REAL NOT NULL
);
"""


class LedgerError(RuntimeError):
    pass


class LeaseLost(LedgerError):
    """Fencing token geçersiz: lease süresi dolmuş, geri alınmış ya da yenisi verilmiş."""


class InvalidTransition(LedgerError):
    pass


class GateFailed(LedgerError):
    def __init__(self, reasons: list[str]):
        super().__init__("; ".join(reasons))
        self.reasons = reasons


@dataclass(frozen=True)
class Lease:
    task_id: str
    token: int
    attempt: int
    owner: str
    expires: float

    def as_dict(self) -> dict:
        return {
            "task_id": self.task_id,
            "token": self.token,
            "attempt": self.attempt,
            "owner": self.owner,
            "expires": self.expires,
        }


class Ledger:
    """Her iş parçacığı/işlem kendi ``Ledger`` örneğini (kendi bağlantısını) açmalıdır."""

    def __init__(self, db_path: str, workspace: str | None = None,
                 clock: Callable[[], float] = time.time):
        self.db_path = db_path
        self.workspace = workspace or os.path.join(os.path.dirname(os.path.abspath(db_path)), "workspace")
        self.store = SafeRoot(self.workspace)
        self.clock = clock
        self._lock = threading.RLock()
        self.conn = sqlite3.connect(db_path, timeout=30, isolation_level=None, check_same_thread=False)
        self.conn.row_factory = sqlite3.Row
        self.conn.execute("PRAGMA busy_timeout=30000")
        self.conn.execute("PRAGMA foreign_keys=ON")
        try:
            self.conn.execute("PRAGMA journal_mode=WAL")
        except sqlite3.OperationalError:
            pass
        with self.tx():
            for stmt in SCHEMA.strip().split(";"):
                if stmt.strip():
                    self.conn.execute(stmt)

    def close(self) -> None:
        self.conn.close()

    # ------------------------------------------------------------------ altyapı
    @contextmanager
    def tx(self):
        """``BEGIN IMMEDIATE``: yazma kilidi işlem başında alınır, ayrı bağlantılar arası serileştirir."""
        with self._lock:
            self.conn.execute("BEGIN IMMEDIATE")
            try:
                yield self.conn
            except BaseException as e:
                self.conn.execute("ROLLBACK")
                rej = getattr(e, "rejected", None)
                if rej:  # reddedilen yazma denetim izine ayrı işlemde kaydedilir
                    self.conn.execute("BEGIN IMMEDIATE")
                    self._event(self.conn, rej["task_id"], "stale_write_rejected", **rej["detail"])
                    self.conn.execute("COMMIT")
                raise
            else:
                self.conn.execute("COMMIT")

    def _event(self, c, task_id, kind, **detail):
        c.execute("INSERT INTO events(task_id, kind, detail, created) VALUES (?,?,?,?)",
                  (task_id, kind, json.dumps(detail, ensure_ascii=False), self.clock()))

    def _next_fence(self, c) -> int:
        c.execute("UPDATE meta SET v = v + 1 WHERE k='fence'")
        return c.execute("SELECT v FROM meta WHERE k='fence'").fetchone()[0]

    def _check_lease(self, c, lease: Lease) -> sqlite3.Row:
        row = c.execute("SELECT * FROM tasks WHERE id=?", (lease.task_id,)).fetchone()
        if row is None:
            raise LedgerError(f"görev yok: {lease.task_id}")
        now = self.clock()
        if (row["state"] != "running" or row["lease_token"] != lease.token
                or row["lease_expires"] is None or row["lease_expires"] <= now):
            err = LeaseLost(
                f"{lease.task_id}: token {lease.token} geçersiz (durum={row['state']}, "
                f"güncel token={row['lease_token']})")
            err.rejected = {"task_id": lease.task_id, "detail": {
                "token": lease.token, "current_token": row["lease_token"], "state": row["state"]}}
            raise err
        return row

    def attempt_dir(self, task_id: str, attempt: int) -> str:
        return f"tasks/{task_id}/attempt-{attempt}"

    # ------------------------------------------------------------------ plan
    def add_task(self, task_id: str, title: str, role: str, *, spec: dict | None = None,
                 depends_on: Iterable[str] = (), max_attempts: int = 3) -> None:
        deps = list(depends_on)
        now = self.clock()
        with self.tx() as c:
            for d in deps:
                if c.execute("SELECT 1 FROM tasks WHERE id=?", (d,)).fetchone() is None:
                    raise LedgerError(f"{task_id}: bilinmeyen bağımlılık {d}")
            state = "pending" if deps else "ready"
            c.execute(
                "INSERT INTO tasks(id,title,role,spec,depends_on,state,max_attempts,created,updated)"
                " VALUES (?,?,?,?,?,?,?,?,?)",
                (task_id, title, role, json.dumps(spec or {}, ensure_ascii=False), json.dumps(deps),
                 state, max_attempts, now, now))
            self._event(c, task_id, "created", role=role, depends_on=deps)

    def load_plan(self, plan: dict) -> list[str]:
        """Plan: {"goal": str, "tasks": [{id,title,role,depends_on,max_attempts,...spec}]}.
        Görevler bağımlılık sırasına göre eklenir; döngü varsa hata."""
        tasks = {t["id"]: t for t in plan.get("tasks", [])}
        if len(tasks) != len(plan.get("tasks", [])):
            raise LedgerError("planda yinelenen görev kimliği")
        order, seen, temp = [], set(), set()

        def visit(tid):
            if tid in seen:
                return
            if tid in temp:
                raise LedgerError(f"bağımlılık döngüsü: {tid}")
            if tid not in tasks:
                raise LedgerError(f"bilinmeyen bağımlılık: {tid}")
            temp.add(tid)
            for d in tasks[tid].get("depends_on", []):
                visit(d)
            temp.discard(tid)
            seen.add(tid)
            order.append(tid)

        for tid in tasks:
            visit(tid)
        for tid in order:
            t = tasks[tid]
            spec = {k: v for k, v in t.items()
                    if k not in ("id", "title", "role", "depends_on", "max_attempts")}
            self.add_task(tid, t.get("title", tid), t.get("role", "implementer"), spec=spec,
                          depends_on=t.get("depends_on", []), max_attempts=t.get("max_attempts", 3))
        return order

    def promote(self) -> list[str]:
        """Tüm bağımlılıkları ``succeeded`` olan ``pending`` görevleri ``ready`` yapar;
        bağımlılığı kalıcı başarısız olanları ``blocked`` yapar."""
        changed = []
        with self.tx() as c:
            rows = c.execute("SELECT id, depends_on FROM tasks WHERE state='pending'").fetchall()
            for r in rows:
                deps = json.loads(r["depends_on"])
                states = [c.execute("SELECT state FROM tasks WHERE id=?", (d,)).fetchone()[0] for d in deps]
                if all(s == "succeeded" for s in states):
                    new = "ready"
                elif any(s in ("failed", "cancelled", "blocked") for s in states):
                    new = "blocked"
                else:
                    continue
                c.execute("UPDATE tasks SET state=?, updated=? WHERE id=? AND state='pending'",
                          (new, self.clock(), r["id"]))
                self._event(c, r["id"], "promoted", to=new)
                changed.append(r["id"])
        return changed

    def transition(self, task_id: str, to_state: str, *, reason: str = "") -> None:
        """Yönetici geçişleri. ``running`` yalnız ``claim()`` ile, ``succeeded`` yalnız
        kabul kapısından geçen ``complete()`` ile girilebilir (bulgu 1)."""
        if to_state not in STATES:
            raise InvalidTransition(f"bilinmeyen durum {to_state}")
        with self.tx() as c:
            row = c.execute("SELECT state FROM tasks WHERE id=?", (task_id,)).fetchone()
            if row is None:
                raise LedgerError(f"görev yok: {task_id}")
            if (row["state"], to_state) not in ADMIN_TRANSITIONS:
                raise InvalidTransition(
                    f"{task_id}: {row['state']} → {to_state} transition() ile yapılamaz"
                    + (" (running yalnız claim() ile)" if to_state == "running" else ""))
            extra = ", attempt=0" if row["state"] in ("blocked", "failed") else ""
            c.execute(f"UPDATE tasks SET state=?, error=NULL, updated=?{extra} WHERE id=? AND state=?",
                      (to_state, self.clock(), task_id, row["state"]))
            self._event(c, task_id, "transition", frm=row["state"], to=to_state, reason=reason)

    # ------------------------------------------------------------------ lease
    def claim(self, owner: str, *, task_id: str | None = None, role: str | None = None,
              ttl: float = 60.0) -> Lease | None:
        """``ready`` bir görevi atomik olarak alır. Tek ``running`` girişi budur."""
        now = self.clock()
        with self.tx() as c:
            q = "SELECT id, attempt, max_attempts FROM tasks WHERE state='ready'"
            args: list[Any] = []
            if task_id:
                q += " AND id=?"
                args.append(task_id)
            if role:
                q += " AND role=?"
                args.append(role)
            q += " ORDER BY created, id LIMIT 1"
            row = c.execute(q, args).fetchone()
            if row is None:
                return None
            if row["attempt"] >= row["max_attempts"]:
                c.execute("UPDATE tasks SET state='blocked', updated=? WHERE id=?", (now, row["id"]))
                self._event(c, row["id"], "blocked", reason="max_attempts")
                return None
            token = self._next_fence(c)
            attempt = row["attempt"] + 1
            expires = now + ttl
            cur = c.execute(
                "UPDATE tasks SET state='running', attempt=?, lease_owner=?, lease_token=?, "
                "lease_expires=?, error=NULL, updated=? WHERE id=? AND state='ready'",
                (attempt, owner, token, expires, now, row["id"]))
            if cur.rowcount != 1:
                return None
            self._event(c, row["id"], "claimed", owner=owner, token=token, attempt=attempt)
            return Lease(row["id"], token, attempt, owner, expires)

    def heartbeat(self, lease: Lease, ttl: float = 60.0) -> Lease:
        with self.tx() as c:
            self._check_lease(c, lease)
            expires = self.clock() + ttl
            c.execute("UPDATE tasks SET lease_expires=?, updated=? WHERE id=? AND lease_token=?",
                      (expires, self.clock(), lease.task_id, lease.token))
        return Lease(lease.task_id, lease.token, lease.attempt, lease.owner, expires)

    def reconcile(self) -> dict[str, list[str]]:
        """Süresi dolmuş lease'leri geri alır. Tek işlem + satır başına koşullu
        güncelleme: okunan token ve 'süresi dolmuş' koşulu UPDATE'te tekrar
        doğrulanır; canlı yeni lease etkilenmez (bulgu 3)."""
        out: dict[str, list[str]] = {"retried": [], "blocked": []}
        with self.tx() as c:
            now = self.clock()
            rows = c.execute(
                "SELECT id, lease_token, attempt, max_attempts FROM tasks "
                "WHERE state='running' AND lease_expires <= ?", (now,)).fetchall()
            for r in rows:
                new = "blocked" if r["attempt"] >= r["max_attempts"] else "ready"
                cur = c.execute(
                    "UPDATE tasks SET state=?, lease_owner=NULL, lease_token=NULL, lease_expires=NULL,"
                    " error='lease süresi doldu', updated=? "
                    "WHERE id=? AND state='running' AND lease_token=? AND lease_expires <= ?",
                    (new, now, r["id"], r["lease_token"], now))
                if cur.rowcount == 1:
                    out["retried" if new == "ready" else "blocked"].append(r["id"])
                    self._event(c, r["id"], "lease_expired", token=r["lease_token"], to=new)
        return out

    # ------------------------------------------------------------------ worker çıktıları
    def put_artifact(self, lease: Lease, relpath: str, data: bytes | str) -> dict:
        """Dosyayı deneme dizinine güvenli yazar ve hash'ini token kontrolüyle kaydeder."""
        if isinstance(data, str):
            data = data.encode("utf-8")
        with self.tx() as c:  # yazmadan önce lease geçerli olmalı
            self._check_lease(c, lease)
        full = f"{self.attempt_dir(lease.task_id, lease.attempt)}/{relpath}"
        digest = self.store.write_bytes(full, data)
        return self._record_artifact(lease, relpath, digest, len(data))

    def register_artifact(self, lease: Lease, relpath: str) -> dict:
        """Worker komutunun deneme dizinine kendisi ürettiği dosyayı güvenli okuyup kaydeder."""
        full = f"{self.attempt_dir(lease.task_id, lease.attempt)}/{relpath}"
        data = self.store.read_bytes(full)
        return self._record_artifact(lease, relpath, sha256_bytes(data), len(data))

    def _record_artifact(self, lease, relpath, digest, size) -> dict:
        with self.tx() as c:
            self._check_lease(c, lease)
            c.execute(
                "INSERT INTO artifacts(task_id,attempt,token,relpath,sha256,size,created) "
                "VALUES (?,?,?,?,?,?,?) ON CONFLICT(task_id,attempt,relpath) DO UPDATE SET "
                "sha256=excluded.sha256, size=excluded.size, token=excluded.token, created=excluded.created",
                (lease.task_id, lease.attempt, lease.token, relpath, digest, size, self.clock()))
            self._event(c, lease.task_id, "artifact", path=relpath, sha256=digest, token=lease.token)
        return {"path": relpath, "sha256": digest, "size": size}

    def run_evidence(self, lease: Lease, argv: list[str], refs: list[str], *,
                     timeout: float = 600, env: dict | None = None) -> dict:
        """Komutu deneme dizininde *defterin kendisi* çalıştırır; çıkış kodu ve çıktı,
        komut öncesi ve sonrası aynı kalan artifact hash'lerine bağlanarak kaydedilir."""
        if not isinstance(argv, list) or not argv or not all(isinstance(a, str) for a in argv):
            raise LedgerError("argv boş olmayan string listesi olmalı")
        if not refs:
            raise LedgerError("kanıt en az bir artifact'a referans vermeli")
        with self.tx() as c:
            self._check_lease(c, lease)
            before = self._current_refs(c, lease, refs)
        cwd = self.store.abspath(self.attempt_dir(lease.task_id, lease.attempt))
        os.makedirs(cwd, exist_ok=True)
        run_env = dict(os.environ)
        run_env.update(env or {})
        try:
            p = subprocess.run(argv, cwd=cwd, capture_output=True, text=True,
                               timeout=timeout, env=run_env)
            code, output = p.returncode, (p.stdout + p.stderr)
        except subprocess.TimeoutExpired as e:
            code, output = 124, f"zaman aşımı: {e}"
        except OSError as e:
            code, output = 127, f"çalıştırılamadı: {e}"
        output = output[-20000:]
        with self.tx() as c:
            self._check_lease(c, lease)
            after = self._current_refs(c, lease, refs)
            for r in after:  # dosyanın diskteki hali kayıtla aynı mı
                disk = self.store.sha256(f"{self.attempt_dir(lease.task_id, lease.attempt)}/{r['path']}")
                if disk != r["sha256"]:
                    raise LedgerError(f"{r['path']}: diskteki içerik kayıtlı hash ile uyuşmuyor")
            if before != after:
                raise LedgerError("kanıt çalışırken artifact'lar değişti; kanıt kaydedilmedi")
            c.execute(
                "INSERT INTO evidence(task_id,attempt,token,argv,exit_code,output,artifact_refs,created)"
                " VALUES (?,?,?,?,?,?,?,?)",
                (lease.task_id, lease.attempt, lease.token, json.dumps(argv), code, output,
                 json.dumps(after), self.clock()))
            eid = c.execute("SELECT last_insert_rowid()").fetchone()[0]
            self._event(c, lease.task_id, "evidence", id=eid, exit_code=code, token=lease.token)
        return {"id": eid, "exit_code": code, "output": output, "artifact_refs": after}

    def _current_refs(self, c, lease, refs) -> list[dict]:
        out = []
        for r in sorted(set(refs)):
            row = c.execute("SELECT sha256 FROM artifacts WHERE task_id=? AND attempt=? AND relpath=?",
                            (lease.task_id, lease.attempt, r)).fetchone()
            if row is None:
                raise LedgerError(f"kanıt referansı bu denemede kayıtlı değil: {r}")
            out.append({"path": r, "sha256": row["sha256"]})
        return out

    def post_message(self, sender: str, recipient: str, body: str, *,
                     lease: Lease | None = None, task_id: str | None = None) -> int:
        """Ajanlar arası mesaj. Lease verilirse fencing kontrolü yapılır."""
        with self.tx() as c:
            if lease is not None:
                self._check_lease(c, lease)
                task_id = lease.task_id
            c.execute("INSERT INTO messages(sender,recipient,task_id,body,created) VALUES (?,?,?,?,?)",
                      (sender, recipient, task_id, body, self.clock()))
            return c.execute("SELECT last_insert_rowid()").fetchone()[0]

    def messages_for(self, recipients: Iterable[str], since_id: int = 0) -> list[dict]:
        rs = list(recipients) + ["*"]
        q = f"SELECT * FROM messages WHERE id>? AND recipient IN ({','.join('?' * len(rs))}) ORDER BY id"
        return [dict(r) for r in self.conn.execute(q, [since_id, *rs]).fetchall()]

    # ------------------------------------------------------------------ bitiş
    def complete(self, lease: Lease, result: str = "") -> dict:
        """Kabul kapısını *aynı yazma işlemi içinde* çalıştırır; geçerse ``succeeded``."""
        from .qa import acceptance_gate

        with self.tx() as c:
            self._check_lease(c, lease)
            report = acceptance_gate(self, lease.task_id, conn=c)
            if not report["passed"]:
                self._event(c, lease.task_id, "gate_failed", reasons=report["reasons"])
                raise GateFailed(report["reasons"])
            c.execute(
                "UPDATE tasks SET state='succeeded', result=?, lease_owner=NULL, lease_token=NULL,"
                " lease_expires=NULL, updated=? WHERE id=? AND lease_token=?",
                (result, self.clock(), lease.task_id, lease.token))
            self._event(c, lease.task_id, "succeeded", token=lease.token)
        self.promote()
        return report

    def fail(self, lease: Lease, error: str, *, retry: bool = True) -> str:
        with self.tx() as c:
            row = self._check_lease(c, lease)
            new = "ready" if retry and row["attempt"] < row["max_attempts"] else "failed"
            c.execute(
                "UPDATE tasks SET state=?, error=?, lease_owner=NULL, lease_token=NULL, lease_expires=NULL,"
                " updated=? WHERE id=? AND lease_token=?",
                (new, error[-4000:], self.clock(), lease.task_id, lease.token))
            self._event(c, lease.task_id, "failed_attempt", error=error[-500:], to=new)
        if new == "failed":
            self.promote()
        return new

    # ------------------------------------------------------------------ okuma
    def task(self, task_id: str) -> dict:
        r = self.conn.execute("SELECT * FROM tasks WHERE id=?", (task_id,)).fetchone()
        if r is None:
            raise LedgerError(f"görev yok: {task_id}")
        d = dict(r)
        d["spec"] = json.loads(d["spec"])
        d["depends_on"] = json.loads(d["depends_on"])
        return d

    def tasks(self) -> list[dict]:
        rows = self.conn.execute("SELECT id FROM tasks ORDER BY created, id").fetchall()
        return [self.task(r["id"]) for r in rows]

    def artifacts(self, task_id: str, attempt: int | None = None, conn=None) -> list[dict]:
        c = conn or self.conn
        if attempt is None:
            attempt = c.execute("SELECT attempt FROM tasks WHERE id=?", (task_id,)).fetchone()[0]
        return [dict(r) for r in c.execute(
            "SELECT * FROM artifacts WHERE task_id=? AND attempt=? ORDER BY relpath",
            (task_id, attempt)).fetchall()]

    def evidence(self, task_id: str, attempt: int | None = None, conn=None) -> list[dict]:
        c = conn or self.conn
        if attempt is None:
            attempt = c.execute("SELECT attempt FROM tasks WHERE id=?", (task_id,)).fetchone()[0]
        out = []
        for r in c.execute("SELECT * FROM evidence WHERE task_id=? AND attempt=? ORDER BY id",
                           (task_id, attempt)).fetchall():
            d = dict(r)
            d["argv"] = json.loads(d["argv"])
            d["artifact_refs"] = json.loads(d["artifact_refs"])
            out.append(d)
        return out

    def events(self, task_id: str | None = None) -> list[dict]:
        if task_id:
            rows = self.conn.execute("SELECT * FROM events WHERE task_id=? ORDER BY id", (task_id,))
        else:
            rows = self.conn.execute("SELECT * FROM events ORDER BY id")
        return [dict(r) | {"detail": json.loads(r["detail"])} for r in rows.fetchall()]

    def status(self) -> dict:
        counts = {s: 0 for s in STATES}
        for r in self.conn.execute("SELECT state, COUNT(*) n FROM tasks GROUP BY state"):
            counts[r["state"]] = r["n"]
        return {"counts": counts, "tasks": [
            {k: t[k] for k in ("id", "title", "role", "state", "attempt", "lease_owner", "lease_token",
                               "lease_expires", "error", "result")}
            for t in self.tasks()]}

    def read_artifact(self, task_id: str, relpath: str, attempt: int | None = None) -> bytes:
        if attempt is None:
            attempt = self.task(task_id)["attempt"]
        return self.store.read_bytes(f"{self.attempt_dir(task_id, attempt)}/{relpath}")


def new_worker_id(prefix: str = "worker") -> str:
    return f"{prefix}-{uuid.uuid4().hex[:8]}"
