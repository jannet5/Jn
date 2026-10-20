# Ödeme ve destek planları (Google Play)

Kısa karar: varsayılan sürüm ZORUNLU ödeme istemez. Hiçbir şey kilitlenmez. "TekPanel'e destek ol"
menüsünden 1 / 3 / 6 / 12 aylık destek planları alınır. İstersen aynı kodla iki deneme sürümü
(3 gün sessiz, 3 gün uyarılı) ayrı test kanallarına yüklenebilir.

## Neden varsayılan "destek" (bağış gibi), neden zorunlu değil

- Kurucunun kendi sözü: "dayatma yapmasak, kişi kullansa, beğenirse destek olsa."
- Rakip yorumlarında en sık üçüncü şikâyet fiyat/abonelik baskısı; kullanıcılar tek seferlik
  veya ucuz ödeme istiyor (bkz. `docs/arastirma/01-rakipler-google-play.md`).
- Güven: telefondaki mesajlara erişen bir uygulama, kilitleyerek para istemeye başlarsa
  "virüs mü" şüphesi büyür. Destek yolu bu şüpheyi artırmaz.
- Sonuç: A/B kodu duruyor, ama varsayılan kapalı. Kararı sen verirsin (aşağıda).

## Planlar (senin istediğin fiyat mantığı)

- 1 ay = 5 $
- 3 ay = 10 $ (aylık 3,33 $; %33 tasarruf)
- 6 ay = 4 ayın parası = 20 $ (%33 tasarruf)
- 12 ay = 9 ayın parası = 45 $ (%25 tasarruf)
- Türkiye fiyatı TL olarak Play Console'da elle girilir (kur oynar; Play her ülkeye ayrı fiyat
  sistemi sunuyor). Uygulama fiyatı Play'den okur, kodda sabit fiyat yok.

## Play Console'da yapılacaklar (kullanıcı eylemi; ben hesabına giremem)

1. Ürün: Para kazanma > Ürünler > Abonelikler > "Abonelik oluştur"
   - Ürün kimliği: `tekpanel_support` (kodda `PlayPurchaseGateway.PRODUCT_ID`).
2. Aynı ürüne 4 temel plan ekle. Kimlikler tam olarak şunlar olmalı:
   - `monthly` (1 ay), `quarterly` (3 ay), `semiannual` (6 ay), `yearly` (12 ay).
   - Hepsi "otomatik yenilenen". Teklif (offer) EKLEME: uygulama yalnız teklifsiz temel planı gösterir.
3. Fiyatlar: yukarıdaki USD fiyatlarını gir, Türkiye için TL'yi elle düzenle.
4. Satın alma onayı: uygulama 3 gün dolmadan kendisi onaylar (acknowledge). Onaylanmazsa Play iade eder.
5. Test: Ayarlar > Lisans testi'ne kendi Gmail'ini ekle; "iç test" kanalına AAB yükle; gerçek para çekilmez.

## Tek seferlik destek (araştırmanın en çok istenen şeyi)

- Rakip yorumlarında en sık dilek: "tek seferlik bağış seçeneği olsa desteklerdim" (bkz. araştırma 01, Fiyat modelleri).
- Play Console > Para kazanma > Ürünler > Uygulama içi ürünler: 3 ürün, "tüketilebilir":
  - `tip_small` ("Bir çay ısmarla"), `tip_medium` ("Bir kahve ısmarla"), `tip_large` ("Bir yemek ısmarla").
  - Önerilen fiyat: 1 $ / 3 $ / 8 $ (Türkiye için TL'yi elle gir).
- Uygulama satın alma sonrası ürünü hemen tüketir; aynı destek tekrar verilebilir.

## Deneme sürümleri (A/B, 20 + 20 kişi)

- Üç mod, build sırasında seçilir: `-Pmonetization=donate | trial_silent | trial_warn`
  - `donate` (varsayılan): hiçbir şey kilitlenmez, hiçbir şey kendiliğinden açılmaz.
  - `trial_silent`: ilk 3 gün hiçbir şey görünmez. 3. gün bitince fiyat sayfası açılır. "Şimdilik
    değil" derse o gün bir daha çıkmaz (hiçbir özellik kilitlenmez).
  - `trial_warn`: 3 gün boyunca küçük, sakin bir "N ücretsiz gün kaldı" satırı görünür; sonra aynı sayfa.
- Play'de tek uygulama görünür. İki sürüm iki ayrı kapalı test kanalına yüklenir:
  - Kanal A: `./gradlew :app:bundlePlayRelease -Pmonetization=trial_silent`
  - Kanal B: `./gradlew :app:bundlePlayRelease -Pmonetization=trial_warn`
  - Her kanalın kurulum/abonelik sayıları Play Console'da ayrı görünür; karşılaştırma oradan yapılır.
  - İnternet yok kuralı yüzünden uygulamanın içinde analitik YOK. Ölçüm Play'in kendi raporlarından.
- Kilit konusunda dürüst not: sert kilit (3 gün sonra uygulama çalışmasın) istenmedi, o yüzden
  yazılmadı. İstenirse `TrialPolicy.Gate.Paywall`'ı ekranı kapatan bir hâle çevirmek küçük iş.

## Güncelleme (kendini güncelleme)

- Play sürümü: Google Play uygulamayı kendisi günceller (varsayılan ayar).
- Play'in "uygulama içi güncelleme" kütüphanesi bilerek KULLANILMADI: Google Data Transport
  üzerinden INTERNET izni getiriyor (doğrulandı), bu da "internet izni yok" sözünü bozardı.
- Sideload (APK) sürümü: internet izni olmadığı için kendini güncelleyemez. Yeni APK'yı aynı
  linkten indirip üstüne kurarsın (aynı imza anahtarı sayesinde veriler korunur).

## İnternet izni özeti (derlenmiş manifestten ölçüldü)

- sideload sürümü: INTERNET yok.
- play sürümü: Play Billing kütüphanesi INTERNET ve ACCESS_NETWORK_STATE izinlerini getiriyor.
  Uygulamanın kendi kodunda mesajı gönderen hiçbir ağ kodu yok; uygulamadaki gizlilik satırı
  kurulu manifesti okuyup doğruyu yazar (iki sürümde farklı cümle).
