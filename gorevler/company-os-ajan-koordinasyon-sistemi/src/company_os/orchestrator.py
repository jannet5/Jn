"""İş dağıtımı ve sonuç toplama.

``run_plan`` planı deftere yükler, N worker iş parçacığı başlatır (her biri kendi
SQLite bağlantısıyla), worker'lar görevleri lease ile alır, bağımlılık
çıktılarını ve mesajları girdi olarak alır, yürütücüyü çalıştırır, kabul
komutlarını kanıt olarak kaydeder ve kabul kapısından geçerse tamamlar.
Bitince ``build_report``/``publish`` doğrulanmış çıktıları tek yerde toplar.

Yürütücüler:
* ``command`` – argv'yi deneme dizininde çalıştırır, ``outputs`` dosyalarını kaydeder.
* ``mcp``     – stdio MCP sunucusundaki bir aracı çağırır, sonucu artifact yapar.
* ``claude``  – ``claude -p`` (Claude Code başsız mod) ile skill bağlamlı istem çalıştırır.
"""
from __future__ import annotations

import json
import os
import shutil
import subprocess
import sys
import threading
import time
from concurrent.futures import ThreadPoolExecutor

from . import skills as skills_mod
from .artifacts import SafeRoot
from .ledger import GateFailed, Lease, Ledger, LedgerError, LeaseLost, new_worker_id
from .mcp_client import StdioMCPClient


class ExecutorError(RuntimeError):
    pass


def _prepare_inputs(L: Ledger, lease: Lease, task: dict, skill_list) -> dict:
    """Bağımlılık çıktıları (hash doğrulamalı), mesajlar ve skill bağlamı → inputs/."""
    base = L.attempt_dir(lease.task_id, lease.attempt)
    manifest = {"task": {k: task[k] for k in ("id", "title", "role")}, "dependencies": {},
                "skills": [s.as_dict() for s in skill_list]}
    for dep in task["depends_on"]:
        dt = L.task(dep)
        if dt["state"] != "succeeded":
            raise ExecutorError(f"bağımlılık tamamlanmamış: {dep}")
        files = {}
        for a in L.artifacts(dep):
            data = L.read_artifact(dep, a["relpath"])
            from .artifacts import sha256_bytes
            if sha256_bytes(data) != a["sha256"]:
                raise ExecutorError(f"{dep}/{a['relpath']}: girdi hash'i uyuşmuyor")
            L.store.write_bytes(f"{base}/inputs/{dep}/{a['relpath']}", data)
            files[a["relpath"]] = a["sha256"]
        manifest["dependencies"][dep] = {"result": dt["result"], "files": files}
    manifest["messages"] = L.messages_for([lease.task_id, task["role"]])
    L.store.write_bytes(f"{base}/inputs/manifest.json",
                        json.dumps(manifest, ensure_ascii=False, indent=1).encode())
    return manifest


def _env(L: Ledger, lease: Lease, extra: dict | None = None) -> dict:
    env = dict(os.environ)
    env.update({
        "COS_TASK_ID": lease.task_id,
        "COS_ATTEMPT": str(lease.attempt),
        "COS_INPUTS": "inputs",
        "COS_LEDGER_DB": os.path.abspath(L.db_path),
    })
    env.update(extra or {})
    return env


def execute(L: Ledger, lease: Lease, task: dict, skill_list, *, heartbeat) -> str:
    spec = task["spec"]
    ex = spec.get("executor") or {}
    kind = ex.get("type", "command")
    cwd = L.store.abspath(L.attempt_dir(lease.task_id, lease.attempt))
    timeout = float(ex.get("timeout", 600))
    if kind == "command":
        argv = ex.get("argv")
        if not argv:
            raise ExecutorError("command yürütücüsü argv ister")
        argv = [sys.executable if a == "{python}" else a for a in argv]
        p = subprocess.run(argv, cwd=cwd, capture_output=True, text=True, timeout=timeout,
                           env=_env(L, lease, ex.get("env")))
        log = (p.stdout + p.stderr)[-20000:]
        # tanı günlüğü: kabul snapshot'ına girmez, deneme dizininde kalır
        L.store.write_bytes(f"{L.attempt_dir(lease.task_id, lease.attempt)}/_logs/executor.log",
                            log.encode("utf-8"))
        if p.returncode != 0:
            raise ExecutorError(f"komut {p.returncode} ile çıktı: {log[-800:]}")
        for out in ex.get("outputs", []):
            L.register_artifact(lease, out)
        return (p.stdout.strip().splitlines() or [""])[-1][:500]
    if kind == "mcp":
        server = [sys.executable if a == "{python}" else a for a in ex["server"]]
        with StdioMCPClient(server, cwd=cwd, timeout=timeout) as cli:
            heartbeat()
            res = cli.call_tool(ex["tool"], ex.get("arguments", {}))
        out = ex.get("output", "mcp-result.json")
        L.put_artifact(lease, out, json.dumps(res, ensure_ascii=False, indent=1))
        if res.get("isError"):
            raise ExecutorError(f"MCP aracı hata döndü: {json.dumps(res)[:800]}")
        return f"MCP {ex['tool']} → {out}"
    if kind == "claude":
        claude = shutil.which(ex.get("bin", "claude"))
        if not claude:
            raise ExecutorError("claude CLI bulunamadı")
        skill_text = "\n\n".join(f"## Skill: {s.name}\n{s.read()}" for s in skill_list)
        prompt = (f"Görev: {task['title']}\nRol: {task['role']}\n\n{spec.get('instructions', '')}\n\n"
                  f"Girdiler ./inputs altında (manifest.json).\n\n{skill_text}")
        argv = [claude, "-p", prompt, "--output-format", "json"]
        if ex.get("model"):
            argv += ["--model", ex["model"]]
        for t in ex.get("allowed_tools", []):
            argv += ["--allowedTools", t]
        p = subprocess.run(argv, cwd=cwd, capture_output=True, text=True, timeout=timeout,
                           env=_env(L, lease))
        if p.returncode != 0:
            raise ExecutorError(f"claude {p.returncode}: {(p.stdout + p.stderr)[-800:]}")
        try:
            text = json.loads(p.stdout).get("result", p.stdout)
        except json.JSONDecodeError:
            text = p.stdout
        out = ex.get("output", "response.md")
        L.put_artifact(lease, out, text)
        for extra in ex.get("outputs", []):
            L.register_artifact(lease, extra)
        return text.strip()[:500]
    raise ExecutorError(f"bilinmeyen yürütücü: {kind}")


def _heartbeat_loop(L: Ledger, box: dict, ttl: float, stop: threading.Event):
    while not stop.wait(ttl / 3):
        try:
            box["lease"] = L.heartbeat(box["lease"], ttl)
        except LeaseLost:
            box["lost"] = True
            return


def work_one(db: str, workspace: str, skill_dirs, *, worker: str, ttl: float = 60,
             role: str | None = None) -> str | None:
    """Bir görev al, yürüt, kanıtla, tamamla. Alınan görev kimliğini döner."""
    L = Ledger(db, workspace)
    try:
        L.promote()
        lease = L.claim(worker, role=role, ttl=ttl)
        if lease is None:
            return None
        task = L.task(lease.task_id)
        sk = skills_mod.discover(skill_dirs)
        chosen = skills_mod.select(sk, names=task["spec"].get("skills", []),
                                   text=f"{task['title']} {task['spec'].get('instructions', '')}")
        box = {"lease": lease}
        stop = threading.Event()
        hb = threading.Thread(target=_heartbeat_loop, args=(Ledger(db, workspace), box, ttl, stop),
                              daemon=True)
        hb.start()
        try:
            _prepare_inputs(L, lease, task, chosen)
            result = execute(L, lease, task, chosen, heartbeat=lambda: None)
            for acc in task["spec"].get("acceptance", []):
                argv = [sys.executable if a == "{python}" else a for a in acc["argv"]]
                ev = L.run_evidence(lease, argv, acc["refs"], env=_env(L, lease))
                if ev["exit_code"] != 0:
                    raise ExecutorError(f"kabul komutu başarısız ({ev['exit_code']}): {ev['output'][-600:]}")
            for m in task["spec"].get("notify", []):
                L.post_message(worker, m["to"], m["body"].replace("{result}", result), lease=lease)
            L.complete(lease, result)
        except LeaseLost:
            pass  # başka worker devraldı; yazacak bir şey yok
        except (ExecutorError, GateFailed, LedgerError, OSError, subprocess.TimeoutExpired) as e:
            try:
                L.fail(lease, f"{type(e).__name__}: {e}")
            except LeaseLost:
                pass
        finally:
            stop.set()
            hb.join(timeout=2)
        return lease.task_id
    finally:
        L.close()


def run_plan(plan: dict, db: str, workspace: str, *, skill_dirs=(), workers: int = 3,
             ttl: float = 60, deadline: float = 1800, load: bool = True) -> dict:
    L = Ledger(db, workspace)
    if load:
        L.load_plan(plan)
    start = time.time()

    def loop(i: int) -> list[str]:
        done = []
        wid = new_worker_id(f"w{i}")
        W = Ledger(db, workspace)
        try:
            while time.time() - start < deadline:
                W.reconcile()
                tid = work_one(db, workspace, skill_dirs, worker=wid, ttl=ttl)
                if tid:
                    done.append(tid)
                    continue
                W.promote()
                c = W.status()["counts"]
                if c["ready"] == 0 and c["running"] == 0:
                    return done
                time.sleep(0.1)
            return done
        finally:
            W.close()

    try:
        with ThreadPoolExecutor(max_workers=workers) as pool:
            per_worker = list(pool.map(loop, range(workers)))
        rep = build_report(L)
        rep["worker_assignments"] = per_worker
        return rep
    finally:
        L.close()


def build_report(L: Ledger) -> dict:
    tasks = []
    for t in L.tasks():
        tasks.append({
            "id": t["id"], "title": t["title"], "role": t["role"], "state": t["state"],
            "attempt": t["attempt"], "result": t["result"], "error": t["error"],
            "depends_on": t["depends_on"],
            "artifacts": [{"path": a["relpath"], "sha256": a["sha256"], "size": a["size"]}
                          for a in L.artifacts(t["id"])] if t["attempt"] else [],
            "evidence": [{"id": e["id"], "argv": e["argv"], "exit_code": e["exit_code"],
                          "refs": e["artifact_refs"]} for e in L.evidence(t["id"])] if t["attempt"] else [],
        })
    st = L.status()["counts"]
    return {"counts": st, "all_succeeded": st["succeeded"] == len(tasks) and bool(tasks), "tasks": tasks}


def publish(L: Ledger, out_dir: str) -> dict:
    """Başarılı görevlerin doğrulanmış çıktılarını ``out_dir/<görev>/`` altına toplar."""
    from .artifacts import sha256_bytes
    root = SafeRoot(out_dir)
    manifest = {}
    for t in L.tasks():
        if t["state"] != "succeeded":
            continue
        for a in L.artifacts(t["id"]):
            if a["relpath"].startswith("_logs/"):
                continue
            data = L.read_artifact(t["id"], a["relpath"])
            if sha256_bytes(data) != a["sha256"]:
                raise LedgerError(f"{t['id']}/{a['relpath']}: yayın sırasında hash uyuşmuyor")
            root.write_bytes(f"{t['id']}/{a['relpath']}", data)
            manifest[f"{t['id']}/{a['relpath']}"] = a["sha256"]
    root.write_bytes("MANIFEST.json", json.dumps(manifest, indent=1, ensure_ascii=False).encode())
    return manifest


def report_markdown(rep: dict) -> str:
    lines = ["# Company OS çalışma raporu", "",
             f"Durumlar: {json.dumps(rep['counts'], ensure_ascii=False)}",
             f"Tümü başarılı: {'evet' if rep['all_succeeded'] else 'hayır'}", "",
             "| Görev | Rol | Durum | Deneme | Artifact | Kanıt (çıkış) |", "|---|---|---|---|---|---|"]
    for t in rep["tasks"]:
        arts = ", ".join(f"{a['path']} ({a['sha256'][:12]})" for a in t["artifacts"]) or "-"
        evs = ", ".join(f"#{e['id']}({e['exit_code']})" for e in t["evidence"]) or "-"
        lines.append(f"| {t['id']} | {t['role']} | {t['state']} | {t['attempt']} | {arts} | {evs} |")
    for t in rep["tasks"]:
        if t["error"]:
            lines.append(f"\n- **{t['id']}** son hata: {t['error'][:300]}")
    return "\n".join(lines) + "\n"
