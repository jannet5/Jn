# Bildirim uygulamalarına gelen şikayetler ve fırsatlar

Araştırma kolu: sikayetler

Tarih: 20 Ekim 2026

## Kısa özet (önce bunu oku)

- 23 Play uygulamasının yaklaşık 10.100 yorumu okundu (EN + TR).
  - 17'si bildirim kaydedici ve silinen mesaj uygulaması.
  - 6'sı yakın alan: WhatsApp Business, Meta Business Suite, Beeper,
    Pushbullet, SMS Organizer, Spike.
- Buna 23 Reddit konusu, 11 Stack Overflow sorusu, 9 GitHub / GitLab /
  forum konusu ve 17 doküman / haber / Hacker News kaynağı eklendi.
  - Toplam 83 ayrı kaynak kümesi. Dizin en altta.
- En büyük şikayet güvenilirlik: uygulama sessizce duruyor.
  - Düşük puanlı yorumların yaklaşık %16'sı. 17 uygulamanın 15'inde var.
  - En kötüsü "uyarı yok": kullanıcı durduğunu günler sonra anlıyor.
- İkinci büyük şikayet para: reklam ve haftalık abonelik.
  - Notification History Log'da düşük puanlıların yaklaşık üçte biri
    (%32 - %34) fiyat / abonelikten şikayetçi.
  - Yorumlarda haftada 13 - 19 dolar yazıyor.
- Üçüncüsü güven: "tüm bildirimlerimi okuyor" korkusu.
  - Türkiye'de bu korku kendine has bir biçim aldı: kopyala-yapıştır
    "kişisel verilerimin kullanılmasına izin vermiyorum" yorumları.
  - WAMR TR "en yeni" örneğinin yarısından fazlası (%56) bu kalıp.
- Android 13 "kısıtlanmış ayar" sorunu gerçek: mağaza dışı (APK)
  kurulumlarda bildirim erişimi kilitli geliyor. Play'den kurulumda
  çıkmıyor (tek çelişkili rapor var, K10).
- Tekrar, kesik metin ve medya şikayetleri sayıca küçük ama gerçek ve
  teknik olarak çözülebilir (bildirim güncellemelerini birleştirmek).
- Kurucunun istekleriyle örtüşen şikayetler çok. Örnek: uygulama
  seçerken "tümünü seç" yok (WAMR, 786 kişi faydalı buldu).
- İşletme sahibi sesi var ve yüksek sesle: "bazı müşterileri
  kaçırıyorum" (Meta Business Suite TR yorumu).
- Çoğu rakip durduğunu geç ya da hiç söylemiyor (Beeper, Buzzkill,
  Unseen). WAMR uyarı veriyor ama düzeltmeye yardım etmiyor. Sağlık
  göstergesi + tek dokunuşla düzeltme büyük fark yaratır.

## Bu belgede ne var (telefonda gezinmek için)

- Önce yöntem (kısa), sonra 22 şikayet kategorisi (K1 - K22).
  - Her kategoride: kaç kaynakta, alıntılar, TekPanel çözümü.
- Sonra: kurucunun isteklerinin kanıtı, fırsatlar,
  "ilk sürümde çözülmesi gereken 10 şikayet" ve
  "TekPanel için çıkarımlar".
- En sonda: denenmeyen yollar, doğrulanamayanlar, kaynak dizini.
- Acele edersen şu üçünü oku: Kısa özet, "ilk sürümde 10 şikayet",
  "TekPanel için çıkarımlar".

## Nasıl araştırdım (yöntem ve güvenilirlik)

- Play yorumları: Play'in web arayüzünün kullandığı yorum ucu.
  - Kendi yazdığım, TLS doğrulaması açık küçük bir istemci ile.
  - 23 uygulama, 10.124 benzersiz yorum (+ 29 yorumluk 3 küçük küme
    analize girmedi). EN ve TR.
  - Sıra: "en alakalı" ve "en yeni". Örnek rastgele DEĞİL.
    Örneklemin ortalama puanı Play'in gösterdiğinden farklı.
    Sayılar yön gösterir, kesin oran değildir.
- Kategori sayıları: anahtar kelime (regex) taraması, kaba.
  - Sayı = o uygulamanın 1-3 yıldızlı yorumlarında eşleşme.
  - "Kaç uygulamada" = en az 3 eşleşme çıkan uygulama sayısı.
  - Alıntıları tek tek elle okuyup seçtim.
- Reddit: reddit.com doğrudan 403 verdi. Herkese açık arşiv
  "Arctic Shift" kullandım (üçüncü taraf, anonim, salt okunur).
  - Gönderi başlığı araması + seçilen gönderilerin yorum ağacı.
- Stack Overflow: Stack Exchange API'si (anonim, salt okunur).
- GitHub / GitLab / forum sayfaları: WebFetch ile anonim okuma.
  Kimlik, token, gh, MCP kullanılmadı.
- Dokümanlar: developer.android.com, source.android.com, dontkillmyapp.
- Hacker News: Algolia arama API'si.
- Alıntı kuralı: kısa; yabancı dildeyse "(çeviri)" yazdım. Orijinali
  linkte. Play yorum linki yorum kimliğini taşır; sayfada o yorum var
  (bir örnekte sayfa içinde yorum metni geçtiğini kontrol ettim).
- "Faydalı buldu" = Play'deki yorum oyu. Büyük sayı = çok kişi aynı
  şeyi yaşıyor demek.
- Tarihler: yorumların yaklaşık %42'si 2019 - 2022, %32'si 2026'dan
  (en yeni sırası). Eski olanlar güncel sürümde düzelmiş olabilir.
  Tarihi her alıntıda yazdım.
- Kod kopyalanmadı. GitHub issue ve Stack Overflow içeriği
  kopyalanmadı, yalnızca özet + link (SO içeriği CC BY-SA).
- İşaretler: [çoklu] = birden çok bağımsız kaynakta var.
  [tek kaynak] = yalnız bir yerde var. [doğrulanamadı] = emin değilim.

## Şikayet kategorileri

Her kategoride: kaç kaynakta geçiyor, kısa alıntılar, TekPanel çözümü.
Kaynak numaraları (P, R, Q, G, D) en alttaki dizine bakar.

### K1. Güvenilirlik: servis duruyor, mesaj kaçıyor

- Kaç kaynakta (sayılar kaba):
  - Play: 17 bildirim uygulamasının 15'inde. Düşük puanlıların ~%16'sı
    (565 yorum / 3.481). En büyük tek şikayet. [çoklu]
  - Reddit: R02, R05, R19, R20 (4 konu).
  - Stack Overflow: Q01, Q02, Q05, Q06, Q07, Q08 (6 soru).
  - GitHub / forum: G01, G02, G03, G09 (4).
  - Doküman: D03, D04, D05, D06 (4).
- Alıntılar:
  - Notisave, 2021 (1.843 kişi faydalı buldu): "Kurduktan sonra bir hafta
    çalıştı. Sonra tüm bildirimleri kaydetmedi, şimdi hiç çalışmıyor."
    (çeviri)
    https://play.google.com/store/apps/details?id=com.tenqube.notisave&reviewId=6274695e-9df6-4ac6-a0e1-132350faf0b7
  - Notisave, 2021 (720): "Artık 4 günde bir duruyor. Kaydedilmesi gereken
    bildirimleri hangi anda kaçıracağını bilemiyorsun; uygulamanın amacı
    da bu." (çeviri)
    https://play.google.com/store/apps/details?id=com.tenqube.notisave&reviewId=41378a7b-9b8d-4977-9fd6-4c7faa7dc623
  - Notisave TR, 2020 (66): "Programa sık sık girmeyince pasife düşüyor."
    https://play.google.com/store/apps/details?id=com.tenqube.notisave&reviewId=96268cdc-0fdf-4473-9ad0-44bb22fb061f
  - Unseen Messenger, 2021 (292): "Durdu. Hata mesajı yok, sebep yok."
    (çeviri)
    https://play.google.com/store/apps/details?id=com.unseen.messenger&reviewId=29d0d9a2-e5df-489e-b07d-0ca9c7b98fc0
  - WAMR, 2020 (415): uygulamanın kendi uyarısı "devre dışı kaldı veya
    kesintiye uğradı, mesajlar kurtarılamaz"; yeniden kurmak çözmemiş.
    https://play.google.com/store/apps/details?id=com.drilens.wamr&reviewId=7e81bdf3-40bd-48bb-8e0d-4b17ca79512b
  - Beeper, 2024 (42): "Arada bağlantısı kopuyor ve bunu bana söylemiyor.
    Acil mesajlar için bildirim almadığım oldu." (çeviri)
    https://play.google.com/store/apps/details?id=com.beeper.android&reviewId=f7575cf9-6baf-498f-8cf3-4d18d4b69c42
  - Reddit, açık kaynak günlük uygulaması: "Birkaç saat izledi, sonra
    durdu." (çeviri)
    https://www.reddit.com/r/androidapps/comments/117kqfw/_/j9css0c/
  - Reddit, Buzzkill "çalışmıyor": sebep başka uygulamanın bildirim
    izniyle çakışmaymış.
    https://www.reddit.com/r/androidapps/comments/1vio1et/_/p2gyjcw/
  - GitHub, NotiFilter #124 (HyperOS 3): dinleyici hiç başlamıyor;
    "Arka planda otomatik başlatma" açılınca düzeldi.
    https://github.com/BURG3R5/NotiFilter/issues/124
  - GitHub, NotiFilter #93: sistem dinleyiciyi başlatmıyor, 19 yorum,
    "planlanmıyor" diye kapatıldı. (Yani bazı cihazlarda çare yok.)
    https://github.com/BURG3R5/NotiFilter/issues/93
  - GitHub, DoNotNotify #8 (Xiaomi 14 Pro): "Xiaomi / HyperOS'ta
    dinleyici standart Android'den fazla izin istiyor." (çeviri)
    https://github.com/anujja/DoNotNotify/issues/8
  - Stack Overflow: dinleyici çökünce telefon yeniden başlayana kadar
    bağlanmıyor (2013 ve 2017 soruları, toplam ~10 bin görüntülenme;
    eski, güncel Android'de doğrulanmadı).
    https://stackoverflow.com/questions/20198283/notificationlistenerservice-stopping-and-cant-be-restarted-without-a-reboot
    https://stackoverflow.com/questions/45124673/notification-listener-service-does-not-work-after-app-is-crashed
  - KDE Connect (2026): güncelleme bildirim aktarımını bozdu, 1.35.5'te
    düzeldi. Bu güvenilirlik sorunu üreticiden bağımsız, güncellemeyle de
    gelebiliyor.
    https://discuss.kde.org/t/notifications-dont-sync-from-phone-to-desktop-on-1-35-x/44525
- Ortak desen: "sessiz" bozulma. Uyarı yok, kullanıcı günlerce fark etmiyor.
  Buzzkill, Beeper ve Unseen yorumlarında benzer cümle: uyarı yok,
  hata mesajı yok.
- TekPanel çözümü:
  - Ana ekranın tepesinde ince bir SAĞLIK ŞERİDİ: "Yakalama açık" veya
    kırmızı "Durdu. Dokun, düzeltelim." Tek dokunuşla düzeltme ekranı.
  - Her açılışta bildirim erişimi + dinleyici bağlı mı kontrolü.
    Ayarlarda "Son yakalanan bildirim: 2 dk önce" satırı.
  - Dinleyici kopunca otomatik yeniden bağlanma (requestRebind) ve
    açılış / güncelleme sonrası tetikleyici. Bileşen kapat-aç hilesi
    başka projelerde var, resmî dokümanda yok [doğrulanamadı]; ayrıca
    sınanacak.
  - Arka planda 6 - 12 saatte bir kontrol (WorkManager). Dinleyici bağlı
    değilse kullanıcıya TEK yerel bildirim: "TekPanel durdu". Sessiz
    kalma yok.
  - Dinleyici içinde ağır iş yok; sadece veritabanına yaz. Gerekirse
    dinleyiciyi ayrı süreçte çalıştır (SO yanıtlarından).
  - Cihaz markasına özel kurulum sihirbazı: K11.
  - Dürüst sınır: Android bildirimi hiç göndermezse (uygulama geç /
    hiç göndermiyorsa) TekPanel de göremez. Bunu yardım ekranında yaz.
    Bkz. K19.

### K2. Tekrar eden bildirimler

- Kaç kaynakta:
  - Play: 5/17 uygulamada belirgin (44 yorum, ~%1) ama oy sayıları yüksek.
  - Stack Overflow: Q03, Q04 (2 soru, toplam ~13,7 bin görüntülenme).
  - GitHub: G06 (1). Reddit: yok (aramada çıkmadı).
- Alıntılar:
  - WAMR, 2020 (1.340): "Mesajlar bazen birkaç kez tekrarlanıyor, bu da
    kullanıcıyı şaşırtıp uygulamayı yeniden başlatmaya zorluyor."
    (çeviri)
    https://play.google.com/store/apps/details?id=com.drilens.wamr&reviewId=b5f0458c-3cd1-4d25-8f5f-3dd4c28c8ca4
  - Stack Overflow: WhatsApp ve Gmail için dinleyici iki kez tetikleniyor;
    en çok oy alan yanıt (16): "grup özet bildirimi" bayrağını ele.
    https://stackoverflow.com/questions/45890487/android-onnotificationposted-is-called-twice-for-gmail-and-whatsapp
  - Stack Overflow: WhatsApp için dinleyici iki kez tetikleniyor; iki
    bildirimden biri sohbet etiketli, biri etiketsiz.
    https://stackoverflow.com/questions/33412124/android-notificationlistenerservice-onnotificationposted-fire-twice
  - WhatsDeleted #11: WhatsApp okunmamış mesaj için periyodik bildirim
    yolluyor, uygulama her birini kayda ekliyor; 2020'den beri açık.
    https://github.com/4nubhav/WhatsDeleted/issues/11
- TekPanel çözümü:
  - Grup özet bildirimlerini (FLAG_GROUP_SUMMARY) satır yapma; sadece
    sayaç / grup başlığı için kullan.
  - Aynı bildirimin güncellemesini (aynı anahtar) YENİ satır yapma,
    mevcut satırı güncelle. Medya da böyle gelir (bkz. K4).
  - Ek kural: aynı paket + başlık + metin 2 sn içinde ise sayma.
    Dikkat: SO'daki bir yanıt bu kuralın bazı bildirimleri kaçırabileceğini
    söylüyor; kuralı dar tut, birim testi yaz.
  - WhatsApp'ın "okunmamış hatırlatma" bildirimi: aynı metin, farklı
    zaman. Aynı içerikse yeni satır açma, "hatırlatma" say.

### K3. Kesik veya eksik mesaj metni

- Kaç kaynakta:
  - Play: sayıca küçük (12 eşleşme, 2/17 uygulama) ama gerçek. [çoklu]
  - Reddit: R11 (1). Kesik görünen "özet" metinleri de var (aşağıda).
- Alıntılar:
  - Unseen, 2021: "WhatsApp'taki tam mesajı neden göremiyorum? Yarıda
    bitiyor. Adı 'Unseen, mesajın yarısı' olmalı." (çeviri)
    https://play.google.com/store/apps/details?id=com.tda.unseen&reviewId=2bc41c5e-3604-40d9-914c-a73e6598b627
  - Unseen, 2021 (38): "Mesaj uzunsa tam görünmüyor; bildirimde ne
    görünüyorsa o." (çeviri)
    https://play.google.com/store/apps/details?id=com.tda.unseen&reviewId=8e243a2e-f7aa-4cce-a9a1-f9d82f1d1404
  - Notisave, 2021 (372): Messenger'dan gelenler sadece "1 yeni mesaj"
    yazıyor, gerçek mesaj yok. (çeviri)
    https://play.google.com/store/apps/details?id=com.tenqube.notisave&reviewId=9772e281-584f-4c19-bf75-6c66b69e0d94
  - Notisave, 2025: Instagram'da mesajın başından birkaç kelime kesiliyor.
    https://play.google.com/store/apps/details?id=com.tenqube.notisave&reviewId=5e54019e-7049-4c08-b751-26b50ae4c643
  - Reddit, Recent Notification için: paylaşım var ama "bir bildirimin
    yalnız ilk beş satırını alıyor." (çeviri)
    https://www.reddit.com/r/androidapps/comments/1jf0ums/_/minf0s4/
- TekPanel çözümü:
  - Metni yalnız ana alandan alma. Sırayla dene: genişletilmiş metin
    (BigText), metin satırları (InboxStyle), konuşma mesajları
    (MessagingStyle). En uzununu sakla.
  - Aynı bildirimin birden çok güncellemesini birleştir; mesaj
    parçalarını sırayla ekle.
  - "1 yeni mesaj" gibi özet metinleri tek başına satır yapma; asıl
    mesaj gelince onunla değiştir.
  - Kaynak uygulama zaten kısaltmışsa yapılacak şey yok. Satırın
    altına küçük ipucu: "Bu uygulama bildirimde bu kadarını gösterdi."
    WhatsApp'ın kendi kesme sınırı [doğrulanamadı].
  - Veriyi asla kesme; tam metni sakla. Kısa görünümde ilk 2 - 3 satırı
    göster, dokununca aynı kutuda uzat (kurucunun isteği I-31, I-33).

### K4. Medya ve profil resmi yok

- Kaç kaynakta:
  - Play: dar tarama 74 eşleşme (~%2), 5 uygulamada belirgin; ama
    en çok oy alan yorumlardan biri bu (2.112 kişi).
  - GitHub: G05. Stack Overflow: Q10 (resim alma sorusu).
  - Not: profil resmi kullanıcı yorumlarında çok az geçiyor. Bu daha
    çok kurucunun isteği (I-35); şikayet verisi değil.
- Alıntılar:
  - Notisave, 2021 (2.112): "Fotoğrafları kaydedemiyor, sadece 'Fotoğraf
    gönderdi' yazıyor." (çeviri)
    https://play.google.com/store/apps/details?id=com.tenqube.notisave&reviewId=6c27bcd8-5b0b-4960-9629-f98c1d82538f
  - WAMR, 2021 (184): "Ses silinince 'ses silindi' bildirimi geliyor ama
    sesi dinleyemiyorsun. Videolar da aynı." (çeviri)
    https://play.google.com/store/apps/details?id=com.drilens.wamr&reviewId=ad1aa7a1-b09b-4c31-9c70-a1fe16e2b948
  - notification-vault #26 (2026): iş profilinde WhatsApp görseli
    "Photo" (kamera simgeli) yazısı olarak kalıyor; görselli ikinci güncelleme
    kaydedilmiyor. Yani görsel ikinci bildirim olarak geliyor.
    https://github.com/snap24/notification-vault/issues/26
  - Stack Overflow: dinleyici ile "resmi nasıl alırım" sorusu; küçük ve
    büyük ikon alınabiliyor, resim alanı null dönüyor.
    https://stackoverflow.com/questions/56861692/how-to-get-image-from-notification
- TekPanel çözümü:
  - Aynı bildirimin güncellemesini izle (K2). "Fotoğraf" yazısı ilk
    bildirim, görsel genelde ikinci güncelleme. İkincisi gelince aynı
    satırı görselle güncelle.
  - Profil resmi: bildirimin büyük ikonu / kişi ikonu. Küçük bir
    önbellekte sakla (kişi başına bir kez).
  - Görseli uygulamanın kendi deposuna kopyala (orijinal silinirse
    kalsın). Boyutu sınırla (kasma: K5).
  - Ses ve video: yakalanamıyorsa dürüst etiket: "Sesli mesaj
    (yakalanamadı)" + orijinal uygulamayı aç düğmesi.
  - Silinen medyayı geri getirme sözü VERME. Pazar bunu vaat edip
    kızdırıyor (Notisave, WAMR, Auto RDM yorumları).

### K5. Yavaş, kasıyor, çöküyor

- Kaç kaynakta:
  - Play: 9/17 uygulamada (253 eşleşme, ~%7; "yavaş / donuyor / kasıyor").
  - GitHub: G04 (1). Reddit: bulunamadı.
  - Kurucunun kendi şikayeti de bu: "aşağı kaydırırken çok kasıyor" (I-44).
- Alıntılar:
  - WAMR, 2022 (391): "Üst düzey cihazda bile yavaş hissettiriyor."
    (çeviri)
    https://play.google.com/store/apps/details?id=com.drilens.wamr&reviewId=ff55856e-ba0f-44fe-aa6e-9980c8db0ed6
  - Notisave, 2019 (345): "Bütün arayüz çok tepkisiz. Üstelik sebepsiz
    kendi bildirimi var, daha da sinir bozucu." (çeviri)
    https://play.google.com/store/apps/details?id=com.tenqube.notisave&reviewId=2d4ff1d7-9804-428e-99e9-61ae1ccebd97
  - WAMR TR, 2026: Redmi Note 13 Pro'da "aşırı ısınma, kasma"; silince
    düzelmiş, "bellek canavarı" demiş.
    https://play.google.com/store/apps/details?id=com.drilens.wamr&reviewId=ce56f2dd-764a-4d87-92db-18793631ef26
  - Notification History Log, 2026 (67): bildirime dokununca uygulama
    zaten açıkken bile sıfırdan, tam açılış ekranıyla yükleniyor.
    (çeviri)
    https://play.google.com/store/apps/details?id=com.ikvaesolutions.notificationhistorylog&reviewId=6d839fdd-4b05-4f9f-8f9f-3fb1a50aa548
  - notification-vault #28 (2026): 287 bin kayıtlı bir uygulamada
    "Tümünü sil" hata vermeden hiçbir şey yapmıyor, ilerleme göstergesi
    yok. [tek kaynak]
    https://github.com/snap24/notification-vault/issues/28
- TekPanel çözümü:
  - Liste sanallaştırılır (LazyColumn, sabit anahtar), veri sayfalanır;
    satır modeli önceden hazırlanır; görseller küçültülüp önbelleğe alınır.
  - Kaydırma ölçümü sürüm kapısı olur (Baseline Profile + Macrobenchmark).
    Hedefler benim önerim: 5 bin satırda atlanan kare < %1.
  - Veritabanında zaman / uygulama / kişi indeksi.
  - Toplu silme parça parça (örn. 500'lük), ilerleme çubuğu, iptal.
  - Bildirime dokununca yeni aktivite açma; tek aktivite, durum korunur.
  - Gereksiz kalıcı bildirim yok: dinleyici için ön plan servisi
    gerekmez (sistem bağlar).

### K6. Pil ve ısınma

- Kaç kaynakta:
  - Play: 10/17 uygulamada ama sayı küçük (87 eşleşme, ~%2).
  - Stack Overflow: Q09 (dinleyici ile gece %23 pil; tek kaynak, sorun
    o uygulamanın kodundan olabilir). Reddit: R09 (istek olarak).
- Alıntılar:
  - Notisave, 2021 (2.112): "Çok pil harcıyor, tüm mesajları da
    algılamıyor." (çeviri)
    https://play.google.com/store/apps/details?id=com.tenqube.notisave&reviewId=6c27bcd8-5b0b-4960-9629-f98c1d82538f
  - Notisave, 2021 (264): "Sürekli açık, günün her saati yaklaşık 1 GB RAM
    kullanıyor; pil çabuk bitiyor." (çeviri)
    https://play.google.com/store/apps/details?id=com.tenqube.notisave&reviewId=121f4ca0-1729-4592-8da5-6ef52ed02f9e
  - WAMR, 2021 (301): "Son güncelleme pili bitiriyor ve telefonu
    ısıtıyor." (çeviri)
    https://play.google.com/store/apps/details?id=com.drilens.wamr&reviewId=fd511ac5-5f83-41dd-b5ba-19a6182d2a5d
  - Reddit, 2026: kullanıcı istek listesine "arka planda çalışırken çok
    pil tüketmeyen" yazmış.
    https://www.reddit.com/r/androidapps/comments/1srmlie/
- TekPanel çözümü:
  - Dinleyici yalnız kuyruğa alır ve Room'a yazar. Görsel küçültme /
    önbellek işi toplu çalışır, uyandırma kilidi yok.
  - Ayarlarda "Bu uygulama pili nasıl etkiliyor" satırı: son 24 saatte
    işlenen bildirim sayısı.
  - Yayın öncesi 24 saatlik pil ve bellek ölçümü (hedefler önerimdir:
    arka planda pilin %1'inin altı, bellek 100 MB altı).

### K7. Reklam

- Kaç kaynakta:
  - Play: 11/17 uygulamada (273 eşleşme, ~%8). Notification History
    Log'da ~%26 (iki örnekte %29 ve %20). Önceki kol: 20 uygulamanın
    12'sinde reklam var.
  - Reddit: R01 (reklamsız tek seferlik anahtar önerisi).
- Alıntılar:
  - WAMR, 2021 (1.938): "Uygulama açık değilken bile her şeyin üstüne
    reklam gösteriyor." (çeviri)
    https://play.google.com/store/apps/details?id=com.drilens.wamr&reviewId=952cab0a-2301-41bc-aec3-07bf51c9215f
  - WAMR, 2021 (3.848): "Herkesin dediği gibi çok reklam var... son
    aylarda izinsiz ShareChat, Moj gibi uygulamalar kuruyor." (çeviri)
    https://play.google.com/store/apps/details?id=com.drilens.wamr&reviewId=7eb0b285-dd20-4ef1-b650-361847138b4c
  - Notification History Log, 2020 (112): bildirimi okuyup çıkınca her
    seferinde tam ekran reklam, altta sürekli banner; tek bildirimi
    silemiyorsun, ödemezsen sadece hepsini. (çeviri)
    https://play.google.com/store/apps/details?id=com.ikvaesolutions.notificationhistorylog&reviewId=58d5c932-29b7-4abd-a051-3d5376d24654
  - Notisave, 2026 (23): "İlk bildirimi doğruladıktan sonra hayatımda
    gördüğüm en aldatıcı reklamlarla karşılaştım." (çeviri)
    https://play.google.com/store/apps/details?id=com.tenqube.notisave&reviewId=0257978d-c98a-4e45-89e4-2c287cc12fb5
- TekPanel çözümü:
  - Reklam yok, reklam SDK'sı yok. Bu aynı zamanda "internet izni yok"
    sözünü mümkün kılar (K9).
  - Ücretsiz sürümde sınır koymak gerekirse özellikle koy, reklamla değil.

### K8. Abonelik ve fiyat zorlaması

- Kaç kaynakta:
  - Play: 9/17 uygulamada (275 eşleşme, ~%8). Notification History
    Log'da düşük puanlıların yaklaşık üçte biri fiyat / abonelikten
    şikayetçi (iki ayrı örneklemde %32 ve %34).
  - Reddit: R01, R04, R21 (3 konu).
- Alıntılar:
  - Notification History Log, 2022 (102): "Premium haftada 14,99 dolar...
    yılda neredeyse 800 dolar." (çeviri)
    https://play.google.com/store/apps/details?id=com.ikvaesolutions.notificationhistorylog&reviewId=1416aa89-142c-449a-b759-1e53249e26c0
  - Notification History Log, 2022 (150): "Çok fazla reklam, çok pahalı.
    Telefon faturandan fazla ödemek istiyorsan bu uygulama." (çeviri)
    https://play.google.com/store/apps/details?id=com.ikvaesolutions.notificationhistorylog&reviewId=4dfbad86-9c76-49f4-9fea-cf7d54a2dd56
  - Notisave, 2026 (63): "Reklamı kaldırmak için haftalık 18,99 dolar mı?
    Netflix'e aylık bu kadar veriyorum." (çeviri)
    https://play.google.com/store/apps/details?id=com.tenqube.notisave&reviewId=01cf1e13-a168-47c4-8d45-decd41ccda49
  - Auto RDM, 2021 (591): "Denemeden sonra ücretlendirilmek istemiyorum
    ama uygulamada iptal yolu yok. Şirkete e-posta attım, cevap yok."
    (çeviri)
    https://play.google.com/store/apps/details?id=com.softinit.iquitos.mainapp&reviewId=1f3072c8-d4a8-4a8a-ac02-2711b26aa77d
  - WAMR, 2022 (391): "Reklamı kaldırmak için satın aldım; reklamlar
    gitmedi, sadece param gitti. Param iadesi istiyorum." (çeviri)
    https://play.google.com/store/apps/details?id=com.drilens.wamr&reviewId=ff55856e-ba0f-44fe-aa6e-9980c8db0ed6
  - Reddit, 2023: "Dışa aktarma hep premium kullanıcılara... ve ben
    meteliksizim." (çeviri)
    https://www.reddit.com/r/androidapps/comments/16mh0ni/_/k19rmkj/
  - Reddit, 2025: reklamsız sürüm için "tek seferlik anahtar" öneriliyor
    (Past Notifications).
    https://www.reddit.com/r/androidapps/comments/1hqx5hv/_/m4u3f0h/
  - Reddit, 2021: kullanıcı 2 dolar ödemiş, "3 saat önceki bildirimlerim
    neden yok" diye kızmış (kurulumdan önceki bildirim beklentisi, K17).
    https://www.reddit.com/r/androidapps/comments/mio5m9/_/gwrkhm5/
- TekPanel çözümü:
  - Haftalık abonelik yok. Ana seçenek tek seferlik "ömür boyu"; istenirse
    yıllık ikinci seçenek.
  - Fiyat satın alma ekranından ÖNCE görünür; ücretsiz denemenin bitiş
    tarihi ve iptal bağlantısı (Play abonelik sayfası) uygulamada.
  - Paywall ilkesi: kullanıcının kaydettiği şeyi görmesini
    engelleme. Süre sınırı kızdırıyor: Recent Notification, 2021 (86):
    saklama süresi 3 güne inmiş, ayar gri / kilitli, "birkaç gün önceki
    bildirimlerim otomatik siliniyor". (çeviri) Kayıt hep durur; sınır
    özellikte olsun (örn. gelişmiş dışa aktarma).
    https://play.google.com/store/apps/details?id=com.libin.notification&reviewId=d2bda8d6-540b-4390-83ad-6354202c9a2a
  - İade / destek e-postasına 24 saatte cevap (Auto RDM örneği).
  - Fiyatı TL ve USD için ayrı düşün; haftalık rakipler 13 - 19 dolar.
    [fiyat kararı kurucunundur; bu madde yalnızca veriyi gösterir]

### K9. İzin korkusu ve gizlilik

- Kaç kaynakta:
  - Play: 12/17 uygulamada, kalıp hariç 252 eşleşme (~%7).
  - Türkiye kalıbı (aşağıda): Notisave TR örnekte %26, WAMR TR %56.
  - Reddit: R02, R03, R08, R12, R13, R17, R18, R22 (8 konu). [çoklu]
  - Hacker News: D16, D17 (2). Haber: D13. Resmî uyarı: Android'in
    kendi ekranı (R17, R18'de kullanıcılar bundan korkuyor).
- Alıntılar:
  - Notisave, 2021 (252): "Samsung Checkout ve banka uygulamalarının
    bildirimleri benim iznim olmadan kaydedilecek şekilde kendiliğinden
    ayarlanmış. Çok şüpheli." (çeviri)
    https://play.google.com/store/apps/details?id=com.tenqube.notisave&reviewId=4f8cba35-492e-400a-a2e8-a846272e3162
  - Notisave TR, 2025 (75): "...azıcık gizliliğe önem verin."
    https://play.google.com/store/apps/details?id=com.tenqube.notisave&reviewId=4284f277-507e-4d98-bf5a-430e9fb4a4d0
  - Reddit, 2021 (en üst yanıt, 8 puan): "Buzzkill. İnternet erişim izni
    bile yok." (çeviri)
    https://www.reddit.com/r/androidapps/comments/pokkkt/_/hcx7sof/
  - Reddit, 2022: "Bildirim dinleyen hiçbir uygulamaya güvenmiyorum.
    OTP ve iki adımlı doğrulama yüzünden Google'ı bile engelliyorum."
    (çeviri)
    https://www.reddit.com/r/Android/comments/vsaagp/_/if6g38q/
  - Reddit, 2026: "Bildirim erişimi, OTP dahil bildirim metnini okuyabilir.
    Her bildirim yöneticisine yüksek güven isteyen uygulama gözüyle bak."
    (çeviri)
    https://www.reddit.com/r/androidapps/comments/1tg2pqb/_/onmxxna/
  - Reddit, 2026: "Denediğim her akıllı filtre ham bildirim metnini
    (2FA kodu, banka bakiyesi) cihaz dışına gönderiyor. Hayır." (çeviri)
    https://www.reddit.com/r/AndroidQuestions/comments/1uq0y0a/
  - Reddit, 2024: Android'in "uygulama mesajlarınızı toplayabilir"
    uyarısını görüp "güvenli mi" diye soran kullanıcı.
    https://www.reddit.com/r/AndroidQuestions/comments/1g6vmh1/
    https://www.reddit.com/r/AndroidQuestions/comments/1bretat/
  - Hacker News, 2026: kötü yazılım, cüzdan OTP bildirimini yakalayıp
    saldırgana yollayabilir. (çeviri)
    https://news.ycombinator.com/item?id=46932725
  - Hacker News, 2016: "Bildirim dinleyici kısmı ürkütücü. Herhangi bir
    uygulama tüm bildirimleri sessizce kopyalayabilir mi?" (çeviri)
    Korku yeni değil, 10 yıllık.
    https://news.ycombinator.com/item?id=11701987
- Türkiye'ye özgü kalıp [tek ülke, ama güçlü]:
  - Türkçe Play yorumlarında kopyala-yapıştır bir metin dolaşıyor:
    "bu uygulamayı ... tarihinde indirdim, kişisel verilerimin (TC
    kimlik, kart, mesaj...) kullanılmasına izin vermiyorum".
  - Notisave TR örneğinde 819 yorumun 215'i (%26); WAMR TR "en yeni"
    örneğinde 300 yorumun 169'u (%56), çoğu 2025'te. Yani şu an da
    sürüyor. Kalıbı yazanların yıldızı karışık (WAMR'da 61'i 5 yıldız).
    Yıldızı düşürmüyor ama güvensizliğin işareti.
  - WhatsApp Business ve Meta Business Suite TR yorumlarında yok
    (örneklemde sıfır): kalıp "başkasının mesajını okuyan" uygulamalara
    yöneliyor. TekPanel tam bu gruba girecek.
  - Örnekler:
    https://play.google.com/store/apps/details?id=com.drilens.wamr&reviewId=de1598d2-e4ab-4800-ac9f-bd31a3de6dfd
    https://play.google.com/store/apps/details?id=com.tenqube.notisave&reviewId=1067f7e0-7b18-43d0-8e73-ae82abc6d249
  - 2022 haberi: benzer yorumların "hiçbir yasal anlamı ya da koruma
    özelliği bulunmuyor" olduğunu yazıyor (olay Ses Kaydedici uygulaması).
    https://www.log.com.tr/?p=747927
  - Kalıbın bildirim uygulamalarına yayılması bizim veriden çıkarım;
    haberde bu uygulamalar geçmiyor. [doğrulanamadı: nedeni]
- TekPanel çözümü:
  - Manifest'te INTERNET izni YOK. Bu mağaza açıklamasının ilk satırı.
    Kullanıcı kendisi doğrulayabilsin: uygulama içinde "Bunu nasıl
    kontrol ederim" ekranı (Ayarlar > Uygulamalar > TekPanel > İzinler).
  - Play "Veri güvenliği": toplanan veri yok, paylaşılan veri yok.
    Tutarlı olmak için çökme raporu, analitik, reklam SDK'sı da yok.
  - Hiçbir uygulama kendiliğinden kaydedilmez. İlk seçimde yalnız
    mesajlaşma ve sosyal uygulamalar önerilir (WhatsApp, WhatsApp
    Business, Instagram, Telegram, SMS). Banka, kimlik ve OTP uygulamaları
    varsayılan KAPALI, açmak için kullanıcı kendisi seçer.
  - İzin gerekçesi en çok iki cümle (kurucunun isteği I-47):
    "TekPanel bildirimlerini yalnız bu telefonda gösterir. İnternet
    izni yok, hiçbir şey dışarı gitmez."
  - Android'in "bu uygulama tüm bildirimleri okuyabilir" uyarısı
    öncesinde ekranda açıkla: "Bu uyarı her bildirim uygulamasında çıkar.
    Sebebi şu: ..."
  - İsteğe bağlı uygulama kilidi (PIN / parmak izi), tek dokunuşla "Tüm
    verileri sil".
  - Türkiye mağaza sayfasında kısa KVKK / gizlilik metni + "izin
    vermiyorum" yorumlarına hazır, kibar geliştirici yanıtı: "Verileriniz
    telefonunuzdan çıkmaz; uygulamada internet izni yoktur."

### K10. İzin verememe: "kısıtlanmış ayar" ve ayar ekranı sorunları

- Kaç kaynakta:
  - Doküman / haber / blog: D07, D08, D09, D10 (4) + GitLab G08. [çoklu]
  - Reddit: R13, R14, R15, R16 (4 konu).
  - Play: Notisave'de 12 yorum "Bu özellik cihazınızda yok" diyor;
    Notification History Log'da 1. Başka uygulamada "kısıtlanmış ayar"
    sözü neredeyse hiç geçmiyor (Play'den kurulumda çıkmıyor).
- Bilinen gerçekler:
  - Android 13'ten beri mağaza dışından (tarayıcı, mesaj uygulaması
    gibi "oturum tabanlı olmayan" kurulum) gelen uygulamalarda bildirim
    erişimi KİLİTLİ gelir. Ekranda: "Güvenliğiniz için bu ayar şu anda
    kullanılamıyor." (çeviri)
    https://chromeunboxed.com/android-13-restricted-setting-feature-blocks-sideloaded-apps-from-notifications/
    https://bayton.org/android/android-13-restricted-permissions/
  - Mağazadan kurulumda sorun yok (Google'ın Esper'e teyidi).
    https://www.esper.io/blog/android-13-sideloading-restriction-harder-malware-abuse-accessibility-apis
  - Açma yolu: Ayarlar > Uygulamalar > uygulama > sağ üst ⋮ > "Kısıtlanmış
    ayarlara izin ver". Bu seçenek, kilitli ayara bir kez dokunulup uyarı
    görülmeden çıkmaz. Hep aynı sıra: önce dene, sonra aç.
    https://help.niagaralauncher.app/article/134-fix-restricted-settings
  - Çelişki notu: Esper (2022) kısıtlamanın o tarihte yalnız erişilebilirliği
    kapsadığını yazıyor. Aynı yıl Reddit'te Mishaal Rahman'ın gönderisi
    (92 puan) ve sonraki kullanıcı raporları bildirim dinleyicisinin de
    kilitlendiğini gösteriyor. Birden çok kaynak: bildirim erişimi dahil.
    https://www.reddit.com/r/Android/comments/vsaagp/
    https://gitlab.com/fdroid/fdroidclient/-/issues/2680
- Alıntılar:
  - F-Droid'den kurulan uygulama için (GitLab #2680): bildirim erişimi
    ve erişilebilirlik gri; aynı uygulama Play'den kurulunca açılıyor.
    Geçici çözüm bilgisayar + adb gerektiriyor. (çeviri)
    https://gitlab.com/fdroid/fdroidclient/-/issues/2680
  - Reddit, 2024 (Android 14): "Bildirim erişimine izin ver" anahtarı
    gri, altında "Kısıtlanmış ayar. Güvenliğiniz için kullanılamıyor."
    (çeviri)
    https://www.reddit.com/r/AndroidQuestions/comments/1bfb05w/
  - Reddit, 2024 (Samsung S21 FE, Android 14): bazı uygulamalarda
    anahtar pasif; kullanıcı ⋮ menüsünde seçeneği bulamamış.
    https://www.reddit.com/r/androidapps/comments/1gv4e9k/
  - Reddit, 2026: "Play Store'dan indirdiğim uygulama için bile
    kısıtlanmış ayara izin veremiyorum." Tek yanıt: Shizuku tabanlı bir
    araç öneriliyor ("karışık sonuçlar aldım"). Kesin çözüm yok.
    [tek kaynak; nedeni doğrulanamadı]
    https://www.reddit.com/r/androidapps/comments/1snb3x4/
  - Notisave, 2020 (326): "İzin ver'e dokunuyorum, ayarlara gidiyor,
    sonra 'bu özellik cihazınızda yok' diyor." (çeviri) Nedeni yorumda
    yok [doğrulanamadı].
    https://play.google.com/store/apps/details?id=com.tenqube.notisave&reviewId=42244139-1bdd-4951-8547-c6f2884b9078
  - Notisave, 2020 (454): "Bütün cihazlarda çalışmıyormuş; bir uyarı
    olmalıydı, internet kotamı boşa harcadım." (çeviri)
    https://play.google.com/store/apps/details?id=com.tenqube.notisave&reviewId=b9398492-c088-47b8-b778-c70e4f8adaf5
- TekPanel çözümü:
  - Ana dağıtım Google Play. Orada bu kilit çıkmaz. APK yan yoldur
    (kurucu istiyor, I-16); APK'da şu akış zorunlu:
    1. Kurulum kaynağını oku (PackageManager.getInstallSourceInfo).
       Play değilse, izin düğmesinden ÖNCE "Kısıtlanmış ayar rehberi"
       göster. (Öneri; farklı üreticilerde sınanacak.)
    2. Rehber: 3 adım, her biri bir ekran görüntüsü + tek cümle
       (kurucunun ekran görüntüleri buraya, I-49).
    3. "İzin ver" düğmesi doğrudan bildirim erişimi ekranını açar
       (ACTION_NOTIFICATION_LISTENER_SETTINGS; varsa uygulamaya özel
       ayrıntı ekranı).
    4. Ekran açılamazsa (Notisave'deki "bu özellik yok" durumu):
       yazılı yedek yol: "Ayarlar'da 'bildirim erişimi' ara."
    5. Dönüşte izni gerçek kaynaktan oku (etkin dinleyici listesi). İzin
       varsa kendiliğinden ana ekrana geç. "İzin verdim ama tekrar
       soruyor" hatası olmasın (kurucu isteği I-50).
  - Rehber metni üreticiye göre değişebilir; "sizde menü adı farklı
    olabilir" notu ekle.
  - İleride sıkılaşma riski: Android 15'te kısıtlı izin listesi
    genişliyor ve "Gelişmiş Onay Modu" geliyor (yalnız arama özetinden
    biliniyor; Android Authority sayfaları 403 verdi) [doğrulanamadı].
    Bu yüzden APK yolunu ana yol yapma.

### K11. Samsung, Xiaomi, Huawei, OnePlus, Oppo, Vivo özel sorunları

- Kaç kaynakta:
  - Play: 11/17 uygulamada (114 eşleşme, ~%3 ama çoğunda "çözüm yok").
  - Stack Overflow: Q02, Q05, Q08 (3). GitHub: G01, G02, G03 (3).
  - Doküman: D03, D04, D05, D06 (4). [çoklu]
- Gerçekler:
  - dontkillmyapp.com puanları (5 = en kötü): Huawei 5, Xiaomi 5,
    OnePlus 5, Samsung 5; Oppo 4, Vivo 3, realme 3, Motorola 3; Pixel 0.
    https://dontkillmyapp.com/
  - Samsung: uyuyan / derin uyuyan uygulamalar, otomatik optimizasyon;
    "firmware güncellemesinden sonra uygulamayı yeniden kısıtlı
    listeye ekleyebilir"; sayfada "geliştirici tarafında bilinen çözüm
    yok" yazıyor.
    https://dontkillmyapp.com/samsung
  - Xiaomi: otomatik başlatma, pilde "kısıtlama yok", son uygulamalarda
    kilitleme; "bu uzantılar için API ve belge yok".
    https://dontkillmyapp.com/xiaomi
  - Huawei: Korunan uygulamalar, uygulama başlatma yönetimi; PowerGenie
    listede olmayan her uygulamayı öldürüyor.
    https://dontkillmyapp.com/huawei
  - Gadgetbridge resmî sayfası: MIUI'de "Açılışta başlat" ve otomatik
    başlatma gerekiyor.
    https://gadgetbridge.org/basics/topics/background-service/
- Alıntılar:
  - Notification History Log, 2019 (59): uygulamanın kendi bildirimi
    "MIUI'deki hatalar yüzünden tüm Xiaomi, Vivo ve Oppo cihazlarda
    desteği bıraktık" demiş; kullanıcı Pro'yu aldıktan SONRA öğrenmiş,
    iade istiyor. (çeviri) Rakip bile pes etmiş.
    https://play.google.com/store/apps/details?id=com.ikvaesolutions.notificationhistorylog&reviewId=dc117005-f8c7-4cdf-b418-3f8be21b72a5
  - Notisave, 2021 (298): "Yardımdaki Vivo kılavuzunu izlesem de işe
    yaramıyor." (çeviri)
    https://play.google.com/store/apps/details?id=com.tenqube.notisave&reviewId=a05ffe81-f736-4a60-bc81-2b84188a2f5a
  - WAMR, 2022 (187): "Honor 9 Lite'ta her 30 - 40 dakikada uygulama
    kapanıyor." (çeviri)
    https://play.google.com/store/apps/details?id=com.drilens.wamr&reviewId=437a25af-6174-4f9a-a34d-e9b6a78c3379
  - BuzzKill, 2024 (10, OnePlus 11): "Pil optimizasyonundan çıkardım,
    arka plan etkinliğine izin verdim, yine kapanıyor." (çeviri)
    https://play.google.com/store/apps/details?id=com.samruston.buzzkill&reviewId=00952e67-fddd-4689-907f-dd14473d57b5
  - Stack Overflow (2017): Huawei ve Xiaomi'de "bir iki hafta sonra
    uygulamayı yeniden kurmak gerekiyor; istisna bile güvenilir
    çözmüyor." (çeviri)
    https://stackoverflow.com/questions/47145722/how-to-deal-with-huaweis-and-xiaomis-battery-optimizations
  - DoNotNotify #8 (2026, HyperOS 3): dinleyici için bildirim erişimi
    + otomatik başlatma + pil "kısıtlama yok" + son uygulamalarda kilit
    birlikte gerekti. Düzeldi.
    https://github.com/anujja/DoNotNotify/issues/8
- TekPanel çözümü:
  - Marka algılayan "Arka planda çalışsın" sihirbazı (ilk kurulumun
    parçası, sonradan Ayarlar'dan da açılır). Her adım: 1 görsel + 1
    cümle + "Aç" düğmesi (ilgili ayar ekranına gider; olmazsa genel
    pil ayarına).
    - Samsung: Pil > "Kısıtlama yok"; uyuyan uygulamalar listesinden çıkar.
    - Xiaomi / Redmi / Poco: Otomatik başlat AÇ; Pil "Kısıtlama yok";
      son uygulamalarda kilitle.
    - Huawei / Honor: Uygulama başlatma > elle yönet, hepsini aç.
    - Oppo / Realme / Vivo / OnePlus: Otomatik başlat + arka plan
      etkinliği + son uygulamalarda kilitle.
  - Sihirbaz sonunda "Test et": TekPanel kendine örnek bir bildirim
    gönderir, yakalanıp yakalanmadığını gösterir. "Çalışıyor" kanıtı.
    (Öneri; ağ gerektirmez. Dinleyicinin kendi bildirimini görüp
    görmediği cihazda sınanacak [doğrulanamadı].)
  - Her açılışta durum kontrolü (K1). Sistem izni sessizce geri alırsa
    sağlık şeridi bunu söyler (Samsung örneği: SO Q05).
  - Mağaza açıklamasında dürüst satır: "Xiaomi, Oppo, Vivo, Huawei'de
    ilk kurulumda ek ayar gerekir." Satın aldıktan sonra öğrenip iade
    isteyen kullanıcı (NHL örneği) olmasın.
  - Test: bu markalarda en az birer GERÇEK telefon veya gerçek cihaz
    bulutu; emülatör bu sorunu göstermez. 7 günlük bırakma testi.

### K12. Arayüz karmaşası

- Kaç kaynakta:
  - Play: 6/17 uygulamada belirgin (33 eşleşme, ~%1) ama oylar yüksek.
  - Reddit: R07, R10 (2 konu, istek biçiminde).
- Alıntılar:
  - WAMR, 2022 (786): "Kötü tasarlanmış. Her uygulamayı uzun listeden
    tek tek işaretlemem gerekti, 'Tümünü seç' yok. Ne kadar saçma."
    (çeviri) Kurucunun I-20 isteğinin aynısı.
    https://play.google.com/store/apps/details?id=com.drilens.wamr&reviewId=5de3958a-fdb4-4916-b19f-49af83913433
  - Notisave, 2019 (345): "Ayarlarda çok hatalı. Sekmeler arası geçiş
    yalnız kaydırmayla. Bildirimin bağlam menüsü çalışmıyor." (çeviri)
    https://play.google.com/store/apps/details?id=com.tenqube.notisave&reviewId=2d4ff1d7-9804-428e-99e9-61ae1ccebd97
  - Reddit, 2025: "Bildirim tepsisini yalnız acil işler için istiyorum.
    Gerisi e-posta gibi ayrı bir bildirim gelen kutusunda dursun,
    ben istediğimde bakayım." (çeviri, özet) TekPanel'in tam fikri.
    https://www.reddit.com/r/androidapps/comments/1o9gsq7/
  - Reddit, 2026: "Güzel ve temiz bir bildirim geçmişi uygulaması?
    Galaxy S20'de yerleşik var ama pek etkili değil." (çeviri)
    https://www.reddit.com/r/androidapps/comments/1r9fpvi/
- TekPanel çözümü:
  - Tek ana ekran. Ayarlar ince bir ok / çekmece arkasında (I-60, I-61).
  - Uygulama seçiminde: "Tümü" düğmesi, arama kutusu, çok bildirim
    alanlar ve seçilenler üstte, dokunulan öğe yerinde kalır (I-20 - I-22).
  - Tek aktivite; bildirimden dönünce yeniden açılış ekranı yok.
  - Kendi kalıcı bildirimimiz yok (Notisave 2019 yorumu: "sebepsiz kendi
    bildirimi var").

### K13. Grup / konuşma ayrımı yok, okunmamış karışıyor

- Kaç kaynakta:
  - Play: 5/17 uygulamada (81 eşleşme, ~%2); WhatsApp Business'ta 1.
  - Reddit: R01. Stack Overflow: Q11. Play: Notifications archive.
- Alıntılar:
  - Notifications archive, 2018 (30): "Bildirimler doğrudan ilgili
    uygulamada açılmalı. Gmail, WhatsApp, Outlook gibi gruplanırsa
    güzel olur." (çeviri)
    https://play.google.com/store/apps/details?id=net.difer.notiarch&reviewId=d5487e24-e20e-4763-9813-8d13859cbde7
  - Notisave TR, 2020 (144): "Okunmamış bildirimlere tıklayınca bütün
    bildirimlerle beraber gözüküyor. Okunmayanları bulmak zorlaşıyor.
    Uygulamalar kendi içindeki başlıklara göre sınıflandırılmıyor."
    https://play.google.com/store/apps/details?id=com.tenqube.notisave&reviewId=6c24b515-1f0a-429b-8a75-f59e74d51654
  - Notisave, 2021 (134): "Instagram bildirimlerinde hesap adı artık yok.
    Instagram'ı da WhatsApp ve Twitter gibi gönderen hesaba göre
    gruplayın." (çeviri)
    https://play.google.com/store/apps/details?id=com.tenqube.notisave&reviewId=fedbd5ab-ebdd-43d4-aac5-8a055d98e05a
  - Zıt istek, Notisave 2020 (60): "Gruplama olmadan, tarihe göre sıralı,
    tüm uygulamaları gösteren ayrı bir sekme olsun." (çeviri)
    https://play.google.com/store/apps/details?id=com.tenqube.notisave&reviewId=b0a161a8-8afb-4765-ab0e-774f4dc67395
  - Notisave, 2023 (5): "WhatsApp Business'ı kurduktan sonra WhatsApp
    mesajlarını göremiyorum; yalnız Business'taki silinen mesajlar
    okunuyor. İki kez yeniden kurdum." (çeviri) Tam kurucunun
    WhatsApp / Business karışıklığı kaygısı. [tek kaynak]
    https://play.google.com/store/apps/details?id=com.tenqube.notisave&reviewId=965c4b75-eb82-4d7b-8aa1-cee2a4b63f2d
  - WhatsApp Business, 2026 (87): "Bire bir sohbetler gruplardan ayrı
    gösterilsin. Çok grup varken önemli kişisel mesajlar kolayca
    kaçıyor." (çeviri)
    https://play.google.com/store/apps/details?id=com.whatsapp.w4b&reviewId=ba107dc7-a0db-485b-b497-0c56c036b69c
  - Reddit, 2025: FilterBox "uygulamaya göre filtreliyor ama günlük
    gruplama yapıyor; günde bir bildirim varsa gruplanmıyor." (çeviri)
    https://www.reddit.com/r/androidapps/comments/1hqx5hv/_/m4vkogq/
- TekPanel çözümü:
  - Üç katman: Kanal (uygulama) > Kişi / sohbet kutusu > mesajlar.
    WhatsApp ve WhatsApp Business paket adıyla AYRI kanaldır; kullanıcı
    birini kapatıp ötekini açabilir (I-27).
  - Bire bir ve grup sohbeti etiketle; grup adı + gönderen adı ayrı
    alanda (SO Q11: eski Android'de grup / bire bir ayrımı için metni
    ayrıştırmak gerekiyor; sağlam bir kural yaz ve test et).
  - İki görünüm: "Gruplu" ve "Zaman akışı" (düz, tarih sırası). İstekler
    zıt; ikisi de olsun.
  - Varsayılan liste: okunmamışlar. Okunanlar ayrı bölümde (K14).
  - Instagram gibi uygulamalarda gönderen adı alanı boş gelirse
    başlıktan türet; yine boşsa "Bilinmeyen" yaz, satırı atma.

### K14. Silince geri alınamıyor, yanlışlıkla silme, kendiliğinden silinme

- Kaç kaynakta:
  - Play: 4/17 uygulamada (32 eşleşme) ama oylar yüksek.
  - Reddit: R06, R08, R09 (3). GitHub: G04 (toplu silme çalışmıyor).
- Alıntılar:
  - Reddit, 2026: "Bildirime dokunuyorum, uygulamayı açıyorum,
    bildirim kayboluyor. Yanlışlıkla önemli bir şeyi temizlediğimi
    hissediyorum. Silinenleri görebilir miyim?" (çeviri)
    https://www.reddit.com/r/AndroidQuestions/comments/1w08pck/
  - Reddit, 2026: "Yanlışlıkla kaydırınca hızlı geri alma sunan
    UnNotification vardı" (yorumlarda yeni Android'de çalışmadığı
    söyleniyor). (çeviri)
    https://www.reddit.com/r/androidapps/comments/1tg2pqb/
  - Notification History Log, 2021 (28): "Yanlışlıkla kaydırdım, günlükte
    yok. Kullanışsız." (çeviri)
    https://play.google.com/store/apps/details?id=com.ikvaesolutions.notificationhistorylog&reviewId=30d1395d-5b9c-47c9-87ed-50890bd749f3
  - WAMR, 2021 (988): "Kendi sildiğim mesajları da kurtarıyor. Bu
    uygulamadan tek tek silmeye, WhatsApp'ta geçirdiğimden çok vakit
    harcıyorum." (çeviri)
    https://play.google.com/store/apps/details?id=com.drilens.wamr&reviewId=e1f5d3e9-86fb-47dd-8214-a2028b1a19dc
  - WAMR TR, 2026: "Uygulamanın kendi kendine bir süre öncesine ait
    geçmiş kayıtlarını sildiğini fark ettim."
    https://play.google.com/store/apps/details?id=com.drilens.wamr&reviewId=be7470b5-971a-4ee9-b809-8f9aed712f30
  - Zıt istekler, tepsi: Notisave 2020 (299) "durum çubuğunu boşaltıyor,
    bu rahatsız edici; boşaltmamalı" derken Reddit 2026 gönderisi tersini
    istiyor: bildirimi tepsiden KALDIRIP günlüğe koysun. (çeviri)
    https://play.google.com/store/apps/details?id=com.tenqube.notisave&reviewId=4096313e-0f19-4653-820c-96df029f0821
    https://www.reddit.com/r/androidapps/comments/1srmlie/
- TekPanel çözümü:
  - "Okundu" = silme değil, arşive taşıma. 5 saniyelik "Geri al"
    şeridi; "Okunanlar" bölümünden geri çıkarılır (I-40, I-41).
  - "Ekranı tertemiz yap": onay sorar (I-42) + sonrasında "Geri al"
    şeridi; arka planda 30 gün çöp kutusu.
  - Hiçbir şey kendiliğinden silinmez. Otomatik temizlik yalnız
    kullanıcı seçerse çalışır ve silmeden önce haber verir.
  - Orijinal bildirimi tepsiden kaldırma: AYAR, varsayılan KAPALI.
    İki zıt istek de karşılanır.
  - Tek tek silme / okundu ücretsiz.

### K15. Arama ve filtre zayıf

- Kaç kaynakta:
  - Play: 5/17 uygulamada ama sayı küçük (38 eşleşme, ~%1).
  - Reddit: R01, R10 (kullanıcı kriter listesi). Meta Business Suite
    EN yorumu (aşağıda).
- Alıntılar:
  - NLog, 2025: "Terk edilmiş. WhatsApp'ı hiç kaydetmiyor. Hangi
    uygulamaların kaydedileceği seçilemiyor. Kayıtlı bildirimlerde
    arama yok, koyu tema yok." (çeviri)
    https://play.google.com/store/apps/details?id=org.hcilab.projects.nlog&reviewId=5c6dd6ce-49dc-4a8d-a502-040f25f7e0ac
  - NLog, 2021 (12): "Çok bildirim geliyor, istediğimi bulmak zor.
    Kara liste, arama ve mesajı kendi yerinde açma lazım." (çeviri)
    https://play.google.com/store/apps/details?id=org.hcilab.projects.nlog&reviewId=0a5916be-d80a-4441-8dd6-38cd7dbed4cf
  - Meta Business Suite, 2025 (1.699): "Mesajlarda arama seçeneği yok."
    (çeviri; yorumun tamamı K19'da)
    https://play.google.com/store/apps/details?id=com.facebook.pages.app&reviewId=1646a95d-b39f-4033-ae48-cf68121724c2
  - Reddit, 2025 kullanıcı kriterleri: "tüm bildirimleri sakla, uygulamaya
    göre süz, bazı uygulamaları yok say." (çeviri)
    https://www.reddit.com/r/androidapps/comments/1hqx5hv/
- TekPanel çözümü:
  - Üstte arama kutusu: tam metin, Türkçe karakter duyarsız (ı / i,
    ş / s, ğ / g). Room FTS ile.
  - Kanal çipleri (I-25), kişi süzgeci, tarih aralığı, "yalnız
    okunmamış". Arama ve süzgeç ücretsiz.

### K16. Kopyalama, paylaşma, dışa aktarma

- Kaç kaynakta:
  - Play: 5/17 (43 eşleşme, ~%1). Reddit: R11, R21. Meta Business Suite
    TR yorumu (işletme).
- Alıntılar:
  - Reddit, 2025: "Günlükteki bir bildirime dokunup paylaş menüsünü
    açabileceğim bir uygulama arıyorum." Yanıtlarda Recent Notification
    önerilmiş; birisi "yalnız ilk beş satırı alıyor" demiş. (çeviri)
    https://www.reddit.com/r/androidapps/comments/1jf0ums/
  - Reddit, 2023: "Dışa aktarma işlevi hep premium'a ayrılmış."
    (çeviri)
    https://www.reddit.com/r/androidapps/comments/16mh0ni/
  - Meta Business Suite TR, 2020 (66): "Fotoğraf altına gelen fiyat
    isteklerine dönerken panodan yapıştır çıkmıyor; 4 ayrı fiyat
    menümü her kişi için tek tek kopyalıyorum."
    https://play.google.com/store/apps/details?id=com.facebook.pages.app&reviewId=dd69882d-df80-476b-bd46-4a69e1e275d0
- TekPanel çözümü:
  - Basılı tutunca Kopyala + Paylaş (I-43). Tam metni kopyalar, kesik
    metni değil (K3).
  - Metinde telefon, IBAN, adres, bağlantı algıla: dokununca kopyala /
    ara / haritada aç. Esnaf için pratik küçük kazanç. (Öneri.)
  - Dışa aktarma (metin / CSV): kurucunun fiyat kararı; en az
    "tek konuşmayı paylaş" ücretsiz olsun.

### K17. Yalnız kurulumdan sonrasını tutuyor

- Kaç kaynakta:
  - Play: 4/17 (23 eşleşme). Reddit: R04. Doküman / HN: D02, D12, D15.
- Alıntılar:
  - Auto RDM, 2021 (473): "Silinen mesajları kurulumdan SONRA
    kurtarıyor. Ben kurulumdan ÖNCE silinenleri istiyordum." (çeviri)
    https://play.google.com/store/apps/details?id=com.softinit.iquitos.mainapp&reviewId=0dd0a54e-289f-49a4-91de-174e30ce457c
  - Auto RDM, 2023 (74): "Yanıltıcı. Zaten silinmiş mesajları getirir
    sanıp indirdim, gelecekte olanları değil." (çeviri)
    https://play.google.com/store/apps/details?id=com.softinit.iquitos.mainapp&reviewId=aafbc51f-46a1-4049-ac1c-4157681feba4
  - Reddit, 2021: kurulumdan önceki bildirimleri gösteren günlük
    uygulaması soruluyor; önerilen uygulama için "yalnız son 7 gün
    görünüyor, kurulumdan öncesini kapsar mı belli değil" deniyor.
    https://www.reddit.com/r/androidapps/comments/mio5m9/
  - Android'in kendi geçmişi de aynı: "açılmadan önceki bildirimleri
    kaydetmez".
    https://appuals.com/android-notification-history-not-showing/
- TekPanel çözümü:
  - İlk ekranda tek dürüst satır: "TekPanel kurulumdan sonra gelen
    bildirimleri tutar. Öncesini geri getiremez." Mağaza açıklamasında da.
  - İlk açılışta tepsideki mevcut bildirimleri içeri al
    (getActiveNotifications). Ekranda: "Şu an telefonda bekleyen 14
    bildirim alındı" (I-18).
  - "Silinen mesajı kurtarırım" vaadi YOK. Vaat: "Hepsi tek yerde,
    kaybolmaz."

### K18. Özel durumlar: iş profili ve ikinci kopya uygulamalar

- Kaç kaynakta: GitHub G05, G07 (2). [çoklu ama küçük]
- Alıntılar:
  - notification-vault #26 (2026): iş profilinde WhatsApp görseli
    yalnız metin olarak kaydediliyor.
    https://github.com/snap24/notification-vault/issues/26
  - voicenotify #174 (2025): iş profili uygulamalarında kara liste
    çalışmıyor; hepsi okunuyor.
    https://github.com/pilot51/voicenotify/issues/174
- TekPanel çözümü:
  - Bildirimin kullanıcı kimliğini (UserHandle) kaydet; ikinci kopya /
    iş profili bildirimlerini ayrı kanal etiketiyle göster ("WhatsApp
    (iş)"). İlk sürümde tam destek yoksa açıkça yaz.
  - Samsung "Çift Mesajlaşma" gibi ikinci kopya özelliklerinde bu turda
    kaynak bulunamadı [doğrulanamadı]; gerçek cihazda sınanacak.

### K19. İşletme sahibi: müşteri mesajı kaçıyor, bildirim gelmiyor

- Kaç kaynakta:
  - Play: Meta Business Suite'te düşük puanlı yorumların ~%17'si
    (EN 50 / 291) ve ~%10'u (TR 25 / 254) "bildirim" sözcüğünü
    geçiriyor; WhatsApp Business'ta EN 22, TR 7 yorum.
  - Reddit: R19, R20 (işletim sistemi bildirim vermiyor), r/smallbusiness
    (aşağıda, güvenilirliği düşük). Önceki kol: işletmeye yazılmış
    bildirim uygulaması bulunamadı.
- Alıntılar:
  - Meta Business Suite TR, 2019 (49): "Sayfalarımda satış yapıyorum,
    mesajlara anında cevap vermek önemli. Bildirimler açık olduğu halde
    sesli ya da görsel hiçbir bildirim yok. Bu yüzden bazı müşterileri
    kaçırıyorum."
    https://play.google.com/store/apps/details?id=com.facebook.pages.app&reviewId=d4515bf3-2456-467e-adb3-5a5db43701b3
  - Meta Business Suite TR, 2023 (38): "Gönderi ve reklam bildirimleri
    tıkır tıkır çalışıyor ama işletmeye gelen Facebook / Instagram
    mesajlarında çalışmıyor. Girip bakarsam görüyorum. İzinlerin hepsi
    açık."
    https://play.google.com/store/apps/details?id=com.facebook.pages.app&reviewId=8e878994-8997-46bc-9cde-2497d5106844
  - Meta Business Suite TR, 2021 (22): "Sayfaya gelen mesajlar çok geç
    düşüyor. Bildirim tamamen keyfe keder."
    https://play.google.com/store/apps/details?id=com.facebook.pages.app&reviewId=6ef4ebd7-f9c1-4d70-a45b-6443efcf3a1b
  - Meta Business Suite EN, 2025 (1.699): "Sayfaya gelen mesajlar düzenli
    olarak gecikiyor, bazen 24 saatten fazla. Mesajlarda arama yok.
    Bildirimler temizlenmiyor; okunmamış mesaj var diyor ama gelen
    kutusunda görünmüyor." (çeviri)
    https://play.google.com/store/apps/details?id=com.facebook.pages.app&reviewId=1646a95d-b39f-4033-ae48-cf68121724c2
  - WhatsApp Business TR, 2021 (648): "Business'ı kurduğum andan beri
    mesaj bildiriminin sesi çıkmıyor. Bazen tuş kilidi varken mesaj
    gelince bildirim görünmüyor; Business'ı açıp kendim bakmam
    gerekiyor."
    https://play.google.com/store/apps/details?id=com.whatsapp.w4b&reviewId=af16c473-4834-4da7-89ad-b3767e2b44c3
  - Reddit, 2023 (Android 13): "Güncellemeden beri uygulama açılmadıkça
    bildirimler gelmiyor; pil optimizasyonunu kapattım, yine olmadı."
    (çeviri, özet)
    https://www.reddit.com/r/AndroidQuestions/comments/1364rsk/
  - Reddit, 2022 (Android 11): "Çoğu kaynaktan, o uygulama açılmadıkça
    bildirim almıyorum." (çeviri)
    https://www.reddit.com/r/AndroidQuestions/comments/uice5r/
  - Reddit r/smallbusiness, 2026-10 [tek kaynak; pazar araştırması
    gönderisi olabilir, moderatör uyarısı var]: "Bazen iki kişi aynı
    müşteriye yanıt veriyor, bazen mesaj gömülüyor çünkü herkes başkası
    ilgileniyor sanıyor." (çeviri)
    https://www.reddit.com/r/smallbusiness/comments/1xb1dso/
- Önemli ayrım:
  - Bu şikayetlerin bir kısmı kaynak uygulamanın bildirimi GEÇ ya da HİÇ
    göndermesi. TekPanel yalnız Android'in ilettiğini görür.
- TekPanel çözümü / fırsat:
  - Dürüst sınır: yardım ekranında "Bildirim telefona hiç gelmediyse
    TekPanel de göremez" + ilgili uygulamanın bildirim / pil kontrol
    listesi (3 madde, görselli).
  - "Yanıt bekleyen müşteriler" hissi: okunmamış sayısı büyük rozet;
    satırda "3 sa önce" gibi bekleme süresi; uzun bekleyen satır
    vurgulanır. Okundu akışı (I-40, I-41) bunu doğal verir.
  - Kanal başına ayrı bildirim sesi / profili ("yalnız WhatsApp
    Business'tan gelince çal") ileriki sürüm önerisi.
  - Rakiplerin hiçbiri "işletme sahibi" diye konumlanmıyor (önceki kol
    bulgusu). Mağaza dili: "Müşteri mesajı kaçırma."

### K20. Android'in yerleşik geçmişi yetersiz ve gizli (fırsat)

- Kaç kaynakta:
  - Doküman / haber: D02, D11, D12. Hacker News: D14, D15. Reddit: R06,
    R10, R21 (3). [çoklu]
- Bilinenler:
  - Yerleşik geçmiş "üreticiler için isteğe bağlı bir özellik"; varsayılan
    saklama 1 gün; kapatılınca tüm kayıtlar siliniyor.
    https://source.android.com/docs/core/display/notification-history
  - Pixel'de varsayılan kapalı; kullanıcı varlığını bilmeli.
    https://www.reddit.com/r/AndroidQuestions/comments/1w08pck/_/p6bb2fq/
  - Hacker News (2026): uygulamayı kaldırırsan o uygulamanın kayıtları
    görünmüyor; kapatıp açınca eski kayıtlar siliniyor; süre sınırı var.
    https://news.ycombinator.com/item?id=47720855
  - Hacker News (2026): "Android'in benzer bir bildirim geçmişi var ama
    varsayılan kapalı ve açıkça izin gerektiriyor." (çeviri)
    https://news.ycombinator.com/item?id=47707221
  - Pixel'de geçmişten bildirime dokununca uygulama düzgün açılmıyordu,
    Mart 2024 güncellemesiyle düzeldi.
    https://9to5google.com/2024/03/15/google-pixel-notification-history-fix/
- Alıntılar:
  - Reddit, 2026 (Samsung): "Bildirim geçmişi ana bildirim ayarlarında
    değil. Çok kafa karıştırıcı. Yıllarca bilmiyordum." (çeviri)
    https://www.reddit.com/r/AndroidQuestions/comments/1w08pck/_/p6b9qhw/
  - Reddit, 2023: "Android kullanıcısıyım hep, şimdiye kadar fark
    etmemişim." (çeviri)
    https://www.reddit.com/r/androidapps/comments/16mh0ni/_/k19rtxo/
  - Recent Notification, 2026 (28): "Bildirime dokununca asıl gönderi
    yerine rastgele gönderiler açılıyor." (çeviri) Dokununca doğru yere
    gitme zamanla bozuluyor.
    https://play.google.com/store/apps/details?id=com.libin.notification&reviewId=a4b571a8-6eb7-44ca-8da7-37a2b9313bc6
- TekPanel çözümü / fırsat:
  - Mesaj tek cümle: "Android'in kendi geçmişi 1 gün tutar ve gizlidir.
    TekPanel kalıcıdır ve gruplar." Karşılaştırma yazısı değil.
  - Dokununca ilgili yere git (I-39): bildirimin kendi PendingIntent'i;
    çalışmazsa yedek olarak uygulamayı aç. Silinmiş bildirimin
    PendingIntent'inin çalışıp çalışmadığı cihazda sınanacak
    [doğrulanamadı].

### K21. Android 15+ hassas içerik gizleme (yeni risk)

- Kaç kaynakta: D01 (resmî doküman). Reddit: R08, R12, R13 (OTP korkusu,
  aynı yöne bakıyor).
- Resmî metin: "Android, güvenilmeyen NotificationListenerService
  uygulamalarının, OTP algılanan bildirimlerde redakte edilmemiş içeriği
  okumasını engelleyecek." (çeviri)
  https://developer.android.com/about/versions/15/behavior-changes-all
- TekPanel çözümü:
  - Bu bir hata değil, sistem kuralı. Gizlenen içerikli satıra küçük
    etiket: "Android bu bildirimin içeriğini gizledi." Aksi halde
    kullanıcı uygulamayı suçlar.
  - İşletme sahibi için OTP zaten istenen içerik değil; sorun değil,
    ama etiket şart.
  - Hangi cihaz ve sürümde ne kadar gizlendiği bu turda ölçülemedi
    [doğrulanamadı]; Android 15 / 16 cihazda sınanacak.

### K22. Güven ve dolandırıcı algısı

- Kaç kaynakta: Play: WAMR, Auto RDM, Notification History Log
  yorumları (K7, K8, K11'de); Reddit R02, R03, R22; önceki kol: "Notisave"
  adını taklit eden en az 4 Play girdisi; Play'de "TekPanel: Sipariş
  Yönetimi" adlı başka bir uygulama var.
- Alıntılar:
  - Reddit, 2026: "Kurtarılmış mesajlar için WAMR güvenli mi?" Yanıt:
    "bu uygulamalar sadece bildirimlerinden kaydeder; bunu telefonunda
    zaten yapabilirsin." (çeviri)
    https://www.reddit.com/r/androidapps/comments/1uzkuw4/
  - WAMR 3.848 oylu yorum: "izinsiz başka uygulama kuruyor" (K7).
- TekPanel çözümü:
  - Geliştirici kimliği net: gerçek ad / firma, destek e-postası, web
    sayfası gizlilik metniyle. Mağaza sayfasında "İnternet izni yok"
    kanıtı (K9).
  - Ad çakışması ve taklit riski: önceki kol (01) ayrıntılı. Karar
    kurucunun; burada yalnız risk: kullanıcı güveni isme bağlı.

## Kurucunun istekleri ile şikayetler örtüşüyor mu

Kurucunun istek defteri: docs/ISTEKLER.md (I-numaraları oradan).

- Örtüşenler (başkaları da aynı şeyi yaşıyor):
  - I-20 "Tümünü seç" yok: WAMR 786 oy (K12).
  - I-18 / I-17 kurulumdan önceki / mevcut bildirimler: Auto RDM, Reddit
    (K17).
  - I-27 WhatsApp ve Business karışması: Notisave 2023 (K13). [tek kaynak]
  - I-31, I-33 kesik metin: Unseen, HexCoders, Recent Notification (K3).
  - I-35 medya: Notisave 2.112 oy (K4). Profil resmi şikayeti çıkmadı.
  - I-40, I-41 okundu ve okunmamış ayrımı: Notisave TR 144 oy (K13).
  - I-42 "ekranı temizle" korkusu: yanlışlıkla silme (K14).
  - I-43 kopyalama: Reddit paylaş menüsü isteği (K16).
  - I-44 kasma: WAMR, Notisave, Redmi yorumu (K5).
  - I-46 - I-50 izin akışı: Notisave "bu özellik yok", Android 13 kilidi
    (K10), izin korkusu (K9).
- Kullanıcı yorumlarında çok az geçenler (talep doğrulanamadı):
  - I-21, I-22 sıralama (çok mesaj alan üstte, dokunulan yerinde): bu
    kolda doğrudan karşılığı bulunamadı. [doğrulanamadı]
  - I-24 paket adı (com.whatsapp) gizleme: bu kolda ölçülmedi.
  - I-36, I-38 yanıtla / uygulamaya özgü eylemler: şikayet az (52 eşleşme,
    ~%1). İlk sürüm için düşük öncelik kanıtı yok ama acil de değil.
- İstek defterinde karşılığı yok ya da zayıf, ama kullanıcıların çok
  yazdığı:
  - Sessiz durma uyarısı (K1), üretici sihirbazı (K11), reklam /
    abonelik politikası (K7, K8), gizlilik kanıtı (K9; I-57 kısmen
    değiniyor). Bunlar plana eklenmeli.

## Fırsatlar (rakiplerin boş bıraktığı yerler)

- F1. İşletme sahibi için konumlanma.
  - Kanıt: Meta Business Suite TR yorumları "müşteri kaçırıyorum"
    diyor (K19). Önceki kol: işletmeye yazılmış bildirim uygulaması yok.
  - Eylem: mağaza dili "müşteri mesajı kaçırma". "Yanıt bekleyen"
    görünümü, bekleme süresi.
- F2. Dürüst fiyat.
  - Kanıt: haftalık 13 - 19 dolar yorumları; Reddit'te tek seferlik
    ödeme önerisi (K8).
  - Eylem: reklamsız, haftalıksız, tek seferlik ana seçenek.
- F3. Doğrulanabilir gizlilik: "internet izni yok".
  - Kanıt: Reddit'te en üst yanıt "Buzzkill. İnternet izni bile yok."
    (8 puan, K9). Türkiye'de "izin vermiyorum" kalıbı.
  - Eylem: kanıtı uygulama içinde göster (izin listesine kısayol).
- F4. Sessiz bozulmayı yok et.
  - Kanıt: Beeper, Buzzkill, Unseen yorumları "uyarı yok" (K1).
  - Eylem: sağlık şeridi, test bildirimi, "TekPanel durdu" bildirimi.
- F5. Üretici sihirbazı.
  - Kanıt: bir rakip Xiaomi / Vivo / Oppo desteğini bıraktı (K11).
  - Eylem: marka bazlı kurulum, gerçek cihaz testi, dürüst mağaza notu.
- F6. Android'in 1 günlük, gizli geçmişinin üstüne çık.
  - Kanıt: Reddit, HN, AOSP (K20). Kullanıcılar var olduğunu bile
    bilmiyor.
  - Eylem: "kalıcı ve gruplu" mesajı.
- F7. "Bildirim gelen kutusu" fikri.
  - Kanıt: Reddit 2025 "e-posta gibi ayrı gelen kutusu" (K12).
  - Eylem: ana mesaj bu; "okundu" = e-posta arşivi mantığı.
- F8. Türkçe öncelikli deneyim.
  - Kanıt: TR yorumlarında gizlilik kalıbı (K9); WhatsApp Business TR
    bildirim şikayetleri (K19).
  - Eylem: Türkçe rehber ekranları, TL fiyat, Türkçe karakter duyarsız
    arama, kibar yorum cevap şablonu.
- F9. Esnaf için küçük kazançlar.
  - Kanıt: Meta Business Suite TR "fiyat listesini her kişiye tek tek
    kopyalıyorum" (K16).
  - Eylem: telefon / IBAN / adres algıla ve kopyala. Hazır cevap
    şablonu ikinci sürüm adayı (yeni özellik, onay gerekir).

## İlk sürümde mutlaka çözülmesi gereken 10 şikayet

Sıra: kaç kişiyi vurduğu, kullanıcıyı ne kadar kızdırdığı, kurucunun
hedefi (satış + güven). Her birinin sonunda "bitti sayılır" ölçütü.

1. Sessizce durma / mesaj kaçırma (K1).
   - Çözüm: sağlık şeridi, otomatik yeniden bağlanma, "TekPanel durdu"
     bildirimi.
   - Bitti: sistem ayarından erişimi kapatınca şerit 5 sn içinde
     kırmızı olur; açınca düzelir. Ayrıca 7 günlük bırakma testi.
2. Üretici öldürmesi: Samsung, Xiaomi, Oppo / Vivo, Huawei, OnePlus (K11).
   - Çözüm: marka sihirbazı + "Test et" + dürüst mağaza notu.
   - Bitti: 4 markada gerçek cihazda 7 günde yakalama kaybı yok (saatlik
     test bildirimi).
3. İzin korkusu / gizlilik (K9).
   - Çözüm: internet izni yok + kanıt ekranı; hassas uygulamalar
     varsayılan kapalı; 2 cümlelik izin metni.
   - Bitti: CI'da "manifest'te INTERNET yok" otomatik testi; ilk
     seçim ekranında banka / OTP uygulamaları kapalı.
4. Reklam ve abonelik zorlaması (K7, K8).
   - Çözüm: reklam yok, haftalık yok, fiyat önce görünür, iptal
     bağlantısı uygulamada.
   - Bitti: satın alma ekranı fiyat + süre + iptal yolunu tek ekranda
     gösterir; hiçbir reklam SDK'sı bağımlılık listesinde yok.
5. İzin verememe: kısıtlanmış ayar ve "özellik yok" ekranı (K10).
   - Çözüm: kurulum kaynağı kontrolü, 3 adımlık görsel rehber, doğrudan
     ayar ekranı, yazılı yedek yol, dönüşte gerçek kontrol.
   - Bitti: APK ile kurulmuş Android 13/14/15 cihazda rehberle izin
     verilir; izin verildikten sonra tekrar sorulmaz.
6. İçerik doğruluğu: tekrar ve kesik metin (K2, K3).
   - Çözüm: güncellemeleri tek satırda birleştir, özet bildirimi ele,
     en uzun metni sakla.
   - Bitti: aynı bildirimin 3 güncellemesi tek satır; 600 karakterlik
     mesaj eksiksiz saklanır; grup özeti satır olmaz.
7. Medya ve profil resmi (K4).
   - Çözüm: ikinci güncellemeyle gelen görseli aynı satıra ekle; profil
     resmi önbelleği; yakalanamayan medya için dürüst etiket.
   - Bitti: fotoğraflı WhatsApp bildirimi aynı satırda görselle görünür.
8. Okunmamış / okundu ve konuşma ayrımı (K13).
   - Çözüm: kanal > kişi > mesajlar; WhatsApp ve Business ayrı;
     varsayılan liste okunmamışlar; iki görünüm.
   - Bitti: bir kişinin 5 mesajı tek kutuda; "Okundu" yalnız düğmeyle.
9. Geri alma ve kurulum öncesi dürüstlüğü (K14, K17).
   - Çözüm: okundu = arşiv + 5 sn geri al; ekranı temizle = onay + geri
     al; hiçbir şey kendiliğinden silinmez; ilk ekranda kurulum öncesi
     uyarısı; mevcut bildirimleri içeri al.
   - Bitti: yanlışlıkla temizlenen ekran 1 dokunuşla geri gelir.
10. Kasma, ısınma, pil (K5, K6).
    - Çözüm: sanallaştırılmış liste, sayfalama, indeks, toplu silme
      parça parça, dinleyicide ağır iş yok.
    - Bitti: 5 bin satırda kaydırmada atlanan kare < %1; 24 saatlik
      ölçümde arka plan pil payı < %1 (hedefler önerimdir).

Sonraki sürüme bırakılabilecekler: arama süzgeci ayrıntıları (K15),
kopyala / dışa aktar süslemeleri (K16), iş profili / ikinci kopya
(K18), kanal başına ses profili (K19). Arama ve kopyalama temel
düzeyde ilk sürümde zaten olmalı (I-43); ertelenecek olan ayrıntılardır.

## TekPanel için çıkarımlar

### Ne yapalım (ve neden)

- Sağlık şeridi + test bildirimi ekle.
  - Neden: en büyük şikayet "sessizce durdu"; çoğu rakip bunu
    söylemiyor (Beeper, Buzzkill, Unseen yorumları).
- 4 markada (Samsung, Xiaomi, Oppo / Vivo, Huawei / Honor) gerçek
  cihaz testi + 7 gün bırakma; emülatör yetmez.
  - Neden: dontkillmyapp 5/5 puanlı markalar; bir rakip bu yüzden
    desteği bıraktı (NHL, K11).
- Marka bazlı "arka planda çalışsın" sihirbazı.
  - Neden: Samsung sayfası "geliştirici tarafında çözüm yok" diyor;
    tek çare kullanıcıya doğru adımları göstermek.
- Manifest'ten INTERNET iznini çıkar ve CI'da kontrol et; mağaza
  açıklamasının ilk satırı yap.
  - Neden: Reddit'te en çok oy alan güven cümlesi bu; Türkiye'de
    "izin vermiyorum" kalıbı var.
- Hassas uygulamalar (banka, kimlik, OTP) varsayılan kapalı; hiçbir
  uygulama kendiliğinden kaydedilmesin.
  - Neden: Notisave 252 oylu "kendiliğinden ayarlanmış" şikayeti.
- Reklamsız, haftalıksız fiyat: tek seferlik ana seçenek.
  - Neden: haftada 13 - 19 dolar yorumları; Reddit'te tek seferlik
    talebi.
- Play'i ana dağıtım yap; APK'ya kısıtlı ayar rehberi ekle, kurulum
  kaynağına göre göster.
  - Neden: kilit yalnız mağaza dışı kurulumda; Android 15 ile
    sıkılaşabilir.
- Bildirim güncellemelerini tek satırda birleştir; grup özetini ele;
  en uzun metni sakla; görseli ikinci güncellemeden al.
  - Neden: tekrar, kesik metin ve medya şikayetlerinin ortak kökü.
- "Okundu" = arşiv + 5 sn geri al; orijinal bildirimi tepsiden silme
  ayarı varsayılan KAPALI.
  - Neden: silme korkusu (K14) ve zıt istekler (tepsi).
- Gruplu ve düz akış olmak üzere iki görünüm; WhatsApp ile Business ayrı
  kanal.
  - Neden: zıt istekler (Notisave 2020) ve karışıklık şikayeti.
- İlk ekranda dürüst vaat: "kurulumdan sonrasını tutar".
  - Neden: Auto RDM yorumları; Android'in kendi geçmişi de aynı.
- Android 15+ için "içerik Android tarafından gizlendi" etiketi.
  - Neden: resmî kural; etiket yoksa kullanıcı uygulamayı suçlar.
- Kaydırma akıcılığı ve pil ölçümünü sürüm kapısı yap.
  - Neden: kurucunun ilk şikayeti kasma; rakipler 1 GB RAM / pil
    yorumu alıyor.
- Türkiye için: TL fiyat, Türkçe karakter duyarsız arama, Türkçe rehber,
  "izin vermiyorum" yorumlarına hazır kibar cevap.
  - Neden: Türkçe yorumlarda gizlilik kaygısı çok yüksek (K9).

### Ne yapmayalım (ve neden)

- Ücretsizde "son 3 gün" gibi görme sınırı koyma.
  - Neden: Recent Notification 10 günden 3 güne indirince kızgın
    yorumlar geldi.
- Kendiliğinden silme (otomatik temizlik) varsayılan olmasın.
  - Neden: WAMR TR kullanıcısı "geçmiş kayıtlar kendiliğinden silinmiş"
    diye puan kırdı.
- Haftalık abonelik, otomatik deneme tahsilatı, iptal yolunu
  saklamak yok.
  - Neden: Auto RDM ve Notification History Log yorumları.
- Reklam, analitik, çökme raporu SDK'sı ekleme.
  - Neden: INTERNET izni yok sözünü bozar; çökme raporu için yerel günlük
    + kullanıcı elle paylaşsın.
- "Silinen mesajı / medyayı kurtarırız" diye pazarlama yapma.
  - Neden: Notisave, WAMR, Auto RDM'nin en çok oy alan şikayetleri bu
    vaadin tutmaması; Android'in bildirimi tek mesajdan ibaret.
- Dinleyicinin içinde ağır iş (görsel işleme, ağ, büyük sorgu) yapma.
  - Neden: dinleyici çökerse bazı cihazlarda yeniden başlayana kadar
    bağlanmıyor (SO Q07, 2017 sorusu; güncel Android'de doğrulanmadı).
- APK'yı ana dağıtım yapma.
  - Neden: kısıtlı ayar kilidi; Play dışında güven sorunu da büyük.
- Uzun hukuki izin metni yazma.
  - Neden: korku büyütür (kurucu da böyle söylemişti, I-47).
- Kendi kalıcı bildirimini zorunlu kılma.
  - Neden: Notisave 2019 yorumu: "sebepsiz kendi bildirimi var".
- Emülatörde çalıştı diye geçme.
  - Neden: üretici öldürmeleri emülatörde görünmez.
- Başka uygulamanın adına / ikonuna benzeme.
  - Neden: önceki kol (01 belgesi) Notisave'i taklit eden en az 4 Play
    girdisi buldu; güven isme bağlı. Ad çakışması riski de orada.

## Denenen ve çalışmayan yollar

- reddit.com doğrudan (search.json): 403, Reddit'in kendi "Blocked"
  sayfası. Çözüm: herkese açık arşiv Arctic Shift (üçüncü taraf).
  - pullpush.io: 429, "ücretsiz kazıma yok" dedi. Bırakıldı.
  - Arctic Shift sık sık "Timeout. Maybe slow down" döndü.
    Tam metin araması (`query=`) çok yavaş; gönderi BAŞLIĞI araması
    (`title=`) daha hafif çalıştı. Sorgular arasına 10 - 12 sn koydum.
    Türkçe alt reddit'ler ("bildirim geçmişi", r/Turkey) ve
    r/tasker "notification listener" sonuç vermedi. Reddit'te Türkçe
    şikayet sesi bu yüzden zayıf; yerine Türkçe Play yorumlarını kullandım.
  - Silinmiş veya moderatörce kaldırılmış gönderilerin gövdesi yok;
    onlardan yalnız başlık okundu, alıntı yapılmadı.
- google-play-scraper kütüphanesi: DENEDİM ve BIRAKTIM.
  - İçe aktarılınca TLS doğrulamasını genel olarak kapatıyor
    (`ssl._create_unverified_context`). Ortam kuralı bunu yasaklıyor.
  - Bazı uygulamalarda isteği sonsuza kadar bekletti (zaman aşımı yok).
  - Çözüm: kütüphaneyi sildim; ondan gelen veriyi de çöpe attım; kendi
    küçük istemcimi yazdım (varsayılan, doğrulamalı TLS; 25 sn zaman
    aşımı) ve bütün yorumları yeniden çektim. Belgedeki bütün Play
    verisi bu ikinci çekimden. Her Play yorum linkini veriyle tek tek
    eşleştirdim.
- Play'de "Bildirim Gizle - Geçmişi Gör" (TR uygulama) 0 yorum döndü;
  DoNotNotify (7), NotiWeave (7), Notistar (15) yorumu az, analize
  girmedi.
- Stack Overflow web sayfaları engelli (önceki kollar da yazmış).
  Stack Exchange API'si (anonim, salt okunur) çalıştı.
- Samsung Community (eu. ve r2.) sayfaları WebFetch'te 403 verdi.
  Yalnız arama özetleri var (24 saat sınırı, "6 saat" iddiası): bulgu
  olarak kullanılmadı. [doğrulanamadı]
- Android Authority sayfaları (Android 15 hassas bildirim, kısıtlı
  ayar genişlemesi) 403. GrapheneOS forum sayfası yüklenmedi.
- X.com: arama aracı hiç sonuç vermedi; Twitter / X şikayetleri
  bulunamadı. AlternativeTo da sonuç vermedi. Technopat ve DonanımHaber
  forumlarında bu konuda başlık bulunamadı.
- Hacker News: 4 yorum kullanıldı; bu iş üzerine ayrı bir tartışma
  konusu çıkmadı.
- Xataka (2016) "WhatsApp uzun mesajı kesiyor": sohbet içi kesme, bildirimle
  ilgisiz. Home Assistant forumunda bir push bildirim sorunu: dinleyiciyle
  ilgisiz. İkisi de kullanılmadı.
- F-Droid GitLab sayfasının çekilen içeriğinde GitLab CLI ile dal /
  birleştirme isteği açmamı isteyen gömülü talimat vardı. Görevimin
  parçası değildi, uygulanmadı.
- Kaç kaynakta geçtiğini sayarken regex'e güvendim; eşleşmeler kaba.
  Bu yüzden her kategoride alıntıları elle okuyup seçtim ve yüzdeleri
  "yaklaşık" olarak verdim.

## Doğrulanamayanlar ve güvenilirlik notları

- Play örneklemi "en alakalı" ve "en yeni" sırasından. Rastgele değil;
  1 yıldızlılar ve çok oy alanlar fazla temsil ediliyor olabilir.
  Örneklem ortalamaları Play'in gösterdiği puandan farklı.
- Yorumların yaklaşık %42'si 2019 - 2022 arası (kaba sayım, iki sıra
  arasındaki yinelenenler dahil). Eski şikayetler güncel sürümlerde
  düzelmiş olabilir; her alıntıda tarihi yazdım.
- Kategori yüzdeleri anahtar kelime taraması: yanlış pozitif / negatif
  var. Medya kategorisinde geniş tarama %13, dar tarama %2 verdi;
  dar olanı kullandım.
- Notisave "Bu özellik cihazınızda yok": nedeni yorumlarda yok.
  Tahminim: bildirim erişimi ayar ekranının açılamaması; doğrulanamadı.
- Play'den kurulan uygulamada "kısıtlanmış ayar" çıkması (R14) tek ve
  çözümsüz rapor. Nedeni (başka mağaza, sonradan güncelleme, üretici
  yazılımı) bilinmiyor.
- Android 15'te kısıtlı izin listesi genişlemesi ve Gelişmiş Onay Modu:
  yalnız arama özeti. Bu kolda doğrulanmadı.
- Android 15 OTP gizleme: resmî metin var ama hangi bildirimlerin ve
  hangi cihazlarda gizlendiği ölçülmedi.
- WhatsApp'ın uzun mesajı bildirimde kendi tarafında nerede kestiği:
  bulunamadı.
- Dinleyicinin kendi uygulamamızın bildirimini görüp görmediği
  (test bildirimi fikri): cihazda sınanacak.
- Bileşeni kapatıp açarak dinleyiciyi yeniden bağlama hilesi: üç
  projede kullanılıyor (önceki kol), resmî dokümanda yok.
- Türkiye "izin vermiyorum" kalıbının ortaya çıkış nedeni: haber (2022)
  başka bir uygulamayı anlatıyor. Bildirim uygulamalarına yayıldığı
  bizim verimizden çıkarım.
- Reddit r/smallbusiness gönderisi pazar araştırması olabilir
  (moderatör uyarısı var). Güvenilirliği düşük; destekleyici olarak
  kullanıldı.
- Yorumların bir kısmı (Samsung topluluk, SamMobile, Tom's Guide gibi)
  yalnız arama özetinden biliniyor ve belgeye girmedi.
- Hâlâ bilinmeyen: şikayetlerin gerçek oranı. Play'in "tüm yorumlar"
  verisine toplu erişim yok.

## Kaynak dizini

Numaralar kategorilerde kullanılan kısaltmalardır. Yorum sayıları
çektiğim benzersiz yorumlardır (toplam Play örneği 10.124).

### P: Play yorum kümeleri (23)

Bildirim kaydedici / silinen mesaj uygulamaları (17):
- P01 Notisave - Notification Log (2.380 yorum, EN + TR)
  https://play.google.com/store/apps/details?id=com.tenqube.notisave
- P02 Notification History Log, ZipoApps (750)
  https://play.google.com/store/apps/details?id=com.ikvaesolutions.notificationhistorylog
- P03 Recent Notification, LibApps (400)
  https://play.google.com/store/apps/details?id=com.libin.notification
- P04 Notification History Log, Argon (92)
  https://play.google.com/store/apps/details?id=com.argonremote.notificationhistory
- P05 Notifications archive, Difer (78)
  https://play.google.com/store/apps/details?id=net.difer.notiarch
- P06 Notification History (Norg), Lufesu (95)
  https://play.google.com/store/apps/details?id=com.lufesu.app.notification_organizer
- P07 Notification History, svprdga (109)
  https://play.google.com/store/apps/details?id=com.svprdga.notificationhistory
- P08 NotiSaver, HexCoders (300)
  https://play.google.com/store/apps/details?id=hexcoders.notisave
- P09 Notification Log (NLog) (300)
  https://play.google.com/store/apps/details?id=org.hcilab.projects.nlog
- P10 Notification Manager - History, BinaryScript (52)
  https://play.google.com/store/apps/details?id=com.binaryscript.notificationmanager
- P11 WAMR (900, EN + TR)
  https://play.google.com/store/apps/details?id=com.drilens.wamr
- P12 BuzzKill (400)
  https://play.google.com/store/apps/details?id=com.samruston.buzzkill
- P13 Nap (203)
  https://play.google.com/store/apps/details?id=io.leao.nap
- P14 Past Notifications (225)
  https://play.google.com/store/apps/details?id=com.coconuts.pastnotifications
- P15 Unseen Messenger (300)
  https://play.google.com/store/apps/details?id=com.unseen.messenger
- P16 Unseen - No Last Seen (300)
  https://play.google.com/store/apps/details?id=com.tda.unseen
- P17 Auto RDM (300)
  https://play.google.com/store/apps/details?id=com.softinit.iquitos.mainapp

Yakın alan (6):
- P18 Beeper (300)
  https://play.google.com/store/apps/details?id=com.beeper.android
- P19 WhatsApp Business (1.240, EN + TR)
  https://play.google.com/store/apps/details?id=com.whatsapp.w4b
- P20 Meta Business Suite (700, EN + TR)
  https://play.google.com/store/apps/details?id=com.facebook.pages.app
- P21 Pushbullet (300)
  https://play.google.com/store/apps/details?id=com.pushbullet.android
- P22 SMS Organizer (300)
  https://play.google.com/store/apps/details?id=com.microsoft.android.smsorganizer
- P23 Spike (100)
  https://play.google.com/store/apps/details?id=com.pingapp.app

### R: Reddit konuları (23)

- R01 "Bildirimleri saklayıp uygulamaya göre gösteren uygulama"
  https://www.reddit.com/r/androidapps/comments/1hqx5hv/
- R02 "Bildirim günlüğü için en güvenli uygulama?"
  https://www.reddit.com/r/androidapps/comments/117kqfw/
- R03 "Bildirim günlüğü?" (güvenilir uygulama arayışı)
  https://www.reddit.com/r/androidapps/comments/pokkkt/
- R04 "Kurulumdan önceki bildirimleri gösteren günlük uygulaması?"
  https://www.reddit.com/r/androidapps/comments/mio5m9/
- R05 "Buzzkill çalışmıyor"
  https://www.reddit.com/r/androidapps/comments/1vio1et/
- R06 "Başka kimsede bu bildirim sorunu var mı?" (yanlışlıkla temizleme)
  https://www.reddit.com/r/AndroidQuestions/comments/1w08pck/
- R07 "Bildirim gelen kutusu" isteği
  https://www.reddit.com/r/androidapps/comments/1o9gsq7/
- R08 "UnNotification uygulaması?" (geri alma)
  https://www.reddit.com/r/androidapps/comments/1tg2pqb/
- R09 "Bildirimleri tepsiden kaldırıp günlüğe koyan uygulama"
  https://www.reddit.com/r/androidapps/comments/1srmlie/
- R10 "Güzel ve temiz bildirim geçmişi uygulaması?"
  https://www.reddit.com/r/androidapps/comments/1r9fpvi/
- R11 "Bildirim geçmişi + paylaş menüsü"
  https://www.reddit.com/r/androidapps/comments/1jf0ums/
- R12 "Tamamen çevrimdışı bildirim filtresi yaptım" (geliştirici tanıtımı;
  bulut sızıntısı kaygısı)
  https://www.reddit.com/r/AndroidQuestions/comments/1uq0y0a/
- R13 "Android 13 kısıtlanmış ayar bildirim dinleyiciyi de kilitliyor"
  https://www.reddit.com/r/Android/comments/vsaagp/
- R14 "Play'den indirdiğim uygulama için kısıtlanmış ayara izin veremiyorum"
  https://www.reddit.com/r/androidapps/comments/1snb3x4/
- R15 "Bazı uygulamalarda bildirim erişimi sorunu"
  https://www.reddit.com/r/androidapps/comments/1gv4e9k/
- R16 "Uygulama izni sorusu" (Android 14, gri anahtar)
  https://www.reddit.com/r/AndroidQuestions/comments/1bfb05w/
- R17 "Bildirim erişimi" (güvenli mi, Android 12 uyarısı)
  https://www.reddit.com/r/AndroidQuestions/comments/1bretat/
- R18 "Bu uygulamaya bildirim erişimi vermek güvenli mi?"
  https://www.reddit.com/r/AndroidQuestions/comments/1g6vmh1/
- R19 "Uygulama açık olmayınca bildirimler kayboluyor" (Android 13)
  https://www.reddit.com/r/AndroidQuestions/comments/1364rsk/
- R20 "Aylardır süren eksik bildirimler" (Android 11)
  https://www.reddit.com/r/AndroidQuestions/comments/uice5r/
- R21 "Telefon bildirimlerimi kaydeden uygulama var mı?"
  https://www.reddit.com/r/androidapps/comments/16mh0ni/
- R22 "Silinen WhatsApp mesajlarını kurtarma (WAMR güvenli mi?)"
  https://www.reddit.com/r/androidapps/comments/1uzkuw4/
- R23 r/smallbusiness: ekipte tek WhatsApp Business numarası (zayıf)
  https://www.reddit.com/r/smallbusiness/comments/1xb1dso/

### Q: Stack Overflow soruları (11)

- Q01 Dinleyici durdu, yeniden başlatmadan başlamıyor (2013)
  https://stackoverflow.com/questions/20198283/notificationlistenerservice-stopping-and-cant-be-restarted-without-a-reboot
- Q02 Huawei ve Xiaomi pil optimizasyonları (2017)
  https://stackoverflow.com/questions/47145722/how-to-deal-with-huaweis-and-xiaomis-battery-optimizations
- Q03 Dinleyici iki kez tetikleniyor (WhatsApp, 2015)
  https://stackoverflow.com/questions/33412124/android-notificationlistenerservice-onnotificationposted-fire-twice
- Q04 Gmail ve WhatsApp için iki kez (2017)
  https://stackoverflow.com/questions/45890487/android-onnotificationposted-is-called-twice-for-gmail-and-whatsapp
- Q05 Yeniden başlatınca bildirim erişimi kayboluyor (Samsung, 2015)
  https://stackoverflow.com/questions/29824122/losing-of-notification-access-permission-after-reboot
- Q06 Bir süre sonra durup bir daha başlamıyor (2016)
  https://stackoverflow.com/questions/38431093/notificationlistenerservice-stops-after-some-time-and-never-restart-again
- Q07 Uygulama çökünce dinleyici çalışmıyor (2017)
  https://stackoverflow.com/questions/45124673/notification-listener-service-does-not-work-after-app-is-crashed
- Q08 Doze ve Huawei Korunan Uygulamalar (2016)
  https://stackoverflow.com/questions/36900480/notificationlistenerservice-and-doze-mode-and-app-standby
- Q09 Dinleyicide aşırı pil tüketimi (2019)
  https://stackoverflow.com/questions/58810404/how-to-fix-extreme-battery-drain-with-notificationlistener-service
- Q10 Bildirimden resim alma (2019)
  https://stackoverflow.com/questions/56861692/how-to-get-image-from-notification
- Q11 WhatsApp grup mu bire bir mi? (2020)
  https://stackoverflow.com/questions/63913290/how-to-identify-if-whatsapp-notification-is-coming-from-a-group-or-an-individual

### G: GitHub / GitLab / forum (9)

- G01 NotiFilter #124 (HyperOS 3, otomatik başlatma)
  https://github.com/BURG3R5/NotiFilter/issues/124
- G02 NotiFilter #93 (dinleyici başlamıyor, kapatıldı)
  https://github.com/BURG3R5/NotiFilter/issues/93
- G03 DoNotNotify #8 (Xiaomi 14 Pro)
  https://github.com/anujja/DoNotNotify/issues/8
- G04 notification-vault #28 (Tümünü sil çalışmıyor)
  https://github.com/snap24/notification-vault/issues/28
- G05 notification-vault #26 (iş profili görsel)
  https://github.com/snap24/notification-vault/issues/26
- G06 WhatsDeleted #11 (tekrarlı kayıt)
  https://github.com/4nubhav/WhatsDeleted/issues/11
- G07 voicenotify #174 (iş profili)
  https://github.com/pilot51/voicenotify/issues/174
- G08 F-Droid istemcisi #2680 (GitLab, kilitli bildirim erişimi)
  https://gitlab.com/fdroid/fdroidclient/-/issues/2680
- G09 KDE Connect forumu: 1.35.x bildirim aktarımı bozuldu
  https://discuss.kde.org/t/notifications-dont-sync-from-phone-to-desktop-on-1-35-x/44525

### D: Doküman, haber, blog, Hacker News (17)

- D01 Android 15 davranış değişiklikleri (OTP gizleme)
  https://developer.android.com/about/versions/15/behavior-changes-all
- D02 AOSP bildirim geçmişi belgesi
  https://source.android.com/docs/core/display/notification-history
- D03 dontkillmyapp Samsung
  https://dontkillmyapp.com/samsung
- D04 dontkillmyapp Xiaomi
  https://dontkillmyapp.com/xiaomi
- D05 dontkillmyapp Huawei
  https://dontkillmyapp.com/huawei
- D06 Gadgetbridge arka plan servisi sayfası
  https://gadgetbridge.org/basics/topics/background-service/
- D07 Niagara Launcher yardım: kısıtlanmış ayarlar
  https://help.niagaralauncher.app/article/134-fix-restricted-settings
- D08 Chrome Unboxed: Android 13 kısıtlı ayar
  https://chromeunboxed.com/android-13-restricted-setting-feature-blocks-sideloaded-apps-from-notifications/
- D09 Bayton: Android 13 kısıtlı izinler
  https://bayton.org/android/android-13-restricted-permissions/
- D10 Esper: Android 13 kısıtlaması
  https://www.esper.io/blog/android-13-sideloading-restriction-harder-malware-abuse-accessibility-apis
- D11 9to5Google: Pixel bildirim geçmişi düzeltmesi (2024)
  https://9to5google.com/2024/03/15/google-pixel-notification-history-fix/
- D12 Appuals: bildirim geçmişi boş / görünmüyor
  https://appuals.com/android-notification-history-not-showing/
- D13 Log: Play yorumlarında "izin vermiyorum" olayı (Mart 2022)
  https://www.log.com.tr/?p=747927
- D14 Hacker News: yerleşik geçmişin sınırları (2026)
  https://news.ycombinator.com/item?id=47720855
- D15 Hacker News: yerleşik geçmiş varsayılan kapalı (2026)
  https://news.ycombinator.com/item?id=47707221
- D16 Hacker News: dinleyici kötüye kullanımı, OTP (2026)
  https://news.ycombinator.com/item?id=46932725
- D17 Hacker News: "bildirim dinleyici ürkütücü" (2016)
  https://news.ycombinator.com/item?id=11701987

## Temizlik ve lisans notu

- İndirilen ekran görüntüsü, tasarım, APK, depo yok. Silinecek bir şey
  oluşmadı.
- Geçici dosyalar (yorum verisi, betikler) yalnız
  /tmp/claude-0/-home-user-Jn/422ffb5b-fa2e-516a-a2cc-64c9eb14ac8a/
  scratchpad/arastirma/sikayetler altında. Yorum verisindeki kullanıcı
  adlarını işlemin sonunda sildim; belgede kullanıcı adı yok.
- Kod kopyalanmadı. Stack Overflow içeriği CC BY-SA; yalnız soru
  başlığı + link + kendi özetim. GitHub issue içeriği özetlendi, kopya
  yok. Play ve Reddit yorumlarından kısa alıntı + link.
- Okuduğum projeler: NotiFilter, DoNotNotify, notification-vault,
  WhatsDeleted, voicenotify: yalnız issue başlığı ve gövdesi okundu;
  kodlarına bu kolda bakılmadı, lisans sorusu doğmadı (lisanslar için 02
  belgesine bak).
- Repodaki başka hiçbir dosya değiştirilmedi; commit / push yapılmadı.
