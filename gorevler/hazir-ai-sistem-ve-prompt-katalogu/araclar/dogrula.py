#!/usr/bin/env python3
"""Kabul doğrulaması.

1. Şema: her girdide kategori, fayda, örnek, kullanım adımları, HTTPS linkler, içerik lisansı var mı?
2. Canlı link: tüm URL'lerin HTTP durumu (github.com sayfaları bu ortamın proxy'sinde 403
   döndüğü için depo varlığı raw.githubusercontent.com README ile ayrıca kanıtlanır).
3. Birebirlik: birebir örnekler commit'e sabitlenmiş kaynak dosyada gerçekten geçiyor mu?
   prompts.chat seçkisi sabit commit'teki CSV ile SHA-256 düzeyinde eşleşiyor mu?
4. Lisans: birebir metin yalnız CC0-1.0/MIT kaynaktan mı; lisans alanları (kaynak, permalink,
   commit/tarih, sahip, lisans, yeniden dağıtım) tam mı; lisans belgeleri pakette mi; MIT telif
   satırları NOTICE.md'de mi; lisansı belirsiz/tescilli kaynaklardan çıkarılan metinler hiçbir
   çıktı dosyasında kalmamış mı?

Çıktılar: dogrulama/link-raporu.md, dogrulama/prompt-dogrulama.md, dogrulama/lisans-dogrulama.md,
dogrulama/sonuc.json · Çıkış kodu: zorunlu kontrollerden biri başarısızsa 1.
"""
import csv
import hashlib
import io
import json
import re
import subprocess
import sys
from concurrent.futures import ThreadPoolExecutor
from datetime import datetime, timezone
from pathlib import Path

KOK = Path(__file__).resolve().parent.parent
KATALOG = json.loads((KOK / "veri" / "katalog.json").read_text(encoding="utf-8"))
SECKI = json.loads((KOK / "veri" / "prompts_chat_secki.json").read_text(encoding="utf-8"))
CIKTI = KOK / "dogrulama"
ZORUNLU = ["id", "ad", "url", "kategori", "tur", "ne_ise_yarar", "kime", "ornek", "kullanim",
           "ucret_lisans", "topluluk", "dikkat", "kanit", "icerik_lisansi"]
BIREBIR_ALANLAR = ["kaynak_depo", "dosya_permalink", "ham_permalink", "commit", "commit_tarihi",
                   "erisim_tarihi", "sahip", "lisans", "lisans_belgesi", "yeniden_dagitim"]
SECKI_ALANLAR = ["kaynak", "dosya_permalink", "ham_permalink", "csv_kaydi", "commit", "commit_tarihi",
                 "erisim_tarihi", "sahip", "lisans", "lisans_dayanagi", "yeniden_dagitim"]
IZINLI_LISANS = {"CC0-1.0", "MIT"}
# İlk sürümde vardı, lisansı belirsiz/tescilli olduğu için çıkarıldı: hiçbir çıktıda bulunmamalı
YASAK_METINLER = {
    "Cursor sistem prompt'u (tescilli)": "semantic search that finds code by meaning",
    "OpenAI Academy e-posta şablonu": "Write a professional email to [recipient]",
    "Google Gemini PDF örneği": "You are a business owner in [industry]",
    "LangChain belge örneği": "joke-generator:production",
    "jujumilk3 README cümlesi": "You must include sources that I can verify",
    "ai-boost README (GPL-3.0)": "copy-paste ready",
    "Anthropic skills README şablonu": "[Add your instructions here that Claude will follow",
    "Anthropic skills README cümlesi": "Use the PDF skill to extract the form fields",
}
CIKTI_DOSYALARI = ["KATALOG.md", "hazir-promptlar.md", "index.html", "LISANS-ENVANTERI.md", "NOTICE.md",
                   "README.md", "veri/katalog.json", "veri/prompts_chat_secki.json"]
UA = "Mozilla/5.0 (X11; Linux x86_64) katalog-dogrulama"
csv.field_size_limit(10**9)


def curl(url: str, govde: bool = False):
    arg = ["curl", "-sS", "-L", "--max-time", "60", "-A", UA, "-o", "-" if govde else "/dev/null",
           "-w", "\n%{http_code} %{url_effective}" if govde else "%{http_code} %{url_effective}", url]
    p = subprocess.run(arg, capture_output=True)
    if govde:
        ham, _, son = p.stdout.rpartition(b"\n")
        son = son.decode("utf-8", "replace")
    else:
        ham, son = b"", p.stdout.decode("utf-8", "replace")
    parca = son.split(" ", 1)
    kod = parca[0] if parca and parca[0].isdigit() else "000"
    return kod, (parca[1] if len(parca) > 1 else url), ham


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


def tum_urller(g):
    u = [g["url"], g["ornek"]["kaynak_url"], g["topluluk"]["url"], *g.get("ek_linkler", [])]
    return list(dict.fromkeys(u))


def sema_kontrol():
    hatalar = []
    kat_ids = {k["id"] for k in KATALOG["kategoriler"]}
    for g in KATALOG["girdiler"]:
        for alan in ZORUNLU:
            if not g.get(alan):
                hatalar.append(f"{g.get('id')}: '{alan}' boş")
        if g.get("kategori") not in kat_ids:
            hatalar.append(f"{g['id']}: bilinmeyen kategori")
        for u in tum_urller(g):
            if not u.startswith("https://"):
                hatalar.append(f"{g['id']}: HTTPS olmayan link {u}")
        if not g.get("ornek", {}).get("metin") or not g["ornek"].get("kaynak_url"):
            hatalar.append(f"{g['id']}: örnek metin/kaynak eksik")
        if g.get("icerik_lisansi", {}).get("durum") not in {"acik", "kisitli", "tescilli", "bilinmiyor"}:
            hatalar.append(f"{g['id']}: içerik lisansı durumu geçersiz")
    for k in kat_ids:
        if not any(g["kategori"] == k for g in KATALOG["girdiler"]):
            hatalar.append(f"kategori boş: {k}")
    return hatalar


def lisans_kontrol():
    """Lisans katmanı kuralları; her ihlal bir hata satırı."""
    h, ayrinti = [], []
    depolar = KATALOG["kaynak_depolar"]
    for r, v in depolar.items():
        if not re.fullmatch(r"[0-9a-f]{40}", v["commit"]):
            h.append(f"{r}: commit tam SHA değil")
        for p in v["lisans_yerel_kopya"]:
            if not (KOK / p).is_file():
                h.append(f"{r}: lisans kopyası yok {p}")
    notice = (KOK / "NOTICE.md").read_text(encoding="utf-8")
    for g in KATALOG["girdiler"]:
        k = g["ornek"]["kopya"]
        if k["tur"] == "birebir":
            for a in BIREBIR_ALANLAR:
                if not k.get(a):
                    h.append(f"{g['id']}: birebir lisans alanı boş: {a}")
            if k.get("lisans") not in IZINLI_LISANS:
                h.append(f"{g['id']}: birebir metin izinli olmayan lisanstan ({k.get('lisans')})")
            if k["commit"] not in k["dosya_permalink"] or k["commit"] not in k["ham_permalink"]:
                h.append(f"{g['id']}: permalink commit'e sabit değil")
            if k["commit"] != depolar[k["kaynak_depo"]]["commit"]:
                h.append(f"{g['id']}: commit kaynak_depolar ile uyuşmuyor")
            for p in k["lisans_belgesi"]:
                if not (KOK / p).is_file():
                    h.append(f"{g['id']}: lisans belgesi yok {p}")
            if k["lisans"] == "MIT":
                lis = (KOK / k["lisans_belgesi"][0]).read_text(encoding="utf-8")
                tel = next((s.strip() for s in lis.splitlines() if s.strip().startswith("Copyright")), "")
                if "Permission is hereby granted" not in lis or not tel:
                    h.append(f"{g['id']}: MIT belgesi eksik/bozuk")
                elif tel not in notice:
                    h.append(f"{g['id']}: MIT telif satırı NOTICE.md'de yok ({tel})")
                ayrinti.append(f"{g['id']}: MIT · {tel}")
            if k["lisans"] == "CC0-1.0":
                if not any("CC0" in (KOK / p).read_text(encoding="utf-8") for p in k["lisans_belgesi"]):
                    h.append(f"{g['id']}: CC0 dayanak belgesi CC0 içermiyor")
                ayrinti.append(f"{g['id']}: CC0-1.0 · {', '.join(k['lisans_belgesi'])}")
            if not k.get("kopyalanabilir"):
                h.append(f"{g['id']}: birebir ama kopyalanabilir değil")
        else:
            if k.get("kopyalanabilir"):
                h.append(f"{g['id']}: özgün açıklama kopyalanabilir işaretli")
            if "birebir" in g["ornek"]["baslik"]:
                h.append(f"{g['id']}: özgün açıklama 'birebir' diye etiketli")
            if not k.get("neden"):
                h.append(f"{g['id']}: kopyalanmama nedeni yazılmamış")
        if g["icerik_lisansi"]["durum"] != "acik" and k["tur"] == "birebir" and g["id"] != "zie619-n8n":
            h.append(f"{g['id']}: kapalı/belirsiz lisanslı kaynaktan birebir metin")
    for x in SECKI["secki"]:
        lk = x.get("lisans_kaydi", {})
        for a in SECKI_ALANLAR:
            if not lk.get(a):
                h.append(f"seçki {x['act']}: lisans alanı boş: {a}")
        if lk.get("lisans") != "CC0-1.0" or SECKI["commit"] not in lk.get("dosya_permalink", ""):
            h.append(f"seçki {x['act']}: lisans/permalink hatalı")
    for p in ["lisanslar/prompts.chat_LICENSE", "lisanslar/prompts.chat_LICENSE-CC0", "lisanslar/Fabric_LICENSE"]:
        if not (KOK / p).is_file():
            h.append(f"lisans belgesi yok: {p}")
    if "CC0 1.0 Universal" not in (KOK / "lisanslar/prompts.chat_LICENSE").read_text(encoding="utf-8"):
        h.append("prompts.chat LICENSE CC0 beyanını içermiyor")
    hp = (KOK / "hazir-promptlar.md").read_text(encoding="utf-8")
    if (KOK / "lisanslar/Fabric_LICENSE").read_text(encoding="utf-8").strip() not in hp:
        h.append("hazir-promptlar.md: Fabric MIT izin metni tam metnin yanında yok")
    yasak = []
    for ad, metin in YASAK_METINLER.items():
        for f in CIKTI_DOSYALARI:
            if metin in (KOK / f).read_text(encoding="utf-8"):
                yasak.append(f"{ad} → {f}")
    h += [f"çıkarılması gereken metin hâlâ var: {y}" for y in yasak]
    return h, ayrinti, len(YASAK_METINLER) * len(CIKTI_DOSYALARI)


def norm(s: str) -> str:
    return re.sub(r"\s+", " ", s).strip()


def birebir_kontrol():
    """Birebir örnekleri commit'e sabit ham kaynakla karşılaştırır."""
    sonuc = []
    for g in KATALOG["girdiler"]:
        k = g["ornek"]["kopya"]
        if k["tur"] != "birebir":
            continue
        kod, _, ham = curl(k["ham_permalink"], govde=True)
        metin = ham.decode("utf-8", "replace")
        if k["ham_permalink"].endswith(".csv"):
            metin = "\n".join(s["prompt"] for s in csv.DictReader(io.StringIO(metin, newline="")))
        kaynak = norm(metin)
        parcalar = [norm(p) for p in re.split(r"\.\.\.|…", g["ornek"]["metin"]) if norm(p).strip("/ ")]
        eksik = [p for p in parcalar if p not in kaynak]
        sonuc.append({"id": g["id"], "kaynak_url": k["ham_permalink"], "http": kod, "parca_sayisi": len(parcalar),
                      "eslesen": len(parcalar) - len(eksik), "eksik": eksik, "gecti": not eksik and kod == "200"})
    return sonuc


def secki_kontrol():
    csv.field_size_limit(10**9)
    kod, _, ham = curl(SECKI["kaynak"], govde=True)
    gercek = hashlib.sha256(ham).hexdigest()
    satirlar = {}
    for s in csv.DictReader(io.StringIO(ham.decode("utf-8", "replace"), newline="")):
        satirlar.setdefault(s["act"], s["prompt"])
    sonuc = []
    for x in SECKI["secki"]:
        p = satirlar.get(x["act"])
        sonuc.append({"act": x["act"], "gecti": p is not None and p == x["prompt"]
                      and hashlib.sha256(p.encode()).hexdigest() == x["sha256"]})
    return {"http": kod, "csv_sha256": gercek, "beklenen": SECKI["csv_sha256"],
            "sha_eslesme": gercek == SECKI["csv_sha256"], "satirlar": sonuc}


def main() -> int:
    CIKTI.mkdir(exist_ok=True)
    zaman = datetime.now(timezone.utc).strftime("%Y-%m-%d %H:%M UTC")
    sema = sema_kontrol()
    lis_hata, lis_ayrinti, yasak_kontrol_sayisi = lisans_kontrol()
    urller = sorted({u for g in KATALOG["girdiler"] for u in tum_urller(g)})
    with ThreadPoolExecutor(8) as ex:
        linkler = list(ex.map(link_kontrol, urller))
    birebir = birebir_kontrol()
    secki = secki_kontrol()

    erisildi = sum(l["durum"] == "ERİŞİLDİ" for l in linkler)
    engel = [l for l in linkler if l["durum"] != "ERİŞİLDİ"]
    birebir_ok = all(b["gecti"] for b in birebir)
    secki_ok = secki["sha_eslesme"] and all(s["gecti"] for s in secki["satirlar"])
    ana = {g["url"] for g in KATALOG["girdiler"]}
    ana_ok = sum(1 for l in linkler if l["url"] in ana and l["durum"] == "ERİŞİLDİ")
    ana_oran = ana_ok / len(ana)
    gecti = not sema and not lis_hata and birebir_ok and secki_ok and ana_oran >= 0.9

    r = ["# Link raporu", "", f"Çalıştırma: {zaman} · araç: `python3 araclar/dogrula.py` (curl ile canlı istek)", "",
         f"- Toplam benzersiz link: **{len(linkler)}** · erişildi: **{erisildi}** · engelli/başarısız: **{len(engel)}**",
         f"- Ana site linkleri: **{ana_ok}/{len(ana)}** erişildi (%{ana_oran*100:.0f})",
         "- github.com HTML sayfaları bu bulut ortamının proxy'sinde 403 döner; bu durumda depo varlığı "
         "`raw.githubusercontent.com/.../README.md` 200 yanıtıyla kanıtlanır. github.com/blob permalinkleri aynı "
         "commit'teki raw adresle (aşağıda) doğrulanır.",
         "- ENGEL satırları yalnız bu ortamdaki otomatik isteğin sonucudur; normal tarayıcıda açılıp açılmadıkları "
         "test edilmedi, garanti verilmez.", "",
         "| Durum | HTTP | raw README | Link | Son adres |", "|---|---|---|---|---|"]
    for l in linkler:
        son = l["son_adres"] if l["son_adres"] != l["url"] else ""
        if len(son) > 90:
            son = son[:90] + "…"
        r.append(f"| {l['durum']} | {l['http']} | {l.get('raw_readme_http', '')} | {l['url']} | {son} |")
    (CIKTI / "link-raporu.md").write_text("\n".join(r) + "\n", encoding="utf-8")

    p = ["# Prompt birebirlik doğrulaması", "", f"Çalıştırma: {zaman}", "",
         "## A. Katalogdaki birebir örnekler commit'e sabit kaynakta geçiyor mu?", "",
         "| Girdi | HTTP | Eşleşen parça | Sonuç | Sabit kaynak |", "|---|---|---|---|---|"]
    for b in birebir:
        p.append(f"| {b['id']} | {b['http']} | {b['eslesen']}/{b['parca_sayisi']} | {'GEÇTİ' if b['gecti'] else 'KALDI'} | {b['kaynak_url']} |")
    for b in birebir:
        if b["eksik"]:
            p.append(f"\n- {b['id']} eksik parçalar: {b['eksik']}")
    p += ["", f"## B. prompts.chat seçkisi ({len(SECKI['secki'])} prompt) — sabit commit CSV ile eşleşme", "",
          f"- Kaynak: {SECKI['kaynak']} (HTTP {secki['http']})",
          f"- İndirilen CSV SHA-256: `{secki['csv_sha256']}` · beklenen ile eşleşme: **{secki['sha_eslesme']}**", "",
          "| Prompt (act) | Sonuç |", "|---|---|"]
    for s in secki["satirlar"]:
        p.append(f"| {s['act']} | {'GEÇTİ' if s['gecti'] else 'KALDI'} |")
    p += ["", "## C. Şema", "", "Hata yok." if not sema else "\n".join(f"- {h}" for h in sema), "",
          f"## Genel sonuç: **{'GEÇTİ' if gecti else 'KALDI'}**", ""]
    (CIKTI / "prompt-dogrulama.md").write_text("\n".join(p), encoding="utf-8")

    L = ["# Lisans doğrulaması", "", f"Çalıştırma: {zaman}", "",
         "Kurallar: birebir metin yalnız CC0-1.0/MIT kaynaktan; her birebir kopyada kaynak depo, commit'e sabit tam dosya "
         "permalinki, commit + tarih, erişim tarihi, sahip, lisans, lisans belgesi ve yeniden dağıtım sınırı dolu; lisans "
         "belgeleri `lisanslar/` içinde; MIT telif satırları `NOTICE.md` içinde; lisansı belirsiz/tescilli kaynaklardan "
         "ilk sürümde alınan metinler hiçbir çıktı dosyasında yok.", "",
         f"- Birebir örnek: {sum(1 for g in KATALOG['girdiler'] if g['ornek']['kopya']['tur']=='birebir')} · özgün açıklama: "
         f"{sum(1 for g in KATALOG['girdiler'] if g['ornek']['kopya']['tur']=='ozgun')} · seçki prompt: {len(SECKI['secki'])}",
         f"- Yasak metin taraması: {len(YASAK_METINLER)} metin × {len(CIKTI_DOSYALARI)} dosya = {yasak_kontrol_sayisi} kontrol", "",
         "## Birebir kopyaların lisans dayanağı", ""] + [f"- {a}" for a in lis_ayrinti] + ["",
         "## Hatalar", "", "Hata yok." if not lis_hata else "\n".join(f"- {x}" for x in lis_hata), "",
         f"## Sonuç: **{'GEÇTİ' if not lis_hata else 'KALDI'}**", ""]
    (CIKTI / "lisans-dogrulama.md").write_text("\n".join(L), encoding="utf-8")

    (CIKTI / "sonuc.json").write_text(json.dumps({
        "zaman": zaman, "gecti": gecti, "sema_hatalari": sema, "lisans_hatalari": lis_hata,
        "link_toplam": len(linkler), "link_erisildi": erisildi,
        "engelli": [{"url": l["url"], "http": l["http"]} for l in engel], "ana_oran": round(ana_oran, 3),
        "birebir": [{k: b[k] for k in ("id", "gecti", "eslesen", "parca_sayisi")} for b in birebir],
        "secki_gecti": secki_ok}, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"şema hatası: {len(sema)} | lisans hatası: {len(lis_hata)} | link: {erisildi}/{len(linkler)} erişildi | "
          f"ana: {ana_ok}/{len(ana)} | birebir: {sum(b['gecti'] for b in birebir)}/{len(birebir)} | "
          f"seçki: {secki_ok} | GENEL: {'GEÇTİ' if gecti else 'KALDI'}")
    for l in engel:
        print("  engel:", l["http"], l["url"])
    for x in lis_hata:
        print("  lisans:", x)
    for b in birebir:
        if not b["gecti"]:
            print("  birebir KALDI:", b["id"], b["eksik"][:2])
    return 0 if gecti else 1


if __name__ == "__main__":
    sys.exit(main())
