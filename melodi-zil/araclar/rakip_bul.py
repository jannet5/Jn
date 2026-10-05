# Google Play'de zil sesi yapıcı rakipleri bulup yorumlarını CSV'ye çeken araç
import csv, sys  # CSV yazmak ve çıkışta hata kodu vermek için
from google_play_scraper import search, app, reviews, Sort  # Play arama, uygulama bilgisi ve yorum fonksiyonları
sorgular = ["ringtone maker", "zil sesi yapma", "youtube ringtone", "melody ringtone maker"]  # aranacak anahtar kelimeler
gorulen = {}  # paket adına göre tekrarları engellemek için sözlük
for s in sorgular:  # her sorgu için
    try:  # arama hata verebilir
        for r in search(s, lang="en", country="us", n_hits=8):  # ilk 8 sonuç
            gorulen.setdefault(r["appId"], r)  # yeni paketse kaydet
    except Exception as e:  # arama başarısızsa
        print("arama hatası", s, e)  # ekrana yaz
print("Bulunan paket sayısı:", len(gorulen))  # kaç rakip bulundu
with open("fabrika/RAKIPLER_HAM.csv", "w", newline="", encoding="utf-8-sig") as f:  # rakip özet tablosu
    w = csv.writer(f); w.writerow(["paket", "ad", "puan", "indirme", "fiyat", "gelistirici"])  # başlıklar
    for pid, r in gorulen.items():  # her rakip için
        try:  # detay çekmeyi dene
            a = app(pid, lang="en", country="us")  # uygulama detayı
            w.writerow([pid, a.get("title"), a.get("score"), a.get("installs"), a.get("price"), a.get("developer")])  # satır yaz
        except Exception as e:  # detay alınamazsa
            w.writerow([pid, r.get("title"), r.get("score"), "", "", r.get("developer")])  # arama bilgisiyle yaz
toplam = 0  # toplam yorum sayacı
for pid in list(gorulen)[:12]:  # en fazla 12 rakibin yorumları
    for dil, ulke in (("en", "us"), ("tr", "tr")):  # İngilizce ve Türkçe yorumlar
        try:  # yorum çekmeyi dene
            rs, _ = reviews(pid, lang=dil, country=ulke, sort=Sort.NEWEST, count=120)  # en yeni 120 yorum
        except Exception as e:  # başarısızsa
            print("yorum hatası", pid, dil, e); continue  # atla
        if not rs: continue  # yorum yoksa atla
        with open(f"fabrika/yorumlar/{pid}_{dil}.csv", "w", newline="", encoding="utf-8-sig") as f:  # paket+dil dosyası
            w = csv.DictWriter(f, fieldnames=["uygulama", "tarih", "yildiz", "yorum", "begeni"]); w.writeheader()  # başlıklar
            for r in rs:  # her yorum
                w.writerow({"uygulama": pid, "tarih": str(r.get("at", ""))[:10], "yildiz": r.get("score"), "yorum": (r.get("content") or "").replace("\n", " "), "begeni": r.get("thumbsUpCount", 0)})  # satır
        toplam += len(rs)  # sayacı artır
print("Toplam yorum:", toplam)  # özet
