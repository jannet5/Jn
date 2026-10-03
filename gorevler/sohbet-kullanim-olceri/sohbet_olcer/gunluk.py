"""Transkriptten kronolojik işlem günlüğü: araç, komut, dosya, URL, kısa
gerekçe (aracı çağırmadan önceki asistan metni) ve sonuç. Maddeler arasında
boş satır bulunur. Kullanıcı mesajlarının metni varsayılan olarak yazılmaz."""

from .zaman import iso_ayristir, saat, tarih_saat


def _kisalt(metin, n=220):
    metin = " ".join(str(metin).split())
    return metin if len(metin) <= n else metin[: n - 1] + "…"


def _arac_ozeti(ad, girdi):
    g = girdi if isinstance(girdi, dict) else {}
    if ad == "Bash":
        s = f"komut: {_kisalt(g.get('command', ''), 300)}"
        if g.get("description"):
            s += f" (açıklama: {_kisalt(g['description'], 120)})"
        return s
    for anahtar, etiket in (("file_path", "dosya"), ("notebook_path", "dosya"),
                            ("url", "URL"), ("query", "sorgu"), ("pattern", "desen")):
        if g.get(anahtar):
            s = f"{etiket}: {_kisalt(g[anahtar], 200)}"
            if anahtar == "pattern" and g.get("path"):
                s += f" (yol: {g['path']})"
            return s
    if g.get("description"):
        return f"açıklama: {_kisalt(g['description'], 160)}"
    anahtarlar = ", ".join(sorted(g)[:6])
    return f"parametreler: {anahtarlar}" if anahtarlar else "parametresiz"


def _sonuc_ozeti(icerik):
    if isinstance(icerik, list):
        parcalar = [c.get("text", "") for c in icerik if isinstance(c, dict) and c.get("type") == "text"]
        icerik = " ".join(parcalar) if parcalar else f"{len(icerik)} parça içerik"
    return _kisalt(icerik or "(boş çıktı)", 200)


def gunluk(okuma, oturum=None, tz="Europe/Istanbul", kullanici_metni=False):
    olaylar = []
    son_gerekce = None
    bekleyen = {}
    for dosya, k in okuma.kayitlar:
        if oturum and k.get("sessionId") and not k["sessionId"].startswith(oturum):
            continue
        zaman = iso_ayristir(k.get("timestamp"))
        mesaj = k.get("message") or {}
        icerik = mesaj.get("content")
        if k.get("type") == "assistant" and isinstance(icerik, list):
            for blok in icerik:
                if not isinstance(blok, dict):
                    continue
                if blok.get("type") == "text" and blok.get("text", "").strip():
                    son_gerekce = _kisalt(blok["text"], 240)
                elif blok.get("type") == "tool_use":
                    olay = {"zaman": zaman, "tur": "arac", "ad": blok.get("name", "?"),
                            "ozet": _arac_ozeti(blok.get("name"), blok.get("input")),
                            "gerekce": son_gerekce, "sonuc": None,
                            "yan": bool(k.get("isSidechain"))}
                    son_gerekce = None
                    bekleyen[blok.get("id")] = olay
                    olaylar.append(olay)
        elif k.get("type") == "user":
            if isinstance(icerik, list):
                for blok in icerik:
                    if isinstance(blok, dict) and blok.get("type") == "tool_result":
                        olay = bekleyen.pop(blok.get("tool_use_id"), None)
                        if olay is not None:
                            olay["sonuc"] = ("HATA: " if blok.get("is_error") else "") + \
                                _sonuc_ozeti(blok.get("content"))
            elif isinstance(icerik, str) and not k.get("isMeta"):
                olaylar.append({"zaman": zaman, "tur": "kullanici",
                                "metin": _kisalt(icerik, 400) if kullanici_metni
                                else f"({len(icerik)} karakter; metin gizli)"})
    satirlar = []
    for o in olaylar:
        z = saat(o["zaman"], tz) if o["zaman"] else "--:--:--"
        if o["tur"] == "kullanici":
            satirlar.append(f"{z} — Kullanıcı mesajı {o['metin']}")
            continue
        s = f"{z} — {o['ad']}{' (alt ajan)' if o['yan'] else ''}: {o['ozet']}"
        if o["gerekce"]:
            s += f"\n   Gerekçe: {o['gerekce']}"
        s += f"\n   Sonuç: {o['sonuc'] if o['sonuc'] is not None else 'sonuç kaydı yok (yarıda kalmış olabilir)'}"
        satirlar.append(s)
    if not satirlar:
        return "Günlükte gösterilecek işlem yok.\n"
    bas = next((o["zaman"] for o in olaylar if o["zaman"]), None)
    baslik = f"İşlem günlüğü ({tarih_saat(bas, tz)} başlangıç, {tz})" if bas else "İşlem günlüğü"
    return baslik + "\n\n" + "\n\n".join(satirlar) + "\n"
