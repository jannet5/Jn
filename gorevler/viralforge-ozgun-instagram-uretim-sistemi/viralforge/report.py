"""HTML rapor (kaynak + neden tuttu + 4 varyasyon) ve ChatGPT Pro web yolu için istem sayfası / içe alma."""
from __future__ import annotations

import html
import os
import re
import shutil
from pathlib import Path

from .brain import validate_analysis
from .prompts import image_prompt
from .store import VARIANT_IDS, Workspace, read_json

CSS = """:root{--bg:#fafafa;--fg:#111;--card:#fff;--mut:#666;--bd:#ddd}
@media (prefers-color-scheme:dark){:root{--bg:#121212;--fg:#eee;--card:#1d1d1d;--mut:#aaa;--bd:#333}}
body{font-family:system-ui,sans-serif;background:var(--bg);color:var(--fg);margin:0;padding:16px}
.post{background:var(--card);border:1px solid var(--bd);border-radius:10px;padding:14px;margin:0 0 18px}
.grid{display:grid;grid-template-columns:repeat(auto-fill,minmax(160px,1fr));gap:8px}
.grid img{width:100%;border-radius:6px;aspect-ratio:1;object-fit:cover;background:#8882}
small,.mut{color:var(--mut)} a{color:inherit} pre{white-space:pre-wrap;font-size:12px}
button{cursor:pointer;padding:4px 10px;margin:4px 0}"""


def _rel(path: Path, base: Path) -> str:
    return html.escape(os.path.relpath(path, base).replace(os.sep, "/"))


def build_report(ws: Workspace, out: Path | None = None) -> Path:
    out = out or ws.root / "rapor.html"
    parts = [f"<!doctype html><meta charset=utf-8><meta name=viewport content='width=device-width,initial-scale=1'>"
             f"<title>ViralForge Rapor</title><style>{CSS}</style><h1>ViralForge rapor</h1>"]
    for post in ws.iter_posts(selected_only=True):
        code = post["shortcode"]
        data = read_json(ws.analysis_path(code)) or {}
        ver = read_json(ws.verify_path(code)) or {}
        src = "".join(f"<img src='{_rel(p, out.parent)}' alt='kaynak'>" for p in ws.media_files(code)[:2])
        var = ""
        for v in data.get("variants", []):
            p = ws.variant_path(code, v["id"])
            ok = ver.get("variants", {}).get(v["id"], {}).get("ok")
            img = f"<img src='{_rel(p, out.parent)}' alt='{v['id']}'>" if p.exists() else "<div class=mut>üretilmedi</div>"
            var += (f"<div>{img}<b>{v['id']}</b> {'✅' if ok else '⏳'}<br><small>{html.escape(v['angle'])}</small></div>")
        wiw = data.get("why_it_worked", {})
        audio = post["audio"]
        parts.append(
            f"<div class=post><h3>#{post.get('rank', '')} <a href='{html.escape(post['url'])}'>{html.escape(post['url'])}</a></h3>"
            f"<p class=mut>👍 {post['metrics'].get('likes')} 💬 {post['metrics'].get('comments')} ▶ {post['metrics'].get('views')} · "
            f"Ses: {html.escape(str(audio.get('title') or audio['status']))} {html.escape(str(audio.get('artist') or ''))}</p>"
            f"<p><b>Açıklama:</b> {html.escape((post.get('caption') or '')[:300])}</p>"
            f"<p><b>Neden tuttu:</b> {html.escape(wiw.get('combined', 'analiz yok'))}</p>"
            f"<div class=grid>{src}{var}</div></div>")
    out.write_text("".join(parts), encoding="utf-8")
    return out


def build_chatgpt_sheet(ws: Workspace, out: Path | None = None) -> tuple[Path, int]:
    """ChatGPT Pro web arayüzünde elle/yarı otomatik üretim için kopyala-yapıştır sayfası.
    İndirilen görseller `<kısa_kod>_<v1..v4>.png` adıyla kaydedilip `ingest-images` ile alınır."""
    out = out or ws.root / "chatgpt-pro-istemleri.html"
    js = ("<script>function cp(id){navigator.clipboard.writeText(document.getElementById(id).innerText)}</script>")
    parts = [f"<!doctype html><meta charset=utf-8><meta name=viewport content='width=device-width,initial-scale=1'>"
             f"<title>ChatGPT Pro İstemleri</title><style>{CSS}</style>{js}<h1>ChatGPT Pro görsel istemleri</h1>"
             "<p>Her istemi ChatGPT'ye yapıştırın, görseli <code>KISAKOD_vN.png</code> adıyla tek klasöre indirin, "
             "sonra <code>python -m viralforge ingest-images KLASÖR</code>.</p>"]
    n = 0
    for post in ws.iter_posts(selected_only=True):
        code = post["shortcode"]
        data = read_json(ws.analysis_path(code))
        if not data or validate_analysis(data, code):
            continue
        for v in data["variants"]:
            if ws.variant_path(code, v["id"]).exists():
                continue
            pid = f"{code}_{v['id']}"
            text = image_prompt(v, None).replace("$imagegen ", "")
            parts.append(f"<div class=post><b>{pid}.png</b> <button onclick=\"cp('{pid}')\">Kopyala</button>"
                         f"<pre id='{pid}'>{html.escape(text)}</pre></div>")
            n += 1
    out.write_text("".join(parts), encoding="utf-8")
    return out, n


_NAME_RE = re.compile(r"^(?P<code>[A-Za-z0-9_-]{5,64})_(?P<vid>v[1-4])\.(png|jpg|jpeg|webp)$", re.IGNORECASE)


def ingest_images(ws: Workspace, folder: str) -> dict:
    from PIL import Image

    taken = unknown = 0
    for p in sorted(Path(folder).iterdir()):
        m = _NAME_RE.match(p.name)
        if not m or not ws.post_path(m.group("code")).exists():
            unknown += 1
            continue
        dest = ws.variant_path(m.group("code"), m.group("vid").lower())
        dest.parent.mkdir(parents=True, exist_ok=True)
        if p.suffix.lower() == ".png":
            shutil.copy2(p, dest)
        else:
            with Image.open(p) as im:
                im.save(dest, "PNG")
        taken += 1
    return {"ingested": taken, "unmatched": unknown}
