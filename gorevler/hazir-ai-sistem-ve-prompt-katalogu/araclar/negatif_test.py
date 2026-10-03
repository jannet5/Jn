#!/usr/bin/env python3
"""Lisans denetiminin gerçekten hata yakaladığını kanıtlar.

Paketin geçici bir kopyasına bilerek 4 ihlal koyar ve dogrula.lisans_kontrol()'ün
dördünü de bulmasını bekler. Asıl dosyalara dokunmaz. Çıkış kodu 0 = denetim çalışıyor.
"""
import importlib.util
import json
import shutil
import sys
import tempfile
from pathlib import Path

KOK = Path(__file__).resolve().parent.parent
BEKLENEN = {
    "izinsiz lisans": "izinli olmayan lisanstan",
    "eksik alan": "lisans alanı boş: sahip",
    "tescilli kopyalanabilir": "özgün açıklama kopyalanabilir işaretli",
    "yasak metin": "çıkarılması gereken metin hâlâ var",
}


def main() -> int:
    with tempfile.TemporaryDirectory() as t:
        kopya = Path(t) / "paket"
        shutil.copytree(KOK, kopya, ignore=shutil.ignore_patterns("*.png"))
        p = kopya / "veri" / "katalog.json"
        d = json.loads(p.read_text(encoding="utf-8"))
        g = {x["id"]: x for x in d["girdiler"]}
        g["fabric"]["ornek"]["kopya"]["lisans"] = "GPL-3.0"
        del g["spec-kit"]["ornek"]["kopya"]["sahip"]
        g["x1xhlol"]["ornek"]["kopya"]["kopyalanabilir"] = True
        p.write_text(json.dumps(d, ensure_ascii=False), encoding="utf-8")
        with (kopya / "KATALOG.md").open("a", encoding="utf-8") as f:
            f.write("\nsemantic search that finds code by meaning\n")
        spec = importlib.util.spec_from_file_location("dogrula", kopya / "araclar" / "dogrula.py")
        mod = importlib.util.module_from_spec(spec)
        spec.loader.exec_module(mod)
        hatalar, _, _ = mod.lisans_kontrol()
    sonuc = {ad: any(ip in h for h in hatalar) for ad, ip in BEKLENEN.items()}
    for ad, ok in sonuc.items():
        print(f"{'YAKALANDI' if ok else 'KAÇTI'}: {ad}")
    print(f"toplam hata satırı: {len(hatalar)}")
    return 0 if all(sonuc.values()) else 1


if __name__ == "__main__":
    sys.exit(main())
