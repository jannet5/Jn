"""Codex = beyin. Analiz `codex exec --image ... --output-schema`, üretim `codex exec '$imagegen ...'`.

Ücret koruması: Codex ChatGPT (Pro) girişiyle çalışınca kullanım plan limitinden düşer, API faturası
çıkmaz. OPENAI_API_KEY / CODEX_API_KEY ortamda varsa Codex API faturasına geçebileceği için
bu değişkenler alt sürece AKTARILMAZ (allow_api_billing=True verilmedikçe).
"""
from __future__ import annotations

import os
import re
import shutil
import tempfile
import time
from concurrent.futures import ThreadPoolExecutor, as_completed
from pathlib import Path

from . import launcher, prompts
from .store import VARIANT_IDS, Workspace, read_json, write_json

SCHEMA = Path(__file__).parent / "schemas" / "analysis.schema.json"
LIMIT_MARKERS = ("usage limit", "rate limit", "rate_limit", "429", "quota", "try again at", "too many requests")
BILLING_VARS = ("OPENAI_API_KEY", "CODEX_API_KEY")
AUTH_MARKERS = ("401 unauthorized", "not logged in", "missing bearer", "please log in", "codex login")


class LimitReached(RuntimeError):
    """ChatGPT plan kullanım limiti doldu: checkpoint'te dur, sonra aynı komutla devam."""


class AuthMissing(RuntimeError):
    """Codex ChatGPT girişi yok/geçersiz: 1000 kez denemek yerine hemen dur."""


class CodexUnavailable(RuntimeError):
    """Codex bulunamadı/başlatılamadı (WinError 193, ENOENT, izin): hemen dur, nedeni göster."""


def codex_launcher() -> launcher.Launcher:
    """Çalışma zamanı ve doctor'ın kullandığı TEK çözümleyici: VF_CODEX_BIN (tam yol, boşluklu olabilir) ya da PATH."""
    return launcher.resolve("codex", "VF_CODEX_BIN")


def child_env(allow_api_billing: bool = False) -> dict:
    env = dict(os.environ)
    if not allow_api_billing:
        for k in BILLING_VARS:
            env.pop(k, None)
    return env


def _run(args: list[str], stdin: str | None, timeout: int, allow_api_billing: bool, cwd: Path | None = None):
    try:
        proc = launcher.run(codex_launcher(), args, input_text=stdin, timeout=timeout,
                            env=child_env(allow_api_billing), cwd=cwd)
    except launcher.LaunchError as e:
        raise CodexUnavailable(str(e)) from e
    blob = f"{proc.stdout}\n{proc.stderr}".lower()
    if proc.returncode != 0 and any(m in blob for m in AUTH_MARKERS):
        raise AuthMissing((proc.stderr or proc.stdout).strip()[-300:])
    if proc.returncode != 0 and any(m in blob for m in LIMIT_MARKERS):
        raise LimitReached((proc.stderr or proc.stdout).strip()[-300:])
    return proc


# --- Doğrulama --------------------------------------------------------------

def _tokens(s: str) -> set[str]:
    return set(re.findall(r"[a-zA-ZçğıöşüÇĞİÖŞÜ]{3,}", s.lower()))


def jaccard(a: str, b: str) -> float:
    ta, tb = _tokens(a), _tokens(b)
    return len(ta & tb) / max(len(ta | tb), 1)


def validate_analysis(data: dict, shortcode: str, max_prompt_similarity: float = 0.6) -> list[str]:
    errs = []
    if not isinstance(data, dict):
        return ["JSON nesnesi değil"]
    for key in ("why_it_worked", "success_factors", "variants", "audience_emotion", "hook_type"):
        if key not in data:
            errs.append(f"eksik alan: {key}")
    if errs:
        return errs
    if data.get("shortcode") != shortcode:
        errs.append("shortcode uyuşmuyor")
    wiw = data["why_it_worked"]
    for k in ("visual", "audio", "caption", "combined"):
        if not str(wiw.get(k, "")).strip():
            errs.append(f"why_it_worked.{k} boş")
    if len(data["success_factors"]) < 3:
        errs.append("en az 3 başarı faktörü gerekli")
    vs = data["variants"]
    if [v.get("id") for v in vs] != list(VARIANT_IDS):
        errs.append("variants tam olarak v1..v4 olmalı")
    else:
        for v in vs:
            if len(v.get("image_prompt", "")) < 40:
                errs.append(f"{v['id']} image_prompt çok kısa")
        for i in range(4):
            for j in range(i + 1, 4):
                sim = jaccard(vs[i]["image_prompt"], vs[j]["image_prompt"])
                if sim > max_prompt_similarity:
                    errs.append(f"{vs[i]['id']}~{vs[j]['id']} istemleri fazla benzer ({sim:.2f})")
    return errs


# --- Analiz -----------------------------------------------------------------

def analyze_one(ws: Workspace, post: dict, allow_api_billing: bool = False, retries: int = 1,
                timeout: int = 600) -> dict:
    code = post["shortcode"]
    images = ws.media_files(code)[:4]
    if not images:
        return {"shortcode": code, "status": "skip", "reason": "medya yok"}
    prompt = prompts.analysis_prompt(post, len(images))
    last_errs: list[str] = []
    for attempt in range(retries + 1):
        if last_errs:
            prompt += "\nÖnceki denemenin sorunları, düzelt: " + "; ".join(last_errs)
        with tempfile.TemporaryDirectory() as td:
            out = Path(td) / "last.json"
            cmd = ["exec", "--skip-git-repo-check", "--ephemeral", "--sandbox", "read-only",
                   "--output-schema", str(SCHEMA), "-o", str(out)]
            for img in images:
                cmd += ["--image", str(img)]
            cmd.append("-")
            proc = _run(cmd, prompt, timeout, allow_api_billing, cwd=ws.post_dir(code))
            data = read_json(out)
        if data is None:
            last_errs = [f"codex çıktı üretmedi (exit {proc.returncode}): {proc.stderr.strip()[-200:]}"]
            continue
        last_errs = validate_analysis(data, code)
        if not last_errs:
            data["_meta"] = {"runner": "codex", "images": [str(i.name) for i in images], "attempts": attempt + 1}
            write_json(ws.analysis_path(code), data)
            return {"shortcode": code, "status": "ok"}
    return {"shortcode": code, "status": "fail", "reason": "; ".join(last_errs)}


def _parallel(fn, items, jobs: int, log):
    results = []
    with ThreadPoolExecutor(max_workers=max(jobs, 1)) as ex:
        futs = {ex.submit(fn, it): it for it in items}
        try:
            for fut in as_completed(futs):
                r = fut.result()
                results.append(r)
                log(f"{r.get('shortcode')} {r.get('variant', '')} {r['status']} {r.get('reason', '')}".rstrip())
        except LimitReached as e:
            for f in futs:
                f.cancel()
            log(f"DURDU: ChatGPT plan kullanım limiti ({e}). İlerleme kayıtlı; limit sıfırlanınca aynı komutu tekrar çalıştırın.")
            results.append({"status": "limit"})
        except CodexUnavailable as e:
            for f in futs:
                f.cancel()
            log(f"DURDU: Codex başlatılamadı: {e}. Kontrol: `python -m viralforge doctor`.")
            results.append({"status": "launch"})
        except AuthMissing as e:
            for f in futs:
                f.cancel()
            log(f"DURDU: Codex ChatGPT girişi yok ({str(e)[-120:]}). "
                "Çözüm: `codex login` → Sign in with ChatGPT; sonra aynı komut.")
            results.append({"status": "auth"})
    return results


def analyze_all(ws: Workspace, jobs: int = 2, limit: int | None = None, allow_api_billing: bool = False,
                log=print) -> dict:
    todo = [p for p in ws.iter_posts(selected_only=True)
            if not validate_analysis_file(ws, p["shortcode"])]
    if limit:
        todo = todo[:limit]
    res = _parallel(lambda p: analyze_one(ws, p, allow_api_billing), todo, jobs, log)
    return _summ(res)


def validate_analysis_file(ws: Workspace, code: str) -> bool:
    data = read_json(ws.analysis_path(code))
    return bool(data) and not validate_analysis(data, code)


# --- Görsel üretim ----------------------------------------------------------

def _codex_generated_dir() -> Path:
    return Path(os.environ.get("CODEX_HOME", Path.home() / ".codex")) / "generated_images"


def generate_one(ws: Workspace, code: str, variant: dict, allow_api_billing: bool = False,
                 timeout: int = 900) -> dict:
    target = ws.variant_path(code, variant["id"])
    if target.exists() and target.stat().st_size > 0:
        return {"shortcode": code, "variant": variant["id"], "status": "exists"}
    target.parent.mkdir(parents=True, exist_ok=True)
    started = time.time()
    cmd = ["exec", "--skip-git-repo-check", "--ephemeral", "--sandbox", "workspace-write",
           "-C", str(ws.post_dir(code)), "-"]
    proc = _run(cmd, prompts.image_prompt(variant, str(target)), timeout, allow_api_billing)
    if not target.exists():
        # Codex görseli kendi klasörüne bırakıp kopyalamadıysa, bu çağrıdan sonra oluşan en yeni PNG'yi al.
        gen = _codex_generated_dir()
        fresh = sorted((p for p in gen.rglob("*.png") if p.stat().st_mtime >= started - 1),
                       key=lambda p: p.stat().st_mtime) if gen.exists() else []
        if fresh:
            shutil.copy2(fresh[-1], target)
    if target.exists() and target.stat().st_size > 0:
        return {"shortcode": code, "variant": variant["id"], "status": "ok"}
    return {"shortcode": code, "variant": variant["id"], "status": "fail",
            "reason": f"görsel oluşmadı (exit {proc.returncode}): {proc.stderr.strip()[-200:]}"}


def generate_all(ws: Workspace, jobs: int = 1, limit: int | None = None, allow_api_billing: bool = False,
                 log=print) -> dict:
    tasks = []
    for post in ws.iter_posts(selected_only=True):
        data = read_json(ws.analysis_path(post["shortcode"]))
        if not data or validate_analysis(data, post["shortcode"]):
            continue
        for v in data["variants"]:
            if not ws.variant_path(post["shortcode"], v["id"]).exists():
                tasks.append((post["shortcode"], v))
    if limit:
        tasks = tasks[:limit]
    res = _parallel(lambda t: generate_one(ws, t[0], t[1], allow_api_billing), tasks, jobs, log)
    return _summ(res)


def _summ(res: list[dict]) -> dict:
    out: dict = {"ok": 0, "fail": 0, "skip": 0, "exists": 0, "limit": 0, "auth": 0, "launch": 0}
    for r in res:
        out[r["status"]] = out.get(r["status"], 0) + 1
    out["failures"] = [r for r in res if r["status"] == "fail"][:20]
    return out
