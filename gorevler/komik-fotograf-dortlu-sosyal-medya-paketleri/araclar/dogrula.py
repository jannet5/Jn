#!/usr/bin/env python3
"""Kabul doğrulaması: python3 araclar/dogrula.py <cikti_klasoru>"""
import csv, hashlib, json, os, subprocess, sys, zipfile
from PIL import Image

cikti = os.path.abspath(sys.argv[1])
kok = os.path.join(cikti, "komik-foto-paketleri")
zp = os.path.join(cikti, "komik-foto-paketleri.zip")
hata = []


def ok(kosul, mesaj):
    print(("GEÇTİ " if kosul else "KALDI ") + mesaj)
    if not kosul:
        hata.append(mesaj)


# H1 — klasör + kaynaklar
rows = list(csv.DictReader(open(os.path.join(kok, "foto", "kaynaklar.csv"), encoding="utf-8-sig")))
ok(len(rows) == 48, f"kaynaklar.csv 48 satır ({len(rows)})")
ok(all(r["kaynak_sayfa"].startswith("https://") for r in rows), "her fotoğrafın HTTPS kaynak sayfası var")
for r in rows:
    p = os.path.join(kok, r["dosya"])
    h = hashlib.sha256(open(p, "rb").read()).hexdigest()
    im = Image.open(p); im.verify()
    if h != r["sha256"]:
        hata.append("sha uyuşmuyor " + r["dosya"])
ok(not [e for e in hata if e.startswith("sha")], "48 fotoğraf açılıyor ve SHA-256 eşleşiyor")
ok(len({r["sha256"] for r in rows}) == 48, "fotoğraflar birbirinin kopyası değil")
x_say = sum(1 for r in rows if r["x_gonderisi"])
print(f"BİLGİ doğrudan X gönderisi bağlantılı fotoğraf: {x_say}")

# H2 — dörtlü karışık sıra
pk = json.load(open(os.path.join(kok, "paketler", "paketler.json"), encoding="utf-8"))
beklenen = [[b + s + 3 * k for k in range(4)] for b in range(0, 48, 12) for s in (1, 2, 3)]
ok([p["numaralar"] for p in pk] == beklenen, "paket sırası 1-4-7-10 / 2-5-8-11 / 3-6-9-12 kuralına uyuyor")
hepsi = sorted(n for p in pk for n in p["numaralar"])
ok(hepsi == list(range(1, 49)), "her fotoğraf tam bir kez kullanıldı")
for p in pk:
    d = os.path.join(kok, "paketler", f"paket-{p['paket']:02d}")
    for si in range(1, 5):
        for alt, boy in (("instagram", (1080, 1350)), ("tiktok", (1080, 1920))):
            im = Image.open(os.path.join(d, alt, f"{si}.jpg"))
            if im.size != boy:
                hata.append(f"boyut {d}/{alt}/{si}")
    sure = subprocess.run(["ffprobe", "-v", "error", "-show_entries", "format=duration", "-of", "csv=p=0",
                           os.path.join(d, "tiktok-reels-onizleme-sessiz.mp4")], capture_output=True, text=True).stdout.strip()
    if not (9.5 <= float(sure) <= 10.5):
        hata.append(f"video süresi {d} {sure}")
    t = open(os.path.join(d, "PAYLASIM.txt"), encoding="utf-8").read()
    for anahtar in ("MÜZİK:", "INSTAGRAM AÇIKLAMA:", "TIKTOK AÇIKLAMA:"):
        if anahtar not in t:
            hata.append(f"{d} {anahtar} eksik")
ok(not [e for e in hata if e.startswith(("boyut", "video", "paket"))], "12 paket: IG 1080x1350 ×4, TikTok 1080x1920 ×4, 10 sn önizleme, PAYLASIM.txt tam")

# H3/H4 — müzik + açıklama
muz = [p["muzik"] for p in pk]
ok(len(set(muz)) == 12, "12 pakette 12 farklı müzik")
ok(all(len(p["aciklama"]) > 10 and p["etiketler"].count("#") >= 3 for p in pk), "her pakette açıklama ve ≥3 etiket")

# ZIP geri okuma
beklenen_sha = open(zp + ".sha256").read().split()[0]
gercek = hashlib.sha256(open(zp, "rb").read()).hexdigest()
ok(beklenen_sha == gercek, f"ZIP SHA-256 eşleşiyor ({gercek[:16]}…)")
with zipfile.ZipFile(zp) as z:
    ok(z.testzip() is None, f"ZIP bütünlük testi ({len(z.namelist())} dosya)")
    ok(z.read("komik-foto-paketleri/foto/kaynaklar.csv") == open(os.path.join(kok, "foto", "kaynaklar.csv"), "rb").read(),
       "ZIP içinden geri okunan kaynaklar.csv diskteki ile aynı")

print("\nSONUÇ:", "TÜM KABULLER GEÇTİ" if not hata else f"{len(hata)} HATA: {hata[:5]}")
sys.exit(1 if hata else 0)
