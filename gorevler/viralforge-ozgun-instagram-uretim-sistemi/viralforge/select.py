"""'Tutmuş' gönderi seçimi: etkileşim puanı + kayıt eksiksizliği."""
from __future__ import annotations

import math

from .store import Workspace


def completeness(ws: Workspace, post: dict) -> dict:
    """Kaynak satırındaki dört zorunlu alan: https URL, fotoğraf, ses kaydı, açıklama."""
    return {
        "url_https": post["url"].startswith("https://"),
        "photo": bool(ws.media_files(post["shortcode"])),
        "audio": post["audio"]["status"] in ("known", "none"),
        "caption": bool(post.get("caption")),
    }


def score(post: dict) -> float | None:
    """Takipçi sayısı varsa etkileşim oranı, yoksa log ölçekli mutlak etkileşim.
    Yorum ve kaydetme benzeri derin etkileşim beğeniden ağır sayılır."""
    m = post["metrics"]
    likes, comments, views = m.get("likes"), m.get("comments"), m.get("views")
    if likes is None and comments is None and views is None:
        return None
    raw = (likes or 0) + 3 * (comments or 0) + 0.05 * (views or 0)
    if m.get("followers"):
        return round(1000 * raw / max(m["followers"], 1), 4)
    return round(math.log10(raw + 1) * 100, 4)


def select_top(ws: Workspace, top: int = 1000, min_likes: int = 0, require_complete: bool = True) -> dict:
    ranked = []
    skipped_incomplete = skipped_metric = 0
    for post in ws.iter_posts():
        post["score"] = score(post)
        ok = all(completeness(ws, post).values())
        if post["score"] is None or (post["metrics"].get("likes") or 0) < min_likes:
            skipped_metric += 1
            post["selected"] = False
        elif require_complete and not ok:
            skipped_incomplete += 1
            post["selected"] = False
        else:
            ranked.append(post)
        ws.save_post(post)
    ranked.sort(key=lambda p: p["score"], reverse=True)
    for i, post in enumerate(ranked):
        post["selected"] = i < top
        post["rank"] = i + 1
        ws.save_post(post)
    return {"selected": min(top, len(ranked)), "eligible": len(ranked),
            "skipped_incomplete": skipped_incomplete, "skipped_no_metrics_or_low": skipped_metric}
