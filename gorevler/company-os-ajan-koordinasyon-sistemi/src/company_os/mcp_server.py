"""Company OS defterini Claude Code'a (veya herhangi bir MCP istemcisine) açan
stdio MCP sunucusu. Bağımlılık yok; JSON-RPC 2.0, satır ayrımlı mesajlar.

Claude Code ana oturumu planı ``plan_submit`` ile yükler, alt ajanlar
(``kit/.claude/agents``) ``task_claim`` → ``artifact_write`` → ``evidence_run`` →
``task_complete`` döngüsünü fencing token ile yürütür.

Protokol: ``initialize`` el sıkışması desteklenir; istemcinin istediği sürüm
destekleniyorsa aynen döner. 2026-07-28 sürümündeki durumsuz (handshake'siz)
istemciler için ``tools/list`` / ``tools/call`` el sıkışma olmadan da çalışır.
"""
from __future__ import annotations

import json
import os
import sys
import traceback

from . import __version__, skills as skills_mod
from .ledger import GateFailed, Lease, Ledger, LedgerError, new_worker_id

SUPPORTED = ["2026-07-28", "2025-11-25", "2025-06-18", "2025-03-26", "2024-11-05"]


def _s(props: dict, required=()) -> dict:
    return {"type": "object", "properties": props, "required": list(required)}


STR, INT, NUM = {"type": "string"}, {"type": "integer"}, {"type": "number"}
LEASE = {"task_id": STR, "token": INT}

TOOLS = [
    ("plan_submit", "Hedefi uzman ajan görevlerine bölen planı deftere yükler. "
     "plan = {goal, tasks:[{id,title,role,depends_on,instructions,skills,acceptance}]}.",
     _s({"plan": {"type": "object"}}, ["plan"])),
    ("status", "Tüm görevlerin durumu, deneme sayısı ve lease bilgisi.", _s({})),
    ("task_claim", "Hazır bir görevi lease ile alır (running'e tek giriş). Dönen token sonraki tüm "
     "yazmalarda zorunludur. Görev talimatı, ilgili skill'ler, bağımlılık çıktıları ve mesajlar döner.",
     _s({"worker": STR, "role": STR, "task_id": STR, "ttl": NUM})),
    ("task_heartbeat", "Lease süresini uzatır.", _s(LEASE | {"ttl": NUM}, ["task_id", "token"])),
    ("artifact_write", "Görev çıktısını mevcut denemenin dizinine güvenli yazar ve hash'ini kaydeder.",
     _s(LEASE | {"path": STR, "content": STR}, ["task_id", "token", "path", "content"])),
    ("evidence_run", "Kabul komutunu (argv) deneme dizininde defter çalıştırır; sonucu verilen "
     "artifact hash'lerine bağlar.",
     _s(LEASE | {"argv": {"type": "array", "items": STR}, "refs": {"type": "array", "items": STR}},
        ["task_id", "token", "argv", "refs"])),
    ("task_complete", "Kabul kapısını çalıştırır; geçerse görevi succeeded yapar, geçmezse nedenleri döner.",
     _s(LEASE | {"summary": STR}, ["task_id", "token"])),
    ("task_fail", "Denemeyi başarısız işaretler (deneme hakkı varsa yeniden ready olur).",
     _s(LEASE | {"error": STR}, ["task_id", "token", "error"])),
    ("message_post", "Ajanlar arası mesaj (alıcı: rol, görev kimliği ya da '*').",
     _s({"sender": STR, "recipient": STR, "body": STR, "task_id": STR, "token": INT},
        ["sender", "recipient", "body"])),
    ("messages_read", "Alıcıya (rol/görev) gelen mesajlar.",
     _s({"recipients": {"type": "array", "items": STR}, "since_id": INT}, ["recipients"])),
    ("skills_list", "Kayıtlı skill'leri (ad+açıklama) listeler; query verilirse ilgili olanları seçer.",
     _s({"query": STR})),
    ("skill_read", "Bir skill'in SKILL.md gövdesini döner.", _s({"name": STR}, ["name"])),
    ("artifact_read", "Başarılı bir görevin artifact içeriğini döner.",
     _s({"task_id": STR, "path": STR}, ["task_id", "path"])),
    ("reconcile", "Süresi dolmuş lease'leri geri alır (canlı lease'lere dokunmaz).", _s({})),
    ("report", "Sonuç toplama: görev başına durum, artifact hash'leri, kanıtlar.", _s({})),
]


class Server:
    def __init__(self, db: str, workspace: str | None = None, skill_dirs=()):
        self.ledger = Ledger(db, workspace)
        self.skill_dirs = list(skill_dirs)

    # ------------------------------------------------------------ araçlar
    def _lease(self, a) -> Lease:
        t = self.ledger.task(a["task_id"])
        return Lease(a["task_id"], int(a["token"]), t["attempt"], t["lease_owner"] or "", 0.0)

    def call(self, name: str, a: dict):
        L = self.ledger
        if name == "plan_submit":
            order = L.load_plan(a["plan"])
            return {"created": order, "status": L.status()["counts"]}
        if name == "status":
            return L.status()
        if name == "task_claim":
            L.promote()
            lease = L.claim(a.get("worker") or new_worker_id("claude"), task_id=a.get("task_id"),
                            role=a.get("role"), ttl=float(a.get("ttl", 900)))
            if lease is None:
                return {"claimed": False, "status": L.status()["counts"]}
            return {"claimed": True, "lease": lease.as_dict(), **self.briefing(lease.task_id)}
        if name == "task_heartbeat":
            return L.heartbeat(self._lease(a), float(a.get("ttl", 900))).as_dict()
        if name == "artifact_write":
            return L.put_artifact(self._lease(a), a["path"], a["content"])
        if name == "evidence_run":
            return L.run_evidence(self._lease(a), a["argv"], a["refs"])
        if name == "task_complete":
            try:
                rep = L.complete(self._lease(a), a.get("summary", ""))
                return {"succeeded": True, "gate": rep}
            except GateFailed as e:
                return {"succeeded": False, "reasons": e.reasons}
        if name == "task_fail":
            return {"state": L.fail(self._lease(a), a["error"])}
        if name == "message_post":
            lease = self._lease(a) if a.get("token") is not None and a.get("task_id") else None
            mid = L.post_message(a["sender"], a["recipient"], a["body"], lease=lease,
                                 task_id=a.get("task_id"))
            return {"id": mid}
        if name == "messages_read":
            return {"messages": L.messages_for(a["recipients"], int(a.get("since_id", 0)))}
        if name == "skills_list":
            sk = skills_mod.discover(self.skill_dirs)
            if a.get("query"):
                sk = skills_mod.select(sk, text=a["query"], limit=5)
            return {"skills": [s.as_dict() for s in sk]}
        if name == "skill_read":
            for s in skills_mod.discover(self.skill_dirs):
                if s.name == a["name"]:
                    return {"name": s.name, "body": s.read()}
            raise LedgerError(f"skill yok: {a['name']}")
        if name == "artifact_read":
            t = L.task(a["task_id"])
            if t["state"] != "succeeded":
                raise LedgerError("yalnız succeeded görevlerin artifact'ları okunur")
            return {"content": L.read_artifact(a["task_id"], a["path"]).decode("utf-8", "replace")}
        if name == "reconcile":
            return L.reconcile() | {"promoted": L.promote()}
        if name == "report":
            from .orchestrator import build_report
            return build_report(L)
        raise LedgerError(f"bilinmeyen araç: {name}")

    def briefing(self, task_id: str) -> dict:
        L = self.ledger
        t = L.task(task_id)
        spec = t["spec"]
        sk = skills_mod.discover(self.skill_dirs)
        chosen = skills_mod.select(sk, names=spec.get("skills", []),
                                   text=f"{t['title']} {spec.get('instructions', '')}")
        deps = []
        for d in t["depends_on"]:
            dt = L.task(d)
            deps.append({"task_id": d, "result": dt["result"],
                         "artifacts": [{"path": x["relpath"], "sha256": x["sha256"]} for x in L.artifacts(d)]})
        return {
            "task": {k: t[k] for k in ("id", "title", "role", "attempt")},
            "instructions": spec.get("instructions", ""),
            "acceptance": spec.get("acceptance", []),
            "skills": [s.as_dict() for s in chosen],
            "dependencies": deps,
            "messages": L.messages_for([task_id, t["role"]]),
            "attempt_dir": L.store.abspath(L.attempt_dir(task_id, t["attempt"])),
        }

    # ------------------------------------------------------------ JSON-RPC
    def handle(self, msg: dict) -> dict | None:
        mid, method = msg.get("id"), msg.get("method")
        params = msg.get("params") or {}
        if mid is None:  # bildirim
            return None
        try:
            if method == "initialize":
                want = params.get("protocolVersion")
                result = {
                    "protocolVersion": want if want in SUPPORTED else SUPPORTED[0],
                    "capabilities": {"tools": {"listChanged": False}},
                    "serverInfo": {"name": "company-os", "version": __version__},
                    "instructions": "Company OS görev defteri. Önce status, sonra task_claim; "
                                    "her yazmada lease token gönder; bitirmeden önce evidence_run.",
                }
            elif method == "ping":
                result = {}
            elif method == "tools/list":
                result = {"tools": [{"name": n, "description": d, "inputSchema": s} for n, d, s in TOOLS]}
            elif method == "tools/call":
                name = params.get("name")
                if name not in {t[0] for t in TOOLS}:
                    return _err(mid, -32602, f"bilinmeyen araç: {name}")
                try:
                    out = self.call(name, params.get("arguments") or {})
                    result = {"content": [{"type": "text", "text": json.dumps(out, ensure_ascii=False, indent=1)}],
                              "structuredContent": out if isinstance(out, dict) else {"value": out},
                              "isError": False}
                except (LedgerError, ValueError, KeyError, OSError) as e:
                    result = {"content": [{"type": "text", "text": f"{type(e).__name__}: {e}"}], "isError": True}
            else:
                return _err(mid, -32601, f"desteklenmeyen yöntem: {method}")
            return {"jsonrpc": "2.0", "id": mid, "result": result}
        except Exception as e:  # beklenmeyen: sunucuyu düşürme
            traceback.print_exc(file=sys.stderr)
            return _err(mid, -32603, str(e))

    def serve(self, inp=sys.stdin, out=sys.stdout):
        for line in inp:
            line = line.strip()
            if not line:
                continue
            try:
                msg = json.loads(line)
            except json.JSONDecodeError:
                resp = _err(None, -32700, "ayrıştırma hatası")
            else:
                if isinstance(msg, list):
                    resps = [r for r in (self.handle(m) for m in msg) if r]
                    resp = resps or None
                else:
                    resp = self.handle(msg)
            if resp is not None:
                out.write(json.dumps(resp, ensure_ascii=False) + "\n")
                out.flush()


def _err(mid, code, message):
    return {"jsonrpc": "2.0", "id": mid, "error": {"code": code, "message": message}}


def main(db: str, workspace: str | None, skill_dirs):
    # stdout yalnız protokol içindir; her şey stderr'e
    Server(db, workspace, skill_dirs).serve()
