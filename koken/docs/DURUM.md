# Köken: durum ve sonraki tur (kontrol noktası)

Tarih: 2026-10-17. Commit: bu dosyanın bulunduğu commit.

## Bitenler (kanıtlı)
- Motor `web/etym.js`: canlı Vikisözlük ile 18 kelime elle, işçi testinde 20 testin 19'u (+ `sevgi` ayrıca) geçti.
- Arayüz: 390x844 ve 360x640 Chromium, açık/koyu, çevrimdışı, olmayan kelime, çekimli kelime.
- APK: `dist/Koken.apk` (243 KB, imzalı, minSdk 24, yalnızca INTERNET). Çalıştırılmadı.
- Kart: Artifact yayınlandı (sample + downloads yeteneği). Gerçek Claude çalışma zamanında denenmedi.

## Bilinen hatalar (araştırma işçilerinin bulguları, kodda DÜZELTİLMEDİ)
1. TDK adresi yanlış: motor `sozluk.gov.tr/gts` çağırıyor, doğrusu `www.sozluk.gov.tr/gts?ara=` (www'suz adres bağlantıyı sıfırlıyor). CORS `*`, tarayıcıda da çalışır.
2. Nişanyan `/api/words/...` artık 404. Çalışan: `https://www.nisanyansozluk.com/kelime/<k>` (HTML, köken `<meta name="description">` içinde) ve `/kelime/<k>/__data.json`. CORS yok, yalnız APK köprüsünde.
3. `api.etimolojiturkce.com/word/<k>` Türkçe köken veriyor, CORS açık, motorda yok. Ekle.
4. Vikisözlük `action=parse` 429 verirse REST HTML (`/api/rest_v1/page/html/<k>`) yedeğine düşülmeli; HTML'de `data-ety-tree-json` ile hazır köken zinciri var.
5. 429 sırasında çekimli kelimede (kitaplar) yanıltıcı "kayıt bulunamadı" çıkabiliyor; "ulaşılamadı" demeli.
6. githack manifest'i `text/plain` verdiği için web sürümü "ana ekrana ekle" ile kurulmuyor.
7. Android emülatör testi yapılmadı (sistem imajı indirildi, açılış doğrulanmadı).

## Sonraki tur sırası
1 → 2 → 3 → 4 → 5 (motor), APK yeniden derle (`android/build.sh`), emülatörde aç ve bir kelime ara, `card/build-card.py` ile kartı yeni APK ile yeniden yayınla.
