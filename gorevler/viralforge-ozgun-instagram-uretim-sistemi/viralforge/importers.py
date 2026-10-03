"""Kaynak toplama: bağlantı listeleri, Instagram "Bilgilerini indir" dışa aktarımı,
tarayıcı yakalama dosyaları ve başka araçların (yt-dlp/gallery-dl/Apify) meta JSON'ları."""
from __future__ import annotations

import datetime as dt
import json
import re
import zipfile
from pathlib import Path

from . import urls
from .store import Workspace

TEXT_EXTS = {".txt", ".json", ".html", ".htm", ".csv", ".tsv", ".md", ".jsonl"}


def new_post(kind: str, code: str) -> dict:
    return {
        "shortcode": code,
        "kind": kind,
        "url": urls.canonical_url(kind, code),
        "sources": [],
        "caption": None,
        "owner": None,
        "posted_at": None,
        "metrics": {"likes": None, "comments": None, "views": None, "followers": None},
        "audio": {"status": "unknown", "title": None, "artist": None, "original": None, "audio_url": None},
        "media": [],
        "fetch": None,
        "selected": False,
        "score": None,
    }


def _iter_texts(path: Path):
    """Dosya, klasör veya ZIP içindeki metin dosyalarını (ad, içerik) olarak verir."""
    if path.is_dir():
        for p in sorted(path.rglob("*")):
            if p.is_file():
                yield from _iter_texts(p)
    elif path.suffix.lower() == ".zip":
        with zipfile.ZipFile(path) as zf:
            for name in zf.namelist():
                if Path(name).suffix.lower() in TEXT_EXTS:
                    yield f"{path}!{name}", zf.read(name).decode("utf-8", "replace")
    elif path.suffix.lower() in TEXT_EXTS or path.suffix == "":
        yield str(path), path.read_text(encoding="utf-8", errors="replace")


def import_urls(ws: Workspace, inputs: list[str]) -> dict:
    """Her türlü metin kaynağından gönderi URL'lerini toplar, https'ye çevirir, tekilleştirir."""
    ws.ensure()
    added = seen = 0
    for inp in inputs:
        for origin, text in _iter_texts(Path(inp)):
            # Instagram JSON dışa aktarımı \/ kaçışlı olabilir.
            text = text.replace("\\/", "/")
            for kind, code in urls.find_post_urls(text):
                seen += 1
                post = ws.load_post(code)
                if post is None:
                    post = new_post(kind, code)
                    added += 1
                if origin not in post["sources"]:
                    post["sources"].append(origin)
                ws.save_post(post)
    total = sum(1 for _ in ws.iter_posts())
    return {"found_links": seen, "new_posts": added, "total_posts": total}


# --- Meta veri birleştirme -------------------------------------------------

_OG_RE = re.compile(
    r"^\s*([\d.,]+[KkMmBb]?)\s+likes?,\s*([\d.,]+[KkMmBb]?)\s+comments?\s*-\s*([A-Za-z0-9._]+)\s+on\s+([^:]+):\s*(.*)$",
    re.DOTALL,
)


def parse_count(s) -> int | None:
    """'1,234' / '12.5K' / '3M' / 1234 -> int."""
    if s is None:
        return None
    if isinstance(s, (int, float)):
        return int(s)
    s = str(s).strip().replace(",", "")
    mult = 1
    if s[-1:].lower() in ("k", "m", "b"):
        mult = {"k": 1_000, "m": 1_000_000, "b": 1_000_000_000}[s[-1].lower()]
        s = s[:-1]
    try:
        return int(float(s) * mult)
    except ValueError:
        return None


def parse_og_description(desc: str) -> dict:
    """Instagram og:description: '1,234 likes, 56 comments - kullanici on May 1, 2026: "açıklama"'."""
    m = _OG_RE.match(desc or "")
    if not m:
        return {}
    caption = m.group(5).strip()
    if len(caption) >= 2 and caption[0] in "\"“" and caption[-1] in "\"”.":
        caption = caption.strip(".").strip("\"“”")
    return {
        "likes": parse_count(m.group(1)),
        "comments": parse_count(m.group(2)),
        "owner": m.group(3),
        "posted_text": m.group(4).strip(),
        "caption": caption,
    }


def merge_meta(post: dict, meta: dict, origin: str) -> dict:
    """Farklı araçların alan adlarını ortak kayda eşler. Var olan değeri boşla ezmez."""
    def put(path: list[str], value):
        if value in (None, "", []):
            return
        node = post
        for k in path[:-1]:
            node = node[k]
        node[path[-1]] = value

    og = parse_og_description(meta.get("og_description") or "")
    put(["caption"], meta.get("caption") or meta.get("description") or og.get("caption"))
    put(["owner"], meta.get("owner") or meta.get("uploader_id") or meta.get("uploader") or meta.get("username")
        or og.get("owner"))
    ts = meta.get("timestamp")
    if isinstance(ts, (int, float)):
        put(["posted_at"], dt.datetime.fromtimestamp(ts, dt.timezone.utc).isoformat())
    else:
        put(["posted_at"], meta.get("posted_at") or meta.get("upload_date") or og.get("posted_text"))
    put(["metrics", "likes"], parse_count(meta.get("like_count", meta.get("likes"))) or og.get("likes"))
    put(["metrics", "comments"], parse_count(meta.get("comment_count", meta.get("comments"))) or og.get("comments"))
    put(["metrics", "views"], parse_count(meta.get("view_count", meta.get("views", meta.get("play_count")))))
    put(["metrics", "followers"], parse_count(meta.get("channel_follower_count", meta.get("followers"))))

    audio = meta.get("audio") if isinstance(meta.get("audio"), dict) else {}
    title = audio.get("title") or meta.get("track") or meta.get("audio_title")
    artist = audio.get("artist") or meta.get("artist") or meta.get("audio_artist")
    if title or artist:
        put(["audio", "title"], title)
        put(["audio", "artist"], artist)
        put(["audio", "audio_url"], audio.get("audio_url") or meta.get("audio_url"))
        orig = audio.get("original")
        if orig is None and title:
            orig = "original audio" in str(title).lower() or "orijinal ses" in str(title).lower()
        post["audio"]["original"] = orig
        post["audio"]["status"] = "known"
    elif audio.get("status") == "none" or meta.get("audio_status") == "none":
        # Müzik eklenmemiş fotoğraf: bu da kayıttır, "bilinmiyor" değildir.
        post["audio"]["status"] = "none"

    if origin not in post["sources"]:
        post["sources"].append(origin)
    return post


def import_meta(ws: Workspace, inputs: list[str]) -> dict:
    """yt-dlp .info.json, tarayıcı yakalama (vf-capture-*.json) ve benzeri meta dosyalarını içe alır.
    Yanındaki görsel dosyaları (aynı adlı .jpg/.png/.webp) medya olarak kopyalanır."""
    import shutil

    ws.ensure()
    merged = 0
    for inp in inputs:
        p = Path(inp)
        files = sorted(p.rglob("*.json")) if p.is_dir() else [p]
        for f in files:
            data = json.loads(f.read_text(encoding="utf-8", errors="replace"))
            items = data if isinstance(data, list) else [data]
            for meta in items:
                if not isinstance(meta, dict):
                    continue
                link = (meta.get("url") or meta.get("webpage_url") or meta.get("original_url")
                        or meta.get("post_url") or "")
                norm = urls.normalize(link)
                sc = meta.get("shortcode") or meta.get("post_shortcode")  # gallery-dl: post_shortcode
                if not norm and sc:
                    norm = urls.normalize(f"https://www.instagram.com/p/{sc}/")
                if not norm:
                    continue
                kind, code, _ = norm
                post = ws.load_post(code) or new_post(kind, code)
                merge_meta(post, meta, str(f))
                stem = f.name[: -len(".info.json")] if f.name.endswith(".info.json") else f.stem
                imgs = []
                if Path(stem).suffix.lower() in (".jpg", ".jpeg", ".png", ".webp"):
                    imgs.append(f.parent / stem)  # gallery-dl: foto.jpg + foto.jpg.json
                for ext in (".jpg", ".jpeg", ".png", ".webp"):
                    imgs += sorted(f.parent.glob(f"{stem}*{ext}"))
                for img in dict.fromkeys(imgs):
                    if not img.exists():
                        continue
                    dest = ws.post_dir(code) / "media" / img.name
                    dest.parent.mkdir(parents=True, exist_ok=True)
                    if not dest.exists():
                        shutil.copy2(img, dest)
                    rel = f"media/{img.name}"
                    if not any(m.get("file") == rel for m in post["media"]):
                        post["media"].append({"file": rel, "remote": None})
                ws.save_post(post)
                merged += 1
    return {"merged_records": merged}
