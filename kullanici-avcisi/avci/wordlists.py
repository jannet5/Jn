"""Dil listeleri: hazır tohum listeler + internetten 50.000 kelimelik sıklık listeleri."""
import glob
import os

import requests

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
WORDLISTS = os.path.join(ROOT, "wordlists")
FREQ_URL = "https://raw.githubusercontent.com/hermitdave/FrequencyWords/master/content/2018/{l}/{l}_50k.txt"

LANG_NAMES = {
    "ar": "Arapça", "tr": "Türkçe", "fa": "Farsça", "ur": "Urduca", "he": "İbranice",
    "ru": "Rusça", "es": "İspanyolca", "fr": "Fransızca", "de": "Almanca", "it": "İtalyanca",
    "pt": "Portekizce", "en": "İngilizce", "hi": "Hintçe", "id": "Endonezce", "el": "Yunanca",
    "ja": "Japonca", "ko": "Korece", "zh_cn": "Çince",
}


def local_files(lang: str):
    return sorted(glob.glob(os.path.join(WORDLISTS, f"{lang}_*.txt")))


def has_big_list(lang: str) -> bool:
    return os.path.exists(os.path.join(WORDLISTS, f"{lang}_50k.txt"))


def fetch_lang(lang: str, top: int = 50000):
    """(ok, mesaj) döner. Listeyi wordlists/<dil>_50k.txt olarak kaydeder."""
    if not lang.replace("_", "").isalnum():
        return False, f"Geçersiz dil kodu: {lang}"
    os.makedirs(WORDLISTS, exist_ok=True)
    try:
        r = requests.get(FREQ_URL.format(l=lang), timeout=60)
    except requests.RequestException as e:
        return False, f"{lang}: internete ulaşılamadı ({type(e).__name__})"
    if r.status_code != 200:
        return False, f"{lang}: liste bulunamadı (HTTP {r.status_code})"
    lines = r.text.splitlines()[:top]
    out = os.path.join(WORDLISTS, f"{lang}_50k.txt")
    with open(out, "w", encoding="utf-8") as f:
        f.write("\n".join(lines) + "\n")
    return True, f"{lang}: {len(lines)} kelime indirildi"
