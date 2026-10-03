#!/usr/bin/env python3
"""Android kendiliğinden yeniden başlama teşhis aracı (salt-okunur, gizlilik öncelikli).

Komutlar:
  hepsi  : USB'ye bağlı telefondan izin listesindeki kayıtları toplar, analiz eder,
           yerel rapor + maskelenmiş paylaşım paketi (ZIP + SHA-256) üretir.
  isle   : Daha önce `--ham-sakla` ile saklanmış ham klasörü yeniden işler (cihaz gerekmez).
  denetle: Bir paylaşım klasörünü/ZIP'ini kişisel veri sızıntısı için geri okur.

Kullanım:
  python reboot_teshis.py hepsi [--cikti KLASOR] [--seri SERI] [--gun 7]
                                [--guvenli-mod bilinmiyor|kapanmadi|kapandi]
                                [--goster PAKET ...] [--zaman-asimi 120] [--ham-sakla]
  python reboot_teshis.py isle HAM_KLASOR --cikti KLASOR [--gun 7] [--simdi "YYYY-MM-DD HH:MM:SS"]
  python reboot_teshis.py denetle PAYLASIM_KLASORU_VEYA_ZIP

adb seçimi: PATH'teki adb (Windows'ta adb.exe) mutlak yola çözülür. Test/özel kurulum için
REBOOT_TESHIS_ADB_KOMUTU ortam değişkenine JSON dize listesi verilebilir
(ör. ["C:\\py\\python.exe", "testler\\sahte_adb.py"]); verilirse YALNIZ o kullanılır,
geçersizse araç durur ve PATH'teki adb'ye düşmez. Tüm dosya G/Ç ve adb çıktısı UTF-8'dir;
Windows Türkçe kod sayfası (cp1254) varsayılmaz.

Güvenlik sınırları:
  * Telefonda ayar değiştirmez, uygulama kaldırmaz, yeniden başlatmaz, root istemez.
  * Tam getprop, tam logcat, ham dropbox içeriği ve tam paket listesi DİSKE YAZILMAZ;
    bellekte süzülür. Ham kopya yalnız açıkça `--ham-sakla` verilirse `yerel-ham/`
    altına yazılır ve paylaşım paketine asla girmez.
  * YEREL çıktılar (`ozet-yerel.json`, `rapor-yerel.md`) yine kişisel veri içerir:
    uygulama adları, kurulum/güncelleme zamanları, sürümler ve maskelenmiş hata
    metinleri. Paylaşmayın; işiniz bitince silin.
  * Paylaşım paketine serbest metin (istisna mesajı, Watchdog Subject, stderr)
    HİÇ girmez; yalnız izin listesindeki hata sınıfları ve yapısal kategoriler girer.
    Üçüncü taraf paket adları takma adla gösterilir (`--goster` hariç).
  * Paylaşım paketi geçici klasörde üretilir, diskten ve ZIP'ten geri okunup
    denetlenir, ancak tümü geçerse yerine taşınır. Her çalıştırma önce eski
    paylaşım çıktılarını siler; başarısızlıkta hiçbir paylaşım dosyası kalmaz.
    Denetim yalnız TANIMLI örüntüleri arar; "temiz" sonucu kişisel veri
    olmadığının garantisi değildir.
Yalnız Python 3.10+ standart kütüphanesi ve PATH'te `adb` gerekir.
"""
from __future__ import annotations

import argparse
import datetime as dt
import hashlib
import json
import os
import re
import secrets
import shutil
import subprocess
import sys
import tempfile
import time
import zipfile
from dataclasses import asdict, dataclass, field
from pathlib import Path

SURUM = 2

# ------------------------------------------------------------ izin listeleri ---
IZINLI_PROP = [
    "sys.boot.reason", "sys.boot.reason.last", "persist.sys.boot.reason", "ro.boot.bootreason",
    "ro.product.manufacturer", "ro.product.model", "ro.build.version.release",
    "ro.build.version.sdk", "ro.build.version.security_patch", "ro.build.fingerprint",
]
# Paylaşım raporuna giren cihaz alanları (yapı parmak izi bile dışarıda kalır).
PAYLASIM_PROP = ["ro.product.manufacturer", "ro.product.model", "ro.build.version.release",
                 "ro.build.version.sdk", "ro.build.version.security_patch"]

DROPBOX_ETIKETLER = [
    "SYSTEM_BOOT", "SYSTEM_RESTART", "SYSTEM_LAST_KMSG", "SYSTEM_TOMBSTONE",
    "system_server_crash", "system_server_native_crash", "system_server_watchdog",
    "system_server_anr", "system_app_crash", "system_app_native_crash",
    "data_app_crash", "data_app_native_crash",
]
ICERIK_ETIKETLER = ["system_server_crash", "system_server_native_crash",
                    "system_server_watchdog", "SYSTEM_RESTART"]
YENIDEN_BASLAMA_ETIKETLERI = {"SYSTEM_BOOT", "SYSTEM_RESTART", "system_server_crash",
                              "system_server_native_crash", "system_server_watchdog"}
PIL_ALANLARI = ["AC powered", "USB powered", "Wireless powered", "status", "health",
                "level", "voltage", "temperature"]
# Açık gösterilebilen platform önekleri; diğer noktalı adlar paylaşımda takma ada çevrilir.
SISTEM_ONEKLERI = ("android.", "com.android.", "com.google.android.", "java.", "javax.",
                   "kotlin.", "dalvik.", "libcore.", "sun.", "jdk.", "sys.", "ro.", "persist.")
SISTEM_ADLARI = {"android", "system_server", "system", "com.android", "surfaceflinger"}

# Ham kopya dosya adları (yalnız --ham-sakla / isle için).
HAM_DOSYALAR = ["getprop.txt", "uptime.txt", "dropbox_liste.txt", "dropbox_icerik.txt",
                "battery.txt", "thermal.txt", "df.txt", "paketler_3.txt", "paket_zamanlari.txt",
                "logcat_crash.txt", "logcat_events_boot.txt", "logcat_onceki_acilis.txt"]

ZAMAN_BICIMI = "%Y-%m-%d %H:%M:%S"


# ------------------------------------------------------------------ maskeleme ---
IPV6 = re.compile(r"(?<![\w:])(?=[0-9a-f:]*(?:[a-f][0-9a-f:]*|::))(?:[0-9a-f]{1,4}:|:){2,7}[0-9a-f]{0,4}(?![\w:])",
                  re.I)
MASKE_KURALLARI = [
    (re.compile(r"\b(?:Bearer|Basic|Token)\s+[\w.~+/=-]{6,}", re.I), "<kimlik-bilgisi>"),
    (re.compile(r"[\"']?(?:password|passwd|secret|token|api[_-]?key|access[_-]?key|authorization|auth|cookie)"
                r"[\"']?\s*[:=]\s*[\"']?[^\s\"',}]+[\"']?", re.I), "<gizli-alan>"),
    (re.compile(r"[A-Za-z]:\\[^\s\"']*"), "<yol>"),
    (re.compile(r"(?:/home|/Users|/root)/[^\s\"']*"), "<yol>"),
    (re.compile(r"[\w.+-]+@[\w-]+(?:\.[\w-]+)+"), "<e-posta>"),
    (re.compile(r"\b(?:https?|ftp|content|file)://\S+", re.I), "<uri>"),
    (re.compile(r"\b(?:[0-9a-f]{2}[:-]){5}[0-9a-f]{2}\b", re.I), "<mac>"),
    (re.compile(r"\b(?:\d{1,3}\.){3}\d{1,3}\b"), "<ip>"),
    (IPV6, "<ip>"),
    (re.compile(r"(?:/storage|/sdcard|/mnt/media_rw|/data/user(?:_de)?/\d+|/data/data|/data/media)\S*"), "<yol>"),
    (re.compile(r"\b(?:account|acct|email|user(?:name)?|name|phone|number|imei|meid|serial|"
                r"android_id|ssid|bssid|token|password|pass)\s*[=:]\s*\S+", re.I), "<gizli-alan>"),
    (re.compile(r"\+?\d[\d \-()]{8,}\d"), "<sayi>"),
    (re.compile(r"\b[0-9a-f]{16,}\b", re.I), "<kimlik>"),
]
NOKTALI_AD = re.compile(r"\b[A-Za-z_][\w$]*(?:\.[A-Za-z_][\w$]*){1,}\b")


def maskele(metin: str, ozel: list[str] | None = None, uzunluk: int = 160) -> str:
    """Serbest metindeki kişisel veri biçimlerini sabit etiketlerle değiştirir."""
    m = metin or ""
    for deger in (ozel or []):
        if deger and len(deger) >= 3:
            m = m.replace(deger, "<seri>")
    for desen, yerine in MASKE_KURALLARI:
        m = desen.sub(yerine, m)
    return m.strip()[:uzunluk]


# Paylaşımda açık gösterilebilen hata sınıfları (yapısal; mesaj metni ASLA paylaşılmaz).
HATA_SINIFLARI = {
    "java.lang.RuntimeException", "java.lang.IllegalStateException", "java.lang.IllegalArgumentException",
    "java.lang.NullPointerException", "java.lang.SecurityException", "java.lang.OutOfMemoryError",
    "java.lang.StackOverflowError", "java.lang.IndexOutOfBoundsException",
    "java.lang.ArrayIndexOutOfBoundsException", "java.lang.ClassCastException",
    "java.lang.UnsupportedOperationException", "java.lang.NumberFormatException",
    "java.lang.UnsatisfiedLinkError", "java.lang.NoSuchMethodError", "java.lang.NoClassDefFoundError",
    "java.lang.ArithmeticException", "java.lang.InterruptedException", "java.lang.Error",
    "java.util.ConcurrentModificationException", "java.util.NoSuchElementException",
    "java.util.concurrent.TimeoutException", "java.io.IOException", "java.io.FileNotFoundException",
    "android.os.DeadObjectException", "android.os.DeadSystemException", "android.os.RemoteException",
    "android.os.TransactionTooLargeException", "android.os.BadParcelableException",
    "android.os.NetworkOnMainThreadException", "android.app.RemoteServiceException",
    "android.database.sqlite.SQLiteException", "android.database.sqlite.SQLiteFullException",
    "android.database.CursorWindowAllocationException", "android.view.WindowManager$BadTokenException",
    "android.content.ActivityNotFoundException", "android.util.AndroidRuntimeException",
}
WATCHDOG_ISPARCACIKLARI = {"main", "android.ui", "android.fg", "android.io", "android.display",
                           "android.anim", "android.anim.lf", "android.bg", "ActivityManager",
                           "PowerManagerService", "PackageManager", "WindowManager", "watchdog"}
PLATFORM_ONEKLERI = ("java.", "javax.", "android.", "dalvik.", "libcore.", "kotlin.", "com.android.")


def hata_sinifi_paylasim(istisna: str) -> str:
    if not istisna:
        return ""
    if istisna in HATA_SINIFLARI:
        return istisna
    return "<diğer-platform-istisnası>" if istisna.startswith(PLATFORM_ONEKLERI) else "<uygulama-istisnası>"


def watchdog_kategori(konu: str) -> str:
    """Watchdog Subject'ini serbest metin taşımayan bir kategoriye indirger."""
    m = re.match(r"\s*Blocked in handler on [\w ]*?thread \(([\w.$]+)\)", konu or "")
    if m:
        return "handler-takılması:" + (m.group(1) if m.group(1) in WATCHDOG_ISPARCACIKLARI else "<diğer>")
    m = re.match(r"\s*Blocked in monitor (com\.android\.server\.[A-Za-z][\w.$]{0,80}?)(?:\s|$)", konu or "")
    if m:
        return "monitor-takılması:" + m.group(1)
    return "<watchdog-diğer>" if konu else ""


def sinyal_kategori(sinyal: str) -> str:
    m = re.search(r"\b(SIG[A-Z]{2,7})\b", sinyal or "")
    if m:
        return "sinyal:" + m.group(1)
    return "<abort-mesajı>" if sinyal else ""


def paylasim_ipucu(o: dict) -> str:
    """Çökme özetinin paylaşılabilir, yalnız yapısal temsili."""
    if o.get("konu"):
        return watchdog_kategori(o["konu"])
    if o.get("istisna"):
        return hata_sinifi_paylasim(o["istisna"])
    return sinyal_kategori(o.get("sinyal", ""))


def sistem_adi_mi(ad: str) -> bool:
    return ad in SISTEM_ADLARI or ad.startswith(SISTEM_ONEKLERI)


class TakmaAd:
    """Paylaşımda üçüncü taraf paket adlarını kararlı ama geri çevrilemez takma adlara çevirir."""

    def __init__(self, goster: list[str] | None = None, tuz: str | None = None):
        self.goster = set(goster or [])
        self.tuz = tuz or secrets.token_hex(16)

    def ad(self, paket: str) -> str:
        temel = paket.split(":")[0]
        if sistem_adi_mi(temel) or temel in self.goster:
            return paket
        return "uyg-" + hashlib.sha256((self.tuz + temel).encode()).hexdigest()[:8]

    def metin(self, s: str) -> str:
        return NOKTALI_AD.sub(lambda mt: self.ad(mt.group(0)), s)


# ----------------------------------------------------------- adb (yapısal) ---
@dataclass
class AdbSonuc:
    ad: str
    cikis_kodu: int | None
    zaman_asimi: bool = False
    izin_hatasi: bool = False
    servis_yok: bool = False
    hata: str = ""
    sure_sn: float = 0.0

    @property
    def basarili(self) -> bool:
        return self.cikis_kodu == 0 and not (self.zaman_asimi or self.izin_hatasi or self.servis_yok)


IZIN_DESENI = re.compile(r"permission denied|permission denial|securityexception|not allowed|"
                         r"requires android\.permission|operation not permitted", re.I)
SERVIS_YOK_DESENI = re.compile(r"can't find service", re.I)


ADB_KOMUTU_ORTAM = "REBOOT_TESHIS_ADB_KOMUTU"


def adb_komutu() -> list[str]:
    """Çalıştırılacak adb komutunu belirler.

    Ortamda REBOOT_TESHIS_ADB_KOMUTU varsa (JSON dize listesi, ör. ["python", "sahte_adb.py"])
    YALNIZ o kullanılır; geçersizse ValueError — PATH'teki gerçek adb'ye ASLA düşülmez.
    Yoksa PATH'teki adb mutlak yola çözülür (Windows'ta adb.exe); bulunamazsa FileNotFoundError."""
    deger = os.environ.get(ADB_KOMUTU_ORTAM)
    if deger is not None:
        try:
            komut = json.loads(deger)
        except ValueError:
            raise ValueError(f"{ADB_KOMUTU_ORTAM} geçerli JSON değil") from None
        if not (isinstance(komut, list) and komut and all(isinstance(x, str) and x for x in komut)):
            raise ValueError(f"{ADB_KOMUTU_ORTAM} boş olmayan bir JSON dize listesi olmalı")
        return komut
    yol = shutil.which("adb")
    if not yol:
        raise FileNotFoundError("adb")
    return [yol]


class Adb:
    def __init__(self, seri: str | None, zaman_asimi: int = 120, gizli: list[str] | None = None,
                 taban: list[str] | None = None):
        self.taban = list(taban) if taban else adb_komutu()
        self.seri = seri
        self.zaman_asimi = zaman_asimi
        self.gizli = gizli or []
        self.sonuclar: list[AdbSonuc] = []

    def calistir(self, ad: str, args: list[str], kaydet: bool = True) -> tuple[AdbSonuc, str]:
        komut = self.taban + (["-s", self.seri] if self.seri else []) + args
        bas = time.monotonic()
        try:
            p = subprocess.run(komut, capture_output=True, text=True, encoding="utf-8",
                               errors="replace", timeout=self.zaman_asimi)
            cikti, hata, kod, asim = p.stdout, p.stderr, p.returncode, False
        except subprocess.TimeoutExpired as e:
            cikti = (e.stdout or b"").decode("utf-8", "replace") if isinstance(e.stdout, bytes) else (e.stdout or "")
            hata, kod, asim = "zaman aşımı", None, True
        except FileNotFoundError:
            cikti, hata, kod, asim = "", "adb bulunamadı", 127, False
        # Hata tespiti stderr + çıktının ilk satırlarıyla sınırlı: çökme metnindeki
        # "SecurityException" gibi içerik izin hatası sayılmasın.
        bas_metin = (hata or "") + "\n" + "\n".join(cikti.splitlines()[:3])
        s = AdbSonuc(ad=ad, cikis_kodu=kod, zaman_asimi=asim or "DUMP TIMEOUT" in cikti[:500],
                     izin_hatasi=bool(IZIN_DESENI.search(bas_metin)),
                     servis_yok=bool(SERVIS_YOK_DESENI.search(bas_metin)),
                     hata=maskele(hata, self.gizli, 200), sure_sn=round(time.monotonic() - bas, 2))
        if kaydet:
            self.sonuclar.append(s)
        return s, cikti


def cihazlari_ayristir(metin: str) -> list[dict]:
    """`adb devices -l` çıktısını [{seri, durum, nitelikler}] listesine çevirir."""
    cihazlar = []
    for satir in metin.splitlines():
        satir = satir.strip()
        if not satir or satir.startswith(("List of devices", "*")):
            continue
        parca = satir.split()
        if len(parca) < 2:
            continue
        durum = parca[1]
        if durum == "no" and len(parca) > 2 and parca[2] == "permissions":
            durum = "no permissions"
        cihazlar.append({"seri": parca[0], "durum": durum})
    return cihazlar


def cihaz_sec(cihazlar: list[dict], seri: str | None) -> str:
    """Hazır cihazı seçer; uygun değilse açıklayıcı ValueError fırlatır (seri metne yazılmaz)."""
    hazir = [c["seri"] for c in cihazlar if c["durum"] == "device"]
    if seri:
        eslesen = [c for c in cihazlar if c["seri"] == seri]
        if not eslesen:
            raise ValueError("Verilen seri `adb devices -l` listesinde yok; ilerlenmedi.")
        if eslesen[0]["durum"] != "device":
            raise ValueError(f"Verilen seri hazır değil (durum: {eslesen[0]['durum']}). "
                             "'unauthorized' ise telefonda USB hata ayıklama iznini onaylayın.")
        return seri
    if not cihazlar:
        raise ValueError("`adb devices -l` boş: bağlı telefon görünmüyor. USB hata ayıklamayı açıp "
                         "kabloyu/izni kontrol edin.")
    if not hazir:
        durumlar = ", ".join(sorted({c["durum"] for c in cihazlar}))
        raise ValueError(f"Hazır cihaz yok (durumlar: {durumlar}). 'unauthorized' ise telefonda izni onaylayın.")
    if len(hazir) > 1:
        raise ValueError(f"{len(hazir)} hazır cihaz var; --seri ile birini seçin.")
    return hazir[0]


# ------------------------------------------------------------------ toplama ---
def topla_ham(adb: Adb, servis_bekle_sn: int = 120) -> dict[str, str]:
    """Cihazdan yalnız gerekli kayıtları BELLEĞE toplar. Diske yazmaz."""
    ham: dict[str, str] = {}
    bas = time.monotonic()
    while time.monotonic() - bas < servis_bekle_sn:
        s, o = adb.calistir("servis_kontrol", ["shell", "service check dropbox"], kaydet=False)
        if ": found" in o:
            break
        print("  dropbox servisi bekleniyor (sistem yeni açılıyor olabilir)...")
        time.sleep(5)

    satirlar = []
    for anahtar in IZINLI_PROP:  # tam getprop yerine tek tek izinli anahtarlar
        _, o = adb.calistir(f"getprop:{anahtar}", ["shell", f"getprop {anahtar}"])
        satirlar.append(f"[{anahtar}]: [{o.strip()}]")
    ham["getprop.txt"] = "\n".join(satirlar) + "\n"

    komutlar = {
        "uptime.txt": "cat /proc/uptime; date +%s; date '+%Y-%m-%d %H:%M:%S'",
        "dropbox_liste.txt": "dumpsys -t 60 dropbox",
        "battery.txt": "dumpsys -t 60 battery",
        "thermal.txt": "dumpsys -t 60 thermalservice",
        "df.txt": "df /data",
        "paketler_3.txt": "pm list packages -3 -i",
        "logcat_crash.txt": "logcat -b crash -d -v threadtime -v year",
        "logcat_events_boot.txt": "logcat -b events -d -v threadtime -v year",
        "logcat_onceki_acilis.txt": "logcat -L -b all -d -v threadtime -v year",
    }
    # Büyük çıktılar bellekte hemen süzülür; süzülmemiş hali hiçbir yerde tutulmaz.
    suzgec = {
        "thermal.txt": (r"thermal status|^\s*status:", None),
        "logcat_events_boot.txt": (r"boot_progress_start|\bI watchdog\b", None),
        "logcat_onceki_acilis.txt": (r"kernel panic|watchdog|thermal|battery|shutdown|reboot", 200),
        "logcat_crash.txt": (r"FATAL EXCEPTION|Process: |DeadSystemException|The system died|Fatal signal|"
                             r"beginning of", None),
    }
    for dosya, kmt in komutlar.items():
        print(f"  toplanıyor: {dosya}")
        _, o = adb.calistir(dosya, ["shell", kmt])
        if dosya in suzgec:
            desen, son = suzgec[dosya]
            o = "\n".join(x for x in o.splitlines() if re.search(desen, x, re.I))
            if son:
                o = "\n".join(o.splitlines()[-son:])
        ham[dosya] = o

    bloklar = []
    for etiket in ICERIK_ETIKETLER:
        _, o = adb.calistir(f"dropbox_icerik:{etiket}", ["shell", f"dumpsys -t 60 dropbox --print {etiket}"])
        bloklar.append(f"##### ETIKET {etiket}\n{o[-400000:]}\n")
    ham["dropbox_icerik.txt"] = "".join(bloklar)

    paketler = []
    for satir in ham["paketler_3.txt"].splitlines():
        m = re.match(r"package:(\S+)(?:\s+installer=(\S+))?", satir.strip())
        if not m:
            continue
        _, o = adb.calistir("paket_zamani", ["shell", f"dumpsys -t 60 package {m.group(1)}"])
        o = "\n".join(x for x in o.splitlines() if re.search(r"versionName|firstInstallTime|lastUpdateTime", x))
        paketler.append(f"##### PAKET {m.group(1)} installer={m.group(2) or 'null'}\n{o}\n")
    ham["paket_zamanlari.txt"] = "".join(paketler)
    return ham


# --------------------------------------------------------------- ayrıştırma ---
def _tarih(s: str) -> dt.datetime | None:
    try:
        return dt.datetime.strptime(s.strip()[:19], ZAMAN_BICIMI)
    except (ValueError, AttributeError):
        return None


def getprop_ayristir(metin: str) -> dict[str, str]:
    tum = dict(re.findall(r"^\[([^\]]+)\]: \[([^\]]*)\]", metin, flags=re.M))
    return {k: tum[k] for k in IZINLI_PROP if k in tum}


DROPBOX_SATIR = re.compile(r"^(\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2})\s+(\S+)\s+\(([^)]*)\)", re.M)


def dropbox_olaylari(metin: str) -> list[tuple[dt.datetime, str]]:
    olaylar = []
    for zaman, etiket, _ in DROPBOX_SATIR.findall(metin):
        t = _tarih(zaman)
        if t and etiket in DROPBOX_ETIKETLER:
            olaylar.append((t, etiket))
    return sorted(olaylar)


ISTISNA = re.compile(r"^((?:[a-z_]\w*\.)+[A-Z]\w*(?:Exception|Error))\b:?\s*(.*)$")


def cokme_ozetleri(metin: str, gizli: list[str]) -> list[dict]:
    """dropbox --print içeriğinden yalnız yapısal alanları çıkarır; ham metni tutmaz."""
    ozetler = []
    for parca in re.split(r"^##### ETIKET ", metin, flags=re.M)[1:]:
        etiket, _, govde = parca.partition("\n")
        etiket = etiket.strip()
        for giris in re.split(r"^={10,}\s*$", govde, flags=re.M):
            m = DROPBOX_SATIR.search(giris)
            if not m:
                continue
            kayit = {"zaman": m.group(1), "etiket": etiket, "istisna": "", "mesaj": "",
                     "konu": "", "sinyal": "", "paketler": []}
            for satir in giris[m.end():].splitlines():
                satir = satir.strip()
                if not satir:
                    continue
                if satir.startswith("Package:"):
                    p = satir.split()[1] if len(satir.split()) > 1 else ""
                    if p:
                        kayit["paketler"].append(p)
                elif satir.startswith("Subject:") and not kayit["konu"]:
                    kayit["konu"] = maskele(satir[8:], gizli)
                elif satir.startswith(("Abort message:", "signal ")) and not kayit["sinyal"]:
                    kayit["sinyal"] = maskele(satir, gizli, 120)
                elif not kayit["istisna"]:
                    mi = ISTISNA.match(satir)
                    if mi:
                        kayit["istisna"] = mi.group(1)
                        kayit["mesaj"] = maskele(mi.group(2), gizli, 120)
            ozetler.append(kayit)
    return ozetler


def paketleri_ayristir(metin: str) -> list[dict]:
    paketler = []
    for parca in re.split(r"^##### PAKET ", metin, flags=re.M)[1:]:
        baslik, _, govde = parca.partition("\n")
        ad, _, kurucu = baslik.partition(" installer=")

        def al(anahtar):
            m = re.search(anahtar + r"=([^\n]+)", govde)
            return m.group(1).strip() if m else ""
        ilk, son = _tarih(al("firstInstallTime")), _tarih(al("lastUpdateTime"))
        paketler.append({"ad": ad.strip(), "kurucu": kurucu.strip() or "null",
                         "surum": maskele(al("versionName"), uzunluk=40),
                         "ilk": ilk.strftime(ZAMAN_BICIMI) if ilk else "",
                         "son": son.strftime(ZAMAN_BICIMI) if son else ""})
    return paketler


YAN_YUKLEME_KURUCULARI = {"null", "com.android.shell", "com.google.android.packageinstaller",
                          "com.android.packageinstaller"}


def ham_isle(ham: dict[str, str], pencere_gun: int = 7, gizli: list[str] | None = None,
             simdi: dt.datetime | None = None, toplama: list[dict] | None = None) -> dict:
    """Ham metinleri izin listesi + zaman penceresi + maskeleme ile yapısal özete çevirir."""
    gizli = gizli or []
    prop = getprop_ayristir(ham.get("getprop.txt", ""))
    up_satir = ham.get("uptime.txt", "").splitlines()
    if simdi is None:  # Önce cihazın yerel saati (loglarla aynı saat dilimi), sonra epoch.
        simdi = next((t for t in (_tarih(x) for x in reversed(up_satir)) if t), None)
    if simdi is None:
        try:
            simdi = dt.datetime.fromtimestamp(int(ham.get("uptime.txt", "").split()[2]))
        except (IndexError, ValueError):
            simdi = dt.datetime.now()
    alt = simdi - dt.timedelta(days=pencere_gun)
    pencerede = lambda t: t is not None and alt <= t <= simdi + dt.timedelta(minutes=5)

    olaylar = [(t, e) for t, e in dropbox_olaylari(ham.get("dropbox_liste.txt", "")) if pencerede(t)]
    ozetler = [o for o in cokme_ozetleri(ham.get("dropbox_icerik.txt", ""), gizli)
               if o["etiket"] in ICERIK_ETIKETLER and pencerede(_tarih(o["zaman"]))]

    events = ham.get("logcat_events_boot.txt", "")
    bps = sorted(t for t in (_tarih(z) for z in re.findall(
        r"^(\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2})\.\d+\s.*\bboot_progress_start\b", events, re.M)) if pencerede(t))
    watchdog = []
    for z, w in re.findall(r"^(\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2})\.\d+\s.*\bwatchdog: (.+)$", events, re.M):
        if pencerede(_tarih(z)):
            watchdog.append({"zaman": z, "konu": maskele(w, gizli)})

    crash = ham.get("logcat_crash.txt", "")
    ilk_hata, sistem_oldu, surecler = None, 0, {}
    for satir in crash.splitlines():
        t = _tarih(satir[:19])
        if not pencerede(t):
            continue
        if re.search(r"DeadSystemException|The system died|FATAL EXCEPTION IN SYSTEM PROCESS", satir):
            sistem_oldu += 1
        m = re.search(r"Process: ([\w.:]+), PID", satir)
        if m:
            surecler[m.group(1)] = surecler.get(m.group(1), 0) + 1
        if re.search(r"FATAL EXCEPTION|Fatal signal", satir) and ilk_hata is None:
            ilk_hata = t.strftime(ZAMAN_BICIMI)
    # İlk hata: crash tamponu, system_server dropbox kayıtları ve events Watchdog'un en erkeni.
    adaylar = [o["zaman"] for o in ozetler if o["etiket"].startswith("system_server")]
    adaylar += [w["zaman"] for w in watchdog] + ([ilk_hata] if ilk_hata else [])
    ilk_hata = min(adaylar) if adaylar else None

    pil_ham = dict(re.findall(r"^\s*([A-Za-z ]+):\s*(.+)$", ham.get("battery.txt", ""), re.M))
    pil = {k: pil_ham[k].strip() for k in PIL_ALANLARI if k in pil_ham}
    termal = re.search(r"(?:thermal status|status):\s*(\d+)", ham.get("thermal.txt", ""), re.I)
    df = re.search(r"(\d+)%\s+/data", ham.get("df.txt", ""))
    onceki = ham.get("logcat_onceki_acilis.txt", "")
    onceki_var = bool(onceki.strip()) and "read failure" not in onceki.lower() and "STDERR" not in onceki[:200]

    paketler = paketleri_ayristir(ham.get("paket_zamanlari.txt", ""))
    for p in paketler:
        p["yan_yukleme"] = p["kurucu"] in YAN_YUKLEME_KURUCULARI
        p["pencerede"] = pencerede(_tarih(p["ilk"])) or pencerede(_tarih(p["son"]))

    return {
        "surum": SURUM, "pencere_gun": pencere_gun,
        "cihaz_zamani": simdi.strftime(ZAMAN_BICIMI),
        "cihaz": prop,
        "dropbox_olaylari": [{"zaman": t.strftime(ZAMAN_BICIMI), "etiket": e} for t, e in olaylar],
        "cokme_ozetleri": ozetler,
        "cerceve_baslangiclari": [t.strftime(ZAMAN_BICIMI) for t in bps],
        "watchdog": watchdog,
        "crash_tamponu": {"sistem_oldu": sistem_oldu, "surecler": surecler, "ilk_hata": ilk_hata},
        "pil": pil, "termal_durum": termal.group(1) if termal else "",
        "data_doluluk": int(df.group(1)) if df else None,
        "onceki_acilis": {"var": onceki_var,
                          "panik_izi": len(re.findall(r"kernel panic", onceki, re.I)) if onceki_var else 0},
        "paketler": paketler,
        "toplama": toplama or [],
    }


# ------------------------------------------------------------------ analiz ---
SINIF_ACIKLAMA = {
    "kullanici": "Kullanıcı/komut/OTA kaynaklı planlı yeniden başlatma",
    "yazilim": "Android çerçevesi (system_server) tarafında yumuşak yeniden başlama",
    "cekirdek": "Çekirdek çökmesi veya watchdog (çekirdek/sürücü/firmware YA DA donanım olabilir)",
    "guc": "Güç/pil kaynaklı kapanma izi (pil, gerilim düşmesi, PMIC; uzun güç tuşu basışı da olabilir)",
    "isi": "Isı korumasıyla kapanma",
    "belirsiz": "Genel/belirsiz neden (cold/hard/warm/reboot) — tek başına ayırt etmez",
}


def boot_nedeni_sinifla(neden: str) -> str:
    n = (neden or "").strip().lower()
    if not n:
        return "belirsiz"
    if "thermal" in n:
        return "isi"
    if "rescueparty" in n:
        return "yazilim"
    if any(k in n for k in ("battery", "undervoltage", "brownout", "ocp", "pmic", "power_key_lp",
                             "long_press", "vbat", "uvlo", "over_current", "lowbattery", "low_battery")):
        return "guc"
    if any(k in n for k in ("kernel_panic", "watchdog", "hw_reset", "tz_err", "sec_wdt",
                             "apwdt", "bark", "bite", "abnormal")):
        return "cekirdek"
    if any(k in n for k in ("userrequested", "shell", "adb", "ota", "recovery", "bootloader",
                             "fastboot", "factory_reset", "reboot,requested")):
        return "kullanici"
    if n.startswith(("reboot,framework", "reboot,system_server", "reboot,userspace")):
        return "yazilim"
    return "belirsiz"


SAGLIK = {"1": "bilinmiyor", "2": "iyi", "3": "aşırı ısınma", "4": "ölü",
          "5": "aşırı gerilim", "6": "belirtilmemiş arıza", "7": "soğuk"}

TOPLAMA_ETKISI = {
    "dropbox_liste.txt": "dropbox olay listesi", "dropbox_icerik": "çökme metinleri",
    "logcat_crash.txt": "crash tamponu", "logcat_events_boot.txt": "çerçeve başlangıçları/Watchdog",
    "paketler_3.txt": "uygulama listesi", "paket_zamani": "kurulum zamanları",
    "battery.txt": "pil durumu", "getprop": "açılış nedeni", "uptime.txt": "cihaz saati",
    "logcat_onceki_acilis.txt": "önceki açılış logu",
}


def degerlendir(oz: dict, guvenli_mod: str = "bilinmiyor", pencere_saat: int = 72) -> dict:
    """Kanıtları hipotezlere çevirir. Asla tek kesin neden ilan etmez."""
    c = oz["cihaz"]
    neden = c.get("sys.boot.reason", "") or c.get("ro.boot.bootreason", "")
    sinif = boot_nedeni_sinifla(neden)
    anlar = sorted({_tarih(o["zaman"]) for o in oz["dropbox_olaylari"] if o["etiket"] in YENIDEN_BASLAMA_ETIKETLERI}
                   | {_tarih(z) for z in oz["cerceve_baslangiclari"][1:]})
    pencere = dt.timedelta(hours=pencere_saat)
    eslesme = []
    for an in anlar:
        yakin = [p for p in oz["paketler"]
                 if any(t and an - pencere <= t <= an for t in (_tarih(p["ilk"]), _tarih(p["son"])))]
        eslesme.append((an, yakin))

    eksik = []
    for s in oz["toplama"]:
        if not (s["cikis_kodu"] == 0 and not (s["zaman_asimi"] or s["izin_hatasi"] or s["servis_yok"])):
            tur = ("zaman aşımı" if s["zaman_asimi"] else "izin yok" if s["izin_hatasi"]
                   else "servis yok" if s["servis_yok"] else f"çıkış kodu {s['cikis_kodu']}")
            anahtar = s["ad"].split(":")[0]
            eksik.append({"kayit": s["ad"], "tur": tur, "etki": TOPLAMA_ETKISI.get(anahtar, s["ad"])})
    eksik_kayit = {e["kayit"].split(":")[0] for e in eksik}

    kanit = {
        "olay_zamani": bool(anlar),
        "ilk_hata": bool(oz["crash_tamponu"]["ilk_hata"]),
        "karsilastirma": guvenli_mod in ("kapanmadi", "kapandi"),
    }
    hipotezler = []
    ss = [o for o in oz["cokme_ozetleri"] if o["etiket"].startswith("system_server") or o["etiket"] == "SYSTEM_RESTART"]
    yumusak = max(0, len(oz["cerceve_baslangiclari"]) - 1)
    if ss or yumusak or oz["crash_tamponu"]["sistem_oldu"] or sinif == "yazilim":
        hipotezler.append({
            "ad": "Android sistem yazılımı tarafında yumuşak yeniden başlama",
            "destek": [x for x in [
                f"{len(ss)} system_server çökme/watchdog kaydı" if ss else "",
                f"{yumusak} ek çerçeve başlangıcı (boot_progress_start)" if yumusak else "",
                f"crash tamponunda {oz['crash_tamponu']['sistem_oldu']} 'sistem öldü' satırı"
                if oz["crash_tamponu"]["sistem_oldu"] else ""] if x],
            "not": "Tetikleyici bir uygulama, sistem bileşeni, düşük bellek/depolama veya donanım kaynaklı "
                   "takılma olabilir; çökme metninin ilk istisnası ipucudur, kanıt değildir."})
    cokme_paketleri: dict[str, int] = {}
    for o in ss:
        for p in o["paketler"]:
            if not sistem_adi_mi(p):
                cokme_paketleri[p] = cokme_paketleri.get(p, 0) + 1
    yakin_yan = {p["ad"] for _, y in eslesme for p in y if p["yan_yukleme"]}
    if cokme_paketleri or yakin_yan:
        hipotezler.append({
            "ad": "Üçüncü taraf bir uygulamanın tetiklemesi",
            "destek": [x for x in [
                ("çökme kaydında geçen paket(ler): " + ", ".join(f"{{P:{k}}}" for k in cokme_paketleri))
                if cokme_paketleri else "",
                ("yeniden başlamadan önceki pencerede kurulan/güncellenen yan yüklenmiş: "
                 + ", ".join(f"{{P:{k}}}" for k in sorted(yakin_yan))) if yakin_yan else ""] if x],
            "not": "Paket adının çökme metninde görünmesi veya kurulum zamanının yakınlığı nedensellik "
                   "değildir (o an ön planda olan ya da etkilenen masum uygulama da görünür). Doğrulama: "
                   "Güvenli Mod karşılaştırması ve tek tek kaldırarak eleme."})
    if sinif in ("guc", "isi") or (oz["pil"].get("health") not in (None, "", "2")):
        hipotezler.append({
            "ad": "Güç/pil/ısı (donanım ağırlıklı)",
            "destek": [x for x in [f"açılış nedeni `{neden}`" if sinif in ("guc", "isi") else "",
                                   f"pil sağlığı: {SAGLIK.get(oz['pil'].get('health', ''), oz['pil'].get('health', '?'))}"
                                   if oz["pil"].get("health") not in (None, "", "2") else ""] if x],
            "not": "Uzun güç tuşu basışı veya takılı tuş da benzer iz bırakabilir."})
    if sinif == "cekirdek" or oz["onceki_acilis"]["panik_izi"]:
        hipotezler.append({
            "ad": "Çekirdek/watchdog seviyesinde kapanma",
            "destek": [x for x in [f"açılış nedeni `{neden}`" if sinif == "cekirdek" else "",
                                   "önceki açılış logunda kernel panic izi" if oz["onceki_acilis"]["panik_izi"] else ""] if x],
            "not": "Çekirdek/sürücü/firmware hatası veya donanım (bellek, pil, anakart) olabilir."})

    if all(kanit.values()) and hipotezler:
        duzey = "olası (karşılaştırmalı kanıt var, yine de kesin değil)"
    elif hipotezler:
        duzey = "belirsiz hipotez (ilk hata, olay zamanı veya Güvenli Mod karşılaştırmasından en az biri eksik)"
    else:
        duzey = "belirsiz — mevcut kayıtlar ayırt etmiyor"
    if guvenli_mod == "kapanmadi":
        gm = ("Güvenli Mod'da kapanmadı → üçüncü taraf uygulama katmanı **olası** (kesin değil: Güvenli Mod'da "
              "kullanım/yük de farklıdır; yeterli süre gözlenmelidir).")
    elif guvenli_mod == "kapandi":
        gm = ("Güvenli Mod'da da kapandı → üçüncü taraf uygulama dışı neden **olası**: sistem yazılımı, "
              "firmware/sürücü veya donanım.")
    else:
        gm = "Güvenli Mod karşılaştırması henüz yapılmadı/bildirilmedi."
    return {"sinif": sinif, "neden": neden, "anlar": anlar, "eslesme": eslesme, "eksik": eksik,
            "eksik_kayit": eksik_kayit, "kanit": kanit, "hipotezler": hipotezler, "duzey": duzey,
            "guvenli_mod": gm, "cokme_paketleri": cokme_paketleri}


SONRAKI_TESTLER = (
    "1. Telefon bir sonraki kendiliğinden kapanıp açıldığında **hemen** aracı tekrar çalıştırın (kayıtlar halka tampondadır).\n"
    "2. Güvenli Mod'da en az normalde kapandığı kadar kullanın; sonucu `--guvenli-mod kapanmadi|kapandi` ile verin. "
    "Sonuç **olasılık** bildirir, kesinlik değil.\n"
    "3. Güvenli Mod'da kapanmıyorsa: yan yüklenen/son güncellenen uygulamaları birer birer kaldırıp her birinden sonra gözleyin.\n"
    "4. Güvenli Mod'da da kapanıyorsa: resmi sistem güncellemelerini kurun; şarjda/pilde, soğuk/sıcak, kılıfsız durumu karşılaştırın; "
    "kabarma varsa şarj etmeyin, servise götürün.\n"
    "5. Yedek alıp fabrika ayarından sonra uygulama kurmadan da sürerse: üçüncü taraf uygulamalar büyük ölçüde elenir; "
    "**sistem yazılımı/firmware/sürücü ile donanım hâlâ ayrılmamıştır** (fabrika ayarı firmware'i değiştirmez). "
    "Ayrım için servis: resmi firmware'in yeniden yüklenmesi ve donanım testi (pil ölçümü vb.).\n"
)


def rapor_yaz(oz: dict, d: dict, paylasim: bool, takma: TakmaAd) -> str:
    P = (lambda s: takma.metin(s)) if paylasim else (lambda s: s)

    def paket_sub(metin: str) -> str:
        return re.sub(r"\{P:([^}]+)\}", lambda m: f"`{takma.ad(m.group(1)) if paylasim else m.group(1)}`", metin)

    r = ["# Yeniden başlama teşhis raporu" + (" — PAYLAŞIM SÜRÜMÜ (maskelenmiş)" if paylasim else " — YEREL") + "\n\n"]
    if paylasim:
        r.append("Bu sürümde serbest metin maskelenmiş, üçüncü taraf paket adları takma adla (`uyg-xxxxxxxx`) "
                 "gösterilmiştir. Ham kayıt içermez.\n\n")
    else:
        r.append("Yerel sürüm: gerçek paket adlarını içerir; paylaşmak için `paylasim/` klasörünü kullanın.\n\n")
    c = oz["cihaz"]
    r.append(f"Cihaz saati: {oz['cihaz_zamani']} · Zaman penceresi: son {oz['pencere_gun']} gün\n\n")
    r.append("## 1. Cihaz\n\n")
    r.append(f"- Model: {c.get('ro.product.manufacturer', '?')} {c.get('ro.product.model', '?')}\n"
             f"- Android: {c.get('ro.build.version.release', '?')} (SDK {c.get('ro.build.version.sdk', '?')}), "
             f"güvenlik yaması {c.get('ro.build.version.security_patch', '?')}\n")
    if not paylasim:
        r.append(f"- Yapı: `{c.get('ro.build.fingerprint', '?')}`\n")
    r.append("\n## 2. Son açılış nedeni (AOSP canonical boot reason)\n\n")
    if "getprop" in d["eksik_kayit"]:
        r.append("- Açılış nedeni okunamadı (bkz. bölüm 3).\n")
    r.append(f"- `sys.boot.reason` = `{c.get('sys.boot.reason') or '—'}` → **{SINIF_ACIKLAMA[d['sinif']]}**\n"
             f"- `ro.boot.bootreason` = `{c.get('ro.boot.bootreason') or '—'}`\n"
             f"- önceki/kalıcı = `{c.get('sys.boot.reason.last') or c.get('persist.sys.boot.reason') or '—'}`\n")

    r.append("\n## 3. Veri tamlığı\n\n")
    if d["eksik"]:
        r.append("| Kayıt | Sorun | Etkilenen bilgi |\n|---|---|---|\n")
        r.extend(f"| {e['kayit']} | {e['tur']} | {e['etki']} |\n" for e in d["eksik"])
        r.append("\n**Eksik veri, ilgili arızanın olmadığı anlamına gelmez.** Bu alanlardaki 'kayıt yok' "
                 "ifadeleri 'toplanamadı' olarak okunmalıdır.\n")
    elif oz["toplama"]:
        r.append("Tüm adb komutları başarıyla tamamlandı.\n")
    else:
        r.append("Toplama durumu bilinmiyor (ham klasörden yeniden işlendi).\n")
    k = d["kanit"]
    r.append(f"\n- Olay zamanı: {'var' if k['olay_zamani'] else 'YOK'} · İlk hata zamanı: "
             f"{oz['crash_tamponu']['ilk_hata'] or 'YOK'} · Güvenli Mod karşılaştırması: "
             f"{'var' if k['karsilastirma'] else 'YOK'}\n")

    r.append(f"\n## 4. Hipotezler — kanıt düzeyi: **{d['duzey']}**\n\n")
    if d["hipotezler"]:
        for i, h in enumerate(d["hipotezler"], 1):
            r.append(f"**H{i}. {h['ad']}**\n")
            r.extend(f"- destek: {P(paket_sub(x))}\n" for x in h["destek"])
            r.append(f"- not: {h['not']}\n\n")
    else:
        r.append("Kayıtlar tek başına ayırt etmiyor; tek kesin neden ilan edilemez.\n\n")
    r.append(f"Güvenli Mod: {d['guvenli_mod']}\n")

    r.append("\n## 5. Olay zaman çizelgesi\n\n| Zaman | Kaynak | Olay |\n|---|---|---|\n")
    satirlar = [(o["zaman"], "dropbox", o["etiket"]) for o in oz["dropbox_olaylari"]]
    satirlar += [(z, "events", "boot_progress_start") for z in oz["cerceve_baslangiclari"]]
    satirlar += [(w["zaman"], "events", "watchdog") for w in oz["watchdog"]]
    if satirlar:
        r.extend(f"| {z} | {k_} | {e} |\n" for z, k_, e in sorted(satirlar)[-60:])
    elif {"dropbox_liste.txt", "logcat_events_boot.txt"} & d["eksik_kayit"]:
        r.append("| — | — | toplanamadı (bkz. bölüm 3) |\n")
    else:
        r.append("| — | — | pencerede kayıt yok |\n")

    ozet_satir: dict[str, int] = {}
    for o in oz["cokme_ozetleri"]:
        if paylasim:
            metin = paylasim_ipucu(o)
        else:
            metin = o["konu"] or (f"{o['istisna']}: {o['mesaj']}" if o["istisna"] else o["sinyal"])
        if metin:
            anahtar = f"{o['etiket']}: {metin}"
            ozet_satir[anahtar] = ozet_satir.get(anahtar, 0) + 1
    for w in oz["watchdog"]:
        anahtar = f"watchdog: {watchdog_kategori(w['konu']) if paylasim else w['konu']}"
        ozet_satir[anahtar] = ozet_satir.get(anahtar, 0) + 1
    if ozet_satir:
        baslik = ("İlk hata sınıfı / kategori ipuçları (yapısal; mesaj metni paylaşılmaz)" if paylasim
                  else "İlk istisna / konu ipuçları (maskelenmiş; YEREL — kişisel metin içerebilir)")
        r.append(f"\n### {baslik}\n\n| İpucu | Sayı |\n|---|---|\n")
        r.extend(f"| `{P(k_).replace('|', '/')}` | {v} |\n"
                 for k_, v in sorted(ozet_satir.items(), key=lambda x: -x[1])[:15])
    if oz["crash_tamponu"]["surecler"]:
        r.append("\n### crash tamponunda çöken süreçler\n\n| Süreç | Sayı |\n|---|---|\n")
        r.extend(f"| `{takma.ad(k_) if paylasim else k_}` | {v} |\n"
                 for k_, v in sorted(oz["crash_tamponu"]["surecler"].items(), key=lambda x: -x[1])[:15])

    r.append(f"\n## 6. Yeniden başlama anları ↔ önceki 72 saatte kurulan/güncellenen uygulamalar\n\n")
    if {"paketler_3.txt", "paket_zamani"} & d["eksik_kayit"]:
        r.append("Uygulama listesi/kurulum zamanları toplanamadı; karşılaştırma yapılamadı "
                 f"({len(d['anlar'])} yeniden başlama anı bulundu).\n")
    elif d["eslesme"]:
        r.append("| An | Öncesindeki kurulum/güncellemeler |\n|---|---|\n")
        for an, yakin in d["eslesme"][-30:]:
            liste = ", ".join(f"`{takma.ad(p['ad']) if paylasim else p['ad']}`" for p in yakin) or "—"
            r.append(f"| {an:%Y-%m-%d %H:%M:%S} | {liste} |\n")
        r.append("\nZamansal yakınlık nedensellik değildir.\n")
    else:
        r.append("Pencerede yeniden başlama anı bulunamadı.\n")

    ilgili = [p for p in oz["paketler"] if p["pencerede"] or p["yan_yukleme"]]
    r.append(f"\n## 7. İlgili üçüncü taraf uygulamalar (pencerede kurulan/güncellenen veya yan yüklenen; "
             f"{len(oz['paketler']) - len(ilgili)} diğer uygulama listelenmedi)\n\n")
    if ilgili:
        r.append("| Paket | Kaynak | İlk kurulum | Son güncelleme |\n|---|---|---|---|\n")
        for p in sorted(ilgili, key=lambda p: p["son"], reverse=True)[:40]:
            ad = takma.ad(p["ad"]) if paylasim else p["ad"]
            r.append(f"| `{ad}` | {'yan yükleme/adb' if p['yan_yukleme'] else 'mağaza/diğer'} | "
                     f"{p['ilk'] or '?'} | {p['son'] or '?'} |\n")
    else:
        r.append("—\n")

    pil = oz["pil"]
    r.append("\n## 8. Pil, ısı, depolama\n\n")
    if "battery.txt" in d["eksik_kayit"] or not pil:
        r.append("- Pil durumu okunamadı (eksik veri, pil sorunu olmadığı anlamına gelmez).\n")
    else:
        try:
            sic = f"{int(pil.get('temperature', '0')) / 10:.1f} °C"
        except ValueError:
            sic = "?"
        r.append(f"- Sağlık: {SAGLIK.get(pil.get('health', ''), pil.get('health', '?'))}; seviye %{pil.get('level', '?')}; "
                 f"sıcaklık {sic}; gerilim {pil.get('voltage', '?')} mV (Android'in bildirdiği değer; "
                 "kapasite/şişme ölçümü değildir)\n")
    if oz["termal_durum"]:
        r.append(f"- Termal durum kodu: {oz['termal_durum']}\n")
    if oz["data_doluluk"] is not None:
        r.append(f"- /data doluluk: %{oz['data_doluluk']}"
                 + (" — %90 üstü; Google'a göre %10'dan az boş alan sorun çıkarabilir" if oz["data_doluluk"] >= 90 else "") + "\n")
    r.append(f"- Önceki açılış logu (`logcat -L`): {'var' if oz['onceki_acilis']['var'] else 'yok/desteklenmiyor'}\n")
    r.append("\n## 9. Sonraki güvenli test sırası\n\n" + SONRAKI_TESTLER)
    return "".join(r)


# ------------------------------------------------------- paylaşım denetimi ---
DENETIM_DESENLERI = {
    "e-posta": re.compile(r"[\w.+-]+@[\w-]+(?:\.[\w-]+)+"),
    "uri": re.compile(r"\b(?:https?|ftp|content|file)://", re.I),
    "ip": re.compile(r"\b(?:\d{1,3}\.){3}\d{1,3}\b"),
    "mac": re.compile(r"\b(?:[0-9a-f]{2}[:-]){5}[0-9a-f]{2}\b", re.I),
    "telefon/imei": re.compile(r"(?<![\d-])\+?\d{10,17}(?![\d-])"),
    "kullanıcı yolu": re.compile(r"/storage/|/sdcard|/data/user/\d|/data/data/"),
    "yapı parmak izi": re.compile(r"\b\w+/\w+/\w+:\d+/"),
    "ipv6": IPV6,
    "windows/ev dizini yolu": re.compile(r"[A-Za-z]:\\|(?:/home|/Users|/root)/\w"),
    "json/anahtar sırrı": re.compile(r"[\"']?(?:password|passwd|secret|token|api[_-]?key|access[_-]?key|"
                                     r"authorization|cookie)[\"']?\s*[:=]", re.I),
    "bearer/basic kimlik": re.compile(r"\b(?:Bearer|Basic)\s+[\w.~+/=-]{6,}", re.I),
}
DENETIM_TEMIZ_METNI = ("Tanımlı örüntüler bulunmadı. (Bu, kişisel veri bulunmadığının garantisi değildir; "
                       "yalnız denetimdeki örüntü listesi aranmıştır.)")


def paylasim_denetle(metinler: dict[str, str], ozel: list[str] | None = None,
                     izinli: list[str] | None = None) -> list[str]:
    """Paylaşım dosyalarını geri okuyup kişisel veri / ham içerik sızıntısı arar.
    Bulguları dosya + kategori olarak döndürür (değeri tekrar yazmaz)."""
    bulgular = []
    for dosya, metin in metinler.items():
        for kategori, desen in DENETIM_DESENLERI.items():
            if desen.search(metin):
                bulgular.append(f"{dosya}: {kategori}")
        for deger in (ozel or []):
            if deger and deger in metin:
                bulgular.append(f"{dosya}: özel değer (seri/paket)")
        for ad in NOKTALI_AD.findall(metin):
            if ad in set(izinli or []):
                continue
            if not sistem_adi_mi(ad) and not re.fullmatch(r"[\w-]+\.(?:md|json|zip|txt|sha256|py)", ad):
                bulgular.append(f"{dosya}: maskelenmemiş noktalı ad")
                break
    return sorted(set(bulgular))


def _paylasim_ozeti(oz: dict, takma: TakmaAd) -> dict:
    """Paylaşım JSON'u: yalnız izinli, maskelenmiş ve takma adlı alanlar."""
    return {
        "surum": oz["surum"], "pencere_gun": oz["pencere_gun"], "cihaz_zamani": oz["cihaz_zamani"],
        "cihaz": {k: (v if re.fullmatch(r"[\w ,.()+-]{0,64}", v) else "<biçim-dışı>")
                  for k, v in oz["cihaz"].items() if k in PAYLASIM_PROP or "boot.reason" in k},
        "dropbox_olaylari": oz["dropbox_olaylari"],
        "cokme_ozetleri": [{"zaman": o["zaman"], "etiket": o["etiket"], "ipucu": paylasim_ipucu(o),
                            "paketler": [takma.ad(p) for p in o["paketler"]]}
                           for o in oz["cokme_ozetleri"]],
        "cerceve_baslangiclari": oz["cerceve_baslangiclari"],
        "watchdog": [{"zaman": w["zaman"], "kategori": watchdog_kategori(w["konu"])} for w in oz["watchdog"]],
        "crash_tamponu": {**oz["crash_tamponu"],
                          "surecler": {takma.ad(k): v for k, v in oz["crash_tamponu"]["surecler"].items()}},
        "pil": {k: v for k, v in oz["pil"].items() if re.fullmatch(r"[\w.-]{0,16}", v)},
        "termal_durum": oz["termal_durum"], "data_doluluk": oz["data_doluluk"],
        "onceki_acilis": oz["onceki_acilis"],
        "paketler": [{"ad": takma.ad(p["ad"]), "yan_yukleme": p["yan_yukleme"], "pencerede": p["pencerede"],
                      "ilk": p["ilk"], "son": p["son"]} for p in oz["paketler"]
                     if p["pencerede"] or p["yan_yukleme"]],
        # stderr metni (hata) paylaşılmaz; yalnız yapısal alanlar.
        "toplama": [{k: t[k] for k in ("ad", "cikis_kodu", "zaman_asimi", "izin_hatasi", "servis_yok", "sure_sn")
                     if k in t} for t in oz["toplama"]],
    }


PAYLASIM_ARTEFAKTLARI = ("paylasim", "paylasim.zip", "paylasim.zip.sha256")
GECICI_ONEK = ".paylasim-gecici-"


def paylasim_temizle(cikti: Path) -> None:
    """Bu çıktı klasöründeki tüm paylaşım artefaktlarını (eski ve yarım kalmış) siler."""
    for ad in PAYLASIM_ARTEFAKTLARI:
        p = cikti / ad
        if p.is_dir() and not p.is_symlink():
            shutil.rmtree(p)
        elif p.exists() or p.is_symlink():
            p.unlink()
    for p in cikti.glob(GECICI_ONEK + "*"):
        shutil.rmtree(p, ignore_errors=True)


def _zip_yaz(kaynak: Path, hedef: Path) -> None:
    with zipfile.ZipFile(hedef, "w", zipfile.ZIP_DEFLATED) as zf:
        for f in sorted(kaynak.iterdir()):
            zi = zipfile.ZipInfo(f"paylasim/{f.name}", date_time=(2026, 1, 1, 0, 0, 0))
            zi.compress_type = zipfile.ZIP_DEFLATED
            zf.writestr(zi, f.read_bytes())


def ciktilari_yaz(oz: dict, cikti: Path, guvenli_mod: str, goster: list[str],
                  ozel: list[str] | None = None) -> tuple[Path | None, list[str]]:
    """Yerel rapor + paylaşım klasörü + ZIP/SHA yazar.

    Paylaşım tarafı: (1) eski paylaşım artefaktları önce silinir; (2) yeni paket geçici
    klasörde üretilir; (3) dosyalar diskten, ZIP içeriği ZIP'ten, SHA dosyası diskten geri
    okunup denetlenir; (4) ancak hepsi geçerse os.replace ile yerine taşınır (SHA en son).
    Herhangi bir hata/bulgu durumunda bu klasörde hiçbir paylaşım artefaktı kalmaz."""
    cikti.mkdir(parents=True, exist_ok=True)
    paylasim_temizle(cikti)  # önceki çalıştırmanın paketi asla "teslim" gibi kalmasın
    d = degerlendir(oz, guvenli_mod)
    takma = TakmaAd(goster)
    (cikti / "ozet-yerel.json").write_text(json.dumps(oz, ensure_ascii=False, indent=1), encoding="utf-8")
    (cikti / "rapor-yerel.md").write_text(rapor_yaz(oz, d, False, takma), encoding="utf-8")

    gizli_paketler = [p["ad"] for p in oz["paketler"] if p["ad"] not in set(goster) and not sistem_adi_mi(p["ad"])]
    gizli_paketler += [p for o in oz["cokme_ozetleri"] for p in o["paketler"]
                       if not sistem_adi_mi(p) and p not in set(goster)]
    gizli_paketler += [p for p in oz["crash_tamponu"]["surecler"] if not sistem_adi_mi(p.split(":")[0])
                       and p.split(":")[0] not in set(goster)]
    ozel_tum = [x for x in (ozel or []) + gizli_paketler if x]

    gec = Path(tempfile.mkdtemp(prefix=GECICI_ONEK, dir=cikti))
    try:
        pk = gec / "paylasim"
        pk.mkdir()
        (pk / "rapor-paylasim.md").write_text(rapor_yaz(oz, d, True, takma), encoding="utf-8")
        (pk / "ozet-paylasim.json").write_text(json.dumps(_paylasim_ozeti(oz, takma), ensure_ascii=False,
                                                          indent=1), encoding="utf-8")
        bulgular = paylasim_denetle({f.name: f.read_text(encoding="utf-8") for f in pk.iterdir()},
                                    ozel_tum, goster)
        if bulgular:
            return None, bulgular
        z, shaf = gec / "paylasim.zip", gec / "paylasim.zip.sha256"
        _zip_yaz(pk, z)
        shaf.write_text(f"{hashlib.sha256(z.read_bytes()).hexdigest()}  paylasim.zip\n", encoding="utf-8")
        with zipfile.ZipFile(z) as zf:
            if zf.testzip() is not None:
                return None, ["ZIP bütünlük denetimi başarısız"]
            icerik = {n: zf.read(n).decode("utf-8") for n in zf.namelist()}
        bulgular = paylasim_denetle(icerik, ozel_tum, goster)
        if bulgular:
            return None, bulgular
        if shaf.read_text(encoding="utf-8").split()[0] != hashlib.sha256(z.read_bytes()).hexdigest():
            return None, ["SHA geri okuma uyuşmadı"]
        os.replace(pk, cikti / "paylasim")
        os.replace(z, cikti / "paylasim.zip")
        os.replace(shaf, cikti / "paylasim.zip.sha256")  # SHA'nın varlığı = tamamlanmış paket
        return cikti / "paylasim.zip", []
    except BaseException:
        paylasim_temizle(cikti)
        raise
    finally:
        shutil.rmtree(gec, ignore_errors=True)


# --------------------------------------------------------------------- CLI ---
def ham_klasor_oku(k: Path) -> dict[str, str]:
    return {ad: (k / ad).read_text(encoding="utf-8", errors="replace") for ad in HAM_DOSYALAR if (k / ad).exists()}


def main(argv=None):
    # Konsol/boru kod sayfası (ör. Windows cp1254) UTF-8 dışı olabilir: yazdırma asla çökmesin.
    for akis in (sys.stdout, sys.stderr):
        try:
            akis.reconfigure(errors="replace")
        except (AttributeError, ValueError):
            pass
    ap = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    alt = ap.add_subparsers(dest="komut", required=True)
    for ad in ("hepsi", "isle"):
        s = alt.add_parser(ad)
        if ad == "isle":
            s.add_argument("ham_klasor")
            s.add_argument("--simdi", default=None, help="Pencere bitişi (YYYY-MM-DD HH:MM:SS)")
        else:
            s.add_argument("--seri", default=None)
            s.add_argument("--zaman-asimi", type=int, default=120)
            s.add_argument("--servis-bekle", type=int, default=120)
            s.add_argument("--ham-sakla", action="store_true",
                           help="Ham kayıtları yerel-ham/ altına yaz (paylaşıma girmez)")
        s.add_argument("--cikti", default=f"reboot-kayit-{dt.datetime.now():%Y%m%d-%H%M%S}")
        s.add_argument("--gun", type=int, default=7)
        s.add_argument("--guvenli-mod", choices=["bilinmiyor", "kapanmadi", "kapandi"], default="bilinmiyor")
        s.add_argument("--goster", action="append", default=[],
                       help="Paylaşımda açık gösterilecek paket (örn. kendi uygulamanız)")
    s = alt.add_parser("denetle")
    s.add_argument("yol")
    a = ap.parse_args(argv)

    if a.komut == "denetle":
        y = Path(a.yol)
        if y.suffix == ".zip":
            with zipfile.ZipFile(y) as zf:
                metinler = {n: zf.read(n).decode("utf-8", "replace") for n in zf.namelist()}
        else:
            metinler = {f.name: f.read_text(encoding="utf-8", errors="replace") for f in y.iterdir() if f.is_file()}
        b = paylasim_denetle(metinler)
        print(DENETIM_TEMIZ_METNI if not b else "BULGU:\n" + "\n".join(b))
        return 0 if not b else 3

    cikti = Path(a.cikti)
    ozel: list[str] = []
    if a.komut == "isle":
        simdi = _tarih(a.simdi) if a.simdi else None
        oz = ham_isle(ham_klasor_oku(Path(a.ham_klasor)), a.gun, simdi=simdi)
    else:
        try:
            taban = adb_komutu()
        except FileNotFoundError:
            print("HATA: 'adb' bulunamadı. Platform-Tools: https://developer.android.com/tools/releases/platform-tools",
                  file=sys.stderr)
            return 2
        except ValueError as e:
            print(f"HATA: {e}; gerçek adb'ye düşülmedi.", file=sys.stderr)
            return 2
        adb = Adb(None, a.zaman_asimi, taban=taban)
        s_, liste = adb.calistir("devices", ["devices", "-l"], kaydet=False)
        if not s_.basarili:
            print(f"HATA: `adb devices -l` başarısız ({s_.hata or s_.cikis_kodu}).", file=sys.stderr)
            return 2
        try:
            seri = cihaz_sec(cihazlari_ayristir(liste), a.seri)
        except ValueError as e:
            print(f"HATA: {e}", file=sys.stderr)
            return 2
        ozel = [seri]
        adb = Adb(seri, a.zaman_asimi, gizli=ozel, taban=taban)
        _, aid = adb.calistir("android_id", ["shell", "settings get secure android_id"], kaydet=False)
        if aid.strip() and "null" not in aid:
            ozel.append(aid.strip())
        ham = topla_ham(adb, a.servis_bekle)
        if a.ham_sakla:
            hk = cikti / "yerel-ham"
            hk.mkdir(parents=True, exist_ok=True)
            for ad, metin in ham.items():
                (hk / ad).write_text(metin, encoding="utf-8")
            print(f"UYARI: ham kayıtlar {hk} altına yazıldı; kişisel veri içerebilir, PAYLAŞMAYIN.")
        oz = ham_isle(ham, a.gun, gizli=ozel, toplama=[asdict(x) for x in adb.sonuclar])
        del ham
    z, bulgular = ciktilari_yaz(oz, cikti, a.guvenli_mod, a.goster, ozel)
    print(f"Yerel rapor (kişisel veri içerir, paylaşmayın): {cikti / 'rapor-yerel.md'}")
    if z:
        print(f"Paylaşım paketi: {z}\nSHA-256: {(cikti / 'paylasim.zip.sha256').read_text(encoding='utf-8').split()[0]}\n"
              f"Geri okuma denetimi: {DENETIM_TEMIZ_METNI}")
        return 0
    print("Paylaşım paketi ÜRETİLMEDİ (eski paylaşım dosyaları da silindi); geri okuma denetiminde bulgu:\n  "
          + "\n  ".join(bulgular), file=sys.stderr)
    return 4


if __name__ == "__main__":
    sys.exit(main())
