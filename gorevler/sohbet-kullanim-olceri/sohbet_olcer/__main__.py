"""Komut satırı: python3 -m sohbet_olcer <komut> ...

  rapor    Bir oturumun token kullanımını ve hesap kotasını raporlar.
  gunluk   Transkriptten kronolojik işlem günlüğü üretir.
  oturumlar Transkriptlerdeki oturumları ve token toplamlarını listeler.
  kaydet   Statusline komutu olarak çalışır; kota anlık görüntüsünü kaydeder.
  elle     claude.ai Ayarlar → Kullanım ekranından okunan değeri kaydeder.
  kanca    Claude Code Stop kancası: her yanıt sonunda kısa rapor gösterir.
  uzlastir stream-json/SDK çıktısındaki API sonucunu transkriptle karşılaştırır.
"""

import argparse
import json
import os
import sys
from datetime import datetime, timezone

from . import gunluk as gunluk_mod
from . import kota, rapor, transkript
from .zaman import iso_ayristir

VARSAYILAN_TRANSKRIPT = "~/.claude/projects"
VARSAYILAN_KOTA_KAYDI = os.environ.get(
    "SOHBET_OLCER_KOTA_KAYDI", os.path.expanduser("~/.claude/sohbet-olcer/kota.jsonl"))


def _kota_goruntuleri(yollar, uyarilar):
    goruntuler = []
    for yol in yollar:
        if not os.path.exists(yol):
            uyarilar.append(f"Kota kaynağı bulunamadı: {yol}")
            continue
        g, bozuk = kota.dosyadan_oku(yol)
        if bozuk:
            uyarilar.append(f"{yol}: {bozuk} bozuk satır atlandı")
        goruntuler.extend(g)
    return goruntuler


def komut_rapor(a):
    okuma = transkript.oku(a.transkript or [VARSAYILAN_TRANSKRIPT])
    ozetler = transkript.oturumlara_ayir(okuma)
    if not ozetler:
        print("Transkriptlerde token kullanımı içeren istek bulunamadı.", file=sys.stderr)
        return 2
    if a.oturum:
        try:
            oz = transkript.oturum_bul(ozetler, a.oturum)
        except KeyError as e:
            print(f"Hata: {e.args[0]}", file=sys.stderr)
            return 2
    else:
        oz = max(ozetler.values(), key=lambda o: o.son or datetime.min.replace(tzinfo=timezone.utc))
    uyarilar = []
    kaynaklar = list(a.kota or [])
    if not a.kota and os.path.exists(VARSAYILAN_KOTA_KAYDI):
        kaynaklar.append(VARSAYILAN_KOTA_KAYDI)
    goruntuler = _kota_goruntuleri(kaynaklar, uyarilar)
    simdi = iso_ayristir(a.simdi) if a.simdi else datetime.now(timezone.utc)
    veri = rapor.rapor_verisi(okuma, ozetler, oz, goruntuler, simdi, bool(kaynaklar))
    if a.json:
        print(json.dumps(rapor.json_raporu(veri), ensure_ascii=False, indent=2))
    else:
        sys.stdout.write(rapor.metin_raporu(veri, okuma, ozetler, a.tz))
    for u in uyarilar:
        print(f"Uyarı: {u}", file=sys.stderr)
    return 0


def komut_oturumlar(a):
    okuma = transkript.oku(a.transkript or [VARSAYILAN_TRANSKRIPT])
    ozetler = transkript.oturumlara_ayir(okuma)
    for oz in sorted(ozetler.values(), key=lambda o: o.ilk or datetime.min.replace(tzinfo=timezone.utc)):
        print(f"{oz.oturum}  istek={oz.istek_sayisi}  toplam_token={oz.toplam}  "
              f"ilk={oz.ilk.isoformat() if oz.ilk else '?'}  son={oz.son.isoformat() if oz.son else '?'}")
    return 0


def komut_gunluk(a):
    okuma = transkript.oku(a.transkript or [VARSAYILAN_TRANSKRIPT], kayitlari_tut=True)
    okuma.kayitlar.sort(key=lambda dk: dk[1].get("timestamp") or "")
    sys.stdout.write(gunluk_mod.gunluk(okuma, a.oturum, a.tz, a.kullanici_metni))
    return 0


def komut_kaydet(a):
    """Statusline: stdin JSON'u okur, rate_limits varsa kilitli ekler, kısa
    bir durum satırı basar. Hiçbir durumda statusline'ı bozmamak için
    hata vermez."""
    try:
        girdi = json.load(sys.stdin)
    except ValueError:
        print("kota: girdi okunamadı")
        return 0
    rl = girdi.get("rate_limits")
    if isinstance(rl, dict) and rl:
        kota.kilitli_ekle(a.kayit, {"kaynak": "statusline",
                                    "ts": datetime.now(timezone.utc).isoformat(),
                                    "session_id": girdi.get("session_id"),
                                    "transcript_path": girdi.get("transcript_path"),
                                    "rate_limits": rl})
        parcalar = []
        for ad, etiket in (("five_hour", "5s"), ("seven_day", "7g")):
            p = rl.get(ad) or {}
            if p.get("used_percentage") is not None:
                parcalar.append(f"{etiket} %{rapor.yuzde(p['used_percentage'])}")
        print("kota " + " · ".join(parcalar) if parcalar else "kota: erişilemiyor")
    else:
        print("kota: erişilemiyor")
    return 0


def komut_elle(a):
    yenilenme = iso_ayristir(a.yenilenme) if a.yenilenme else None
    if a.yenilenme and yenilenme is None:
        print("Hata: --yenilenme ISO 8601 olmalı (örn. 2026-10-10T09:00+03:00)", file=sys.stderr)
        return 2
    zaman = iso_ayristir(a.zaman) if a.zaman else datetime.now(timezone.utc)
    kota.kilitli_ekle(a.kayit, {"kaynak": "elle", "ts": zaman.isoformat(), "pencere": a.pencere,
                                "yuzde": a.yuzde, "cozunurluk": a.cozunurluk,
                                "yenilenme": yenilenme.isoformat() if yenilenme else None})
    print(f"Kaydedildi: {a.pencere} yüzde {a.yuzde} ({a.kayit})")
    return 0


def komut_kanca(a):
    """Stop kancası: stdin'den session_id ve transcript_path alır, raporu
    dosyaya yazar ve systemMessage olarak kısa özet döndürür."""
    try:
        girdi = json.load(sys.stdin)
    except ValueError:
        return 0
    yol = girdi.get("transcript_path")
    if not yol or not os.path.exists(yol):
        return 0
    okuma = transkript.oku([yol])
    ozetler = transkript.oturumlara_ayir(okuma)
    try:
        oz = transkript.oturum_bul(ozetler, girdi.get("session_id") or "")
    except KeyError:
        return 0
    kaynaklar = [a.kayit] if os.path.exists(a.kayit) else []
    goruntuler = _kota_goruntuleri(kaynaklar, [])
    veri = rapor.rapor_verisi(okuma, ozetler, oz, goruntuler, datetime.now(timezone.utc),
                              bool(kaynaklar))
    metin = rapor.metin_raporu(veri, okuma, ozetler, a.tz)
    hedef = os.path.join(a.cikti, f"{oz.oturum}.txt")
    os.makedirs(a.cikti, exist_ok=True)
    with open(hedef, "w", encoding="utf-8") as f:
        f.write(metin)
    ozet = f"Bu sohbet: {rapor.sayi(oz.toplam)} token ({oz.istek_sayisi} istek)."
    for ad, etiket in (("five_hour", "5 saatlik"), ("seven_day", "haftalık")):
        d = veri["pencereler"].get(ad)
        if d and d["goruntu"].kullanilan is not None:
            ozet += f" {etiket.capitalize()} kota (hesap geneli): %{rapor.yuzde(d['goruntu'].kalan)} kaldı."
        else:
            ozet += f" {etiket.capitalize()} kota: erişilemiyor."
    ozet += f" Ayrıntı: {hedef}"
    print(json.dumps({"systemMessage": ozet}, ensure_ascii=False))
    return 0


ALANLAR4 = ("input_tokens", "output_tokens", "cache_creation_input_tokens", "cache_read_input_tokens")


def komut_uzlastir(a):
    """Her stream-json dosyası için: asistan mesaj kimliklerini transkriptteki
    isteklerle eşler, transkript toplamını API'nin result.usage toplamıyla
    karşılaştırır. Eşleşme mesaj kimliğiyle yapıldığından oturum kimliği
    çakışsa bile doğru konuşma bulunur."""
    okuma = transkript.oku(a.transkript or [VARSAYILAN_TRANSKRIPT])
    hata = 0
    for yol in a.akis:
        kimlikler, sonuc = [], None
        with open(yol, encoding="utf-8") as f:
            for satir in f:
                try:
                    d = json.loads(satir)
                except ValueError:
                    continue
                if d.get("type") == "assistant":
                    mid = (d.get("message") or {}).get("id")
                    if mid and mid not in kimlikler:
                        kimlikler.append(mid)
                elif d.get("type") == "result" and isinstance(d.get("usage"), dict):
                    sonuc = d["usage"]
        if sonuc is None:
            print(f"{yol}: result.usage yok, uzlaştırılamadı")
            hata += 1
            continue
        api = {k: int(sonuc.get(k, 0)) for k in ALANLAR4}
        bulunan = [okuma.istekler[m] for m in kimlikler if m in okuma.istekler]
        tr = {k: sum(i.sayac.get(k, 0) for i in bulunan) for k in ALANLAR4}
        konusmalar = sorted({i.oturum for i in bulunan})
        esit = api == tr and len(bulunan) == len(kimlikler)
        hata += 0 if esit else 1
        print(f"{yol}: {'EŞLEŞTİ' if esit else 'UYUŞMADI'}; API toplamı {sum(api.values())}, "
              f"transkript toplamı {sum(tr.values())}; {len(bulunan)}/{len(kimlikler)} mesaj bulundu; "
              f"konuşma: {', '.join(konusmalar) or '-'}")
        if not esit:
            for k in ALANLAR4:
                if api[k] != tr[k]:
                    print(f"   {k}: API {api[k]} / transkript {tr[k]}")
    return 1 if hata else 0


def ayristirici():
    p = argparse.ArgumentParser(prog="sohbet_olcer", description=__doc__,
                                formatter_class=argparse.RawDescriptionHelpFormatter)
    p.add_argument("--tz", default="Europe/Istanbul", help="saat dilimi (IANA)")
    alt = p.add_subparsers(dest="komut", required=True)

    r = alt.add_parser("rapor", help="oturum kullanım raporu")
    r.add_argument("--transkript", action="append", help=f"dosya ya da dizin (varsayılan {VARSAYILAN_TRANSKRIPT})")
    r.add_argument("--oturum", help="oturum kimliği ya da öneki (varsayılan: en son etkin oturum)")
    r.add_argument("--kota", action="append", help="kota kaynağı: statusline kaydı, rate_limit_event dökümü, elle kaydı")
    r.add_argument("--json", action="store_true", help="makine okunur çıktı")
    r.add_argument("--simdi", help="test için 'şimdi' zamanı (ISO 8601)")
    r.set_defaults(f=komut_rapor)

    o = alt.add_parser("oturumlar", help="oturumları listele")
    o.add_argument("--transkript", action="append")
    o.set_defaults(f=komut_oturumlar)

    g = alt.add_parser("gunluk", help="kronolojik işlem günlüğü")
    g.add_argument("--transkript", action="append")
    g.add_argument("--oturum")
    g.add_argument("--kullanici-metni", action="store_true", help="kullanıcı mesaj metnini de yaz")
    g.set_defaults(f=komut_gunluk)

    k = alt.add_parser("kaydet", help="statusline komutu olarak kota kaydı")
    k.add_argument("--kayit", default=VARSAYILAN_KOTA_KAYDI)
    k.set_defaults(f=komut_kaydet)

    e = alt.add_parser("elle", help="Ayarlar → Kullanım ekranındaki değeri kaydet")
    e.add_argument("--pencere", required=True, choices=["five_hour", "seven_day", "seven_day_opus", "seven_day_sonnet"])
    e.add_argument("--yuzde", type=float, required=True, help="ekranda görünen kullanılan yüzde")
    e.add_argument("--yenilenme", help="ekranda görünen yenilenme zamanı, ISO 8601")
    e.add_argument("--zaman", help="okuma zamanı (varsayılan şimdi)")
    e.add_argument("--cozunurluk", type=float, default=1.0, help="ekran yuvarlaması, puan")
    e.add_argument("--kayit", default=VARSAYILAN_KOTA_KAYDI)
    e.set_defaults(f=komut_elle)

    u = alt.add_parser("uzlastir", help="API sonucu ile transkripti karşılaştır")
    u.add_argument("akis", nargs="+", help="claude -p --output-format stream-json --verbose çıktısı")
    u.add_argument("--transkript", action="append")
    u.set_defaults(f=komut_uzlastir)

    h = alt.add_parser("kanca", help="Claude Code Stop kancası")
    h.add_argument("--kayit", default=VARSAYILAN_KOTA_KAYDI)
    h.add_argument("--cikti", default=os.path.expanduser("~/.claude/sohbet-olcer/raporlar"))
    h.set_defaults(f=komut_kanca)
    return p


def main(argv=None):
    a = ayristirici().parse_args(argv)
    return a.f(a)


if __name__ == "__main__":
    sys.exit(main())
