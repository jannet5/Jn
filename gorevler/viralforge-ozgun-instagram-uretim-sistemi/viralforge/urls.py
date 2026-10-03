"""Instagram gönderi bağlantılarını bulma ve tek biçime (https) getirme."""
from __future__ import annotations

import re

# /p/, /reel/, /reels/, /tv/ ve kullanıcı adlı biçimleri (instagram.com/<user>/p/<kod>/) yakalar.
_URL_RE = re.compile(
    r"(?:https?:)?(?://)?(?:www\.|m\.)?instagram\.com/(?:[A-Za-z0-9._]+/)?(p|reel|reels|tv)/([A-Za-z0-9_-]{5,64})",
    re.IGNORECASE,
)

KIND_MAP = {"p": "p", "reel": "reel", "reels": "reel", "tv": "reel"}


def find_post_urls(text: str) -> list[tuple[str, str]]:
    """Metindeki tüm gönderi bağlantılarını (tür, kısa kod) olarak sırayla döndürür."""
    out = []
    for m in _URL_RE.finditer(text or ""):
        out.append((KIND_MAP[m.group(1).lower()], m.group(2)))
    return out


def canonical_url(kind: str, shortcode: str) -> str:
    return f"https://www.instagram.com/{kind}/{shortcode}/"


def normalize(url: str) -> tuple[str, str, str] | None:
    """Tek bir bağlantıyı (tür, kısa kod, https kanonik URL) olarak döndürür; gönderi değilse None."""
    found = find_post_urls(url)
    if not found:
        return None
    kind, code = found[0]
    return kind, code, canonical_url(kind, code)
