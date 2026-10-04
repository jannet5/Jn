import argparse  # komut satırı parametrelerini okumak için argparse modülü
import csv  # CSV dosyası yazmak için csv modülü
import json  # App Store'dan gelen JSON cevabını çözmek için json modülü
import sys  # hata durumunda çıkış yapmak için sys modülü
import urllib.request  # App Store RSS adresine istek atmak için urllib modülü
from collections import Counter  # yıldız dağılımını saymak için Counter sınıfı
from pathlib import Path  # çıktı dosya yolu için Path sınıfı

ALANLAR = ["kaynak", "uygulama", "tarih", "yildiz", "baslik", "yorum", "begeni", "surum"]  # CSV sütun adları


def play_yorumlari(paket: str, adet: int, dil: str, ulke: str) -> list[dict]:  # Google Play yorumlarını çeken fonksiyon
    try:  # paket kurulu mu diye denenecek
        from google_play_scraper import Sort, reviews  # Play yorum kütüphanesinden sıralama ve yorum fonksiyonu alınıyor
    except ImportError:  # paket kurulu değilse
        sys.exit("Önce kur: pip install google-play-scraper")  # kurulum talimatıyla çıkılıyor
    sonuc, _ = reviews(paket, lang=dil, country=ulke, sort=Sort.NEWEST, count=adet)  # en yeni yorumlar istenen adette çekiliyor
    return [  # her yorum ortak formata çevrilip liste olarak döndürülüyor
        {  # tek bir yorum sözlüğü başlıyor
            "kaynak": "play",  # kaynağın Google Play olduğu yazılıyor
            "uygulama": paket,  # uygulamanın paket adı yazılıyor
            "tarih": str(r.get("at", ""))[:10],  # yorum tarihi YYYY-AA-GG olarak alınıyor
            "yildiz": r.get("score", ""),  # verilen yıldız sayısı alınıyor
            "baslik": "",  # Play yorumlarında başlık olmadığı için boş bırakılıyor
            "yorum": (r.get("content") or "").replace("\n", " "),  # yorum metni alınıp satır sonları boşluğa çevriliyor
            "begeni": r.get("thumbsUpCount", 0),  # yorumun kaç kişi tarafından faydalı bulunduğu alınıyor
            "surum": r.get("reviewCreatedVersion") or "",  # yorumun yazıldığı uygulama sürümü alınıyor
        }  # sözlük bitiyor
        for r in sonuc  # çekilen her yorum için tekrarlanıyor
    ]  # liste bitiyor


def appstore_yorumlari(app_id: str, ulke: str, adet: int) -> list[dict]:  # App Store yorumlarını çeken fonksiyon
    liste: list[dict] = []  # toplanan yorumlar için boş liste oluşturuluyor
    for sayfa in range(1, 11):  # Apple RSS en fazla 10 sayfa (≈500 yorum) verdiği için 1'den 10'a dönülüyor
        url = f"https://itunes.apple.com/{ulke}/rss/customerreviews/page={sayfa}/id={app_id}/sortby=mostrecent/json"  # sayfanın adresi oluşturuluyor
        try:  # istek hata verebileceği için deneniyor
            with urllib.request.urlopen(url, timeout=20) as cevap:  # adrese istek atılıp cevap açılıyor
                veri = json.load(cevap)  # gelen JSON çözülüyor
        except Exception as hata:  # bir hata olursa
            print(f"  ! sayfa {sayfa} alınamadı: {hata}")  # hata ekrana yazdırılıyor
            break  # döngüden çıkılıyor
        girdiler = veri.get("feed", {}).get("entry", [])  # yorum girdileri JSON'dan alınıyor
        if isinstance(girdiler, dict):  # tek yorum varsa liste yerine sözlük gelebiliyor
            girdiler = [girdiler]  # o durumda listeye çevriliyor
        yorumlar = [g for g in girdiler if "im:rating" in g]  # sadece puanı olan girdiler (gerçek yorumlar) alınıyor
        if not yorumlar:  # bu sayfada yorum yoksa
            break  # daha fazla sayfa olmadığı için döngüden çıkılıyor
        for g in yorumlar:  # sayfadaki her yorum dolaşılıyor
            liste.append({  # yorum ortak formatta listeye ekleniyor
                "kaynak": "appstore",  # kaynağın App Store olduğu yazılıyor
                "uygulama": app_id,  # uygulamanın App Store id'si yazılıyor
                "tarih": g.get("updated", {}).get("label", "")[:10],  # yorum tarihi alınıyor
                "yildiz": int(g["im:rating"]["label"]),  # yıldız sayısı sayıya çevrilerek alınıyor
                "baslik": g.get("title", {}).get("label", ""),  # yorum başlığı alınıyor
                "yorum": g.get("content", {}).get("label", "").replace("\n", " "),  # yorum metni tek satıra çevrilerek alınıyor
                "begeni": g.get("im:voteSum", {}).get("label", 0),  # yorumun oy toplamı alınıyor
                "surum": g.get("im:version", {}).get("label", ""),  # uygulama sürümü alınıyor
            })  # ekleme bitiyor
        if len(liste) >= adet:  # istenen adede ulaşıldıysa
            break  # daha fazla sayfa çekilmiyor
    return liste[:adet]  # listenin istenen adet kadarı döndürülüyor


def main() -> None:  # programın ana fonksiyonu
    p = argparse.ArgumentParser(description="Mağaza yorumlarını CSV'ye çeker")  # parametre okuyucu oluşturuluyor
    kaynak = p.add_mutually_exclusive_group(required=True)  # --play ve --appstore'dan sadece biri seçilebilsin diye grup açılıyor
    kaynak.add_argument("--play", help="Google Play paket adı (örn. com.spotify.music)")  # Play paket adı parametresi
    kaynak.add_argument("--appstore", help="App Store id (linkteki id123456 kısmındaki sayı)")  # App Store id parametresi
    p.add_argument("--adet", type=int, default=300, help="Kaç yorum çekilsin (varsayılan 300)")  # yorum adedi parametresi
    p.add_argument("--dil", default="tr", help="Dil kodu, sadece Play için (varsayılan tr)")  # dil parametresi
    p.add_argument("--ulke", default="tr", help="Ülke kodu (varsayılan tr)")  # ülke parametresi
    p.add_argument("--yildiz", default="", help="Sadece bu yıldızlar, örn. 2,3")  # yıldız filtresi parametresi
    p.add_argument("--cikti", required=True, help="CSV dosya yolu")  # çıktı dosyası parametresi
    a = p.parse_args()  # parametreler okunuyor

    cekilecek = a.adet * 5 if a.yildiz else a.adet  # filtre varsa filtre sonrası yeterli yorum kalsın diye 5 katı çekiliyor
    if a.play:  # Play seçildiyse
        yorumlar = play_yorumlari(a.play, cekilecek, a.dil, a.ulke)  # Play yorumları çekiliyor
    else:  # App Store seçildiyse
        yorumlar = appstore_yorumlari(a.appstore, a.ulke, cekilecek)  # App Store yorumları çekiliyor

    if a.yildiz:  # yıldız filtresi verildiyse
        izinli = {int(y) for y in a.yildiz.split(",")}  # virgülle ayrılmış yıldızlar sayı kümesine çevriliyor
        yorumlar = [y for y in yorumlar if int(y["yildiz"]) in izinli][: a.adet]  # sadece istenen yıldızdakiler tutulup istenen adede kırpılıyor

    cikti = Path(a.cikti)  # çıktı yolu Path nesnesine çevriliyor
    cikti.parent.mkdir(parents=True, exist_ok=True)  # çıktının klasörü yoksa oluşturuluyor
    with cikti.open("w", newline="", encoding="utf-8-sig") as f:  # CSV, Excel'de Türkçe karakterler bozulmasın diye BOM'lu UTF-8 açılıyor
        yazici = csv.DictWriter(f, fieldnames=ALANLAR)  # sözlükleri satır olarak yazacak yazıcı oluşturuluyor
        yazici.writeheader()  # sütun başlıkları yazılıyor
        yazici.writerows(yorumlar)  # tüm yorumlar yazılıyor

    dagilim = Counter(int(y["yildiz"]) for y in yorumlar)  # her yıldızdan kaç yorum olduğu sayılıyor
    print(f"✅ {len(yorumlar)} yorum → {cikti}")  # toplam yorum ve dosya yolu yazdırılıyor
    print("Yıldız dağılımı: " + "  ".join(f"{k}★:{dagilim.get(k, 0)}" for k in range(1, 6)))  # 1'den 5'e yıldız dağılımı yazdırılıyor


if __name__ == "__main__":  # dosya doğrudan çalıştırıldıysa
    main()  # ana fonksiyon çağrılıyor
