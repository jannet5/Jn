#!/usr/bin/env python3
"""Teslim dosyalarının kabul doğrulaması.

Kontroller:
  1. Zorunlu dosyalar ve bölüm başlıkları var mı (görevdeki 3 zorunlu kapsam).
  2. Tüm bağlantılar HTTPS mi (http:// yasak).
  3. liste.md'deki her satırda fiyat + HTTPS bağlantı var mı.
  4. Özel kaynak metni sızmış mı (--ozel ile verilen dosyadan uzun cümle parçaları aranır).
  5. --ag verilirse her bağlantıya HEAD/GET atılıp durum kodu raporlanır (bilgi amaçlı;
     bot koruması 403 döndüren siteler hata sayılmaz, ayrı listelenir).
Çıkış kodu: 0 = kabul, 1 = ret.
"""
import argparse, json, pathlib, re, sys, urllib.request, urllib.error, ssl, os

KOK = pathlib.Path(__file__).resolve().parent.parent
ZORUNLU = {
    "harita.md": ["## 1. Hedef", "## 2. Bağımlılıklar", "## 3. A / B / C yolları"],
    "rapor.md": ["## 1. İhtiyaç profili", "## 3. OMEN: Ekşi Sözlük ve Reddit",
                 "## 4. Alternatifler", "## 5. Güncel fiyatlar", "## 6. Karar",
                 "## 7. Yapılamayanlar"],
    "liste.md": ["# "],
    "kaynaklar.md": ["# "],
    "calisma-gunlugu.md": ["# "],
}
URL_RE = re.compile(r"\b(?:https?)://[^\s)>\]\"'`|]+")

def hata(msgs, m):
    msgs.append(m); print("RET  :", m)

def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--ozel", help="Sızıntı kontrolü için özel kaynak dosyası")
    ap.add_argument("--ag", action="store_true", help="Bağlantılara ağ isteği at")
    ap.add_argument("--rapor", help="Ağ sonuçlarını JSON olarak yaz")
    a = ap.parse_args()
    hatalar, tum_url = [], set()

    for ad, basliklar in ZORUNLU.items():
        p = KOK / ad
        if not p.exists():
            hata(hatalar, f"{ad} yok"); continue
        t = p.read_text(encoding="utf-8")
        for b in basliklar:
            if b not in t:
                hata(hatalar, f"{ad}: '{b}' bölümü yok")
        for u in URL_RE.findall(t):
            u = u.rstrip(".,;:")
            if u.startswith("http://"):
                hata(hatalar, f"{ad}: HTTPS olmayan bağlantı {u}")
            tum_url.add(u)
    print(f"OK   : {len(tum_url)} benzersiz bağlantı, hepsi https kontrol edildi")

    liste = (KOK / "liste.md").read_text(encoding="utf-8")
    satirlar = [s for s in liste.splitlines() if s.startswith("- **")]
    if len(satirlar) < 3:
        hata(hatalar, "liste.md: en az 3 ürün satırı bekleniyordu")
    for s in satirlar:
        if "TL" not in s or "https://" not in s:
            hata(hatalar, f"liste.md satırında fiyat/bağlantı eksik: {s[:60]}")
    print(f"OK   : liste.md {len(satirlar)} ürün satırı")

    if a.ozel:
        ozel = pathlib.Path(a.ozel).read_text(encoding="utf-8")
        parcalar = [c.strip() for c in re.split(r"[.?!\n]", ozel) if len(c.strip()) > 40]
        for p in KOK.rglob("*"):
            if p.is_file() and p.suffix in {".md", ".py", ".txt", ".html"}:
                t = p.read_text(encoding="utf-8", errors="ignore")
                for c in parcalar:
                    if c in t:
                        hata(hatalar, f"Özel kaynak cümlesi sızmış: {p.name}: {c[:50]}…")
        print(f"OK   : sızıntı taraması ({len(parcalar)} cümle parçası)")

    if a.ag:
        ctx = ssl.create_default_context(cafile=os.environ.get("SSL_CERT_FILE") or None)
        sonuc = {}
        for u in sorted(tum_url):
            try:
                r = urllib.request.Request(u, headers={"User-Agent": "Mozilla/5.0"})
                with urllib.request.urlopen(r, timeout=20, context=ctx) as y:
                    sonuc[u] = y.status
            except urllib.error.HTTPError as e:
                sonuc[u] = e.code
            except Exception as e:
                sonuc[u] = f"hata:{type(e).__name__}"
        ok = sum(1 for v in sonuc.values() if v == 200)
        print(f"BİLGİ: ağ kontrolü {ok}/{len(sonuc)} bağlantı 200 döndü")
        for u, v in sonuc.items():
            if v != 200:
                print(f"       {v}  {u}")
        if a.rapor:
            pathlib.Path(a.rapor).write_text(json.dumps(sonuc, indent=1, ensure_ascii=False), encoding="utf-8")
    print("SONUÇ:", "KABUL" if not hatalar else f"RET ({len(hatalar)} hata)")
    return 1 if hatalar else 0

if __name__ == "__main__":
    sys.exit(main())
