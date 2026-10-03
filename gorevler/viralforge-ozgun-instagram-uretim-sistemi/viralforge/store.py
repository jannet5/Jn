"""Dosya sistemi tabanlı çalışma alanı.

Her gönderi kendi klasöründe durur; Codex (beyin) dosyaları doğrudan okuyabilsin diye
veritabanı yerine düz JSON kullanılır:

    <ws>/posts/<kısa_kod>/post.json        kaynak kaydı (URL, medya, açıklama, ses, metrikler)
    <ws>/posts/<kısa_kod>/media/           kaynak fotoğraf / kapak / karusel kareleri
    <ws>/posts/<kısa_kod>/analysis.json    Codex'in "neden tuttu" analizi + 4 varyasyon briefi
    <ws>/posts/<kısa_kod>/variants/v1..v4.png  üretilen özgün görseller
    <ws>/posts/<kısa_kod>/verify.json      doğrulama sonucu
"""
from __future__ import annotations

import json
import os
import tempfile
from pathlib import Path
from typing import Iterator

VARIANT_IDS = ("v1", "v2", "v3", "v4")
TARGET_POSTS = 1000


def read_json(path: Path, default=None):
    try:
        return json.loads(Path(path).read_text(encoding="utf-8"))
    except (FileNotFoundError, json.JSONDecodeError):
        return default


def write_json(path: Path, data) -> None:
    """Yarım yazılmış dosya kalmasın diye atomik yazar (kesintide checkpoint bozulmaz)."""
    path = Path(path)
    path.parent.mkdir(parents=True, exist_ok=True)
    fd, tmp = tempfile.mkstemp(dir=path.parent, prefix=".tmp-", suffix=".json")
    with os.fdopen(fd, "w", encoding="utf-8") as fh:
        json.dump(data, fh, ensure_ascii=False, indent=2)
    os.replace(tmp, path)


class Workspace:
    def __init__(self, root: str | Path):
        self.root = Path(root).resolve()
        self.posts_dir = self.root / "posts"

    def ensure(self) -> "Workspace":
        self.posts_dir.mkdir(parents=True, exist_ok=True)
        return self

    def post_dir(self, shortcode: str) -> Path:
        return self.posts_dir / shortcode

    def post_path(self, shortcode: str) -> Path:
        return self.post_dir(shortcode) / "post.json"

    def load_post(self, shortcode: str) -> dict | None:
        return read_json(self.post_path(shortcode))

    def save_post(self, post: dict) -> None:
        write_json(self.post_path(post["shortcode"]), post)

    def iter_posts(self, selected_only: bool = False) -> Iterator[dict]:
        if not self.posts_dir.exists():
            return
        for d in sorted(self.posts_dir.iterdir()):
            post = read_json(d / "post.json")
            if not post:
                continue
            if selected_only and not post.get("selected"):
                continue
            yield post

    def media_files(self, shortcode: str) -> list[Path]:
        mdir = self.post_dir(shortcode) / "media"
        if not mdir.exists():
            return []
        exts = {".jpg", ".jpeg", ".png", ".webp"}
        return sorted(p for p in mdir.iterdir() if p.suffix.lower() in exts and p.stat().st_size > 0)

    def analysis_path(self, shortcode: str) -> Path:
        return self.post_dir(shortcode) / "analysis.json"

    def variant_path(self, shortcode: str, vid: str) -> Path:
        return self.post_dir(shortcode) / "variants" / f"{vid}.png"

    def verify_path(self, shortcode: str) -> Path:
        return self.post_dir(shortcode) / "verify.json"
