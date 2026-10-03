#!/usr/bin/env python3
"""Drive denetim aracı — Google Drive for desktop (G:) kullanımı için yerel kontrol.

Yalnız okuma yapar; hiçbir dosyayı silmez, taşımaz, ağa bağlanmaz.

Alt komutlar:
  yol <yol>          Yolun hangi "dünyada" olduğunu söyler: yerel disk mi,
                     Drive sanal sürücüsü mü; web AI'nın bu yolu göremeyeceğini
                     ve doğru erişim yolunu (yükleme / bağlayıcı / yerel araç) yazar.
  tara <kök>         Kök altında aktif geliştirme klasörlerini bulur
                     (node_modules, .gradle, build, .git, .venv ...), boyut ve
                     dosya sayısıyla raporlar; Drive içindeyse risk notu düşer.
  onbellek           DriveFS önbellek klasörünün (varsayılan konum) boyutunu ölçer.
  olc <dosya>        Sıralı ve rastgele okuma hızını ölçer (SSD vs. akış farkı).

Standart kütüphane dışında bağımlılık yoktur (Python 3.8+).
"""
from __future__ import annotations

import argparse
import json
import os
import random
import sys
import time
from dataclasses import dataclass, asdict, field
from pathlib import Path, PureWindowsPath
from typing import Iterable, List, Optional

# Aktif çalışma klasörleri: çok sayıda küçük dosya, sık yazma, kilit dosyaları.
# Değer: (tür, öneri)
AKTIF_KLASORLER = {
    "node_modules": ("npm/pnpm/yarn bağımlılıkları",
                     "Drive dışında (C:\\dev\\...) tut; package.json + lock dosyası yeter, `npm ci` ile yeniden üretilir."),
    ".gradle": ("Gradle proje önbelleği / GRADLE_USER_HOME",
                "Yerel diskte tut; GRADLE_USER_HOME'u senkron/ağ diskine yönlendirme (kilit dosyaları; Gradle forumunda önerilmiyor)."),
    "build": ("derleme çıktısı (Gradle/Android/genel)",
              "Yeniden üretilebilir; Drive'a yalnız son APK/ZIP gibi ürünleri kopyala."),
    ".git": ("Git deposu iç verisi",
             "Senkronize klasörde canlı depo tutma; uzak depoya push et veya `git bundle` dosyasını Drive'a koy."),
    ".venv": ("Python sanal ortamı", "Yerel diskte tut; requirements.txt / pyproject yeterli."),
    "venv": ("Python sanal ortamı", "Yerel diskte tut; requirements.txt / pyproject yeterli."),
    "__pycache__": ("Python bayt kodu önbelleği", "Yeniden üretilebilir; senkronize etmeye gerek yok."),
    "target": ("Maven/Cargo derleme çıktısı", "Yeniden üretilebilir; yerel diskte tut."),
    ".next": ("Next.js derleme önbelleği", "Yeniden üretilebilir; yerel diskte tut."),
    "dist": ("paket/derleme çıktısı", "Son ürünse arşivle; ara çıktıysa yerelde tut."),
    ".cache": ("araç önbelleği", "Yeniden üretilebilir; yerel diskte tut."),
}

GOOGLE_KISAYOL_UZANTILARI = (".gdoc", ".gsheet", ".gslides", ".gdraw", ".gform")

WEB_AI_NOTU = (
    "Bir web sohbeti (ChatGPT/Claude/Gemini/Grok) bu yolu kendiliğinden açamaz: yol yalnız dosyanın "
    "bulunduğu makinenin dosya sisteminde anlamlıdır. Web/bulut AI dosyaya ancak şu yollarla ulaşır: "
    "(1) dosyayı sohbete yüklemek, (2) servisin Google Drive bağlayıcısı — OAuth, yalnız senin Drive "
    "izinlerin kadar, (3) bu makinede çalışan ve o klasöre izin verilmiş bir yerel ajan/köprü (masaüstü "
    "uygulaması, yerel MCP dosya sunucusu vb.), (4) herkese açık link — ancak servis linki açabiliyorsa. "
    "Yerel araçlar (Cursor, Claude Code CLI/Desktop, Python, ComfyUI) kendi izin/sandbox ayarları elverdiği "
    "sürece bu yolu doğrudan açabilir."
)


@dataclass
class YolRaporu:
    yol: str
    windows_bicimi: bool
    surucu: Optional[str]
    drive_sanal_surucu_olasi: bool
    drive_isareti: List[str]
    bu_makinede_var: bool
    birim_etiketi: Optional[str] = None
    dosya_sistemi: Optional[str] = None
    web_ai_erisimi: str = WEB_AI_NOTU
    yerel_arac_erisimi: str = ""


def _windows_birim_bilgisi(kok: str):
    """Windows'ta GetVolumeInformationW ile etiket ve dosya sistemi adını döndürür."""
    if os.name != "nt":
        return None, None
    try:
        import ctypes
        etiket = ctypes.create_unicode_buffer(261)
        fs = ctypes.create_unicode_buffer(261)
        ok = ctypes.windll.kernel32.GetVolumeInformationW(
            ctypes.c_wchar_p(kok), etiket, 261, None, None, None, fs, 261)
        if ok:
            return etiket.value, fs.value
    except Exception:  # pragma: no cover - yalnız Windows
        pass
    return None, None


def yol_siniflandir(yol: str, var_mi=os.path.exists, birim_bilgisi=_windows_birim_bilgisi) -> YolRaporu:
    """Bir yolu sınıflandırır. var_mi ve birim_bilgisi testte değiştirilebilir."""
    windows_bicimi = len(yol) >= 2 and yol[1] == ":" and yol[0].isalpha()
    surucu = yol[0].upper() + ":" if windows_bicimi else None
    isaretler: List[str] = []
    parcalar = [p.lower() for p in (PureWindowsPath(yol).parts if windows_bicimi else Path(yol).parts)]
    for anahtar in ("my drive", "drive'ım", "shared drives", "ortak drive'lar", "google drive",
                    "googledrive", "cloudstorage"):
        if any(anahtar in p for p in parcalar):
            isaretler.append(f"yol parçası '{anahtar}' içeriyor")
    etiket = fs = None
    if surucu:
        etiket, fs = birim_bilgisi(surucu + "\\")
        if etiket and "google drive" in etiket.lower():
            isaretler.append(f"birim etiketi '{etiket}'")
        if surucu == "G:":
            isaretler.append("G: Drive for desktop'ın varsayılan sürücü harfi (değiştirilebilir)")
    if yol.lower().endswith(GOOGLE_KISAYOL_UZANTILARI):
        isaretler.append("Google Docs/Sheets/Slides dosyası: Google'a göre tarayıcıda açılır; yerel araç bu "
                         "dosyadan belge metnini okuyamayabilir (doğrulanmadı) — dışa aktar veya bağlayıcı kullan")
    var = bool(var_mi(yol))
    olasi = bool(isaretler)
    if olasi:
        yerel = ("Yerel araçlar açabilir AMA şartlar: Drive for desktop çalışıyor, oturum açık; akış "
                 "modunda dosya çevrimdışı değilse internet gerekir (ilk açılışta indirilir). Video, "
                 "büyük ZIP, rastgele okuma ve çok küçük dosya yükünde SSD'den yavaş/kırılgan olabilir.")
    else:
        yerel = "Yerel disk yolu: yerel araçlar, izin/sandbox ayarları elverdiği sürece doğrudan ve yerel disk hızında açar."
    return YolRaporu(yol=yol, windows_bicimi=windows_bicimi, surucu=surucu,
                     drive_sanal_surucu_olasi=olasi, drive_isareti=isaretler,
                     bu_makinede_var=var, birim_etiketi=etiket, dosya_sistemi=fs,
                     yerel_arac_erisimi=yerel)


@dataclass
class AktifKlasor:
    yol: str
    ad: str
    tur: str
    oneri: str
    dosya_sayisi: int
    bayt: int


@dataclass
class TaramaRaporu:
    kok: str
    drive_icinde_olasi: bool
    bulunan: List[AktifKlasor] = field(default_factory=list)
    toplam_dosya: int = 0
    toplam_bayt: int = 0
    risk: str = ""


def _klasor_olc(yol: Path):
    sayi = bayt = 0
    for kok, _dirs, dosyalar in os.walk(yol, followlinks=False):
        for d in dosyalar:
            sayi += 1
            try:
                bayt += os.lstat(os.path.join(kok, d)).st_size
            except OSError:
                pass
    return sayi, bayt


def tara(kok: str, drive_icinde: Optional[bool] = None) -> TaramaRaporu:
    kokp = Path(kok)
    if drive_icinde is None:
        drive_icinde = yol_siniflandir(str(kokp.resolve())).drive_sanal_surucu_olasi
    rapor = TaramaRaporu(kok=str(kokp), drive_icinde_olasi=drive_icinde)
    for mevcut, dirs, _dosyalar in os.walk(kokp, followlinks=False):
        bulunanlar = [d for d in dirs if d in AKTIF_KLASORLER]
        for d in sorted(bulunanlar):
            p = Path(mevcut) / d
            sayi, bayt = _klasor_olc(p)
            tur, oneri = AKTIF_KLASORLER[d]
            rapor.bulunan.append(AktifKlasor(str(p), d, tur, oneri, sayi, bayt))
            rapor.toplam_dosya += sayi
            rapor.toplam_bayt += bayt
        # Bulunan aktif klasörlerin içine inme (iç içe node_modules sayımı şişirmesin).
        dirs[:] = [d for d in dirs if d not in AKTIF_KLASORLER]
    if not rapor.bulunan:
        rapor.risk = "Aktif geliştirme klasörü yok: arşiv/depolama için uygun."
    elif drive_icinde:
        rapor.risk = (f"YÜKSEK: Drive içinde {len(rapor.bulunan)} aktif klasör, {rapor.toplam_dosya} dosya. "
                      "Drive for desktop'ın resmi ayar sayfasında alt klasör/desen hariç tutma ayarı bulunamadı (2026-10-03 kontrolü); "
                      "her küçük yazma senkron kuyruğuna girebilir. "
                      "Projeyi yerel diske taşı, Drive'a yalnız kaynak arşivi/bundle koy.")
    else:
        rapor.risk = ("Yerel diskte aktif klasörler var (normal). Drive'a kopyalarken bunları dışarıda bırak "
                      "(ör. `git bundle` veya kaynak ZIP).")
    return rapor


def onbellek_yolu(env=os.environ, platform=sys.platform) -> Optional[Path]:
    if platform.startswith("win"):
        la = env.get("LOCALAPPDATA")
        return Path(la) / "Google" / "DriveFS" if la else None
    if platform == "darwin":
        return Path(env.get("HOME", "~")).expanduser() / "Library" / "Application Support" / "Google" / "DriveFS"
    return None


def onbellek_raporu(yol: Optional[Path] = None) -> dict:
    yol = yol or onbellek_yolu()
    if yol is None:
        return {"durum": "desteklenmeyen platform (Drive for desktop yalnız Windows/macOS)", "yol": None}
    if not yol.exists():
        return {"durum": "bulunamadı (Drive for desktop kurulu değil veya ContentCachePath değiştirilmiş)",
                "yol": str(yol)}
    sayi, bayt = _klasor_olc(yol)
    return {"durum": "bulundu", "yol": str(yol), "dosya_sayisi": sayi, "bayt": bayt,
            "insan": insan_boyut(bayt),
            "not": "Bu alan SSD'den düşer. Akış modunda yalnız açılan/çevrimdışı dosyalar + yüklenmeyi bekleyenler buradadır."}


def olc(dosya: str, blok: int = 1 << 20, rastgele_okuma: int = 64, rastgele_blok: int = 4096,
        tohum: int = 1) -> dict:
    p = Path(dosya)
    boyut = p.stat().st_size
    t0 = time.perf_counter()
    okunan = 0
    with open(p, "rb", buffering=0) as f:
        while True:
            b = f.read(blok)
            if not b:
                break
            okunan += len(b)
    sirali_sn = max(time.perf_counter() - t0, 1e-9)
    rnd = random.Random(tohum)
    t1 = time.perf_counter()
    with open(p, "rb", buffering=0) as f:
        for _ in range(rastgele_okuma):
            f.seek(rnd.randrange(0, max(boyut - rastgele_blok, 1)))
            f.read(rastgele_blok)
    rastgele_sn = max(time.perf_counter() - t1, 1e-9)
    return {
        "dosya": str(p), "bayt": boyut,
        "sirali_MBps": round(okunan / sirali_sn / 1e6, 2),
        "rastgele_okuma_sayisi": rastgele_okuma,
        "rastgele_ort_ms": round(rastgele_sn / rastgele_okuma * 1000, 3),
        "not": ("İlk ölçüm 'soğuk' (Drive'dan indirme dahil), ikinci ölçüm önbellekten gelir. "
                "SSD ile G: arasında aynı dosyayı iki kez ölçüp karşılaştır."),
    }


def insan_boyut(b: int) -> str:
    x = float(b)
    for birim in ("B", "KB", "MB", "GB", "TB"):
        if x < 1024 or birim == "TB":
            return f"{x:.1f} {birim}"
        x /= 1024
    return f"{x:.1f} TB"


def _yaz(veri, as_json: bool, out=None):
    out = out or sys.stdout
    if as_json:
        print(json.dumps(veri, ensure_ascii=False, indent=2, default=lambda o: asdict(o)), file=out)
        return
    if isinstance(veri, YolRaporu):
        d = asdict(veri)
        print(f"Yol: {d['yol']}", file=out)
        print(f"  Bu makinede var mı: {'evet' if d['bu_makinede_var'] else 'HAYIR'}", file=out)
        print(f"  Drive sanal sürücüsü olası: {'evet' if d['drive_sanal_surucu_olasi'] else 'hayır'}", file=out)
        for i in d["drive_isareti"]:
            print(f"    - {i}", file=out)
        if d["birim_etiketi"] or d["dosya_sistemi"]:
            print(f"  Birim: etiket={d['birim_etiketi']} fs={d['dosya_sistemi']}", file=out)
        print(f"  Yerel araç: {d['yerel_arac_erisimi']}", file=out)
        print(f"  Web AI: {d['web_ai_erisimi']}", file=out)
    elif isinstance(veri, TaramaRaporu):
        print(f"Kök: {veri.kok}  (Drive içinde olası: {'evet' if veri.drive_icinde_olasi else 'hayır'})", file=out)
        for k in veri.bulunan:
            print(f"  [{k.ad}] {k.yol}  — {k.dosya_sayisi} dosya, {insan_boyut(k.bayt)}  ({k.tur})", file=out)
            print(f"      öneri: {k.oneri}", file=out)
        print(f"  Toplam: {veri.toplam_dosya} dosya, {insan_boyut(veri.toplam_bayt)}", file=out)
        print(f"  Risk: {veri.risk}", file=out)
    else:
        for k, v in veri.items():
            print(f"{k}: {v}", file=out)


def main(argv: Optional[Iterable[str]] = None) -> int:
    ap = argparse.ArgumentParser(prog="drive_denetim", description=__doc__.splitlines()[0])
    ap.add_argument("--json", action="store_true", help="JSON çıktı")
    alt = ap.add_subparsers(dest="komut", required=True)
    a = alt.add_parser("yol"); a.add_argument("yol")
    a = alt.add_parser("tara"); a.add_argument("kok")
    a.add_argument("--drive", choices=["otomatik", "evet", "hayir"], default="otomatik",
                   help="kökün Drive içinde olup olmadığını zorla")
    alt.add_parser("onbellek")
    a = alt.add_parser("olc"); a.add_argument("dosya")
    a.add_argument("--rastgele", type=int, default=64)
    ns = ap.parse_args(list(argv) if argv is not None else None)

    if ns.komut == "yol":
        _yaz(yol_siniflandir(ns.yol), ns.json)
    elif ns.komut == "tara":
        if not Path(ns.kok).is_dir():
            print(f"Hata: klasör yok: {ns.kok}", file=sys.stderr)
            return 2
        zorla = {"otomatik": None, "evet": True, "hayir": False}[ns.drive]
        _yaz(tara(ns.kok, zorla), ns.json)
    elif ns.komut == "onbellek":
        _yaz(onbellek_raporu(), ns.json)
    elif ns.komut == "olc":
        if not Path(ns.dosya).is_file():
            print(f"Hata: dosya yok: {ns.dosya}", file=sys.stderr)
            return 2
        _yaz(olc(ns.dosya, rastgele_okuma=ns.rastgele), ns.json)
    return 0


if __name__ == "__main__":
    sys.exit(main())
