# Google Play rakipleri: bildirim toplayıcılar ve gelen kutuları
Araştırma kolu: play-rakipler

Tarih: 20 Ekim 2026 (tüm sayılar bu tarihte Play'den okundu)

## Kısa özet

- Play'de 150'den fazla uygulama sayfası tarandı. 57'si aşağıda kartla
  anlatılıyor (Play linkli).
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
  - 47 uygulama için "en alakalı" 100 + "en yeni" 100 yorum (~8.000 satır).
  - Notisave için ayrıca 1.559 benzersiz yorum.
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
  Inbox (500+), Rasayel Inbox (1K+), respond.io, Spike. Hepsi bulut ve hesap
  tabanlı (WhatsApp Business API, e-posta, ekip kutusu); bildirimden değil.
  - https://play.google.com/store/apps/details?id=com.chatterapp
  - https://play.google.com/store/apps/details?id=com.icloudready.unified_inbox
  - https://play.google.com/store/apps/details?id=io.rasayel.app
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
- Fiyat: ücretsiz, reklamsız. Uygulama içi ürünler 1,99-5,49 dolar (ne olduğu
  doğrulanamadı).
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
- Veri güvenliği: paylaşım yok; uygulama etkinliği verisi toplanıyor.
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
- Veri güvenliği: paylaşım yok; bazı kişisel bilgi toplanıyor.
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
- Paket: com.nalarin.notifiltration. Uygulama veya kelimeyle engelle + geçmiş.
  3,64 (278). 1K+. Ürünler 1,99-19,99 dolar. Veri: çökme günlüğü paylaşılıyor.
  Arayüz ekran görüntüsü incelenmedi.
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
- Veri güvenliği: cihaz kimliği gibi veriler üçüncü taraflarla paylaşılıyor.
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
- Veri güvenliği: paylaşım yok; ad gibi kişisel bilgi toplanıyor.
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
- Veri güvenliği: paylaşım yok; uygulama etkinliği toplanıyor.
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
- Konum: Jio Haptik'in WhatsApp Business API ortak ekip kutusu. Hesap şart.
- Özellikler: ortak ekip kutusu, sohbet atama, hazır cevaplar, etiketler,
  otomasyon. Ürünler 0,49-21,99 dolar (plan türü belirsiz).
- Puan: 3,83 (2.412). İndirme: 100K+. Veri: çökme günlüğü paylaşılıyor.
- Arayüz (mağaza görselleri): üstte "Tüm sohbetler" açılır başlık + arama +
  süz. Sekmeler: Açık | Kapalı. Satır: avatar + ad + önizleme + renkli etiket
  (örn. "Ödeme sorunu", "Ürün teslim edilmedi") + saat. Süzgeç alt paneli:
  "Atanan" ve "Konuşma etiketi". "/" yazınca hazır cevap listesi ("/Sipariş
  teslimi", "/Yeni fiyat"). "Sohbeti ata" paneli: ekip listesi + arama.
- Yorumlar (ortalama 2,25): "açık sohbetler bazen boş görünüyor" (2021-11),
  "PWA gibi yavaş, bildirim gecikmeli, kaçırılan sohbet" (2023-07),
  "sesli uyarı yok" (2025-08).

## D8) Callbell, Superchat, JivoChat (kısaca)

- Callbell: https://play.google.com/store/apps/details?id=com.callbell.mobilechat
  - Paket: com.callbell.mobilechat. İndirme 50K+. Ücretsiz uygulama, hesap ve
    abonelik masaüstünden.
  - WhatsApp, Messenger, Instagram, Telegram ortak kutusu; masaüstü hesabı
    şart. Puan 2,0 (209). Veri: ses kaydı, mesaj, kişiler paylaşılıyor
    (Veri güvenliği sayfası). UI: ekran görüntüsü incelenmedi. Yorum: "WhatsApp entegrasyonu işe yaramadı, müşteri
    kaybettim" (2020-04). Seven: "küçük işletmede birden çok kişi aynı
    WhatsApp'ı kullanıyor" (2020-08).
- Superchat: https://play.google.com/store/apps/details?id=com.superxsuperchat
  - Paket: com.superxsuperchat. İndirme 50K+. Veri: paylaşım yok, bazı kişisel
    bilgi toplanıyor.
  - WhatsApp, Instagram, e-posta, Telegram, SMS, Messenger. Puan 4,08 (193).
  - Arayüz: "Tüm gelen kutuları" + arama. Sekmeler: Açık | Ertelenen | Bitti.
    Satırda avatar (köşede kanal rozeti), etiket kapsülleri (Pazarlama, Satış),
    önizleme, saat, kırmızı okunmamış noktası. Yazarken yapay zekâ menüsü
    (kısalt, yazım, ton, çevir).
  - Yorum: "iptal etmek neredeyse imkânsız, tahsilata gönderildi, 109 euro"
    (2025-05).
- JivoChat: https://play.google.com/store/apps/details?id=com.jivosite.mobile
  - Paket: com.jivosite.mobile. Web sitesi canlı sohbet + çok kanal.
  - Puan 4,55 (23.983). 1M+. Veri: cihaz kimliği reklam/analitik için paylaşılıyor.
  - Arayüz: alt çubukta 5 sekme: Inbox, Konuşmalarım, Ziyaretçiler, Ekip,
    Arşiv. Inbox satırı: renkli avatar + ad + önizleme + yeşil okunmamış sayısı.
    Üstte arama, telefon ve çevrimiçi/çevrimdışı anahtarı.
  - Yorum: giriş sonrası sonsuz dönüyor (2021-03); mesaj 10 sn geç görünüyor
    (2019-08).
- Ortak desen: kurulumu hesap/API istiyor, abonelik iptali zor, mobil uygulama
  masaüstüne göre zayıf ve bildirim gecikmeli.

## D9) All Messages - All Social App (360 Tool)

- Link: https://play.google.com/store/apps/details?id=socialallinoneapp.allsocialmediaapps
- Paket: socialallinoneapp.allsocialmediaapps
- Konum: "tüm sosyal uygulamalar tek yerde". Aslında web görünümlü tarayıcı.
- Özellikler: uygulama kısayolları ve web oturumları, iki hesap, parola kilidi,
  kullanım istatistiği.
- Fiyat: reklam + 2,99-11,99 dolar ürün. Puan: 4,52 (25.050). 1M+.
- Veri güvenliği: paylaşım yok olarak beyan ediyor.
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
  - Paket: com.pingapp.app. "Mesajı kişiye göre grupla" e-posta kutusu.
    4,05 (19.296). 1M+. Abonelik 7,99-144,99 dolar. Veri: e-posta adresi
    analitik için paylaşılıyor. Arayüz: sohbet görünümlü e-posta, okundu
    göstergesi; ekran görüntüsü ayrıntılı incelenmedi.
  - Yorumlar: özellikler ödeme duvarının arkasına taşındı (2022-09).
- SMS Organizer (Microsoft): https://play.google.com/store/apps/details?id=com.microsoft.android.smsorganizer
  - Paket: com.microsoft.android.smsorganizer. SMS'i otomatik düzenler,
    hatırlatma çıkarır, "kişisel veri çevrimiçi yüklenmez" diyor. 1M+. Son
    güncelleme Eylül 2024. Veri: paylaşım yok. Cihazda sınıflandırma fikrinin
    iyi örneği. Arayüz ekran görüntüsü incelenmedi.
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
  Son güncelleme: Ağustos 2025. Veri: paylaşım yok.
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
- Puan: 3,9 (1.311.500). İndirme: 1B+. Veri: paylaşım yok.
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
- Paket: com.notirecall.app. Veri: toplamıyor, paylaşmıyor.
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
  (paket aynı; 1K+, 4,4 / 19 puan; veri toplamıyor; ürünler 1,99-9,49 dolar). "NotifAI - AI Summary" 100+, "Cortex-AI Notification
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

# NOTISAVE DETAYI

## Kimlik ve rakamlar

- Ad: Notisave - Notification Log. Paket: com.tenqube.notisave
- Play: https://play.google.com/store/apps/details?id=com.tenqube.notisave
- Geliştirici (Play): Firehawk. Site: https://firehawk.ai/notisave
  - Paket adında "tenqube" geçiyor. Sahipliğin başka bir şirketten Firehawk'a
    geçtiği doğrulanamadı.
  - Firehawk'ın diğer ürünleri: Unseen Messenger (5M+) ve Unseen - No Last
    Seen (10M+, 4,56 puan, 388.327 puan sayısı).
- Puan 4,51 (104.915). İndirme 10M+ (sayfa verisinde ~43,8 milyon).
- Son güncelleme: 14 Ocak 2026. İçerik: Herkes.
- Fiyat: ücretsiz + reklam + uygulama içi ürünler 0,99-99,99 dolar
  (Türkiye mağazasında 4,99-829,99 TL).
- 2019'da MobileSyrup "Haftanın Uygulaması" yapmış:
  https://mobilesyrup.com/2019/04/21/drowning-in-notifications-notisave-app-android-review/
- iOS'ta aynı adla ayrı bir uygulama var ("Notisave: History Log", başka
  geliştirici, "100% Fake" diyen ilk yorum, reklamlı):
  https://apps.apple.com/app/id6748842428

## Tüm özellikleri (Play metni, site, basın, ekran görüntüleri)

- Tüm uygulamalardan gelen bildirimi otomatik kaydeder (bildirim erişimi ile).
- "Okunmamış" ve "Kaydedilen" ayrımı. Özel gruplar sekme olarak durur
  (görselde "Family", "Bank" gibi).
- Kişi bazlı konuşma listesi ("Messages"): avatar, ad, son mesaj, saat,
  sağda okunmamış sayısı. Konuşmaya girince sohbet balonları, gün başlıkları,
  arama kutusu.
- Silinen mesaj: görselde "This message was deleted." satırı.
- "Görüldü atmadan oku": önizlemeyi uygulamayı açmadan okuma.
- Arama; kelime, tarih, uygulamaya göre süzme (Play metni).
- Sessiz mod: bildirimi durum çubuğunda göstermeden kaydeder. Çubukta tek
  özet bildirimi kalır ("27 bildirim kaydedildi").
- Bildirim engelleme: "All apps" listesi, her uygulama için anahtar, arama.
  Açıklama: "Seçili uygulamaların bildirimleri durum çubuğunda engellenir".
- Kilit: parola ve parmak izi.
- Cevap yazma: Play metni söylemiyor ama yorumlar var. Kullanıcılar WhatsApp,
  Instagram, Messenger mesajlarına uygulamadan cevap yazabildiğini anlatıyor
  ("ilk uygulamayı açmadan cevaplıyorum", 2026-10; "son görülmem değişmeden
  cevaplıyorum", 2021-08; 2018-12).
  - Bazı sürümlerde cevap kutusu kayboldu (2020-02, 2022-05).
  - "Arka plandan kaldırınca eski mesajlara cevap veremiyorum, sadece yeni
    gelenlere" (2020-03). Çıkarım: cevap, canlı bildirimin cevap eylemine
    bağlı çalışıyor; bildirim yaşamı bitince cevap da bitiyor.
  - İstek/şikayet olarak da geçiyor (2019-03, 2020-05, 2020-07, 2025-03).
- Eski sürüm (2019): New / All / Chat / SNS / Shop sekmeleri, uygulama başına
  kart, kartta zil simgesi (muhtemelen sustur), "(15 mesaj)" sayacı, New'da onay
  işareti ile temizle, koyu tema (MobileSyrup).
- Yedek, çoklu cihaz eşitleme: Play metninde yok. Kullanıcılar istiyor.
- Web veya PC sürümü: bulunamadı.

## Ana ekran düzeni

Güncel arayüz (Play mağaza görselleri, 2026):
- Üst: başlık + arama simgesi.
- Sekmeler: Unread, Saved, özel gruplar.
- Liste: uygulama kartı veya kişi satırı. Satırda avatar, köşede uygulama
  simgesi, ad, tek satır önizleme, saat, sağda yuvarlak okunmamış sayısı.
- Sohbet ekranı: sol üstte geri, arama; balonlar; en altta gün ayracı.
- Görsellerde alt çubuk görünmüyor. Ayarlar ve engelleme listesi ayrı sayfa.
- Kilit ekranı: sayısal tuş takımı + parmak izi.

Eski arayüz ölçüleri (Play'deki 1080x1920 ham ekran görüntüsünden ölçtüm;
3x yoğunluk, 360x640dp varsayımı, tahmini):
- Üst bölge (durum çubuğu + başlık + sekmeler): ~143dp.
- İlk kart ~146dp'den başlar.
- Kart yan boşluğu 12dp. Kart genişliği ~335dp.
- Kart yüksekliği ~154-156dp (3 satır sohbet).
- Kartlar arası boşluk ~11-14dp.
- Renkler: zemin açık gri, kart beyaza yakın (yaklaşık #F1F2F4 ve #FDFDFD).
Güncel arayüz için tahmin (pazarlama görselinden, ±10%):
- Sohbet satırı aralığı ~80dp. Avatar çapı ~44dp.
- Pazarlama görseli gerçek ekranı birebir göstermeyebilir.

Dokununca ne oluyor:
- Bildirime dokununca ilgili uygulama açılır (MobileSyrup, 2019). Kurucunun
  "dokununca okundu sayılıp kayboluyor" şikayeti burada okunma, uygulama
  içindeki "okunmamış" mantığıyla ilgili olabilir (çıkarım, doğrulanamadı).
- Konuşmaya dokununca sohbet ekranı. Çoklu seçim için her konuşmaya tek tek
  dokunmak gerekiyor; "seç ve sürükle" isteği var (2023-02, 246 faydalı).

## Ayarlar ve filtreler (yorum ve görsellerden)

- Üç klasör mantığı (2019): Bildirimleri engelle, Bildirimleri kaydet, ve
  üçüncüsü. Yorumlar:
  - "Uygulama ekleme bir kez, sonra 'ekle' düğmesi kayboluyor" (2019-08, 902).
  - "Bazı uygulamalar kendiliğinden Engelle klasörüne düşüyor, ayarı
    değiştirsem de" (2021-02, 273 faydalı).
  - "YouTube engelli ama bildirim çubuğunda görünmeye devam ediyor" (2024-02).
- Kilit: "parola koyunca uygulama açılmıyor, boş ekran" (2022-12, 434; 2023-06;
  2023-11).
- Silme: tek tek seçerek. Toplu seçim zor (2023-02).
- Sesli/vibrasyon, bildirim sesi: bilgi bulunamadı.

## Ücretli özellikler

- Play metni hangi özelliğin ücretli olduğunu yazmıyor.
- Geliştirici yanıtı: "Reklamlar ücretsiz sürümde. Premium reklamsız. Premium
  daha fazla özellik açar" (2026-06):
  https://play.google.com/store/apps/details?id=com.tenqube.notisave
- Yorumlardan fiyat (2026): 3 günlük deneme, sonra HAFTALIK. Rakamlar farklı:
  - "$14,99 hafta" (2026-04, 2026-06), "14,99 sterlin hafta" (2026-10),
    "£14,99 / 20 dolar hafta" (2026-07), "14 CHF hafta" (2026-05),
    "€15 hafta ve VIP destek" (2026-05),
    "$18,99 hafta" (2026-02), "$23 hafta" (2026-10).
  - Eski "Ömür boyu Premium (reklamsız)" alanlar: güncellemeden sonra reklam
    görüyor ve "haftalık $4,99" istenmiş (2026-08).
  - "Premium reklamı kaldırmıyor" iddiası (2026-07) doğrulanamadı.
- Sonuç: fiyat bölgeye, teklife ve tarihe göre değişiyor. Tek kesin sayı yok.

## Kullanıcıların sevdiği şeyler

- Silinen mesajı okumak, görüldü atmadan okumak (2020-2025 yorumlarda çok).
- Uygulamadan cevap yazabilmek (WhatsApp'ı açmadan): 2018-12, 2021-08,
  2026-10 yorumları.
- Bildirim çubuğunu temizleyip kaydı saklaması: "yer açılıyor, önemli DM'ler
  kalıyor" (2019-04, 100 faydalı).
- Yıllarca arka planda sessiz çalışması: "4 yıldır kullanıyorum, sadece bir şey
  silinince açarım" (2024-10).
- Basit ve temiz arayüz. 2026'nın yeni yorumlarında 5 yıldız ağırlıklı:
  831 yeni yorumdan 649'u 5 yıldız (örnek sapmalı, doğrulanamadı).

## Sevmedikleri şeyler (1.559 yorum örneğinden)

Tema sayıları (kaba anahtar kelime taraması; bir yorum birden çok temada):
- "Kaydetmiyor, eksik, durdu": 441 yorum (~%28).
- Silinen mesaj konusu: 254 (~%16). Fotoğraf/medya: 136 (~%9).
- Gizlilik, kilit, izin, güven: 129 (~%8).
- Reklam: 43. Abonelik ve fiyat: 39 (2026'da artıyor: 2026'daki 104 düşük
  puanlı yorumun 20'si reklam, 19'u fiyat, 17'si "çalışmıyor").
Öne çıkan şikayetler:
- Fotoğraf: yerine "Sent a photo" yazıyor (2021-12, 2.112 faydalı); "yalnız
  metin yedekleniyor, medya gönderen silmediği sürece görünüyor" (2026-02).
  Ama 2018-12 ve 2020-08 yorumları "medyayı da kaydediyor" diyor; sürüme göre
  değişmiş olabilir, tutarsız.
- Bir süre sonra kayıt durur: "bir hafta çalıştı, sonra durdu" (2021-04,
  1.843 faydalı).
- Çubuğu izinsiz boşaltması: "kurunca tüm okunmamış bildirimlerimi sildi,
  uygulamada da göstermedi; ek bir yer istemiştim, yerine geçmesini değil"
  (2026-04). "Çubuğu boşaltması rahatsız edici" (2020-04, 299 faydalı).
- İzin döngüsü: "Bildirim erişimi iste, ayara götür, 'bu özellik cihazınızda
  yok' de, tekrar iste" (2020-07, 326 faydalı).
- Beyaz ekran: bildirim panelinden uygulamaya girince doğrulama, sonra beyaz
  ekran, hiçbir şey açılmıyor (2022-12, 434).
- Pil ve RAM: "~1 GB RAM, pil hızlı bitiyor" (2021-06), "pil" (2021-12).
- Ağır ve aldatıcı reklamlar (2026-06). Bitmeyen onay (consent) ekranları,
  "hepsinin seçimini kaldır" düğmesi yok (2026-05).
- Güven: "çerez onayı GDPR'a uymuyor, kullanıcı verisini 3. taraflara
  satıyor, aynı uygulamayı farklı adla yeniden yüklüyor" (2026-07, tek yorum,
  doğrulanamadı).
- Kötüye kullanım yorumları: "eski sevgilinin silinen mesajlarını okudum"
  (2021-05). Bu tür kullanım mağaza itibarı ve politika riski taşır.

## Sistem nasıl çalışıyor (çıkarım, kod okunmadı)

- Android'in bildirim dinleme servisini kullanır. Gelen her bildirim yerel
  veritabanına yazılır. Bu yüzden:
  - Uygulama açıkken bildirim oluşmazsa kayıt yok ("mesajlaşma uygulamasını
    kapat, sonra bildirimle gelsin", 2022-10 yorumu).
  - Bildirimde resim yoksa sadece "Sent a photo" kalır.
  - Silinen mesaj, o mesajın zaten kaydedilmiş bildirim kopyası olduğu için
    görünür. Kurulumdan önceki mesajlar yok.
- Bildirimi çubuktan kaldırıp tek özet bildirim bırakıyor (sessiz mod).
  Kullanıcılar bu agresifliği sevmiyor.

## Notisave'e karşı net fırsatlar

- Reklamsız ve haftalıksız fiyat. Fotoğraf ve medya. Çubuğu varsayılan
  olarak bozmama. Güvenilir kayıt durumu. İzin sihirbazı. Türkçe.
- Kişi/konuşma düzeni zaten var; farkı işletme odağı ve hız olmalı.

# Yorumlardan çıkan ortak sorunlar (sayılarla)

Düşük puanlı (3 yıldız ve altı) yorumlardaki tema oranları. Kaba tarama.
Örnek: uygulama başına ~200 yorum (en alakalı + en yeni).

- Bildirim geçmişi uygulamaları (~13 uygulama, 625 düşük puanlı yorum):
  - Kayıt eksik, durdu, çalışmıyor: 207 (%33).
  - Abonelik ve fiyat: 123 (%20). Gizlilik, izin, güven: 84 (%13).
  - Reklam: 80 (%13). Silinen mesaj beklentisi: 63 (%10).
  - Arka plan ve yeniden başlatma: 39 (%6). Çökme: 35 (%6). Medya: 23 (%4).
  - Arayüz ve tasarım: 20 (%3).
- Yönetici/filtre uygulamaları (~13, 494 düşük yorum):
  - Çalışmıyor: 142 (%29). Kural ve filtre sorunu: 95 (%19).
  - Fiyat: 92 (%19). Gizlilik: 85 (%17). Arayüz: 50 (%10). Çökme: 47 (%10).
- Silinen mesaj uygulamaları (3, 259 düşük yorum):
  - Çalışmıyor: 89 (%34). Silinen mesaj: 84 (%32). Fiyat: 53 (%20).
  - Medya: 43 (%17). Gizlilik: 34 (%13).
- Gelen kutusu ve işletme uygulamaları (~18, 1.389 düşük yorum):
  - Çalışmıyor/bildirim gelmiyor: 289 (%21). Çökme: 223 (%16).
  - Fiyat: 174 (%13). Gizlilik: 144 (%10). Medya: 123 (%9).
  - Cevap verme: 107 (%8). Arayüz: 83 (%6).
- Tüm 29 bildirim uygulamasında 3.684 yorumda ayrıca:
  - Medya/fotoğraf isteği %3,8. İzin sorunu %0,9. Yavaşlık/kasma %0,9.
  - Cevap verme: Notisave'in 1.559 yorumunda ~13 yorum (%0,8) hem seviyor
    hem özlüyor ("cevap kutusu kayboldu", "cevap yazabilsem 5 yıldız verirdim").
    Diğer uygulamalarda neredeyse hiç anılmıyor.
  - Yani kasma önemli ama kategorinin ana derdi değil; ana dert güvenilirlik,
    reklam ve fiyat. Cevap verme küçük ama gerçek bir talep.
- Aynı şikayet, birden çok bağımsız uygulamada tekrar ediyor (güvenilir bulgu):
  "bir süre sonra kayıt durur", "reklam ve haftalık fiyat", "fotoğraf yok",
  "izin tekrar kapanır", "tümünü seç yok".

# Fiyat modelleri (gözlenen)

- Haftalık abonelik: Notisave (3 gün deneme, ~15 dolar/hafta), Notification
  History Log (ZipoApps, ~13-15 dolar/hafta). Tepkiler çok sert.
- Aylık/yıllık/ömür boyu: BinaryScript (sadece dışa aktarma kilitli),
  Notification History (svprdga, aylık ve yıllık; tepki var), Norg (Premium:
  eski kayıt, reklamsız).
- Tek seferlik: BuzzKill 3,99 dolar (66,99 TL), Notification History Pro 1,99,
  Past Notifications Key 2,99, Notification Widget 5,99, FilterBox Pro ~4
  (kullanıcı beyanı), NotiRecall Pro 7,99.
- Ücretsiz ve reklamsız küçükler: Nap, DoNotNotify, Glimpse, NLog.
- Reklam: 12/20.
- Aylık: Beeper 9,99 dolar, Pushbullet 4,99 dolar, Bridge 0,99 dolar.
- Kullanıcı sinyali:
  - Reddit: "Ücretsiz veya tek seferlik olur. Abonelikse düşük olsun"
    (2025-01).
    https://www.reddit.com/r/androidapps/comments/1hqx5hv/app_that_stores_notification_history_and_can_view/
  - Play (NHL): "Tek seferlik bağış seçeneği olsa desteklerdim; haftalık 369
    peso (80 dolar/ay) delilik" (2025-04, 5 yıldız).
  - BuzzKill 5 yıldız yorumu: "reklamsız iyi uygulamaya para vermek güzel".
- Deneme: 3 gün + net fiyat ekranı sektörde yaygın (Notisave). Şikayet
  denemeye değil, deneme sonrası haftalık tahsilata ve "iptal yolu yok"a.
- TL çıpaları (Play TR, 20 Ekim 2026): BuzzKill 66,99 TL; FilterBox ürünleri
  90,99-164,99 TL; WAMR 104,99 TL; Notisave ürünleri 4,99-829,99 TL;
  BinaryScript 114,99-1.149,99 TL; Beeper 434,99-23.929,99 TL.

# Google Play dışı mecralar

## Android'in kendi bildirim geçmişi (en büyük "rakip")

- Android 11 ve üstünde: Ayarlar > Bildirimler > Bildirim geçmişi. Varsayılan
  KAPALI. Son 24 saati tutar. Açmadan önceki bildirimler yok. Parolayla
  korunmuyor.
  - https://9to5google.com/2022/04/13/what-is-notification-history/
  - https://www.slashgear.com/1401276/how-to-access-android-notification-log
- Resmî Google yardım sayfasını arama sonuçlarında bulamadım (doğrulanamadı);
  bilgiler teknoloji sitelerinden.
- Kullanıcılar bunu biliyor: "Android'in yerleşik geçmişi aynı işi yapıyor"
  diyen Play yorumu (2022-10), Reddit'te "sadece uygulama adını gösteriyor,
  bildirimi değil" (2020-08), "Samsung'da etkisiz" (2026-02).
- Çıkarım: rakiplerin satış cümlesi "24 saatten uzun, aranabilir, kişiye göre".
  TekPanel'in farkı bunun ötesinde olmalı (konuşma, medya, işletme).

## F-Droid (açık kaynak mağaza)

- Aramalar: https://search.f-droid.org/?q=notification+history ve benzerleri.
- Bildirim kaydeden gerçek uygulama sayısı çok az. Bulduklarım:
  - NotificationLog (de.jl.notificationlog): GPL-3.0 veya üstü. Bildirimleri
    yerelde kaydeder, görüntüler, dışa aktarır, siler. Kod Codeberg'de,
    son sürüm Ağustos 2025. AlternativeTo özetinde listenin başında geçiyor.
    https://f-droid.org/en/packages/de.jl.notificationlog/
    https://codeberg.org/jonas-l/notificationlog-android
  - Notification Vault: GPL-3.0, çevrimdışı, AES-256 şifreli.
    https://f-droid.org/en/packages/com.zygisk_enc.notivault/
  - DoNotNotify: MIT, kural ve geçmiş.
    https://f-droid.org/en/packages/com.donotnotify.donotnotify/
  - SpeakThat!: GPL-3.0, bildirimi sesli okur.
    https://f-droid.org/en/packages/com.micoyc.speakthat/
  - KyuubiMask: Apache-2.0, ekran paylaşırken bildirimi gizler.
    https://f-droid.org/en/packages/com.rtneg.kyuubimask/
  - KDE Connect: GPL-3.0 (E3 kartı).
- NLog (Stuttgart, Play'deki org.hcilab.projects.nlog) F-Droid'de yok (sayfa
  404). Reddit'te F-Droid'den önerilen "Notification Log" yukarıdaki
  de.jl.notificationlog (2020-08 yorumu, "FOSS, gizlilik sorunu yok").
- Çıkarım: "gizlilik odaklı, açık kaynak, çevrimdışı" bölümünde az sayıda küçük
  uygulama var. Kullanıcılar bunu arıyor ("açık kaynak daha iyi", Reddit
  2026-02). TekPanel kapalı kaynak olsa da veri güvenliği beyanı ve internet
  izni olmaması aynı kitleye hitap eder.
- Lisans notu: bu uygulamaların kodu bu kolda okunmadı, kopyalanmadı.

## AlternativeTo

- Doğrudan erişim engelli (403). Yalnızca arama sonucu özetlerinden bilgi:
  - "Notification History Alternatives": https://alternativeto.net/software/notification-history
  - "Notification History Log Alternatives": https://alternativeto.net/software/notification-history-log
  - NotiRecall: https://alternativeto.net/software/notirecall/about/
  - BuzzKill (AlternativeTo'da "$3"): https://alternativeto.net/software/buzzkill/about
- Özet: listelerde en üstte açık kaynak "NotificationLog" (F-Droid), ardından
  Notifications Recovery ve Notification History Log. WhatsRemoved+ de
  anılıyor. Bu özetler tek kaynak, ayrıntı doğrulanamadı.

## Product Hunt

- Doğrudan erişim engelli (403). Arama özetleri:
  - Notification History Log (2017): https://www.producthunt.com/products/notification-history-log
  - NotiBox (2016): https://www.producthunt.com/products/notibox
  - Pinnit (2024): https://www.producthunt.com/products/pinnit-pin-track-notifications
    Yorum: "Xiaomi Android 14'te geçmiş çöp bildirimle doluyor".
- Çıkarım: kategori yıllardır var, yeni "lansman" heyecanı düşük; Play araması
  ve Reddit önerisi daha belirleyici.

## Reddit (arşiv API'si ile okundu)

- Reddit'in kendi sitesine doğrudan erişim ve arama engelli veya sınırlı. Üçüncü
  taraf Reddit arşivi (arctic-shift) ile r/androidapps, r/Android,
  r/AndroidQuestions gönderileri okundu. Silinmiş (removed) gövdeler boş.
- Konular ve bulgular:
  - "Temiz bir bildirim geçmişi uygulaması?" (2026-02): "Galaxy S20'de yerleşik
    geçmiş yetersiz, açık kaynak olsun." Cevaplar: BuzzKill ve bir FOSS
    dinleyici. https://www.reddit.com/r/androidapps/comments/1r9fpvi/a_nice_and_clean_pplication_for_notification/
  - "Uygulamaya göre görüntüleme" (2025-01): istenen: hepsini sakla, uygulamaya
    göre süz, bazılarını yok say, kanal adını göster. "Ücretsiz veya tek
    seferlik, abonelikse düşük". Önerilenler: BuzzKill, FilterBox, Past
    Notifications, Samsung NotiStar.
    https://www.reddit.com/r/androidapps/comments/1hqx5hv/app_that_stores_notification_history_and_can_view/
  - "Bildirim geçmişi + paylaş menüsü" (2025-03): Nap, Pinnit, Recent
    Notifications, Notifications archive. Bir yorum: "paylaşım bildirimin
    ilk beş satırını alıyor" (hangi uygulama olduğu belirsiz).
    https://www.reddit.com/r/androidapps/comments/1jf0ums/looking_for_an_app_with_notification_history_and/
  - "Sistematik kaydet, kapat, uygulamaya göre süz" (2026-04): FilterBox ve
    BuzzKill önerildi; pil tüketimi şart.
    https://www.reddit.com/r/androidapps/comments/1srmlie/suggestions_for_apps_that_savelog_notification/
  - "Tüm mesajları tek yerde gösteren bir mesaj merkezi var mı?" (2022-07):
    "Beeper tam bu", DM Me. APUS Message Center "artık çalışmıyor".
    https://www.reddit.com/r/androidapps/comments/w1iw06/are_there_any_allinone_message_centers_for/
  - "Bildirim geçmişi Android'in az takdir edilen özelliği mi?" (r/AndroidQuestions,
    2026-02): "iOS'ta yok."
    https://www.reddit.com/r/AndroidQuestions/comments/1r1bw38/do_you_consider_notification_history_a_very/
- Piyasa doygunluğu: 2026'da r/androidapps ve r/Android'de en az 5 farklı
  küçük geliştirici "bildirim geçmişi uygulaması yaptım" gönderisi var
  (NotiWeave, ReNotify, Notisaver ve adı geçmeyenler; bazıları tekrarlı). Örnek:
  https://www.reddit.com/r/androidapps/comments/1x35h3t/i_made_my_notification_history_app_open_source/
  Çıkarım: fark yaratmayan "geçmiş" uygulaması ölüme mahkûm; işletme odağı ve
  kalite şart.

## XDA ve diğer forum/haber

- FilterBox tanıtım başlığı (kapalı): https://xdaforums.com/t/app-filterbox-powerful-notification-manager.4068543/
- Glimpse Notifications (yıllardır süren başlık): https://xdaforums.com/t/app-glimpse-notifications.3090575/
  Geliştirici "başka uygulamanın bildirimine dokunmam, sadece tetikleyici
  olarak kullanırım" diyor (güven dili).
- Notifications Logger (eski): https://xdaforums.com/t/app-4-0-notifications-logger.2859791/
- Hacker News'te anlamlı tartışma bulunamadı (Pinnit Show HN, 2024):
  https://news.ycombinator.com/item?id=41614548
- Basın: MobileSyrup (Notisave 2019), schieb.de (Almanca rehber),
  androidayuda.com, BGR:
  https://www.schieb.de/745993/benachrichtigungen-bei-android-dauerhaft-archivieren
  https://en.androidayuda.com/how-to-recover-a-notification-that-you-have-deleted-by-mistake-on-your-android/
  https://www.bgr.com/2259167/hidden-setting-android-deleted-messages/

## iOS karşılığı yok mu? Neden?

- Gerçek bir karşılığı yok. Sebepler:
  - iOS uygulama kum havuzu (sandbox): Apple mühendisi, başka uygulamaların
    mesaj ve çağrı kayıtlarına erişim API'si verilmediğini yazmış. SMS yalnızca
    istenmeyen mesaj filtresi gibi iki kısıtlı durumda ve kaydedilemez.
    https://developer.apple.com/forums/thread/818261
  - Başka uygulamanın bildirimini okuma konusunda doğrudan bir Apple belgesi
    bulamadım (doğrulanamadı). Sonuç aynı: App Store'da çalışan bir benzeri yok.
- İstisna: AB'de bildirimleri üçüncü taraf akıllı saatlere iletme (DMA
  zorunluluğu). iOS 26.3 beta'da test edildi; haberler kamuya iOS 26.5 ile
  açıldığını söylüyor (tarih doğrulanamadı). Tek cihaza, yalnız AB'de, aksesuar
  üreticisinin desteğiyle. Başka uygulamaların bildirim geçmişi değil.
  - https://heise.de/-10669113
  - https://macrumors.com/2025/12/22/ios-26-3-dma-airpods-pairing
  - https://mjtsai.com/blog/?p=50482
- "Notisave: History Log" adlı iOS uygulaması mevcut ama nasıl çalıştığı
  açıklanmıyor, ilk yorumu "100% Fake". Doğrulanamadı:
  https://apps.apple.com/app/id6748842428
- Beeper iOS'ta çalışıyor ama hesap girişi/köprü ile, bildirimden değil:
  https://www.beeper.com/
- Android'de olması: bildirim dinleme servisi ve kullanıcının açık izni var.
- TekPanel ile ilişki: iOS sürümü planlanmamalı. "iOS'ta yok, çünkü Apple
  izin vermiyor" cümlesi pazarlamada Android-özel avantaj olarak durabilir.

## Web ve PC tarafı

- Pushbullet (E2): bulut üzerinden PC'ye bildirim. Hesap ve sunucu gerekir.
- KDE Connect (E3): yerel ağ, uçtan uca TLS, açık kaynak. Gizlilik açısından
  TekPanel'e en yakın PC modeli.
- Microsoft Phone Link (E4): Microsoft hesabı ister, PC Windows 11.
  https://www.microsoft.com/en-us/windows/sync-across-your-devices
- Bridge (E1): ucuz abonelik, bildirimi başka telefon/PC'ye yansıtıp
  cevaplatıyor. https://play.google.com/store/apps/details?id=com.xitlabs.bridge
- AirDroid (bildirim yansıtma + dosya): https://www.airdroid.com/
- Join (João Dias): https://joaoapps.com/join/
- Beeper masaüstü ve web (hesap tabanlı): https://www.beeper.com/
- TekPanel ile ilişki:
  - "Hiç internet izni yok" vaadi ile bulut tabanlı PC eşitleme çelişir.
  - İleride istenirse yalnızca yerel ağ (QR ile eşleşen, KDE Connect gibi)
    düşünülmeli. İlk sürümde yapma.
  - Küçük işletme için PC'de görmek gerçek bir talep olabilir (Meta Business
    Suite'in şikayetleri bunu gösteriyor); ama bu ayrı bir ürün kararı.

## Diğer mağazalar ve ayna siteler (sadece bilgi sayfası)

- Samsung Galaxy Store: Good Lock / NotiStar (A13 kartı).
- Cafe Bazaar (İran), Notisave bilgi sayfası:
  https://cafebazaar.ir/app/com.tenqube.notisave?l=en
- apkcombo bilgi sayfası açılamadı (ağ erişimi engelli).
- Huawei AppGallery ve Xiaomi GetApps kontrol edilmedi (doğrulanamadı).
- Android 13+ "Kısıtlı ayarlar": Play dışından (APK ile) yüklenen uygulamalar
  bildirim dinleme iznini doğrudan açamıyor; kullanıcı "Kısıtlı ayarlara izin
  ver" demeli. Android 15'te daha sıkı. Kurucunun APK ile dağıtımı ve test için
  önemli. Play'den yüklenirse sorun olmaz.
  - https://9to5google.com/2024/09/12/android-15-sideloaded-apps-restrictions/
  - https://androidauthority.com/android-15-restricted-settings-sideloading-3481098
  - https://chromeunboxed.com/android-13-restricted-setting-feature-blocks-sideloaded-apps-from-notifications/

# Türkiye pazarı notları (Play TR aramaları, 20 Ekim 2026)

- "bildirim geçmişi" aramasında ilk 10 sonuç yerelleştirilmiş başlıklar
  kullanıyor ("Bildirim Geçmişi", "Bildirim Geçmişi Günlüğü"): svprdga,
  DroidNova, ZipoApps, ServeAny, LuckerDeveloper, BinaryScript, ThunderCraft,
  Lufesu, NotiHub. Geliştirici adlarına göre hiçbiri Türk geliştirici değil
  (adlardan çıkarım).
- "bildirim kaydedici": Tiny Falcon "Bildirim Kaydedici" (4,4) birinci, Notisave
  ikinci. "silinen mesajları gör": WAMR birinci ("WAMR: silinen mesajlar").
  "bildirim yöneticisi": BinaryScript birinci.
- "işletme mesaj paneli", "müşteri mesajları tek yerde", "whatsapp instagram
  mesajları tek ekranda" aramalarında anlamlı bir yerli ürün çıkmadı. Sonuçlar
  SMS uygulamaları, toplu mesaj göndericiler, WhatsApp Business ve Interakt.
  Yani Türkçe konuşan küçük işletme için "mesaj paneli" boş görünüyor.
- Yerli komşular: Bookio (randevu), TekPanel: Sipariş Yönetimi (sipariş),
  Trendyol Satıcı Paneli. Mesajlar ve bildirimler için yerli bir ürün yok.
- Türkçe aramalar şu kelimeleri gösteriyor: "bildirim geçmişi", "bildirim
  kaydedici", "silinen mesajlar", "bildirim yöneticisi". Mağaza başlığı ve
  açıklaması bunları içermeli.
- TL fiyat çıpaları "Fiyat modelleri" bölümünde.
- Ad çakışması: F5 kartı ("TekPanel: Sipariş Yönetimi").

# 20 uygulamanın özellik sayımı (madde madde)

Sayılan 20 bildirim uygulaması:
Notisave, Notification History Log (ZipoApps), Recent Notification,
Notification History Log (Arcangelo), Notifications archive, Norg,
Notification History (svprdga), NotiSaver (HexCoders), NLog,
Notification Manager - History (BinaryScript), Past Notifications,
Notification History Pro, BuzzKill, FilterBox, Nap, DoNotNotify, WAMR,
Auto RDM, Unseen Messenger, NotiWeave.

Nasıl sayıldı:
- Kanıt = Play metni, mağaza ekran görüntüsü veya yorum.
- Kanıt bulamadığım özellik SAYILMADI. Yani sayılar alt sınır.
- Uygulamaları çalıştırmadım, kod okumadım.

Sayımlar:
- Bildirim geçmişi kaydı: 20/20 (tanım gereği).
- Arama: 14/20.
  - Notisave, NHL-Zipo, NHL-Arcangelo, Archive, Norg, svprdga, HexCoders,
    BinaryScript, Past Notifications, NH Pro, FilterBox, Nap, Unseen, NotiWeave.
  - Yok veya kanıtsız: Recent Notification, NLog, BuzzKill, DoNotNotify, WAMR,
    Auto RDM.
- Uygulamaya veya tarihe göre süzme: 13/20.
  - Notisave, NHL-Zipo, Recent, Archive, Norg, svprdga, HexCoders, BinaryScript,
    Past Notifications, NH Pro, FilterBox, Nap, NotiWeave.
- Uygulamaya göre gruplu liste: 9/20.
  - Notisave, NHL-Zipo, Recent, Archive, Norg, HexCoders, BinaryScript,
    Past Notifications, NH Pro.
- Kişiye göre sohbet veya kişi listesi: 6/20.
  - Notisave, HexCoders, WAMR, Auto RDM, Unseen, NotiWeave.
  - Sohbet balonu görünümü kanıtlı: 4/20 (Notisave, HexCoders, WAMR, NotiWeave).
- Aynı kişiyi uygulamalar arası birleştirme: 1/20 (NotiWeave, kendi iddiası).
- Okunmamış/okundu durumu: 5/20.
  - Notisave, HexCoders, Unseen, NotiWeave, Norg (toplu okundu, Premium).
- Uygulama seçme veya kara liste: 19/20 (NLog hariç).
- "Tümünü seç / seçimi kaldır" kanıtı: 1/20 (NHL-Zipo). WAMR'da yok şikayeti.
  - Telefon-PC tarafında Bridge'de var (20'ye dahil değil).
- Seçilenleri listede üste alma ("kullanımda" bölümü): 1/20 (NotiWeave).
- Mesaj sayısına göre uygulama sıralama: kanıt bulunamadı, 0/20.
  - Sayaç gösterenler var (NH Pro, Past Notifications "Count"), sıralayan yok.
- Uygulama kilidi (parmak izi, yüz, parola): 3/20.
  - Notisave, BinaryScript, FilterBox.
- Dışa aktarma veya yedek: 9/20.
  - NHL-Zipo, NHL-Arcangelo, Archive, NLog, Norg, BinaryScript (abonelik),
    Nap, Auto RDM, Unseen.
- Ana ekran widget'ı: 5/20.
  - NHL-Zipo, Recent, Norg, NH Pro, FilterBox.
- Bildirimi çubuktan kaldırıp kayıtta tutma: 8/20.
  - Notisave, Archive, Norg, BinaryScript, BuzzKill, FilterBox, Nap, DoNotNotify.
- Kural, anahtar kelime, regex: 3/20 tam.
  - BuzzKill, FilterBox, DoNotNotify.
  - Kısmi: Past Notifications (kelime yok say), NH Pro (filtre), Nap (zaman).
- Fotoğraf/medya kaydı: 5/20 net.
  - Archive, NHL-Zipo, WAMR, Auto RDM, Unseen.
  - Notisave kısmi/tutarsız (yorumlar çelişiyor, yukarıda).
- Konuşma görünümü ve medya birlikte, reklamsız: 0/20.
  - Konuşma + medya olanlar (WAMR, Auto RDM, Unseen) hepsi reklamlı ve
    "silinen mesaj" odaklı.
- Uygulama içinden cevap verme: 1/20 kısmi (Notisave, yalnız yorumlara göre,
  canlı bildirim eylemiyle).
  - BuzzKill yalnız otomatik cevap kuralı. Norg'da kullanıcı istiyor (2020-11).
  - Dışarıda var: nBubble, Notification Widget, Bridge, Beeper.
- "Okundu yap" düğmesi (orijinal bildirimi okundu yapma): 0/20.
  - Dışarıda var: nBubble, Notification Widget, Bridge.
- Dokununca orijinal uygulamayı veya mesajı açma: 4/20 kanıtlı.
  - Notisave, Recent, Norg, NH Pro. Bozuk bildirilen: HexCoders, NLog.
- Kopyala veya paylaş: 6/20.
  - Recent, NHL-Zipo, NHL-Arcangelo, Archive, NLog, NH Pro.
- Favori, yıldız, sabitle: 4/20.
  - NHL-Zipo, Norg, BinaryScript, Nap.
- Eski kaydı otomatik silme (süre ayarı): 3/20.
  - Recent, svprdga, NH Pro.
- Yakalama durumu uyarısı: 1/20 (NHL-Zipo "yeni bildirim saklanamıyor").
- Reklam: 12/20.
  - Notisave, NHL-Zipo, NHL-Arcangelo, Archive, Norg, svprdga, HexCoders,
    BinaryScript (etiket), Past Notifications, WAMR, Auto RDM, Unseen.
- Veriyi 3. taraflarla paylaştığını beyan eden: 7/20.
  - Notisave, NHL-Zipo, NHL-Arcangelo, svprdga, HexCoders, Auto RDM, Unseen.
  - 13/20 paylaşmadığını beyan ediyor.
- "İnternet erişimim yok" açık iddiası: 4/20.
  - BuzzKill, Nap (tartışmalı), Recent, DoNotNotify.
- Çevrimdışı yapay zekâ: 1/20 (FilterBox, spam engeli).
- Tek seferlik ödeme seçeneği: 5/20.
  - BuzzKill, NH Pro, Past Notifications (anahtar), FilterBox, BinaryScript.
- Haftalık abonelik (yorumlarla doğrulanmış): 2/20 (Notisave, NHL-Zipo).
- İşletme/müşteri odaklı sunum (randevu, sipariş, müşteri): 0/20.

## Kimsede olmayan fırsat listesi

Not: "kimsede yok" derken şunu kastediyorum: incelediğim 20 bildirim
uygulamasında Play metni, görseller ve yorumlarda kanıt bulamadım.
Uygulama içini görmedim.

- İşletme sahibine dönük ürün (müşteri mesajı, sipariş, randevu dili): 0/20.
  - Şirket hesabını susturmak için BuzzKill kullanan bir işletmeci yorumu var
    (2024-09), yani ihtiyaç gerçek, ürün karşılığı yok.
- Panelde görünür "Cevapla" ve "Okundu yap" düğmeleri: geçmiş uygulamalarında
  yok (Notisave'de cevap yalnız yorumlarda anlatılıyor, "okundu yap" düğmesi
  kanıtı yok).
  - nBubble, Notification Widget, Bridge (ayrı kategori) açıkça yapıyor.
- Konuşma görünümü + medya + reklamsız bir arada: 0/20.
- Akıllı kanal seçici: Hepsini seç + arama + seçilenler üstte + mesaj sayısına
  göre sıra + paket adı yok. Parçalar farklı uygulamalarda var; hepsi birden
  hiçbirinde yok. Arcangelo paket adını gösteriyor, WAMR'da hepsini seç yok.
- Cihazda sınıflandırma (sipariş, randevu, şikayet, sorular): 0/20. Çevrimdışı
  yapay zekâ yalnız spam engeli için (FilterBox). Küçük AI özet uygulamaları
  (1K+ ve altı) var.
- Yakalama sağlığı: tek ekranda "çalışıyor mu" + tek dokunuşta düzelt + üreticiye
  özel pil rehberi. Parçalar var (NHL-Zipo uyarısı, Auto RDM sınırlama listesi,
  Glimpse self-test, NotiRecall durum kontrolü); bütünü yok.
- Haftalık abonelik olmayan, reklamsız, "ücretsiz başla, sonra aylık veya tek
  seferlik" net fiyat: kategoride kullanıcıların açıkça istediği ama sunulmayan
  şey (Reddit ve Play yorumları).
- Türkçe, yerel fiyat ve yerli işletme dili: yerli bir rakip yok.
- Cihaz üstü "hatırlat, cevap bekliyor" (BuzzKill yapabiliyor ama kural kurmak
  gerekiyor, "öğrenme eğrisi dik"): 1 tık sürümü yok.
- Kısıtlı ayar (Android 13+) ve izin döngüsü için adım adım, ekran görüntülü
  rehber: yorumlarda şikayet çok, çözüm sunan görülmedi (uygulama içini
  görmediğim için doğrulanamadı).

# Denenen ve çalışmayan yollar

- Play paket adı tahmini: "com.weawow.notisave" 404 verdi. Doğru adı Play
  aramasından buldum (com.tenqube.notisave). Tahmin yerine arama kullanıldı.
- apkcombo.com: DNS çözülemedi (araç ve curl ile ulaşılamadı). Zaten APK
  mirror'dan indirme yapılmayacaktı. Bilgi sayfası da açılmadı.
- AlternativeTo, AppBrain, Product Hunt, APKPure, chrome-stats, Medium: doğrudan
  erişimde 403 (bot koruması). Çözüm: arama özetlerini kullandım; bu yüzden
  o bilgiler "tek kaynak" etiketli.
- Reddit:
  - Arama aracı reddit.com alanını engelledi (hata).
  - reddit.com JSON ve old.reddit.com: 403. RSS: art arda denemede 429.
  - pullpush.io: 429.
  - Çalışan yol: arctic-shift Reddit arşivi API'si. Eksik: silinmiş
    gönderi gövdeleri ("[removed]") okunamadı.
- Lifehacker alanı arama aracında engelli; BuzzKill basın alıntıları Play
  metninden ve geliştirici sitesinden, bağımsız doğrulanamadı.
- exodus-privacy (izin/izleyici raporu): ağa çıkılamadı veya API kimlik
  doğrulaması istedi. Gerçek izin listesine bu yüzden ulaşamadım; Play web
  sayfasında da izin listesi yok. Sadece Veri güvenliği bilgisi var.
- Pushbullet Pro sayfası yüklenmedi; güncel fiyat doğrulanamadı.
- Hacker News (Algolia) ve StackExchange API çalıştı ama anlamlı sonuç
  vermedi.
- Play yorumları: resmi API yok. Web arayüzünün kullandığı ucu kullandım;
  bu uç belgesiz, ileride değişebilir.
- Play aramalarında "Flowbell": alakasız sonuç. Aynı adlı bir bildirim
  uygulaması bulunamadı.
- Google'ın resmi "Bildirim geçmişi" yardım sayfası arama sonuçlarında
  çıkmadı; teknoloji sitelerine dayandım.
- Ekran görüntülerinden "tıklayınca ne oluyor" bilgisi sınırlı: mağaza
  görselleri tek kare. Dokunma davranışını yorumlardan ve metinden çıkardım.
  Gerçek kullanım testi için uygulamaların kurulması gerekir (yapmadım).

# Doğrulanamayanlar

- Notisave'in Premium'unda tam olarak hangi özelliklerin kilitli olduğu.
- Notisave'in güncel haftalık fiyatı (yorumlarda 4,99 ile 23 dolar arası).
- "Notisave sahipliği Tenqube'den Firehawk'a geçti mi" sorusu.
- Play'deki "gerçek kurulum" sayı alanının anlamı (sıralama için kullandım).
- BuzzKill'in TIME ödülü ve Android Authority / Lifehacker alıntıları.
- BuzzKill'in izin listesi (tek kaynak, üçüncü taraf özet).
- Nap ve FilterBox'ın "internet yok / çevrimdışı" iddiaları (yorumlar çelişiyor).
- Meta Business Suite gelen kutusunun WhatsApp Business uygulamasıyla tam
  entegrasyonu.
- Pushbullet'ın güncel fiyat ve kotaları.
- Samsung NotiStar'ın güncel durumu (kaldırıldı mı).
- iOS tarafında Apple'ın başka uygulamanın bildirimini okumaya dair resmi
  belgesi ve AB bildirim iletme özelliğinin kesin yayın tarihi.
- BinaryScript'in "reklam yok" metni ile Play'deki "reklam içerir" etiketi.
- Notistar (MakLab) ve Samsung NotiStar arasında bir bağ var mı.
- Huawei AppGallery ve Xiaomi GetApps'te rakip durumu.
- Yorum tema oranları kaba anahtar kelime sayımıdır; rastgele örnek değil.

# TekPanel için çıkarımlar

## Ne yapalım

- Yakalama güvenilirliğini 1 numaralı özellik yap.
  - Neden: düşük puanlı yorumların %21-34'ü "kayıt durdu, bildirim gelmiyor"
    (Notisave %28). Auto RDM'in kendi sınırlama listesi bunun nedenlerini
    sayıyor.
  - Nasıl: ana ekranda küçük "Yakalama: çalışıyor / durdu" göstergesi, durunca
    tek dokunuşla düzelt; yeniden başlatma sonrası otomatik toparlanma; pil
    muafiyeti için cihaz markasına özel kısa rehber; test bildirimi.
  - Örnekler: Glimpse "self-test", NotiRecall "yakalama durumu".
- Orijinal bildirime varsayılan olarak DOKUNMA (silme).
  - Neden: Notisave kullanıcıları "okunmamışlarımı sildi, ek yer istemiştim"
    diyor (2026-04); Reddit: "ek bir yer, yerine geçen değil".
  - Çubuğu temizleme seçenek olsun, kapalı başlasın.
- Fotoğrafı ve görseli sakla, dokununca büyüt.
  - Neden: Notisave'de medya tutarsız ("Sent a photo", 2.112 faydalı).
    Net olarak 5/20 rakipte var. Yorumların %3,8'i medya istiyor.
- Kişi bazlı tek kutu, kutuya dokununca içindeki mesajlar genişlesin.
  - Rakip kalıpları: Notisave'de uygulama kartı ve içinde "Yoda (15 mesaj)";
    Norg'da satırda aşağı ok; Archive'da galeri; NotiWeave'de "Threads".
  - Kurucunun isteği (tek kutu, üç dokunuşluk açılım) Norg'un alt paneli +
    Notisave'in kart kalıbının birleşimi gibi düşünülebilir.
- "Cevapla" ve "Okundu" düğmeleri kartın içinde, kullanıcı ayrı basınca çalışsın.
  - Neden: nBubble ve Notification Widget bunu yapıyor (yorumlarda sevilen);
    Notisave kullanıcıları da "WhatsApp'ı açmadan cevap" özelliğini seviyor,
    kaybolunca şikayet ediyor (2020-02, 2022-05).
    nBubble'da "dokununca WhatsApp açılıp mavi tik atıyor" şikayeti, kurucunun
    "dokununca okundu sayılıyor" şikayetiyle aynı.
  - Not: bunlar orijinal bildirimin eylemleri; bildirim silinmişse veya
    uygulama arka plandan kaldırılmışsa eski mesaja cevap çalışmayabilir
    (Notisave 2020-03 yorumu; çıkarım, test edilmeli). Orijinali silmeme
    kararı bunu da korur.
- Kanal (uygulama) seçicisi akıllı olsun.
  - Hepsini seç ve seçimi kaldır (NHL-Zipo, Bridge), uygulama arama, açık
    olanlar üstte (NotiWeave "kullanımda"), çok mesaj alan üstte, paket adı
    gösterme (Arcangelo gösteriyor, kötü), cihazdaki gerçek simgeler.
  - Neden: WAMR yorumu "tümünü seç yok, tek tek işaret" (786 faydalı).
- İlk açılışı zorunlusuz yap; izin ekranını kısa tut.
  - Neden: Notisave "izin iste, ayara götür, 'özellik yok' de, tekrar iste"
    (326 faydalı) ve izin sonrası tekrar sorma yorumları.
  - Android 13+ "Kısıtlı ayarlar" yalnız Play dışı kurulumda çıkar; APK ile
    test veren kişiler için kısa, görsel rehber hazırla.
  - Auto RDM'in "çalışmadığı durumlar" listesi gibi dürüst ve kısa bir not.
- Fiyatı kategorinin hatasına karşı konumla.
  - Haftalık abonelik yok, reklam yok. Neden: reklam ve haftalık fiyat 2026'da
    Notisave'in düşük puanlarının başında.
  - 3 gün deneme sektörde yaygın; şikayet denemeye değil, sonrasındaki
    haftalık tahsilata ve iptal zorluğuna. Deneme sonunda fiyatı net göster,
    iptali kolay yap.
  - Aylık/3/6/12 aylık plana ek olarak tek seferlik veya ömür boyu seçeneğini
    ve gönüllü destek seçeneğini değerlendir. Neden: Reddit "ücretsiz veya tek
    seferlik, abonelikse düşük"; BuzzKill tek seferlik ve en yüksek puanlı;
    NHL kullanıcısı "bağış seçeneği olsa desteklerdim".
  - TL çıpaları: BuzzKill 66,99 TL tek sefer, FilterBox 91-165 TL, WAMR 105 TL.
    Kurucunun ayda 5 dolarlık planı bu çıpalara göre pahalı algılanabilir.
    Kurucunun 20'şer kişilik iki sürümlü deneme fikri (biri uyarısız) algıyı
    ölçebilir; fiyat için ayrı bir deney gerekir.
- Gizliliği kanıtlanabilir yap.
  - "Veri toplamıyor, paylaşmıyor" Veri güvenliği beyanı + internet izni yok.
  - Neden: rakiplerin 7/20'si veri paylaştığını beyan ediyor; yalnız 4/20
    "internet yok" diyor; güven yorumları var (Glimpse'e övgü).
- Mağaza metnini Türkçe aranan kelimelerle yaz.
  - "bildirim geçmişi", "bildirim kaydedici", "bildirim yöneticisi".
  - İşletme dili: "müşteri mesajlarını kaçırma, tek ekranda gör".
  - "Silinen mesajı gör" çok aranıyor (WAMR 100M+) ama ana mesaj yapma:
    kötüye kullanım yorumları (eski sevgili vb.) ve politika/itibar riski var.
- Ad çakışmasını çöz.
  - Play'de "TekPanel: Sipariş Yönetimi" (YYVZ LLC) zaten var, aynı sektör,
    Türkiye. Marka taraması (TÜRKPATENT) ve alternatif ad önerisi gerekli.
  - Benzer ad kalabalığı rakiplerde sorun: "Notisave" adı en az 4 listede taklit.
- Cihaz test matrisine Samsung (S21/S23), Xiaomi, OnePlus, Motorola ve Pixel,
  Android 12-15 ekle.
  - Neden: şikayetler bu cihazlarda yoğun (BuzzKill S21 Ultra, Notistar S23
    Ultra, Notification Widget Xiaomi, Notisave Moto/Gionee).
- Küçük ama sevilen ekler: geri al (BuzzKill, FilterBox), hatırlat (BuzzKill),
  hassas uygulamalar varsayılan kapalı (NotiRecall), uygulama kilidi (3/20),
  kopyala ve paylaş (6/20; kurucu uzun basınca kopyala istiyor).
- Yakın rakipleri izle: NotiWeave (kişi bazlı sohbet kutuları), Norg (alt panel),
  BinaryScript (ücretsiz çekirdek + sadece dışa aktarma ücretli).
- Android'in 24 saatlik yerleşik geçmişinin üstüne çık: uzun saklama, konuşma,
  medya, eylemler. Kullanıcı "yerleşik olan yeter" diyebiliyor (2022-10).

## Ne yapmayalım

- Haftalık abonelik, sert ödeme duvarı, reklam, "puan ver" zorlaması.
  - Neden: Notisave, NHL, BinaryScript, Notifiltr yorumlarında en sert tepkiler.
  - Ödeme yapana reklam göstermek: "Ömür boyu aldım, şimdi reklam var"
    (Notisave 2026-08).
- Karmaşık kural motorunu ilk sürüme koyma.
  - Neden: BuzzKill "öğrenme eğrisi dik", FilterBox "ilk bakışta bunaltıcı";
    kurucu da sade ekran istiyor.
- İlk sürümde internet izni ve bulut/PC eşitleme ekleme.
  - Neden: "hiç internet izni yok" vaadi güçlü bir fark; Nap ve FilterBox
    iddiası yüzünden şüphe yaşadı. PC talebi gerçek olabilir, ama ayrı karar
    (KDE Connect gibi yerel ağ modeli).
- iOS sürümü planlama. Apple başka uygulamanın bildirimini okutmuyor.
- Bildirimi sessizce silme, kurulumda kullanıcı onayı olmadan veri boşaltma.
- APK mirror ile rakip analizi. Zararlı yazılım ve telif riski.
- Rakip arayüzlerini kopyalama. Bu kolda yalnız kalıplar öğrenildi; tasarım
  kimliği ayrı üretilmeli.

## Neden bu sırayla

- En çok şikayet: güvenilirlik > reklam ve fiyat > eksik medya > izin
  karmaşası. Cevap verme az anılıyor (Notisave'de ~%0,8) ama anılınca güçlü
  (seviyor, kaybolunca şikayet ediyor). İşletme kullanımı için mantıklı;
  ürünün işletme konumu ayrıca doğrulanmalı (örneğin 20 kullanıcıyla deneme).
- Kimsede olmayan fırsat alanı: işletme odağı + konuşma + medya + dürüst fiyat.
