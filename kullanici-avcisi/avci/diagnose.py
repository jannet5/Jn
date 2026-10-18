"""Tanı raporu: araç çalışmazsa "ne oldu?" sorusunun cevabı tek bir metinde.

Rapor şifre, oturum ya da kişisel veri içermez. Sorunu çözecek kişiye (ya da yapay zekâya) olduğu gibi gönderilir.
"""
import os
import platform
import random
import string
import sys
import time

import requests

from . import __version__
from .checkers import Checker, InstagramProfileChecker, Result
from .selftest import run_selftest


class _Replay(Checker):
    """Ham cevaplardan alınan kararları tekrar oynatır (aynı istekleri ikinci kez atmamak için)."""
    name = "replay"

    def __init__(self, known: Result, junk: Result):
        self.known, self.junk = known, junk

    def check(self, username: str) -> Result:
        return self.known if username == "instagram" else self.junk


def probe_instagram(checker: InstagramProfileChecker):
    """2 istek: bilinen hesap + uydurma isim. (ham cevaplar, bağlantı testi sonucu) döner."""
    junk = "zq" + "".join(random.choice(string.ascii_lowercase) for _ in range(12))
    raws = [("instagram", checker.raw("instagram")), (junk, checker.raw(junk))]
    results = [Result(r.get("decision", "error"), r.get("decision_detail", r.get("error", ""))) for _, r in raws]
    ok, msg = run_selftest(_Replay(results[0], results[1]))
    return raws, (ok, msg)


def _version(mod: str) -> str:
    try:
        return __import__(mod).__version__
    except Exception:
        return "YOK"


def tail(path: str, n: int = 40) -> str:
    try:
        with open(path, encoding="utf-8", errors="replace") as f:
            return "".join(f.readlines()[-n:]).rstrip() or "(boş)"
    except OSError:
        return "(kayıt dosyası yok)"


def build_report(store=None, log_path: str = None, selftest=None, ui_logs=None, raws=None, demo: bool = False) -> str:
    L = []
    add = L.append
    add("=== AVCI TANI RAPORU ===")
    add(f"Zaman: {time.strftime('%Y-%m-%d %H:%M:%S')}   Araç sürümü: {__version__}   Demo modu: {'EVET' if demo else 'hayır'}")
    add("")
    add("[Ortam]")
    add(f"Python: {sys.version.split()[0]}   Sistem: {platform.platform()}")
    add(f"requests: {_version('requests')}   unidecode: {_version('unidecode')}")
    add(f"Çalışma klasörü: {os.getcwd()}")
    add("")
    add("[Havuz]")
    if store is not None:
        add(", ".join(f"{k}={v}" for k, v in store.stats().items()))
    else:
        add("(veritabanı açılamadı)")
    add("")
    add("[Bağlantı testi: bilinen hesap 'dolu', uydurma isim 'boş' çıkmalı]")
    if selftest is not None:
        add(("BAŞARILI: " if selftest[0] else "BAŞARISIZ: ") + selftest[1])
    else:
        add("(yapılmadı)")
    if raws:
        add("")
        add("[Instagram'ın ham cevapları: yöntem değiştiyse sorunun kaynağı burada görünür]")
        for name, r in raws:
            add(f"- sorgu: {name}")
            for k, v in r.items():
                add(f"    {k}: {v}")
    add("")
    add("[Ekrandaki son olaylar]")
    add("\n".join(ui_logs) if ui_logs else "(yok)")
    if log_path:
        add("")
        add(f"[Kayıt dosyası son satırlar: {log_path}]")
        add(tail(log_path))
    add("")
    add("Bu raporda şifre, oturum ya da kişisel bilgi yoktur.")
    return "\n".join(L)
