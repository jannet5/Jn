#!/usr/bin/env python3
"""Android kendiliğinden yeniden başlama teşhis aracı (salt-okunur).

İki iş yapar:
  topla  : USB'ye bağlı telefondan adb ile salt-okunur kayıtları bir klasöre yazar.
  analiz : O klasörü okuyup rapor.md üretir (yeniden başlama nedeni sınıfı,
           olay zaman çizelgesi, yeni yüklenen/güncellenen uygulamalarla
           karşılaştırma, pil/ısı durumu, önerilen sonraki güvenli test).

Kullanım:
  python reboot_teshis.py topla  [--cikti KLASOR] [--seri SERI]
  python reboot_teshis.py analiz KLASOR
  python reboot_teshis.py hepsi  [--cikti KLASOR] [--seri SERI]

Telefonda hiçbir ayar değiştirmez, uygulama kaldırmaz, yeniden başlatmaz.
Yalnız Python 3.8+ standart kütüphanesi ve PATH'te `adb` gerekir.
"""
from __future__ import annotations

import argparse
import datetime as dt
import os
import re
import shutil
import subprocess
import sys
from pathlib import Path

# dumpsys dropbox içinde yeniden başlama / çökme ile ilgili etiketler.
DROPBOX_ETIKETLER = [
    "SYSTEM_BOOT",
    "SYSTEM_RESTART",
    "SYSTEM_LAST_KMSG",
    "SYSTEM_RECOVERY_LOG",
    "SYSTEM_TOMBSTONE",
    "system_server_crash",
    "system_server_native_crash",
    "system_server_watchdog",
    "system_server_anr",
    "system_server_wtf",
    "system_app_crash",
    "system_app_native_crash",
    "data_app_crash",
    "data_app_native_crash",
    "data_app_anr",
    "SYSTEM_FSCK",
    "SYSTEM_AUDIT",
    "storage_trim",
]

# Çıktı dosyası -> adb shell komutu (hepsi salt-okunur).
KOMUTLAR = {
    "getprop.txt": "getprop",
    "uptime.txt": "cat /proc/uptime; date +%s; date",
    "dropbox_liste.txt": "dumpsys dropbox",
    "battery.txt": "dumpsys battery",
    "thermal.txt": "dumpsys thermalservice",
    "df.txt": "df /data",
    "paketler_3.txt": "pm list packages -3 -i",
    "logcat_crash.txt": "logcat -b crash -d -v threadtime -v year",
    "logcat_events_boot.txt": "logcat -b events -d -v threadtime -v year -e 'boot_progress|am_crash|am_anr|power_|battery_|sysui'",
    "logcat_onceki_acilis.txt": "logcat -L -b all -d -v threadtime -v year",
    "logcat_tum.txt": "logcat -b all -d -v threadtime -v year",
}


# ----------------------------------------------------------------- toplama ---
def _adb(args: list[str], seri: str | None, zaman_asimi: int = 120) -> str:
    komut = ["adb"] + (["-s", seri] if seri else []) + args
    try:
        p = subprocess.run(komut, capture_output=True, text=True,
                           encoding="utf-8", errors="replace", timeout=zaman_asimi)
    except subprocess.TimeoutExpired:
        return "[ZAMAN AŞIMI]\n"
    cikti = p.stdout
    if p.returncode != 0 and p.stderr.strip():
        cikti += "\n[STDERR] " + p.stderr.strip() + "\n"
    return cikti


def topla(klasor: Path, seri: str | None) -> Path:
    if shutil.which("adb") is None:
        sys.exit("HATA: 'adb' bulunamadı. Android SDK Platform-Tools kurup PATH'e ekleyin:\n"
                 "https://developer.android.com/tools/releases/platform-tools")
    cihazlar = _adb(["devices"], None)
    satirlar = [s for s in cihazlar.splitlines()[1:] if s.strip()]
    hazir = [s for s in satirlar if s.split()[-1] == "device"]
    if not hazir:
        sys.exit("HATA: Hazır cihaz yok. 'adb devices' çıktısı:\n" + cihazlar +
                 "\nUSB hata ayıklamayı açıp bilgisayara izin verin ('unauthorized' ise telefonda onaylayın).")
    if len(hazir) > 1 and not seri:
        sys.exit("HATA: Birden fazla cihaz var, --seri ile seçin:\n" + "\n".join(hazir))

    klasor.mkdir(parents=True, exist_ok=True)
    for dosya, kmt in KOMUTLAR.items():
        print(f"  toplanıyor: {dosya}")
        (klasor / dosya).write_text(_adb(["shell", kmt], seri), encoding="utf-8")

    # Reboot/çökme etiketlerinin içerikleri (en fazla son birkaç kayıt).
    db = []
    for etiket in DROPBOX_ETIKETLER:
        icerik = _adb(["shell", f"dumpsys dropbox --print {etiket}"], seri)
        db.append(f"##### ETIKET {etiket}\n{icerik[-200000:]}\n")
    (klasor / "dropbox_icerik.txt").write_text("".join(db), encoding="utf-8")

    # Üçüncü taraf paketlerin kurulum/güncelleme zamanları.
    paket_satirlari = []
    for satir in (klasor / "paketler_3.txt").read_text(encoding="utf-8").splitlines():
        m = re.match(r"package:(\S+)(?:\s+installer=(\S+))?", satir.strip())
        if not m:
            continue
        ad, kurucu = m.group(1), m.group(2) or "null"
        bilgi = _adb(["shell", f"dumpsys package {ad} | grep -E 'versionName|firstInstallTime|lastUpdateTime' "], seri)
        paket_satirlari.append(f"##### PAKET {ad} installer={kurucu}\n{bilgi}\n")
    (klasor / "paket_zamanlari.txt").write_text("".join(paket_satirlari), encoding="utf-8")
    print(f"Toplama bitti: {klasor}")
    return klasor


# ----------------------------------------------------------------- analiz ---
SINIF_ACIKLAMA = {
    "kullanici": "Kullanıcı/komut/OTA kaynaklı planlı yeniden başlatma",
    "yazilim": "Yazılım (Android çerçevesi / system_server / uygulama tetiklemeli 'yumuşak' yeniden başlama)",
    "cekirdek": "Çekirdek çökmesi veya donanım watchdog (sürücü/çekirdek yazılımı YA DA donanım olabilir)",
    "guc": "Güç/pil kaynaklı kapanma (pil, gerilim düşmesi, PMIC) — donanım ağırlıklı",
    "isi": "Isı korumasıyla kapanma",
    "belirsiz": "Belirsiz/genel neden (cold/hard/warm/reboot) — tek başına ayırt etmez",
}


def boot_nedeni_sinifla(neden: str) -> str:
    n = (neden or "").strip().lower()
    if not n:
        return "belirsiz"
    if "thermal" in n:
        return "isi"
    if any(k in n for k in ("battery", "undervoltage", "brownout", "ocp", "pmic",
                             "power_key_lp", "long_press", "vbat", "uvlo", "over_current",
                             "lowbattery", "low_battery")):
        # Uzun basma kullanıcı kaynaklı da olabilir; analizde ayrıca belirtilir.
        return "guc"
    if any(k in n for k in ("kernel_panic", "watchdog", "hw_reset", "tz_err", "sec_wdt",
                             "apwdt", "bark", "bite", "abnormal")):
        return "cekirdek"
    if any(k in n for k in ("userrequested", "shell", "adb", "ota", "recovery",
                             "bootloader", "fastboot", "factory_reset", "reboot,requested",
                             "rescueparty", "shutdown,userrequested")):
        return "kullanici" if "rescueparty" not in n else "yazilim"
    if n.startswith(("reboot,framework", "reboot,system_server", "reboot,userspace")):
        return "yazilim"
    return "belirsiz"


def _oku(klasor: Path, ad: str) -> str:
    p = klasor / ad
    return p.read_text(encoding="utf-8", errors="replace") if p.exists() else ""


def getprop_ayristir(metin: str) -> dict[str, str]:
    return dict(re.findall(r"^\[([^\]]+)\]: \[([^\]]*)\]", metin, flags=re.M))


DROPBOX_SATIR = re.compile(
    r"^(\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2})\s+(\S+)\s+\(([^)]*)\)", re.M)


def dropbox_olaylari(metin: str) -> list[tuple[dt.datetime, str]]:
    olaylar = []
    for zaman, etiket, _ in DROPBOX_SATIR.findall(metin):
        try:
            olaylar.append((dt.datetime.strptime(zaman, "%Y-%m-%d %H:%M:%S"), etiket))
        except ValueError:
            pass
    return sorted(olaylar)


def dropbox_icerik_bloklari(metin: str) -> dict[str, str]:
    bloklar = {}
    for parca in re.split(r"^##### ETIKET ", metin, flags=re.M)[1:]:
        etiket, _, govde = parca.partition("\n")
        bloklar[etiket.strip()] = govde
    return bloklar


def paketleri_ayristir(metin: str) -> list[dict]:
    paketler = []
    for parca in re.split(r"^##### PAKET ", metin, flags=re.M)[1:]:
        baslik, _, govde = parca.partition("\n")
        ad, _, kurucu = baslik.partition(" installer=")
        def al(anahtar):
            m = re.search(anahtar + r"=([^\n]+)", govde)
            return m.group(1).strip() if m else ""
        def tarih(s):
            try:
                return dt.datetime.strptime(s[:19], "%Y-%m-%d %H:%M:%S")
            except ValueError:
                return None
        paketler.append({
            "ad": ad.strip(),
            "kurucu": kurucu.strip() or "null",
            "surum": al("versionName"),
            "ilk": tarih(al("firstInstallTime")),
            "son": tarih(al("lastUpdateTime")),
        })
    return paketler


def battery_ayristir(metin: str) -> dict[str, str]:
    return {k.strip(): v.strip() for k, v in re.findall(r"^\s*([A-Za-z ]+):\s*(.+)$", metin, re.M)}


SAGLIK = {"1": "bilinmiyor", "2": "iyi", "3": "aşırı ısınma", "4": "ölü",
          "5": "aşırı gerilim", "6": "belirtilmemiş arıza", "7": "soğuk"}

PAKET_ADI = re.compile(r"\b(?:Process|Package|pkg|>>>)\s*[:=]?\s*([a-zA-Z][\w]*(?:\.[\w]+)+)")


def suclu_paketler(govde: str) -> dict[str, int]:
    sayac: dict[str, int] = {}
    for p in PAKET_ADI.findall(govde):
        if p.startswith(("android.", "java.", "com.android.internal")):
            continue
        sayac[p] = sayac.get(p, 0) + 1
    return sayac


def analiz(klasor: Path, pencere_saat: int = 72) -> str:
    prop = getprop_ayristir(_oku(klasor, "getprop.txt"))
    neden_simdi = prop.get("sys.boot.reason", "")
    neden_onceki = prop.get("sys.boot.reason.last", "") or prop.get("persist.sys.boot.reason", "")
    neden_bl = prop.get("ro.boot.bootreason", "")
    sinif = boot_nedeni_sinifla(neden_simdi or neden_bl)

    olaylar = dropbox_olaylari(_oku(klasor, "dropbox_liste.txt"))
    bloklar = dropbox_icerik_bloklari(_oku(klasor, "dropbox_icerik.txt"))
    paketler = paketleri_ayristir(_oku(klasor, "paket_zamanlari.txt"))
    pil = battery_ayristir(_oku(klasor, "battery.txt"))
    crash_log = _oku(klasor, "logcat_crash.txt")
    onceki = _oku(klasor, "logcat_onceki_acilis.txt")
    df = _oku(klasor, "df.txt")

    sistem_cokme = [o for o in olaylar if o[1] in (
        "system_server_crash", "system_server_native_crash", "system_server_watchdog", "SYSTEM_RESTART")]
    acilislar = [o for o in olaylar if o[1] == "SYSTEM_BOOT"]
    kmsg = [o for o in olaylar if o[1] in ("SYSTEM_LAST_KMSG", "SYSTEM_TOMBSTONE")]
    uyg_cokme = [o for o in olaylar if o[1].startswith("data_app")]

    # system_server çökme içeriklerinde geçen paketler (yan yükleme ilişkisi için).
    suclu: dict[str, int] = {}
    for etiket in ("system_server_crash", "system_server_native_crash", "system_server_watchdog",
                   "SYSTEM_RESTART"):
        for k, v in suclu_paketler(bloklar.get(etiket, "")).items():
            suclu[k] = suclu.get(k, 0) + v
    ucuncu = {p["ad"] for p in paketler}
    suclu_ucuncu = {k: v for k, v in suclu.items() if k in ucuncu}

    # Yeniden başlama anları: SYSTEM_BOOT + yumuşak yeniden başlamalar.
    anlar = sorted({o[0] for o in acilislar + sistem_cokme})
    pencere = dt.timedelta(hours=pencere_saat)
    eslesme = []
    for an in anlar:
        yakin = [p for p in paketler
                 if (p["ilk"] and an - pencere <= p["ilk"] <= an)
                 or (p["son"] and an - pencere <= p["son"] <= an)]
        eslesme.append((an, yakin))

    yan_yuklu = [p for p in paketler if p["kurucu"] in ("null", "com.android.shell", "com.google.android.packageinstaller",
                                                        "com.android.packageinstaller")]

    # ---- karar (kanıt düzeyi) ----
    kanitlar, karar = [], ""
    if suclu_ucuncu:
        kanitlar.append("system_server çökme kaydında üçüncü taraf paket adı geçiyor: "
                        + ", ".join(f"`{k}` ({v} kez)" for k, v in suclu_ucuncu.items()))
    if sistem_cokme:
        kanitlar.append(f"{len(sistem_cokme)} adet system_server çökme/watchdog/SYSTEM_RESTART kaydı var (yazılım tarafı yumuşak yeniden başlama izi).")
    if kmsg:
        kanitlar.append(f"{len(kmsg)} adet SYSTEM_LAST_KMSG/TOMBSTONE kaydı var (çekirdek/yerel çökme izi; içeriği incelenmeli).")
    saglik = SAGLIK.get(pil.get("health", ""), pil.get("health", "?"))
    if pil.get("health") and pil.get("health") != "2":
        kanitlar.append(f"Pil sağlığı Android'e göre: **{saglik}** (donanım/güç şüphesini artırır).")
    try:
        sicaklik = int(pil.get("temperature", "0")) / 10
    except ValueError:
        sicaklik = 0
    if sicaklik >= 45:
        kanitlar.append(f"Pil sıcaklığı yüksek: {sicaklik:.1f} °C.")

    if suclu_ucuncu:
        karar = ("**Yazılım — uygulama tetiklemeli olma olasılığı YÜKSEK.** Çökme kaydı belirli bir üçüncü taraf "
                 "paketi gösteriyor. Kesinleştirmek için o paketi kaldırıp (veya Güvenli Mod'da) aynı kullanım "
                 "süresince yeniden başlama olmadığını gözleyin.")
    elif sinif == "yazilim" or (sistem_cokme and sinif in ("belirsiz", "kullanici")):
        karar = ("**Yazılım tarafı OLASI** (system_server yeniden başlaması izi var) ama belirli uygulama kaydı yok. "
                 "Güvenli Mod testi ve yeni yüklenen uygulamaları tek tek kaldırma ile daraltın.")
    elif sinif in ("guc", "isi"):
        karar = ("**Donanım/güç veya ısı tarafı OLASI.** Güvenli Mod'da da sürüyorsa ve şarjdayken/soğukken "
                 "davranış değişiyorsa pil/şarj devresi servis kontrolü gerekir.")
    elif sinif == "cekirdek":
        karar = ("**Çekirdek/watchdog seviyesinde.** Sürücü/ROM hatası veya donanım (pil, bellek, anakart) olabilir. "
                 "Güvenli Mod'da da tekrar ederse ve güncel yazılımdaysanız donanım ağırlık kazanır.")
    else:
        karar = ("**Kayıtlar tek başına ayırt etmiyor.** Tek kesin neden ilan edilemez; aşağıdaki güvenli test "
                 "sırasıyla ilerleyin ve bir sonraki kapanmadan hemen sonra bu aracı tekrar çalıştırın.")

    # ---- rapor ----
    r = []
    r.append("# Yeniden başlama teşhis raporu\n")
    r.append(f"Kayıt klasörü: `{klasor}`  \nÜretim: {dt.datetime.now():%Y-%m-%d %H:%M}\n")
    r.append("## 1. Cihaz\n")
    r.append(f"- Model: {prop.get('ro.product.manufacturer','?')} {prop.get('ro.product.model','?')}\n"
             f"- Android: {prop.get('ro.build.version.release','?')} (SDK {prop.get('ro.build.version.sdk','?')}), "
             f"güvenlik yaması {prop.get('ro.build.version.security_patch','?')}\n"
             f"- Yapı: `{prop.get('ro.build.fingerprint','?')}`\n")
    r.append("## 2. Son açılış nedeni (AOSP canonical boot reason)\n")
    r.append(f"- `sys.boot.reason` = `{neden_simdi or '—'}` → **{SINIF_ACIKLAMA[sinif]}**\n"
             f"- `ro.boot.bootreason` (önyükleyici) = `{neden_bl or '—'}`\n"
             f"- önceki/kalıcı (`sys.boot.reason.last` / `persist.sys.boot.reason`) = `{neden_onceki or '—'}`\n")
    if "long_press" in (neden_simdi + neden_bl).lower() or "power_key" in (neden_simdi + neden_bl).lower():
        r.append("- Not: güç tuşu uzun basma izi var; takılı tuş/kılıf baskısı da donanım nedeni olabilir.\n")
    r.append("\n## 3. Kanıtlar\n")
    r.extend(f"- {k}\n" for k in kanitlar) if kanitlar else r.append("- Belirgin kanıt kaydı bulunamadı.\n")
    r.append("\n## 4. Ara karar (kanıt düzeyine göre)\n\n" + karar + "\n")

    r.append("\n## 5. Olay zaman çizelgesi (dumpsys dropbox)\n\n| Zaman | Etiket |\n|---|---|\n")
    ilgili = [o for o in olaylar if o[1] in DROPBOX_ETIKETLER]
    r.extend(f"| {z:%Y-%m-%d %H:%M:%S} | {e} |\n" for z, e in ilgili[-60:]) if ilgili else r.append("| — | kayıt yok |\n")

    r.append(f"\n## 6. Yeniden başlama anları ↔ son {pencere_saat} saatte kurulan/güncellenen uygulamalar\n\n")
    if eslesme:
        r.append("| Yeniden başlama/çökme anı | Öncesindeki kurulum/güncellemeler |\n|---|---|\n")
        for an, yakin in eslesme[-30:]:
            liste = ", ".join(f"`{p['ad']}`" for p in yakin) or "—"
            r.append(f"| {an:%Y-%m-%d %H:%M:%S} | {liste} |\n")
    else:
        r.append("Kayıtlı yeniden başlama anı yok (dropbox boş/temizlenmiş olabilir).\n")

    r.append("\n## 7. Üçüncü taraf uygulamalar (en yeni güncellenen önce)\n\n"
             "| Paket | Kurucu | Sürüm | İlk kurulum | Son güncelleme | Not |\n|---|---|---|---|---|---|\n")
    for p in sorted(paketler, key=lambda p: p["son"] or dt.datetime.min, reverse=True)[:40]:
        not_ = "yan yükleme/adb" if p in yan_yuklu else ""
        if p["ad"] in suclu_ucuncu:
            not_ = (not_ + " · ÇÖKME KAYDINDA").strip(" ·")
        r.append(f"| `{p['ad']}` | {p['kurucu']} | {p['surum'] or '?'} | "
                 f"{p['ilk'] or '?'} | {p['son'] or '?'} | {not_} |\n")

    r.append("\n## 8. Pil, ısı, depolama\n\n")
    r.append(f"- Sağlık: {saglik}; seviye %{pil.get('level','?')}; sıcaklık {sicaklik:.1f} °C; "
             f"gerilim {pil.get('voltage','?')} mV; şarj: AC={pil.get('AC powered','?')} USB={pil.get('USB powered','?')}\n")
    m = re.search(r"(\d+)%\s+/data", df)
    if m:
        dolu = int(m.group(1))
        r.append(f"- /data doluluk: %{dolu}" + (" — **%90 üstü; Google'a göre %10'dan az boş alan sorun çıkarabilir**" if dolu >= 90 else "") + "\n")
    if crash_log.strip():
        r.append(f"- `logcat -b crash` satır sayısı: {len(crash_log.splitlines())}\n")
    r.append(f"- Önceki açılış logu (`logcat -L`, pstore): {'var' if onceki.strip() and 'STDERR' not in onceki[:200] else 'yok/desteklenmiyor'}\n")

    r.append("\n## 9. Sonraki güvenli test sırası\n\n"
             "1. Telefon bir sonraki kendiliğinden kapanıp açıldığında **hemen** bu aracı tekrar çalıştırın (kayıtlar halka tampon; geç kalınca silinir).\n"
             "2. Güvenli Mod'da (üçüncü taraf uygulamalar kapalı) en az eşit süre kullanın: kapanma **durursa** → uygulama; **sürerse** → sistem/donanım.\n"
             "3. Güvenli Mod temizse: yan yüklenen/son güncellenen uygulamaları (önce kendi APK'nız) birer birer kaldırıp her birinden sonra gözleyin.\n"
             "4. Güvenli Mod'da da sürüyorsa: şarjdayken vs. pilde, soğukken vs. sıcakken, kılıfsız ve tuşlara baskısız durumu karşılaştırın; pil sağlığı/şişme kontrolü için servise götürün.\n"
             "5. Yedek alıp fabrika ayarı sonrası hiçbir uygulama kurmadan da tekrarlıyorsa → donanım neredeyse kesin.\n")
    rapor = "".join(r)
    (klasor / "rapor.md").write_text(rapor, encoding="utf-8")
    return rapor


def main(argv=None):
    ap = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    alt = ap.add_subparsers(dest="komut", required=True)
    for ad in ("topla", "hepsi"):
        s = alt.add_parser(ad)
        s.add_argument("--cikti", default=f"reboot-kayit-{dt.datetime.now():%Y%m%d-%H%M%S}")
        s.add_argument("--seri", default=None)
    s = alt.add_parser("analiz")
    s.add_argument("klasor")
    s.add_argument("--pencere-saat", type=int, default=72)
    a = ap.parse_args(argv)
    if a.komut == "topla":
        topla(Path(a.cikti), a.seri)
    elif a.komut == "analiz":
        print(analiz(Path(a.klasor), a.pencere_saat))
    else:
        print(analiz(topla(Path(a.cikti), a.seri)))


if __name__ == "__main__":
    main()
