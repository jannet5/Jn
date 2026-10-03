#!/usr/bin/env python3
"""Komik fotoğraf dörtlü paket üreticisi.

Girdi : veri/secim-ham.json (fotoğraf + kaynak linkleri), veri/paketler.json (müzik + açıklama)
Çıktı : <cikti>/komik-foto-paketleri/ klasörü ve yanında ZIP + SHA-256.

Fotoğraflar üçüncü kişilere ait olduğu için public depoya konmaz; bu betik
onları kaynak URL'lerinden indirip yalnız özel çıktı klasörüne yazar.

Kullanım: python3 araclar/build.py <cikti_klasoru>
"""
import csv, hashlib, html, io, json, os, re, shutil, subprocess, sys, unicodedata, urllib.request, zipfile
from PIL import Image, ImageDraw, ImageFilter, ImageFont, ImageOps

KOK = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
UA = {"User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/128 Safari/537.36"}
FONT = "/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf"
IG = (1080, 1350)   # Instagram dikey gönderi 4:5
TT = (1080, 1920)   # TikTok foto modu / Reels 9:16
SLAYT_SN = 2.5


def slug(s, n=40):
    s = s.translate(str.maketrans("çğıöşüÇĞİÖŞÜ", "cgiosuCGIOSU"))
    s = unicodedata.normalize("NFKD", s).encode("ascii", "ignore").decode()
    s = re.sub(r"[^A-Za-z0-9]+", "-", s).strip("-").lower()
    return s[:n].strip("-") or "foto"


def sha(p):
    h = hashlib.sha256()
    with open(p, "rb") as f:
        for b in iter(lambda: f.read(1 << 20), b""):
            h.update(b)
    return h.hexdigest()


def karistir_kategoriler(items, tohum=None):
    """Klasör numarası: sabit tohumlu karıştırma. Tohum, 1-4-7-10 kuralıyla oluşan
    her dörtlüde en az 3 farklı kategori olacak şekilde seçilir (deterministik)."""
    import random
    def skor(sira):
        return min(len({sira[n - 1]["kat"] for n in g}) for g in dortlu_gruplar(len(sira)))
    for t in ([tohum] if tohum is not None else range(1000)):
        sira = items[:]
        random.Random(t).shuffle(sira)
        if skor(sira) >= 3:
            return sira
    raise SystemExit("uygun karışık sıra bulunamadı")


def dortlu_gruplar(n):
    """Kullanıcının tarifi: 1,4,7,10 -> cebe; sonra 2,5,8,11; sonra 3,6,9,12.
    Her 12'lik blokta ikişer atlayarak (adım 3) dörtlü gruplar. 1 tabanlı numaralar döner."""
    assert n % 12 == 0, "foto sayısı 12'nin katı olmalı"
    gruplar = []
    for b in range(0, n, 12):
        for bas in (1, 2, 3):
            gruplar.append([b + bas + 3 * k for k in range(4)])
    return gruplar


def indir(url):
    r = urllib.request.urlopen(urllib.request.Request(url, headers=UA), timeout=60)
    return r.read()


def slayt(img, boyut, kredi):
    W, H = boyut
    arka = ImageOps.fit(img, boyut, Image.LANCZOS).filter(ImageFilter.GaussianBlur(40))
    arka = Image.blend(arka, Image.new("RGB", boyut, (0, 0, 0)), 0.45)
    alan_h = H - 90
    on = ImageOps.contain(img, (W, alan_h), Image.LANCZOS)
    arka.paste(on, ((W - on.width) // 2, (alan_h - on.height) // 2 + 10))
    d = ImageDraw.Draw(arka)
    f = ImageFont.truetype(FONT, 24)
    tw = d.textlength(kredi, font=f)
    d.text(((W - tw) / 2, H - 55), kredi, font=f, fill=(235, 235, 235))
    return arka


def kredi_metni(it):
    if it.get("x_url"):
        return "kaynak: X " + it["kaynak_sayfa"].split("/")[3]
    host = re.sub(r"^www\.", "", urllib.request.urlparse(it["kaynak_sayfa"]).netloc)
    return "kaynak: " + host


def main():
    cikti = os.path.abspath(sys.argv[1] if len(sys.argv) > 1 else "cikti")
    kok = os.path.join(cikti, "komik-foto-paketleri")
    if os.path.exists(kok):
        shutil.rmtree(kok)
    os.makedirs(os.path.join(kok, "foto"))
    items = karistir_kategoriler(json.load(open(os.path.join(KOK, "veri/secim-ham.json"), encoding="utf8")))
    paket_veri = json.load(open(os.path.join(KOK, "veri/paketler.json"), encoding="utf8"))

    # 1) Koleksiyon klasörü
    for no, it in enumerate(items, 1):
        it["no"] = no
        it["dosya"] = f"foto/{no:02d}_{it['kat']}_{slug(it['baslik'])}.jpg"
        ham = indir(it["gorsel_url"])
        kaynak_im = Image.open(io.BytesIO(ham))
        im = kaynak_im.convert("RGB")
        if kaynak_im.format == "JPEG":   # orijinal baytlar aynen (yeniden sıkıştırma yok)
            open(os.path.join(kok, it["dosya"]), "wb").write(ham)
        else:
            im.save(os.path.join(kok, it["dosya"]), "JPEG", quality=92)
        it["boyut"] = f"{im.width}x{im.height}"
        it["sha256"] = sha(os.path.join(kok, it["dosya"]))
        it["_img"] = im
        print(f"[foto] {it['dosya']} {it['boyut']}")

    with open(os.path.join(kok, "foto", "kaynaklar.csv"), "w", newline="", encoding="utf-8-sig") as f:
        w = csv.writer(f)
        w.writerow(["no", "dosya", "kategori", "baslik", "kaynak_sayfa", "kaynak_sayfa_basligi", "x_gonderisi", "gorsel_url", "boyut", "sha256"])
        for it in items:
            w.writerow([it["no"], it["dosya"], it["kat"], it["baslik"], it["kaynak_sayfa"], it["kaynak_sayfa_basligi"], it.get("x_url") or "", it["gorsel_url"], it["boyut"], it["sha256"]])

    # 2) Dörtlü paketler
    gruplar = dortlu_gruplar(len(items))
    assert len(gruplar) == len(paket_veri), f"{len(gruplar)} grup, {len(paket_veri)} paket verisi"
    muzikler = [p["muzik"]["sanatci"] + " - " + p["muzik"]["sarki"] for p in paket_veri]
    assert len(set(muzikler)) == len(muzikler), "her pakete farklı müzik"
    ozet = []
    for pi, (grup, pv) in enumerate(zip(gruplar, paket_veri), 1):
        pd = os.path.join(kok, "paketler", f"paket-{pi:02d}")
        os.makedirs(os.path.join(pd, "instagram"))
        os.makedirs(os.path.join(pd, "tiktok"))
        fotolar = [items[n - 1] for n in grup]
        for si, it in enumerate(fotolar, 1):
            kr = kredi_metni(it)
            slayt(it["_img"], IG, kr).save(os.path.join(pd, "instagram", f"{si}.jpg"), "JPEG", quality=85)
            slayt(it["_img"], TT, kr).save(os.path.join(pd, "tiktok", f"{si}.jpg"), "JPEG", quality=85)
        # Sessiz önizleme videosu (müzik uygulama içinden eklenecek)
        vid = os.path.join(pd, "tiktok-reels-onizleme-sessiz.mp4")
        cmd = ["ffmpeg", "-y", "-loglevel", "error"]
        for si in range(1, 5):
            cmd += ["-loop", "1", "-t", str(SLAYT_SN), "-i", os.path.join(pd, "tiktok", f"{si}.jpg")]
        cmd += ["-filter_complex", "".join(f"[{i}:v]scale=1080:1920,setsar=1,fps=30[v{i}];" for i in range(4)) + "[v0][v1][v2][v3]concat=n=4:v=1:a=0,format=yuv420p[o]",
                "-map", "[o]", "-c:v", "libx264", "-preset", "veryfast", "-crf", "28", "-movflags", "+faststart", vid]
        subprocess.run(cmd, check=True)
        m = pv["muzik"]
        aciklama = pv["aciklama"].strip()
        etiket = " ".join(pv["etiketler"])
        with open(os.path.join(pd, "PAYLASIM.txt"), "w", encoding="utf-8") as f:
            f.write(f"PAKET {pi:02d}\n\n")
            f.write("SIRA (koleksiyon numarası -> slayt):\n")
            for si, it in enumerate(fotolar, 1):
                f.write(f"  {si}. #{it['no']:02d} {it['baslik']}  ({it['kaynak_sayfa']})\n")
            f.write(f"\nMÜZİK: {m['sanatci']} - {m['sarki']}\n  Önerilen bölüm: {m.get('bolum','nakarat')}\n  Neden: {m['neden']}\n")
            f.write("  Not: Şarkıyı Instagram/TikTok uygulamasının kendi müzik kütüphanesinden ekle (telif için).\n")
            f.write(f"\nINSTAGRAM AÇIKLAMA:\n{aciklama}\n\n{etiket}\n")
            f.write(f"\nTIKTOK AÇIKLAMA:\n{pv.get('tiktok_aciklama', aciklama)}\n\n{etiket}\n")
            f.write("\nKAYNAK SAHİPLERİ: " + ", ".join(sorted({kredi_metni(i).replace('kaynak: ', '') for i in fotolar})) + "\n")
        ozet.append(dict(paket=pi, numaralar=grup, muzik=f"{m['sanatci']} - {m['sarki']}", aciklama=aciklama, etiketler=etiket,
                         fotolar=[dict(no=i["no"], baslik=i["baslik"], dosya=i["dosya"], kaynak=i["kaynak_sayfa"]) for i in fotolar]))
        print(f"[paket] {pi:02d} {grup} {m['sarki']}")

    json.dump(ozet, open(os.path.join(kok, "paketler", "paketler.json"), "w", encoding="utf-8"), ensure_ascii=False, indent=1)
    galeri(kok, items, ozet)
    for ad in ("README.md", "YAYINLAMA-REHBERI.md"):
        p = os.path.join(KOK, "teslim-sablon", ad)
        if os.path.exists(p):
            shutil.copy(p, os.path.join(kok, ad))
    os.makedirs(os.path.join(kok, "arastirma"))
    for ad in ("muzik-listesi.md", "aciklama-ornekleri.md", "kaynaklar.md"):
        shutil.copy(os.path.join(KOK, "arastirma", ad), os.path.join(kok, "arastirma", ad))
    shutil.copy(os.path.join(KOK, "harita.md"), os.path.join(kok, "arastirma", "harita.md"))
    for it in items:
        it.pop("_img", None)

    # 3) ZIP + SHA-256 + geri okuma
    zp = os.path.join(cikti, "komik-foto-paketleri.zip")
    with zipfile.ZipFile(zp, "w", zipfile.ZIP_DEFLATED) as z:
        for r, _, fs in os.walk(kok):
            for fn in sorted(fs):
                full = os.path.join(r, fn)
                z.write(full, os.path.relpath(full, cikti))
    h = sha(zp)
    open(zp + ".sha256", "w").write(f"{h}  komik-foto-paketleri.zip\n")
    print(f"[zip] {zp} {os.path.getsize(zp)} bayt sha256={h}")


def galeri(kok, items, ozet):
    e = html.escape
    p = ['<!doctype html><html lang="tr"><head><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1">',
         '<title>Komik Foto Paketleri</title><style>*{box-sizing:border-box}html,body{max-width:100%;overflow-x:hidden}h1,h2,p,figcaption{overflow-wrap:anywhere}body{font-family:system-ui,sans-serif;margin:0;padding:16px;background:#111;color:#eee}'
         'h2{margin:28px 0 8px}.row{display:grid;grid-template-columns:repeat(auto-fill,minmax(min(150px,45%),1fr));gap:8px}'
         'img{width:100%;border-radius:6px;display:block}figure{margin:0}figcaption{font-size:12px;color:#bbb}a{color:#8cf}'
         'pre{white-space:pre-wrap;background:#222;padding:10px;border-radius:6px}</style></head><body>',
         '<h1>Komik Foto Paketleri</h1><p>Her paket 4 fotoğraf; sıra 1-4-7-10 / 2-5-8-11 / 3-6-9-12 kuralıyla karışık. Müzik uygulama içinden eklenir.</p>']
    for o in ozet:
        p.append(f"<h2>Paket {o['paket']:02d} — 🎵 {e(o['muzik'])}</h2><div class=row>")
        for si, f in enumerate(o["fotolar"], 1):
            p.append(f"<figure><img loading=lazy src='paketler/paket-{o['paket']:02d}/instagram/{si}.jpg'><figcaption>#{f['no']:02d} {e(f['baslik'])} — <a href='{e(f['kaynak'])}'>kaynak</a></figcaption></figure>")
        p.append(f"</div><pre>{e(o['aciklama'])}\n\n{e(o['etiketler'])}</pre>")
    p.append("<h2>Tüm koleksiyon</h2><div class=row>")
    for it in items:
        p.append(f"<figure><img loading=lazy src='{it['dosya']}'><figcaption>#{it['no']:02d} {e(it['baslik'])} — <a href='{e(it['kaynak_sayfa'])}'>kaynak</a></figcaption></figure>")
    p.append("</div></body></html>")
    open(os.path.join(kok, "index.html"), "w", encoding="utf-8").write("\n".join(p))


if __name__ == "__main__":
    main()
