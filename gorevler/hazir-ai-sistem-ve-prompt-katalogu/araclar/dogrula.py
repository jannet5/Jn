#!/usr/bin/env python3
"""Kabul doğrulaması.

1. Şema: her girdide kategori, fayda, örnek, kullanım adımları, HTTPS linkler var mı?
2. Canlı link: tüm URL'lerin HTTP durumu (github.com sayfaları bu ortamın proxy'sinde 403
   döndüğü için depo varlığı raw.githubusercontent.com README ile ayrıca kanıtlanır).
3. Birebirlik: 'birebir' denen örnek metinler kaynak dosyada gerçekten geçiyor mu?
   prompts.chat seçkisi CSV anlık görüntüsüyle SHA-256 düzeyinde eşleşiyor mu?

Çıktılar: dogrulama/link-raporu.md, dogrulama/prompt-dogrulama.md, dogrulama/sonuc.json
Çıkış kodu: zorunlu kontrollerden biri başarısızsa 1.
"""
import csv
import hashlib
import json
import re
import subprocess
import sys
import urllib.parse
from concurrent.futures import ThreadPoolExecutor
from datetime import datetime, timezone
from pathlib import Path

KOK = Path(__file__).resolve().parent.parent
KATALOG = json.loads((KOK / "veri" / "katalog.json").read_text(encoding="utf-8"))
SECKI = json.loads((KOK / "veri" / "prompts_chat_secki.json").read_text(encoding="utf-8"))
CSV = KOK / "veri" / "kaynak-anlik" / SECKI["anlik_goruntu"]
CIKTI = KOK / "dogrulama"
ZORUNLU = ["id", "ad", "url", "kategori", "tur", "ne_ise_yarar", "kime", "ornek", "kullanim",
           "ucret_lisans", "topluluk", "dikkat", "kanit"]
UA = "Mozilla/5.0 (X11; Linux x86_64) katalog-dogrulama"


def curl(url: str, govde: bool = False):
    arg = ["curl", "-sS", "-L", "--max-time", "30", "-A", UA, "-o", "-" if govde else "/dev/null",
           "-w", "\n%{http_code} %{url_effective}" if govde else "%{http_code} %{url_effective}", url]
    p = subprocess.run(arg, capture_output=True)
    cikti = p.stdout.decode("utf-8", "replace")
    if govde:
        icerik, _, son = cikti.rpartition("\n")
    else:
        icerik, son = "", cikti
    parca = son.split(" ", 1)
    kod = parca[0] if parca and parca[0].isdigit() else "000"
    return kod, (parca[1] if len(parca) > 1 else url), icerik


def github_repo(url: str):
    m = re.match(r"https://github\.com/([^/]+)/([^/#?]+)/?$", url)
    return (m.group(1), m.group(2)) if m else None


def link_kontrol(url: str) -> dict:
    kod, son, _ = curl(url)
    sonuc = {"url": url, "http": kod, "son_adres": son}
    gr = github_repo(url)
    if gr and kod != "200":
        rkod, _, _ = curl(f"https://raw.githubusercontent.com/{gr[0]}/{gr[1]}/HEAD/README.md")
        sonuc["raw_readme_http"] = rkod
    ok = kod.startswith(("2", "3")) or sonuc.get("raw_readme_http") == "200"
    sonuc["durum"] = "ERİŞİLDİ" if ok else ("BOT KORUMASI/ENGEL" if kod in ("403", "429") else "BAŞARISIZ")
    return sonuc


def sema_kontrol():
    hatalar = []
    kat_ids = {k["id"] for k in KATALOG["kategoriler"]}
    for g in KATALOG["girdiler"]:
        for alan in ZORUNLU:
            if not g.get(alan):
                hatalar.append(f"{g.get('id')}: '{alan}' boş")
        if g.get("kategori") not in kat_ids:
            hatalar.append(f"{g['id']}: bilinmeyen kategori")
        if len(g.get("kullanim", [])) < 1:
            hatalar.append(f"{g['id']}: kullanım adımı yok")
        for u in tum_urller(g):
            if not u.startswith("https://"):
                hatalar.append(f"{g['id']}: HTTPS olmayan link {u}")
        if not g.get("ornek", {}).get("metin") or not g["ornek"].get("kaynak_url"):
            hatalar.append(f"{g['id']}: örnek metin/kaynak eksik")
    for k in kat_ids:
        if not any(g["kategori"] == k for g in KATALOG["girdiler"]):
            hatalar.append(f"kategori boş: {k}")
    return hatalar


def tum_urller(g):
    u = [g["url"], g["ornek"]["kaynak_url"], g["topluluk"]["url"], *g.get("ek_linkler", [])]
    return list(dict.fromkeys(u))


def birebir_kontrol():
    """Başlığında 'birebir' geçen örnekleri kaynağıyla karşılaştırır."""
    sonuc = []
    for g in KATALOG["girdiler"]:
        o = g["ornek"]
        if "birebir" not in o["baslik"]:
            continue
        url = o["kaynak_url"]
        if url.endswith(".pdf"):
            kod, _, _ = curl(url)
            tmp = CIKTI / "_gecici.pdf"
            subprocess.run(["curl", "-sSL", "--max-time", "60", "-o", str(tmp), url])
            metin = subprocess.run(["pdftotext", str(tmp), "-"], capture_output=True).stdout.decode("utf-8", "replace")
            tmp.unlink(missing_ok=True)
        else:
            kod, _, metin = curl(url, govde=True)
        norm = lambda s: re.sub(r"\s+", " ", s.replace("\\n", "\n")).strip()
        kaynak = norm(metin)
        parcalar = [norm(p) for p in re.split(r"\.\.\.|…", o["metin"]) if norm(p).strip("/ ")]
        eksik = [p for p in parcalar if p not in kaynak]
        sonuc.append({"id": g["id"], "kaynak_url": url, "http": kod, "parca_sayisi": len(parcalar),
                      "eslesen": len(parcalar) - len(eksik), "eksik": eksik, "gecti": not eksik and kod == "200"})
    return sonuc


def secki_kontrol():
    csv.field_size_limit(10**9)
    gercek = hashlib.sha256(CSV.read_bytes()).hexdigest()
    satirlar = {}
    for s in csv.DictReader(CSV.open(encoding="utf-8")):
        satirlar.setdefault(s["act"], s["prompt"])
    sonuc = []
    for x in SECKI["secki"]:
        p = satirlar.get(x["act"])
        sonuc.append({"act": x["act"], "gecti": p is not None and hashlib.sha256(p.encode()).hexdigest() == x["sha256"] == hashlib.sha256(x["prompt"].encode()).hexdigest()})
    # Canlı CSV ile de karşılaştır (anlık görüntü hâlâ güncel mi?)
    kod, _, canli = curl(SECKI["kaynak"], govde=True)
    canli_esit = hashlib.sha256(canli.encode("utf-8")).hexdigest() == gercek
    # CSV'de çift tırnaklar "" olarak kaçışlıdır; ham metinde arama yerine CSV olarak ayrıştır
    canli_promptlar = {s["prompt"] for s in csv.DictReader(canli.splitlines(keepends=True))} if kod == "200" else set()
    canli_icerir = sum(1 for x in SECKI["secki"] if x["prompt"] in canli_promptlar)
    return {"anlik_goruntu_sha256": gercek, "kayitli_sha256": SECKI["anlik_goruntu_sha256"],
            "anlik_eslesme": gercek == SECKI["anlik_goruntu_sha256"], "satirlar": sonuc,
            "canli_http": kod, "canli_birebir_ayni_dosya": canli_esit, "canli_csvde_bulunan": canli_icerir}


def main() -> int:
    CIKTI.mkdir(exist_ok=True)
    zaman = datetime.now(timezone.utc).strftime("%Y-%m-%d %H:%M UTC")
    sema = sema_kontrol()
    urller = sorted({u for g in KATALOG["girdiler"] for u in tum_urller(g)})
    with ThreadPoolExecutor(8) as ex:
        linkler = list(ex.map(link_kontrol, urller))
    birebir = birebir_kontrol()
    secki = secki_kontrol()

    erisildi = sum(l["durum"] == "ERİŞİLDİ" for l in linkler)
    engel = [l for l in linkler if l["durum"] != "ERİŞİLDİ"]
    birebir_ok = all(b["gecti"] for b in birebir)
    secki_ok = secki["anlik_eslesme"] and all(s["gecti"] for s in secki["satirlar"])
    # Zorunlu: şema temiz, birebirlik ve seçki geçti, ana sitelerin (girdi url) en az %90'ı erişildi
    ana = {g["url"] for g in KATALOG["girdiler"]}
    ana_ok = sum(1 for l in linkler if l["url"] in ana and l["durum"] == "ERİŞİLDİ")
    ana_oran = ana_ok / len(ana)
    gecti = not sema and birebir_ok and secki_ok and ana_oran >= 0.9

    r = [f"# Link raporu", "", f"Çalıştırma: {zaman} · araç: `python3 araclar/dogrula.py` (curl ile canlı istek)", "",
         f"- Toplam benzersiz link: **{len(linkler)}** · erişildi: **{erisildi}** · engelli/başarısız: **{len(engel)}**",
         f"- Ana site linkleri: **{ana_ok}/{len(ana)}** erişildi (%{ana_oran*100:.0f})",
         "- Not: github.com HTML sayfaları bu bulut ortamının proxy'sinde 403 döner; bu durumda depo varlığı "
         "`raw.githubusercontent.com/.../README.md` 200 yanıtıyla kanıtlanmıştır.", "",
         "| Durum | HTTP | raw README | Link | Son adres |", "|---|---|---|---|---|"]
    for l in linkler:
        son = l["son_adres"] if l["son_adres"] != l["url"] else ""
        if len(son) > 90:
            son = son[:90] + "…"
        r.append(f"| {l['durum']} | {l['http']} | {l.get('raw_readme_http', '')} | {l['url']} | {son} |")
    (CIKTI / "link-raporu.md").write_text("\n".join(r) + "\n", encoding="utf-8")

    p = ["# Prompt birebirlik doğrulaması", "", f"Çalıştırma: {zaman}", "",
         "## A. Katalogdaki 'birebir' örnekler kaynak dosyada geçiyor mu?", "",
         "| Girdi | HTTP | Eşleşen parça | Sonuç | Kaynak |", "|---|---|---|---|---|"]
    for b in birebir:
        p.append(f"| {b['id']} | {b['http']} | {b['eslesen']}/{b['parca_sayisi']} | {'GEÇTİ' if b['gecti'] else 'KALDI'} | {b['kaynak_url']} |")
    for b in birebir:
        if b["eksik"]:
            p.append(f"\n- {b['id']} eksik parçalar: {b['eksik']}")
    p += ["", "## B. prompts.chat seçkisi (30 prompt) CSV ile SHA-256 eşleşmesi", "",
          f"- Anlık görüntü SHA-256: `{secki['anlik_goruntu_sha256']}` · kayıtlı ile eşleşme: **{secki['anlik_eslesme']}**",
          f"- Canlı CSV HTTP: {secki['canli_http']} · bugün indirilen canlı dosya anlık görüntüyle birebir aynı: **{secki['canli_birebir_ayni_dosya']}** · seçkiden canlı CSV'de bulunan: **{secki['canli_csvde_bulunan']}/{len(SECKI['secki'])}**", "",
          "| Prompt (act) | Sonuç |", "|---|---|"]
    for s in secki["satirlar"]:
        p.append(f"| {s['act']} | {'GEÇTİ' if s['gecti'] else 'KALDI'} |")
    p += ["", "## C. Şema", "", "Hata yok." if not sema else "\n".join(f"- {h}" for h in sema), "",
          f"## Genel sonuç: **{'GEÇTİ' if gecti else 'KALDI'}**", ""]
    (CIKTI / "prompt-dogrulama.md").write_text("\n".join(p), encoding="utf-8")

    (CIKTI / "sonuc.json").write_text(json.dumps({
        "zaman": zaman, "gecti": gecti, "sema_hatalari": sema, "link_toplam": len(linkler), "link_erisildi": erisildi,
        "engelli": [{"url": l["url"], "http": l["http"]} for l in engel], "ana_oran": round(ana_oran, 3),
        "birebir": [{k: b[k] for k in ("id", "gecti", "eslesen", "parca_sayisi")} for b in birebir],
        "secki_gecti": secki_ok, "canli_csvde_bulunan": secki["canli_csvde_bulunan"]}, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"şema hatası: {len(sema)} | link: {erisildi}/{len(linkler)} erişildi | ana: {ana_ok}/{len(ana)} | "
          f"birebir: {sum(b['gecti'] for b in birebir)}/{len(birebir)} | seçki: {secki_ok} | GENEL: {'GEÇTİ' if gecti else 'KALDI'}")
    for l in engel:
        print("  engel:", l["http"], l["url"])
    for b in birebir:
        if not b["gecti"]:
            print("  birebir KALDI:", b["id"], b["eksik"][:2])
    return 0 if gecti else 1


if __name__ == "__main__":
    sys.exit(main())
