"""Bağlantı testi: kontrol bu internet bağlantısında gerçekten çalışıyor mu?

Bilinen bir hesap (instagram) 'dolu', uydurma bir isim 'boş' çıkmalı. Değilse kullanıcıya
sade bir dille nedenini söyler. Engel aşmaya çalışmaz.
"""
import random
import string

from .checkers import FREE, LIMITED, TAKEN, Checker


def run_selftest(checker: Checker):
    """(ok, mesaj) döner."""
    junk = "zq" + "".join(random.choice(string.ascii_lowercase) for _ in range(12))
    known = checker.check("instagram")
    if known.status == LIMITED:
        return False, ("Instagram bu internet bağlantısını yavaşlattı (429). Sunucu, VPN, ofis veya "
                       "yurt ağlarında olur. Evdeki interneti ya da telefon hotspot'unu dene, VPN'i kapat.")
    if known.status != TAKEN:
        return False, f"Test başarısız: bilinen hesap 'dolu' görünmedi ({known.status}: {known.detail})."
    new = checker.check(junk)
    if new.status == LIMITED:
        return False, "Instagram ikinci sorguda yavaşla dedi (429). Biraz bekleyip tekrar dene."
    if new.status != FREE:
        return False, f"Test başarısız: uydurma isim 'boş' görünmedi ({new.status}: {new.detail})."
    return True, "Bağlantı çalışıyor: dolu hesap 'dolu', olmayan isim 'boş' çıktı."
