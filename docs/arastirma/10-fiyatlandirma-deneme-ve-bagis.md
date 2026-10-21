# Sende: fiyat, deneme ve destek (bağış) kararı
Araştırma kolu: para-kazanma

- Tarih: 21 Ekim 2026. Bu dosya bir karar belgesidir; koda dokunulmadı.
- Okuma sırası: "Kısa cevap", sonra "ÖNERİ" (bölüm 11), sonra
  "Teknik şartname" (bölüm 12). Geri kalanı kanıt ve gerekçedir.

## Kısa cevap

- Model: "Açık çekirdek + isteğe bağlı Sende Pro + bahşiş".
  - Mesajı yakalamak, okumak, geçmiş HER ZAMAN ücretsiz kalır.
  - Pro yalnızca zaman kazandıran iş araçlarını açar.
  - Neden: saf bağışın geliri büyük olasılıkla çok düşük kalır
    (yayınlanmış veri bulunamadı), sert kilit ise bu kategoride güveni
    ve puanı yakar. Orta yol ikisini dengeler.
- Deneme: 7 gün, kartsız, uygulama içinde (yerel) ve AÇIKÇA söylenen.
  - "Hiçbir şey söylemeden 3 gün" önerilmez (gerekçe: bölüm 6-b).
- Ödeme: Play sürümünde yalnız Google Play Billing (zorunlu).
  - Sideload (APK) sürümünde ödeme alınamaz.
- Planlar: 3 görünür plan (aylık, 6 aylık, yıllık) + 3 bahşiş.
  - Kurucunun 1/3/6/12 basamağı tutarsız (bölüm 6-a); sadeleştirildi.
- Başlangıç fiyatı (TL, KDV dahil, VARSAYIM): 129,99 / 519,99 / 779,99.
  - Bahşiş: 49,99 / 99,99 / 249,99. İlk 8 haftada gözden geçirilir.
- Bağış: Play sürümünde yalnız "tüketilebilir ürün" (bahşiş).
  - Uygulama içinden dışarı bağış linki koyma: politika yasağı.
  - Metinde "bağış" yerine "destek" de.
- 20+20 kişilik A/B: dönüşümü ÖLÇEMEZ. Sadece anlaşılırlık ve güven
  testi olarak kullan (bölüm 8).
- INTERNET izni: Billing'in kendisi istemiyor; Data Transport istiyor.
  İzni çıkarmayı dene (bölüm 3), ama önce cihazda sına.
- Dürüst gelir beklentisi: abonelik uygulamaları için medyan yaklaşık
  1.000 kurulum başına 100 dolar (14. gün). Bölüm 5.

## Okuma notu ve sınırlar

- Etiketler:
  - [resmi] Google/Android/Firebase resmi sayfası, bu kolda açıldı.
  - [ölçüm] Kendim indirip/okuyup ölçtüm (Maven paketleri, TCMB XML).
  - [çoklu] Birden çok bağımsız kaynakta aynı bulgu.
  - [tek] Tek kaynak; sağlama yapılamadı.
  - [çıkarım] Benim yorumum veya hesabım.
  - [doğrulanamadı] Bulunamadı veya kanıtlanamadı.
- ÖNEMLİ SINIR: bu oturumun web arama kotası (200) başka ajanlarla
  paylaşılıyor ve çalışma ortasında doldu.
  - Sonuç: Reddit, Stack Overflow, XDA, GitHub issue taraması bu kolda
    YAPILAMADI. Kota dolunca arama motorlarını başka yoldan kullanmadım.
  - Yerine: bilinen adreslerin doğrudan açılması (resmi dokümanlar,
    RevenueCat/Adapty raporları, TCMB, Maven) ve önceki kolların (01, 03)
    bulduğu kullanıcı yorumları.
  - Forum kanıtı isteyen maddeler "doğrulanamadı" diye işaretli.
  - Devam edilecekse yeni bir mesajla arama yeniden başlatılabilir.
- Lisans notu: Billing AAR'ı "Android SDK Lisansı", Data Transport
  Apache-2.0 (POM dosyalarında yazıyor). Yalnızca manifest ve sınıf
  adlarına baktım; kod kopyalanmadı.
  - https://dl.google.com/android/maven2/com/android/billingclient/billing/8.0.0/billing-8.0.0.pom
  - https://dl.google.com/android/maven2/com/google/android/datatransport/transport-backend-cct/3.1.8/transport-backend-cct-3.1.8.pom

## 1. Google Play ödeme politikası

- Dijital ürün için Play Billing zorunlu: ücretli indirme (Bölüm 1) ve
  uygulamada özellik/içerik/hizmet satışı (Bölüm 2). Örnekler arasında
  abonelik, "reklamsız sürüm" gibi uygulama işlevi ve bulut yazılım var.
  [resmi]
  - https://support.google.com/googleplay/android-developer/answer/9858738
  - https://support.google.com/googleplay/android-developer/answer/10281818
- Play Billing'in KULLANILMAYACAĞI durumlar (Bölüm 3): fiziksel mal ve
  hizmet, "vergiden muaf bağışlar", kişiler arası ödeme, açık artırma.
  Bağış bu politikada yalnız bu ifadeyle geçiyor: "tax exempt donations".
  [resmi]
  - https://support.google.com/googleplay/android-developer/answer/9858738
- Yorum: "vergiden muaf bağış" kâr amacı gütmeyen kuruluş mantığıdır.
  Bir ticari geliştiricinin kendi uygulaması için istediği paranın bu
  muafiyete girdiğine dair cümle bulunmadı. [çıkarım]
- SSS'de bahşiş: bahşişin %100'ü "yaratıcıya" gidiyor ve karşılığında
  dijital içerik/hizmet verilmiyorsa kişiler arası ödeme sayılıyor,
  Play Billing gerekmiyor. Bu cümle içerik üreticileri için yazılmış;
  bir uygulamanın kendi geliştiricisine bahşişe uygulanıp uygulanmadığı
  belli değil. [resmi + doğrulanamadı]
  - https://support.google.com/googleplay/android-developer/answer/10281818
- Uygulama içinden Play dışı ödeme yöntemine yönlendirmek yasak
  (Bölüm 4): link, buton, webview, mesaj, reklam, kayıt akışı dahil.
  SSS: "alternatif ödemeye götürebilecek bir web sayfasına doğrudan link"
  de yasak. Yalnız hesap yönetimi, gizlilik, yardım merkezi linkleri
  serbest. Uygulamanın DIŞINDA başka satın alma seçeneklerini anlatmak
  serbest. [resmi]
  - https://support.google.com/googleplay/android-developer/answer/10281818
- Bölüm 8 ve 9: "uygun ülkelerde" alternatif fatura ve harici link
  programları var. Sayfa ülke adı vermiyor. Türkiye'nin uygun olup
  olmadığı bulunamadı. [doğrulanamadı]
  - https://support.google.com/googleplay/android-developer/answer/9858738
- Play DIŞINDA dağıtılan uygulama (APK, kendi site) Play Billing
  kullanmak zorunda değil; iade ve destek doğrudan verilebilir. [resmi]
  - https://support.google.com/googleplay/android-developer/answer/10281818
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

## 2. Play Billing 7/8/9 ile abonelik

- Sürümler: 7.0.0 (14 Mayıs 2024) prepaid planlar ve
  `PendingPurchasesParams`; 8.0.0 (30 Haziran 2025)
  `enableAutoServiceReconnection`, "tek seferlik ürün" adı;
  9.0.0 (19 Mayıs 2026); 9.1.0 (18 Haziran 2026). [resmi]
  - https://developer.android.com/google/play/billing/release-notes
- Zorunlu sürüm takvimi: 7 için son tarih 31 Ağustos 2026 (uzatma 1
  Kasım 2026); 8 için 31 Ağustos 2027; 9 için 31 Ağustos 2028.
  Yani şu an yeni sürüm yüklemek için 8+ gerekir. [resmi]
  - https://developer.android.com/google/play/billing/deprecation-faq
  - Bizde `billing-ktx:8.0.0` var (build.gradle.kts, satır 165): 2027
    Ağustos'a kadar yeterli. [ölçüm]
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
- Prepaid plan: otomatik yenilenmez, uzatmak için "top-up" gerekir.
  Top-up daima tam fiyatla; 3 gün içinde onaylanmazsa iade edilir ve
  kalan abonelik iptal olur. `enablePrepaidPlans()` 7.0.0'dan beri var;
  8.0.0 paketinde de var. [resmi + ölçüm]
  - https://developer.android.com/google/play/billing/subscriptions
  - https://developer.android.com/google/play/billing/release-notes
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
    `sende_support`.
  - Dokümana göre `queryPurchasesAsync` her `onResume`'da da çağrılmalı
    (yeni PURCHASED durumuna geçen satın almalar için). Kodda yalnız
    bağlantı kurulunca çağrılıyor.
  - https://developer.android.com/google/play/billing/integrate

## 3. İnternet izni olmadan faturalama

- Kısa cevap: ödeme akışı için uygulamanın INTERNET izni GEREKMİYOR;
  izin Billing'in telemetri bağımlılığından geliyor. Çıkarılıp
  çıkarılamayacağı cihazda sınanmadı. [ölçüm + doğrulanamadı]
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
- Seçenekler:
  - A) Olduğu gibi bırak (ODEME.md'deki mevcut karar). Bu durumda
    mağazada "internet izni yok" cümlesi Play sürümü için YANLIŞ olur.
  - B) Play flavor manifestine `tools:node="remove"` ile INTERNET'i
    (önce yalnız onu) çıkar. Telemetri çalışmaz, ödeme akışı etkilenmez
    beklentisi. Risk: ACCESS_NETWORK_STATE'in iş zamanlayıcıyla bağı
    doğrulanamadı; bu yüzden onu şimdilik bırak. [çıkarım]
  - C) `transport-*` bağımlılığını dışla: Billing sınıfı bunları
    kullanıyor, çalışma anında NoClassDefFoundError riski. ÖNERİLMEZ.
    [çıkarım]
- B için test planı (gerçek cihaz, iç test kanalı, lisans test hesabı):
  - Birleşmiş manifestte INTERNET yok mu (`aapt dump permissions`).
  - Bağlan, ürünleri sorgula, test kartıyla al, onayla, `queryPurchasesAsync`.
  - Uçak modunda da `queryPurchasesAsync` (çevrimdışı davranış
    belgelenmemiş; Play Store önbelleğine mi bakıyor sınanmalı).
  - logcat'te `SecurityException` ve `EACCES` ara.
  - API 26, 30, 34, 36 en az birer cihaz/emülatör (Play imajlı).
- Veri güvenliği formu: "toplamak" = veriyi cihazdan dışarı göndermek.
  Yalnız cihazda işlenen veri toplama sayılmaz. İzin listesi ile veri
  güvenliği bölümü ayrı şeylerdir. Ödeme bilgisini uygulama görmüyorsa
  beyan gerekmez; satın alma geçmişi uygulamaya gelirse düşünülmeli.
  [resmi]
  - https://support.google.com/googleplay/android-developer/answer/10787469
- Güven bağlamı: önceki kol, Reddit'te en çok oy alan güven cümlesinin
  "Buzzkill. İnternet erişim izni bile yok." olduğunu bulmuştu
  (03 ve 08 kolu; bu kolda yeniden açılamadı). [tek]
  - https://www.reddit.com/r/androidapps/comments/pokkkt/_/hcx7sof/

## 4. Türkiye: kur, vergi, hizmet bedeli

- Kur: TCMB 21.10.2026 döviz satış 49,6015 TL/USD; bir yıl önce
  (21.10.2025) 41,9626. Dolar kuru bir yılda %18,2 yükseldi (TL'nin
  dolar karşısında değer kaybı yaklaşık %15). [ölçüm + hesap]
  - https://www.tcmb.gov.tr/kurlar/today.xml
  - https://www.tcmb.gov.tr/kurlar/202510/21102025.xml
- Enflasyon: yıllık TÜFE Eylül 2026 %29,73; Eylül 2025 %33,29.
  [resmi]
  - https://www.tcmb.gov.tr/wps/wcm/connect/TR/TCMB+TR/Main+Menu/Istatistikler/Enflasyon+Verileri/Tuketici+Fiyatlari
- Alım gücü çıpası: 2026 net asgari ücret 28.075 TL (brüt 33.030).
  [tek: Vikipedi, T24'e dayanıyor]
  - https://tr.wikipedia.org/wiki/T%C3%BCrkiye%27de_asgari_%C3%BCcret
  - Hesap: kurucunun 5 dolarlık aylık planı ≈ 248 TL ≈ net asgari
    ücretin %0,88'i. [çıkarım]
- Play'de TRY: Türkiye hem geliştirici hem satıcı kaydı için destekli,
  varsayılan para birimi TRY. [resmi]
  - https://support.google.com/googleplay/android-developer/answer/9306917
- Fiyat girişi: girdiğin fiyat taban olur, yerel fiyat dönüştürülür,
  ülke bazında elle fiyat verilebilir. Dönüşüm "fiyatı girdiğin günün
  kuruyla" yapılır, kendiliğinden güncellenmez; elle yenilemek gerekir.
  Sonuç: TL fiyatı sabit kalırsa dolar karşılığı eriyor. [resmi + çıkarım]
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
    özetinden geldi; birincil kaynak açılamadı. [doğrulanamadı]
- Hizmet bedeli (Türkiye için yeni takvim gelene kadar geçerli olan):
  abonelik %15 (gelir ne olursa olsun); diğer işlemlerde "15% katmanı"
  için ilk 1 milyon dolara %15, üstüne %30. Katmana kayıt gerekip
  gerekmediği sayfada açık değil. [resmi + doğrulanamadı]
  - https://support.google.com/googleplay/android-developer/answer/112622
- Yeni takvim: AB, Birleşik Krallık, ABD 30 Haziran 2026; Avustralya ve
  Japonya 30 Eylül 2026; Güney Kore 31 Aralık 2026; "dünyanın geri
  kalanı" 30 Eylül 2027. Yeni oranlar: abonelik %10; yıllık kazancın
  ilk 1 milyon dolarında her tür işlem %10; tek seferlik yeni kurulumda
  %20, mevcutta %25; ayrıca %5 "fatura ücreti" (şimdilik yalnız AU, JP,
  ABD, BK, AB için; diğerleri "yakında"). Türkiye adıyla anılmıyor;
  "geri kalan dünya"ya düşeceği bir ÇIKARIMDIR. [resmi + çıkarım]
  - https://support.google.com/googleplay/android-developer/answer/16954621
- Kaba net hesabı (VARSAYIM: KDV %20 fiyatın içinde, %15 hizmet bedeli
  KDV'siz tutardan alınır; bu taban Türkiye için doğrulanamadı):
  - 129,99 TL → geliştiriciye ≈ 92 TL (≈ 1,86 dolar).
  - 779,99 TL → ≈ 552 TL (≈ 11,1 dolar).
  - 49,99 TL bahşiş → ≈ 35 TL.
  [çıkarım]
- Prepaid planın hizmet bedeli sınıfı (otomatik yenilenen gibi %15 mi,
  "tek seferlik" gibi mi) bulunamadı. Yeni takvimde "yinelenen" ile
  "yinelenmeyen" ayrı oranlandığı için prepaid riskli. [doğrulanamadı]

## 5. Dönüşüm ve gelir verileri

RevenueCat "State of Subscription Apps 2026" (web özeti): [resmi rapor]
  - https://www.revenuecat.com/state-of-subscription-apps/
- Sert paywall (indirme sonrası 35. günde ücretliye geçen oranı) medyan
  %10,7; freemium %2,1. 14. gün kurulum başına gelir: sert paywall
  2,32 dolar, freemium 0,27 dolar.
- İndirmeden denemeye (30 gün): İş (Business) %9,1; Araçlar (Utilities)
  %6,5. Bölge: Kuzey Amerika %7,1; diğer bölgeler %3,0-3,7.
- Denemeden ücretliye medyan, deneme uzunluğuna göre: 4 gün ve altı
  %25,5; 5-9 gün %37,4; 10-16 gün %35,4; 17-32 gün %42,5.
- 3 günlük denemelerdeki iptallerin %84'ü 0-1. günde (7 günlükte %64).
- MEA bölgesi (Türkiye'nin hangi bölgeye sayıldığı sayfada açık
  değil [doğrulanamadı]): denemeden ücretliye ≈ %20-23; indirmeden
  denemeye %3,0-3,7; dönüşümlerin %63,5'i 0. günde; 14. gün kurulum
  başına gelir ≈ 0,10 dolar; aylık plan payı ≈ %52-55, yıllık payı en
  düşük bölge.
- Fiyat çıpaları: yıllık medyan 34,80 dolar; aylık medyan 7-8 dolar
  (özet metin 10 dolar da diyor, tutarsız). Hindistan/Güneydoğu Asya:
  aylık 3,75, yıllık 18,32 dolar; bu bölgelerde fiyatlar üst pazarların
  yaklaşık %46-54'ü.
- Google Play'de iptallerin %31'i ödeme başarısızlığından (App Store
  %14).
- Uygulamaların %23,2'si abonelik + ömür boyu sunuyor; dörtte biri ömür
  boyu plan sunuyor.

RevenueCat "State of Subscription Apps 2025" (web özeti): [resmi rapor]
  - https://www.revenuecat.com/state-of-subscription-apps-2025/
- Sert paywall %12,11, freemium %2,18 (35. gün). Sert paywall iade oranı
  %5,8, freemium %3,4.
- 60. gün kurulum başına gelir: App Store 0,38 dolar, Google Play 0,14.
  Teklif kullanımı: App Store %17,6; Google Play %7,3.
- 1. yıl elde tutma: yıllık %44,1; aylık %17,0; haftalık %3,4.
- Yıllık planlarda ucuz fiyat grubu %53,7 elde tutma.

Adapty "State of In-App Subscriptions 2026": [resmi rapor]
  - https://adapty.io/state-of-in-app-subscriptions/
- Kurulumdan denemeye %10,9; denemeden ücretliye %25,6; yıllık medyan
  38,42 dolar; aylık 12,99 dolar. Fiyatlar ülkeler arası ~4 kat değişiyor.
  Türkiye "en hızlı büyüyen üç pazardan biri" (Japonya, Meksika ile);
  Türkiye'ye özel rakam yok.
- Sert paywall'ın abone başına ömür boyu değeri yumuşak paywall'dan
  %21 yüksek.

Ne diyor, ne demiyor: [çıkarım]
- Sert paywall 5 kat dönüşüm veriyor; ama bu ölçümler zaten paywall'ı
  seçmiş abonelik uygulamalarından. "Mesajlarına erişim izni isteyen"
  güvene dayalı bir araç için aynı sonuç geçmez (aşağıda kullanıcı sesi).
- Yayınlanmış bir "saf bağış / bahşiş" dönüşüm oranı BULUNAMADI.
  [doğrulanamadı] Freemium medyanının (%2,1) altında olacağını varsaymak
  makul ama kaynaksız bir tahmindir.
- Beklenti büyüklüğü (abonelik uygulamaları medyanı): 14. günde kurulum
  başına ≈ 0,10 dolar (MEA/geri kalan dünya). Yani 1.000 kurulum ≈ 100
  dolar; 10.000 kurulum ≈ 1.000 dolar. Google Play'in 60. gün medyanı
  0,14 dolar. [çıkarım, kaba]
- Okunuşu: ayda 1.000 dolar için ayda yaklaşık 10.000 yeni kurulum
  gerekir. Bu yüzden Play sıralaması (puan, yorum) gelirin asıl
  kaldıracı. [çıkarım]

Kullanıcı sesi (önceki kollar; bu kolda yeniden açılamadı): [tek/çoklu]
- Play'de 9/17 rakipte abonelik/fiyat şikâyeti (275 eşleşme, ~%8);
  Notification History Log'da düşük puanların yaklaşık üçte biri.
  [çoklu, 03 kolu] 
  - docs/arastirma/03-sikayetler-ve-firsatlar.md (bölüm K8)
- "Reklamı kaldırmak için haftalık 18,99 dolar mı?" (Notisave, 2026).
  - https://play.google.com/store/apps/details?id=com.tenqube.notisave&reviewId=01cf1e13-a168-47c4-8d45-decd41ccda49
- "Denemeden sonra ücretlendirilmek istemiyorum ama uygulamada iptal yolu
  yok" (Auto RDM, 2021).
  - https://play.google.com/store/apps/details?id=com.softinit.iquitos.mainapp&reviewId=1f3072c8-d4a8-4a8a-ac02-2711b26aa77d
- Reddit: "Ücretsiz veya tek seferlik olur, abonelikse düşük olsun"
  (2025-01).
  - https://www.reddit.com/r/androidapps/comments/1hqx5hv/app_that_stores_notification_history_and_can_view/
- Notification History Log, 2025-04: "Tek seferlik bağış seçeneği olsa
  desteklerdim" (5 yıldız; 01 kolu). [tek]
- BuzzKill: 3,99 dolar ücretli indirme, 4,70 puan (3.004 yorum), 100K+
  kurulum; yorum "bir kahveden vazgeç, reklamsız iyi uygulamaya öde"
  (01 kolu). [tek; Play sayfası bu kolda açılamadı]
- FilterBox: "denemek için 6 dolarlık satın almaya zorlandım, iade yok"
  (2024-12); Notification Widget 5,99 dolar, ücretsiz deneme yok →
  şikâyet (01 kolu). [tek]
- Recent Notification saklama süresini 3 güne indirince öfke (03 kolu).
  - https://play.google.com/store/apps/details?id=com.libin.notification&reviewId=d2bda8d6-540b-4390-83ad-6354202c9a2a
- Türkiye TL çıpaları (Play TR, 20 Ekim 2026, 01 kolu): BuzzKill 66,99
  TL, FilterBox 90,99-164,99 TL, WAMR 104,99 TL. BuzzKill'in TL fiyatı
  eski kurdan kalmış (3,99 dolar ≈ 198 TL olmalıydı); yani TL çıpalar
  "eski fiyat" izlenimi verir. [çıkarım]

## 6. Kurucunun planı üzerine dürüst değerlendirme

### 6-a. 1/3/6/12 ay basamağı

- Hesap (kur 49,60): 1 ay 5 dolar ≈ 248 TL; 3 ay 10 dolar ≈ 496 TL;
  6 ay 20 dolar ≈ 992 TL; 12 ay 45 dolar ≈ 2.232 TL. [çıkarım]
- Aylık eşdeğer: 5,00 / 3,33 / 3,33 / 3,75 dolar. Yani 12 aylık plan,
  3 ve 6 aylıktan AYLIK bazda PAHALI. Basamak tutarsız; kullanıcı
  3 aylığı seçer, yıllık satılmaz. [çıkarım]
- Piyasa oranı: yıllık medyan / aylık medyan ≈ 3,5-5 kat (RevenueCat
  34,80 / 7-10) ve ≈ 3,0 kat (Adapty 38,42 / 12,99). Kurucunun "12 ay =
  9 ay" oranı bunun yaklaşık iki-üç katı; yıllık plan cazip görünmez.
  [çıkarım; medyanlar farklı uygulamalardan]
  - https://www.revenuecat.com/state-of-subscription-apps/
  - https://adapty.io/state-of-in-app-subscriptions/
- Dört seçenek tek ekranda seçim yorgunluğu yapar; RevenueCat'e göre
  MEA'da aylık pay yüksek, yıllık pay en düşük: kısa taahhüt isteyen
  bir kitle. [çıkarım]
- Play kuralı: yıllık plan en belirgin yerde "aylık fiyat" diye
  gösterilemez; toplam fiyat öne çıkmalı. [resmi]
  - https://support.google.com/googleplay/android-developer/answer/9900533
- Öneri: 3 plan (aylık, 6 aylık, yıllık), basamak düzenli:
  6 aylık = 4 ayın parası (%33 indirim; kurucunun kendi formülü),
  yıllık = 6 ayın parası (%50 indirim). 3 aylık plan görünürden çıkar.

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
    Yönetmeliği) var; bu kolda yalnız cayma süresi (madde 9) ve cayma
    istisnası (madde 15) metni görüldü. Fiyat/süre ön bilgilendirme
    yükümlülüğü (madde 5) bu kolda DOĞRULANAMADI. Platform satıcı olduğu
    için asıl yük Google'da, ama tüketici öfkesi bizde. [kısmen çıkarım]
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
  ve aboneliği için geçerli ödeme yöntemi gerekir.) [çıkarım]

### 6-e. "Zorunlu olmasın, bağış alsak mı?"

- Evet bahşiş alınır, ama tek başına bir iş modeli değildir. Play'de
  yol: tüketilebilir ürün. Gerekçe bölüm 1. [resmi + çıkarım]
- Yapılmaması gereken: uygulama içinde dış bağış sitesine link (Bölüm 4
  yasağı). [resmi]
  - https://support.google.com/googleplay/android-developer/answer/9858738
- Kelime: Türkiye'de "bağış/yardım toplama" 2860 sayılı Yardım Toplama
  Kanunu'na takılabilir; kanun metnine ulaşılamadı (PDF okunamadı).
  Güvenli yol: "bağış" değil "destek"; bahşiş bir ürün satışı gibi
  sunulur ("Çay ısmarla"). Hukuki görüş al. [doğrulanamadı]
- Kimin bahşiş vereceği: rakip yorumlarında "bağış seçeneği olsa
  desteklerdim" gibi cümleler var ama sayısı küçük (tek/az kaynak).
  [tek, 01 kolu]
- Bağış/bahşiş gelir hedefini karşılamaz (bölüm 5). Ama güveni artırır
  ve yorumlarda "adil" algısı yaratır. Bu yüzden Pro'nun YANINA konur,
  yerine değil. [çıkarım]

## 7. Yerel deneme ve sömürü (veri silip yeniden deneme)

- `firstInstallTime`: "uygulamanın ilk kurulduğu an". Mevcut kod
  deneme başlangıcını buradan alıyor (SupportViewModel.kt, satır 31).
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
- ASIL nokta: çekirdek ücretsiz kalıyorsa "sömürünün maliyeti" ~0'dır.
  Sömüren kişi Pro özelliklerini 7 gün daha görür, hepsi bu. Sert kilit
  yoksa sömürü önleme için mühendislik harcamaya değmez; Çözüm 1 ve saat
  önlemi yeterli. [çıkarım]

## 8. A/B testini Play'de yapmanın yolları

- Mağaza girdisi deneyleri yalnız simge, öne çıkan görsel, ekran
  görüntüsü ve (yerelleştirilmiş deneyde) açıklamayı test eder; fiyat
  veya uygulama içi davranışı değil. Aynı anda 1 varsayılan grafik
  deneyi ya da 5 yerelleştirilmiş deney, en çok 2 varyant, 6 ay sonra
  otomatik biter. [resmi]
  - https://support.google.com/googleplay/android-developer/answer/6227309
- Farklı sürümleri farklı gruplara vermek: kapalı test kanalları.
  [resmi]
  - Aynı anda birden çok kapalı test olabilir; testçiler e-posta
    listesi veya Google Grubu ile eklenir.
  - Bir kullanıcı birden fazla kanala uygunsa EN YÜKSEK sürüm kodunu
    alır. İç testteki kullanıcı kapalı/açık yapıyı alamaz.
  - Liste kapasiteleri geniş (kapalı testte liste başına 2.000 kişi).
  - https://support.google.com/googleplay/android-developer/answer/9845334
- Yeni kişisel geliştirici hesabında üretime çıkmak için kapalı testte
  en az 12 testçi, 14 gün kesintisiz opt-in şart. İki ayrı kanalda
  sayımın nasıl yapıldığı belli değil; 20+20 bu şartı karşılar gibi
  duruyor. [resmi + doğrulanamadı]
  - https://support.google.com/googleplay/android-developer/answer/14151465
- Firebase Remote Config bu işi görür ama değerleri Firebase arka ucundan
  ağ üzerinden çeker (REST API bağımlılığı) ve koşullu hedefleme için
  Google Analytics ister. INTERNET gerektirir, "internet izni yok"
  vaadiyle çelişir. ÖNERİLMEZ. [resmi + çıkarım]
  - https://firebase.google.com/docs/remote-config/android/get-started
- Play Console abonelik raporu: abonelik, base plan ve teklif kırılımı;
  ülke; ücretsiz denemeden ücretliye dönüşüm kartı; elde tutma. Uygulama
  SÜRÜMÜ kırılımı belirtilmemiş. [resmi]
  - https://support.google.com/googleplay/android-developer/answer/7391949
- Play'in "fiyat deneyi" özelliği: Console yardım sayfalarında (Para
  kazanma konu listesi, "Merchandising and optimization" sayfası)
  bulunamadı. [doğrulanamadı]
  - https://support.google.com/googleplay/android-developer/answer/16973687
- Önerilen sunucusuz A/B tasarımı (lansmandan sonra, binlerce kurulumda):
  [çıkarım]
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
- KDV: bölüm 4. Kullanıcıya gösterilen fiyat vergi dahildir; faturayı
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

Üç model:
- Model 1: Saf destek (bağış + bahşiş, hiçbir şey kilitli değil).
  - Artı: en yüksek güven, en az risk, "dayatma yok" tam karşılanır.
  - Eksi: gelir büyük olasılıkla en düşük; kaynaklı veri yok [çıkarım].
- Model 2 (ÖNERİLEN): Açık çekirdek + Pro + bahşiş.
  - Artı: ücretsiz kısım "hostage" yok; Pro değer anında kendiliğinden
    sunulur; gelir ölçülebilir; bahşiş güveni korur.
  - Eksi: Pro'ya değerli bir şey koymak gerek (ürün kararı).
- Model 3: Deneme sonrası sert paywall.
  - Artı: veriye göre en yüksek dönüşüm (5 kat).
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
  [çıkarım; 01 ve 03 kolu yorumları, tek tek kaynaklar]
  - https://play.google.com/store/apps/details?id=com.libin.notification&reviewId=d2bda8d6-540b-4390-83ad-6354202c9a2a
- Kural 3 (rahatsız etme tavanı): kendiliğinden çıkan teklif en çok
  ayda bir ve toplam 3 kez; "bir daha gösterme" her zaman var. Başka
  her yerde teklif, kullanıcı bir Pro özelliğine dokununca çıkar.
  [çıkarım]

Gelir hedefine dürüst bakış: [çıkarım]
- Saf bağışın "çok kişiye satış" hedefini karşıladığına dair hiçbir veri
  bulunamadı; abonelik medyanı bile mütevazı (bölüm 5). "Dayatma olmasın"
  tercihi, kurucu farkında olsun olmasın, gelirin büyük kısmını masadan
  kaldırma ihtimali taşır.
- Pro modelinde bile beklenti mütevazı: ~1.000 kurulum = ~100 dolar
  (14. gün medyan). Ayda 1.000 dolar için ayda yaklaşık 10.000 yeni
  kurulum gerekir. Çözüm fiyat artırmak değil; kurulum ve puan.
- "Dayatmasız" olmak, uzun vadede kurulum ve puan getirir: kategori
  yorumları paywall ve abonelik baskısına çok sert tepki veriyor
  (bölüm 5). Bu yüzden Model 2, kısa vadede biraz gelir bırakıp uzun
  vadede sıralamayı kazandırır.
- Kurucuya açık soru (ürün kararı): "Pro'ya hangi 2-3 özellik girer?"
  Bu cevap gelire en çok etki eden tek karardır.

## 11. ÖNERİ

Model: "Açık çekirdek + Sende Pro + bahşiş" (Model 2).

Deneme:
- 7 gün, kartsız, yerel. İlk açılışta tek satır bilgi; son 2 günde
  sakin bir çip; 7. günden sonra tek seferlik "deneme bitti" kartı.
- Süre dolunca hiçbir veri kaybolmaz; yalnız Pro özellikleri kilitlenir.
- Play'in kendi ücretsiz denemesi (kart ister, otomatik döner) v1'de
  KULLANILMAZ. Gerekçe: güven, kartsızlık, politika yükü. [çıkarım]

Planlar ve başlangıç fiyatları (TL, KDV dahil; İLK 8 HAFTA GÖZDEN GEÇİR):
- Aylık: 129,99 TL (≈ 2,62 dolar)
- 6 aylık: 519,99 TL (4 ayın parası; %33 indirim; ≈ 10,5 dolar)
- Yıllık: 779,99 TL (6 ayın parası; %50 indirim; ≈ 15,7 dolar)
- Bahşiş: 49,99 / 99,99 / 249,99 TL (≈ 1 / 2 / 5 dolar)
- Nasıl türedi (varsayım, kesin değil): [çıkarım]
  - Kurucunun dolar fiyatları TL çıpalara göre çok yüksek (aylık 248 TL
    ile BuzzKill 67 TL, FilterBox 91-165 TL).
  - RevenueCat'te Hindistan/Güneydoğu Asya aylık medyan 3,75, yıllık 18,32
    dolar; önerilen aylık bunun ~%70'i, yıllık ~%86'sı. Türkiye'ye özel
    veri yok; bu bir üst-alt sınır çıpasıdır.
  - Net asgari ücretin %0,46'sı (aylık).
  - Yıllık = 6 ay: piyasa yıllık/aylık oranına (3-5 kat) hâlâ biraz
    pahalı ama kurucunun 9 katından çok daha yakın; yıllık plan cazip
    kalır.
- Yeniden fiyatlama kuralı: dolar kuru bir yılda %18 yükseldi, TÜFE
  %29,7. Her 6 ayda bir TRY fiyatını elle yenile. Mevcut abonelere zam
  onay ister (bölüm 4); bu yüzden lansmanda çok düşük başlama.
- İsteğe bağlı lansman teklifi: "yeni müşteri kazanımı" uygunluklu bir
  teklif (örn. ilk dönem %50). Play şartları açıkça yazılmalı (bölüm 1).
- "Ömür boyu" (tek seferlik) v1'de YOK. Gerekçe: LTV'yi sınırlar,
  fiyat doğrulanmadı; ama Reddit'te sürekli istenen şey, ilk 3 ayın
  verisine göre yeniden değerlendir. Eklenirse reklam asla eklenmez.
  [çıkarım]

Pro'ya girecekler (kurucu seçer; aday liste): [çıkarım]
- Toplu dışa aktarma (CSV/metin). Tek konuşmayı paylaşma ücretsiz.
- Uygulamadan yanıtlama (şimdi var; lansmanda Pro'ya konabilir çünkü
  henüz kimseden alınmıyor).
- Gelecek iş araçları: hazır cevaplar (03 kolu ikinci sürüm adayı),
  müşteri notu/etiket, telefon/IBAN algılama.
- Hiçbiri yoksa v1'de "Pro" yerine "Destek" konumla (rozet, tema) ve
  gerçek kilitleri v1.x'te YENİ özelliklerle ekle.

Bağış/bahşiş:
- Üç tüketilebilir ürün. Menüde ve Pro sayfasının altında. "Bağış"
  değil "Çay ısmarla / Kahve / Yemek". Uygulama satın almadan sonra
  hemen tüketir (mevcut kod yapıyor).
- Dışa link yok. Sideload'da bile ilk sürümde link koyma (iki sürümün
  davranışı ayrışmasın).

20+20 testi:
- Anlaşılırlık ve güven testi olarak yap (bölüm 6-d, 8).
- Karar kriteri dönüşüm değil: "kandırıldım" diyen var mı.

Lansman sonrası izleme (Play Console, uygulama içi analitik yok):
- Abonelik kartları (yeni, iptal, elde tutma), ülke kırılımı, puan,
  yorumlar. Puan 4,3 altına inerse paywall yaklaşımını gözden geçir.
  [çıkarım]
- İlk 2.000 kurulumda ödeme yapan oranı %0,5'in altındaysa Pro kapsamını
  ve fiyatı yeniden düşün. Eşik benim yargım. [çıkarım]

## 12. Teknik şartname (uygulayacak kişi için)

### 12-a. Ürün kimlikleri (Play Console)

- Abonelik ürünü: `sende_pro`. Kimlik sonradan değiştirilemez ve
  silinen kimlik yeniden kullanılamaz; bu yüzden şimdi karar ver.
  Mevcut kodda `sende_support` var; değiştirilecek (12-d).
  [çıkarım]
  - Ürün kimliği en çok 40 karakter, ad 55 karakter. [resmi]
    https://support.google.com/googleplay/android-developer/answer/140504
- Base plan'lar (hepsi otomatik yenilenen):
  - `monthly` (1 ay), `semiannual` (6 ay), `yearly` (1 yıl).
  - `quarterly` tanımlanmaz (kodda eşlemede kalabilir, zararsız).
- Teklif: v1'de yok. (Lansman teklifi istenirse "yeni müşteri kazanımı".)
- Grace ve hold: Console varsayılanları; geri satın alma (resubscribe)
  açık. [resmi]
  https://support.google.com/googleplay/android-developer/answer/140504
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
- Play durumu `entitled: Boolean?` (null = bilinmiyor), `lastEntitledAt`.
- Sayaçlar: `nudgeCount`, `lastNudgeDay`.

Durumlar:
- TRIAL (gün 0..6): her şey açık. Gün 0: tek satır bilgi. Gün 5-6:
  "Pro denemen N gün sonra bitiyor" çipi.
- FREE (gün 7+, Pro yok): çekirdek açık. Pro özellikleri kilitli.
  7. günün ilk ön plan açılışında TEK SEFERLİK "Deneme bitti" kartı.
- PRO: `queryPurchasesAsync` `sende_pro` için PURCHASED döndürüyor
  (grace dönemi dahil; Play onu aktif döndürür).
- PENDING: satın alma bekliyor (yavaş ödeme yöntemi). Hak VERİLMEZ;
  "Ödemen onay bekliyor" yazılır. Onay gelince `onResume` yenilemesi
  PRO'ya geçirir. [resmi]
  https://developer.android.com/google/play/billing/integrate
- UNAVAILABLE: Play yok / `BILLING_UNAVAILABLE` / sideload.
  Davranış: fail-open (kilit yok, teklif yok, "burada ödeme yok" metni).

Geçişler:
- TRIAL → FREE: `now - trialStart >= 7 gün`.
- FREE/TRIAL → PRO: Play satın alma döndürür.
- PRO → FREE: Play sorgusu BAŞARILI (OK) ve satın alma yok (iade,
  süre bitişi, hold, pause). Hata kodlarında (NETWORK_ERROR,
  SERVICE_UNAVAILABLE, SERVICE_DISCONNECTED) durum DEĞİŞMEZ; son bilinen
  PRO en çok 14 gün korunur (`lastEntitledAt`). [tasarım]
- Yenileme zamanları: uygulama açılışı, her `onResume`, satın alma
  sonrası, "Satın alımları kontrol et" düğmesi.
- Tek sayaç kuralı: kendiliğinden teklif en çok 30 günde bir, toplam
  3 kez; kullanıcı "bir daha gösterme" derse sıfır. Bahşiş hiçbir durum
  değiştirmez (yalnız teşekkür).

Uygulama biçimi:
- Saf Kotlin işlev önerisi: `ProPolicy.resolve(inputs): ProState`.
- Birim testleri: gün 0, 6, 7, saat geri alma, hata kodunda sticky PRO,
  iade sonrası düşme, PENDING, fail-open, nudge tavanı.

### 12-c. Ekranlar ve kısa metinler

- Menü satırı "Sende Pro": altında durum ("Deneme: 3 gün kaldı",
  "Aktif", "Ücretsiz"). Rakamlar yerelde hesaplanır.
- Pro sayfası (alt sayfa):
  - Başlık: "Sende Pro". Alt metin tek cümle (değer).
  - 3 plan kartı: ad (Aylık / 6 aylık / Yıllık), TOPLAM fiyat büyük,
    küçük satırda yenileme dönemi ("Her ay / 6 ayda bir / yılda bir
    otomatik yenilenir"), indirim etiketi. Yıllık varsayılan seçili.
    Aylık eşdeğer fiyat gösterilecekse küçük ve ikincil (politika).
  - Düğme: "Pro'ya geç". Altında: "Ödemeyi Google Play alır. İstediğin
    an iptal edebilirsin." + "Aboneliği yönet" bağlantısı (derin bağlantı)
    + "Satın alımları kontrol et".
  - Bahşiş bölümü: üç satır, fiyatlı. Teşekkür mesajı.
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
  `nudgeCount`, `lastNudgeDay`.
- `SupportViewModel.kt`: `firstInstallTime` yerine kalıcı `trialStart`.
- `PlayPurchaseGateway.kt`: `PRODUCT_ID = "sende_pro"`; `onResume`
  yenilemesi; PENDING işleme; hata kodunda sticky; derin bağlantı.
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
- Dize dosyaları: TR, EN, FR, AR (yenileme satırları, deneme metinleri).
- Testler: `TrialPolicyTest` güncellenir; Robolectric ekran görüntüsü:
  Pro sayfası (3 plan, boş plan, PENDING), deneme çipi; birim test: fiyat
  basamağının tek yönlü olması (aylık eşdeğer azalır).
- `docs/ODEME.md`: kurucu kararından sonra güncellenir (bu kol dokunmadı).

## Denenen ve çalışmayan yollar

- Web arama kotası: 200 aramalık oturum kotası çalışma ortasında doldu
  (başka ajanlarla paylaşımlı). Kota dolmadan yalnızca 4 arama
  yapabildim (Play ödeme politikası, hizmet bedeli, Türkiye vergisi,
  teklif uygunluğu). Kota dolunca arama motorlarını başka araçla (curl
  vb.) veya site içi arama uçlarıyla kullanmadım. Sonuç: Reddit, Stack
  Overflow, XDA, GitHub issue taraması eksik; sonraki mesajla
  tamamlanabilir.
- Reddit doğrudan açma: engelli ("unable to fetch"). Önceki kol da JSON
  ve old.reddit için 403/429 bulmuştu. Üçüncü taraf arşiv kullanmadım
  (kota kuralı). Reddit cümleleri 01/03 kollarından, [tek].
- Play mağaza sayfaları (BuzzKill, Simple Mobile Tools "Thank You"):
  içerik gelmedi veya 404. Bu yüzden rakip fiyat/puan bilgileri önceki
  koldan alındı.
- Mesafeli Sözleşmeler Yönetmeliği: mevzuat.gov.tr sayfası 404 verdi;
  Resmî Gazete dizininden 2014 metnine ulaşıldı (güncel değişiklikler
  yok). Yardım Toplama Kanunu: Vikipedi 404, mevzuat PDF'i sıkıştırılmış
  olup okunamadı (PDF aracı yok). Sonuç: 2860 sayılı kanun
  doğrulanamadı.
- Google Play ödeme yöntemleri sayfası: Türkiye girdisi (operatör
  faturası, Play bakiyesi) okunamadı. Android `JobInfo`, `ANDROID_ID`
  ve `PackageInfo` sayfaları çok uzun olduğundan özetleyici kısmen
  başarısız oldu; PackageInfo ve Settings.Secure metinleri curl ile
  indirilip doğrudan okundu.
- Play Console "fiyat deneyleri" yardım sayfası bulunamadı (konu listeleri
  ve "Merchandising" sayfası bunu içermiyor).
- 2025 RevenueCat sayfası PDF'in tamamı değil, web özeti; deneme türleri
  (kartlı/kartsız) ve Türkiye'ye özel rakamlar yok.

## Doğrulanamayan veya bulunamayan şeyler

- Saf bağış/bahşiş dönüşüm oranı için yayınlanmış veri.
- Türkiye'nin Bölüm 8/9 (alternatif fatura/harici link) programına uygun
  olup olmadığı.
- Bahşiş SSS cümlesinin bir uygulama geliştiricisi için geçerli olup
  olmadığı.
- Google Play'in Türkiye'de DST'yi geliştirici payına nasıl yansıttığı,
  hizmet bedelinin KDV'siz tutardan alınıp alınmadığı, 15% katmanına
  kayıt gerekip gerekmediği, prepaid planın hizmet bedeli sınıfı.
- Kurucunun kendi KDV/vergi durumu (kaynaklar çelişkili).
- INTERNET izni çıkarıldığında Billing akışının çökmeden çalıştığı
  (cihazda sınanmadı), çevrimdışı `queryPurchasesAsync` davranışı.
- `clear data`'nın `firstInstallTime`'a etkisi (yalnız çıkarım).
- Türkiye'de Play ödeme yöntemleri (kart, operatör faturası, hediye
  kartı) ve hedef kitlenin ödeme yöntemi sahipliği.
- Yardım Toplama Kanunu'nun bahşiş/destek ürünleri için etkisi.
- RevenueCat'in Türkiye'yi hangi bölgeye saydığı.

## Sende için çıkarımlar

Ne yapalım:
- Modeli "açık çekirdek + Pro + bahşiş" yap; çekirdeği (yakalama,
  okuma, geçmiş) hiçbir koşulda kilitleme.
  - Neden: saf bağış gelir getirmez, sert paywall kategoride güveni ve
    puanı bozar. Orta yol ikisini dengeler (bölüm 5, 10).
- Pro sınırını LANSMANDAN ÖNCE çiz ve sonradan sadece kullanıcı lehine
  oynat.
  - Neden: sonradan sıkılaştırma öfke topluyor ("ömür boyu aldım,
    şimdi reklam var"; Recent Notification'ın süre kısıtlaması; 01 ve
    03 kolu, tek tek kaynaklar).
- Denemeyi 7 gün, kartsız ve yerel yap; açıkça söyle (tek sakin satır).
  - Neden: 4 gün altı deneme dönüşümü düşük (%25,5 vs %37,4); sessiz
    paywall hem Play ruhuna hem güvene ters (bölüm 6-b).
- Planları 3'e indir (aylık, 6 aylık, yıllık) ve basamağı tek yönlü
  yap: 129,99 / 519,99 / 779,99 TL (varsayım).
  - Neden: kurucunun 12 aylık planı 3 ve 6 aylıktan aylık bazda pahalı;
    yıllık/aylık oranı piyasanın ~2 katı (bölüm 6-a).
- Fiyatı toplam olarak göster, aylık eşdeğeri küçük tut; iptal bağlantısı
  ve yenileme dönemi ekranda olsun.
  - Neden: Play yıllık planı "aylık fiyatla" öne çıkarmayı yasaklıyor ve
    uygulamada iptal yolu istiyor (bölüm 1).
- Bahşişi tüketilebilir ürün olarak tut (tip_small/medium/large),
  "bağış" kelimesini kullanma.
  - Neden: Play yolu bu; dışa bağış linki politika ihlali; Türkiye'de
    yardım toplama hukuku belirsiz (bölüm 6-e).
- Deneme başlangıcını DataStore'a da yaz (Auto Backup ile geri gelir),
  saat geri almaya karşı `lastSeen` tut.
  - Neden: ucuz, sunucusuz ve yeterli; çekirdek ücretsiz olduğu için
    daha fazlası boşa mühendislik (bölüm 7).
- `queryPurchasesAsync`'i her `onResume`'da çağır; PENDING'i yönet;
  hata kodunda son bilinen PRO'yu 14 gün koru; Play yoksa fail-open.
  - Neden: iade/hold kendiliğinden düşsün, ödeme yapan kullanıcı ağ
    hatasında kilitlenmesin (bölüm 2, 12-b).
- INTERNET iznini Play sürümünde çıkarmayı iç test kanalında gerçek
  cihazla sına; çalışırsa uygula ve CI'a ekle.
  - Neden: "izin yok" en çok oy alan güven cümlesi; izin Billing'in
    kendisinden değil Data Transport'tan geliyor (bölüm 3).
- Google Play fiyatını TRY'de ELLE gir ve 6 ayda bir yenile.
  - Neden: Play kuru girdiğin günde donduruyor, dolar kuru bir yılda
    %18 yükseldi, enflasyon %29,7 (bölüm 4).
- Mali müşavire danış (KDV/stopaj, ihracat istisnası) lansmandan önce.
  - Neden: kaynaklar çelişkili (bölüm 4).
- 20+20 testini algı/anlaşılırlık testi olarak yap; "ödeme yöntemin var
  mı" sorusunu ekle.
  - Neden: dönüşümü ölçecek örnek yok, lisans testçisi gerçek para
    ödemiyor (bölüm 6-d).

Ne yapmayalım:
- Tek başına saf bağışla ya da "hiçbir şey söylemeden 3 gün" ile
  başlama.
- Sert paywall koyma; günlük açılan kapatılabilir paywall'ı (mevcut
  `trial_*`) kullanma; o da bir dayatmadır.
- Firebase Remote Config, analitik veya reklam SDK'sı ekleme.
  - Neden: INTERNET vaadini bozar; Remote Config ağdan çeker (bölüm 8).
- Uygulama içinden dış ödeme/bağış sitesine link verme (Play sürümü).
  - Neden: Payments politikası Bölüm 4 (bölüm 1).
- Haftalık plan ekleme, reklam gösterme, ücretli kullanıcıya reklam.
  - Neden: kategori şikâyetlerinin başı (bölüm 5).
- Public APK'yı Pro'nun bedava yolu olarak bırakma; APK'yı test/güven
  içinde tut veya Pro'yu orada zaten açık say.
  - Neden: sideload'da Billing yok (bölüm 6-c).
- Play'in ücretsiz denemesini (kart isteyen, otomatik dönen) v1'de
  kullanma.
  - Neden: kartsızlık ve güven; ayrıca ödeme yöntemi sahipliği
    belirsiz (bölüm 2, 6-d).

Sonraki adımlar (kurucu için kısa liste):
- 1) Pro'ya hangi 2-3 özellik girer, karar ver.
- 2) Play Console'da `sende_pro` ve 3 tüketilebilir ürünü kur, TL
  fiyatları gir.
- 3) INTERNET izin sınaması (iç test).
- 4) Mali müşavir görüşmesi.
- 5) 20+20 kalitatif test, sonra üretim.
