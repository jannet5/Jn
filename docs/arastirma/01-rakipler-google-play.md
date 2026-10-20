# Google Play rakipleri: bildirim toplayıcılar ve gelen kutuları

Araştırma kolu: play-rakipler

Tarih: 20 Ekim 2026 (tüm sayılar bu tarihte Play'den okundu)

## Kısa özet

- 46 Play uygulaması incelendi. Kartı olanlar aşağıda.
- Özellik sayımı için 20 bildirim uygulaması seçildi.
- Pazarın gerçek talebi: "silinen mesajı gör" ve "görüldü atmadan oku".
  - WAMR 100M+, Notisave 10M+, Auto RDM 10M+ kurulum.
- En çok şikayet edilen 3 şey:
  - Yakalama kaçırma veya bir süre sonra durma.
  - Reklam (12/20 uygulamada var).
  - Fiyat: Notisave ve Notification History Log'da haftalık abonelik.
- İşletme sahibine (berber, kuaför, mağaza) yazılmış bildirim uygulaması
  bulunamadı. Hepsi "geçmiş" ve "silinen mesaj" diyor.
- En yakın konsept: NotiWeave (kişi bazlı sohbet kutuları). Çok yeni (100+).
- Aynı ada sahip bir uygulama Play'de zaten var:
  "TekPanel: Sipariş Yönetimi" (YYVZ LLC). Ad çakışması riski, aşağıda.
- Reddit'te kullanıcılar tek seferlik veya ucuz ödeme istiyor.
- iOS'ta karşılığı yok. Apple başka uygulamanın bildirimini okumaya izin vermiyor.
- APK mirror sitelerinden APK indirilmedi. Gerekçe aşağıda.

## Nasıl araştırdım (yöntem ve güvenilirlik)

- Play sayfaları: anonim curl ile
  https://play.google.com/store/apps/details?id=PAKET
  - Puan, puan sayısı, fiyat, "reklam içerir", "uygulama içi satın alma",
    açıklama metni sayfadan okundu.
  - Veri güvenliği: https://play.google.com/store/apps/datasafety?id=PAKET
  - Play web sayfasında artık izin listesi yok. İzin bilgisini Veri güvenliği
    sayfasından ve geliştiricinin açıklama metninden aldım.
- Türkiye mağazası (hl=tr, gl=TR) 8 uygulama için ayrıca okundu (TL fiyatlar).
- Yorumlar: Play'in web arayüzünün kullandığı yorum ucu ile çekildi.
  - 48 uygulama için "en alakalı" 100 + "en yeni" 100 yorum.
  - Notisave için 1.559 benzersiz yorum.
  - Örnek rastgele değil. Sayılar yön gösterir, kesin oran değildir.
  - Tema sayıları anahtar kelime taramasıyla çıkarıldı (kaba).
- Ekran görüntüleri: Play'deki mağaza görselleri indirildi, notlar alındı,
  ardından SİLİNDİ (kurucunun isteği).
  - Mağaza görselleri gerçek ekranı birebir göstermeyebilir.
- "Gerçek kurulum" sayıları: Play sayfasının içindeki sayı alanı.
  - Play bunu ekranda göstermiyor, sadece "10M+" gibi aralık gösteriyor.
  - Alanın anlamı resmen doğrulanamadı. Sadece büyüklük sıralaması için kullandım.
- Play dışı: F-Droid, XDA, Product Hunt, AlternativeTo (sadece arama özeti),
  Reddit (arşiv API'si), Apple Developer Forums, Microsoft, KDE, Beeper.
- Kod kopyalanmadı, hiçbir uygulama çalıştırılmadı. Amaç sistemi anlamak.
- Lisans notları ilgili kartlarda (GPL, MIT, Apache).

### APK mirror sitelerinden APK indirmedim, neden

- Kurucunun kuralı: mirror sitelerinden APK indirme ve tersine mühendislik yok.
- Telif ve kullanım şartı: bu siteler geliştiricinin izni olmadan APK dağıtıyor.
  Uygulamaların lisans sözleşmeleri genelde tersine mühendisliği yasaklar.
  Notisave'in sözleşme metnini okuyamadım (doğrulanamadı).
- Zararlı yazılım riski gerçek: APKPure'un resmi istemcisi Nisan 2021'de
  Triada truva atı ile bulaşmıştı.
  - https://therecord.media/official-client-for-the-apkpure-android-app-store-compromised-with-malware/
  - https://www.bleepingcomputer.com/news/security/android-malware-found-embedded-in-apkpure-store-application/
- Gerek de yoktu: Play metni, ekran görüntüleri, yorumlar ve Veri güvenliği
  sayfası yeterli bilgi verdi.
- Bu yüzden "Play'den APK çekip kod analizi" bu kolda yapılmadı. İstenirse
  açık kaynak rakipler (F-Droid, GitHub) üzerinden yapılır; o başka kolun işi.

## Rakip haritası (kısa liste)

- Grup A, bildirim geçmişi ve günlük (13):
  Notisave, Notification History Log (ZipoApps), Recent Notification,
  Notification History Log (Arcangelo), Notifications archive, Norg (Lufesu),
  Notification History (svprdga), NotiSaver (HexCoders), NLog,
  Notification Manager - History (BinaryScript), Past Notifications,
  Notification History Pro, Notistar (MakLab).
- Grup B, yönetici ve filtre (11):
  BuzzKill, FilterBox, Nap, DoNotNotify, NManager, Glimpse, TidyPanel,
  NotiAlarm, nBubble, Notification Widget, Notifiltr.
- Grup C, silinen mesaj (3): WAMR, Auto RDM, Unseen Messenger.
- Grup D, birleşik gelen kutusu ve işletme mesajı (13):
  Beeper, Meta Business Suite, WhatsApp Business, Shopify Inbox, respond.io,
  WATI, Interakt, Callbell, Superchat, JivoChat, All Messages (360 Tool),
  Spike, SMS Organizer.
- Grup E, telefon-PC (4): Bridge, Pushbullet, KDE Connect, Link to Windows.
- Grup F, yeni ve küçük, izlenecekler (6):
  NotiWeave, NotiRecall, Notification Vault (F-Droid), AI Notifications
  (Summaries), TekPanel: Sipariş Yönetimi, Bookio.
- Bulunamadı: "Flowbell" (Play'de ve webde aynı amaçlı bir uygulama çıkmadı;
  arama sonuçları alakasız). "Notification Hub" adıyla büyük bir uygulama yok;
  NotiHub adlı küçükler var (50+ ve 1K+ kurulum).
- "NotiStar" iki ayrı şey:
  - Samsung Good Lock modülü (Galaxy Store, Play'de değil).
  - Play'de MakLab Apps'in "Notistar - Notification History" uygulaması.
- "Unified Inbox" benzerleri: Chatter | Unified Inbox (1K+), iCloud-Ready Unified
  Inbox (500+), respond.io, Spike. Hepsi e-posta veya bulut tabanlı.
- Eskiden APUS Message Center vardı (bildirimleri tek yerde toplayan, hızlı
  cevaplı). Play'de artık bulunamadı (ABD mağazasında 404). Son sürüm 3.4.5,
  2019-2020.
  - https://mobile.softpedia.com/apk/apus-message-center-intelligent-management/
  - https://cafebazaar.ir/app/com.apusapps.tools.unreadtips?l=en

## Nasıl okunur

- Her kartta Play linki ilk satırda. Veri güvenliği sayfası aynı paketle:
  https://play.google.com/store/apps/datasafety?id=PAKET
- "Yorumlar" satırları o uygulamanın Play yorumlarından, tarihleriyle.
- Fiyat, USD mağazasından (ABD). Türkiye mağazasında farklı olabilir.

# Grup A: bildirim geçmişi ve günlük

## A1) Notisave (Firehawk)

- Link: https://play.google.com/store/apps/details?id=com.tenqube.notisave
- Paket: com.tenqube.notisave
- Konum: "bildirim günlüğü, görüldü atmadan oku, silinen mesajı gör".
- Puan: 4,51 (104.915 puan). İndirme: 10M+ (sayfa verisi ~43,8 milyon).
- Fiyat: ücretsiz + reklam + abonelik. Ayrıntı: "NOTISAVE DETAYI" bölümü.
- Veri güvenliği: cihaz kimliğini reklam ve analitik için paylaşıyor,
  "veri toplamıyor" diyor, silme isteği yolu yok.
- Arayüz: aşağıdaki özel bölümde.

## A2) Notification History Log (ZipoApps)

- Link: https://play.google.com/store/apps/details?id=com.ikvaesolutions.notificationhistorylog
- Paket: com.ikvaesolutions.notificationhistorylog
- Konum: "eski bildirimleri gör, yanlışlıkla silineni geri getir".
  - Product Hunt'ta aynı adla bir ürün listeli (aynı uygulama olduğu
    doğrulanamadı): https://www.producthunt.com/products/notification-history-log
- Özellikler (Play metni):
  - "Advanced History" ekranı: uygulamaya göre gruplu zaman çizelgesi.
  - Arama, favori, geçmiş filtreleri, temizle.
  - Metin veya Excel'e dışa aktarma, ana ekran widget'ı, kara liste,
    yedekle-geri yükle (şifreli, telefonda), açık/koyu tema.
  - Bu satırlar metinde yıldızlı (*). Ücretli olduğu doğrulanamadı; ancak
    yorumlar "çoğu özellik ücretli, tek tek silme bile" diyor.
  - Android'in kendi bildirim geçmişini açan kısayol var.
- Fiyat: reklam + abonelik. Yorumlarda haftalık fiyat:
  - 2022-11: "haftada 14,99 dolar" (1 yıldız, 102 kişi faydalı buldu).
  - 2024-08: "haftada 13 dolar". 2025-05: "haftada 14,99 dolar".
- Puan: 3,98 (40.360). İndirme: 5M+ (sayfa verisi ~6,8 milyon).
- Veri güvenliği: cihaz kimliği, çökme ve tanılama verisi paylaşılıyor.
- Arayüz (ekran görüntüleri):
  - Üst çubukta arama. Filtre paneli: "Yeni önce / Eski önce / A-Z" ve
    "Bugün / Dün / Bu hafta" kapsülleri.
  - Satır: uygulama simgesi + başlık + metin + saat.
  - Ayarlar: medya dosyaları (görsel, video, ses, GIF, belge) tek tek aç-kapa.
  - Engellenen uygulamalar listesinde "TÜMÜNÜ SEÇ / SEÇİMİ KALDIR" düğmeleri var.
- Yorumlar:
  - Açılışta, ekran geçişinde ve her ekranda reklam (2021-2024).
  - "Yeni bildirim saklanamıyor" uyarısı sürekli çıkıyor (2022-10).
  - Android'in yerleşik geçmişi aynı işi yapıyor diyen var (2022-10).
  - "Geçmiş değil günlük tutucu, kurmadan önceki bildirimi bulamazsın; adı
    yanıltıcı" diyen var (2021-04).
  - Beğenen: "bildirimleri akıllıca kaydediyor, uzun kara liste gerekmiyor"
    (Pro kullanıcı, 2019).

## A3) Recent Notification (LibApps)

- Link: https://play.google.com/store/apps/details?id=com.libin.notification
- Paket: com.libin.notification
- Konum: "kaçırdığın bildirimleri sakla". Sade, 2018'den beri.
- Özellikler (Play metni):
  - Uygulamaya göre gruplu. Durum çubuğundaki gibi tam ayrıntı (büyük metin,
    gelen kutusu stili). Güne göre filtre.
  - Gruba veya tek tek kaydırarak sil. Eski bildirimi N gün sonra otomatik sil.
  - Dokununca ilgili uygulamayı açar.
  - İnternet erişimi gerekmiyor, sistem uygulamalarının bildirimini okumuyor.
- Fiyat: reklamsız, uygulama içi satın alma 1,99-2,49 dolar (Pro).
  - Pro: uygulama başına 1000 bildirim (yorum, 2021-03).
- Puan: 3,92 (141.477). İndirme: 10M+. Son güncelleme: Temmuz 2023.
- Veri güvenliği: veri toplamıyor, paylaşmıyor.
- Arayüz: üst çubukta başlık + yıldız (favoriler) + dişli. Liste satırı: simge
  + başlık + önizleme + sağda saat; simgenin altında sayı rozeti. Sağ altta
  yuvarlak filtre düğmesi. Ayarlar: tema, vurgu rengi paleti, hariç tutulan
  uygulamalar, sistem/devam eden bildirimleri hariç tut, bildirim limiti,
  gün limiti.
- Yorumlar:
  - Seven: telefon yeniden başlayınca kaybolan bildirimleri kurtarıyor
    (2019-11, 317 kişi faydalı buldu). Ana ekran widget'ı güzel (2020-04).
  - Sevmeyen: ücretsiz sürümde saklama süresi 3 gün ve gri, değiştirilemiyor
    (2021-08). Bildirim hiç düşmedi (2021-08). Instagram bildirimlerini
    kaçırıyor (2019-11). Dokununca rastgele gönderiye gidiyor (2026-06).

## A4) Notification History Log (Arcangelo Muschitiello)

- Link: https://play.google.com/store/apps/details?id=com.argonremote.notificationhistory
- Paket: com.argonremote.notificationhistory
- Özellikler: kök gerekmez, kara liste, tam önizleme, metni paylaş/kopyala,
  metinde hızlı arama, yedekle-geri yükle. Ücretsiz sürüm son 1000 bildirimi
  tutuyor.
- Fiyat: reklam + uygulama içi satın alma (0,99-99,99 dolar).
- Puan: 3,5 (1.625). İndirme: 1M+.
- Veri güvenliği: çökme günlüğü ve analitik paylaşılıyor.
- Arayüz: mavi üst çubuk (yardım, yenile, zil, menü). Satırda büyük uygulama
  ikonu + başlık + alt başlık + tarih. Altta arama kutusu ve iki yuvarlak
  düğme. Önizleme ekranında tam metin + paylaş, kopyala, aç, bağlantı simgeleri.
  - Kara liste ekranında paket adları (com....) olduğu gibi görünüyor.
    Kurucunun "paket adı görünmesin" isteğinin tam tersi.
- Yorumlar: listede özel bir şikayet öne çıkmadı; en sık sorun reklam ve
  "bildirim kaydı durdu" (Play yorumları).

## A5) Notifications archive (Difer)

- Link: https://play.google.com/store/apps/details?id=net.difer.notiarch
- Paket: net.difer.notiarch
- Konum: "bildirimleri biraz daha uzun hatırla".
- Özellikler:
  - Uygulamaya göre gruplu arşiv, sistem uygulamaları dahil.
  - Uygulama filtrele veya yok say (uzun basma), resimleri yok say.
  - Galeri ızgarası ve tam ekran görünüm (resimli bildirimler).
  - İfade ile arama. Tek dosyaya dışa aktar, içe aktar (bulut dahil).
- Fiyat: reklam + uygulama içi satın alma (1,39-10,99 dolar).
- Puan: 4,33 (1.518). İndirme: 500K+. Güncelleme: 20 Ekim 2026.
- Veri güvenliği: veri toplamıyor, paylaşmıyor.
- Arayüz: koyu tema. Alt çubukta iki sekme: liste ve galeri. Liste: uygulama
  adı + başlık + gövde + sağda ikon; resimli bildirimde resim satırın içinde.
  Ayarlar: "devam eden bildirimleri kaydet", "sistem bildirimleri", durum
  çubuğundan otomatik kaldır, görsel kaydet / yok say.
- Yorumlar: "mesajlar doğrudan ilgili uygulamada açılmalı" ve "Gmail,
  WhatsApp gibi gruplanmalı" önerisi (2018-12, 4 yıldız). Bazı cihazlarda
  1 günden fazla saklamıyor / çöküyor (2019-2020, kısa 1 yıldızlar).

## A6) Notification History, kısa adı Norg (Lufesu Inc.)

- Link: https://play.google.com/store/apps/details?id=com.lufesu.app.notification_organizer
- Paket: com.lufesu.app.notification_organizer
- Özellikler: otomatik kayıt, arama, uygulama filtresi, görüldü atmadan önizle,
  silinen mesaj bildirimi, önemli uyarılar, ana ekran widget'ları, dışa-içe
  aktar.
- Fiyat: ücretsiz sürümde SON 3 GÜN görünür. Premium: eski kayıtlar,
  reklamsız, sabitleme, toplu okundu, tam widget.
  - Uygulama içi satın alma 1,99-4,99 dolar (türü, tek seferlik mi abonelik mi,
    doğrulanamadı).
- Puan: 4,09 (1.566). İndirme: 100K+. Güncelleme: 13 Ekim 2026.
- Veri güvenliği: paylaşım yok; bazı uygulama etkinliği verisi toplanıyor.
- Arayüz (ekran görüntüleri):
  - Üstte "10 bildirim" + arama + arşiv + dişli. Altında iki sekme:
    "En yeni" ve "Uygulamaya göre".
  - Satır: simge + başlık + metin + tarih, sağda aşağı ok (genişlet).
  - Dokununca alttan panel açılır: gönderen, tarih, tam metin, 5 düğme:
    Sabitle, Favori, Önemli, Engelle, Anahtar kelime. En altta Kapat / Aç.
  - Tek ekrana yakın: ayarlar dişliden, yeni sayfa az.
- Yorumlar: "kayan simge olsun, kayıt çalıştığını anlayayım" (2020-12).
  Beğenen: "tam ihtiyacım olan yönetim", sonra satın aldım (2021-2022).
  Reklam yüzünden "adware" diyen var (2020-11).
- Not: bu kart TekPanel'e en yakın "bottom sheet ile tam metin" örneği.

## A7) Notification History (David Serrano Canales)

- Link: https://play.google.com/store/apps/details?id=com.svprdga.notificationhistory
- Paket: com.svprdga.notificationhistory
- Özellikler: otomatik kayıt, uygulama ve tarih aralığı filtresi (birlikte
  kullanılabilir), arama çubuğu, hariç tutma listesi, tekrarları gizle,
  otomatik silme (varsayılan 1 ay). "Veri cihazdan çıkmaz" diyor.
- Fiyat: reklam + abonelik/deneme (0,99-28,99 dolar).
  - "Aylık ve yıllık abonelik kabul edilemez" (1 yıldız, 2021-03).
  - "Deneme sonrası otomatik tahsilat" şikayeti (2026-01).
- Puan: 3,72 (3.065). İndirme: 100K+.
- Veri güvenliği: çökme günlüğü, satın alma geçmişi, cihaz kimliği paylaşılıyor.
- Arayüz: mor üst çubuk (arama, filtre, dişli). Kart satırı: uygulama adı +
  tarih + kişi fotoğrafı + ad + mesaj, sağda üç nokta menü. Filtre: açılır
  pencere, "uygulamaya göre" ve "tarihe göre" anahtarları.
- Yorumlar: bildirim erişimi sessizce kapandı, geçmiş boş (2025-06). "Silinen
  mesajı tutmuyor" diyen (2021-11).

## A8) NotiSaver: Notification Log (TheHexCoders)

- Link: https://play.google.com/store/apps/details?id=hexcoders.notisave
- Paket: hexcoders.notisave
- Not: Notisave adına çok benziyor (ad taklidi riski). Play'de "Notisave" veya
  "NotiSaver" yazan en az 4 liste var: Firehawk'ınki, HexCoders,
  com.app.notisave (Uni Intel), com.noti.log.notifications.save.
  iOS'ta da "Notisave: History Log" adlı ayrı bir uygulama var.
- Özellikler: bildirim kaydı, silinen mesaj, durum (status) kaydedici,
  uygulama grupları (Sohbet, Sosyal, Favori), okunmamış sekmesi.
- Fiyat: reklam + 3,99 dolar tek ürün. Puan: 3,09 (2.693). İndirme: 100K+.
- Veri güvenliği: cihaz kimliği reklam için paylaşılıyor.
- Arayüz (mağaza görseli): mor tema; "Groups" ekranında "Add group" kutusu;
  kişi listesi (avatar + ad + son mesaj); kişiye girince sohbet balonu
  görünümü; kopyala / sil / paylaş menüsü; uygulama seçerken onay kutuları.
- Yorumlar: "uygulama grupları güzel, hepsini uygulamaya göre ayırıyor, ama
  zaman damgası yok" (2020-05, 133 faydalı). "Mesaja dokununca ana ekrana
  atıyor, tam göremiyorum" (2020-07). Reklamsız satın aldım, reklam gitmedi
  (2021-05).

## A9) Notification Log, kısa adı NLog (weberdo)

- Link: https://play.google.com/store/apps/details?id=org.hcilab.projects.nlog
- Paket: org.hcilab.projects.nlog
- Konum: Stuttgart Üniversitesi insan-bilgisayar etkileşimi grubunun araştırma
  projesi. Kaydeder, listeler, JSON olarak dışa aktarır.
- Fiyat: ücretsiz, reklamsız (uygulama içi ürünler 1,99-5,49 dolar; ne olduğu doğrulanamadı).
- Puan: 4,21 (3.386). İndirme: 500K+. Son güncelleme: Eylül 2019 (terk edilmiş).
- Veri güvenliği: veri toplamıyor, paylaşmıyor.
- Arayüz: en sade örnek. Liste: simge + başlık + metin. Ayrıntıda ham
  JSON (paket adı, zaman, bayraklar) + "Uygulamayı aç" + "Bildirim ayarları".
  Ayarlarda: metni kaydet, devam edenleri kaydet, günlüğü dışa aktar/sil.
- Yorumlar: "WhatsApp'ı kaydetmiyor, hangi uygulamaların kaydedileceği
  seçilemiyor, arama yok, koyu tema yok" (2025-07). 9 gün kayıt yok (2020-08).
  Bildirime dokununca ilgili mesaja gitmiyor (2021-03).
- Lisans: doğrulanamadı (F-Droid'de yok: sayfa 404).

## A10) Notification Manager - History (BinaryScript)

- Link: https://play.google.com/store/apps/details?id=com.binaryscript.notificationmanager
- Paket: com.binaryscript.notificationmanager
- Özellikler: her bildirim anında kaydedilir, tam metin, yıldızla, arama
  (tüm geçmiş veya tek uygulama), uygulama yönetimi (aktif / sessiz / engelli),
  parmak izi ve yüz ile kilit, CSV dışa aktarma (abonelik), Material You, 31 dil.
- Fiyat (Play metni): kayıt, geçmiş, arama, uygulama kontrolü ve kilit ÜCRETSİZ.
  Abonelik yalnızca CSV dışa aktarmayı ekliyor: aylık, yıllık, ömür boyu.
  - Ürün aralığı 4,99-39,99 dolar. Play "reklam içerir" etiketi var ama metin
    "reklam yok" diyor (tutarsız, doğrulanamadı).
  - Yorumlar ise "sert paywall, abonelik ekranını geçemiyorum" diyor
    (2026-02, 2026-04, 2026-08). Mağaza metniyle ürün davranışı farklı olabilir.
- Puan: 3,88 (529). İndirme: 50K+. Güncelleme: 19 Ekim 2026.
- Veri güvenliği: paylaşım yok; yaklaşık konum gibi bazı veriler toplanıyor.
- Arayüz (ekran görüntüleri): koyu tema, alt çubukta 3 sekme: Ana, Uygulamalar,
  Ayarlar.
  - Ana: arama kutusu + yıldız; satırda uygulama adı + süre (7dk, 14dk) +
    başlık + metin + sağda yıldız.
  - Uygulamalar: üstte sayaçlar (Aktif 7, Sessiz 2, Engelli 1) + süzgeç
    kapsülleri; satırda simge + bildirim sayısı + dişli.
  - Dışa aktarma: uygulama seç, tarih aralığı, "yalnız yıldızlılar", önizleme
    ("1565 bildirim"), mavi "CSV'ye aktar" düğmesi.
  - Uygulama detayı: o uygulamanın tüm geçmişi + arama + dışa aktarma ikonu.
- Yorumlar: yeniden başlatınca bildirim izni bazen kapanıyor (2026-09).
  "Kilitli abonelik ekranı" ve "ömür boyu çok pahalı" (2026-03, 2026-04).

## A11) Past Notifications (Coconuts Develop)

- Link: https://play.google.com/store/apps/details?id=com.coconuts.pastnotifications
- Paket: com.coconuts.pastnotifications
- Ayrı reklamsız sürüm (tek seferlik 2,99 dolar):
  https://play.google.com/store/apps/details?id=com.coconuts.pastnotifications.adfree
- Özellikler: kronolojik geçmiş, uygulamaya göre grup, süzme ve arama, uzun
  bildirimin tam metni, durum çubuğu ikonundan geçmişe erişim, yok sayma
  listesi (uygulama ve kelime), yedek, "Read" atmadan okuma.
- Puan: 3,78 (1.513). İndirme: 500K+. Reklamlı. Güncelleme: Temmuz 2025.
- Veri güvenliği: paylaşım yok; çökme verisi toplanıyor.
- Arayüz: turuncu tema. Üç sekme: GEÇMİŞ, UYGULAMALARA GÖRE, YOK SAYILAN. Üstte
  "Filtre..." kutusu + takvim. Uygulamaya göre sekmesi: uygulama adı + "Count:
  203" sayacı + üç nokta menü.
- Reddit'te önerildi: "Past Notifications, reklamsız için tek seferlik anahtar
  var" (r/androidapps, 2025-01).
  https://www.reddit.com/r/androidapps/comments/1hqx5hv/app_that_stores_notification_history_and_can_view/

## A12) Notification History Pro (Yotta Studio)

- Link: https://play.google.com/store/apps/details?id=com.evanhe.nh
- Paket: com.evanhe.nh
- Özellikler: bildirim, toast, USSD, flash SMS ve dialog kaydı; uygulama kur /
  güncelle / sil geçmişi; uygulamaya göre grup; süz; ara; panoya kopyala;
  bildirimden uygulamayı aç; masaüstü widget'ı; ücretsiz sürümden içe aktar.
  - Erişilebilirlik servisi kullanıyor (USSD ve dialog için).
- Fiyat: ücretli indirme 1,99 dolar, reklamsız. Puan: 4,39 (548). İndirme: 10K+.
- Veri güvenliği: veri toplamıyor, paylaşmıyor.
- Arayüz: eski stil, koyu. Ana liste: uygulama başına satır (ad + "Count:
  641" + "Last: tarih"). Alt çubuk: yıldız, TEMİZLE, dişli.
  Ayrıntı: onay kutulu liste, altta kaydet / ara / temizle / çöp.
- Yorumlar: "bildirim çubuğunda kalıcı bildirim rahatsız ediyor" (2018),
  "otomatik temizleme hâlâ çalışmıyor" (2021). Lisans kontrolü hatası (2018).

## A13) Notistar - Notification History (MakLab Apps) ve Samsung NotiStar

- Link: https://play.google.com/store/apps/details?id=com.notistar.app.notistar
- Paket: com.notistar.app.notistar
- Konum: "silinen WhatsApp mesajlarını gör". Reklamlı, 0,49-6,49 dolar ürünler.
- Puan: 2,25 (55 puan). İndirme: 10K+. Yorumlar: reklam örtüyor, bildirim hiç
  düşmedi (S23 Ultra, 12 saat), (2025-09, 2025-10, 2026-04).
- Reddit'te geliştirici "6 ay ücretsiz premium, ön kayıt" ile duyurmuş
  (gönderi silinmiş): https://www.reddit.com/r/androidapps/comments/1mzrve3/app_release_notistar_a_notification_manager_app/
- Samsung'un Good Lock modülü NotiStar ayrı bir ürün:
  - Reddit'te "Samsung'da Good Lock'tan NotiStar al" diye önerildi
    (r/androidapps, 2025-01).
  - Özet: bırakılan bildirimleri arşivler, hangi uygulamaların kaydedileceği
    seçilir, kategori ve saklama süresi ayarlanır (üçüncü taraf rehberler).
    https://www.whistleout.com/CellPhones/Guides/samsung-good-lock-notistar
  - Samsung topluluğunda "NotiStar özelliği kaldırıldı" başlığı var; güncel
    durum doğrulanamadı.
    https://r2.community.samsung.com/t5/Galaxy-S/Removed-NotiStar-Feature/m-p/21884783
  - Good Lock Play sayfası (10M+): https://play.google.com/store/apps/details?id=com.samsung.android.goodlock

# Grup B: bildirim yöneticisi ve filtre

## B1) BuzzKill (Sam Ruston)

- Link: https://play.google.com/store/apps/details?id=com.samruston.buzzkill
- Paket: com.samruston.buzzkill
- Konum: "bildirim süper güçleri". Kural motoru. Kategorinin en yüksek puanlısı.
- Özellikler (Play metni, 25+ özellik):
  - Cooldown (art arda mesajda tek ses), özel ses/titreşim (kişiye veya
    kelimeye), otomatik kapat, otomatik cevap (görmediysen), "görene kadar
    hatırlat", geri al (yanlış kaydırmaya ikinci şans), snooze/toplu teslim,
    alarm, gizli (içeriği sakla), bildirim geçmişi (gözden geçir/geri yükle).
- Fiyat: ücretli indirme 3,99 dolar (TL mağazada 66,99 TL). Reklam yok.
  - Tek seferlik ödeme; yorumlar bunu açıkça seviyor: "bir kahveden vazgeç,
    reklamsız iyi uygulamaya öde" (2024-09, 153 faydalı).
- Puan: 4,70 (3.004). İndirme: 100K+. Güncelleme: 17 Ekim 2026.
- Veri güvenliği: veri toplamıyor, paylaşmıyor. "İnternet erişimi yok"
  diyor (kendi metni).
  - Üçüncü taraf özet: yüksek riskli izin listesi (arka plan konum,
    WRITE_SECURE_SETTINGS). Tek kaynak, doğrulanamadı:
    https://chrome-stats.com/d/com.samruston.buzzkill
- Basın: Play metni TIME "En İyi İcatlar 2025", Android Authority ve Lifehacker
  alıntıları veriyor. Bağımsız olarak doğrulayamadım (Lifehacker'a arama
  aracıyla erişilemedi). Geliştirici sitesi: https://buzzkillapp.com
  - Bağımsız inceleme: "çekirdek özellikler sağlam, öğrenme eğrisi biraz dik":
    https://grand-screen.com/apps/buzzkill-notification-manager/
- Arayüz (ekran görüntüleri): "Notification rules" listesi. Her kural renkli
  bir kart, cümle gibi okunuyor ("Dad'den mesaj gelince, 15 dakikada bir
  hatırlat"), sağda "Aktif" anahtarı. Eylem seçimi simgeli liste.
- Yorumlar:
  - Seven: şirket Instagram hesabında oturum açık, bildirim istemiyor; başka
    yolu yok, BuzzKill ile susturuyor (2024-09). Bu, işletme sahibi kullanımı.
  - Sevmeyen: Samsung S21 Ultra'da kurallar tetiklenmiyor (2026-10), donuyor
    (2026-05), kural sırası seçilemiyor (2025-05), toplu teslimde ilk mesaj da
    gecikiyor (2022-05), erişilebilirlik ayarından kapanmış (2023-12).
  - İade denemesi reddedilmiş (2026-09).

## B2) FilterBox (Ruoxin He)

- Link: https://play.google.com/store/apps/details?id=com.catchingnow.np
- Paket: com.catchingnow.np
- Konum: "bildirim geçmişi + kurallar + çevrimdışı yapay zekâ ile spam engeli".
- Özellikler:
  - Tüm bildirim geçmişi (yerelde şifreli), arama, geri yükleme.
  - Kurallar: kişiye özel zil sesi, sesli okuma, silinen sohbet mesajını gör,
    mesai dışı sessize al, hassas kelimeyi gizle, öncelikli uyarı (tam ekran,
    arama gibi), 5 sn "geri al" düğmesi, çok sık gelenleri sustur (Smart shh),
    SMS doğrulama kodunu kopyala düğmesi, webhook, widget, parmak izi/yüz kilidi.
- Fiyat: ücretsiz + Pro (1,99-6,99 dolar; TL mağazada 90,99-164,99 TL).
  Reddit'te "Pro yaklaşık 4 dolar, ömür boyu" denmiş (2025-01):
  https://www.reddit.com/r/androidapps/comments/1hqx5hv/app_that_stores_notification_history_and_can_view/
- Puan: 4,06 (3.326). İndirme: 100K+. Güncelleme: 2 Ekim 2026.
- Veri güvenliği: paylaşım yok; isteğe bağlı cihaz kimliği ve çökme verisi.
- Arayüz (ekran görüntüleri): 3 sekme: Intelligence, Analytics, Settings.
  Geçmiş zaman bloklarıyla (örn. "11 Ağustos 14:00-16:00") ve bildirim
  kartlarında "AI Score %99,9" etiketi. Analiz: halka grafik ("2338 bildirim,
  858 süzüldü") + uygulama bazında sayılar. Kural ekranı: eylem kartları
  (Dismiss, Mute, Snooze, Change sound, Replace, Read out) + koşul olarak
  "Uygulamalar içeriyor" ve "Anahtar kelime içeriyor".
- XDA tanıtım başlığı (kapalı): https://xdaforums.com/t/app-filterbox-powerful-notification-manager.4068543/
- Yorumlar:
  - Seven: gereksiz bildirimi ayıklamak "gece gündüz fark" (2023-01).
  - Sevmeyen: Android güncellemesinden sonra arka planda durup izin
    yeniden açtırıyor (2021-09), saat aralıklı kelime filtresi hatalı (2022-05),
    "denemek için 6 dolarlık satın almaya zorlandım, iade yok" (2024-12),
    kural ekranı ilk bakışta bunaltıcı (2021-02), widget gecikmeli (2021-10).
  - "Açılır açılmaz internete bağlanıyor, telemetriyi kapatamadan" (2021-03).
    "Çevrimdışı" iddiasıyla çelişiyor; doğrulanamadı.
  - Reddit'te "uygulamaya göre sıralayamıyor, günlük gruplama var" (2025-01).

## B3) Nap: notification manager (Leao)

- Link: https://play.google.com/store/apps/details?id=io.leao.nap
- Paket: io.leao.nap
- Konum: "bildirimi şimdi kapat, sonra bak". Sakin, reklamsız.
- Özellikler: tüm uygulamalar için geçmiş, "nap" zamanlaması (seçili
  uygulamaların bildirimlerini otomatik kapatır, sonunda özet bildirimi verir,
  Rahatsız Etme modunu ayarlar), snooze, yıldızla ("Saved" akışı), uygulamaya
  göre akışlar, içerikte arama.
- Fiyat: ücretsiz, reklamsız, 0,99 dolarlık uygulama içi ürün.
- Puan: 4,26 (466). İndirme: 10K+. Güncelleme: Ekim 2025.
- Veri güvenliği: veri toplamıyor, paylaşmıyor. Satın alma ve yedek için Google
  Play Hizmetleri kullandığını söylüyor.
  - "İnternet izni yok" diyor ama bir kullanıcı izin istediğini söylemiş ve
    güvenlik duvarıyla engellemiş (2025-01). Çelişki, doğrulanamadı.
- Arayüz (ekran görüntüleri): kırmızı üst çubuk, "Tümünde ara" kutusu. Tarih
  başlıkları altında satırlar: uygulama adı (renkli) + "1sa 20dk önce" +
  başlık + metin. Kaydırınca: sağa snooze (mavi), sola yıldız (sarı). Snooze
  penceresi: "Şu kadar süre / şu zamana kadar" + "Bildirimi kapat" onay kutusu.
  Alt gezinme: Saved, Inbox.
- Yorumlar: "bir hafta çalışıp durdu" (2024-12). OnePlus'ta uygulama bazlı
  yerine tüm telefonu sessize alıyor (2024-09). "Hepsini temizle yok, her
  kapatmada onay penceresi" (2022-06). Seven: "hiçbir mesajı kaybetmiyorum,
  ücretsiz" (2024-08).
- Reddit'te geliştirici: "önceki bildirimleri göremezsin" demiş (2020).
  https://www.reddit.com/r/androidapps/comments/i9mirz/app_for_seeing_notification_history/

## B4) DoNotNotify (Anuj J.)

- Link: https://play.google.com/store/apps/details?id=com.donotnotify.donotnotify
- Paket: com.donotnotify.donotnotify
- Açık kaynak: lisans MIT. https://f-droid.org/en/packages/com.donotnotify.donotnotify/
  Kod: https://github.com/anujja/DoNotNotify
- Özellikler: engelleme veya izin listesi kuralları, metinde "içerir" veya
  regex, saat aralığı, 40+ hazır kural, uygulama kurulunca otomatik kural,
  bildirim geçmişi (saklama süresi ayarlı), engellenenler geçmişi, kuralları
  JSON olarak al-ver. Ağ izni yok.
- Fiyat: ücretsiz, reklamsız. İndirme: 5K+. Puan sayısı Play'de görünmüyor.
- Yorumlar (çok az): "hiçbir kural bildirimi kaldırmıyor" (2026-03).
  Seven: "root'suz yapılabilmesine inanamadım, ücretsiz kalması harika" (2026-02).

## B5) NManager (GOODEV)

- Link: https://play.google.com/store/apps/details?id=com.goodev.notification.manager
- Paket: com.goodev.notification.manager
- Özellikler: gereksiz bildirimi engelle, tek yerde topla, kara liste.
- Fiyat: reklamlı + uygulama içi ürün 2,99-11,99 dolar.
- Puan: 4,65 (2.664). İndirme: 100K+. Güncelleme: Haziran 2025.
- Veri güvenliği: çökme günlüğü ve analitik/reklam için paylaşım.
- Arayüz (ekran görüntüleri): mavi üst çubuk. İki sekme: Bildirimler ve
  Kara liste. Bildirim satırı: simge + başlık + saat. Sola kaydırınca kırmızı
  "X". Altta geniş gri "HEPSİNİ TEMİZLE" düğmesi (onaysız görünüyor).
  İlk açılışta tek ekranlık "bu servisi etkinleştir" yönlendirmesi.
- Yorumlar: "telefonun zaten yaptığından fazlasını yapmıyor, bir fark: reklam
  izletiyor" (2024-01, 57 faydalı). "Ayarları değiştirme izni istedi, güvenmedim"
  (2025-08). Seven: "tek yerde toplanan susturulmuş bildirimler" (2021-02).

## B6) Glimpse Notifications (Nullgrad Apps)

- Link: https://play.google.com/store/apps/details?id=de.nullgrad.glimpse
- Paket: de.nullgrad.glimpse
- Konum: geçmiş uygulaması değil. Bildirim gelince ekranı uyandırır (cep modu,
  hareket algılama, tekrarlayan hatırlatma, sessiz saatler).
- Neden listede: güven örneği. "Özel bir hobi projesi, ücretsiz, veri
  toplanmıyor" diyor; yorumlarda "gizli özellik, izin kötüye kullanımı yok"
  övgüsü var (2021-10, 50 faydalı).
- Puan: 4,05 (5.552). İndirme: 1M+. Reklamsız. XDA başlığı:
  https://xdaforums.com/t/app-glimpse-notifications.3090575/page-33
- Arayüz: düz ayar listesi + alt çubukta Ayarlar, Self-test, Bilgi. "Self-test"
  sekmesi, kurulumun çalıştığını doğruluyor (iyi fikir).
- Yorumlar: "kurulumu fazla karışık" (2026-03). Pil ve Always-On ekran
  çakışmaları (2021).

## B7) TidyPanel (Dharma Poudel)

- Link: https://play.google.com/store/apps/details?id=com.dharmapoudel.tidypanel
- Paket: com.dharmapoudel.tidypanel
- Konum: sistemin "arka planda çalışıyor" gibi kaldırılamayan bildirimlerini gizler.
- Fiyat: ücretsiz + Pro 1,99 dolar. Reklam yok. Puan: 3,64 (1.234). 50K+.
- Arayüz: "Aktif bildirimler" listesi ve "Gizlenen bildirimler" listesi; gizleme
  ölçütü olarak başlık, gövde veya özel metin seçilir.
- Yorumlar: Android 14'te güncelleme sonrası çökme döngüsü (2023-10).
  "Lisansı iki kez aldım, geliştirici cevap vermiyor" (2025-03).

## B8) NotiAlarm (Soosu Studio)

- Link: https://play.google.com/store/apps/details?id=com.soosu.notialarm
- Paket: com.soosu.notialarm
- Konum: "önemli bildirim gelince alarm çal". Anahtar kelime kuralları.
- Özellikler: kelime filtresi, uygulama bazlı kontrol, takvim, özel alarm
  (tam ekran / kayan), geçmiş, webhook, Wear OS, haftalık rapor.
- Fiyat: reklamlı + ürünler 9,99-13,99 dolar. Puan: 3,91 (1.691). 50K+.
- Arayüz (mağaza görselleri): kural listesi, her satırda uygulama + etiketler
  ve anahtar. Kural formu: dahil / hariç anahtar kelimeler. Rapor ekranı: günlük
  ortalama, çubuk grafik, en çok bildirim gönderen uygulamalar.
- Yorumlar: güncellemeden sonra alarm tam ekrandan kayan hâle döndü ve
  çalmadı (2025-08). "6 testte bir kez tepki verdi" (2025-01).

## B9) nBubble (Dowble)

- Link: https://play.google.com/store/apps/details?id=com.ram.chocolate.nm.stable
- Paket: com.ram.chocolate.nm.stable
- Konum: mesajlaşma bildirimlerini kayan balonda (chat head) toplar, oradan
  cevap yazdırır.
- Özellikler: balondan hızlı cevap, kilit ekranından erişim, bildirim
  geçmişi, uygulama kara listesi, yedekle-geri yükle (dosya).
- Fiyat: ücretsiz, reklamsız, 0,89 dolar ürün. Puan: 3,79 (1.079). 100K+.
- Aynı geliştiricinin ücretli kardeşi: notifyAI, 4,99 dolar, 100+ kurulum:
  https://play.google.com/store/apps/details?id=com.dowble.nbubble
- Arayüz (mağaza görselleri): panel başlığı "Notifications - Swipe to act".
  Her kart: uygulama adı + saat + mesaj + altta "Cevapla" ve "Okundu yap"
  düğmeleri (Mail'de ayrıca "Arşivle"). Cevapla'ya basınca kartın içinde
  yazma kutusu açılır ("Gönderince sohbeti açık tut" kutusu ile).
  Kartı bir yöne kaydırınca "Okundu", diğer yöne "Sil".
  - Kurucunun istediği "cevapla ve okundu düğmeleri" kalıbının bire bir örneği.
- Yorumlar: balon Android 12'de ekran dokunuşunu engelledi (2022-01).
  Bildirim erişimini sürekli kapatıp açmak gerekiyor (2020-08).
  Önemli: "okundu işareti çıkmıyor ama popup'a dokununca WhatsApp açılıp mavi
  tik atıyor; popup'tan metin kopyalanamıyor" (2021-06). Kurucunun
  "dokununca okundu sayılıyor" şikayetiyle aynı.

## B10) Notification Widget (Milan Vyšata)

- Link: https://play.google.com/store/apps/details?id=com.kuma.notificationwidget
- Paket: com.kuma.notificationwidget
- Konum: bildirimleri ana ekran widget'ında listeler; hızlı cevap ve medya
  kontrolü. Play metninde "bildirim geçmişi 3 saat geriye" yazıyor.
- Fiyat: tam sürüm 5,99 dolar tek seferlik, ÜCRETSİZ DENEME YOK
  (yorumlar: 2021-09, 2024-12 şikayet). Reklam yok. Puan: 4,13 (1.370). 100K+.
- Veri güvenliği: veri toplamıyor. "Veri şifrelenmiyor" etiketi var.
- Arayüz (ekran görüntüleri): bildirim kartları kilit ekranı gibi: uygulama
  adı + saat + başlık + metin; altında eylem düğmeleri "Okundu yap · Cevapla",
  cevapsız aramada "Geri ara · Mesaj". Sağ üstte kırmızı "X" ile kapat.
  Cevapla: küçük pencere, hazır yanıt düğmeleri (Tamam, beğen, beğenme, gülen
  yüz) + yazı alanı + gönder.
- Yorumlar: "ödeme yaptım, sürekli ücretsiz sürüme dönüyor" (2021-08).
  "Kaydedilen kayıtlar için almıştım, kayıt tutmuyor" (2024-12). Xiaomi'de
  Otomatik başlat şart (Play metni).

## B11) Notifiltr (Nalarin), kısaca

- Link: https://play.google.com/store/apps/details?id=com.nalarin.notifiltration
- Uygulama veya kelimeyle engelle + geçmiş. 3,64 (278). 1K+. 1,99-19,99 dolar.
- Yorum: "puan vermeden devam etmiyor" ve "susturmak için ömür boyu ödeme"
  (2026-04, 2026-07). Zorla puan istemek ve sert paywall tepkisi aldı.

# Grup C: silinen mesaj uygulamaları (en büyük kullanıcı kitlesi)

## C1) WAMR (drilens)

- Link: https://play.google.com/store/apps/details?id=com.drilens.wamr
- Paket: com.drilens.wamr
- Konum: "silinen mesajı gör, mavi tik atmadan oku".
- Puan: 4,36 (1.003.466 puan). İndirme: 100M+ (sayfa verisi ~106 milyon).
- Özellikler: bildirim geçmişinden yedek alır; silinince haber verir; medya
  (foto, video, ses, GIF, çıkartma) kurtarma; konuşma görünümü.
- Fiyat: reklam + 3,60 dolarlık ürün. Veri: paylaşım yok, bazı veri toplanıyor.
- Arayüz (mağaza görselleri): iki sekme "Bildirimler | Ekler" (ayrıca
  "Silinen medya"). Konuşma listesi (avatar + ad + son mesaj + saat). Silinmiş
  mesaj kırmızımsı balon + çöp simgesiyle işaretli. "Uygulama seç" alt paneli.
  Silinince bildirim: "Silinen medya mesajı, kurtardım".
- Yorumlar: "durdu, çevrimdışıyken silineni kurtaramıyor" (2020-04);
  "Hepsini seç yok, tek tek onay kutusu" (2022-06, 786 faydalı); kendi sildiğim
  mesajı da kurtarıp listeye ekliyor, onları tek tek silmek zorunda kalıyorum
  (2021-08, 988 faydalı); aynı mesajı birden çok gösteriyor (2021-01).

## C2) Auto RDM: See Deleted Messages (ZipoApps)

- Link: https://play.google.com/store/apps/details?id=com.softinit.iquitos.mainapp
- Paket: com.softinit.iquitos.mainapp
- Puan: 4,43 (168.329). İndirme: 10M+ (sayfa verisi ~25 milyon).
- Fiyat: reklam + abonelik/deneme, ürünler 0,99-149,99 dolar.
  Yorumlar: "ücretsiz denemeden sonra aboneliği iptal edecek düğme yok"
  (2021-12, 591 faydalı), "ek ödeme yöntemi istiyor" (2022-02).
- Veri güvenliği: uygulama bilgisi ve performans verisi paylaşılıyor (tam liste
  Play sayfasında).
- Dürüst sınırlama listesi (Play metni) çok iyi bir örnek: sohbet sessizse,
  sohbet açıkken, bildirim kapalıysa, önizleme gizliyse, kurulumdan önce
  silindiyse, pil kısıtı varsa ÇALIŞMAZ. TekPanel'in onboarding'i için not.
- Arayüz: yeşil tema; sekmeler "Chats | Media | Watcher"; sohbet satırları
  (avatar + ad + son mesaj); üstte "Uygulamalar" düğmesi; "Doğrudan sohbet"
  (numarayı kaydetmeden WhatsApp'ta mesaj aç).

## C3) Unseen Messenger - Deleted Msg (Firehawk)

- Link: https://play.google.com/store/apps/details?id=com.unseen.messenger
- Paket: com.unseen.messenger
- Aynı geliştirici (Firehawk, Notisave'in sahibi). Kardeşi: "Unseen - No Last
  Seen" 10M+, puan 4,56 (388.327): https://play.google.com/store/apps/details?id=com.tda.unseen
- Puan: 4,44 (17.681). 5M+. Reklam + ürünler 0,99-99,99 dolar.
- Özellikler: bildirimlerden yedek, "tüm mesajlar tek yerde", galeri, arama
  ("kişi adı" veya "tam mesaj" seçerek), yedekten geri yükleme.
- Arayüz (mağaza görselleri): üstte "Tüm Mesajlar - 98 bildirim ve mesaj"
  özet satırı. Altında kişi/uygulama satırları: avatar + ad + son mesaj +
  saat + sarı okunmamış rozeti. Arama penceresi iki seçenekli. Uygulama
  seçiminde uzun onay kutusu listesi. Üstte uygulama sekmeleri (kapsül).
- Yorumlar: "mesaj yerine 'mesaj gönderdi' yazıyor" (2021-01), "yalnızca
  bildirim gelince kaydeder, uygulamadayken kaydetmez" (2020-11),
  "premium aldım, ilk günden sonra durdu, destek cevap vermedi" (2021-07).

# Grup D: birleşik gelen kutusu ve işletme mesajı

Bu gruptaki uygulamalar bildirimden değil, hesaba bağlanarak (bulut, resmi
API veya köprü) çalışıyor. Bu yüzden TekPanel'in "cihazda, internetsiz" yaklaşımı
ile farklı bir kategori. Yine de işletme sahibinin alternatifi bunlar.

## D1) Beeper: Every Chat in One App (Automattic)

- Link: https://play.google.com/store/apps/details?id=com.beeper.android
- Paket: com.beeper.android
- Konum: tüm sohbet uygulamalarını tek kutuda toplar, cevap yazdırır.
- Özellikler (Play metni): 15 uygulama (WhatsApp, Instagram, Messenger,
  Discord, Google Chat, Google Mesajlar, Google Voice, Line, LinkedIn,
  Signal, Slack, Telegram, Tumblr, Viber, X). Uygulama başına birden çok hesap.
  Tüm sohbetlerde arama (kişi, uygulama, resim, video, bağlantı, dosya).
  Aynı kişinin sohbetlerini uygulamalar arası birleştirme, etiketler,
  özel bildirimler. iPhone, Mac, Windows'ta da var.
- Fiyat: ücretsiz katman "5 hesaba kadar" (Play metni). Beeper Plus: 7 gün
  deneme, sonra ayda 9,99 dolar; toplam 10 hesap, ağ başına 3 hesap
  (site: https://www.beeper.com/pricing). Play ürünleri 9,99-499,99 dolar.
  TL mağazada 434,99-23.929,99 TL.
- Puan: 3,34 (5.041). İndirme: 1M+. Güncelleme: 13 Ekim 2026. İçerik: Teen.
- Veri güvenliği: paylaşım yok.
- Arka plan: Automattic Nisan 2024'te satın aldı (haberlere göre ~125 milyon
  dolar, resmi onay yok): https://www.engadget.com/the-owner-of-wordpress-has-bought-beeper-the-app-that-flipped-the-bird-to-apples-imessage-supremacy-170033314.html
  Temmuz 2025'te "On-Device" modeli ve Plus abonelik: https://alternativeto.net/news/2025/7/beeper-relaunches-with-on-device-model-new-features-and-text-com-merged-services
  Köprü (Matrix bridge) mantığıyla çalıştığı Reddit'te belirtilmiş:
  https://www.reddit.com/r/androidapps/comments/w1iw06/are_there_any_allinone_message_centers_for/
- Arayüz (mağaza görselleri): üstte "Inbox" başlığı ve yuvarlak avatarlı şerit
  (sabitlenmiş sohbetler gibi görünüyor). Altında sohbet listesi: avatar (köşede
  kanal rozeti) + ad + son mesaj + saat + mavi okunmamış sayısı.
  - Hesap ekleme: 15 uygulama simgesi ızgarası + "kalan ücretsiz hesap: 5".
  - "Aynı kişi, birçok uygulama, tek sohbet": üstte "Geçiş yap" paneli.
  - Etiket (label) ayrıntısı: renk paleti + simge seçimi. Alt panelde
    Inbox, Archive, Reminders, Requests, Low Priority + etiketli alanlar.
- Yorumlar (ortalama düşük, 3,47):
  - "Bağlantı sessizce kopuyor, bildirim gelmiyor" (2024-11, 2025-07,
    2026-01). Google Mesajlar sürekli kopuyor.
  - "Etiketler tamamen premium oldu, yılda 100 dolar" (2026-07).
  - "Yeniden bağlanınca sohbet geçmişi kayboldu" (2026-07).
  - "Üçüncü taraf uygulamaları engelleyen servislerle yarışamaz" (2026-08).
  - Seven: "tek yerde tüm sohbetler, yanıtlamak çok hızlı" (2024-12). Kurulum
    için bilgisayar gerekmesi şikayeti (2024-05).
- Neden önemli: "birleşik gelen kutusu" fikrinin en olgun hâli, ama köprü
  güvenilirliği ve hesap girişi yükü var. TekPanel girişsiz, bildirim tabanlı.

## D2) Meta Business Suite (Meta)

- Link: https://play.google.com/store/apps/details?id=com.facebook.pages.app
- Paket: com.facebook.pages.app
- Konum: Facebook sayfası ve Instagram hesabı için gönderi, mesaj, yorum, reklam.
- Özellikler: gönderi/story planla, mesaj ve yorumlara tek yerden cevap,
  otomatik cevap, istatistik, görev listesi, müşteriyle arama.
- Fiyat: ücretsiz. Puan: 4,61 (5.357.336). İndirme: 100M+.
- Veri güvenliği: ad, e-posta gibi kişisel bilgi paylaşılıyor.
- Arayüz (mağaza görselleri): "Inbox" ekranı: başlık + süz + ara + duyuru +
  üç nokta. Sekmeler: Mesajlar | Yorumlar. Satır: avatar (köşede Messenger /
  Instagram / WhatsApp rozeti) + ad + önizleme + gün + etiket ("Paid").
  Mağaza metni "Facebook, Instagram ve WhatsApp'ı tek yerden yönet" diyor;
  WhatsApp Business uygulamasının bu kutuya tam dahil olup olmadığı
  doğrulanamadı (üçüncü taraf özetleri çelişkili, resmî Meta belgesi
  bulunamadı):
  https://www.socialmediatoday.com/news/meta-announces-new-business-messaging-tools-including-improved-whatsapp-ad/623215
  https://sleekflow.io/en-gb/blog/whatsapp-in-meta-business-suite
- Yorumlar (ortalama 3,10, genel 4,61):
  - "Sayfaya gelen mesajlar 24 saatten fazla gecikiyor, mesajlarda arama yok,
    bildirimler temizlenmiyor" (2025-06, 1.699 faydalı).
  - "İşletme olduğumuz için kapatamadığım otomatik cevap var" (2025-05).
  - Taslak kaydı çalışmıyor (2026-07, 2.359 faydalı).
- Neden önemli: küçük işletmenin zorunlu geçtiği yer ve herkes şikayetçi.
  Bildirim ve mesaj gecikmesi, arama yokluğu TekPanel için açık kapı.

## D3) WhatsApp Business (WhatsApp LLC)

- Link: https://play.google.com/store/apps/details?id=com.whatsapp.w4b
- Paket: com.whatsapp.w4b
- Konum: küçük işletme için WhatsApp. TekPanel'in en önemli kaynak kanalı.
- Özellikler: hızlı cevap, uzakta mesajı, etiketler (süz ve bul), işletme
  profili, durum, uygulama içi sipariş ve ödeme.
- Puan: 4,60 (25.347.064). İndirme: 1B+. Ücretsiz, reklamsız.
- Veri güvenliği: paylaşım yok.
- Yorumlar (2026-08/09): numarayı Meta Business Suite'e bağlayınca gelen
  aramada "yeniden bağlanıyor" hatası (1.175 faydalı). "Doğrulama kodu
  gelmiyor, işim durdu" (489). Müşteri notları yalnızca "Notu düzenle"ye
  basınca görünür oldu (368).
- Dikkat: WhatsApp ve WhatsApp Business ayrı paket (com.whatsapp ve
  com.whatsapp.w4b). Kurucunun "kişisel ve işletme karışmasın" isteği
  paket adıyla ayrıştırılabilir. İncelediğim açıklamalarda bunu öne çıkaran
  bir rakip görmedim.

## D4) Shopify Inbox (Shopify)

- Link: https://play.google.com/store/apps/details?id=com.shopify.ping
- Paket: com.shopify.ping
- Konum: Shopify mağaza sohbeti. Müşteriyle yazış, ürün gönder, sepeti gör.
- Özellikler: mağaza ve Shop uygulamasından gelen sohbetleri tek yerde yönet,
  sohbette ürün/indirim gönder, otomatik mesaj, sepete eklemeyi gör, sohbeti
  personele ata, hızlı cevaplar.
- Fiyat: ücretsiz. Puan: 4,47 (7.977). İndirme: 500K+. Veri toplamıyor.
- Arayüz (mağaza görselleri): "Açık" liste: arama kutusu, satırda ad + önizleme +
  saat + durum etiketleri (Çevrimiçi, Ödeme, Ürün ayrıntısı, "Bana atandı").
  Sohbet: balon + ürün kartı. Hızlı cevaplar: kısa başlığa göre sıralı liste
  (Ürünler, Sipariş, Teşekkürler, Selamlama, Saatler). Müşteri profili:
  sepet ve sipariş özeti, "Not ekle".
- Yorumlar (ortalama 2,82): masaüstü bildirimi yok, müşteri resmi görünmüyor
  (2021-07). Uzun mesajda donuyor (2022-03). "Bildirimler sık sık gelmiyor,
  etkinleştirdiğimden beri kazandığımdan çok müşteri kaybettim" (2023-07).
- Dersi: bildirimin gelmemesi işletme için en pahalı hata.

## D5) respond.io Inbox (Respond.io)

- Link: https://play.google.com/store/apps/details?id=com.respond.inbox.io
- Paket: com.respond.inbox.io
- Konum: WhatsApp, Instagram, TikTok, ses ve e-postayı tek kutuda toplayan
  müşteri konuşma platformu (ekip + yapay zekâ ajanı).
- Özellikler: ortak gelen kutu, yapay zekâ ajanı, sohbet atama, iç yorum
  ve @bahsetme, aramalar, yapay zekâ ile cevap taslağı ve çeviri.
- Fiyat: uygulama ücretsiz, platform abonelik. Puan: 4,43 (353). 100K+.
- Arayüz (mağaza görselleri): sohbet başlığında kişi + arama + onay. Altında
  "AI Agent" ve "Hot Lead" etiketi. Dahili yorum turuncu kutu. Gelen aramada
  "Reddet / Kabul" bandı.
- Yorumlar: tek seferde tek resim gönderilebiliyor (2023-07). Yavaş (2024-08).
  Abonelik iptalinden sonra WhatsApp Business'ı telefona geri alamama
  (2025-06). Seven: "Wati'den geçtim, çok fark var" (2025-07).

## D6) WATI - Team Inbox for WhatsApp (Clare.AI)

- Link: https://play.google.com/store/apps/details?id=com.clareai.wati
- Paket: com.clareai.wati
- Konum: WhatsApp Business API ekip gelen kutusu (hesap şart).
- Özellikler: ortak ekip kutusu, yayın (broadcast), kampanya takibi, hızlı cevap.
- Puan: 3,67 (969). 100K+. Yorum örneklemi ortalaması 2,32.
- Arayüz (mağaza görselleri): üstte süzgeç kapsülleri: Tüm sohbetler,
  Aktif, Yayın, Atanmamış. Satırda durum etiketi: Çözüldü, Süresi doldu,
  Açık. Sohbette "/" yazınca hızlı cevap listesi. "Operatör seç" alt paneli
  (çevrimiçi / çevrimdışı). Durum: Açık, Beklemede, Kapalı.
- Yorumlar: "ödeme onaysız çekildi, otomatik yenilemeyi iptal edemezsiniz"
  (2021-12, 89 faydalı). Meta'nın yeni WhatsApp API kuralı sonrası bir hafta
  cevap veremedi (2025-11). Uygulama açılmıyor (2025-09).

## D7) Interakt for WhatsApp Business (Jio Haptik)

- Link: https://play.google.com/store/apps/details?id=com.haptik.interakt
- Paket: com.haptik.interakt
- Ortak ekip kutusu, sohbet atama, otomasyon. Puan: 3,83 (2.412). 100K+.
- Yorumlar (ortalama 2,25): "açık sohbetler bazen boş görünüyor" (2021-11),
  "PWA gibi yavaş, bildirim gecikmeli, kaçırılan sohbet" (2023-07),
  "sesli uyarı yok" (2025-08).

## D8) Callbell, Superchat, JivoChat (kısaca)

- Callbell: https://play.google.com/store/apps/details?id=com.callbell.mobilechat
  - WhatsApp, Messenger, Instagram, Telegram ortak kutusu; masaüstü hesabı
    şart. Puan 2,0 (209). Yorum: "WhatsApp entegrasyonu işe yaramadı, müşteri
    kaybettim" (2020-04). Seven: "küçük işletmede birden çok kişi aynı
    WhatsApp'ı kullanıyor" (2020-08).
- Superchat: https://play.google.com/store/apps/details?id=com.superxsuperchat
  - WhatsApp, Instagram, e-posta, Telegram, SMS, Messenger. Puan 4,08 (193).
    Yorum: "iptal etmek neredeyse imkânsız, tahsilata gönderildi, 109 euro"
    (2025-05).
- JivoChat: https://play.google.com/store/apps/details?id=com.jivosite.mobile
  - Web sitesi canlı sohbet + çok kanal. Puan 4,55 (23.983). 1M+. Yorum: giriş
    sonrası sonsuz dönüyor (2021-03); mesaj 10 sn geç görünüyor (2019-08).
- Ortak desen: kurulumu hesap/API istiyor, abonelik iptali zor, mobil uygulama
  masaüstüne göre zayıf ve bildirim gecikmeli.

## D9) All Messages - All Social App (360 Tool)

- Link: https://play.google.com/store/apps/details?id=socialallinoneapp.allsocialmediaapps
- Paket: socialallinoneapp.allsocialmediaapps
- Konum: "tüm sosyal uygulamalar tek yerde". Aslında web görünümlü tarayıcı.
- Özellikler: uygulama kısayolları ve web oturumları, iki hesap, parola kilidi,
  kullanım istatistiği.
- Fiyat: reklam + 2,99-11,99 dolar ürün. Puan: 4,52 (25.050). 1M+.
- Arayüz (mağaza görselleri): "My apps" ızgarası (simge + okunmamış rozeti),
  "Tüm uygulamalar" listesi (arama + kategori sekmeleri), açık uygulama
  sekmeleri yatay şeritte, parola kilidi tuş takımı.
- Yorumlar: "sadece uygulamaların klasörü, 2 hesap seçeneği yok" (2023-05,
  660 faydalı). "Giriş yapmadan altı reklam ekranı" (2024-04). "Sadece bir
  tarayıcı" (2023-12). Sonuç: kullanıcılar bildirimden tek kutu istiyor,
  ama bunu karşılamıyor.
- Benzer: Messenger (CloudMind), com.domobile.messenger, 5M+: "mesajın nerede
  olduğunu söylemiyor, tek tek uygulamalara girmem gerekiyor" (2020-03,
  1.155 faydalı). Tam kurucunun şikayeti: "tam mesaj görünmüyor".

## D10) Kısaca: Spike, SMS Organizer

- Spike (e-posta + ekip sohbeti): https://play.google.com/store/apps/details?id=com.pingapp.app
  - "Mesajı kişiye göre grupla" e-posta kutusu. 4,05 (19.296). 1M+.
    Yorumlar: özellikler ödeme duvarının arkasına taşındı (2022-09).
- SMS Organizer (Microsoft): https://play.google.com/store/apps/details?id=com.microsoft.android.smsorganizer
  - SMS'i otomatik düzenler, hatırlatma çıkarır, cihazda çalışır. 1M+. Son
    güncelleme Eylül 2024. Cihazda sınıflandırma fikrinin iyi örneği.
- Beklenmedik: eski APUS Message Center artık Play'de yok (yukarıda).

# Grup E: telefon-PC ve çok cihaz

## E1) Bridge - mirror notifications (XitLabs)

- Link: https://play.google.com/store/apps/details?id=com.xitlabs.bridge
- Paket: com.xitlabs.bridge
- Konum: bildirimleri, mesajları ve aramaları diğer telefon, tablet ve bilgisayarlara
  yansıtır; oradan cevaplar, kapatır, "okundu yap".
- Fiyat: 7 gün tam deneme, sonra ayda 0,99 dolar. Puan: 3,6 (1.035). 100K+.
- Veri: paylaşım yok.
- Arayüz (mağaza görselleri): yansıyan bildirim kartında "Okundu yap · Cevapla",
  cevapla yazma kutusu; uygulama ayarlarında "Tüm uygulamaları seç", "kara
  liste modu", "sistem uygulamalarını göster", uygulama arama kutusu ve
  her uygulama için göz simgesi + anahtar. Hızlı Ayarlar karosu.
  - Kurucunun "tümünü seç + uygulama arama" kalıbının örneği.
- Yorumlar: ikinci telefonda kurulumda "yetkisiz" (2023-03). Satın alma
  çalışmıyor, geliştirici cevap vermiyor (2023-02). Tarayıcıdan kapatınca
  telefonda kapanmıyor (2020-05, sonra düzeltilmiş olabilir).

## E2) Pushbullet

- Link: https://play.google.com/store/apps/details?id=com.pushbullet.android
- Paket: com.pushbullet.android
- Özellikler: telefon bildirimlerini PC'de gör, kapatınca telefonda da kapanır,
  WhatsApp gibi uygulamalara PC'den cevap, SMS, dosya ve bağlantı paylaşımı.
- Fiyat: ücretsiz + Pro (4,99-39,99 dolar). Puan: 4,23 (189.824). 5M+.
  Son güncelleme: Ağustos 2025.
- Tarihçe: 2015'te Pro planı (aylık 5 / yıllık 40 dolar) ve ücretsiz kotalar:
  https://cio.de/a/pushbullet-introduces-pro-plan-with-some-free-features-going-behind-the-paywall,3250742
  Güncel fiyatı doğrulayamadım (resmi sayfa yüklenmedi).
- Yorumlar: resim gönderimi sonsuza dek "indiriliyor" (2024-09).
  Bildirim saatlerce gecikiyor (2023-05). Aylık limit ve ücret tepkisi (2019-09).
- Resmi site: https://www.pushbullet.com/

## E3) KDE Connect (KDE Community)

- Link: https://play.google.com/store/apps/details?id=org.kde.kdeconnect_tp
- Paket: org.kde.kdeconnect_tp. Açık kaynak, GPL-3.0 (F-Droid sayfası):
  https://f-droid.org/en/packages/org.kde.kdeconnect_tp/
- Özellikler: bildirim eşitleme ve mesaja cevap, dosya aktarımı, pano, uzaktan
  kumanda; Wi-Fi üzerinden, uçtan uca TLS (Play metni).
- Resmi site: https://kdeconnect.kde.org/ ("Açık kaynak, ücretsiz")
- Puan: 4,03 (28.265). 1M+. Ücretsiz, reklamsız. Veri toplamıyor.
- Yorumlar: "bağlantı sürekli kopuyor" (2023-07, 2026-10). "Arka planda 7/24
  agresif çalışıyor" (2026-05). Depolama kısıtları dosya aktarımını zorlaştırıyor.

## E4) Link to Windows / Phone Link (Microsoft)

- Link: https://play.google.com/store/apps/details?id=com.microsoft.appmanager
- Paket: com.microsoft.appmanager
- Özellikler: Android bildirimlerini PC'de gör, SMS'e cevap, arama, fotoğraf.
  Android için PC ve telefonda aynı Microsoft hesabı şart. iPhone: bildirim
  ve mesaj sınırlı, "oturum tabanlı", telefon PC'ye bağlıyken geliyor.
  https://www.microsoft.com/en-us/windows/sync-across-your-devices
- Puan: 3,9 (1.311.500). İndirme: 1B+.
- Yorumlar: "bağlantı sorunları, yeni telefonu bağlayamıyorum" (2026-02,
  1.576 faydalı). Aramada yankı (2024-11).

# Grup F: yeni, küçük ve izlenecekler

## F1) NotiWeave: Notification Saver (Vianex) - en yakın konsept

- Link: https://play.google.com/store/apps/details?id=com.vianex.notiweave
- Paket: com.vianex.notiweave
- Durum: 100+ kurulum, güncelleme 5 Ekim 2026. Reklamsız, veri toplamıyor.
- Özellikler (Play metni): seçtiğin uygulamalardan bildirim toplar, GÖNDERENE
  GÖRE sohbet gibi başlıklar ("her kişi veya grup için bir sohbet"), aynı kişi
  uygulamalar arasında gruplanır, sohbet içinde arama ve tarih filtresi,
  "okundu yap" ve tek dokunuşla temizle, "tek tepside grupla" seçeneği,
  göndericiye özel zil sesi (20 ton).
- Arayüz (mağaza görselleri): alt çubukta 4 sekme: Threads, Apps, Block,
  Settings. Threads: uygulama satırları (WhatsApp 7 sohbet, Slack 3 sohbet...).
  WhatsApp'a girince kişi listesi (avatar + ad + son mesaj + saat + sayı).
  Sohbet ekranı: arama + "Tüm tarihler", gün başlıkları, "5 okunmamış mesaj"
  çizgisi, resimli mesaj. "Notifications" ekranı: okunmamış sayısı + "Temizle".
  "Apps": uygulama arama kutusu + "kullanımda" listesi (açık olanlar üstte).
- Fiyat: ücretsiz görünüyor; ürün yok. Yorum yok, bu yüzden güvenilirlik
  bilinmiyor. İzlenmeli.
- TekPanel ile ilişki: aynı fikir (kişi bazlı kutu, uygulama seçimi, tek
  ekran). Fark yaratmak için hız, kalite ve işletme odağı gerekir.

## F2) NotiRecall (RakshaLink Labs)

- Link: https://play.google.com/store/apps/details?id=com.notirecall.app
- 100+ kurulum. Ücretsiz katman: en çok 3 uygulama ve 7 gün; Pro ürün 7,99 dolar
  (tek seferlik görünüyor). Uygulama kilidi, bekletme süresi, hassas uygulamalar
  (banka, ödeme, parola) VARSAYILAN KAPALI, "yeni uygulama sen seçene kadar kapalı",
  "bildirim erişimi ve yakalama durumu" kontrolü. Reklamsız.
- AlternativeTo özeti (tek kaynak): https://alternativeto.net/software/notirecall/about/

## F3) Notification Vault (F-Droid)

- Sayfa: https://f-droid.org/en/packages/com.zygisk_enc.notivault/
  Kod: https://github.com/snap24/notification-vault. Lisans: GPL-3.0.
- Özellikler: tamamen çevrimdışı, sıfır internet izni, AES-256-GCM ile
  şifreleme, biyometrik kilit, şifreli yedek (bulut dahil), Quick Settings
  karosu, toast kaydı, bildirim görsellerini kaydetme, anahtar kelime/uygulama
  kuralları, istatistik paneli, saf siyah tema. Bildirim kaydında "açık kaynak
  ve çevrimdışı" konumlandırmasının en güçlüsü. (Kod okunmadı.)

## F4) AI Notifications (Summaries) ve benzerleri

- https://play.google.com/store/apps/details?id=cc.jamesturner.ai_notification_summaries
  (1K+, 4,4 / 19 puan). "NotifAI - AI Summary" 100+, "Cortex-AI Notification
  Manager" 500+. Cihazda mı bulutta mı özetlediği doğrulanamadı. Pazarda
  bildirim özetleme küçük ve yeni.

## F5) TekPanel: Sipariş Yönetimi (YYVZ LLC), AD ÇAKIŞMASI

- Link: https://play.google.com/store/apps/details?id=com.yyvz.tekpanel
- Paket: com.yyvz.tekpanel. Geliştirici sitesi: https://panel.mays.app
- Konum: Yemeksepeti, Getir, Trendyol Yemek, Migros Yemek hesaplarını tek
  ekranda yönetme (ürün aç/kapat, fiyat güncelle, yorum yanıtla).
- Durum: 10+ kurulum, güncelleme 29 Mayıs 2026, ürünler 64,99-999,99 dolar.
- İlişki: Play'de "Tek Panel" (tr-TR) araması bu uygulamayı ilk sırada veriyor.
  Aynı sektör (Türkiye'de küçük işletme), aynı vaat ("tek ekranda"). Marka ve
  mağaza adı çakışması riski. Hukuki bir değerlendirme yapmadım; TÜRKPATENT ve
  Play adı kontrolü yapılmalı.

## F6) Küçük Türk geliştiriciler ve hedef kitleye komşu uygulamalar

- Bookio (randevu yönetimi: berber, kuaför, klinik): 500+ kurulum.
  https://play.google.com/store/apps/details?id=com.randevunasil.business
  - Aynı hedef kitle, farklı problem. Mesajlar değil randevular.
- "Bildirim Takipçisi" (Troy Team, 50+), "Bildirim Gizle - Geçmişi Gör"
  (UzunlarPc, 50+), "Notification Reader" (OguzRobotics, 100+; kişiye göre
  gruplayıp sesli okur). Türkiye'den belirgin bir bildirim geçmişi rakibi yok.
  https://play.google.com/store/apps/details?id=com.sahan.notificationtracker
  https://play.google.com/store/apps/details?id=com.uzunlarweb.bildirimgecmisi
  https://play.google.com/store/apps/details?id=com.oguzrobotics.notificationreader
