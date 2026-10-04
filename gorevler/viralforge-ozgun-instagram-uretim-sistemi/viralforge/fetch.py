"""İsteğe bağlı otomatik zenginleştirme: yt-dlp ile kullanıcının KENDİ tarayıcı çerezleri.

Instagram Kullanım Koşulları izinsiz otomatik veri toplamayı yasaklar; bu yüzden bu adaptör
varsayılan değildir, yavaştır (varsayılan 25 sn aralık) ve yalnızca kullanıcı açıkça
`--i-accept-instagram-terms-risk` verdiğinde çalışır. Varsayılan yol tarayıcı yakalama
(capture/) veya Instagram "Bilgilerini indir" dışa aktarımıdır.
"""
from __future__ import annotations

import datetime as dt
import json
import os
import shutil
import time
import urllib.request
from pathlib import Path

from . import launcher
from .importers import merge_meta
from .store import Workspace

STOP_MARKERS = ("429", "rate-limit", "rate limit", "login required", "checkpoint_required",
                "please wait a few minutes", "challenge_required")


class FetchStopped(RuntimeError):
    pass


def _download(url: str, dest: Path, timeout: int = 60) -> bool:
    req = urllib.request.Request(url, headers={"User-Agent": "Mozilla/5.0"})
    with urllib.request.urlopen(req, timeout=timeout) as r, open(dest, "wb") as fh:
        shutil.copyfileobj(r, fh)
    return dest.stat().st_size > 0


def _best_image(info: dict) -> str | None:
    thumbs = info.get("thumbnails") or []
    thumbs = [t for t in thumbs if t.get("url")]
    if thumbs:
        return max(thumbs, key=lambda t: (t.get("width") or 0) * (t.get("height") or 0))["url"]
    return info.get("thumbnail") or info.get("display_url")


def fetch_one(ws: Workspace, post: dict, cookies: str | None, ytdlp: launcher.Launcher,
              with_audio: bool = False) -> dict:
    cmd = ["-J", "--ignore-no-formats-error", "--no-warnings"]
    if cookies:
        cmd += ["--cookies", cookies]
    cmd.append(post["url"])
    proc = launcher.run(ytdlp, cmd, timeout=180)
    err = (proc.stderr or "").lower()
    if proc.returncode != 0 or not proc.stdout.strip():
        if any(m in err for m in STOP_MARKERS):
            raise FetchStopped(proc.stderr.strip()[-400:])
        post["fetch"] = {"adapter": "yt-dlp", "at": dt.datetime.now(dt.timezone.utc).isoformat(),
                         "error": proc.stderr.strip()[-400:] or f"exit {proc.returncode}"}
        ws.save_post(post)
        return post

    info = json.loads(proc.stdout)
    entries = info.get("entries") or [info]
    merge_meta(post, {**entries[0], **{k: v for k, v in info.items() if k != "entries" and v}}, "yt-dlp")
    mdir = ws.post_dir(post["shortcode"]) / "media"
    mdir.mkdir(parents=True, exist_ok=True)
    for i, entry in enumerate(entries[:10], 1):
        img = _best_image(entry)
        if not img:
            continue
        rel = f"media/{i:02d}.jpg"
        if not (ws.post_dir(post["shortcode"]) / rel).exists():
            _download(img, ws.post_dir(post["shortcode"]) / rel)
        if not any(m.get("file") == rel for m in post["media"]):
            post["media"].append({"file": rel, "remote": img})

    if with_audio and post["kind"] == "reel":
        out = mdir / "audio.%(ext)s"
        acmd = ["-x", "--audio-format", "m4a", "-o", str(out), post["url"]]
        if cookies:
            acmd[0:0] = ["--cookies", cookies]
        launcher.run(ytdlp, acmd, timeout=300)
        audio_file = mdir / "audio.m4a"
        if audio_file.exists():
            post["audio"]["local_file"] = "media/audio.m4a"
            post["audio"]["features"] = audio_features(audio_file)
            if post["audio"]["status"] == "unknown":
                post["audio"]["status"] = "known"
    post["fetch"] = {"adapter": "yt-dlp", "at": dt.datetime.now(dt.timezone.utc).isoformat(), "error": None}
    ws.save_post(post)
    return post


def audio_features(path: Path) -> dict:
    """ffprobe/ffmpeg ile süre ve ortalama ses yüksekliği (Codex sesi doğrudan dinlemez; sayısal özet verilir)."""
    feats = {}
    try:
        out = launcher.run(launcher.resolve("ffprobe", "VF_FFPROBE_BIN"),
                           ["-v", "error", "-show_entries", "format=duration", "-of", "default=nw=1:nk=1", str(path)],
                           timeout=60)
        feats["duration_s"] = round(float(out.stdout.strip()), 2)
        vol = launcher.run(launcher.resolve("ffmpeg", "VF_FFMPEG_BIN"),
                           ["-hide_banner", "-i", str(path), "-af", "volumedetect", "-f", "null", "-"],
                           timeout=120).stderr
        for line in vol.splitlines():
            if "mean_volume" in line:
                feats["mean_volume_db"] = float(line.split(":")[1].split()[0])
            if "max_volume" in line:
                feats["max_volume_db"] = float(line.split(":")[1].split()[0])
    except (ValueError, launcher.LaunchError):  # ffmpeg yoksa ses özellikleri boş kalır
        pass
    return feats


def fetch_all(ws: Workspace, cookies: str | None, delay: float = 25.0, limit: int | None = None,
              with_audio: bool = False, log=print) -> dict:
    try:
        ytdlp = launcher.resolve("yt-dlp", "VF_YTDLP_BIN")
    except launcher.LaunchError as e:
        raise SystemExit(f"{e}\nKurulum: python -m pip install yt-dlp (yalnız isteğe bağlı fetch için).")
    done = failed = 0
    todo = [p for p in ws.iter_posts() if not (p.get("fetch") and not p["fetch"].get("error") and p["media"])]
    if limit:
        todo = todo[:limit]
    for i, post in enumerate(todo):
        try:
            post = fetch_one(ws, post, cookies, ytdlp, with_audio=with_audio)
        except launcher.LaunchError as e:
            log(f"DURDU: yt-dlp başlatılamadı: {e}")
            return {"fetched": done, "failed": failed, "stopped": True, "remaining": len(todo) - i}
        except FetchStopped as e:
            log(f"DURDU (Instagram sınırı/oturum): {e}\nCheckpoint kaydedildi; birkaç saat sonra aynı komutla devam edin.")
            return {"fetched": done, "failed": failed, "stopped": True, "remaining": len(todo) - i}
        if post["fetch"] and post["fetch"].get("error"):
            failed += 1
        else:
            done += 1
        log(f"[{i + 1}/{len(todo)}] {post['shortcode']} {'HATA' if post['fetch'].get('error') else 'ok'}")
        if i + 1 < len(todo):
            time.sleep(delay)
    return {"fetched": done, "failed": failed, "stopped": False, "remaining": 0}


def ensure_terms_ack(flag: bool) -> None:
    if not flag and os.environ.get("VF_ACCEPT_IG_TERMS_RISK") != "1":
        raise SystemExit(
            "Otomatik Instagram çekimi Instagram Kullanım Koşulları açısından risklidir (hesap kısıtlanabilir).\n"
            "Önerilen yol: capture/ içindeki tarayıcı yakalama düğmesi veya 'Bilgilerini indir' dışa aktarımı.\n"
            "Yine de kendi sorumluluğunuzda çalıştırmak için --i-accept-instagram-terms-risk ekleyin."
        )
