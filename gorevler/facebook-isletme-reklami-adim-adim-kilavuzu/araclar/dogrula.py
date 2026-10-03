#!/usr/bin/env python3
"""Kılavuzun kabul doğrulaması.

1) KILAVUZ.md ve kaynaklar.md'deki tüm HTTPS bağlantılarını canlı kontrol eder.
2) Meta yardım makalelerini tr_TR sürümüyle indirip başlığı ve kılavuzda dayandığımız
   kilit ifadeleri sayfa metninde arar.
3) Görev kabul maddelerinin (akış / alternatif yol / resmi doğrulama) kılavuzda karşılığı
   olup olmadığını yapısal olarak denetler.

Kullanım:  python3 araclar/dogrula.py [--rapor rapor.md]
Sadece standart kütüphane + sistem curl'ü kullanır (proxy/CA ayarları curl'den gelir).
"""
import html
import json
import os
import re
import subprocess
import sys
from pathlib import Path

KOK = Path(__file__).resolve().parent.parent
KILAVUZ = (KOK / "KILAVUZ.md").read_text(encoding="utf-8")
KAYNAK = (KOK / "kaynaklar.md").read_text(encoding="utf-8")

# Meta makalesi -> kılavuzda dayandığımız, sayfada bulunması gereken ifadeler (tr_TR veya en_US)
IDDIALAR = {
    "1710077379203657": ["İşletme portfolyosu oluştur", "2"],
    "915885887059947": ["Yeni bir reklam hesabı oluştur", "3'e çıkar"],
    "132073386867900": ["Ödeme yöntemi ekle", "1359188"],
    "350519228479855": ["Para ekle", "Apple"],
    "1238737454289085": ["Türkiye: %5"],
    "190490051321426": ["%75", "yedi katından"],
    "112167992830700": ["50 sonuç"],
    "204798856225114": ["24 saat"],
    "1210227555661027": ["48 saat", "Değerlendirme talep et"],
    "1438417719786914": ["Bilinirlik", "Trafik", "Etkileşim", "Potansiyel müşteriler",
                         "Uygulama tanıtımı", "Satışlar"],
    "1575107409431290": ["Öne çıkarılamıyor", "Kapak"],
    "347839548598012": ["Reklam merkezi", "Facebook içeriğini öne çıkar", "%30"],
    "125897810822573": ["1885029", "1443121", "1815430", "1487470"],
    "422289316306981": ["Neler yapabilirsin?"],
    "1658289035439772": ["+ Create", "Special Ad Category", "may vary"],
    "988356550342487": ["Publish", "24 hours"],
    "563129151097553": ["Mevcut bakiye"],
    "268196136699959": ["CVV", "Şimdi öde"],
}

KABUL = {
    "Başlangıçtan yayına akış": [r"## 3\. Hazırlık", r"## 4\. Yol B", r"## 5\. Yol B",
                                 r"\*\*Yayınla\*\*", r"## 6\. Yayından sonra"],
    "Farklı ekranda alternatif yol": [r"➜ Farklıysa", r"## 7\. Yol A", r"## 8\. Yol C",
                                      r"## 10\. Sorun giderme"],
    "Güncel arayüz ve resmi kural doğrulaması": [r"Son doğrulama:\*\* 3 Ekim 2026",
                                                 r"## 12\. Bu kılavuzda doğrulananlar"],
}


def curl(url, cikti=None):
    cmd = ["curl", "-sS", "-L", "-A", "Mozilla/5.0", "--max-time", "40",
           "-o", cikti or "/dev/null", "-w", "%{http_code} %{url_effective}", url]
    r = subprocess.run(cmd, capture_output=True, text=True)
    parca = (r.stdout or "000 -").split(" ", 1)
    return parca[0], parca[1] if len(parca) > 1 else "-"


TARAYICI_JS = r"""
const { chromium } = require(process.env.NP + '/playwright');
(async () => {
  const b = await chromium.launch({
    ...(process.env.HTTPS_PROXY ? { proxy: { server: process.env.HTTPS_PROXY } } : {}),
    // TLS doğrulaması kapatılmaz: yalnız ortamın kendi proxy CA'sının açık anahtarı güvenilir eklenir
    args: process.env.PROXY_CA_SPKI ? ['--ignore-certificate-errors-spki-list=' + process.env.PROXY_CA_SPKI] : [],
  });
  const p = await b.newPage({ userAgent: 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/141.0 Safari/537.36' });
  try { const r = await p.goto(process.argv[process.argv.length - 1], { waitUntil: 'domcontentloaded', timeout: 45000 });
        console.log(JSON.stringify({ kod: r.status(), baslik: await p.title() })); }
  catch (e) { console.log(JSON.stringify({ kod: 0, baslik: '' })); }
  await b.close();
})();
"""


def tarayici_kontrol(url):
    """curl 403 alan (bot korumalı) sayfayı gerçek Chromium ile açar; başlık dönerse canlı say."""
    try:
        npm_kok = subprocess.run(["npm", "root", "-g"], capture_output=True, text=True).stdout.strip()
        ortam = {**os.environ, "NP": npm_kok}
        ca = os.environ.get("PROXY_CA", "/root/.ccr/agent-proxy-ca.crt")
        if os.path.exists(ca):  # TLS'i yeniden sonlandıran kurumsal/bulut proxy varsa
            spki = subprocess.run(
                f"openssl x509 -in {ca} -pubkey -noout | openssl pkey -pubin -outform der"
                " | openssl dgst -sha256 -binary | base64",
                shell=True, capture_output=True, text=True).stdout.strip()
            ortam["PROXY_CA_SPKI"] = spki
        r = subprocess.run(["node", "-e", TARAYICI_JS, "_", url], capture_output=True, text=True,
                           timeout=90, env=ortam)
        sonuc = json.loads(r.stdout.strip().splitlines()[-1])
        return sonuc["baslik"].strip()
    except Exception:
        return ""


def meta_metin(mid, dil):
    yol = Path("/tmp") / f"dogrula_{mid}_{dil}.html"
    kod, _ = curl(f"https://www.facebook.com/business/help/{mid}?locale={dil}", str(yol))
    t = yol.read_text(encoding="utf-8", errors="ignore") if yol.exists() else ""
    bloklar = []
    for m in re.finditer(r'"__html":"((?:[^"\\]|\\.)*)"', t):
        try:
            s = json.loads('"' + m.group(1) + '"')
        except ValueError:
            continue
        bloklar.append(html.unescape(re.sub(r"<[^>]+>", " ", s)))
    metin = max(bloklar, key=len) if bloklar else ""
    baslik = metin.strip().split("  ")[0][:110] if metin else "(okunamadı)"
    return kod, baslik, re.sub(r"\s+", " ", metin)


def main():
    satirlar = ["# Otomatik kabul raporu", ""]
    hata = 0

    # 1) Bağlantılar
    urller = sorted(set(re.findall(r"https://[^\s)>\]`*,]+", KILAVUZ + "\n" + KAYNAK)))
    satirlar += ["## 1. Bağlantı kontrolü", "", "| Kod | Bağlantı | Son adres |", "|---|---|---|"]
    for u in urller:
        u = u.rstrip(".")
        kod, son = curl(u)
        tamam = kod.startswith(("2", "3"))
        # facebook.com uygulama adresleri girişsiz istekte giriş sayfasına yönlenir: bu da canlı demektir
        not_ = ""
        if kod == "403":  # bot koruması: gerçek tarayıcıyla tekrar dene
            baslik = tarayici_kontrol(u)
            if baslik:
                tamam, not_ = True, f" (Chromium ile açıldı: “{baslik[:60]}”)"
        if not tamam:
            hata += 1
        satirlar.append(f"| {kod}{'' if tamam else ' ❌'} | {u} | {son[:90]}{not_} |")

    # 2) Meta iddiaları
    satirlar += ["", "## 2. Meta resmi makale içerik kontrolü", "",
                 "| Makale | Dil | Başlık | Aranan ifadeler | Sonuç |", "|---|---|---|---|---|"]
    for mid, ifadeler in IDDIALAR.items():
        kod, baslik, metin = meta_metin(mid, "tr_TR")
        eksik = [i for i in ifadeler if i not in metin]
        dil = "tr_TR"
        if eksik:  # TR sürümü yoksa İngilizce dene
            kod2, baslik2, metin2 = meta_metin(mid, "en_US")
            eksik2 = [i for i in ifadeler if i not in metin2]
            if len(eksik2) < len(eksik):
                baslik, eksik, dil = baslik2, eksik2, "en_US"
        sonuc = "✅" if not eksik else f"❌ eksik: {eksik}"
        if eksik:
            hata += 1
        satirlar.append(f"| {mid} | {dil} | {baslik.replace('|', '/')} | {', '.join(ifadeler)} | {sonuc} |")

    # 3) Kabul maddeleri
    satirlar += ["", "## 3. Görev kabul maddeleri (yapısal)", "", "| Madde | Sonuç |", "|---|---|"]
    for madde, desenler in KABUL.items():
        eksik = [d for d in desenler if not re.search(d, KILAVUZ)]
        sayi = len(re.findall(r"➜ Farklıysa", KILAVUZ)) if "alternatif" in madde else None
        ek = f" ({sayi} adet 'Farklıysa' dalı)" if sayi else ""
        if eksik:
            hata += 1
        satirlar.append(f"| {madde} | {'✅' + ek if not eksik else '❌ ' + str(eksik)} |")

    satirlar += ["", f"**Toplam hata: {hata}**"]
    rapor = "\n".join(satirlar) + "\n"
    if "--rapor" in sys.argv:
        Path(sys.argv[sys.argv.index("--rapor") + 1]).write_text(rapor, encoding="utf-8")
    print(rapor)
    sys.exit(1 if hata else 0)


if __name__ == "__main__":
    main()
