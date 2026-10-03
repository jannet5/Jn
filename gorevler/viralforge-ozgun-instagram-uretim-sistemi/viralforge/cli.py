"""ViralForge komut satırı: python -m viralforge <komut>"""
from __future__ import annotations

import argparse
import json
import os
import shutil
import subprocess
import sys
from pathlib import Path

from . import brain, importers, report, select, verify
from .store import Workspace


def _ws(args) -> Workspace:
    return Workspace(args.workspace).ensure()


def _print(obj) -> None:
    print(json.dumps(obj, ensure_ascii=False, indent=2))


def cmd_init(args):
    ws = _ws(args)
    agents = ws.root / "AGENTS.md"
    if not agents.exists():
        shutil.copy(Path(__file__).parent / "AGENTS.template.md", agents)
    _print({"workspace": str(ws.root), "agents_md": str(agents)})


def cmd_doctor(args):
    """Gerçek ön koşulları kontrol eder; eksik olanı açıkça söyler."""
    out = {}
    codex = shutil.which(brain.codex_bin())
    out["codex_cli"] = codex or "YOK → npm i -g @openai/codex"
    if codex:
        r = subprocess.run([codex, "login", "status"], capture_output=True, text=True,
                           env=brain.child_env(False))
        out["codex_login"] = (r.stdout + r.stderr).strip() or f"exit {r.returncode}"
    out["api_key_in_env"] = [k for k in brain.BILLING_VARS if os.environ.get(k)]
    out["api_key_note"] = "Ortamdaki API anahtarları Codex'e aktarılmaz (ücret koruması)."
    out["yt_dlp"] = shutil.which("yt-dlp") or "yok (yalnız isteğe bağlı fetch için)"
    out["ffmpeg"] = shutil.which("ffmpeg") or "yok (yalnız ses özellikleri için)"
    _print(out)


def cmd_import_urls(args):
    _print(importers.import_urls(_ws(args), args.inputs))


def cmd_import_meta(args):
    _print(importers.import_meta(_ws(args), args.inputs))


def cmd_fetch(args):
    from . import fetch
    fetch.ensure_terms_ack(args.i_accept_instagram_terms_risk)
    _print(fetch.fetch_all(_ws(args), args.cookies, delay=args.delay, limit=args.limit, with_audio=args.audio))


def cmd_select(args):
    _print(select.select_top(_ws(args), top=args.top, min_likes=args.min_likes,
                             require_complete=not args.allow_incomplete))


def cmd_analyze(args):
    _print(brain.analyze_all(_ws(args), jobs=args.jobs, limit=args.limit,
                             allow_api_billing=args.allow_api_billing))


def cmd_generate(args):
    if args.jobs > 1:
        print("Uyarı: paralel üretimde Codex görseli hedef yola kopyalamazsa yedek eşleme yanlış olabilir.",
              file=sys.stderr)
    _print(brain.generate_all(_ws(args), jobs=args.jobs, limit=args.limit,
                              allow_api_billing=args.allow_api_billing))


def cmd_verify(args):
    _print(verify.verify_all(_ws(args), min_side=args.min_side))


def cmd_report(args):
    print(report.build_report(_ws(args)))


def cmd_chatgpt_sheet(args):
    path, n = report.build_chatgpt_sheet(_ws(args))
    _print({"sheet": str(path), "prompts": n})


def cmd_ingest(args):
    _print(report.ingest_images(_ws(args), args.folder))


def cmd_status(args):
    _print(verify.status(_ws(args), target_posts=args.target))


def cmd_done(args):
    c = verify.status(_ws(args), target_posts=args.target)
    print(verify.done_answer(c))
    sys.exit(0 if c["done"] else 1)


def cmd_run(args):
    """Toplanmış gönderilerden sona kadar: seç → analiz → üret → doğrula → rapor → bitti mi."""
    ws = _ws(args)
    _print({"select": select.select_top(ws, top=args.target)})
    a = brain.analyze_all(ws, jobs=args.jobs, allow_api_billing=args.allow_api_billing)
    _print({"analyze": a})
    if not a.get("limit") and not a.get("auth"):
        g = brain.generate_all(ws, jobs=1, allow_api_billing=args.allow_api_billing)
        _print({"generate": g})
    _print(verify.verify_all(ws, min_side=args.min_side))
    print(report.build_report(ws))
    c = verify.status(ws, target_posts=args.target)
    print(verify.done_answer(c))
    sys.exit(0 if c["done"] else 1)


def main(argv=None):
    p = argparse.ArgumentParser(prog="viralforge", description="Codex beyinli Instagram başarı analizi ve özgün varyasyon üretimi")
    p.add_argument("-w", "--workspace", default=os.environ.get("VF_WORKSPACE", "vf-workspace"))
    sub = p.add_subparsers(dest="cmd", required=True)

    sub.add_parser("init").set_defaults(fn=cmd_init)
    sub.add_parser("doctor").set_defaults(fn=cmd_doctor)
    s = sub.add_parser("import-urls"); s.add_argument("inputs", nargs="+"); s.set_defaults(fn=cmd_import_urls)
    s = sub.add_parser("import-meta"); s.add_argument("inputs", nargs="+"); s.set_defaults(fn=cmd_import_meta)
    s = sub.add_parser("fetch")
    s.add_argument("--cookies"); s.add_argument("--delay", type=float, default=25.0)
    s.add_argument("--limit", type=int); s.add_argument("--audio", action="store_true")
    s.add_argument("--i-accept-instagram-terms-risk", action="store_true"); s.set_defaults(fn=cmd_fetch)
    s = sub.add_parser("select"); s.add_argument("--top", type=int, default=1000)
    s.add_argument("--min-likes", type=int, default=0); s.add_argument("--allow-incomplete", action="store_true")
    s.set_defaults(fn=cmd_select)
    for name, fn, jobs in (("analyze", cmd_analyze, 2), ("generate", cmd_generate, 1)):
        s = sub.add_parser(name); s.add_argument("--jobs", type=int, default=jobs); s.add_argument("--limit", type=int)
        s.add_argument("--allow-api-billing", action="store_true"); s.set_defaults(fn=fn)
    s = sub.add_parser("verify"); s.add_argument("--min-side", type=int, default=512); s.set_defaults(fn=cmd_verify)
    sub.add_parser("report").set_defaults(fn=cmd_report)
    sub.add_parser("chatgpt-sheet").set_defaults(fn=cmd_chatgpt_sheet)
    s = sub.add_parser("ingest-images"); s.add_argument("folder"); s.set_defaults(fn=cmd_ingest)
    for name, fn in (("status", cmd_status), ("bitti-mi", cmd_done)):
        s = sub.add_parser(name); s.add_argument("--target", type=int, default=1000); s.set_defaults(fn=fn)
    s = sub.add_parser("run"); s.add_argument("--target", type=int, default=1000)
    s.add_argument("--jobs", type=int, default=2); s.add_argument("--min-side", type=int, default=512)
    s.add_argument("--allow-api-billing", action="store_true"); s.set_defaults(fn=cmd_run)

    args = p.parse_args(argv)
    try:
        args.fn(args)
    except brain.LimitReached as e:
        print(f"Plan limiti doldu, ilerleme kayıtlı: {e}", file=sys.stderr)
        sys.exit(75)


if __name__ == "__main__":
    main()
