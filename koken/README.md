# Köken

Bir kelime yaz. Uygulama telefonun kendi internetinden bağlanıp kelimenin **kökenini** bulur:
hangi dilden gelmiş, ne demek, hangi parçalardan oluşmuş, yol haritası.

- Sunucu yok. API anahtarı yok. Hesap yok. İnterneti olan herkes kullanır.
- Telefona kelime veritabanı yüklenmez. Her arama canlı yapılır.

## Nasıl çalışıyor (sade anlatım)

1. Kelimeyi yazarsın.
2. Uygulama Vikisözlük'e (en ve tr) doğrudan telefondan sorar. Vikisözlük anahtarsız ve herkese açık.
3. Gelen sayfadaki "Köken" bölümünü ayıklar, yol haritasına (Türkçe ← Arapça ← ...) çevirir.
4. Android uygulamasında ek olarak Nişanyan, TDK ve Etymonline'ı da dener. Biri kapalıysa diğeri yine çalışır.
5. İstersen İngilizce metni "Türkçeye çevir" düğmesiyle çevirir (yine anahtarsız).

## Klasörler

| Klasör | İçindeki |
|---|---|
| `web/` | Motor (`etym.js`) + arayüz. Düz HTML/JS, derleme yok. Tarayıcıda da, APK içinde de aynı kod. |
| `android/` | Küçük Android kabuğu: WebView + "yerel HTTP köprüsü" (tarayıcı kısıtı olmadan istek atar). |
| `dist/` | Hazır `Koken.apk`, tek dosya `koken.html`, SHA256. |
| `card/` | Claude içinde açılan "Köken" kartı (Artifact) şablonu. |
| `tests/` | Motor ve arayüz testleri (canlı ağ). |
| `docs/` | Sözleşme, araştırma, indirme ölçümleri. |

## APK üretmek

```
koken/android/build.sh      # dist/Koken.apk üretir, imzalar, sha256 yazar
```

Anahtar dosyası (`android/koken-release.jks`) repoda durur; böylece yeni sürüm eskisinin üstüne kurulur.
