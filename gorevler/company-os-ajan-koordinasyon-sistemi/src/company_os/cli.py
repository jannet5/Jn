"""Komut satırı: ``python -m company_os <komut>`` ya da kurulumdan sonra ``company-os``."""
from __future__ import annotations

import argparse
import json
import os
import shutil
import sys

from . import __version__, skills as skills_mod
from .ledger import Ledger
from .orchestrator import build_report, publish, report_markdown, run_plan

KIT = os.path.join(os.path.dirname(__file__), "kit")


def _ws(a):
    return a.workspace or os.path.join(os.path.dirname(os.path.abspath(a.db)), "workspace")


def _dump(obj):
    print(json.dumps(obj, ensure_ascii=False, indent=1))


def install_kit(target: str, db: str, python: str = sys.executable) -> list[str]:
    """Claude Code projesine alt ajanları, orkestrasyon skill'ini ve .mcp.json kaydını kurar."""
    written = []
    for sub in ("agents", "skills"):
        src = os.path.join(KIT, sub)
        for dirpath, _, files in os.walk(src):
            for f in files:
                rel = os.path.relpath(os.path.join(dirpath, f), src)
                dst = os.path.join(target, ".claude", sub, rel)
                os.makedirs(os.path.dirname(dst), exist_ok=True)
                shutil.copyfile(os.path.join(dirpath, f), dst)
                written.append(os.path.relpath(dst, target))
    mcp_path = os.path.join(target, ".mcp.json")
    cfg = {}
    if os.path.exists(mcp_path):
        with open(mcp_path, encoding="utf-8") as fh:
            cfg = json.load(fh)
    src_root = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
    cfg.setdefault("mcpServers", {})["company-os"] = {
        "type": "stdio",
        "command": python,
        "args": ["-m", "company_os", "mcp-serve", "--db", os.path.abspath(db),
                 "--skills", os.path.join(os.path.abspath(target), ".claude", "skills")],
        "env": {"PYTHONPATH": src_root},
    }
    with open(mcp_path, "w", encoding="utf-8") as fh:
        json.dump(cfg, fh, indent=2, ensure_ascii=False)
        fh.write("\n")
    written.append(".mcp.json")
    return written


def expand_placeholders(obj, plan_dir: str):
    """Plandaki "{plan_dir}" yer tutucularını plan dosyasının dizini ile değiştirir."""
    if isinstance(obj, str):
        return obj.replace("{plan_dir}", plan_dir)
    if isinstance(obj, list):
        return [expand_placeholders(x, plan_dir) for x in obj]
    if isinstance(obj, dict):
        return {k: expand_placeholders(v, plan_dir) for k, v in obj.items()}
    return obj


def main(argv=None) -> int:
    p = argparse.ArgumentParser(prog="company-os", description="Company OS ajan koordinasyon sistemi")
    p.add_argument("--version", action="version", version=__version__)
    sub = p.add_subparsers(dest="cmd", required=True)

    def common(sp, skills=False):
        sp.add_argument("--db", default="company-os.db", help="SQLite defter dosyası")
        sp.add_argument("--workspace", help="artifact kök dizini (varsayılan: db yanında workspace/)")
        if skills:
            sp.add_argument("--skills", action="append", default=[],
                            help="SKILL.md dizini (tekrarlanabilir)")

    sp = sub.add_parser("run", help="planı ajanlara dağıt, çalıştır, sonuçları topla")
    common(sp, True)
    sp.add_argument("plan")
    sp.add_argument("--workers", type=int, default=3)
    sp.add_argument("--ttl", type=float, default=60)
    sp.add_argument("--deadline", type=float, default=1800)
    sp.add_argument("--out", help="doğrulanmış çıktıların toplanacağı dizin")
    sp.add_argument("--report", help="Markdown rapor dosyası")
    sp.add_argument("--resume", action="store_true", help="planı yeniden yükleme, mevcut defterden devam et")

    for name, hlp in (("status", "durum"), ("reconcile", "süresi dolmuş lease'leri geri al"),
                      ("report", "sonuç raporu (JSON)")):
        common(sub.add_parser(name, help=hlp))

    sp = sub.add_parser("retry", help="blocked/failed görevi yeniden ready yap")
    common(sp)
    sp.add_argument("task_id")

    sp = sub.add_parser("gate", help="bir görevin kabul kapısı raporu")
    common(sp)
    sp.add_argument("task_id")

    sp = sub.add_parser("publish", help="başarılı çıktıları bir dizine topla")
    common(sp)
    sp.add_argument("out")

    sp = sub.add_parser("mcp-serve", help="stdio MCP sunucusu (Claude Code için)")
    common(sp, True)

    sp = sub.add_parser("skills", help="skill'leri listele / seç")
    sp.add_argument("--skills", action="append", default=[])
    sp.add_argument("--query", default="")

    sp = sub.add_parser("install-kit", help="Claude Code projesine alt ajanları + skill + .mcp.json kur")
    sp.add_argument("target")
    sp.add_argument("--db", default=None, help="varsayılan: <target>/.company-os/company-os.db")

    a = p.parse_args(argv)
    if a.cmd == "mcp-serve":
        from .mcp_server import main as serve
        serve(a.db, a.workspace, a.skills or list(skills_mod.DEFAULT_DIRS))
        return 0
    if a.cmd == "skills":
        sk = skills_mod.discover(a.skills or list(skills_mod.DEFAULT_DIRS))
        if a.query:
            sk = skills_mod.select(sk, text=a.query, limit=5)
        _dump([s.as_dict() for s in sk])
        return 0
    if a.cmd == "install-kit":
        db = a.db or os.path.join(a.target, ".company-os", "company-os.db")
        os.makedirs(os.path.dirname(os.path.abspath(db)), exist_ok=True)
        Ledger(db).close()
        _dump({"installed": install_kit(a.target, db), "db": os.path.abspath(db)})
        return 0
    if a.cmd == "run":
        with open(a.plan, encoding="utf-8") as fh:
            plan = json.load(fh)
        plan_dir = os.path.dirname(os.path.abspath(a.plan))
        plan = expand_placeholders(plan, plan_dir)
        sdirs = a.skills or [os.path.join(plan_dir, d) for d in plan.get("skill_dirs", [])]
        rep = run_plan(plan, a.db, _ws(a), skill_dirs=sdirs, workers=a.workers, ttl=a.ttl,
                       deadline=a.deadline, load=not a.resume)
        L = Ledger(a.db, _ws(a))
        try:
            if a.out:
                rep["published"] = publish(L, a.out)
        finally:
            L.close()
        if a.report:
            with open(a.report, "w", encoding="utf-8") as fh:
                fh.write(report_markdown(rep))
        _dump(rep)
        return 0 if rep["all_succeeded"] else 1

    L = Ledger(a.db, _ws(a))
    try:
        if a.cmd == "status":
            _dump(L.status())
        elif a.cmd == "reconcile":
            _dump(L.reconcile() | {"promoted": L.promote()})
        elif a.cmd == "report":
            _dump(build_report(L))
        elif a.cmd == "retry":
            L.transition(a.task_id, "ready", reason="cli retry")
            _dump(L.task(a.task_id))
        elif a.cmd == "gate":
            from .qa import acceptance_gate
            _dump(acceptance_gate(L, a.task_id))
        elif a.cmd == "publish":
            _dump(publish(L, a.out))
        return 0
    finally:
        L.close()
