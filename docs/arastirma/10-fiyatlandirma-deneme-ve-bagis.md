# Sende: fiyat, deneme ve destek (bağış) kararı
Araştırma kolu: para-kazanma

- Tarih: 22 Ekim 2026. Bu dosya bir karar belgesidir; koda dokunulmadı.
- Bu sürüm 21 Ekim taslağının ikinci geçişidir. İlk geçişte arama
  kotası dolmuştu; şimdi Stack Overflow, GitHub issue, Hacker News
  ve 2026 vakaları eklendi. Öneri bu kanıta göre güncellendi.
- Okuma sırası: "Kısa cevap", sonra "ÖNERİ" (bölüm 11), sonra
  "Teknik şartname" (bölüm 12). Geri kalanı kanıt ve gerekçedir.

## Kısa cevap

- Model: "Açık çekirdek + Sende Pro + destek ürünü (bahşiş)".
  - Mesajı yakalamak, okumak, geçmiş HER ZAMAN ücretsiz kalır.
  - Pro yalnızca zaman kazandıran iş araçlarını açar.
  - Neden: saf bağış çok az gelir getirir (bölüm 5-b), sert kilit
    bu kategoride güveni ve puanı yakar (bölüm 5, 10).
- Deneme: 7 gün, kartsız, uygulama içinde (yerel), AÇIKÇA söylenen.
  - "Hiçbir şey söylemeden 3 gün" önerilmez (bölüm 6-b).
- Ödeme: Play sürümünde yalnız Google Play Billing (zorunlu).
  - Sideload (APK) sürümünde ödeme alınamaz.
- Pro ürünleri (3): Aylık, Yıllık, Ömür boyu (tek seferlik).
  - Kurucunun 1/3/6/12 basamağı tutarsız ve Paycell gibi operatör
    faturası sınırlarına takılır (bölüm 6-a, 4-b).
- Başlangıç fiyatı (TL, KDV dahil, VARSAYIM):
  - Aylık 99,99; Yıllık 599,99; Ömür boyu 999,99.
  - Destek ürünleri: 49,99 / 99,99 / 249,99.
  - Önceki taslak aylık 129,99 diyordu; yeni TL çıpaları
    nedeniyle indirildi (bölüm 4-b). İlk 8 haftada gözden geçir.
- Bağış: Play sürümünde dışarı bağış linki YOK. 2026'da bile
  reddediliyor (AnkiDroid, 28 Ağustos 2026; bölüm 1-b).
  - Yol: tüketilebilir ürün. Kelime "destek", karşılığında küçük
    bir "Destekçi" rozeti (işlev vermeyen).
- 20+20 kişilik A/B: dönüşümü ÖLÇEMEZ. Anlaşılırlık ve güven
  testi olarak kullan (bölüm 6-d, 8).
- INTERNET izni: Billing'in kendisi istemiyor, Data Transport
  istiyor. Çıkarmayı dene, ama KANITLANMADI: iki bağımsız kaynak da
  cihazda denemedi (bölüm 3). Yedek plan: eş uygulama (bölüm 3).
- Dürüst gelir beklentisi: abonelik uygulamaları için kabaca 1.000
  kurulum başına 100 dolar (14. gün). Saf bağışta bunun kabaca onda
  biri (kaba çıkarım). Bölüm 5.

İlk taslaktan değişenler:
- Yeni kanıt: bağışta 6 bağımsız vaka (2016-2026), tek seferlik
  ürünlerde Play fiyat deneyi var, Paycell işlem sınırı 300 TL,
  Billing INTERNET konusunda 2 bağımsız kaynak, eş uygulama örneği.
- Yeni öneri: 6 aylık plan çıktı, "Ömür boyu" girdi, fiyatlar
  düştü, 15% hizmet bedeli kaydı (Account Group) eklendi.

## Okuma notu ve sınırlar

- Etiketler:
  - [resmi] Google/Android/Firebase/TCMB resmi sayfası, bu kolda açıldı.
  - [ölçüm] Kendim indirip/okuyup ölçtüm (Maven paketleri, TCMB XML).
  - [çoklu] Birden çok bağımsız kaynakta aynı bulgu.
  - [forum] Geliştirici/kullanıcı topluluğu (SO, GitHub, HN, LWN).
  - [tek] Tek kaynak; sağlama yapılamadı.
  - [çıkarım] Benim yorumum veya hesabım.
  - [doğrulanamadı] Bulunamadı veya kanıtlanamadı.
- Kaynak erişimi bu geçişte:
  - Çalışan: genel arama, WebFetch, Stack Exchange API, HN Algolia
    API, GitHub issue/PR sayfaları, Google/Android yardım sayfaları.
  - ÇALIŞMAYAN: Reddit (arama süzgeci, RSS ve old.reddit engelli)
    ve XDA'dan bulgu çıkmadı. Reddit cümleleri önceki kollardan
    (01, 03) gelir ve [tek] sayılır.
  - WebFetch özetleyicisi küçük bir model. Kritik metinler (politika,
    ücret, kayıt, doğrulama, kanun) curl ile indirilip okundu.
- Lisans notu:
  - Billing AAR'ı "Android SDK Lisansı", Data Transport Apache-2.0
    (POM dosyalarında yazıyor). Yalnız manifest ve sınıf adlarına
    baktım; kod kopyalanmadı.
    https://dl.google.com/android/maven2/com/android/billingclient/billing/8.0.0/billing-8.0.0.pom
    https://dl.google.com/android/maven2/com/google/android/datatransport/transport-backend-cct/3.1.8/transport-backend-cct-3.1.8.pom
  - Simple Thank You: GPL-3.0-only (F-Droid sayfası). Fossify Thank
    You sayfası lisans adı vermiyor. Eş uygulama fikri için YALNIZ
    sayfa metinlerini okudum, kod okumadım ve kopyalamadım.
  - AnkiDroid, StreetComplete, andOTP, HoldMyTrack, audio-blackbox:
    yalnız issue/PR tartışmalarını okudum, kod okumadım.

## 1. Google Play ödeme politikası

- Dijital ürün için Play Billing zorunlu: ücretli indirme ve
  uygulamada özellik/içerik/hizmet satışı. Örnekler arasında
  "reklamsız sürüm", "ücretsiz sürümde olmayan yeni özellikler" ve
  abonelik var. Metin 22 Ekim 2026'da indirilip okundu. [resmi]
  - https://support.google.com/googleplay/android-developer/answer/9858738
  - https://support.google.com/googleplay/android-developer/answer/10281818
- Play Billing'in KULLANILMAYACAĞI durumlar: fiziksel mal ve
  hizmet, "kişiler arası ödemeler, açık artırma ve vergiden muaf
  bağışlar" ("tax exempt donations"). [resmi]
  - https://support.google.com/googleplay/android-developer/answer/9858738
- Yorum: "vergiden muaf bağış" kâr amacı gütmeyen kuruluş mantığıdır.
  Ticari bir geliştiricinin kendi uygulaması için istediği paranın bu
  muafiyete girdiğine dair cümle yok. [çıkarım; bölüm 1-b doğruluyor]
- SSS'de bahşiş: bahşişin %100'ü "yaratıcıya" gidiyor ve karşılığında
  dijital içerik/hizmet (çıkartma, rozet, özel emoji dahil)
  verilmiyorsa kişiler arası ödeme sayılıyor, Play Billing gerekmiyor;
  "bunlardan biri doğru değilse Play Billing kullanılmalı". Cümle
  içerik üreticileri için yazılmış. Play üzerinden ödeme alınca %100
  yaratıcıya gitmez (hizmet bedeli kesilir) ve rozet gibi bir şey
  verirsen koşul zaten bozulur. Sonuç: bizim için pratik yol Play
  Billing. [resmi + çıkarım]
  - https://support.google.com/googleplay/android-developer/answer/10281818
- Uygulama içinden Play dışı ödeme yöntemine yönlendirmek yasak:
  link, buton, webview, mesaj, reklam, kayıt akışı dahil. SSS:
  "alternatif ödemeye götürebilecek bir web sayfasına doğrudan link"
  de yasak. Uygulamanın DIŞINDA başka satın alma seçeneklerini anlatmak
  serbest. [resmi]
  - https://support.google.com/googleplay/android-developer/answer/10281818
- "Uygun ülkelerde" alternatif fatura ve harici link programları var.
  Türkiye'nin uygun olup olmadığı politika sayfasında yazmıyor.
  Hizmet bedeli sayfasındaki takvimde "genişletilmiş fatura seçeneği"
  yalnız AB/BK/ABD (30 Haziran 2026), AU/JP (30 Eylül 2026), Güney
  Kore (31 Aralık 2026) ve "dünyanın geri kalanı" (30 Eylül 2027) için
  geçiyor. Türkiye adıyla anılmıyor; 2027'ye düşeceği ÇIKARIMDIR.
  [resmi + çıkarım]
  - https://support.google.com/googleplay/android-developer/answer/16954621
- Play DIŞINDA dağıtılan uygulama (APK, kendi site) Play Billing
  kullanmak zorunda değil; iade ve destek doğrudan verilebilir. [resmi]
- Fiyat şeffaflığı: uygulamadaki fiyat Play'in ödeme ekranındaki fiyatla
  aynı olmalı; anlatılan özellik ek ücret istiyorsa mağaza girdisi bunu
  söylemeli. [resmi]
  - https://support.google.com/googleplay/android-developer/answer/9858738
- Abonelik politikası (deneme için de geçerli): [resmi]
  - Teklifin şartlarını açık yaz; denemenin ücretliye nasıl ve ne zaman
    döneceğini söyle. Ücretliye otomatik geçişi açıklamayan teklif ihlal.
  - Deneme olmadan içeriğe nasıl erişileceği görülebilmeli.
  - Yıllık planı en belirgin yerde AYLIK fiyatla göstermek ihlal örneği.
  - Aylık plan "her ay otomatik yenilenir" demeli.
  - İptal için uygulamada kolay yol (Play abonelik merkezine link) şart.
  - "Aldatıcı veya manipülatif satın alma deneyimi" yasak.
  - https://support.google.com/googleplay/android-developer/answer/9900533
- Çıkarım: Play sürümünde bağış = tüketilebilir uygulama içi ürün
  (bahşiş). Kodda zaten var (`tip_small/medium/large`). [çıkarım]

### 1-b. Bağış ve bahşiş: gerçek vakalar (2016-2026)

- Soru: "Uygulama içine dış bağış linki koyabilir miyim?" Cevap, altı
  bağımsız vakada aynı: HAYIR. [çoklu + forum]
  - 2016: Stack Overflow'da bir geliştirici, Google Play ekibinin
    cevabını aktarıyor: bağış yalnız doğrulanmış kâr amacı gütmeyen
    kuruluşlar için; aksi halde bağış işlevini kaldır, Play Billing
    veya ücretli uygulama kullan. (Cevap tarihi 12 Şubat 2016.)
    https://stackoverflow.com/questions/17783664
  - 2020: Stack Overflow'da başka bir geliştirici, önce PayPal.me
    linki, sonra KENDİ sitesindeki bağış alt sayfasına link verdi;
    iki kez reddedildi. (Cevap tarihi 3 Kasım 2020.)
    https://stackoverflow.com/questions/33270564
  - Ekim 2019: andOTP Play'den kaldırıldı; çözüm "play" derleme
    çeşidinden bağış linklerini çıkarmak. 19 Ekim'de geri döndü.
    https://github.com/andOTP/andOTP/issues/396
  - Nisan 2021: Pixel Wheels (ücretsiz oyun) bağış linki yüzünden
    reddedildi. Hacker News yorumlarında "bağış Play'de gri alan".
    https://news.ycombinator.com/item?id=26761594
    https://news.ycombinator.com/item?id=21270739 (Ekim 2019)
  - Şubat 2022: StreetComplete, bağış linki içeren GitHub ana sayfasına
    link verdiği için bile reddedildi; link kalkınca yayına döndü.
    https://github.com/streetcomplete/StreetComplete/issues/3768
  - 29 Ağustos 2026: AnkiDroid. Google'ın bildirimleri 20 Temmuz,
    6, 7 ve 28 Ağustos 2026. Open Collective bağış linki yüzünden
    güncelleme reddedildi. Vakıf 501(c)(6) belgesi gönderdi; Google
    "kâr amacı gütmeyen ve vergiden muaf" saymadı. Ekip 31 Ağustos'ta
    "play" çeşidinden bağış linklerini çıkardı.
    https://github.com/ankidroid/Anki-Android/issues/21656
    https://gigazine.net/gsc_news/en/20260902-ankidroid-google-play-policy-violation/
  - 2 Ekim 2026: HoldMyTrack, "Play'in ödeme politikası dışarı
    yönlendirmeye izin vermiyor" diyerek Bağış'ı Play sürümünden çıkardı;
    web APK'sında kaldı.
    https://github.com/HoldMyTrack/holdmytrack/pull/160
- Önemli ayrıntı (AnkiDroid, 2026): Google'ın bildirimi seçenekleri
  sayıyor: bağış işlevini kaldır, "bağışları Google Play'in uygulama
  içi satın alma sistemiyle topla", ABD alternatif ödeme programı veya
  harici içerik linkleri programına gir. Yani uygulama içi satın alma
  yolu Google'ın kendi saydığı yol. (Ekibin aktarımı; Google'ın ham
  e-postası görülmedi.) [forum]
  - https://github.com/ankidroid/Anki-Android/issues/21656
- Risk (tek kaynak): aynı konuda bir yorumcu (22 Eylül 2026)
  "bağış politikası ihlali" diye "uygulamayla ilgisiz bir bahşiş
  kutusu" için de uyarı aldıklarını yazdı. Bahşişin uygulama içi
  satın alma mı dış ödeme mi olduğu yazılmamış; Google'ın o konuda ne
  dediği yok. Yorumcu "Play uygulamayı sıkılaştırıyor" diye tahmin
  ediyor (kanıtsız). [tek, ayrıntı yok]
  - https://github.com/ankidroid/Anki-Android/issues/21656
- Sonuçlar: [çıkarım]
  - Play sürümünde dış bağış/Patreon/Open Collective linki YOK. Hesap
    askıya alma riski var, kazanç küçük.
  - Bahşiş yalnız Play Billing ile ve "ürün" gibi sunulur: ad "Çay
    ısmarla", karşılığında küçük, işlevsiz bir "Destekçi" rozeti.
    Böylece ödeme normal bir dijital öğe satışı olur; "bağış" olmaz.
    (andOTP tartışmasında bir yorumcu da bağış yerine küçük bir ücretli
    özellik önermişti.)
  - Sideload sürümü Play kuralına tabi değil; ama iki sürümün
    davranışı ayrışmasın diye ilk sürümde link konmuyor.
  - İlk incelemede bahşiş reddedilirse ürünler kaldırılır; bu geri
    dönülebilir bir risk.

## 2. Play Billing 7/8/9 ile abonelik ve tek seferlik ürün

- Sürümler: 7.0.0 (14 Mayıs 2024) prepaid planlar ve
  `PendingPurchasesParams`; 8.0.0 (30 Haziran 2025)
  `enableAutoServiceReconnection`, "tek seferlik ürün" adı ve tek
  seferlik ürünlerde çoklu satın alma seçeneği/teklif;
  9.0.0 (19 Mayıs 2026); 9.1.0 (18 Haziran 2026). [resmi]
  - https://developer.android.com/google/play/billing/release-notes
- Zorunlu sürüm takvimi (yeni uygulama ve güncelleme son tarihi):
  7 için 31 Ağustos 2026 (uzatma 1 Kasım 2026); 8 için 31 Ağustos
  2027 (uzatma 1 Kasım 2027); 9 için 31 Ağustos 2028. Yani şu an
  yeni sürüm yüklemek için 8+ gerekir. [resmi]
  - https://developer.android.com/google/play/billing/deprecation-faq
  - Bizde `billing-ktx:8.0.0` var (app/build.gradle.kts, satır 165):
    2027 Ağustos'a kadar yeterli. [ölçüm]
- Yapı: abonelik ürünü, altında base plan'lar, altında teklifler (offer).
  [resmi]
  - Base plan türleri: otomatik yenilenen, prepaid, taksitli (taksit
    yalnız Brezilya, Fransa, İtalya, İspanya).
  - Otomatik yenilenen dönemler: haftalık, 4 hafta, aylık, 2/3/4/6/8
    aylık, yıllık.
  - Prepaid süreler: 1 gün, 3 gün, 1 hafta, 4 hafta, 1/2/3/4/6/8 ay,
    1 yıl.
  - https://support.google.com/googleplay/android-developer/answer/140504
- Teklif kuralları: [resmi]
  - Yalnız otomatik yenilenen base plan'lara eklenir (prepaid'e değil).
  - Uygunluk: "yeni müşteri kazanımı" (kullanıcı bu aboneliğe ya da
    uygulamadaki hiçbir aboneliğe hiç sahip olmamış), "yükseltme",
    "geliştirici belirler" (mantığı uygulama kurar, Play bakmaz).
  - Faz türleri: tek ödeme, yinelenen ödeme, ücretsiz deneme.
  - Ücretsiz deneme 3 gün ile 3 yıl arası.
  - Bir abonelikte toplam 250 base plan + teklif, aynı anda en çok
    50 aktif.
  - https://support.google.com/googleplay/android-developer/answer/12154973
  - https://support.google.com/googleplay/android-developer/answer/140504
- Play'in ücretsiz denemesi KARTSIZ olamaz: Play denemeden önce geçerli
  bir ödeme yönteminin olduğunu doğrular. Console'da "uygulama başına
  tek ücretsiz deneme" kutusu var. [resmi]
  - https://developer.android.com/google/play/billing/subscriptions
- Uygun olmayan kullanıcıya teklif SDK listesinde hiç görünmez
  (RevenueCat); topluluk konusunda da "daha önce alan test kullanıcısı
  teklifi görmüyor" deniyor; Play Billing Lab ile 2 saatlik sıfırlama
  var. [çoklu: 3 kaynak]
  - https://www.revenuecat.com/docs/subscription-guidance/subscription-offers/google-play-offers
  - https://community.revenuecat.com/sdks-51/cannot-see-free-trial-offer-for-a-google-play-subscription-2917
  - https://developer.android.com/google/play/billing/test
- "Ömür boyu" = tek seferlik, tüketilmeyen ürün: satın alma 3 gün içinde
  onaylanmalı (`acknowledgePurchase`), yoksa Play iade eder ve hakkı
  geri alır. Bahşiş = tüketilebilir ürün: `consumeAsync` ile tüketilir
  (aynı çağrı onayı da yapar); tüketilmezse aynı ürün tekrar satın
  alınamaz. İkisini KARIŞTIRMA: ömür boyu ürünü tüketirsen hak biter.
  [resmi]
  - https://developer.android.com/google/play/billing/integrate
- Prepaid plan: otomatik yenilenmez, uzatmak için "top-up" gerekir.
  Top-up daima tam fiyatla; 3 gün içinde onaylanmazsa iade edilir ve
  kalan abonelik iptal olur. `enablePrepaidPlans()` 7.0.0'dan beri var;
  8.0.0 paketinde de var. [resmi + ölçüm]
  - https://developer.android.com/google/play/billing/subscriptions
- Yaşam döngüsü: aktif, ödeme sorunu (grace), askıda (hold), duraklatılmış,
  iptal edilmiş (süre sonuna kadar hak sürer), bitmiş. İstemcide
  `queryPurchasesAsync`: grace dönemi "aktif" döner, hold dönmez, iptal
  edilmiş ama bitmemiş döner, pause dönmez. Hold süresi varsayılan
  60 gün eksi grace. [resmi]
  - https://developer.android.com/google/play/billing/lifecycle/subscriptions
  - https://support.google.com/googleplay/android-developer/answer/16631229
- Yeni abonelik satın alımı 3 gün içinde onaylanmazsa Play otomatik iade
  eder ve hakkı iptal eder. Mevcut kod hemen onaylıyor. [resmi + ölçüm]
  - https://developer.android.com/google/play/billing/lifecycle/subscriptions
- İstemcideki `Purchase` nesnesinde BİTİŞ TARİHİ yok. `purchaseTime`
  abonelik kayıt anı; `isAutoRenewing` iptal bilgisi. "Kalan gün"
  göstermek sunucu ister (bizde sunucu yok). [resmi]
  - https://developer.android.com/reference/com/android/billingclient/api/Purchase
- Satın almayı sunucuda doğrulamak "şiddetle önerilir"; bizde sunucu ve
  INTERNET yok, yani istemci tarafı güvene mecburuz. Sahte kilit açma
  riski kabul edilir (düşük fiyat, çekirdek ücretsiz). [resmi + çıkarım]
  - https://developer.android.com/google/play/billing/integrate
- Hata kodları: `BILLING_UNAVAILABLE` = eski Play Store, desteklenmeyen
  ülke, kurumsal yönetici engeli ya da Play'in çekemediği ödeme yöntemi.
  `NETWORK_ERROR` = Play'e ulaşılamadı. 9.0.0'dan itibaren sistem Play
  Store'u engellerse (çocuk modu) `BILLING_UNAVAILABLE` döner. [resmi]
  - https://developer.android.com/reference/com/android/billingclient/api/BillingClient.BillingResponseCode
  - https://developer.android.com/google/play/billing/migrate-gpblv9
- Mevcut koddaki notlar (okuma sonucu): [ölçüm]
  - `PlayPurchaseGateway` yalnız teklifsiz base plan'ı gösteriyor
    (`offerId == null`), yalnız SUBS sorguluyor, ürün kimliği
    `sende_support`. Ömür boyu için INAPP sorgusu eklenmeli.
  - Dokümana göre `queryPurchasesAsync` her `onResume`'da da çağrılmalı
    (yeni PURCHASED durumuna geçen satın almalar için). Kodda yalnız
    bağlantı kurulunca çağrılıyor.
  - `TrialPolicy.TRIAL_DAYS = 3`; deneme başlangıcı
    `firstInstallTime` (SupportViewModel.kt, satır 32).
  - https://developer.android.com/google/play/billing/integrate

## 3. İnternet izni olmadan faturalama

- Kısa cevap: ödeme akışı için uygulamanın INTERNET izni GEREKMİYOR
  görünüyor; izin Billing'in telemetri bağımlılığından geliyor.
  Çıkarılınca cihazda çalıştığı HİÇBİR kaynakta kanıtlanmadı.
  [ölçüm + çoklu + doğrulanamadı]
- Ölçüm 1: Billing 7.1.1, 8.0.0 ve 9.1.0 AAR'larının kendi manifestinde
  yalnız `com.android.vending.BILLING` izni var, INTERNET yok. Manifest
  Play Store'un `InAppBillingService.BIND` hizmetine bağlanmayı
  bildiriyor: ödeme ağı Play Store uygulamasının ağıdır. [ölçüm]
  - https://dl.google.com/android/maven2/com/android/billingclient/billing/7.1.1/billing-7.1.1.aar
  - https://dl.google.com/android/maven2/com/android/billingclient/billing/8.0.0/billing-8.0.0.aar
  - https://dl.google.com/android/maven2/com/android/billingclient/billing/9.1.0/billing-9.1.0.aar
- Ölçüm 2: INTERNET ve ACCESS_NETWORK_STATE, bağımlılık
  `transport-backend-cct 3.1.8` içinden geliyor (ikisini de istiyor);
  `transport-runtime 3.1.8` yalnız ACCESS_NETWORK_STATE istiyor.
  play-services-base, basement, location, tasks ve androidx.activity
  manifestlerinde bu izinler yok. [ölçüm]
  - https://dl.google.com/android/maven2/com/google/android/datatransport/transport-backend-cct/3.1.8/transport-backend-cct-3.1.8.aar
  - https://dl.google.com/android/maven2/com/google/android/datatransport/transport-runtime/3.1.8/transport-runtime-3.1.8.aar
  - https://dl.google.com/android/maven2/com/android/billingclient/billing/8.0.0/billing-8.0.0.pom
- Ölçüm 3: Billing sınıflarından yalnız ikisi (`zzcm`, `zzcn`) Data
  Transport'a başvuruyor; 6.0.0 sürüm notundaki "Added logging" ile
  uyumlu: Billing'in kendi günlük/telemetri gönderimi. [ölçüm + çıkarım]
  - https://dl.google.com/android/maven2/com/android/billingclient/billing/8.0.0/billing-8.0.0.aar
  - https://developer.android.com/google/play/billing/release-notes
- Ölçüm 4: Data Transport'un CCT arka ucunda "Could not make request to
  the backend" hata günlüğü var; ağ hatası yakalanıyor gibi görünüyor.
  Bu, izin yokken çökmeyeceğini KANITLAMAZ. [ölçüm + çıkarım]
  - https://dl.google.com/android/maven2/com/google/android/datatransport/transport-backend-cct/3.1.8/transport-backend-cct-3.1.8.aar
- Bağımsız kaynak 1 (Stack Overflow, soru 7 Ekim 2023): "Billing 6.0
  INTERNET iznini getiriyor, kaçınılabilir mi?" Soran da kaynağı
  `transport-backend-cct:3.1.8` olarak buluyor (bizimle aynı).
  En çok oy alan cevap (10 oy, 5 Kasım 2023): `tools:node="remove"` ile
  izin çıkarılabilir; "en azından test hesabıyla satın alma işlevini
  etkilemiyor gibi, üretimde denemedim." [forum, tek cevap]
  - https://stackoverflow.com/questions/77248121
- Bağımsız kaynak 2 (GitHub issue, 15 Ekim 2026, audio-blackbox #441):
  "internet izni yok" sözü veren başka bir proje aynı soruyu araştırmış.
  Aynı kök nedeni buluyor (Billing 6.0.0'dan beri). `tools:node=
  "remove"` ile birleşik manifestten INTERNET'in düştüğünü DERLEMEDE
  doğrulamış; cihazda denememiş. Sahibi "izinle yayınla" kararı verip
  "internet izni yok" iddiasını geri çekmiş. Data Safety etkisini
  çözememiş. [forum, tek proje]
  - https://github.com/alexandre-machado/audio-blackbox/issues/441
- Seçenekler:
  - A) Olduğu gibi bırak (ODEME.md'deki mevcut karar). Bu durumda
    mağazada "internet izni yok" cümlesi Play sürümü için YANLIŞ olur.
  - B) Play flavor manifestine `tools:node="remove"` ile INTERNET'i
    (önce yalnız onu) çıkar. Ödeme akışı etkilenmez beklentisi;
    Billing'in telemetrisi sessizce başarısız olur beklentisi.
    ACCESS_NETWORK_STATE'i şimdilik bırak (iş zamanlayıcıyla bağı
    doğrulanamadı). [çıkarım]
  - C) `transport-*` bağımlılığını dışla: Billing sınıfı bunları
    kullanıyor, çalışma anında NoClassDefFoundError riski. ÖNERİLMEZ.
    [çıkarım]
  - D) Eş uygulama (Billing kütüphanesi YOK): aşağıya bak.
- B için test planı (gerçek cihaz, iç test kanalı, lisans test hesabı):
  - Birleşmiş manifestte INTERNET yok mu (`aapt dump permissions`).
  - Bağlan, ürünleri sorgula, test kartıyla al, onayla, `queryPurchasesAsync`.
  - Uçak modunda da `queryPurchasesAsync` (çevrimdışı davranış
    belgelenmemiş; bkz. aşağı).
  - logcat'te `SecurityException` ve `EACCES` ara.
  - API 26, 30, 34, 36 en az birer cihaz/emülatör (Play imajlı).
  - Karar: sorunsuzsa B'yi uygula ve CI'a izin denetimi ekle; bir
    sorun veya Data Safety belirsizliği çıkarsa A'ya dön ve gizlilik
    cümlesini dürüstçe değiştir ("Ödeme için Google Play kullanılır").
- Çevrimdışı yetki kontrolü: resmi doküman `queryPurchasesAsync`
  çevrimdışıyken ne yapar demiyor (indirilen integrate/subscriptions/
  lifecycle sayfalarında "cache/offline" geçmiyor). Stack Overflow'da
  (15 Ağustos 2019, 1 oy) "yetkiyi yerelde sakla" öneriliyor; audio-
  blackbox da aynı sonuca varıyor. Bu yüzden "sticky PRO" tasarımı
  (bölüm 12-b). [resmi sessiz + forum]
  - https://stackoverflow.com/questions/57152748
- Seçenek D ayrıntısı, eş uygulama ("Sende Teşekkür"): [forum + çıkarım]
  - Örnek: Simple Mobile Tools'un "Simple Thank You" uygulaması ve
    halefi Fossify Thank You. Ücretli ayrı bir uygulama; yüklüyse
    ana uygulamalarda tüm özellikler açılıyor. F-Droid sayfası:
    izin yok, GPL-3.0-only, "iade olmasın diye en az bir gün yüklü
    tut" uyarısı (sürüm 5.8.0, 27 Ekim 2023). Fossify sayfası: "internet
    izni istemez", kurulu uygulamaları imza kontrolüyle listeler,
    GitHub sürümü v1.3.0 (30 Ocak 2026).
    https://f-droid.org/packages/com.simplemobiletools.thankyou/
    https://www.fossify.org/apps/thank-you/
  - Artı: Billing kütüphanesi ve INTERNET izni hiç gerekmez; Play ve
    sideload manifestleri aynı kalır; abonelik yaşam döngüsü kodu yok.
  - Eksi: yalnız tek seferlik fiyat (abonelik yok); ücretli uygulamaya
    Play denemesi yok (yerel deneme ana uygulamada olur); iki mağaza
    girdisi, kullanıcı kafası karışır; Google'ın bu kalıba bakışı bu
    kolda doğrulanamadı; satış/yorum verisi yok.
  - Karar: B başarısız olur VE kurucu "internet izni yok" sözünü
    aboneliklerden önemli sayarsa yedek plan. Şimdilik önerilmez.
- Veri güvenliği formu: "toplamak" = veriyi cihazdan dışarı göndermek.
  Yalnız cihazda işlenen veri toplama sayılmaz. İzin listesi ile veri
  güvenliği bölümü ayrı şeylerdir. Ödeme bilgisini uygulama görmüyorsa
  beyan gerekmez; Billing'in kendi telemetrisinin "toplama" sayılıp
  sayılmadığını Google'ın hiçbir sayfası söylemiyor. [resmi + doğrulanamadı]
  - https://support.google.com/googleplay/android-developer/answer/10787469
- Güven bağlamı: önceki kol, Reddit'te en çok oy alan güven cümlesinin
  "Buzzkill. İnternet erişim izni bile yok." olduğunu bulmuştu
  (03 ve 08 kolu; bu kolda yeniden açılamadı). [tek]
  - https://www.reddit.com/r/androidapps/comments/pokkkt/_/hcx7sof/

## 4. Türkiye: kur, vergi, hizmet bedeli, ödeme yöntemi

### 4-a. Kur, enflasyon, vergi, hizmet bedeli

- Kur: TCMB 21.10.2026 döviz satış 49,6015 TL/USD; bir yıl önce
  (21.10.2025) 41,9626. Dolar kuru bir yılda %18,2 yükseldi (TL'nin
  dolar karşısında değer kaybı yaklaşık %15). Kur 22 Ekim'de yeniden
  çekildi, bülten hâlâ 21.10.2026. [ölçüm + hesap]
  - https://www.tcmb.gov.tr/kurlar/today.xml
  - https://www.tcmb.gov.tr/kurlar/202510/21102025.xml
- Enflasyon: yıllık TÜFE Eylül 2026 %29,73; Eylül 2025 %33,29.
  [resmi]
  - https://www.tcmb.gov.tr/wps/wcm/connect/TR/TCMB+TR/Main+Menu/Istatistikler/Enflasyon+Verileri/Tuketici+Fiyatlari
- Alım gücü çıpası: 2026 net asgari ücret 28.075 TL (brüt 33.030).
  [tek: Vikipedi, T24'e dayanıyor]
  - https://tr.wikipedia.org/wiki/T%C3%BCrkiye%27de_asgari_%C3%BCcret
- Play'de TRY: Türkiye hem geliştirici hem satıcı kaydı için destekli,
  varsayılan para birimi TRY. [resmi]
  - https://support.google.com/googleplay/android-developer/answer/9306917
- Fiyat girişi: girdiğin fiyat taban olur, yerel fiyat dönüştürülür,
  ülke bazında elle fiyat verilebilir. Dönüşüm "fiyatı girdiğin günün
  kuruyla" yapılır, kendiliğinden güncellenmez; elle yenilemek gerekir.
  Sonuç: TL fiyatı sabit kalırsa dolar karşılığı eriyor. Sayfa, Türkiye
  için en düşük/en yüksek fiyat aralığını vermiyor. [resmi + çıkarım]
  - https://support.google.com/googleplay/android-developer/answer/6334373
- Mevcut abonelerin fiyatı eski kalır; fiyat artışı "kabul et/reddet"
  (opt-in/opt-out) rejimiyle yapılır. TL zammı abonelere bedava geçmez.
  [resmi]
  - https://support.google.com/googleplay/android-developer/answer/12124625
  - https://support.google.com/googleplay/android-developer/answer/140504
- Vergi (kullanıcı tarafı): Türkiye'de fiyatlar vergi DAHİL gösterilir.
  Türkiye için KDV'yi Google belirler, tahsil eder ve öder (satıcı
  Google). [resmi]
  - https://support.google.com/googleplay/android-developer/answer/138000
- Genel KDV oranı %20 (10 Temmuz 2023'ten beri). [tek: Vikipedi]
  - https://tr.wikipedia.org/wiki/Katma_de%C4%9Fer_vergisi
- Dijital hizmet vergisi: %7,5'ten %5'e, 1 Ocak 2026. Apple bunu
  geliştirici hesaplarına 30 Ocak 2026'da yansıttı. Google Play'in
  Türkiye'de aynı şeyi nasıl yansıttığı bulunamadı. [tek + doğrulanamadı]
  - https://www.cnbce.com/teknoloji/apple-app-store-gelirlerinde-turkiye-icin-vergi-guncellemesine-gitti-h24067
- Geliştiricinin kendi vergisi (kurucu Türkiye'deyse): kaynaklar
  ÇELİŞKİLİ, mali müşavir şart. [çelişkili]
  - Paraşüt (SMMM yazısı): hizmet ihracı istisnası için hizmetin yurt
    dışında yararlanılması gerekir; Türkiye'den bir kişi bile indirdiyse
    KDV gündeme gelir; platformun komisyon faturası kesmesi gerektiğini
    savunan idari görüşler var. Stopaj eşiği 2022 verisi.
    https://www.parasut.com/blog/mobil-uygulama-satislarinin-vergilendirilmesi
  - Mükellef blog: 2022 tebliğine göre bu platformlardan elde edilen
    kazançta KDV hesaplanmaz; stopaj %15; limit 3.000.000 TL (bir
    yorumda 4.300.000 TL).
    https://mukellef.co/blog/?p=2592
  - Bir 2026 özelgesinin (İstanbul Defterdarlığı, 30.01.2026) ihracat
    istisnasını uygulama içi satın almada reddettiği iddiası arama
    özetinden geldi (bu geçişte de aynı özet çıktı); birincil kaynak
    açılamadı. [doğrulanamadı]
- Hizmet bedeli, Türkiye için BUGÜN geçerli olan ("kalan pazarlar"
  satırı; sayfa 22 Ekim 2026'da indirilip okundu): [resmi]
  - Otomatik yenilenen abonelik: %15, yıllık gelirden bağımsız.
  - Diğer işlemler (ömür boyu, bahşiş): "15% katmanına kayıtlı"
    geliştirici için ilk 1 milyon dolara %15, üstüne %30.
  - KAYIT ŞART: katman otomatik değil. Ödeme profili + Hesap Grubu
    (Account Group) kurup 15% şartlarını kabul etmek gerekiyor.
    Kayıtsız kalırsan "diğer işlemler" %30'dan başlar. Bu, ömür boyu
    ve bahşiş gelirini doğrudan etkiler.
  - https://support.google.com/googleplay/android-developer/answer/112622
  - https://support.google.com/googleplay/android-developer/answer/10632485
- Yeni takvim (4 Mart 2026'da duyuruldu): AB, Birleşik Krallık, ABD
  30 Haziran 2026; Avustralya ve Japonya 30 Eylül 2026; Güney Kore
  31 Aralık 2026; "dünyanın geri kalanı" 30 Eylül 2027. Yeni oranlar:
  abonelik %10; yıllık kazancın ilk 1 milyon dolarında her tür işlem
  %10; tek seferlik yeni kurulumda %20, mevcutta %25; ayrıca "fatura
  ücreti" (AU, JP, ABD, BK, AB için %5; diğer bölgeler için "ayrıntılar
  yakında"). Türkiye adıyla anılmıyor; "geri kalan dünya"ya düşeceği
  bir ÇIKARIMDIR; yani 30 Eylül 2027'ye kadar eski oranlar sürer ve
  Türkiye'nin fatura ücreti henüz belli değil. [resmi + çıkarım]
  - https://support.google.com/googleplay/android-developer/answer/16954621
- Kaba net hesabı (VARSAYIM: KDV %20 fiyatın içinde, hizmet bedeli
  KDV'siz tutardan alınır; bu taban Türkiye için doğrulanamadı;
  kur 49,60; DST'nin kimin payından düştüğü bilinmiyor): [çıkarım]
  - Aylık 99,99 TL → KDV'siz 83,33 → %15 sonrası ≈ 70,8 TL (≈ 1,43 $).
  - Yıllık 599,99 TL → ≈ 425 TL (≈ 8,57 $).
  - Ömür boyu 999,99 TL → ≈ 708 TL (≈ 14,3 $); katmana kayıtsızsa %30
    ile ≈ 583 TL (≈ 11,8 $).
  - Bahşiş 49,99 / 99,99 / 249,99 TL → ≈ 35 / 71 / 177 TL
    (≈ 0,71 / 1,43 / 3,57 $).
- Prepaid planın hizmet bedeli sınıfı (otomatik yenilenen gibi %15 mi,
  "tek seferlik" gibi mi) bulunamadı. Yeni takvimde "yinelenen" ile
  "yinelenmeyen" ayrı oranlandığı için prepaid riskli. [doğrulanamadı]

### 4-b. Türkiye'de ödeme yöntemi ve alım gücü çıpaları

- Operatör faturası (Paycell/Turkcell) Play'de kart girmeden ödeme
  imkânı veriyor. Turkcell'in sayfasında (tarihsiz, alt bilgi 2026):
  [resmi, operatör]
  - İşlem başı sınır 300 TL.
  - Aylık sınır: faturalı hat 200-1.100 TL, faturasız hat 500 TL.
  - 90 günden yeni faturalı hat için aylık sınır 60 TL.
  - Sınırlar iş yerine göre değişebiliyor. Abonelik desteği sayfada
    yazmıyor. Vodafone ve Türk Telekom sınırları bulunamadı.
  - https://www.turkcell.com.tr/servisler/eglence/turkcell-mobil-odeme-google-play
  - Sonuç: yalnız operatör faturası olan bir kullanıcı 300 TL'nin
    üstündeki bir planı (yıllık, ömür boyu, kurucunun 3/6/12 ayı)
    AYNI ANDA alamaz. Bu kullanıcı payı bilinmiyor [doğrulanamadı];
    ama aylık plan ve bahşişler (< 300 TL) erişilebilir kalmalı.
    [çıkarım]
- Kart dışı yöntemlerin Play'deki Türkiye durumunu Google'ın "ödeme
  yöntemleri" sayfası net vermedi (Türkiye bir ülke listesinde geçiyor,
  listenin başlığı sayfa metninden çıkmadı). [doğrulanamadı]
  - https://support.google.com/googleplay/answer/2651410
- Operatör faturasında "izinsiz Google Play harcaması" şikâyetleri var
  (bireysel anlatımlar, genel veri değil). Aboneliklerde "kandırıldım"
  algısı riski. [tek, anekdot]
  - https://www.sikayetvar.com/turkcell/izinsiz-google-play-islemleriyle-3000-tl-fatura-soku
- Tüketici TL çıpaları (en yakın ölçüm, hepsi tüketici uygulaması):
  - WhatsApp Plus (süs/kişiselleştirme aboneliği): Türkiye'de aylık
    41,99 TL, 1 ay ücretsiz deneme (Teknoblog, 18 Haziran 2026). [tek]
    https://www.teknoblog.com/whatsapp-plus-turkiye-fiyati-aciklandi/
  - BirGün (5 Ağustos 2026): dijital abonelik örnek ücretleri 79, 99,
    199 TL; ING Türkiye araştırmasına göre katılımcıların %36'sı
    otomatik ödemeli aboneliğini unutmuş (örneklem sayısı yazmıyor).
    [tek]
    https://www.birgun.net/haber/odemeler-kucuk-yuk-buyuk-unutulan-abonelikler-butceyi-zorluyor-727579
  - Kategori (Play TR, 20 Ekim 2026, 01 kolu): BuzzKill tek seferlik
    66,99 TL, FilterBox 90,99-164,99 TL, WAMR 104,99 TL. [tek]
  - Sonuç: aylık 99,99 TL, bu çıpaların orta-üst bandında; WhatsApp
    Plus'ın 2,4 katı. Küçük işletme sahibi tüketiciden fazla öder
    varsayımı VAR ama KANITI yok. Berber/kuaför randevu yazılımı
    fiyatları arandı, bulunamadı. [çıkarım + doğrulanamadı]
- Pazar: StatCounter Eylül 2026, Türkiye mobil işletim sistemi payı
  Android %76,99, iOS %23 (web trafiği ölçümü, satış değil). [resmi
  ölçüm sitesi]
  - https://gs.statcounter.com/os-market-share/mobile/turkey
- Türkiye'ye özel uygulama içi satın alma geliri/ödeme isteği verisi
  (Sensor Tower, data.ai) bulunamadı. Adapty "Türkiye en hızlı
  büyüyen üç pazardan biri" diyor, rakam yok (bölüm 5). [doğrulanamadı]

## 5. Dönüşüm ve gelir verileri

RevenueCat "State of Subscription Apps 2026" (web sayfası, 22 Ekim 2026'da
yeniden açıldı; 2025 verisi, bazı rakamlar 2026 başına kadar): [resmi rapor]
  - https://www.revenuecat.com/state-of-subscription-apps/
- Sert paywall (indirme sonrası 35. günde ücretliye geçen oranı) medyan
  %10,7; freemium %2,1. 14. gün kurulum başına gelir: sert paywall
  2,32 dolar, freemium 0,27 dolar.
- İndirmeden denemeye (30 gün): İş (Business) %9,1; Araçlar (Utilities)
  %6,5. Bölge: Kuzey Amerika %7,1; diğer bölgeler %3,0-3,7.
- Denemeden ücretliye medyan, deneme uzunluğuna göre: 4 gün ve altı
  %25,5; 5-9 gün %37,4; 17-32 gün %42,5 (10-16 gün %35,4, sayfada
  okunaksız).
- 3 günlük denemelerdeki iptallerin %84'ü 0-1. günde (7 günlükte %64);
  3 günlükte %55,4'ü 0. günde.
- MEA bölgesi (Türkiye'nin hangi bölgeye sayıldığı sayfada açık
  değil [doğrulanamadı]): denemeden ücretliye %19,5 (Kuzey Amerika
  %34,2); indirmeden denemeye %3,0-3,7; dönüşümlerin %63,5'i 0. günde;
  14. gün kurulum başına gelir Latin Amerika, MEA ve "diğer" kümesinde
  "yaklaşık 0,10 dolar etrafında"; aylık plan payı %52, yıllık payı en
  düşük.
- Fiyat çıpaları: yıllık medyan 34,80 dolar; aylık medyan 7-8 dolar
  (özet metin 10 dolar da diyor, tutarsız). Hindistan/Güneydoğu Asya:
  aylık 3,75, yıllık 18,32 dolar (bölgelerin en düşüğü).
- Google Play'de iptallerin yaklaşık %31'i ödeme başarısızlığından
  (App Store %14).
- Para kazanma karması: abonelik tek başına %63,5; abonelik + ömür
  boyu %23,2; tüketilebilir ürün içerenler %10,7; üçü birden %2,5.
  Yani uygulamaların yaklaşık dörtte biri ömür boyu plan sunuyor.
- Rapor bahşiş (tip) verisi içermiyor.

RevenueCat "State of Subscription Apps 2025" (önceki geçişte okundu,
bu geçişte yeniden açılmadı; web özeti): [resmi rapor]
  - https://www.revenuecat.com/state-of-subscription-apps-2025/
- Sert paywall %12,11, freemium %2,18 (35. gün). Sert paywall iade oranı
  %5,8, freemium %3,4.
- 60. gün kurulum başına gelir: App Store 0,38 dolar, Google Play 0,14.
  Teklif kullanımı: App Store %17,6; Google Play %7,3.
- 1. yıl elde tutma: yıllık %44,1; aylık %17,0; haftalık %3,4.

Adapty "State of In-App Subscriptions 2026" (22 Ekim 2026'da
doğrulandı): [resmi rapor]
  - https://adapty.io/state-of-in-app-subscriptions/
- Kurulumdan denemeye %10,9; denemeden ücretliye %25,6; yıllık medyan
  38,42 dolar; aylık 12,99 dolar (2025 verisi). Fiyatlar ülkeler arası
  ~4 kat değişiyor. "En hızlı büyüyen pazarlar Japonya, Meksika ve
  Türkiye"; Türkiye'ye özel rakam yok.
- Sert paywall'ın abone başına ömür boyu değeri yumuşak paywall'dan
  %21 yüksek.

Ne diyor, ne demiyor: [çıkarım]
- Sert paywall 5 kat dönüşüm veriyor; ama bu ölçümler zaten paywall'ı
  seçmiş abonelik uygulamalarından. "Mesajlarına erişim izni isteyen"
  güvene dayalı bir araç için aynı sonuç geçmez (aşağıda kullanıcı sesi).
- Beklenti büyüklüğü (abonelik uygulamaları medyanı): 14. günde kurulum
  başına ≈ 0,10 dolar (MEA/geri kalan dünya kümesi). Yani 1.000 kurulum
  ≈ 100 dolar; 10.000 kurulum ≈ 1.000 dolar. Google Play'in 60. gün
  medyanı 0,14 dolar. [çıkarım, kaba]
- Okunuşu: ayda 1.000 dolar için ayda yaklaşık 10.000 yeni kurulum
  gerekir. Bu yüzden Play sıralaması (puan, yorum) gelirin asıl
  kaldıracı. [çıkarım]

### 5-b. Saf bağış/bahşiş ne getirir? (yayınlanmış veri çok az)

- Kural: bir "saf bağış dönüşüm oranı" raporu BULUNAMADI. Elde
  yalnız dağınık, eski veya satıcı iddiası rakamlar var. [doğrulanamadı]
- IronSource KudosKits (uygulama içi "teşekkür" ödemesi, TechCrunch,
  29 Kasım 2013): şirket, altı ayda kullanıcıların %0,58'inin ödeme
  yaptığını, ortalama 2,10 dolar olduğunu söyledi; TechCrunch rakamı
  doğrulamadı ("sözde"). [tek, satıcı iddiası, eski]
  https://techcrunch.com/2013/11/29/ironsource-announces-kudoskits/
- Docear (açık kaynak literatür yönetimi, yazı 20 Kasım 2013): iki
  yılda 33 bağışçı, 434 dolar; "birkaç bin aktif kullanıcı". Ortalama
  13,16 dolar. [tek, eski]
  https://isg.beel.org/blog/tag/statistics
- FairMail geliştiricisi (LWN yorumu, 10 Kasım 2024): "kullanıcıların
  yüzde birinin küçük bir kesri uygulamayı destekliyor"; maliyetleri
  karşılamaya yetiyor. Aynı konudaki "en çok %1-5 bağış yapar"
  yorumu (31 Ekim 2024) kaynaksız tahmin; gerçek proje verisiyle
  uyuşmuyor. [forum]
  https://lwn.net/Articles/996453/
- Garmin Connect IQ geliştirici forumu (tarihsiz, "2 yıldan eski";
  Android DEĞİL, saat uygulamaları): yaklaşık 2.500 indirmeye 15
  bağış (~100 euro); 130 binden fazla indirmeye 50 dolardan az; bir
  geliştirici "bağış artırma çabası hâlâ zarar" diyor; 3-5 dolarlık
  ücretli uygulamaları ve 30 günlük denemesi için "dönüşüm %20-30"
  diyor (deneme kullanıcılarından). [forum, tek, tarihsiz]
  https://forums.garmin.com/developer/connect-iq/f/discussion/354118/boost-donations---monetization
- Okunuşu: bağış oranı büyük olasılıkla %1'in altında, ortalama tutar
  1-13 dolar. Kaba hesap: %0,5 x 2 dolar ≈ 0,01 dolar/kurulum; bu,
  abonelik medyanının (≈ 0,10 dolar) yaklaşık onda biri. Yani 1.000
  kurulumda ≈ 10 dolar. [çıkarım, çok kaba; kaynakları farklı
  dönem/platform]
- Zamanlama etkisi: RevenueCat'in bahşiş rehberi kademeli üç bahşiş
  (1,99 / 4,99 / 9,99 dolar) öneriyor ve kullanıcıların birden çok
  seçenek sunulunca daha yüksekleri seçtiğini söylüyor (rakam yok).
  [tek, anekdot]
  https://www.revenuecat.com/blog/engineering/building-a-tip-jar-feature-with-revenuecat
- appopportunity.com (8 Mart 2026) "bağışlar iyi niyet geliri" diyor
  ama hiçbir rakamın kaynağı yok; kullanmadım. [kaynaksız]

Kullanıcı sesi (önceki kollar; bu kolda yeniden açılamadı): [tek/çoklu]
- Play'de 9/17 rakipte abonelik/fiyat şikâyeti (275 eşleşme, ~%8);
  Notification History Log'da düşük puanların yaklaşık üçte biri.
  [çoklu, 03 kolu]
  - docs/arastirma/03-sikayetler-ve-firsatlar.md (bölüm K8)
- "Reklamı kaldırmak için haftalık 18,99 dolar mı?" (Notisave, 2026).
  - https://play.google.com/store/apps/details?id=com.tenqube.notisave&reviewId=01cf1e13-a168-47c4-8d45-decd41ccda49
- "Denemeden sonra ücretlendirilmek istemiyorum ama uygulamada iptal
  yolu yok" (Auto RDM, 2021).
  - https://play.google.com/store/apps/details?id=com.softinit.iquitos.mainapp&reviewId=1f3072c8-d4a8-4a8a-ac02-2711b26aa77d
- Reddit: "Ücretsiz veya tek seferlik olur, abonelikse düşük olsun"
  (2025-01).
  - https://www.reddit.com/r/androidapps/comments/1hqx5hv/app_that_stores_notification_history_and_can_view/
- Notification History Log, 2025-04: "Tek seferlik bağış seçeneği olsa
  desteklerdim" (5 yıldız; 01 kolu). [tek]
- Tek seferlik ödeme sunan rakipler 5/20 (BuzzKill, NH Pro, Past
  Notifications anahtarı, FilterBox, BinaryScript); haftalık abonelik
  2/20 ve en sert tepkiyi alıyor (01 kolu). [çoklu]
- BuzzKill: 3,99 dolar ücretli indirme, 4,70 puan (3.004 yorum), 100K+
  kurulum; yorum "bir kahveden vazgeç, reklamsız iyi uygulamaya öde"
  (01 kolu). [tek; Play sayfası bu kolda açılamadı]
- FilterBox: "denemek için 6 dolarlık satın almaya zorlandım, iade yok"
  (2024-12); Notification Widget 5,99 dolar, ücretsiz deneme yok →
  şikâyet (01 kolu). [tek]
- Recent Notification saklama süresini 3 güne indirince öfke (03 kolu).
  - https://play.google.com/store/apps/details?id=com.libin.notification&reviewId=d2bda8d6-540b-4390-83ad-6354202c9a2a

## 6. Kurucunun planı üzerine dürüst değerlendirme

### 6-a. 1/3/6/12 ay basamağı

- Hesap (kur 49,60): 1 ay 5 dolar ≈ 248 TL; 3 ay 10 dolar ≈ 496 TL;
  6 ay 20 dolar ≈ 992 TL; 12 ay 45 dolar ≈ 2.232 TL. [çıkarım]
- Aylık eşdeğer: 5,00 / 3,33 / 3,33 / 3,75 dolar. Yani 12 aylık plan,
  3 ve 6 aylıktan AYLIK bazda PAHALI. Basamak tutarsız; kullanıcı
  3 aylığı seçer, yıllık satılmaz. [çıkarım]
- Kategori çıpası: kurucunun 1 ayı (248 TL), kategorideki en bilinen
  tek seferlik uygulamanın (BuzzKill, 66,99 TL) 3,7 katı; yıllık planı
  (2.232 TL) 33 katı. Pro bir iş aracı olmadıkça savunması zor. [çıkarım]
- Piyasa oranı: yıllık medyan / aylık medyan ≈ 3,5-5 kat (RevenueCat
  34,80 / 7-10) ve ≈ 3,0 kat (Adapty 38,42 / 12,99). Kurucunun "12 ay =
  9 ay" oranı bunun yaklaşık iki-üç katı; yıllık plan cazip görünmez.
  [çıkarım; medyanlar farklı uygulamalardan]
  - https://www.revenuecat.com/state-of-subscription-apps/
  - https://adapty.io/state-of-in-app-subscriptions/
- Operatör faturası sınırı (300 TL/işlem, bölüm 4-b): kurucunun 3, 6
  ve 12 aylık planları (496 / 992 / 2.232 TL) bu yolla ALINAMAZ.
  Yalnız aylık plan alınır. [çıkarım; kullanıcı payı bilinmiyor]
- Dört seçenek tek ekranda seçim yorgunluğu yapar; RevenueCat'e göre
  MEA'da aylık pay yüksek, yıllık pay en düşük: kısa taahhüt isteyen
  bir kitle. [çıkarım]
- Play kuralı: yıllık plan en belirgin yerde "aylık fiyat" diye
  gösterilemez; toplam fiyat öne çıkmalı. [resmi]
  - https://support.google.com/googleplay/android-developer/answer/9900533
- Öneri: 3 Pro ürünü (bölüm 11): Aylık, Yıllık, Ömür boyu.
  - Yıllık = 6 ayın parası (%50 indirim). Kurucunun "12 ay = 9 ay"
    yerine; çünkü 12 ay = 9 ay oranı aylıktan sapmıyor.
  - Ömür boyu = 10 ayın parası. Gerekçe: kategorinin tek seferlik
    tercihi (5/20 rakip, Reddit), sunucu maliyeti olmaması, kod
    sadeliği, Play fiyat deneyinin yalnız tek seferlik ürünlerde
    çalışması (bölüm 8).
  - 3 ve 6 aylık planlar çıktı: üçüncü bir seçim yükü; yıllıkla
    ömür boyu arasında sıkışıyorlar.

### 6-b. "Hiçbir şey söylemeden 3 gün" deneme

- Play'in 3 günü: ücretsiz deneme için Play'in alt sınırı 3 gün. Bizim
  denememiz yerel olduğu için 3 gün zorunlu değil. [resmi]
  - https://support.google.com/googleplay/android-developer/answer/140504
- 3 gün kısa: RevenueCat'te 4 gün ve altı denemelerin dönüşümü (%25,5)
  5-9 günlükten (%37,4) düşük. Küçük işletme için tam bir haftalık
  döngü (hafta içi + hafta sonu) 7 gün ister. [resmi veri + çıkarım]
  - https://www.revenuecat.com/state-of-subscription-apps/
- "Sessiz" olmanın riskleri:
  - Play, "aldatıcı veya manipülatif satın alma deneyimi"ni yasaklıyor;
    deneme şartlarını açık anlatma yükümlülüğü var. Yerel, ücretsiz bir
    deneme "otomatik ücretliye dönüş" olmadığı için bu cümlelerin
    doğrudan konusu değil, ama sürpriz paywall bu ruha ters. [çıkarım]
    https://support.google.com/googleplay/android-developer/answer/9900533
  - Türkiye'de mesafeli sözleşme mevzuatı (Mesafeli Sözleşmeler
    Yönetmeliği) var; bu kolda yalnız 2014 metninden cayma süresi
    (madde 9) ve cayma istisnası (madde 15) görüldü; sonraki
    değişiklikler okunmadı. Fiyat/süre ön bilgilendirme yükümlülüğü
    (madde 5) DOĞRULANAMADI. Platform satıcı olduğu için asıl yük
    Google'da, ama tüketici öfkesi bizde. [kısmen çıkarım]
    https://www.resmigazete.gov.tr/eskiler/2014/11/20141127-6.htm
  - Kategori kanıtı: sürpriz sınır ve kilit en çok öfke toplayan şey
    (Recent Notification, Notification Widget, FilterBox yorumları).
    [çoklu, 01 ve 03 kolu]
  - Bu uygulama bildirimlere erişiyor; "virüs mü" şüphesi var (I-57).
    Sürpriz, güveni en pahalı harcayan şeydir. [çıkarım]
- Kanıt eksiği: "söyleyen" ile "söylemeyen" denemeyi karşılaştıran
  yayınlanmış bir dönüşüm verisi BULUNAMADI. [doğrulanamadı]
- Öneri: söyle, ama sakin söyle (tek satır, ilk açılışta; son 2 günde
  küçük çip). Mevcut `trial_warn` moduna yakın; `trial_silent` tercih
  edilmez.

### 6-c. Ödeme Google Play üzerinden

- Zorunlu ve doğru: dijital özellik/abonelik için Play Billing şart.
  [resmi]
  - https://support.google.com/googleplay/android-developer/answer/9858738
- Sideload (APK) sürümünde Play Billing yok; orada ödeme alınamaz.
  APK kamuya açık kalırsa Pro bedava dağılır. Karar: APK yalnız test ve
  güven için; kamuya açık satış yolu Play. [çıkarım]
- Sideload'ın geleceği: Android geliştirici doğrulaması 30 Eylül 2026'da
  Brezilya, Endonezya, Singapur, Tayland'da başladı; "2027 ve sonrası"
  küresel genişleme duyuruldu. Türkiye ilk dalgada değil. Play'deki
  uygulamaları Google otomatik kaydediyor (%99); Play dışı dağıtılan
  APK için Console'dan elle kayıt gerekiyor. Sonuç: APK'yı 2027'ye
  kadar kayıt altına al; yoksa sertifikalı cihazlarda kurulum
  zorlaşır (ileri akışla hâlâ mümkün). [resmi + çıkarım]
  - https://developer.android.com/developer-verification
- Play dışında satış (örn. çevrimdışı lisans anahtarı) politika
  açısından serbest ama Play sürümünden ona link vermek yasak. Android
  imza için `SHA256withECDSA` öneriliyor; ek sistem, bu aşamada gerek
  yok. [resmi + çıkarım]
  - https://developer.android.com/privacy-and-security/cryptography
- Huawei gibi Play'siz cihazlarda Billing çalışmaz (Billing, Play
  Store'un servisine bağlanıyor). Orada fail-open (kilitsiz) davranmak
  şart. [ölçüm: AAR manifesti + çıkarım]

### 6-d. 20'şer kişilik iki sürüm

- Sayı küçük, ölçüm anlamsız: iki oranı karşılaştırmak için %80 güçle
  kol başına yaklaşık şu kadar kişi gerekir (hesabım): %2'den %4'e
  1.141; %3'ten %6'ya 749; %5'ten %10'a 435; %10'dan %20'ye 199;
  %20'den %40'a 82. [çıkarım, hesap]
- 20 kişide %3 dönüşümle hiç ödeme yapan çıkmama olasılığı %54;
  beklenen ödeme sayısı 0,6. [çıkarım, hesap]
- Daha kötüsü: lisans test hesabı sahte ödeme kullanıyor ve test
  siparişlerinde vergi bile hesaplanmıyor; abonelik raporlarına girmiyor.
  Lisans testçisi olmayanlar GERÇEK para öder. Yani testçilerden gerçek
  "ödeme isteği" ölçülemez. [resmi]
  - https://developer.android.com/google/play/billing/test
  - https://support.google.com/googleplay/android-developer/answer/7391949
- Doğru kullanım: kalitatif test. Sorular: "Fiyat ekranı sizi şaşırttı
  mı?", "Kandırılmış hissettiniz mi?", "Bu uygulamaya güvenir misiniz?",
  "Play'de ödeme yönteminiz var mı?". (Son soru önemli: Play denemesi
  ve aboneliği için geçerli ödeme yöntemi gerekir; operatör faturası
  kullanıyorsa 300 TL sınırı.) [çıkarım]
- Bonus: Yeni KİŞİSEL geliştirici hesabı (13 Kasım 2023'ten sonra
  açılan) üretime çıkmak için kapalı testte en az 12 testçiyi 14 gün
  kesintisiz opt-in tutmak zorunda. 20 kişilik kanal bu şartı karşılar:
  iki kanal kullanırsan HER BİRİNDE en az 12 olsun. Kurumsal hesapta
  bu şart yok. [resmi; iki kanalda sayımın nasıl yapıldığı belirsiz]
  - https://support.google.com/googleplay/android-developer/answer/14151465

### 6-e. "Zorunlu olmasın, bağış alsak mı?"

- Evet, bahşiş alınır; ama tek başına bir iş modeli değildir (bölüm
  5-b: ≈ onda biri). Play'de yol: tüketilebilir ürün. [resmi + çıkarım]
- Yapılmaması gereken: uygulama içinde dış bağış sitesine link, Patreon,
  Open Collective. 2016'dan 2026'ya altı bağımsız vakada reddedildi
  veya geliştiriciyi sürüm çıkarmaktan alıkoydu (bölüm 1-b). [çoklu]
- Kelime: Türkiye'de "bağış/yardım toplama" 2860 sayılı Yardım Toplama
  Kanunu'na takılabilir. Resmî metin (mevzuat.gov.tr PDF'i, bu geçişte
  okundu; Türkçe karakter kodlu yazı tipi yüzünden elle çözüldü): [resmi]
  - Madde 3: yardım, "kamu yararına uygun olarak, amaçlarını
    gerçekleştirmek, muhtaç kişilere yardım sağlamak ve kamu
    hizmetlerinden bir veya birkaçını gerçekleştirmek veya destek
    olmak" için toplanır.
  - Madde 5: toplama şekilleri arasında "bilgileri otomatik ya da
    elektronik olarak işleme tâbi tutmuş sistemler" de sayılıyor.
  - Madde 6: "Kişiler ve kuruluşlar, yetkili makamdan izin almadan
    yardım toplayamazlar." 24 Aralık 2020'de (7262) eklenen fıkra:
    internette izinsiz toplamada içerik 24 saatte kaldırılmazsa erişim
    engeli için sulh ceza hâkimliğine başvurulur.
  - Madde 29 (7262 ile): izinsiz toplamaya 5.000-100.000 TL, internet
    ortamında 10.000-200.000 TL idari para cezası (metinde yazan
    tutarlar; yeniden değerleme ile güncel tutar farklı olabilir
    [doğrulanamadı]). Bir fıkra Anayasa Mahkemesi'nin 18.1.2024
    tarihli kararıyla iptal edilmiş.
    https://www.mevzuat.gov.tr/MevzuatMetin/1.5.2860.pdf
  - Yorum: kanun "kamu yararı" amaçlı toplamayı anlatıyor. Bir
    geliştiricinin kendi ürünü için sattığı "çay ısmarla" ürünü
    büyük olasılıkla bu kapsamda değil; ama "bağış/yardım" kelimesi ve
    kutu/IBAN akışı dikkat çekebilir. Güvenli yol: "destek" ve ürün
    sunumu. Hukuki görüş al. [çıkarım, hukuki tavsiye değil]
- Kimin bahşiş vereceği: rakip yorumlarında "bağış seçeneği olsa
  desteklerdim" gibi cümleler var ama sayısı küçük (tek/az kaynak).
  [tek, 01 kolu]
- Bağış/bahşiş gelir hedefini karşılamaz (bölüm 5-b). Ama güveni artırır
  ve yorumlarda "adil" algısı yaratır. Bu yüzden Pro'nun YANINA konur,
  yerine değil. [çıkarım]

## 7. Yerel deneme ve sömürü (veri silip yeniden deneme)

- `firstInstallTime`: "uygulamanın ilk kurulduğu an". Mevcut kod
  deneme başlangıcını buradan alıyor (SupportViewModel.kt, satır 32).
  [resmi + ölçüm]
  - https://developer.android.com/reference/android/content/pm/PackageInfo
- Kaldır-yeniden kur yeni bir `firstInstallTime` verir; yani deneme
  baştan başlar. "Verileri sil" bu alanı sıfırlamaz (paket bilgisidir),
  ama bu cihazda sınanmalı. [çıkarım, sınanmadı]
- Çözüm 1 (öneri): deneme başlangıcını `DataStore`'a da yaz ve
  `min(firstInstallTime, kayıtlı)` kullan. Android Auto Backup, kurulum
  sırasında (Play'den, cihaz kurulumunda, adb ile) uygulama verisini
  geri yükler. Depo kuralları zaten `sharedpref` ve `file` alanını
  yedeğe dahil ediyor (data_extraction_rules.xml); DataStore dosyası
  `files/datastore` altında olduğu için yedeğe girer. Yedekleme açıksa
  kaldır-kur denemeyi sıfırlamaz. [resmi + ölçüm + çıkarım]
  - https://developer.android.com/identity/data/autobackup
  - Sınır: yedekleme kapalıysa veya ~günlük yedek henüz alınmadıysa
    olmaz; 25 MB sınırı bizim için sorun değil.
- Çözüm 2 (önerilmez): Block Store. 16 anahtar, anahtar başına 4 KB;
  Google yedeği açıksa kaldır-kur sonrası da kalır. Ama Play Hizmetleri
  bağımlılığı getirir (izin etkisi ölçülmedi) ve kazanç küçük. [resmi]
  - https://developer.android.com/identity/block-store
- Çözüm 3 (işe yaramaz): `ANDROID_ID`. Android 8+ için imza anahtarı,
  kullanıcı ve cihaz başına bir değer; kaldır-kur sonrası aynı kalır
  ama "bunu daha önce gördüm" diyecek kalıcı bir yer (sunucu) yok. [resmi
  + çıkarım]
  - https://developer.android.com/reference/android/provider/Settings.Secure
- Saat geri alma: kullanıcı cihaz tarihini geri alırsa deneme uzar.
  Basit önlem: son görülen zamanı sakla, `max(şimdi, sonGörülen)`
  kullan. [tasarım, çıkarım]
- Play'in kendi denemesi sömürüye en dayanıklısı (uygulama başına tek
  deneme, Google hesabına bağlı; Play deneme kötüye kullanımını
  algılayabilir) ama kart ister ve ücretliye otomatik döner. [resmi]
  - https://developer.android.com/google/play/billing/subscriptions
  - https://support.google.com/googleplay/android-developer/answer/12154973
- Bağımsız not: "silip yeniden kur" sorununu başka platformlarda da
  geliştiriciler yaşıyor (ör. Garmin forumunda deneme modu); tartışmalarda
  tek sağlam çözüm sunucu tarafı kayıt. Bizde sunucu yok; bu yüzden
  sömürü kabul edilen bir kayıp. [forum, tek]
  - https://forums.garmin.com/developer/connect-iq/f/discussion/257559/deleting-re-loading-to-get-around-trial-mode
- ASIL nokta: çekirdek ücretsiz kalıyorsa "sömürünün maliyeti" ~0'dır.
  Sömüren kişi Pro özelliklerini 7 gün daha görür, hepsi bu. Sert
  kilit yoksa sömürü önleme için mühendislik harcamaya değmez; Çözüm 1
  ve saat önlemi yeterli. [çıkarım]

## 8. A/B testini Play'de yapmanın yolları

- Mağaza girdisi deneyleri yalnız simge, öne çıkan görsel, ekran
  görüntüsü ve (yerelleştirilmiş deneyde) açıklamayı test eder; fiyat
  veya uygulama içi davranışı değil. Aynı anda 1 varsayılan grafik
  deneyi ya da 5 yerelleştirilmiş deney, en çok 2 varyant, 6 ay sonra
  otomatik biter. [resmi]
  - https://support.google.com/googleplay/android-developer/answer/6227309
- Play Console FİYAT DENEYLERİ var (ilk geçişte bulunamamıştı).
  [resmi]
  - Yalnız aktif TEK SEFERLİK ürünler için; abonelik anılmıyor.
    Yani bahşiş ve "ömür boyu" fiyatı test edilir, aylık/yıllık
    abonelik fiyatı test EDİLEMEZ.
  - "Merchant" (satıcı) geliştirici şart; Mağaza varlığını yönet ve
    finansal veri izinleri gerekir.
  - Kullanıcılar kontrol ve varyantlar arasında eşit bölünür; bir
    kullanıcı deney boyunca tek fiyat görür. En çok 2 varyant + kontrol;
    ülke/bölge başına aynı anda bir deney; duraklatılamaz; en uzun 6
    ay; anlamlılığa varınca 14 gün sonra fiyatlar eski haline döner.
  - Güven düzeyi varsayılan %90 (%70-99 seçilir); asgari saptanabilir
    etki varsayılan %30 (%5 aralıklarla %5-50). Kod değişikliği
    gerekmez; INTERNET gerekmez (ödeme zaten Play üzerinden).
  - Deneme yapılacak ülkede bir sürüm (iç test dahil) yayınlanmış ve
    yerel para birimi destekli olmalı. Türkiye açıkça anılmıyor,
    yerel para birimi TRY destekli (bölüm 4-a). Anlamlılık ülkeler
    birleştirilerek hesaplanır; yalnız Türkiye için tek ülke seç.
  - Mayıs 2023 duyurusunda Space Ape "satın almalar %20-40 arttı"
    diyor (geliştirici beyanı, doğrulanmamış).
  - https://support.google.com/googleplay/android-developer/answer/13343030
  - https://android-developers.googleblog.com/2023/05/price-experiments-in-play-console.html
  - Sonuç: %30 etkiyi %90 güvenle yakalamak için yüzlerce alıcı
    gerekir; ilk haftalarda anlamsız. Lansmandan aylar sonra, ömür
    boyu ve bahşiş için kullan. [çıkarım]
- Farklı sürümleri farklı gruplara vermek: kapalı test kanalları.
  [resmi]
  - Aynı anda birden çok kapalı test olabilir; testçiler e-posta
    listesi veya Google Grubu ile eklenir.
  - Bir kullanıcı birden fazla kanala uygunsa EN YÜKSEK sürüm kodunu
    alır. İç testteki kullanıcı kapalı/açık yapıyı alamaz.
  - Liste kapasiteleri geniş (kapalı testte liste başına 2.000 kişi).
  - https://support.google.com/googleplay/android-developer/answer/9845334
- Firebase Remote Config bu işi görür ama değerleri Firebase arka ucundan
  ağ üzerinden çeker (REST API bağımlılığı) ve koşullu hedefleme için
  Google Analytics ister. INTERNET gerektirir, "internet izni yok"
  vaadiyle çelişir. ÖNERİLMEZ. [resmi + çıkarım]
  - https://firebase.google.com/docs/remote-config/android/get-started
- Play Console abonelik raporu: abonelik, base plan ve teklif kırılımı;
  ülke; ücretsiz denemeden ücretliye dönüşüm kartı; elde tutma. Uygulama
  SÜRÜMÜ kırılımı belirtilmemiş. [resmi]
  - https://support.google.com/googleplay/android-developer/answer/7391949
- Önerilen sunucusuz abonelik A/B tasarımı (lansmandan sonra,
  binlerce kurulumda): [çıkarım]
  - TEK build, tek kanal. Uygulama ilk açılışta yerelde rastgele kova
    (A/B) seçer ve saklar. Hiçbir veri dışarı gitmez.
  - Varyantı Play raporunda ayırmak için aynı fiyatlı ikinci base plan
    kullan (örn. `yearly` ve `yearly_b`). Rapor base plan kırılımı
    veriyor. Payda: Play Console'daki toplam kurulum / 2.
  - Yeterli örnek için bölüm 6-d'deki sayılar geçerli. Dönüşüm %1
    civarındaysa kol başına binlerce kurulum gerekir; erken A/B
    gereksiz.
- Kurucunun 20+20 planı için mekanik: [resmi + çıkarım]
  - Kanal A: `-Pmonetization=trial_silent`, kanal B: `trial_warn`.
    Sürüm kodları farklı, testçi listeleri KESİŞMEZ.
  - Amaç dönüşüm değil algı. Anket 3-4 soru (bölüm 6-d).

## 9. Geri ödeme, iptal ve KDV akışı

- İptal: kullanıcı Play abonelik merkezinde iptal eder; iptal mevcut
  dönem sonunda geçerli olur, genelde iade yoktur. Hak, dönem sonuna
  kadar sürer; `queryPurchasesAsync` iptal edilmiş ama bitmemiş
  aboneliği döndürmeye devam eder. Erişimi erken kesmek abonelik
  politikasına aykırıdır. [resmi]
  - https://support.google.com/googleplay/android-developer/answer/9900533
  - https://developer.android.com/google/play/billing/lifecycle/subscriptions
- Uygulamada iptal linki zorunlu. Derin bağlantı biçimi (dokümandan):
  `https://play.google.com/store/account/subscriptions?sku=<ürün>&package=<paket>`
  [resmi]
  - https://developer.android.com/google/play/billing/subscriptions
- İade: satın almadan sonraki 48 saat içinde Google'ın iade akışı;
  48 saatten sonra geliştirici kendi politikasıyla iade eder. İade
  edilince hak kaybolur; bir uygulama/oyun için yalnız bir kez iade
  alınabilir. Geliştirici doğrudan destek ve iade verebilir. [resmi]
  - https://support.google.com/googleplay/answer/15574908
  - https://support.google.com/googleplay/answer/2479637
  - https://support.google.com/googleplay/android-developer/answer/10281818
- Onaylanmayan yeni abonelik 3 günde otomatik iade (kod hemen onaylıyor).
  [resmi]
  - https://developer.android.com/google/play/billing/lifecycle/subscriptions
- Ömür boyu ürününde iade: Play iade edince `queryPurchasesAsync`
  ürünü artık döndürmez; uygulama hakkı o zaman düşürür (OK sonuç ve
  ürün yok). [resmi + çıkarım]
- Sistem davranışı: iade/süre bitişi/hold olunca `queryPurchasesAsync`
  satın almayı artık döndürmez; uygulama hakkı kendiliğinden düşürür.
  Bunun için her ön plana gelişte (`onResume`) yenile. [resmi + çıkarım]
  - https://developer.android.com/google/play/billing/integrate
- Mesafeli satış hukuku: 14 günlük cayma ve "elektronik ortamda anında
  ifa edilen hizmetler / anında teslim edilen gayrimaddi mallar" için
  cayma istisnası var (2014 metni, madde 9 ve 15). Google satıcı
  olduğundan pratikte Google'ın iade politikası belirler. [kısmen çıkarım]
  - https://www.resmigazete.gov.tr/eskiler/2014/11/20141127-6.htm
- İade oranı referansı: sert paywall %5,8, freemium %3,4 (RevenueCat
  2025). [resmi rapor]
  - https://www.revenuecat.com/state-of-subscription-apps-2025/
- KDV: bölüm 4-a. Kullanıcıya gösterilen fiyat vergi dahildir; faturayı
  Google keser (satıcı Google).
- Destek e-postası: önceki kol "iade/destek e-postasına 24 saatte cevap"
  demişti (Auto RDM örneği). Hem mağaza girdisine hem uygulamaya yaz.
  [tek, 03 kolu]

## 10. Dürüst denge: "dayatma yok" ile gelir hedefi

Dayatma ne demek (üç ayrı şey):
- Sürpriz kilit: kullanıcıya söylenmeden bir şeyin kapanması.
- Rahatsız etme (nag): tekrar tekrar açılan, kilitlemeyen ama yoran
  ekran. Mevcut `trial_*` modları BU sınıfta: günde bir kez açılan
  "Şimdilik değil" paywall'ı (TrialPolicy.kt). Kilit yok ama dayatma var.
  [ölçüm]
- Sürpriz tahsilat: Play denemesinin kartı çekmesi, iptal yolunu
  saklamak.

Üç model, sayılarla (kaba, bölüm 5): [çıkarım]
- Model 1: Saf destek (bağış + bahşiş, hiçbir şey kilitli değil).
  - Artı: en yüksek güven, en az risk, "dayatma yok" tam karşılanır.
  - Eksi: 1.000 kurulumda kabaca 10 dolar (%0,5 x 2 dolar); abonelik
    medyanının onda biri. Yayınlanmış veri yok, tahmin.
- Model 2 (ÖNERİLEN): Açık çekirdek + Pro + bahşiş.
  - Artı: ücretsiz kısım "hostage" yok; Pro değer anında kendiliğinden
    sunulur; gelir ölçülebilir; bahşiş güveni korur.
  - Eksi: Pro'ya değerli bir şey koymak gerek (ürün kararı).
  - Beklenti: freemium medyanı %2,1; ödeme yapanlar için Pro net
    fiyatı bölüm 4-a'da. 1.000 kurulumda kabaca 50-100 dolar.
- Model 3: Deneme sonrası sert paywall.
  - Artı: veriye göre en yüksek dönüşüm (5 kat; sert %10,7 vs freemium
    %2,1) ve %21 daha yüksek abone başı ömür boyu değer (Adapty).
  - Eksi: kurucunun kendi tercihine ters; kategoride paywall en çok
    şikâyet edilen şey; yeni uygulamanın ilk puanlarını bozar;
    Play sıralaması düşer. Lansmanda ÖNERİLMEZ. [çıkarım]

Dengeyi kuran üç kural (Model 2'yi "dayatmasız" yapan):
- Kural 1 (asla kilitlenmeyen çekirdek): yakalama, tüm seçili kanallar,
  geçmiş, okuma, "okundu", kopyalama, "uygulamada aç", tek konuşmayı
  paylaşma, izin/sağlık uyarıları, gizlilik ekranı. Mesajlar
  gizlenmez, silinmez, süreyle sınırlanmaz. [çıkarım; 03 kolu ile uyumlu]
- Kural 2 (ileri yönlü sınır): ücretsiz verilen şey sonradan kilitlenmez.
  Pro sınırı LANSMANDA çizilir; sonra sadece kullanıcı lehine kayar.
  Nedeni: sonradan sıkılaştırma öfke topluyor: Recent Notification
  saklama süresini düşürünce, Notisave ömür boyu alanlara reklam
  gösterince ("Ömür boyu aldım, şimdi reklam var", 2026-08).
  Ömür boyu ürününe ASLA reklam eklenmez. [çıkarım; 01 ve 03 kolu
  yorumları, tek tek kaynaklar]
  - https://play.google.com/store/apps/details?id=com.libin.notification&reviewId=d2bda8d6-540b-4390-83ad-6354202c9a2a
- Kural 3 (rahatsız etme tavanı): kendiliğinden çıkan teklif en çok
  ayda bir ve toplam 3 kez; "bir daha gösterme" her zaman var. Başka
  her yerde teklif, kullanıcı bir Pro özelliğine dokununca çıkar.
  [çıkarım]
  - Garmin forumundaki bir geliştirici de "yarı rahatsız edici ekran
    iyi taktik değil" diyor. [forum, tek]

Gelir hedefine dürüst bakış: [çıkarım]
- Saf bağışın "çok kişiye satış" hedefini karşıladığına dair hiçbir veri
  bulunamadı; bulunan dağınık rakamlar bağış oranını %1'in altında
  gösteriyor (bölüm 5-b). "Dayatma olmasın" tercihi, kurucu farkında
  olsun olmasın, gelirin büyük kısmını masadan kaldırma ihtimali taşır.
- Pro modelinde bile beklenti mütevazı: ~1.000 kurulum = ~100 dolar
  (14. gün medyan, abonelik uygulamaları). Ayda 1.000 dolar için ayda
  yaklaşık 10.000 yeni kurulum gerekir. Çözüm fiyat artırmak değil;
  kurulum ve puan.
- Türkiye fiyat seviyesi bunu daha da küçültür: aylık Pro'dan geliştirici
  eline ≈ 1,4 dolar kalır (bölüm 4-a). 1.000 dolar/ay için, aylık
  plan olsaydı ≈ 700 ödeyen gerekirdi. Bu yüzden Yıllık ve Ömür boyu
  seçenekleri gelir için aylıktan daha önemli. [çıkarım]
- "Dayatmasız" olmak, uzun vadede kurulum ve puan getirir: kategori
  yorumları paywall ve abonelik baskısına çok sert tepki veriyor
  (bölüm 5). Bu yüzden Model 2, kısa vadede biraz gelir bırakıp uzun
  vadede sıralamayı kazandırır.
- Kurucuya açık soru (ürün kararı): "Pro'ya hangi 2-3 özellik girer?"
  Bu cevap gelire en çok etki eden tek karardır. Pro yalnız "bildirim
  geçmişi araçları" olursa (rakiplerin tek seferlik 67-165 TL bandı),
  aylık 99,99 TL savunulamaz; o durumda tek ürün olarak "Ömür boyu"
  299,99 TL civarı mantıklıdır. Pro gerçek iş aracı olursa (hazır
  cevap, müşteri notu, sipariş/randevu yakalama) bölüm 11'deki
  fiyatlar savunulabilir. [çıkarım]

## 11. ÖNERİ

Model: "Açık çekirdek + Sende Pro + destek ürünü" (Model 2).

Deneme:
- 7 gün, kartsız, yerel. İlk açılışta tek satır bilgi; son 2 günde
  sakin bir çip; 7. günden sonra tek seferlik "deneme bitti" kartı.
- Süre dolunca hiçbir veri kaybolmaz; yalnız Pro özellikleri kilitlenir.
- Play'in kendi ücretsiz denemesi (kart ister, otomatik döner) v1'de
  KULLANILMAZ. Gerekçe: güven, kartsızlık, politika yükü, operatör
  faturası kullanıcıları. [çıkarım]

Pro ürünleri ve başlangıç fiyatları (TL, KDV dahil; İLK 8 HAFTA GÖZDEN
GEÇİR; hepsi VARSAYIM):
- Aylık (abonelik): 99,99 TL (≈ 2,02 dolar). Tek giriş fiyatı;
  operatör faturası sınırının (300 TL) altında.
- Yıllık (abonelik): 599,99 TL (6 ayın parası; %50 indirim;
  ≈ 12,10 dolar). Varsayılan seçili, "en iyi değer".
- Ömür boyu (tek seferlik): 999,99 TL (10 ayın parası;
  ≈ 20,16 dolar). Yıllığa göre ~20. ayda kendini öder.
- Destek ürünleri (tüketilebilir): 49,99 / 99,99 / 249,99 TL
  (≈ 1 / 2 / 5 dolar).
- Nasıl türedi (varsayım, kesin değil): [çıkarım]
  - Kurucunun dolar fiyatları TL çıpalara göre çok yüksek (aylık 248
    TL ile BuzzKill 67 TL, FilterBox 91-165 TL, WhatsApp Plus 41,99 TL).
  - Türkiye aylık abonelik örnekleri 79-199 TL bandında (BirGün,
    5 Ağustos 2026); 99,99 bandın alt-orta kesimi.
  - RevenueCat'te Hindistan/Güneydoğu Asya aylık medyan 3,75, yıllık
    18,32 dolar; önerilen aylık bunun ~%54'ü, yıllık ~%66'sı. Türkiye'ye
    özel veri yok; bu bir alt-üst sınır çıpasıdır.
  - Net asgari ücretin %0,36'sı (aylık).
  - İlk taslaktaki 129,99 TL'den indirme nedeni: Türkiye'ye özel
    tüketici çıpaları Hindistan/Güneydoğu Asya vekilinden daha
    ilgili; fark küçük, asıl belirleyici Pro'nun içeriği.
- Yeniden fiyatlama kuralı: dolar kuru bir yılda %18 yükseldi, TÜFE
  %29,7. Her 6 ayda bir TRY fiyatını elle yenile. Mevcut abonelere zam
  onay ister (bölüm 4-a); bu yüzden lansmanda çok düşük başlama.
  Ömür boyu ve bahşiş fiyatı yeni alıcılar için serbestçe güncellenir.
- İsteğe bağlı lansman teklifi: "yeni müşteri kazanımı" uygunluklu bir
  teklif (örn. ilk dönem %50). Play şartları açıkça yazılmalı (bölüm 1).
- 3 ve 6 aylık planlar: YOK (bölüm 6-a). 3 aylık plan yalnız
  operatör faturası payı büyük çıkarsa 249,99 TL ile denenebilir.
- Haftalık plan, reklam, ücretli kullanıcıya reklam: ASLA.

Pro'ya girecekler (kurucu seçer; aday liste): [çıkarım]
- Toplu dışa aktarma (CSV/metin). Tek konuşmayı paylaşma ücretsiz.
- Uygulamadan yanıtlama (şimdi var; lansmanda Pro'ya konabilir çünkü
  henüz kimseden alınmıyor).
- Gelecek iş araçları: hazır cevaplar (03 kolu ikinci sürüm adayı),
  müşteri notu/etiket, telefon/IBAN algılama.
- Hiçbiri yoksa v1'de "Pro" yerine "Destek" konumla (rozet, tema) ve
  gerçek kilitleri v1.x'te YENİ özelliklerle ekle; bu durumda tek
  ürün "Ömür boyu" 299,99 TL (bölüm 10).

Bağış/bahşiş:
- Üç tüketilebilir ürün. Menüde ve Pro sayfasının altında. "Bağış"
  değil "Çay ısmarla / Kahve / Yemek". Uygulama satın almadan sonra
  hemen tüketir (mevcut kod yapıyor). Karşılığında küçük, işlevsiz bir
  "Destekçi" rozeti; tüketilmiş satın alma geri yüklenemeyeceği için
  rozet yerelde saklanır (yeniden kurulumda yedekle gelir).
- Dışa link yok. Sideload'da bile ilk sürümde link koyma (iki sürümün
  davranışı ayrışmasın). İnceleme tip kutusunu reddederse ürünler
  kaldırılır, Pro etkilenmez.

Play Console işleri (önce bunlar):
- Satıcı (merchant) profili ve Türkiye vergi bilgisi (mali müşavir).
- Hesap Grubu + 15% hizmet bedeli kaydı (bölüm 4-a): ömür boyu ve
  bahşiş için %30 yerine %15.
- Geliştirici doğrulaması kaydı (APK için, bölüm 6-c).

20+20 testi:
- Anlaşılırlık ve güven testi olarak yap (bölüm 6-d, 8). Aynı
  kanallar yeni kişisel hesap için zorunlu 12 testçi/14 gün şartını
  da karşılar.
- Karar kriteri dönüşüm değil: "kandırıldım" diyen var mı.

Lansman sonrası izleme (Play Console, uygulama içi analitik yok):
- Abonelik kartları (yeni, iptal, elde tutma), ülke kırılımı, puan,
  yorumlar. Puan 4,3 altına inerse paywall yaklaşımını gözden geçir.
  [çıkarım]
- İlk 2.000 kurulumda ödeme yapan oranı %0,5'in altındaysa Pro kapsamını
  ve fiyatı yeniden düşün. Eşik benim yargım. [çıkarım]
- Binlerce kurulumdan sonra: ömür boyu ve bahşiş fiyatı için Play fiyat
  deneyi aç (bölüm 8).

## 12. Teknik şartname (uygulayacak kişi için)

### 12-a. Ürün kimlikleri (Play Console)

- Abonelik ürünü: `sende_pro`. Kimlik sonradan değiştirilemez ve
  silinen kimlik yeniden kullanılamaz; bu yüzden şimdi karar ver.
  Mevcut kodda `sende_support` var; değiştirilecek (12-d).
  [çıkarım]
  - Ürün kimliği en çok 40 karakter, ad 55 karakter. [resmi]
    https://support.google.com/googleplay/android-developer/answer/140504
- Base plan'lar (hepsi otomatik yenilenen):
  - `monthly` (1 ay), `yearly` (1 yıl).
  - `quarterly`, `semiannual` tanımlanmaz (kodda eşlemede kalabilir,
    zararsız).
- Teklif: v1'de yok. (Lansman teklifi istenirse "yeni müşteri kazanımı".)
- Grace ve hold: Console varsayılanları; geri satın alma (resubscribe)
  açık. [resmi]
  https://support.google.com/googleplay/android-developer/answer/140504
- Tek seferlik ürün (tüketilmeyen): `sende_pro_lifetime` ("Ömür boyu").
  Tek satın alma seçeneği; 3 gün içinde onayla. Tüketme.
- Tüketilebilir ürünler: `tip_small`, `tip_medium`, `tip_large`.
- Prepaid (v1'de yok): `p3m`, `p12m` ileride; engeller: bitiş tarihi
  istemcide yok, teklif eklenemez, hizmet bedeli sınıfı belirsiz.
- Fiyatlar: yukarıdaki TL; diğer ülkeleri Play'in önerisine bırak.
  Türkiye fiyatı elle düzenle (kendiliğinden güncellenmez).
- Lisans test hesapları: kendi Gmail'in; test kartları "her zaman
  onaylar/reddeder", "yavaş onaylar" (PENDING sınavı).
  https://developer.android.com/google/play/billing/test
- Test yenileme hızları (lisans testçisi): aylık 5 dk, 6 ay 15 dk,
  yıllık 30 dk, en çok 6 yenileme. Onaysız satın alma test ortamında
  birkaç dakikada iade edilir (sayfada hem 3 hem 5 dakika geçiyor).
  [resmi]
  https://developer.android.com/google/play/billing/test

### 12-b. Durum makinesi

Girdiler:
- `trialStart`: DataStore'da kalıcı; `min(firstInstallTime, kayıtlı)`.
- `lastSeen`: görülen en büyük saat; `now = max(saat, lastSeen)`.
- Play durumu: `subActive: Boolean?` (SUBS sorgusu) ve
  `lifetimeOwned: Boolean?` (INAPP sorgusu); null = bilinmiyor.
  `lastEntitledAt`: son BAŞARILI sorguda yetkinin görüldüğü an.
- Sayaçlar: `nudgeCount`, `lastNudgeDay`; `supporterBadge` (bahşiş).

Durumlar:
- TRIAL (gün 0..6): her şey açık. Gün 0: tek satır bilgi. Gün 5-6:
  "Pro denemen N gün sonra bitiyor" çipi.
- FREE (gün 7+, Pro yok): çekirdek açık. Pro özellikleri kilitli.
  7. günün ilk ön plan açılışında TEK SEFERLİK "Deneme bitti" kartı.
- PRO: `subActive` veya `lifetimeOwned` doğru (grace dönemi dahil;
  Play onu aktif döndürür). Kaynak (abonelik/ömür boyu) ayrıca tutulur,
  yalnız ekranda gösterilir.
- PENDING: satın alma bekliyor (yavaş ödeme yöntemi). Hak VERİLMEZ;
  "Ödemen onay bekliyor" yazılır. Onay gelince `onResume` yenilemesi
  PRO'ya geçirir. [resmi]
  https://developer.android.com/google/play/billing/integrate
- UNAVAILABLE: Play yok / `BILLING_UNAVAILABLE` / sideload.
  Davranış: fail-open (kilit yok, teklif yok, "burada ödeme yok" metni).

Geçişler:
- TRIAL → FREE: `now - trialStart >= 7 gün`.
- FREE/TRIAL → PRO: Play satın alma döndürür.
- PRO → FREE: HER İKİ sorgu da BAŞARILI (OK) ve ikisinde de satın alma
  yok (iade, süre bitişi, hold, pause). Hata kodlarında (NETWORK_ERROR,
  SERVICE_UNAVAILABLE, SERVICE_DISCONNECTED) durum DEĞİŞMEZ; son bilinen
  PRO en çok 30 gün korunur (`lastEntitledAt`). 30 gün tasarım
  tercihidir: çevrimdışı yetki davranışı belgelenmemiş (bölüm 3).
  [tasarım]
- Yenileme zamanları: uygulama açılışı, her `onResume`, satın alma
  sonrası, "Satın alımları kontrol et" düğmesi.
- Tek sayaç kuralı: kendiliğinden teklif en çok 30 günde bir, toplam
  3 kez; kullanıcı "bir daha gösterme" derse sıfır. Bahşiş hiçbir durum
  değiştirmez (yalnız teşekkür ve rozet).
- Satın alma işleme: ürün kimliğine göre dallan. `tip_*` → rozeti
  yaz, sonra `consumeAsync`. `sende_pro_lifetime` → onayla, TÜKETME.
  `sende_pro` → onayla.

Uygulama biçimi:
- Saf Kotlin işlev önerisi: `ProPolicy.resolve(inputs): ProState`.
- Birim testleri: gün 0, 6, 7, saat geri alma, hata kodunda sticky PRO,
  iade sonrası düşme, PENDING, fail-open, nudge tavanı, ömür boyu
  sorgusu başarısızken abonelik düşmemesi, bahşişin durumu
  değiştirmemesi.

### 12-c. Ekranlar ve kısa metinler

- Menü satırı "Sende Pro": altında durum ("Deneme: 3 gün kaldı",
  "Aktif", "Ücretsiz"). Rakamlar yerelde hesaplanır.
- Pro sayfası (alt sayfa):
  - Başlık: "Sende Pro". Alt metin tek cümle (değer).
  - 3 plan kartı: Yıllık (varsayılan seçili, "En iyi değer"),
    Aylık, Ömür boyu. Her kartta TOPLAM fiyat büyük; küçük satırda
    dönem: "Her ay otomatik yenilenir" / "Yılda bir otomatik
    yenilenir" / "Tek seferlik. Abonelik değildir." İndirim etiketi.
    Aylık eşdeğer fiyat gösterilecekse küçük ve ikincil (politika).
  - Düğme: "Pro'ya geç". Altında: "Ödemeyi Google Play alır. İstediğin
    an iptal edebilirsin." (abonelik için) + "Aboneliği yönet"
    bağlantısı (derin bağlantı) + "Satın alımları kontrol et".
  - Destek bölümü: üç satır, fiyatlı ("Çay ısmarla" vb.). Teşekkür
    mesajı ve rozet. "Bağış" kelimesi YOK.
  - Kapat düğmesi. Zorla açılan sayfa yok (Kural 3 dışında).
- İlk açılış bilgi satırı: "İlk 7 gün tüm özellikler açık. Sonra temel
  kullanım ücretsiz kalır; Pro isteğe bağlıdır."
- Deneme bitti kartı: "Deneme bitti. Mesajların her zaman Sende. Pro
  özelliklerini istersen aç." [Pro'ya bak] [Şimdilik değil]
- Kilitli özelliğe dokunma: kısa bilgi + Pro sayfası. Özelliğin adını
  söyle.
- Hata metni (`BILLING_UNAVAILABLE`): "Bu cihazda Google Play ödemesi
  kullanılamıyor. Temel kullanım etkilenmez."
- Gizlilik satırı: mevcut yapı; Play sürümünde kurulu manifestten doğru
  cümleyi seçiyor. İzin çıkarılırsa cümle de değişir (bölüm 3).
- Mağaza girdisi: "Uygulama içi satın alma var" etiketi otomatik; açıklamada
  Pro'nun ne olduğu, 7 günlük denemenin kartsız ve yerel olduğu, çekirdeğin
  ücretsiz kaldığı yazılmalı (ek ücret gerektiren özellik anlatılıyorsa
  bunu söylemek zorunlu). [resmi]
  https://support.google.com/googleplay/android-developer/answer/9858738

### 12-d. Koda dönük değişiklik listesi (yapılmadı; öneri)

- `TrialPolicy.kt`: `TRIAL_DAYS = 7`; `Gate.Paywall` günlük kapanmayı
  kaldır; `Gate.ProLocked` ekle; `trialStart` ve `lastSeen` parametreleri.
- `AppPreferences`: `trialStartMillis`, `lastSeenMillis`, `lastEntitledAt`,
  `nudgeCount`, `lastNudgeDay`, `supporterBadge`.
- `SupportViewModel.kt`: `firstInstallTime` yerine kalıcı `trialStart`.
- `PlayPurchaseGateway.kt`: `PRODUCT_ID = "sende_pro"`; INAPP sorgusu
  ve `sende_pro_lifetime`; `onResume` yenilemesi; PENDING işleme;
  hata kodunda sticky; derin bağlantı; ürün kimliğine göre onayla/tüket.
  Prepaid eklenirse `enablePrepaidPlans()`.
- `build.gradle.kts`: `monetization` varsayılanı `donate` kalsın; yeni
  mod adı `pro` (7 gün + Pro). A/B modları test için dursun.
- Play manifest (deneme): [çıkarım]
  ```
  <uses-permission android:name="android.permission.INTERNET"
      tools:node="remove" />
  ```
  Önce cihazda sına (bölüm 3). Sonra CI'daki izin denetimini Play için
  de ekle.
- Dize dosyaları: TR, EN, FR, AR (yenileme satırları, deneme metinleri,
  ömür boyu açıklaması, rozet).
- Testler: `TrialPolicyTest` güncellenir; Robolectric ekran görüntüsü:
  Pro sayfası (3 plan, boş plan, PENDING), deneme çipi; birim test: fiyat
  basamağının tek yönlü olması (aylık eşdeğer azalır).
- `docs/ODEME.md`: kurucu kararından sonra güncellenir (bu kol dokunmadı).

## Denenen ve çalışmayan yollar

- Reddit: arama süzgeci (`reddit.com`) 400 verdi ("domain erişilemez");
  RSS 429; old.reddit.com 403 ("Blocked" sayfası); WebFetch önceden
  "unable to fetch" demişti. Üçüncü taraf arşiv kullanmadım. Reddit
  cümleleri 01/03 kollarından, [tek].
- Stack Overflow: arama süzgeci (`stackoverflow.com`) aynı 400'ü
  verdi. Stack Exchange API (anonim) çalıştı. Tam metin aramada tüm
  kelimeler aranıyor, uzun sorgular boş döndü; etiketli kısa
  sorgular işe yaradı. Ortak IP kotası birkaç dakikada 287'den 159'a
  düştü; bu yüzden yaklaşık 15 çağrıyla sınırlı kaldım.
- XDA: arama özetlerinde XDA'ya ait bulgu çıkmadı. [doğrulanamadı]
- Hacker News Algolia API: çalıştı ama yorumlar gürültülü; yalnız
  2019 ve 2021 başlık sayfaları kullanıldı.
- GitHub: arama sayfası WebFetch ile kısmen okunabildi; issue/PR
  sayfaları açıldı. API ve token kullanılmadı (kural).
- Play mağaza sayfaları (BuzzKill, Simple Thank You): içerik gelmedi
  veya 404. Rakip fiyat/puan bilgileri önceki koldan; Thank You için
  F-Droid ve Fossify sayfaları kullanıldı.
- Mesafeli Sözleşmeler Yönetmeliği: mevzuat.gov.tr sayfası 404 verdi;
  Resmî Gazete dizininden yalnız 2014 metnine ulaşıldı, sonraki
  değişiklikler okunmadı.
- Yardım Toplama Kanunu: ilk geçişte PDF okunamamıştı. Bu geçişte
  mevzuat.gov.tr PDF'i indirildi; makinede PDF aracı yok, küçük bir
  Python ayrıştırıcısı yazıldı. Yazı tipi glif kodlu olduğu için
  Türkçe karakterlerin bir kısmı bağlamdan çözüldü. İlgili maddeler
  elle okundu. Alomaliye'deki kopyayla uyumlu.
- Google "ödeme yöntemleri" sayfası: Türkiye bir ülke listesinde
  geçiyor, ama listenin neye ait olduğu sayfa metninden çıkmadı.
  Operatör faturası için Turkcell sayfası kullanıldı; Vodafone ve
  Türk Telekom bulunamadı.
- Berber/kuaför/KOBİ randevu ve mesaj yazılımı TL fiyatları: arama
  sonuç vermedi.
- Sensor Tower/data.ai Türkiye uygulama içi satın alma geliri:
  bulunamadı; yalnız küresel ve oyun rakamları çıktı.
- Play fiyat aralıkları (Türkiye için asgari/azami): yardım sayfası
  aralık vermiyor.
- Android geliştirici doğrulaması için Android Authority makalesi 403
  verdi; resmi developer.android.com sayfası kullanıldı.
- Çevrimdışı `queryPurchasesAsync` davranışı: resmi sayfalarda yok;
  cihazda sınanmadı.
- RevenueCat 2025 sayfası PDF'in tamamı değil, web özeti; bu geçişte
  yeniden açılmadı. Deneme türleri (kartlı/kartsız) ve Türkiye'ye özel
  rakamlar yok.

## Doğrulanamayan veya bulunamayan şeyler

- Saf bağış/bahşiş dönüşüm oranı için derli toplu yayınlanmış veri
  (yalnız 2013-2024 dağınık rakamlar).
- Türkiye'nin alternatif fatura/harici link programlarına ne zaman
  gireceği (çıkarım: 30 Eylül 2027).
- Bahşiş SSS cümlesinin bir uygulama geliştiricisi için geçerli olup
  olmadığı; Google'ın bahşiş kutusunu "bağış politikası" ile
  işaretleyip işaretlemediği (tek yorum, ayrıntı yok).
- Google Play'in Türkiye'de DST'yi geliştirici payına nasıl yansıttığı,
  hizmet bedelinin KDV'siz tutardan alınıp alınmadığı, prepaid planın
  hizmet bedeli sınıfı, Türkiye için yeni "fatura ücreti".
- Kurucunun kendi KDV/vergi durumu (kaynaklar çelişkili).
- INTERNET izni çıkarıldığında Billing akışının çökmeden çalıştığı
  (cihazda sınanmadı; iki kaynak da sınamadı), çevrimdışı
  `queryPurchasesAsync` davranışı, Billing telemetrisinin Data Safety
  etkisi.
- `clear data`'nın `firstInstallTime`'a etkisi (yalnız çıkarım).
- Türkiye'de operatör faturası kullanan Play alıcılarının payı;
  Vodafone/Türk Telekom işlem sınırları; Turkcell sınırlarının tarihi.
- Yardım Toplama Kanunu'nun bahşiş/destek ürünleri için etkisi (yalnız
  çıkarım); ceza tutarlarının güncel yeniden değerlenmiş hâli.
- RevenueCat'in Türkiye'yi hangi bölgeye saydığı.
- Eş uygulama kalıbının Google Play politikası açısından bugünkü durumu
  ve satış verisi.
- Küçük işletme sahibinin tüketiciden fazla ödeyeceği varsayımı için
  kanıt.

## Sende için çıkarımlar

Ne yapalım:
- Modeli "açık çekirdek + Pro + destek ürünü" yap; çekirdeği
  (yakalama, okuma, geçmiş) hiçbir koşulda kilitleme.
  - Neden: saf bağış gelir getirmez (bölüm 5-b), sert paywall
    kategoride güveni ve puanı bozar. Orta yol ikisini dengeler
    (bölüm 5, 10).
- Pro sınırını LANSMANDAN ÖNCE çiz ve sonradan sadece kullanıcı lehine
  oynat. Ömür boyu ürününe asla reklam ekleme.
  - Neden: sonradan sıkılaştırma öfke topluyor ("ömür boyu aldım,
    şimdi reklam var"; Recent Notification'ın süre kısıtlaması; 01 ve
    03 kolu, tek tek kaynaklar).
- Denemeyi 7 gün, kartsız ve yerel yap; açıkça söyle (tek sakin satır).
  - Neden: 4 gün altı deneme dönüşümü düşük (%25,5 vs %37,4); sessiz
    paywall hem Play ruhuna hem güvene ters (bölüm 6-b).
- Pro ürünlerini 3'e indir: Aylık 99,99, Yıllık 599,99, Ömür boyu
  999,99 TL (varsayım). 3 ve 6 aylık plan yok.
  - Neden: kurucunun 12 aylık planı 3 ve 6 aylıktan aylık bazda pahalı;
    operatör faturası 300 TL/işlem sınırı uzun planları keser;
    kategori tek seferlik sever (bölüm 6-a, 4-b).
- Fiyatı toplam olarak göster, aylık eşdeğeri küçük tut; iptal bağlantısı
  ve yenileme dönemi ekranda olsun.
  - Neden: Play yıllık planı "aylık fiyatla" öne çıkarmayı yasaklıyor ve
    uygulamada iptal yolu istiyor (bölüm 1).
- Bahşişi tüketilebilir ürün olarak tut (tip_small/medium/large),
  "bağış" kelimesini kullanma, küçük işlevsiz rozet ver.
  - Neden: Play yolu bu; dışa bağış linki 2016-2026 arası altı vakada
    reddedildi; Google'ın kendi bildirimi uygulama içi satın almayı
    sayıyor; Türkiye'de yardım toplama hukuku belirsiz (bölüm 1-b, 6-e).
- Play Console'da Hesap Grubu kurup 15% hizmet bedeli katmanına KAYIT ol.
  - Neden: kayıtsız "diğer işlemler" %30'dan başlıyor; ömür boyu ve
    bahşiş gelirini doğrudan etkiler (bölüm 4-a).
- Deneme başlangıcını DataStore'a da yaz (Auto Backup ile geri gelir),
  saat geri almaya karşı `lastSeen` tut.
  - Neden: ucuz, sunucusuz ve yeterli; çekirdek ücretsiz olduğu için
    daha fazlası boşa mühendislik (bölüm 7).
- `queryPurchasesAsync`'i her `onResume`'da çağır (SUBS ve INAPP);
  PENDING'i yönet; hata kodunda son bilinen PRO'yu 30 gün koru; Play
  yoksa fail-open. Ömür boyu ürününü tüketme, bahşişi tüket.
  - Neden: iade/hold kendiliğinden düşsün, ödeme yapan kullanıcı ağ
    hatasında kilitlenmesin (bölüm 2, 12-b).
- INTERNET iznini Play sürümünde çıkarmayı iç test kanalında gerçek
  cihazla sına; çalışırsa uygula ve CI'a ekle; çalışmazsa sözü dürüstçe
  değiştir. Eş uygulama yedek plan.
  - Neden: "izin yok" en çok oy alan güven cümlesi; izin Billing'in
    kendisinden değil Data Transport'tan geliyor; ama kimse cihazda
    kanıtlamadı (bölüm 3).
- Google Play fiyatını TRY'de ELLE gir ve 6 ayda bir yenile.
  - Neden: Play kuru girdiğin günde donduruyor, dolar kuru bir yılda
    %18 yükseldi, enflasyon %29,7 (bölüm 4-a).
- Mali müşavire danış (KDV/stopaj, ihracat istisnası) lansmandan önce.
  - Neden: kaynaklar çelişkili (bölüm 4-a).
- 20+20 testini algı/anlaşılırlık testi olarak yap; "ödeme yöntemin var
  mı" sorusunu ekle; aynı kanalları zorunlu 12 testçi/14 gün şartı için
  kullan.
  - Neden: dönüşümü ölçecek örnek yok, lisans testçisi gerçek para
    ödemiyor (bölüm 6-d).
- APK'yı Android geliştirici doğrulamasına kaydet (2027 öncesi).
  - Neden: Play dışı dağıtım 2027'de zorlaşıyor (bölüm 6-c).

Ne yapmayalım:
- Tek başına saf bağışla ya da "hiçbir şey söylemeden 3 gün" ile
  başlama.
  - Neden: saf bağış ≈ abonelik medyanının onda biri (bölüm 5-b);
    sessiz paywall güveni yakar (bölüm 6-b).
- Sert paywall koyma; günlük açılan kapatılabilir paywall'ı (mevcut
  `trial_*`) kullanma; o da bir dayatmadır.
- Firebase Remote Config, analitik veya reklam SDK'sı ekleme.
  - Neden: INTERNET vaadini bozar; Remote Config ağdan çeker (bölüm 8).
- Uygulama içinden dış ödeme/bağış sitesine link verme (Play sürümü);
  "sadece web sitemize link" hilesine de güvenme.
  - Neden: Payments politikası; StreetComplete ve Stack Overflow
    vakalarında web sayfası linki bile reddedildi (bölüm 1-b).
- Haftalık plan ekleme, reklam gösterme, ücretli kullanıcıya reklam.
  - Neden: kategori şikâyetlerinin başı (bölüm 5).
- Public APK'yı Pro'nun bedava yolu olarak bırakma; APK'yı test/güven
  içinde tut veya Pro'yu orada zaten açık say.
  - Neden: sideload'da Billing yok (bölüm 6-c).
- Play'in ücretsiz denemesini (kart isteyen, otomatik dönen) v1'de
  kullanma.
  - Neden: kartsızlık ve güven; ayrıca ödeme yöntemi sahipliği ve
    operatör faturası sınırı belirsiz (bölüm 2, 4-b, 6-d).
- Kurucunun 3/6/12 aylık planlarını olduğu gibi yayınlama.
  - Neden: tutarsız basamak, kategori çıpasının çok üstünde, operatör
    faturasıyla alınamaz (bölüm 6-a).

Sonraki adımlar (kurucu için kısa liste):
- 1) Pro'ya hangi 2-3 özellik girer, karar ver (en büyük etki).
- 2) Play Console: merchant profili, Hesap Grubu + 15% kaydı,
  `sende_pro` (monthly, yearly), `sende_pro_lifetime`, 3 bahşiş
  ürünü, TL fiyatlar.
- 3) INTERNET izin sınaması (iç test, gerçek cihaz).
- 4) Mali müşavir görüşmesi.
- 5) 20+20 kalitatif test, sonra üretim.
