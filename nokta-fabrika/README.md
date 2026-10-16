# nokta-fabrika

TXT'deki istek: sağ kenarda yuvarlak yüzen balonlu, üstü çizilen / silinen / sürükle-sıralanan hızlı bir Android liste uygulaması; internet araştırması, 3 farklı harita + kümülatif 4. harita, Drive'a APK, Play'de satış hazırlığı.

| Klasör | İçerik |
|---|---|
| `NOTLAR/` | Gerçek araştırma notları: kaynaklar, sistemler, fikirler, araçlar, tasarım referansları, 5940 mağaza yorumu analizi (`yorumlar/*.csv`), rakipler, Play gereksinimleri, `sonuc_karsilastirma.md` |
| `ORTAK_SPEC.md`, `HARITALAR.md` | Ortak kabul ölçütleri ve A/B/C harita tanımları |
| `harita-a/` | Compose + tasarım sistemi (balonu API 26'da çöküyor, bkz. karşılaştırma) |
| `harita-b/` | Views + Room + panel balon |
| `harita-c/` | Sıfır bağımlılık, 33 KB APK |
| `harita-d/` | Nihai sürüm (B tabanı + A/C dersleri) |
| `RELEASE/` | **Teslim edilen** `nokta-1.1.1-release.apk` / `.aab` (zil ile hatırlatma + uzun basma düzeltmesi), önceki sürümler ve `SHA256SUMS.txt` |
| `PLAY_STORE/` | Gizlilik politikası (TR/EN), mağaza metni, Play Console beyan taslakları, `gorseller/` (ikon, tanıtım görseli, 5 ekran görüntüsü) |

Not: `../nokta-app/` ilk, tek başına yapılmış v1'dir (araştırmasız); nihai sürüm bu klasördeki D'dir.
İmza anahtarları (`*/keystore/`) git'te yoktur; `harita-d/keystore` kaybolursa güncelleme yayınlanamaz, yedekle.
